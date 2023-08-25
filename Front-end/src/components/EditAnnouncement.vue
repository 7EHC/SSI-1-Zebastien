<script setup>
import { RouterLink, useRoute,useRouter } from "vue-router";
import { targetId } from "../composable/fetch";
import { onMounted,ref } from "vue";

const announceById = ref([])
const router = useRouter()
const { params } = useRoute()
const newData = ref({})
const disBut = ref(true)
const cloneNewData = ref({})
const API_ROOT = import.meta.env.VITE_ROOT_API
const pubDate = ref()
const pubTime = ref()
const closeDate = ref()
const closeTime = ref()
const clonePubDate = ref()
const clonePubTime = ref()
const cloneCloseDate = ref()
const cloneCloseTime = ref()

const toCateId = (name)=>{
    if(name === "ทั่วไป"){
    return 1
    }else if(name === "ทุนการศึกษา"){
        return 2
    }else if(name === "หางาน"){
        return 3
    }else if(name === "ฝึกงาน"){
      return 4
    }
}

const hasDataChanged = ()=> {
  const pub = new Date(`${pubDate.value}T${pubTime.value}:00`).toLocaleString()
  const close = new Date(`${closeDate.value}T${closeTime.value}:00`).toLocaleString()
  const pubShow = new Date(pub).toISOString().split(".")
  const closeShow = new Date(close).toISOString().split(".")
  newData.value.publishDate = `${pubShow[0]}Z` === "1999-12-31T17:00:00Z" ? null : `${pubShow[0]}Z`
  newData.value.closeDate = `${closeShow[0]}Z` === "1999-12-31T17:00:00Z" ? null : `${closeShow[0]}Z`

    if (JSON.stringify(cloneNewData.value) === JSON.stringify(newData.value)) {
      disBut.value = true
    } else {
      disBut.value = false
    }

}

const modifyAccount = async (updateAccount) => {
    try {
        const res = await fetch(`${API_ROOT}/announcements/${params.id}`, {
        // const res = await fetch(`http://localhost:8080/api/announcements/${params.id}`, {
            method: 'PUT', // put = replace all record, all field
            // patch = edit some field
            headers: { 'content-type': 'application/json' },
            body: JSON.stringify(updateAccount) //edit backend
        })
        // method put. if it success. it will return status 200
        if (res.status === 200){
            router.push('/admin/announcement')
        } else if(res.status === 500 ){
            throw new Error('Cannot edit')
        }
    } catch (err) {
        console.log(err)
    }
}

const checkShow = (display) => {
  if(display === 'Y') {
  return true
} else {
  return false
}
}

const submit = ()=>{
  const pub = new Date(`${pubDate.value}T${pubTime.value}:00`).toLocaleString()
  const close = new Date(`${closeDate.value}T${closeTime.value}:00`).toLocaleString()
  const pubShow = new Date(pub).toISOString().split(".")
  const closeShow = new Date(close).toISOString().split(".")

  const objToSent = {
    announcementTitle: newData.value.announcementTitle,
    announcementDescription: newData.value.announcementDescription,
    publishDate: `${pubShow[0]}Z` === "1999-12-31T17:00:00Z" ? null : `${pubShow[0]}Z`,
    closeDate: `${closeShow[0]}Z` === "1999-12-31T17:00:00Z" ? null : `${closeShow[0]}Z`,
    announcementDisplay: newData.value.announcementDisplay? 'Y':'N',
    categoryId: toCateId(newData.value.categoryId)
}
// || (Object.keys(objToSent.publishDate.includes(undefined)) || (Object.keys(objToSent.closeDate.includes(undefined)
   if((Object.keys(objToSent.announcementTitle).length === 0 || (Object.keys(objToSent.announcementDescription).length === 0))){
    objToSent.announcementTitle = null
    objToSent.announcementDescription = null
    newData.value = cloneNewData.value
    alert('There is an error: You have entered incorrect data(Your input data may be whitespace or an empty string)')
   }
  modifyAccount(objToSent)

}

onMounted(async ()=>{
announceById.value = await targetId(params.id)

if(announceById.value.publishDate !== null){
 pubDate.value = new Date(announceById.value.publishDate).toLocaleDateString("en-CA" , {dateStyle: "short"})
 pubTime.value = new Date(announceById.value.publishDate).toLocaleTimeString("it-IT" , {hour: "2-digit", minute:"2-digit"})
 clonePubDate.value = new Date(announceById.value.publishDate).toLocaleDateString("en-CA" , {dateStyle: "short"})
 clonePubTime.value = new Date(announceById.value.publishDate).toLocaleTimeString("it-IT" , {hour: "2-digit", minute:"2-digit"})
}else pubDate.value = null

if(announceById.value.closeDate !== null){
 closeDate.value = new Date(announceById.value.closeDate).toLocaleDateString("en-CA" , {dateStyle: "short"})
 closeTime.value = new Date(announceById.value.closeDate).toLocaleTimeString("it-IT" , {hour: "2-digit", minute:"2-digit"})
 cloneCloseDate.value = new Date(announceById.value.publishDate).toLocaleDateString("en-CA" , {dateStyle: "short"})
 cloneCloseTime.value = new Date(announceById.value.publishDate).toLocaleTimeString("it-IT" , {hour: "2-digit", minute:"2-digit"})
}else closeDate.value = null

newData.value = {
    announcementTitle: announceById.value.announcementTitle,
    announcementDescription: announceById.value.announcementDescription,
    publishDate: announceById.value.publishDate,
    closeDate: announceById.value.closeDate ,
    announcementDisplay: checkShow(announceById.value.announcementDisplay),
    categoryId: announceById.value.announcementCategory
}

cloneNewData.value = Object.assign({},newData.value)
})

</script>

<template>
<div class="edit">
    <h2>Announcement Detail ::</h2>
    <table>
      <tr class="ann-item">
        <th>Title</th>
        <td><input class="ann-title" type="text" maxlength="200" v-model.trim="newData.announcementTitle" v-on:input="hasDataChanged" :required="true"></td>
      </tr>
      <tr>
        <th>Category</th>
        <td>
            <select class="ann-category" name="categoryName" id="categoryName" v-model="newData.categoryId" v-on:change="hasDataChanged">
          <option id="1" value="ทั่วไป">ทั่วไป</option>
          <option id="2" value="ทุนการศึกษา" >ทุนการศึกษา</option>
          <option id="3" value="หางาน" >หางาน</option>
          <option id="4" value="ฝึกงาน" >ฝึกงาน</option>
        </select></td>
      </tr>
      <tr>
        <th>Description</th>
        <td>
          <QuillEditor style="height: 200px;" toolbar="full" theme="snow"
              v-model.trim:content="newData.announcementDescription" content-type="html"
              @input="hasDataChanged"/>
        </td>
      </tr>
      <tr>
        <th>Publish Date</th>
        <div>
        <td><input class="ann-publish-date" v-model="pubDate" type="date" v-on:input="hasDataChanged"></td>
        <td><input class="ann-publish-time" v-model="pubTime" type="time" v-on:input="hasDataChanged"></td>
        </div>
      </tr>
      <tr>
        <th>Close Date</th>
        <div>
        <td><input class="ann-close-date" v-model="closeDate" type="date" v-on:input="hasDataChanged"></td>
        <td><input class="ann-close-time" v-model="closeTime" type="time" v-on:input="hasDataChanged"></td>
        </div>
      </tr>
      <tr>
        <th>Display</th>
        <td>
        <input style="height: 15px; width: 15px;" type="checkbox" class="ann-display" name="announcementDisplay" id="announcementDisplay" v-model=newData.announcementDisplay v-on:change="hasDataChanged">
        <label> Check to show this announcement</label>
      </td>
      </tr>
    </table>
    <div class="ann-button">
    <RouterLink :to="{name: 'View'}"><button class="cancelBut">Cancel</button></RouterLink>
    <button class="submitBut" :disabled="disBut" @click="submit">submit</button>
    </div>
</div>
</template>

<style scoped>
.edit {
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
.cancelBut:hover{
    background-color: salmon;
}
.submitBut:hover{
    background-color: lightgreen;
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