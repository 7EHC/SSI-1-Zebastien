<script setup>
import { onMounted, ref } from "vue";
import { useRouter, RouterLink } from "vue-router";
import { useTokenStore } from "../stores/tokenStore.js";
import { getAllUsers } from "../composable/fetch";
import { CheckCircleIcon,XCircleIcon } from '@heroicons/vue/24/solid'
import jwt_decode from "jwt-decode";

const loginStatus = ref("default");
const loginObj = ref({});
const username = ref("");
const password = ref("");
const router = useRouter();
const API_ROOT = import.meta.env.VITE_ROOT_API;
const tokenStore = useTokenStore();

const login = async (input) => {
  try {
    const res = await fetch(`${API_ROOT}/token`, {
      // const res = await fetch('http://localhost:8080/api/token', {
      method: "POST",
      headers: { "content-type": "application/json" },
      body: JSON.stringify(input),
    });

    if (res.status === 200) {
      const response = await res.json(); // Parse the response as JSON

      // Store the access token and refresh token in local storage
      tokenStore.setAccessToken(response.token);
      tokenStore.setRefreshToken(response.refreshToken);

      const decodedToken = jwt_decode(response.token)
      tokenStore.setUserLogin(decodedToken.sub)
      tokenStore.setRole(decodedToken.role);

      // console.log(accessToken);
      // localStorage.setItem('accessToken', response.token);
      // localStorage.setItem('refreshToken', response.refreshToken);

      loginStatus.value = "green";

      // Redirect after 1.5 seconds
      setTimeout(function () {
        router.push("/announcement");
      }, 1500);
    } else if (res.status === 401) {
      loginStatus.value = "red";
    } else if (res.status === 404) {
      loginStatus.value = "else";
    }
  } catch (err) {
    console.log(err);
  }
};

const submit = () => {
  loginStatus.value = "loading"
  loginObj.value = {
    username: username.value,
    password: password.value,
  };

  login(loginObj.value);
};

onMounted(async () => {
  loginObj.value = {
    username: username.value,
    password: password.value,
  };
});
</script>

<template>
  <div class="all">
    <div class="divHead">
    <h1 class="Header"><RouterLink :to="{ name: 'User' }" style="text-decoration: none; display: flex; width: 10px; float: left;"><img src="../assets/BackArrow.svg" alt="back" style="float: left; opacity: 0.8; display: flex;"/></RouterLink> &nbsp;&nbsp;&nbsp;&nbsp;SIT Announcement System (SAS)</h1>
    </div>
    <div class="matchText" v-if="loginStatus === 'default'">
      <p style="text-align: center; margin-left: -30px;" class="ann-message">
        Please Login
      </p>
    </div>
    <div class="matchText" v-else-if="loginStatus === 'loading'">
      <p style="text-align: center; margin-left: -30px;" class="ann-message">
        Please wait ...
      </p>
    </div>
    <div class="matchTextGreen" v-else-if="loginStatus === 'green'">
      <p class="ann-message">Login Successful <CheckCircleIcon style="height: 25px;float: right;margin-right: 10px; color: #00d107;" /> </p>
    </div>
    <div class="matchTextRed" v-else-if="loginStatus === 'red'">
      <p class="ann-message">Password Incorrect <XCircleIcon style="height: 25px;float: right;margin-right: 10px; color: #ff4040;" /></p>
    </div>
    <div class="matchTextNo" v-else>
      <p class="ann-message">Username DOES NOT exist <XCircleIcon style="height: 25px;float: right;margin-right: 10px; color: #ff4040;" /></p>
    </div>
    <div class="form" style="background-color: #1a1a1d;">
      <h2 style="color: #45a29e;">Login to SIT Announcement system (SAS)</h2>
      <div class="div-form">
        <b>Username</b>
        <input
          class="ann-username"
          v-model="username"
          type="text"
          maxlength="45"
          @keydown.enter="submit"
        />
      </div>
      <div class="div-form">
        <b>Password</b>
        <input
          class="ann-password"
          v-model="password"
          type="password"
          minlength="8"
          maxlength="14"
          @keydown.enter="submit"
        />
      </div>
      <div class="ann-div-button">
        <button class="ann-button" @click="submit">Login</button>
      </div>
    </div>
  </div>
</template>

<style scoped>
img:hover {
  animation: shake 0.5s ease-in-out;
} 
@keyframes shake {
  0% { transform: translate(0, 0); }
  20% { transform: translate(-2px, 0); }
  40% { transform: translate(2px, 0); }
  60% { transform: translate(-2px, 0); }
  80% { transform: translate(2px, 0); }
  100% { transform: translate(0, 0); }
}
.divHead {
  margin-left: -10px;
  margin-right: -8px;
}
.Header {
  background-color: #ffff;
  box-shadow: 0 3px 4px rgba(0, 0, 0, 0.1);
  width: 100%;
  padding-bottom: 20px;
  display: flex;
  justify-items: center;
  justify-content: center;
}
.matchText {
  color: black;
  margin-left: 37%;
  margin-top: 5%;
  border: 2px solid #1a1a1d;
  border-radius: 8px;
  padding-left: 30px;
  width: 30%;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  background-color: lightgrey;
}
.matchTextGreen {
  margin-left: 37%;
  margin-top: 5%;
  border: 2px solid #00D107;
  color: #00D107;
  background-color: #DBFFDC;
  border-radius: 8px;
  padding-left: 30px;
  width: 30%;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
.matchTextRed {
  margin-left: 37%;
  margin-top: 5%;
  border: 2px solid #ff4040;
  color: #ff4040;
  background-color: #ffbaba;
  border-radius: 8px;
  padding-left: 30px;
  width: 30%;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
.matchTextNo {
  margin-left: 37%;
  margin-top: 5%;
  border: 2px solid #ff4040;
  color: #ff4040;
  background-color: #ffbaba;
  border-radius: 8px;
  padding-left: 30px;
  width: 30%;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
.all {
  font-family: "Trebuchet MS", "Lucida Sans Unicode", "Lucida Grande",
    "Lucida Sans", Arial, sans-serif;
}
.ann-message {
  font-size: 20px;
  font-weight: bold;
}
.form {
  margin-left: 37%;
  border: lightgray solid 1px;
  border-radius: 8px;
  margin-top: 30px;
  padding: 15px;
  width: 30%;
  font-weight: bold;
}
input {
  width: 99%;
  height: 35px;
  border-radius: 4px;
  border: 1px solid lightgray;
  margin-top: 10px;
  padding-left: 10px;
}
.div-form {
  margin-top: 5px;
  margin-right: 10px;
  color: #45a29e;
}
.ann-div-button {
  display: flex;
  justify-content: center;
}
.ann-button {
  margin-top: 25px;
  margin-right: 10px;
  width: 20%;
  height: 36px;
  border-radius: 7px;
  border: 0px;
  font-size: 14px;
  font-weight: bold;
  transition-duration: 0.3s;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  background-color: #45a29e;
  color: white;
}
.ann-button:hover {
  background-color: white;
  color: #45a29e;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}
</style>
