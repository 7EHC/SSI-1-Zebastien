<script setup>
import { ref, onMounted } from "vue";
import { getAnnouncement, reqAccessToken } from "../composable/fetch";
import { changeTime } from "../composable/changeTime";
import { useRouter } from "vue-router";
import navBar from "./nav.vue";
import { useTokenStore } from "../stores/tokenStore.js";
import { DocumentPlusIcon, MagnifyingGlassPlusIcon, TrashIcon } from '@heroicons/vue/24/solid'
// import  DeletePopup  from "./DeletePopup.vue"
// import AddAnnouncement from "./AddAnnouncement.vue"
// import { useRoute, useRouter } from "vue-router";

const tokenStore = useTokenStore();
const { userRole } = useTokenStore()
const announcement = ref([]);
const timeZone = Intl.DateTimeFormat().resolvedOptions().timeZone;
const router = useRouter();
const confirmDelete = ref(false);
const idToDelete = ref();
const API_ROOT = import.meta.env.VITE_ROOT_API;

const deleteAnn = async (annID) => {
  try {
    const accessToken = ref(tokenStore.accessToken);
    const res = await fetch(
      `${API_ROOT}/announcements/${annID}`,
      // const res = await fetch(`http://ip22ssi1.sit.kmutt.ac.th:8080/api/announcements/${annID}`, { method: 'DELETE' }) //Delete backend
      // const res = await fetch(`http://localhost:8080/api/announcements/${annID}`, { method: 'DELETE' })
      {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${accessToken.value}`,
        },
      }
    );
    if (res.ok) {
      changeConfirm();
      announcement.value = announcement.value.filter((acc) => acc.id !== annID); //Delete frontend
      // console.log(announcement.value)
      router.push("/admin/announcement");
    } else if (res.status === 401) {
      const chekky = await reqAccessToken();
      // console.log(chekky);
      if (chekky === 'refresh expried'){
      alert("Session has expried, please try again.");
      router.push('/login')
    }
    } else {
      throw new Error(`Cannot delete`);
    }
  } catch (err) {
    alert(`Error: ${err}`);
  }
};

const gotoView = (viewId) => {
  router.push({ name: "View", params: { id: viewId } });
};

const changeConfirm = (id) => {
  confirmDelete.value = !confirmDelete.value;
  idToDelete.value = id;
  // console.log(confirmDelete.value)
};

onMounted(async () => {
  const check = await getAnnouncement();
  if (typeof check === "object") {
    announcement.value = check
  } else if (check === "new token success") {
  announcement.value = await getAnnouncement();
  } else if (!announcement.value) {
    announcement.value = [];
  } else if (check === "refresh expried") {
    alert("Session has expried, please try again.");
    router.push("/login");
  }

  // console.log(userRole)
  // console.log(announcement.value)
});
</script>

<template>
  <navBar/>
  <div class="divHead">
<div class="Header">
    <h1>SIT Announcement System (SAS)</h1>
  </div>
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
        <div class="modalBut">
          <button class="cancleBut" @click="changeConfirm">Cancel</button><RouterLink :to="{ name: 'Announcement' }">
        <button @click="deleteAnn(idToDelete)" class="deleteButPopup">Delete</button></RouterLink>
      </div>
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
   <RouterLink :to="{name: 'AddAnnouncement'}"><button id="addBut" style="float: right;"><DocumentPlusIcon style="height: 20px;" />
    </button>
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
        <th class="trHead" v-if="userRole === 'admin'">Owner</th>
        <th class="trHead">Action</th>
      </tr>
      <tr v-for="(ann, index) in announcement" :key="index" class="ann-item">
        <td>{{ index + 1 }}</td>
        <td class="ann-title">{{ ann.announcementTitle }}</td>
        <td class="ann-category">{{ ann.announcementCategory }}</td>
        <td class="ann-publish-date">{{ changeTime(ann.publishDate) }}</td>
        <td class="ann-close-date">{{ changeTime(ann.closeDate) }}</td>
        <td class="ann-display"><p class="disBack" :style="{ backgroundColor: ann.announcementDisplay === 'Y' ? 'lightgreen' : 'lightgray' }">{{ ann.announcementDisplay }}</p></td>
        <td v-if="userRole === 'admin'">{{ ann.announcementOwner }}</td>
        <td class="ann-button">
        <button class="viewBut" @click="gotoView(ann.id)"><MagnifyingGlassPlusIcon style="height: 15px;" /></button>
        <button class="deleteBut" @click="changeConfirm(ann.id)"><TrashIcon style="height: 15px;" /></button>

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
.modalBut {
  margin-top: -20px;
}
.divHead {
  margin-top: -8px;
  margin-left: -10px;
  margin-right: -8px;
  margin-bottom: 20px;
}
.Header {
  background-color: #ffff;
  box-shadow: 0 3px 4px rgba(0, 0, 0, 0.1);
  padding-left: 5px;
}
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
  border: 1px solid lightgray;
}

th {
  background-color: lightgray;
  border: 1px solid lightgray;
  height: 35px;
  font-size: large;
}

td {
  padding: 15px;
  padding-top: 0px;
  /* border: 1px solid lightgray; */
  border-bottom: 1px solid lightgray;
}

.viewBut{
  width: 90px;
  transition: background-color 0.3s;
}
.viewBut:hover{
  background-color: #45a29e;
  font-weight: bold;
}

.deleteBut{
  width: 90px;
  transition: background-color 0.3s;
}
.deleteBut:hover{
  background-color: #CD7474;
  font-weight: bold;
}
.cancleBut:hover{
  background-color: lightgray;
}

#addDiv{
  float: right;
  margin-bottom: 5px;
}
#addBut{
  width: 200px;
  transition: background-color 0.3s;
}
#addBut:hover{
  background-color: #45a29e;
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
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}
.modal>div {
  padding: 10px;
  border-radius: 10px;
  border: 0px;
}

button{
  border-radius: 8px;
  width: 90px;
  margin: 4px;
  margin-top: 30px;
  padding: 15px;
  border: 0px;
  transition-duration: 0.3s;
  cursor: pointer;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
button:hover {
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}
.cancleBut {
  width: 100px;
  height: 30px;
  border: 0px;
  border-radius: 6px;
  background-color: #e6e6e6;
  padding-bottom: 25px;
}

.deleteButPopup{
  background-color: salmon;
  width: 100px;
  height: 30px;
  border: 0px;
  border-radius: 6px;
  padding-bottom: 25px;
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
/*NEW--------------------------------------------------------------------------- */
.trHead{
  background-color: #1a1a1d;
  color: #45a29e;
}
#addBut::after{
  content: "add announcement ";
  display: none;
  color: black;
  transform: translateX(-10%);
}
#addBut:hover::after{
  display: block;
  float: right;
  padding-top: 4px;
}
.deleteBut::after{
  content: "delete";
  display: none;
  color: black;
  transform: translateX(-10%);
}
.deleteBut:hover::after{
  display: block;
  float: right;
  padding-top: 1px;
}
.viewBut::after{
  content: "view";
  display: none;
  color: black;
  transform: translateX(-10%);
}
.viewBut:hover::after{
  display: block;
  float: right;
  padding-top: 1px;
}
</style>
