<script setup>
import { targetId } from "../composable/fetch";
import { onMounted, ref } from "vue";
import { changeTime } from "../composable/changeTime";
import { useRoute, useRouter, RouterLink } from "vue-router";
import { useModeStore } from '../stores/modeStore.js'
import navBar from "./nav.vue";
import { useTokenStore } from "../stores/tokenStore.js";

const { accessToken } = useTokenStore()
const router = useRouter()
const modeStore = useModeStore()
const checkActiveClose = ref(modeStore.AcCloseBut)
const { params } = useRoute();
const AnnDetail = ref([]);

onMounted(async () => {
  AnnDetail.value = await targetId(params.id);
  if (!AnnDetail.value) {
    alert("The request page is not available");
    router.push("/admin/announcement");
    AnnDetail.value = "";
  }
});

</script>

<template>
  <div class="big1" v-if="accessToken">
    <div class="nav">
    <navBar />
    <h2>Announcement Detail ::</h2>
    <div class="ann-item">
      <div class="head">
        <h1 class="ann-title">
          {{ AnnDetail.announcementTitle }}<br /><span class="ann-category">{{
            AnnDetail.announcementCategory
          }}</span>
          <span class="ann-close-date" v-if="checkActiveClose === true">Closed On: {{ changeTime(AnnDetail.closeDate) }}</span>
        </h1>
        <hr style="border: 0.1px solid lightgray" />
        <div style="width: 100%">
          <p class="ann-description" style="font-size: 18px;">
            <!-- <b style="font-size: 20px;">Description: </b> -->
            <div v-html="AnnDetail.announcementDescription"></div>
          </p>
        </div>
      </div>
      <hr style="border: 0.1px solid lightgray" />
      <div class="butClass">
        <RouterLink :to="{ name: 'User' }"
          ><button class="ann-button">Back</button></RouterLink
        >
      </div>
    </div>
  </div>
  </div>

  <div class="big2" v-if="!accessToken">
    <h2>Announcement Detail ::</h2>
    <div class="ann-item">
      <div class="head">
        <h1 class="ann-title">
          {{ AnnDetail.announcementTitle }}<br /><span class="ann-category">{{
            AnnDetail.announcementCategory
          }}</span>
          <span class="ann-close-date" v-if="checkActiveClose === true">Closed On: {{ changeTime(AnnDetail.closeDate) }}</span>
        </h1>
        <hr style="border: 0.1px solid lightgray" />
        <div style="width: 100%">
          <p class="ann-description" style="font-size: 18px;">
            <!-- <b style="font-size: 20px;">Description: </b> -->
            <div v-html="AnnDetail.announcementDescription"></div>
          </p>
        </div>
      </div>
      <hr style="border: 0.1px solid lightgray" />
      <div class="butClass">
        <RouterLink :to="{ name: 'User' }"
          ><button class="ann-button">Back</button></RouterLink
        >
      </div>
    </div>
  </div>
</template>

<style scoped>
.ann-title, .ann-description{
    padding-left: 25px;
    white-space: pre-line;
}
.big1 {
  align-items: start;
  display: grid;
  margin-left: 13.5%;
  margin-right: 5%;
  font-family: 'Trebuchet MS', 'Lucida Sans Unicode', 'Lucida Grande', 'Lucida Sans', Arial, sans-serif;
}
.big2 {
  align-items: start;
  display: grid;
  margin-left: 5%;
  margin-right: 5%;
  font-family: 'Trebuchet MS', 'Lucida Sans Unicode', 'Lucida Grande', 'Lucida Sans', Arial, sans-serif;
}
span{
    font-weight: lighter;
    font-size: medium;
    color: gray;
}

.ann-close-date {
  float: right;
  margin-right: 2%;
}
.ann-button{
    width: 100px;
    cursor: pointer;
    transition-duration: 0.3s;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.ann-item {
  border: 1px solid lightgray;
  border-radius: 10px;
  width: 100%;
}

tr {
  border: solid black 2px;
}

tr,
th,
td {
  margin: 10px;
  padding: 15px;
}

button {
  margin: 15px;
  padding: 8px;
  width: 10%;
  height: 36px;
  border-radius: 7px;
  border: 0px;
}
button:hover{
  /* background-color: lightgray; */
  font-weight: bold;
  background-color: #ffbaba;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}
</style>
