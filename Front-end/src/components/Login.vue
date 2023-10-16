<script setup>
import { onMounted, ref } from "vue";
import { useRouter } from "vue-router";
import { useTokenStore } from "../stores/tokenStore.js";
import { getAllUsers } from "../composable/fetch";
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
    <div class="matchText" v-if="loginStatus === 'default'">
      <p style="text-align: center; margin-left: -30px;" class="ann-message">
        Please Login
      </p>
    </div>
    <div class="matchTextGreen" v-else-if="loginStatus === 'green'">
      <p class="ann-message">Login Successful</p>
    </div>
    <div class="matchTextRed" v-else-if="loginStatus === 'red'">
      <p class="ann-message">Password Incorrect</p>
    </div>
    <div class="matchTextNo" v-else>
      <p class="ann-message">Username DOES NOT exist</p>
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
        />
      </div>
      <div class="ann-div-button">
        <button class="ann-button" @click="submit">Login</button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.matchText {
  margin-left: 37%;
  margin-top: 5%;
  border: 1px solid lightgray;
  border-radius: 8px;
  /* justify-content: center;
    justify-items: center; */
  padding-left: 30px;
  width: 30%;
}
.matchTextGreen {
  margin-left: 37%;
  margin-top: 5%;
  border: 2px solid #0cd321;
  color: #0cd321;
  background-color: #c5ffcb;
  border-radius: 8px;
  /* justify-content: center;
    justify-items: center; */
  padding-left: 30px;
  width: 30%;
}
.matchTextRed {
  margin-left: 37%;
  margin-top: 5%;
  border: 2px solid #ff4040;
  color: #ff4040;
  background-color: #ffbaba;
  border-radius: 8px;
  /* justify-content: center;
    justify-items: center; */
  padding-left: 30px;
  width: 30%;
}
.matchTextNo {
  margin-left: 37%;
  margin-top: 5%;
  border: 2px solid #ff4040;
  color: #ff4040;
  background-color: #ffbaba;
  border-radius: 8px;
  /* justify-content: center;
    justify-items: center; */
  padding-left: 30px;
  width: 30%;
}
.all {
  font-family: "Trebuchet MS", "Lucida Sans Unicode", "Lucida Grande",
    "Lucida Sans", Arial, sans-serif;
}
.ann-message {
  font-size: 20px;
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
.ann-button {
  margin-top: 25px;
  margin-right: 10px;
  width: 180px;
  height: 36px;
  border-radius: 7px;
  border: 0px;
  font-size: 14px;
  font-weight: bold;
}
.ann-button:hover {
  background-color: lightgrey;
}
</style>
