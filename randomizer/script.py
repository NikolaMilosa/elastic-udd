import requests;
import argparse;
from pytimeparse.timeparse import timeparse;
import random;
import logging;
import time;
import sys;

def get_args():
    parser = argparse.ArgumentParser(description="Simple script for gradually populating logs for statistics")
    parser.add_argument("--url", dest="url", default="http://localhost:8080/api/log", help="url of logs endpoint")
    parser.add_argument("--frequency", dest="frequency", default="10s", help="how often should a log request be made")
    return parser.parse_args()

def get_logger():
    FORMAT = '[%(process)d] [%(asctime)s] %(levelname)-8s %(message)s'
    logging.basicConfig(format=FORMAT,level=logging.INFO)
    return logging.getLogger('logGenerator')

def countdown(time_sec):
    while time_sec:
        mins, secs = divmod(time_sec, 60)
        timeformat = 'Submitting again in: {:02d}:{:02d}'.format(mins, secs)
        print(timeformat, end='\r', file=sys.stderr)
        time.sleep(1)
        time_sec -= 1

def main():
    logger = get_logger()
    args = get_args()
    url = args.url
    frequency = timeparse(args.frequency)
    logger.info(f'Starting log generator that will log every {frequency} seconds to {url}')

    firstname_pool = ['Nikola', 'Jovan', 'Dragan', 'Petar']
    lastname_pool =  ['Nikolić', 'Jovanović', 'Dragić', 'Petrović']
    outcome_pool = [True, False]
    city_pool = ['Novi Sad', 'Beograd', 'Niš', 'Kragujevac', 'Čačak']
    companies_pool = ['Sotex', 'Vega', 'Valcon', 'Schneider-Electric DMS']

    while True:
        try:
            response = requests.post(url, json={
                'agent': f"{random.choice(firstname_pool)} {random.choice(lastname_pool)}",
                'successful': random.choice(outcome_pool),
                'company': random.choice(companies_pool),
                'city': random.choice(city_pool)
            }, headers= {
                'Content-Type': 'application/json'
            })
            if response.status_code == 200:
                logger.info('Successfully posted a log entry.')
            else:
                logger.warning(f"Couldn't post a log entry with message: {response.text}")
        except:
            logger.warning(f"Couldn't reach server...")
        countdown(frequency)

if __name__ == "__main__":
    main()