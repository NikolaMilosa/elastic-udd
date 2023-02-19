<template>
    <div class="whole">
        <h1>Query users</h1>
        <div class="search-input">
            <div>
                <div>
                <span>Field:</span>
                <select v-model="current.field">
                    <option>firstname</option>
                    <option>lastname</option>
                    <option>education</option>
                </select>
                </div>
                <div>
                    <span>Value:</span>
                    <input type="text" v-model="current.value">
                </div>
                <div>
                    <span>Operator:</span>
                    <select v-model="current.operator">
                        <option value="and">AND</option>
                        <option value="or">OR</option>
                    </select>
                </div>
                <!-- <div style="width: 100px;">
                    <span>Phraze</span>
                    <input type="checkbox" v-model="current.isPhrase">
                </div> -->
            </div>
            <div class="buttons">
                <button class="add" @click="add">Add</button>
                <button class="remove" @click="reset">Reset</button>
                <button class="execute" @click="execute">Execute</button>
            </div>
        </div>
        <div class="query">
            {{ query_string }}
        </div>
        <div class="output">
            <div v-for="user in users" v-bind:key="user.id">
                <span>{{ user.firstname }}</span>
                <span>{{ user.lastname }}</span>
                <span>{{ user.email }}</span>
                <span>{{ user.address }}</span>
                <span>{{ user.phone }}</span>
                <span>{{ user.education }}</span>
            </div>
        </div>
    </div>
</template>
  
<script>
export default {
    name: 'QueryUsersComp',
    data() {
        return {
            query_string: '',
            query: [],
            current: {
                field: 'firstname',
                value: '',
                operator: 'and',
                isPhrase: false
            },
            users: []
        }
    },
    methods: {
        add: function() {
            let error = new Array();
            if (this.current.value == ""){
                error.push("Value is mandatory");
            }
            
            if (error.length != 0){
                this.showMessage(error.join('<br/>'), "error");
                return
            }

            this.query.push(JSON.parse(JSON.stringify(this.current)));
            this.buildQueryString();
            this.current.value = "";
        },

        reset: function() {
            this.query = new Array()
            this.query_string = "";
        },  

        execute: function() {
            if (this.query.length == 0) {
                this.showMessage('empty query', 'error')
                return;
            }
            let vue = this;
            fetch('http://localhost:8080/api/query/advanced/0/client', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ query: vue.query})
            })
            .then((response) => {
                if (response.status != 200){
                    response.text().then((data) => vue.showMessage(data, 'error'))
                }
                response.json().then((data) => vue.users = data)
            })
        },

        buildQueryString: function() {
            this.query_string = "";

            for (let elem of this.query) {
                this.query_string += "(" + elem.field + ":" + elem.value + ") " + elem.operator + " "; 
            }
            let parts = this.query_string.split(' ');
            this.query_string = parts.join(' ');
        },

        showMessage: function(message, typeOfToast) {
            this.$toast.show(message, {
                type: typeOfToast,
                position: "top-right",
            });
        }
    }
}
</script>
  
<style scoped>
.search-input {
    width: min-content;
    margin: auto;
    display: flex;
    flex-direction: column;
    border-radius: 5px;
    border: 1px solid #CEA2AC;
    padding: 15px;
}
.search-input > div {
    display: flex;
    flex-direction: row;
    margin-bottom: 20px;
}
.search-input > div > div {
    width: 200px;
}
.search-input > div > div > span {
    height: 30px;
    display: flex;
    justify-content: center;
    align-items: center;
}
.search-input > div > div > select {
    height: 30px;
    outline: none;
    border: none;
    font-size: 15px;
    text-align: center;
    width: 90px;
}

.search-input > div > div > input {
    height: 30px;
    outline: none;
    border: none;
    font-size: 15px;
    text-align: center;
}

.add {
    background-color: #4BB543;
}
.remove {
    background-color: #D4403A;
}
.execute {
    background-color: #3B71CA;
}
.buttons {
    display: flex !important;
    justify-content: space-around;
}
.buttons > button {
    align-self: center;
    height: 35px;
    width: 90px;
    outline: none;
    border: none;
    border-radius: 5px;
    color: white;
    cursor: pointer;
}
.query {
    width: 70%;
    height: 60px;
    margin: auto;
    margin-top: 10px;
    font-size: large;
}

.output {
    height: 490px;
    width: 80%;
    margin: auto;
    border: 1px solid #CEA2AC;
    background-color: #e5e5e5;
    border-radius: 5px;
    overflow: auto;
}

.output > div {
    height: 80px;
    width: 100%;
    background-color: #e5e5e5;
    border-bottom: 1px solid #CEA2AC;
    display: flex;
    align-items: center;
    justify-content: space-around;
}

.output > div > span {
    width: calc(100%/6);
}
</style>
  