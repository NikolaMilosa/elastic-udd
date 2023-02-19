<template>
  <HeaderComp :callback="change"/>
  <HomeComp v-if="page == 'home'"/>
  <UploadComp v-if="page == 'upload'"/>
  <QueryComp v-if="page == 'query'"/>
</template>

<script>
import HeaderComp from './components/HeaderComp.vue'
import HomeComp from './components/HomeComp.vue'
import UploadComp from './components/UploadComp.vue'
import QueryComp from './components/QueryComp.vue'

export default {
  name: 'App',
  components: {
    HeaderComp,
    HomeComp,
    QueryComp,
    UploadComp
  },
  data() {
    return {
      page: 'home'
    }
  },
  methods: {
    change: function(page) {
      this.page = page
    }
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
}
body {
  height: 100vh;
  width: 100vw;
  margin: 0;
  background-color: #EFD9CE;
}
</style>
