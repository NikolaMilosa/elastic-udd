<template>
  <HeaderComp :callback="change"/>
  <HomeComp v-if="page == 'home'"/>
  <UploadComp v-if="page == 'upload'"/>
  <QueryUsersComp v-if="page == 'query_users'"/>
  <QueryCvComp v-if="page == 'query_cv'"/>
  <QueryLetterComp v-if="page == 'query_letter'"/>
  <QueryGeolocComp v-if="page == 'query_geoloc'"/>
</template>

<script>
import HeaderComp from './components/HeaderComp.vue'
import HomeComp from './components/HomeComp.vue'
import UploadComp from './components/UploadComp.vue'
import QueryUsersComp from './components/QueryUsersComp.vue'
import QueryCvComp from './components/QueryCvComp.vue'
import QueryLetterComp from './components/QueryLetterComp.vue'
import QueryGeolocComp from './components/QueryGeolocComp.vue'

export default {
  name: 'App',
  components: {
    HeaderComp,
    HomeComp,
    QueryUsersComp,
    UploadComp,
    QueryCvComp,
    QueryLetterComp,
    QueryGeolocComp
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
