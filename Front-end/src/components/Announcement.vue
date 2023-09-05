<script setup>
import { ref, onMounted } from "vue";
import { getAnnouncement } from "../composable/fetch";
import { changeTime } from "../composable/changeTime"
import { useRouter } from "vue-router";
// import  DeletePopup  from "./DeletePopup.vue"
// import AddAnnouncement from "./AddAnnouncement.vue"
// import { useRoute, useRouter } from "vue-router";

const announcement = ref([]);
const timeZone = Intl.DateTimeFormat().resolvedOptions().timeZone
const router = useRouter()
const confirmDelete = ref(false)
const idToDelete = ref()
const API_ROOT = import.meta.env.VITE_ROOT_API

const deleteAnn = async (annID) => {
    try {
        const res = await fetch(`${API_ROOT}/announcements/${annID}`, { method: 'DELETE' });
        // const res = await fetch(`http://ip22ssi1.sit.kmutt.ac.th:8080/api/announcements/${annID}`, { method: 'DELETE' }) //Delete backend
        // const res = await fetch(`http://localhost:8080/api/announcements/${annID}`, { method: 'DELETE' })
        if (res.ok) {
         changeConfirm()
         announcement.value = announcement.value.filter((acc) => acc.id !== annID) //Delete frontend
          // console.log(announcement.value)
            router.push('/admin/announcement')
        } else {
            throw new Error(`Cannot delete`)
        }
    } catch (err) {
        alert(`Error: ${err}`)
    }
}

const gotoView = (viewId) => {
  router.push({ name: 'View', params: { id: viewId } })
}

const changeConfirm = (id) => {
  confirmDelete.value = !confirmDelete.value
  idToDelete.value = id
  // console.log(confirmDelete.value)
}

onMounted(async () => {
  announcement.value = await getAnnouncement()
  // console.log(announcement.value)
  if(!announcement.value) {
    announcement.value = []
  }
});
</script>

<template>
<div class="Header">
    <h1>SIT Announcement System (SAS)</h1>
  </div>
  <!-- <RouterLink :to="{name : 'SasUser'}"><button class="admin-back">Back to admin page</button></RouterLink>
  <RouterLink :to="{name: 'User'}"><button class="admin-back">User</button></RouterLink> -->
  <!-- Delete Confirmation---------------------------------------------------------------------------- -->
  <div class="popup">
  <Teleport to="body">
    <div class="modal" style="background-color: rgba(0, 0, 0, 0.3); width: 100%;" v-if="confirmDelete === true">
      <!-- <div class="modal" style="background-color: rgba(0, 0, 0, 0.3); width: 100%;"> -->
      <div class="window" style="height: 40%; width: 50%; background-color: white;">
        <p style="font-size: 48px;">Delete</p>
        <hr>
        <p style="font-size: 24px; margin-top: 30px;">Do you want to delete these records ?</p>
        <button class="viewBut" @click="changeConfirm">Cancel</button><RouterLink :to="{ name: 'Announcement' }">
        <button @click="deleteAnn(idToDelete)" class="deleteButPopup">Delete</button></RouterLink>
      </div>
    </div>
  </Teleport>
</div>
<!-- ------------------------------------------------------------------------------------------------- -->

<!-- Main Content------------------------------------------------------------------------------------- -->
  <div class="allContents">
    <!-- <RouterLink :to="{name : 'SasUser'}"><button>Back to admin page</button></RouterLink> -->
  <h4 class="timeZone">Date/Time shown in Timezone: {{ timeZone }}</h4>
  <div class="ann-button" id="addDiv">
   <RouterLink :to="{name: 'AddAnnouncement'}">
    <button id="addBut" style="float: right;">Add Announcement</button>
   </RouterLink>
  </div>
  <div v-show="true" class="bigTable">
    <table>
      <tr class="tableHead">
        <th class="trHead">No.</th>
        <th class="trHead">Title</th>
        <th class="trHead">Category</th>
        <th class="trHead">Publish Date</th>
        <th class="trHead">Close Date</th>
        <th class="trHead">Display</th>
        <th class="trHead">Action</th>
      </tr>
      <tr v-for="(ann, index) in announcement" :key="index" class="ann-item">
        <td>{{ index + 1 }}</td>
        <td class="ann-title">{{ ann.announcementTitle }}</td>
        <td class="ann-category">{{ ann.announcementCategory }}</td>
        <td class="ann-publish-date">{{ changeTime(ann.publishDate) }}</td>
        <td class="ann-close-date">{{ changeTime(ann.closeDate) }}</td>
        <td class="ann-display"><p class="disBack" :style="{ backgroundColor: ann.announcementDisplay === 'Y' ? 'mediumseagreen' : 'lightgray' }">{{ ann.announcementDisplay }}</p></td>
        <td class="ann-button">
        <button class="viewBut" @click="gotoView(ann.id)">view</button>
        <button class="deleteBut" @click="changeConfirm(ann.id)">delete</button>

        </td>
      </tr>
    </table>
  </div>
  <h4 class="NoAlert" v-if="announcement.length === 0">No Announcement</h4>
</div>
<!-- ------------------------------------------------------------------------------------------------- -->
<!-- <RouterLink :to="{name: 'User'}">User</RouterLink> -->
</template>

<style scoped>
.allContents {
  font-family: 'Trebuchet MS', 'Lucida Sans Unicode', 'Lucida Grande', 'Lucida Sans', Arial, sans-serif;
  margin-left: 13.5%;
}

.timeZone {
  justify-content: start;
  margin-bottom: -4%;
  font-weight: lighter;
  margin-top: 10px;
}

.NoAlert {
  display: flex;
  justify-content: center;
  margin-top: 10%;
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

button{
  padding: 8px;
  /* width: 90px; */
  border-radius: 8px;
  margin: 4px;
  border: 0px;
}

.viewBut{
  width: 90px;
}
.viewBut:hover{
  background-color: lightgray;
  font-weight: bold;
}

.deleteBut{
  /* background-color: coral; */
  width: 90px;
}
.deleteBut:hover{
  background-color: salmon;
  font-weight: bold;
}

#addDiv{
  float: right;
  margin-bottom: 5px;
}
#addBut{
  /* background-color: lightgreen; */
  width: 160px;
}
#addBut:hover{
  background-color: mediumseagreen;
  font-weight: bold;
}

.ann-button{
  text-align: center;
  width: 200px;
}


.window{
  text-align: center;
  font-family: 'Trebuchet MS', 'Lucida Sans Unicode', 'Lucida Grande', 'Lucida Sans', Arial, sans-serif;
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

button{
  border-radius: 8px;
  width: 90px;
  margin: 4px;
  margin-top: 30px;
  padding: 15px;
  border: 0px;
}

.viewBut{
  width: 90px;
}
.viewBut:hover{
  background-color: lightgray;
}

.deleteButPopup{
  /* background-color: coral; */
  background-color: salmon;
  width: 90px;
}
.deleteButPopup:hover{
  background-color: indianred;
}

.disBack{
  padding: 8px;
  color: black;
  width: 10px;
  border: 0px;
  border-radius: 30%;
  font-weight: bold;
}
.Header {
  display: flex;
  margin-left: 13%;
  justify-content: center;
  font-family: 'Trebuchet MS', 'Lucida Sans Unicode', 'Lucida Grande', 'Lucida Sans', Arial, sans-serif;
}
.admin-back{
  width: 160px;
}
.admin-back:hover{
  font-weight: bold;
  background-color: lightgray;
}
</style>