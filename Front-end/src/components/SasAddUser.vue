<script setup>
import { ref, onMounted } from "vue"
import { useRoute, useRouter } from "vue-router";

const router = useRouter()
const newUserToSend = ref({})
const newUsername = ref('')
const newName = ref('')
const newEmail = ref('')
const newRole = ref('announcer')
const newCreatedOn = ref('')
const newUpdateOn = ref('')
const cloneNewUser = ref({})
const disSave = ref(true)

const hasDataChanged= () =>{
  newUserToSend.value = {
    username:newUsername.value,
    name:newName.value,
    email:newEmail.value,
    role:newRole.value,
    createdOn:newCreatedOn.value,
    updatedOn:newUpdateOn.value
  }
  if (JSON.stringify(cloneNewUser.value) === JSON.stringify(newUserToSend.value)) {
    disSave.value = true
    } else {
      disSave.value = false
    }
}

const addNewUser = async (newUserToSend) => {
    if(newUsername.value.length === 0 || newName.value.length === 0 || newEmail.value.length === 0){
        alert('There is an error: Please enter the required information!!')
        }else try {
        // checkUpdateAccount(newAccount)
        const res = await fetch(`${API_ROOT}/users`,{
        // const res = await fetch('http://localhost:8080/api/users', {
            method: 'POST',
            headers: { 'content-type': 'application/json' },
            body: JSON.stringify(newUserToSend)
        }) //Add account at backend
        // method post. if it success, it will return status 201 / other methods return status 200
        if (res.status === 200) {
            // console.log('Okay')
            const AddUser = await res.json() //keep info that added from backend
            router.push('/admin/user')
        } else if(res.status === 400 || res.status === 500){
            throw new Error('Cannot add')
        }
    } catch (err) {
        console.log(err)
    }
}
const submit = () =>{
  newCreatedOn.value = new Date().toISOString()
  newUpdateOn.value = new Date().toISOString()
  newUserToSend.value = {
    username:newUsername.value,
    name:newName.value,
    email:newEmail.value,
    role:newRole.value,
    createdOn:newCreatedOn.value,
    updatedOn:newUpdateOn.value
  }
  addNewUser(newUserToSend.value)
}

onMounted(async()=>{
  // newUserToSend.value = {
  //   username:'',
  //   name:'',
  //   email:'',
  //   role:'',
  //   createdOn:'',
  //   updatedOn:''
  // }
  newUserToSend.value = {
    username:newUsername.value,
    name:newName.value,
    email:newEmail.value,
    role:newRole.value,
    createdOn:newCreatedOn.value,
    updatedOn:newUpdateOn.value
  }
  // console.log(newUserToSend.value.role)

  cloneNewUser.value = Object.assign({},newUserToSend.value)

  // const currentTimeUTC = new Date().toISOString();
  // console.log(currentTimeUTC)
})
</script>

<template>
  <div class="all">
    <div class="form">
      <h1>User Detail:</h1>

      <div>
        <b>Username<span style="color: red;"> *</span></b>
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
        <b>Name<span style="color: red;"> *</span></b>
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
        <b>Email<span style="color: red;"> *</span></b>
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
