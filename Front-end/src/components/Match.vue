<script setup>
import{ref} from"vue"
// import { hash } from "argon2";

const matchedShow = ref('default')


const switchShow= ()=> {
    matchedShow.value = ['green', 'red', null]
    const randomIndex = Math.floor(Math.random() * matchedShow.value.length);
    return matchedShow.value = matchedShow.value[randomIndex]
    // console.log(matchedShow.value)

}
</script>
 
<template>
<div class="all">
    <div class="matchText" v-if="matchedShow === 'default'">
        <p class="text">Match Password</p>
    </div>
    <div class="matchTextGreen" v-else-if="matchedShow === 'green'">
        <p class="text">Password Matched</p>
    </div>
    <div class="matchTextRed" v-else-if="matchedShow === 'red'">
        <p class="text">Password NOT Matched</p>
    </div>
    <div class="matchTextNo" v-else>
        <p class="text">The specified username DOES NOT exist</p>
    </div>
    <div class="form">
        <h2>Match password</h2>
        <div class="div-form">
        <b>Username</b>
        <input
          class="ann-username"
          type="text"
          maxlength="45"
        />
        </div>
        <div class="div-form">
        <b>Password</b>
        <input
          class="ann-username"
          type="password"
          maxlength="45"
        />
      </div>
      <div class="ann-div-button">
        <button class="ann-button" @click="switchShow">Match or not</button>
      </div>
    </div>
</div>
</template>
 
<style scoped>
.matchText{
    margin-left: 43%;
    margin-top: 5%;
    border: 1px solid lightgray;
    border-radius: 8px;
    /* justify-content: center;
    justify-items: center; */
    padding-left: 30px;
    width: 30%;
}
.matchTextGreen{
    margin-left: 43%;
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
    margin-left: 43%;
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
    margin-left: 43%;
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
.text{
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