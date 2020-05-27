<template>
  <div id="app">
    <h1>
      <img src="./assets/logo.svg" alt="Enroller" class="logo">
      System do zapisów na zajęcia
    </h1>
    <div v-if="authenticatedUsername">
      <h2>Witaj {{ authenticatedUsername }}!
        <a @click="logout()" class="float-right  button-outline button">Wyloguj</a>
      </h2>
      <meetings-page :username="authenticatedUsername"></meetings-page>
    </div>
    <div v-else>
      <login-form @login="login($event)"></login-form>
    </div>
  </div>
</template>

<script>
    import "milligram";
    import LoginForm from "./LoginForm";
    import MeetingsPage from "./meetings/MeetingsPage";
    export default {
        components: {LoginForm, MeetingsPage},
        data() {
            return {
                authenticatedUsername: "",
                registering: false
            };
        },
        methods: {
            login(user) {
                this.authenticatedUsername = user.login;
            },
            register(user){
              this.$http.post('participants', user)
              .then(response => {
                //udalo sie
              })
              .catch(response => {
                //nie udalo sie
              });
            },
            logout() {
                this.authenticatedUsername = '';
            }
        }
    };
</script>

<style>
  #app {
    max-width: 1000px;
    margin: 0 auto;
  }
  .logo {
    vertical-align: middle;
  }
</style>