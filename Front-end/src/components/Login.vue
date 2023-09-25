<script setup>
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';

const loginStatus = ref("default")
const loginObj = ref({})
const username = ref('')
const password =ref('')
const router = useRouter()

const login = async (input) => {
  try {
    // const res = await fetch(`${API_ROOT}/users/match`, {
      const res = await fetch('http://localhost:8080/api/users/token', {
      method: "POST",
      headers: { "content-type": "application/json" },
      body: JSON.stringify(input),
    })
    if (res.status === 200) {
        // Get the token text from the response
        const jwtToken = await res.text();

        // Store the JWT token in local storage
        // localStorage.setItem('jwtToken', jwtToken);

        console.log(jwtToken);

        loginStatus.value = 'green';

        // Redirect after 1.5 seconds
        // setTimeout(function() {
        //     router.push("/admin/user");
        // }, 1500);
    } else if (res.status === 401) {
    //   console.log('password not match')
        loginStatus.value = 'red'
    }else if(res.status === 404){
        loginStatus.value = 'else'
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
        <h2>SAS Login</h2>
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
h2 {
  text-align: center;
}
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