<script setup>
import { useRoute, useRouter } from "vue-router";
import { ref,onMounted } from "vue"
import {getAnnouncement} from "../composable/fetch"

const API_ROOT = import.meta.env.VITE_ROOT_API
const router = useRouter()
const Announcement = ref([])
const newAnn = ref({})
const newTitle = ref('')
const newDesc = ref('')
const pubDate = ref('')
const pubTime = ref('')
const closeDate = ref('')
const closeTime = ref('')
const newDisplay = ref()
const newCateID = ref()


const addNewAnnouncement = async (newAnn) => {
    if(newTitle.value.length === 0 || newDesc.value.length === 0){
        alert('There is an error: Please enter the required information!! (Title, Description, and Category)')
        }
    try {
        // checkUpdateAccount(newAccount)
        const res = await fetch(`${API_ROOT}/announcements`,{
        // const res = await fetch('http://localhost:8080/api/announcements', {
            method: 'POST',
            headers: { 'content-type': 'application/json' },
            body: JSON.stringify(newAnn)
        }) //Add account at backend
        // method post. if it success, it will return status 201 / other methods return status 200
        if (res.status === 200) {
            // console.log('Okay')
            const AddAnnouncement = await res.json() //keep info that added from backend
            router.push('/admin/announcement')
        } else if(res.status === 500){
            throw new Error('Cannot add')
        }
    } catch (err) {
        console.log(err)
    }
}
onMounted(async ()=>{

  Announcement.value = await getAnnouncement()
  let runID = Announcement.value[0].id
  runID++
  newAnn.value = {
  announcementTitle: "",
  announcementDescription: "",
  publishDate: null,
  closeDate: null,
  announcementDisplay:"N",
  categoryId: 1,
  }

})

const fullPubDate = ()=>{
  if (pubDate.value.length&&pubTime.value.length > 0) {
    const showDate = new Date(`${pubDate.value}T${pubTime.value}:00`).toLocaleString()
    const finalShow = new Date(showDate).toISOString().split(".")
    return `${finalShow[0]}Z`
  }else {return null}
}

const fullCloseDate = ()=>{
  if (closeDate.value.length&&closeTime.value.length > 0) {
    const showDate = new Date(`${closeDate.value}T${closeTime.value}:00`).toLocaleString()
    return new Date(showDate).toISOString()
  }else {return null}
}

const submit = ()=>{
  newAnn.value.announcementTitle = newTitle.value === "" ? null : newTitle.value
  newAnn.value.announcementDescription = newDesc.value === "" ? null : newDesc.value
  newAnn.value.publishDate =  fullPubDate()
  newAnn.value.closeDate = fullCloseDate()
  newAnn.value.categoryId = newCateID.value === undefined ? 1 : newCateID.value
  newAnn.value.announcementDisplay = newDisplay.value ? 'Y':'N'
  addNewAnnouncement(newAnn.value)
  console.log(newDesc.value);
}

const updateTestEditor = (event) => {
  newDesc.value = event.target.innerHTML
}

</script>

<template>
  <div class="big">
    <h2>Add Announcement Detail ::</h2>

    <div class="form">
      <div>
        <p>Title</p>
        <input class="ann-title" v-model="newTitle" type="text" maxlength="200" placeholder="Enter less than 200 characters" />
      </div>

      <div>
        <p>Category</p>
        <select name="categoryName" v-model="newCateID" class="ann-category">
          <option id="1" value="1" >ทั่วไป</option>
          <option id="2" value="2" >ทุนการศึกษา</option>
          <option id="3" value="3" >หางาน</option>
          <option id="4" value="4" >ฝึกงาน</option>
        </select>
      </div>

      <div>
        <p>Description</p>
        <QuillEditor style="height: 200px;" toolbar="full" theme="snow"
              :value="newDesc"
              @input="updateTestEditor"/>
      </div>

      <div class="publishdate">
        <p>Publish Date</p>
        <input class="ann-publish-date" type="date" v-model="pubDate"/>
        <input class="ann-publish-time" type="time" v-model="pubTime"/>
      </div>

      <div class="closedate">
        <p>Close Date</p>
        <input class="ann-close-date" type="date" v-model="closeDate"/>
        <input class="ann-close-time" type="time" v-model="closeTime"/>
      </div>

      <div class="ann-display">
        <p>Display</p>
        <input type="checkbox" name="display" value="Y" v-model="newDisplay"><label> Check to show this announcement</label>
      </div>

      <div class="ann-button">
        <RouterLink :to="{ name: 'Announcement' }">
          <button class="ann-button-cancel">Cancel</button>
        </RouterLink>
        <button class="ann-button-back" type="submit" @click="submit">Submit</button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.textTitle {
  width: 98%;
  height: 36px;
  padding-left: 10px;
  border-radius: 7px;
}
.big {
  align-items: end;
  display: grid;
  margin-left: 13%;
  margin-right: 1%;
  font-family: "Trebuchet MS", "Lucida Sans Unicode", "Lucida Grande",
    "Lucida Sans", Arial, sans-serif;
}

div {
  margin-bottom: 20px;
}
.form {
  border: black solid 0.5px;
  border-radius: 2px;
  margin: 10px;
  padding: 15px;
  font-weight: bold;
}

.ann-title {
    width:98%;
    height: 36px;
    padding-left: 10px;
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

.ann-publish-date,
.ann-publish-time,
.ann-close-date,
.ann-close-time {
  padding-left: 10px;
  margin: 5px;
  height: 36px;
  border-radius: 7px;
}

.ann-button-back,
.ann-button-cancel {
  margin-top: 25px;
  margin-right: 10px;
  /* width: 5%; */
  height: 36px;
  border-radius: 7px;
  border: 0px;
}
.ann-button-back:hover {
  background-color: lightgreen;
  font-weight: bold;
}
.ann-button-cancel:hover {
  background-color: salmon;
  font-weight: bold;
}
</style>