<template>
  <v-app
    height="415px"
    id="e3"
    light
    standalone>
    <v-navigation-drawer
      class="pb-0"
      fixed
      height="100%"
      clipped
      enable-resize-watcher
      v-model="drawer" >
      
      <div v-if="navBarState == 'main'">
        <side-bar-menu @showError="showError" v-on:navToCurrency="navToCurrency"></side-bar-menu>
      </div>
      <div v-if="navBarState == 'currency'">
        <v-btn block color="secondary" @click="navToMain" dark>Back</v-btn>
        <currency-list-view v-on:changeDispCur="changeDispCur"></currency-list-view>
      </div>
    </v-navigation-drawer>
    <v-toolbar dark color="blue">
      <v-toolbar-title>
        <v-toolbar-side-icon @click.stop="drawer = !drawer"></v-toolbar-side-icon>
        <span>Cryptowatch</span>
	      </v-toolbar-title>
	       <div class="flex flex-fixed">
	 	      <div v-if="userId" class="ml1 pointer" @click="logout" style = "float:right; font-size:1.5em; cursor: pointer">logout</div>
	 	      <div v-else>
	 		      <router-link v-if= "$route.path != '/login'"  to="/login"
              class="ml1 no-underline">login</router-link>
	 		      <router-link v-if= "$route.path != '/register'"  to="/register" class="ml1 no-underline">register</router-link>
	 	      </div>
	 	   </div>
    </v-toolbar>
    <main>
      <v-container>
        <v-layout>
          <v-flex xs12 id="content-space">
            <currency-view v-if="dispType=='cur'" @showNotification="showNotification" v-bind:dispCur="dispCur"></currency-view>
            <view-watch-list @showNotification="showNotification" v-if="dispType=='watchlist'"></view-watch-list>
          </v-flex>
        </v-layout>
      </v-container>
    </main>

    <v-snackbar
      :timeout="6000"
      :top="true"
      v-model="snackbar">
      {{ errorMsg }}
      <v-btn flat color="green" @click.native="$router.push('/login')">Login</v-btn>
      <v-btn flat color="pink" @click.native="snackbar = false">Close</v-btn>
    </v-snackbar>
    <v-snackbar
      :timeout="6000"
      :top="true"
      v-model="notSnackbar">
      {{ notification }}
      <v-btn flat color="pink" @click.native="notSnackbar = false">Close</v-btn>
    </v-snackbar>
  </v-app>
</template>

<script>
  import CurrencyView from '@/components/CurrencyView.vue'
  import SideBarMenu from '@/components/SideBarMenu.vue'
  import CurrencyListView from '@/components/CurrencyListView.vue'
  import ViewWatchList from '@/components/ViewWatchList.vue'
  import { GC_USER_ID, GC_AUTH_TOKEN } from '@/constants/settings'

  export default {
    components: {
      CurrencyView, SideBarMenu, CurrencyListView, ViewWatchList
    },
    props: ['dispType'],
    methods: {
      showNotification: function (not) {
        this.notification = not
        this.notSnackbar = true;
      },
      showError: function (message) {
        this.errorMsg = message;
        this.snackbar = true;
      },
      logout: function () {
        localStorage.removeItem(GC_USER_ID);
        localStorage.removeItem(GC_AUTH_TOKEN);
        this.$root.$data.userId = null;
      	window.location.reload(true);
      },
      navToMain: function () {
        this.navBarState = 'main';
      },
      navToCurrency: function() {
        this.navBarState = 'currency';
      },
      changeDispCur: function(dispCur) {
        this.$router.push('/coin/' + dispCur);
      },
    },
    created: function () {
      this.$on("navToCurrency", this.navToCurrency);
      this.$on("navToMain", this.navToMain);

      // By default set to display currency type.
      if (this.dispType == null || this.dispType == "") {
        this.dispType = "cur";
      }
    },
    beforeDestroy: function () {
      this.$off('navToCurrency', this.navToCurrency);
      this.$off("navToMain", this.navToMain);
    },
    computed: {
      userId () {
        console.log("In userId function: " + this.$root.$data.userId);
        console.log("val of gcuserid in localstorage " + localStorage.getItem(GC_USER_ID));
        //return this.$root.$data.userId
        return localStorage.getItem(GC_USER_ID);
      },
      dispCur() {
        if (!this.$route.params.hasOwnProperty("cur")) {
          // The default 
          return "btc";
        }
        else {
          return this.$route.params.cur;
        }
      }
    },
    data: () => ({
      snackbar: false,
      errorMsg: "",
      drawer: true,
      navBarState: 'main',
      notification: "",
      notSnackbar: false,
    }),
    watch: {
      drawer: function(val) {
        if (val) {
          document.getElementById("content-space").style.marginLeft = "300px";
        }
        else {
          document.getElementById("content-space").style.marginLeft = "0px";
        }
      }
    }
  }
</script>

<style>
  .pb-0 {
    margin-top: 65px !important;
    height: calc(100% - 65px) !important;
  }
  #content-space {
    margin-left: 300px;
  }
  .ml1:link{
  	color: white !important; 
  	text-decoration: none; 
  	float: right; 
  	padding: 5px; 
  	font-size: 1.5em; 
  	
  }
  .ml1:visited{
  	color:white; 
  }
  .ml1:hover{
  	color: black; 
  }
</style>
