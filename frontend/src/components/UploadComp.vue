<template>
    <div class="whole">
        <h1>Upload a document</h1>
        <div class="form">
            <div class="entry">
                <span>Firstname:</span>
                <input type="text" v-model="entry.firstname">
            </div>
            <div class="entry">
                <span>Lastname:</span>
                <input type="text" v-model="entry.lastname">
            </div>
            <div class="entry">
                <span>Email:</span>
                <input type="email" v-model="entry.email">
            </div>
            <div class="entry">
                <span>Address:</span>
                <input type="text" v-model="entry.address">
            </div>
            <div class="entry">
                <span>Phone:</span>
                <input type="tel" v-model="entry.phone">
            </div>
            <div class="entry">
                <span>Education:</span>
                <select v-model="entry.education">
                    <option value="primary">Primary</option>
                    <option value="lower-secondary">lower-secondary</option>
                    <option value="upper-secondary">upper-secondary</option>
                    <option value="post-secondary">post-secondary</option>
                    <option value="bachelor">bachelor</option>
                    <option value="master">master</option>
                    <option value="phd">phd</option>
                </select>
            </div>
            <div class="entry">
                <span>Cv:</span>
                <input type="file" v-on:change="entry.cv = readFile($event)" accept="application/pdf">
            </div>
            <div class="entry">
                <span>Letter:</span>
                <input type="file" v-on:change="entry.letter = readFile($event)" accept="application/pdf">
            </div>
            <button @click="post">Upload</button>
        </div>
    </div>
</template>
  
<script>

export default {
    name: 'UploadComp',
    data() {
        return {
            entry: {
                firstname: '',
                lastname: '',
                email: '',
                address: '',
                cv: '',
                letter: '',
                phone: '',
                education: 'primary',
            }
        }
    },
    methods: {
        readFile: function(event) {
            return event.target.files[0]
        },
        post: async function() {
            if (!this.validate()){
                return
            }
            let formData = this.getFormData();
            let vue = this;
            fetch('http://localhost:8080/api/upload', {
                method: 'POST',
                body: formData
            })
            .then((response) => {
                let outcome = 'success';
                if (response.status != 200){
                    outcome = 'error'
                }
                response.text().then((data) => vue.showMessage(data, outcome))
            });
        },
        getFormData: function() {
            let formData = new FormData()

            formData.append('firstname', this.entry.firstname);
            formData.append('lastname', this.entry.lastname);
            formData.append('email', this.entry.email);
            formData.append('address', this.entry.address);
            formData.append('cv', this.entry.cv, 'cv.pdf');
            formData.append('letter', this.entry.letter, 'letter.pdf');
            formData.append('phone', this.entry.phone);
            formData.append('education', this.entry.education);

            console.log(this.entry.cv)

            return formData
        },
        validate: function() {
            let error = new Array();
            if (this.entry.firstname == ''){
                error.push('Firstname is mandatory')
            }
            if (this.entry.lastname == ''){
                error.push('Lastname is mandatory')
            }
            if (this.entry.email == ''){
                error.push('Email is mandatory')
            }
            if (this.entry.address == ''){
                error.push('Address is mandatory')
            }
            if (this.entry.cv == ''){
                error.push('Cv is mandatory')
            }
            if (this.entry.letter == ''){
                error.push('Letter is mandatory')
            }
            if (this.entry.phone == ''){
                error.push('Phone is mandatory')
            }
            if (this.entry.education == ''){
                error.push('Education is mandatory')
            }

            if (error.length != 0){
                this.showMessage(error.join(',<br/>'), 'error')
                return false;
            }
            
            return true;
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
.form {
    height: 620px;
    width: 370px;
    margin: auto;
    border-radius: 10px;
    border: 1px solid #CEA2AC;
    padding: 30px;
}

.form > button {
    height: 35px;
    width: 90px;
    outline: none;
    border: none;
    border-radius: 5px;
    color: white;
    background-color: #4BB543;
    cursor: pointer;
}

.entry {
    height: 75px;
    width: 100%;
    margin: auto;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: flex-start;
}

.entry > input, select {
    padding-left: 15px;
    height: 30px;
    margin-top: 5px;
    outline: none;
    width: calc(100% - 30px);
    border-radius: 5px;
    border: none;
}
.entry > span {
    margin-left: 15px;
}
.entry > select {
    width: calc(100% - 13px) !important;
    height: 35px !important;
}
</style>
  