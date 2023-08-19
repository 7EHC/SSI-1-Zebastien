<script setup>
import { useRoute, useRouter } from "vue-router";
import { ref,onMounted } from "vue"

const router = useRouter()
const timeZone = Intl.DateTimeFormat().resolvedOptions().timeZone
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

    <div class="parent-container">
        <h1 class="user-man">User Management</h1>
        <!-- <h4 class="timeZone">Date/Time shown in Timezone: {{ timeZone }}</h4> -->
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
            </table>
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
.ann-timezone,.bigTable{
    padding-left: 12%;
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
  margin-top: 30px;
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
#addBut{
  /* background-color: lightgreen; */
  width: 100px;
  height: 30px;
  border: 0px;
  border-radius: 6px;
  background-color: #e6e6e6;
  font-weight: bold;
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