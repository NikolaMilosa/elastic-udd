<template>
    <div class="whole">
        <h1>Statistics - {{ mode.mode_desc }}</h1>
        <select class="stats-select" @change="changeMode($event)">
            <option value="city">City statistics</option>
            <option value="agent">Agent statistics</option>
            <option value="company">Company statistics</option>
        </select>
        <div class="leaderboard">
            <div class="column">
                <div class="bar third">
                    <p>{{ places[2].value }}</p>
                </div>
                <p>{{ places[2].name }}</p>
            </div>
            <div class="column">
                <div class="bar first">
                    <p>{{ places[0].value }}</p>
                </div>
                <p>{{ places[0].name }}</p>
            </div>
            <div class="column">
                <div class="bar second">
                    <p>{{ places[1].value }}</p>
                </div>
                <p>{{ places[1].name }}</p>
            </div>
        </div>
    </div>
</template>
  
<script>
export default {
    name: 'HomeComp',
    data() {
        return {
            modes: {
                city: {
                    type: 'city',
                    mode_desc: 'Most requests per city'
                },
                agent: {
                    type: 'agent',
                    mode_desc: 'Most successful requests per agent'
                },
                company: {
                    type: 'company',
                    mode_desc: 'Most successful requests per company'
                }
            },
            mode: {
                type: 'city',
                mode_desc: 'Most requests per city'
            },
            places: [
                {
                    name: '',
                    value: ''
                },
                {
                    name: '',
                    value: ''
                },
                {
                    name: '',
                    value: ''
                }
            ]
        }
    },
    created() {
        this.getStats()
    },
    methods: {
        changeMode: async function(event) {
            let mode = event.target.value;
            if (mode == 'city') {
                this.mode = this.modes.city
            }
            else if (mode == 'agent'){
                this.mode = this.modes.agent
            }
            else if (mode == 'company') {
                this.mode = this.modes.company
            }

            this.getStats()
        },

        getStats: function() {
            let vue = this;
            fetch('http://localhost:8080/api/query/statistics')
                .then((response) => response.json())
                .then((data) => {
                    let obj;
                    if (vue.mode.type == 'city'){
                        obj = data.city_stats
                    }
                    else if (vue.mode.type == 'agent'){
                        obj = data.agent_stats
                    }
                    else if (vue.mode.type == 'company'){
                        obj = data.company_stats
                    }

                    var items = Object.keys(obj).map(function(key) {
                        return [key, obj[key]];
                    });

                    items.sort(function(first, second) {
                        return second[1] - first[1];
                    });

                    vue.places = []
                    for (let place of items.slice(0,3)){
                        vue.places.push({
                            name: place[0],
                            value: place[1]
                        })
                    } 
                })
        }
    }
}
</script>
  
<style scoped>
.stats-select {
    width: 250px;
    height: 50px;
    outline: none;
    border: none;
    padding-left: 15px;
    padding-right: 15px;
    margin-bottom: 60px;
}
.leaderboard {
    height: 400px;
    width: 600px;
    margin: auto;
    display: flex;
    flex-direction: row;
    align-items: flex-end;
    justify-content: space-around;
}
.column {
    height: 100%;
    max-width: 180px;
    display: flex;
    flex-direction: column-reverse;
}
.column > p {
    margin-bottom: 30px;
    font-size: 18px;
}

.bar {
    width: 180px;
    background-color: #A675A1;
}
.first {
    height: 150px;
}
.second {
    height: 110px;
}
.third {
    height: 75px;
}
</style>
  