<script setup>
import { ref,onMounted } from "vue"

const ogData = ref({})

onMounted(async()=>{
    ogData
})
</script>
 
<template>
<div class="all">

    <div class="sidenav">
    <div class="text-nav">
        <h1 class="ann-app-title">SAS</h1>
        <hr />
        <RouterLink :to="{name: 'Announcement'}"><button class="ann-menu">Announcement</button></RouterLink>
        <hr />
        <RouterLink :to="{name: 'SasUser'}"><button class="ann-menu">User</button></RouterLink>
        <hr />
      </div>
    </div>

    <div class="form">
      <h1>User Detail:</h1>

      <div>
        <b>Username</b>
        <input
          class="ann-username"
          v-model.trim="newUsername"
          type="text"
          maxlength="45"
          placeholder="Enter less than 45 characters"
          v-on:input="hasDataChanged"
        />
      </div>
      <div class="div-form">
        <b>Name</b>
        <input
          class="ann-name"
          v-model.trim="newName"
          type="text"
          maxlength="45"
          placeholder="Enter less than 100 characters"
          v-on:input="hasDataChanged"
        />
      </div>
    <div class="div-form">
        <b>Email</b>
        <input
          class="ann-email"
          v-model.trim="newEmail"
          type="text"
          maxlength="45"
          placeholder="Enter less than 150 characters"
          v-on:input="hasDataChanged"
        />
    </div>
    <div class="div-form">
        <b>Role</b><br>
        <select name="roleName" class="ann-role" v-model.trim="newRole" v-on:change="hasDataChanged">
          <option id="1" value="announcer" selected >announcer</option>
          <option id="2" value="admin" >admin</option>
        </select>
    </div>
    <div class="ann-div-button">
        <button class="ann-button" type="submit" @click="submit" :disabled="disSave">Save</button>
        <RouterLink :to="{ name: 'SasUser' }">
          <button class="ann-button">Cancel</button>
        </RouterLink>
    </div>

    </div>

</div>
</template>
 
<style scoped>
.sidenav {
  height: 100%;
  width: 200px;
  position: fixed;
  z-index: 1;
  top: 0;
  left: 0;
  background-color: #e6e6e6;
  /* overflow-x: hidden; */
  /* padding-top: 10px; */
}
.text-nav {
  padding-left: 15px;
}
.all {
  font-family: "Trebuchet MS", "Lucida Sans Unicode", "Lucida Grande",
    "Lucida Sans", Arial, sans-serif;
}

.form {
  border: lightgray solid 1px;
  border-radius: 8px;
  margin-top: 60px;
  margin-left: 13%;
  margin-right: 30px;
  padding: 15px;
  font-weight: bold;
}

.div-form,select{
    margin-top: 15px;
}
.ann-username,.ann-name,.ann-email {
  width: 98%;
  height: 36px;
  padding-left: 10px;
  margin-top: 10px;
  border: lightgray 1px solid;
  border-radius: 6px;
}
select {
  height: 36px;
  width: 25%;
  padding-left: 10px;
  background-color: #e6e6e6;
  border-radius: 7px;
}
.ann-button {
  margin-top: 25px;
  margin-right: 10px;
  width: 100px;
  height: 36px;
  border-radius: 7px;
  border: 0px;
}
.ann-button:hover {
  background-color: lightgrey;
}
.ann-menu{
    color: black;
    text-decoration: none;
    background-color: transparent;
    border: 0;
    font-size: 15px;
    margin-top: 5px;
    margin-bottom: 5px;
    padding-left:0;
}
.ann-menu:hover{
    font-weight: bold;
}
</style>