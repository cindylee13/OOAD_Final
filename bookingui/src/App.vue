<template>
  <v-app>
    <v-app-bar app color="primary" dark>
      <div class="d-flex align-center">
        <v-img
          alt="Vuetify Logo"
          class="shrink mr-2"
          contain
          src="https://cdn.vuetifyjs.com/images/logos/vuetify-logo-dark.png"
          transition="scale-transition"
          width="40"
        />

        <v-btn href="/" text contain min-width="100" class="shrink mt-1 hidden-sm-and-down headline">
          <span class="mr-2">Barking.com</span>
        </v-btn>
      </div>

      <v-spacer></v-spacer>

      <v-btn
        href="order"
        target="_self"
        text
        v-if="this.$store.state.userInfo.signedIn"
      >
        <span class="mr-2">My Order</span>
        <v-icon>mdi-hotel</v-icon>
      </v-btn>

      <v-menu offset-y v-if="this.$store.state.userInfo.signedIn">
        <template v-slot:activator="{ on }">
          <v-btn v-on="on" text>
            <span class="mr-2">USERNAME</span>
            <v-icon>mdi-face</v-icon>
          </v-btn>
        </template>
        <v-list>
          <v-list-item v-for="(item, index) in items" :key="index">
            <v-list-item-title>
              {{ item.title }}
            </v-list-item-title>
          </v-list-item>

          <v-list-item>
            <v-btn text @click="signOut">
              <span class="mr-2">Sign Out</span>
              <v-icon>mdi-logout-variant</v-icon>
            </v-btn>
          </v-list-item>
        </v-list>
      </v-menu>

      <!-- href="login" -->
      <v-btn
        text
        v-if="this.$store.state.userInfo.signedIn === false"
        @click="signIn"
      >
        <span class="mr-2">Login</span>
        <v-icon>mdi-login-variant</v-icon>
      </v-btn>
    </v-app-bar>

    <div class="mt-12 mb-6"></div>
    <HotelTable></HotelTable>
  </v-app>
</template>

<script>
// import HelloWorld from './components/HelloWorld';
import HotelTable from "./components/HotelTable";

export default {
  name: "App",

  components: {
    // HelloWorld,
    HotelTable
  },

  data: () => ({
    //
    items: [
      {
        title: "測試測試"
      }
    ]
  }),
  methods: {
    signIn: function() {
      this.$store.commit("userInfoChange", true);
    },
    signOut: function() {
      this.$store.commit("userInfoChange", false);
    }
  }
};
</script>
