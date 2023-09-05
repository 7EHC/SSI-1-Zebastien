<script setup>
import { useRoute, useRouter } from "vue-router";
import { ref,onMounted } from "vue"
import { getAllUsers } from "../composable/fetch"
import { changeTime } from "../composable/changeTime"

const router = useRouter()
const timeZone = Intl.DateTimeFormat().resolvedOptions().timeZone
const user = ref([])
const confirmDelete = ref(false)
const idToDelete = ref()
const API_ROOT = import.meta.env.VITE_ROOT_API

const deleteUser = async (id) => {
    try {
        const res = await fetch(`${API_ROOT}/users/${id}`, { method: 'DELETE' });
        // const res = await fetch(`http://localhost:8080/api/users/${id}`, { method: 'DELETE' })
        if (res.ok) {
         changeConfirm()
         user.value = user.value.filter((usr) => usr.id !== id) //Delete frontend
          // console.log(announcement.value)
            router.push('/admin/user')
        } else {
            throw new Error(`Cannot delete`)
        }
    } catch (err) {
        alert(`Error: ${err}`)
    }
}

const changeConfirm = (id) => {
  confirmDelete.value = !confirmDelete.value
  idToDelete.value = id
  // console.log(confirmDelete.value)
  // console.log(idToDelete.value)
}

const goToEdit = (id) => {
  router.push({ name: 'SasEditUser', params: { id: id } })
}

onMounted(async () =>{
  user.value = await getAllUsers()
  user.value.sort((a, b) => a.role.localeCompare(b.role) || a.username.localeCompare(b.username));
  // return user.value
  // console.log(user.value)
  if(!user.value){
    user.value = []
  }
})
</script>
 
<template>
<div class="all">
  <div class="popup">
  <Teleport to="body">
    <div class="modal" style="background-color: rgba(0, 0, 0, 0.3); width: 100%;" v-if="confirmDelete === true">
      <!-- <div class="modal" style="background-color: rgba(0, 0, 0, 0.3); width: 100%;"> -->
      <div class="window" style="height: 40%; width: 50%; background-color: white;">
        <p style="font-size: 48px;">Delete</p>
        <hr>
        <p style="font-size: 24px; margin-top: 30px;">Do you want to delete these records ?</p>
        <button class="viewBut" @click="changeConfirm">Cancel</button><RouterLink :to="{ name: 'SasUser' }">
        <button @click="deleteUser(idToDelete)" class="deleteButPopup">Delete</button></RouterLink>
      </div>
    </div>
  </Teleport>
</div>

    <div class="parent-container">
        <h1 class="user-man">User Management</h1>
    </div>
    <div>
        <h4 class="ann-timezone">Date/Time shown in Timezone: {{ timeZone }}</h4>

        <div class="ann-button" id="addDiv">
        <RouterLink :to="{name: 'SasAddUser'}"><button id="addBut" style="float: right;">Add User</button></RouterLink>
        </div>
        <div class="bigTable">
            <table>
                <tr class="tableHead">
                    <th class="trHead">No.</th>
                    <th class="trHead">Username</th>
                    <th class="trHead">Name</th>
                    <th class="trHead">Email </th>
                    <th class="trHead">Role</th>
                    <th class="trHead">Created On</th>
                    <th class="trHead">Updated On</th>
                    <th class="trHead">Action</th>
                </tr>

                <tr v-for="(usr, index) in user" :key="index" class="ann-item">
                  <td>{{ index + 1 }}</td>
                  <td class="ann-username">{{ usr.username }}</td>
                  <td class="ann-name">{{ usr.name }}</td>
                  <td class="ann-email">{{ usr.email }}</td>
                  <td class="ann-role">{{ usr.role }}</td>
                  <td class="ann-created-on">{{ changeTime(usr.createdOn) }}</td>
                  <td class="ann-updated-on">{{ changeTime(usr.updatedOn) }}</td>
                  <td class="ann-button-td">
                    <button class="ann-button" id="edit-button" @click="goToEdit(usr.id)">edit</button>
                    <button class="ann-button" id="delete-button" @click="changeConfirm(usr.id)">delete</button>
                  </td>
                </tr>
            </table>  
        </div>
    </div>
    <h4 class="NoAlert" v-if="user.length === 0">No user</h4>
</div>

</template>
 
<style scoped>
.ann-timezone {
  justify-content: start;
  margin-bottom: -4%;
  font-weight: lighter;
  margin-top: 10px;
}
.NoAlert {
  display: flex;
  justify-content: center;
  margin-top: 10%;
  margin-left: 13.5%;
}
.ann-timezone,.bigTable{
    padding-left: 13.5%;
}
.parent-container {
  display: flex;
  padding-left: 50%;
  /* align-items: center; */
}
.all{
    font-family: 'Trebuchet MS', 'Lucida Sans Unicode', 'Lucida Grande', 'Lucida Sans', Arial, sans-serif;
}
.ann-app-title{
    color: black;
}
.text-nav{
    padding-left: 15px;
}
table {
  /* margin-top: 30px; */
  border-collapse: collapse;
  width: 100%;
}

th {
  background-color: lightgray;
  border: 1px solid lightgray;
  height: 35px;
  font-size: large;
}

td {
  padding: 15px;
  border: 1px solid lightgray;
}
.ann-button-td{
  width: 220px;
}
#delete-button:hover{
  background-color: salmon;
  font-weight: bold;
}
#delete-button{
  margin-left: 15px;
}
#edit-button:hover{
  background-color: #fada5e;
  font-weight: bold;
}
#addBut,#edit-button,#delete-button{
  width: 100px;
  height: 30px;
  border: 0px;
  border-radius: 6px;
  background-color: #e6e6e6;
  font-weight: normal;
}
#addBut:hover{
  background-color: mediumseagreen;
  font-weight: bold;
}
.ann-button{
  text-align: center;
}
#addDiv{
  float: right;
  margin-bottom: 10px;
}

.deleteButPopup{
  /* background-color: coral; */
  background-color: salmon;
  width: 100px;
  height: 30px;
  border: 0px;
  border-radius: 6px;
}
.deleteButPopup:hover{
  background-color: indianred;
}

.modal {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  /* background-color: #0000; */
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}
.modal>div {
  /* background-color: rgb(250 204 21); */
  padding: 10px;
  border-radius: 10px;
  /* width: 50%;
  height: 30%; */
  border: 0px;
}
.window{
  text-align: center;
  font-family: 'Trebuchet MS', 'Lucida Sans Unicode', 'Lucida Grande', 'Lucida Sans', Arial, sans-serif;
}
.viewBut{
  width: 100px;
  height: 30px;
  border: 0px;
  border-radius: 6px;
  background-color: #e6e6e6;
}
.viewBut:hover{
  background-color: lightgray;
}

.ann-timezone,#addDiv{
  display: inline-block;
  padding-bottom: 0%;
}
#addDiv{
  padding-top: 18px;
}
</style>