<script setup>
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';

const loginStatus = ref("default")
const loginObj = ref({})
const username = ref('')
const password =ref('')
const router = useRouter()
const API_ROOT = import.meta.env.VITE_ROOT_API

// const login = async (input) => {
//   try {
//     // const res = await fetch(`${API_ROOT}/users/match`, {
//       const res = await fetch('http://localhost:8080/api/users/token', {
//       method: "POST",
//       headers: { "content-type": "application/json" },
//       body: JSON.stringify(input),
//     })
//     if (res.status === 200) {
//         // Get the token text from the response
//         const jwtToken = await res.text();

//         // Store the JWT token in local storage
//         localStorage.setItem('jwtToken', jwtToken);

//         console.log(jwtToken);

//         loginStatus.value = 'green';

//         // Redirect after 1.5 seconds
//         setTimeout(function() {
//             router.push("/admin/user");
//         }, 1500);
//     } else if (res.status === 401) {
//     //   console.log('password not match')
//         loginStatus.value = 'red'
//     }else if(res.status === 404){
//         loginStatus.value = 'else'
//     }
//   } catch (err) {
//     console.log(err);
//   }
// }
const login = async (input) => {
  try {
    const res = await fetch(`${API_ROOT}/token`, {
    // const res = await fetch('http://localhost:8080/api/users/token', {
      method: "POST",
      headers: { "content-type": "application/json" },
      body: JSON.stringify(input),
    });

    if (res.status === 200) {

      const response = await res.json(); // Parse the response as JSON

      // Store the access token and refresh token in local storage
      localStorage.setItem('accessToken', response.token);
      localStorage.setItem('refreshToken', response.refreshToken);

      loginStatus.value = 'green';

      // Redirect after 1.5 seconds
      setTimeout(function() {
        router.push("/admin/user");
      }, 1500);


      // Set timeout for access token expiration to 30 minutes
      setTimeout(function() {
        localStorage.removeItem('accessToken'); // Remove access token from local storage
        alert('Access token has expired');
      }, 30 * 60 * 1000); // 30 minutes in milliseconds

      // Set timeout for refresh token expiration to 24 hours
      setTimeout(function() {
        localStorage.removeItem('refreshToken'); // Remove refresh token from local storage
        alert('Refresh token has expired');
        router.push("/login"); // Redirect to login page
      }, 24 * 60 * 60 * 1000); // 24 hours in milliseconds

    } else if (res.status === 401) {
      loginStatus.value = 'red';
    } else if (res.status === 404) {
      loginStatus.value = 'else';
    }
  } catch (err) {
    console.log(err);
  }
}

const submit = ()=>{
    loginObj.value = {
    username:username.value,
    password:password.value
}

login(loginObj.value)
}

const refreshAccessToken = async () => {
  try {
    const refreshToken = localStorage.getItem('refreshToken');

    const res = await fetch('http://localhost:8080/api/users/refresh', {
      method: "POST",
      headers: { "content-type": "application/json" },
      body: JSON.stringify({ refreshToken }),
    });

    if (res.status === 200) {
      const newAccessToken = await res.text();

      // Update the access token in local storage
      localStorage.setItem('jwtToken', newAccessToken);

      // Schedule the next token refresh after 23 hours
      setTimeout(function() {
        refreshAccessToken();
      }, 23 * 60 * 60 * 1000); // 23 hours in milliseconds
    } else {
      // Handle error (e.g., redirect to login)
      alert('Error refreshing token');
      router.push("/login");
    }
  } catch (err) {
    console.log(err);
  }
}

onMounted(async() =>{
loginObj.value = {
    username:username.value,
    password:password.value
}
})
</script>
 
<template>
<div class="all">
    <div class="matchText" v-if="loginStatus === 'default'">
        <p style="text-align: center; margin-left: -30px;" class="ann-message">Login</p>
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
    <div class="form">
        <h2>Match password</h2>
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
.matchText{
    margin-left: 37%;
    margin-top: 5%;
    border: 1px solid lightgray;
    border-radius: 8px;
    /* justify-content: center;
    justify-items: center; */
    padding-left: 30px;
    width: 30%;
}
.matchTextGreen{
    margin-left: 37%;
    margin-top: 5%;
    border: 2px solid #0CD321;
    color: #0CD321;
    background-color: #C5FFCB;
    border-radius: 8px;
    /* justify-content: center;
    justify-items: center; */
    padding-left: 30px;
    width: 30%;
}
.matchTextRed{
    margin-left: 37%;
    margin-top: 5%;
    border: 2px solid #FF4040;
    color: #FF4040;
    background-color: #FFBABA;
    border-radius: 8px;
    /* justify-content: center;
    justify-items: center; */
    padding-left: 30px;
    width: 30%;
}
.matchTextNo{
    margin-left: 37%;
    margin-top: 5%;
    border: 2px solid #FF4040;
    color: #FF4040;
    background-color: #FFBABA;
    border-radius: 8px;
    /* justify-content: center;
    justify-items: center; */
    padding-left: 30px;
    width: 30%;
}
.all{
    font-family: "Trebuchet MS", "Lucida Sans Unicode", "Lucida Grande",
    "Lucida Sans", Arial, sans-serif;
}
.ann-message{
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
input{
    width: 99%;
    height: 35px;
    border-radius: 4px;
    border: 1px solid lightgray;
    margin-top: 10px;
    padding-left: 10px;
}
.div-form{
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
.ann-button:hover{
    background-color: lightgrey;
}
</style>