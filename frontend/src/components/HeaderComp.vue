<template>
    <div class="header">
        <button class="header-option header-home" @click="this.callback('home')">Home</button>
        <button class="header-option header-document" @click="this.callback('upload')">Upload</button>
        <button class="header-option header-query" @click="this.callback('query')">Query</button>
    </div>
</template>
  
<script>
export default {
    name: 'HeaderComp',
    props: {
        callback: Function
    }
}
</script>
  
<style scoped>
.header {
    height: 60px;
    width: 100vw;
    background-color: #8F3985;
    display: flex;
}
.header-option {
    width: 150px;
    background-color: #8F3985;
    outline: none;
    color: #17171d;
    border: none;
    cursor: pointer;
    border-right: 1px solid #17171d;
    font-size: 15px;
    background-repeat: no-repeat;
    background-size: 20px;
    background-position: 25px 21px;
}
.header-home {
    background-image: url("https://img.icons8.com/ios-filled/50/null/home-page.png");
}
.header-document {
    background-image: url("https://img.icons8.com/ios-filled/50/null/upload-document.png");
}
.header-query {
    background-image: url("https://img.icons8.com/ios-filled/50/null/search--v1.png");
}
</style>
  