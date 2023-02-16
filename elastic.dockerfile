FROM elasticsearch:7.4.0

COPY ./config/serbian-analyzer/build/distributions/serbian-analyzer-1.0-SNAPSHOT.zip /usr/share/elasticsearch/serbian-analyzer/serbian-analyzer-1.0-SNAPSHOT.zip

RUN ./bin/elasticsearch-plugin install file:/usr/share/elasticsearch/serbian-analyzer/serbian-analyzer-1.0-SNAPSHOT.zip

# Smoke test

RUN ./bin/elasticsearch-plugin list

CMD ["eswrapper"]