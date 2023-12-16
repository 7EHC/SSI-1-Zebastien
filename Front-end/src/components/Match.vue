<script setup>
import { ref, onMounted } from "vue";
// import { hash } from "argon2";
import navBar from "./nav.vue";
import { routerKey, useRouter } from "vue-router";

const accessToken = localStorage.getItem("accessToken");
const API_ROOT = import.meta.env.VITE_ROOT_API;
const matchedShow = ref("default");
const usernamePassword = ref({});
const username = ref("");
const password = ref("");
const router = useRouter();

const submit = () => {
  matchedShow.value = "loading"
  usernamePassword.value = {
    username: username.value,
    password: password.value,
  };

  matchPassword(usernamePassword.value);
  // console.log(usernamePassword.value)
};

const matchPassword = async (input) => {
  try {
    const res = await fetch(`${API_ROOT}/users/match`, {
      //   const res = await fetch('http://localhost:8080/api/users/match', {
      method: "POST",
      headers: {
        "content-type": "application/json",
        Authorization: `Bearer ${accessToken}`,
      },
      body: JSON.stringify(input),
    });
    if (res.status === 200) {
      //   console.log('Okay')
      //   const AddUser = await res.json(); //keep info that added from backend
      matchedShow.value = "green";
      //   router.push("/admin/user");
    } else if (res.status === 401) {
      //   console.log('password not match')
      matchedShow.value = "red";
    } else if (res.status === 404) {
      matchedShow.value = "else";
    }
  } catch (err) {
    console.log(err);
  }
};

const switchShow = () => {
  matchedShow.value = ["green", "red", null];
  const randomIndex = Math.floor(Math.random() * matchedShow.value.length);
  return (matchedShow.value = matchedShow.value[randomIndex]);
  // console.log(matchedShow.value)
};

onMounted(async () => {
  matchPassword.value = {
    username: username.value,
    password: password.value,
  };
  if(localStorage.getItem("role") === "announcer") {
    alert("Access Denied!")
    router.push("/")
  }
});
</script>

<template>
  <div class="all">
    <navBar />
    <div class="matchText" v-if="matchedShow === 'default'">
      <p class="ann-message">Match Password</p>
    </div>
    <div class="matchText" v-else-if="matchedShow === 'loading'">
      <p style="text-align: center; margin-left: -30px;" class="ann-message">
        Please wait ...
      </p>
    </div>
    <div class="matchTextGreen" v-else-if="matchedShow === 'green'">
      <p class="ann-message">Password Matched</p>
    </div>
    <div class="matchTextRed" v-else-if="matchedShow === 'red'">
      <p class="ann-message">Password NOT Matched</p>
    </div>
    <div class="matchTextNo" v-else>
      <p class="ann-message">The specified username DOES NOT exist</p>
    </div>
    <div class="form">
      <h2>Match password</h2>
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
        <button class="ann-button" @click="submit">Match or not</button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.matchText {
  margin-left: 43%;
  margin-top: 5%;
  border: 1px solid lightgray;
  border-radius: 8px;
  /* justify-content: center;
    justify-items: center; */
  padding-left: 30px;
  width: 30%;
}
.matchTextGreen {
  margin-left: 43%;
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
  margin-left: 43%;
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
  margin-left: 43%;
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
  margin-left: 43%;
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
