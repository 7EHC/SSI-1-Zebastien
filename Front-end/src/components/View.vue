<script setup>
import { targetId } from "../composable/fetch";
import { onMounted, ref } from "vue";
import { changeTime } from "../composable/changeTime";
import { useRoute, useRouter,RouterLink} from "vue-router";

const { params } = useRoute();
const AnnDetail = ref([]);
const router = useRouter()
const updateAnn = ref({})
// let insertPubDate = ref()
// let insertCloseDate = ref()

const changeEdit = (editID) => {
  // router.push(`/admin/announcement/${params: id}/edit`)
  // router.push({ path: `/admin/announcement/${editID}/edit`})
  router.push({ name: 'Edit', params: { id: editID } })
}

onMounted(async () => {
  AnnDetail.value = await targetId(params.id);
  if(!AnnDetail.value) {
    alert('The request page is not available')
    router.push('/admin/announcement')
    AnnDetail.value = ""
  }

  // console.log(AnnDetail.value.announcementTitle)

  updateAnn.value = {
    announcementTitle: AnnDetail.value.announcementTitle,
    announcementCategory: AnnDetail.value.announcementCategory,
    announcementDescription: AnnDetail.value.announcementDescription,
    publishDate: AnnDetail.value.publishDate,
    closeDate: AnnDetail.value.closeDate,
    announcementDisplay: AnnDetail.value.announcementDisplay
  }
});
</script>

<template>
  <div class="big">
    <h2>Announcement Detail ::</h2>
    <table class="ann-item">
      <tr>
        <th>Title</th>
        <td class="ann-title">{{ AnnDetail.announcementTitle }}</td>
      </tr>
      <tr>
        <th>Category</th>
        <td class="ann-category">{{ AnnDetail.announcementCategory }}</td>
      </tr>
      <tr>
        <th>Description</th>
        <!-- <div v-html="AnnDetail.announcementDescription"></div> -->
        <td class="ann-description"><div style="border: 0.5px solid lightgray; padding: 10px;" v-html="AnnDetail.announcementDescription"></div> </td>
      </tr>
      <tr>
        <th>Publish Date</th>
        <td class="ann-publish-date">{{ changeTime(AnnDetail.publishDate) }}</td>
      </tr>
      <tr>
        <th>Close Date</th>
        <td class="ann-close-date">{{ changeTime(AnnDetail.closeDate) }}</td>
      </tr>
      <tr>
        <th>Display</th>
        <td class="ann-display">{{ AnnDetail.announcementDisplay }}</td>
      </tr>
    </table>
    <div class="butClass">
    <RouterLink :to="{ name: 'Announcement' }"><button class="ann-button">Back</button></RouterLink> 
    <button @click="changeEdit(AnnDetail.id)">edit</button>
    </div>
  </div>

</template>

<style scoped>
.edit {
  align-items: end;
  display: grid;
  margin-left: 5%;
  margin-right: 5%;
  font-family: 'Trebuchet MS', 'Lucida Sans Unicode', 'Lucida Grande', 'Lucida Sans', Arial, sans-serif;
}
.big {
  align-items: end;
  display: grid;
  margin-left: 14%;
  margin-right: 1%;
  font-family: 'Trebuchet MS', 'Lucida Sans Unicode', 'Lucida Grande', 'Lucida Sans', Arial, sans-serif;
}

table {
  border: 0.5px solid gray;
  border-radius: 2px;
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
  background-color: lightgray;
  font-weight: bold;
}

input{
  width: 98%;
  height: 36px;
  padding-left: 10px;
  border-radius: 7px;
}
select {
  height: 36px;
  width: 25%;
  padding-left: 10px;
  background-color: #e6e6e6;
  border-radius: 7px;
}

textarea {
  width: 99%;
  height: 200px;
}
</style>