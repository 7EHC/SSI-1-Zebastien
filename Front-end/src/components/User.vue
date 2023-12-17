<script setup>
import { ref, onMounted, computed, onBeforeMount, onUpdated, onBeforeUpdate } from "vue";
import { getAnnouncement, getPageAnn, getClosePageAnn } from "../composable/fetch";
import { changeTime } from "../composable/changeTime"
import { useRouter } from "vue-router";
import { usePageStore } from '../stores/pageStore.js'
import { useModeStore } from '../stores/modeStore.js'
import { useCategoryStore } from '../stores/categoryStore.js'
import navBar from "./nav.vue";
import { useTokenStore } from "../stores/tokenStore.js";

const { accessToken } = useTokenStore()
const router = useRouter()
const disableNext = ref(true)
const disableNextClose = ref(true)
const disablePrev = ref(true)
const allAnn = ref([])
const allAnnPage = ref([])
const allCloseAnnPage = ref([])
const pageStore = usePageStore()
const defaultPage = ref(pageStore.page)
const defaultPageClose = ref(pageStore.pageClose)
const filterAnnActive = ref([])
const filterAnnClose = ref([])
const timeZone = Intl.DateTimeFormat().resolvedOptions().timeZone
const modeStore = useModeStore()
const activeClose = ref(modeStore.AcCloseBut)
const categoryStore = useCategoryStore()
const categoryFil = ref(categoryStore.categoryFilter)
const arrPageNum = ref([])
const arrClosePageNum = ref([])

const visibleButton = computed(() => {
  const totalPages = filterAnnActive.value.totalPages
  const currentPage = defaultPage.value + 1;
  let startPage = 1;
  let endPage = totalPages;

  if (totalPages > 10) {
    if (currentPage <= 10) {
      endPage = 10;
    } else if (currentPage >= totalPages) {
      startPage = totalPages - 9;
    } else {
      startPage = currentPage - 9;
      endPage = currentPage;
    }
  }
  return Array.from({ length: endPage - startPage + 1 }, (_, i) => i + startPage);
});

const visibleCloseButton = computed(() => {
  const totalPages = filterAnnClose.value.totalPages
  const currentPage = defaultPageClose.value + 1;
  let startPage = 1;
  let endPage = totalPages;

  if (totalPages > 10) {
    if (currentPage <= 10) {
      endPage = 10;
    } else if (currentPage >= totalPages) {
      startPage = totalPages - 9;
    } else {
      startPage = currentPage - 9;
      endPage = currentPage;
    }
  }
  return Array.from({ length: endPage - startPage + 1 }, (_, i) => i + startPage);
});

const filterAc = async (category)=>{
  defaultPage.value = 0
  filterAnnActive.value = await getPageAnn(defaultPage.value ,category)

  defaultPageClose.value = 0
  filterAnnClose.value = await getClosePageAnn(defaultPageClose.value,category) 

  categoryStore.setCategory(category)
}


const changePage =async (page) =>{
  defaultPage.value = page - 1
  filterAnnActive.value = await getPageAnn(defaultPage.value,categoryFil.value)

  pageStore.setPage(defaultPage.value)
}

const changeClosePage =async (page) =>{
  defaultPageClose.value = page - 1
  filterAnnClose.value = await getClosePageAnn(defaultPageClose.value,categoryFil.value)

  pageStore.setClosePage(defaultPageClose.value)
}

const nextPage = async() => {
  defaultPage.value += 1 
  filterAnnActive.value = await getPageAnn(defaultPage.value,categoryFil.value)
  disableNext.value = defaultPage.value === filterAnnActive.value.totalPages - 1;
  
  pageStore.setPage(defaultPage.value)
}

const nextClosePage = async() => {
  defaultPageClose.value += 1
  filterAnnClose.value = await getClosePageAnn(defaultPageClose.value,categoryFil.value)

  pageStore.setClosePage(defaultPageClose.value)
}

const prevPage = async() => {
  defaultPage.value -= 1 
  filterAnnActive.value = await getPageAnn(defaultPage.value,categoryFil.value)

  pageStore.setPage(defaultPage.value)
}

const prevClosePage = async() => {
  defaultPageClose.value -= 1
  filterAnnClose.value = await getClosePageAnn(defaultPageClose.value,categoryFil.value)

  pageStore.setClosePage(defaultPageClose.value)
}


const activeBut = (acc)=>{
  modeStore.setMode(acc)
  if(acc === false){
    pageStore.setPage(defaultPage.value)
    disablePrev.value = defaultPage.value === 0;
    disableNext.value = defaultPage.value === filterAnnActive.value.totalPages - 1 ;
    return 'Active Announcements'
  } else {
    pageStore.setClosePage(defaultPageClose.value)
    disablePrev.value = defaultPageClose.value === 0;
    disableNextClose.value = defaultPageClose.value === filterAnnClose.value.totalPages - 1;
    return 'Closed Announcements'
  }
}   

const gotoView = (viewId) => {
  router.push({ name: 'UserView', params: { id: viewId } })
}

onMounted(async ()=>{
    allAnn.value = await getAnnouncement()

    allAnnPage.value = await getPageAnn(defaultPage.value)
    filterAnnActive.value = await getPageAnn(defaultPage.value)
    arrPageNum.value = Array.from({ length: allAnnPage.value.totalPages }, (_, i) => i + 1)
    
    allCloseAnnPage.value = await getClosePageAnn(defaultPageClose.value)
    filterAnnClose.value = await getClosePageAnn(defaultPageClose.value)
    arrClosePageNum.value = Array.from({ length: allCloseAnnPage.value.totalPages }, (_, i) => i + 1)

    filterAnnActive.value = await getPageAnn(defaultPage.value ,categoryFil.value)
    filterAnnClose.value = await getClosePageAnn(defaultPageClose.value,categoryFil.value) 

if(!filterAnnClose.value) {
  filterAnnClose.value = []
  }
if(!filterAnnActive.value) {
    filterAnnActive.value = []
  }
if((filterAnnClose.value.totalPages) === defaultPageClose.value){
  disableNextClose.value = true
  }
if((filterAnnActive.value.totalPages) === defaultPage.value){
  disableNext.value = true
  }
  

})

</script>
 
<template>
<div class="allContents1" v-if="accessToken">
  <div class="nav">
    <navBar v-if=accessToken />
  <div class="divHead">
    <h1 class="Header">SIT Announcement System (SAS)</h1>
  </div>
    <h4 class="timeZone">Date/Time shown in Timezone: {{ timeZone }}<br><br>
      <b v-if="activeClose===false" >Category:
          <select v-model="categoryFil" @change="filterAc(categoryFil)" class="ann-category-filter">
            <option id="0" value="0" >ทั้งหมด</option>
            <option id="1" value="1" >ทั่วไป</option>
            <option id="2" value="2" >ทุนการศึกษา</option>
            <option id="3" value="3" >หางาน</option>
            <option id="4" value="4" >ฝึกงาน</option>
          </select>
    </b>
    <b v-if="activeClose===true">Category: 
          <select v-model="categoryFil" @change="filterAc(categoryFil)" class="ann-category-filter">
            <option id="0" value="0" >ทั้งหมด</option>
            <option id="1" value="1" >ทั่วไป</option>
            <option id="2" value="2" >ทุนการศึกษา</option>
            <option id="3" value="3" >หางาน</option>
            <option id="4" value="4" >ฝึกงาน</option>
          </select>
    </b>
    </h4>
    <div class="ann-button" id="addDiv" >
        <button id="addBut" style="float: inline-end;" :style="{ backgroundColor: activeClose ? '#ffbaba':'lightgreen'}" @click="activeClose =! activeClose;" >{{ activeBut(activeClose) }}</button>
    </div>
   
    <div>
    <table v-if="activeClose === false">
      <tr class="tableHead">
        <th class="trHead">No.</th>
        <th class="trHead">Title</th>
        <th class="trHead">Category</th>
      </tr>
      <tr v-for="(ann, index) in filterAnnActive.content" :key="index" class="ann-item">
        <td>{{ ++index + (filterAnnActive.size*filterAnnActive.page) }}</td>
        <td class="ann-title" id="titleClick" @click="gotoView(ann.id)"><p>{{ ann.announcementTitle }}</p></td>
        <td class="ann-category">{{ ann.announcementCategory }}</td>
      </tr>
    </table>

    <table v-if="activeClose === true">
      <tr class="tableHead">
        <th class="trHead">No.</th>
        <th class="trHead">Title</th>
        <th class="trHead">Close Date</th>
        <th class="trHead">Category</th>
      </tr>
      <tr v-for="(ann, index) in filterAnnClose.content" :key="index" class="ann-item">
        <td>{{ ++index + (filterAnnClose.size*filterAnnClose.page) }}</td>
        <td class="ann-title" id="titleClick" @click="gotoView(ann.id)"><p>{{ ann.announcementTitle }}</p></td>
        <td class="ann-close-date">{{ changeTime(ann.closeDate) }}</td>
        <td class="ann-category">{{ ann.announcementCategory }}</td>
      </tr>
    </table>
    <h4 class="NoAlert" v-if="activeClose === true && filterAnnClose.totalPages === 0">No Announcements</h4>
    </div>

<!-- Button---------------------------------------------------------------------------------------------------- -->
<div class="allButtons1">
  <div v-if="filterAnnActive.totalPages > 1 && activeClose === false && arrPageNum !== undefined">
    <div class="butAcDiv">
    <button class="ann-page-prev" :style="{ cursor: disablePrev ? 'not-allowed':'pointer', backgroundColor: disablePrev ? 'lightgray':''}" @click="prevPage" :disabled="disablePrev">Prev</button>
    <button v-for="(page,index) in visibleButton" :key="index" :style="[page === defaultPage + 1 ? 'background-color: black':'', page === defaultPage + 1 ? 'color: white':'' ]" :class="['ann-page-'+ index]" @click="changePage(page)" class="butPage">{{ page }}</button>
    <button class="ann-page-next" :style="{ cursor: disableNext ? 'not-allowed':'pointer', backgroundColor: disableNext ? 'lightgray':''}" @click="nextPage" :disabled="disableNext">Next</button>
    </div>
</div>
<div v-if="filterAnnClose.totalPages > 1 && activeClose === true && arrClosePageNum !== undefined">
    <div class="butCloseDiv">
    <button class="ann-page-prev" :style="{ cursor: disablePrev ? 'not-allowed':'pointer', backgroundColor: disablePrev ? 'lightgray':''}" @click="prevClosePage" :disabled="disablePrev">Prev</button>
    <button v-for="(page,index) in visibleCloseButton" :key="index" :style="[page === defaultPageClose + 1 ? 'background-color: black':'', page === defaultPageClose + 1 ? 'color: white':'' ]" :class="['ann-page-'+ index]" @click="changeClosePage(page)" class="butPage">{{ page }}</button>
    <button class="ann-page-next" :style="{ cursor: disableNextClose ? 'not-allowed':'pointer', backgroundColor: disableNextClose ? 'lightgray':''}" @click="nextClosePage" :disabled="disableNextClose">Next</button>
    </div>
  </div>
</div>
<!-- Button---------------------------------------------------------------------------------------------------- -->

<!-- No Announcement------------------------------------------------------------------------------------------------ -->
    <h4 class="NoAlert" v-if="activeClose === false && filterAnnActive.totalPages === 0">No Announcements</h4>
    <h4 class="NoAlert" v-if="filterAnnActive.length === 0">Can't load data</h4>
<!-- No Announcement------------------------------------------------------------------------------------------------ -->

</div>
</div>

<div class="allContents2" v-if="!accessToken">
  <div class="divHead">
    <h1 class="Header">SIT Announcement System (SAS)</h1>
    <div style="position: absolute; top: 0; right: 0;">
    <RouterLink :to="{ name: 'Login' }"
          ><button class="login">Login</button></RouterLink
        ></div>
  </div>
    <h4 class="timeZone">Date/Time shown in Timezone: {{ timeZone }}<br><br>
      <b v-if="activeClose===false" >Category:
          <select v-model="categoryFil" @change="filterAc(categoryFil)" class="ann-category-filter">
            <option id="0" value="0" >ทั้งหมด</option>
            <option id="1" value="1" >ทั่วไป</option>
            <option id="2" value="2" >ทุนการศึกษา</option>
            <option id="3" value="3" >หางาน</option>
            <option id="4" value="4" >ฝึกงาน</option>
          </select>
    </b>
    <b v-if="activeClose===true">Category: 
          <select v-model="categoryFil" @change="filterAc(categoryFil)" class="ann-category-filter">
            <option id="0" value="0" >ทั้งหมด</option>
            <option id="1" value="1" >ทั่วไป</option>
            <option id="2" value="2" >ทุนการศึกษา</option>
            <option id="3" value="3" >หางาน</option>
            <option id="4" value="4" >ฝึกงาน</option>
          </select>
    </b>
    </h4>
    <div class="ann-button" id="addDiv" >
        <button id="addBut" style="float: inline-end;" :style="{ backgroundColor: activeClose ? '#ffbaba':'lightgreen'}" @click="activeClose =! activeClose;" >{{ activeBut(activeClose) }}</button>
    </div>
   
    <div>
    <table v-if="activeClose === false">
      <tr class="tableHead">
        <th class="trHead">No.</th>
        <th class="trHead">Title</th>
        <th class="trHead">Category</th>
      </tr>
      <tr v-for="(ann, index) in filterAnnActive.content" :key="index" class="ann-item">
        <td>{{ ++index + (filterAnnActive.size*filterAnnActive.page) }}</td>
        <td class="ann-title" id="titleClick" @click="gotoView(ann.id)"><p>{{ ann.announcementTitle }}</p></td>
        <td class="ann-category">{{ ann.announcementCategory }}</td>
      </tr>
    </table>

    <table v-if="activeClose === true">
      <tr class="tableHead">
        <th class="trHead">No.</th>
        <th class="trHead">Title</th>
        <th class="trHead">Close Date</th>
        <th class="trHead">Category</th>
      </tr>
      <tr v-for="(ann, index) in filterAnnClose.content" :key="index" class="ann-item">
        <td>{{ ++index + (filterAnnClose.size*filterAnnClose.page) }}</td>
        <td class="ann-title" id="titleClick" @click="gotoView(ann.id)"><p>{{ ann.announcementTitle }}</p></td>
        <td class="ann-close-date">{{ changeTime(ann.closeDate) }}</td>
        <td class="ann-category">{{ ann.announcementCategory }}</td>
      </tr>
    </table>
    <h4 class="NoAlert" v-if="activeClose === true && filterAnnClose.totalPages === 0">No Announcements</h4>
    </div>

<!-- Button---------------------------------------------------------------------------------------------------- -->
<div class="allButtons2">
  <div v-if="filterAnnActive.totalPages > 1 && activeClose === false && arrPageNum !== undefined">
    <div class="butAcDiv">
    <button class="ann-page-prev" :style="{ cursor: disablePrev ? 'not-allowed':'pointer', backgroundColor: disablePrev ? 'lightgray':''}" @click="prevPage" :disabled="disablePrev">Prev</button>
    <button v-for="(page,index) in visibleButton" :key="index" :style="[page === defaultPage + 1 ? 'background-color: black':'', page === defaultPage + 1 ? 'color: white':'' ]" :class="['ann-page-'+ index]" @click="changePage(page)" class="butPage">{{ page }}</button>
    <button class="ann-page-next" :style="{ cursor: disableNext ? 'not-allowed':'pointer', backgroundColor: disableNext ? 'lightgray':''}" @click="nextPage" :disabled="disableNext">Next</button>
    </div>
</div>
<div v-if="filterAnnClose.totalPages > 1 && activeClose === true && arrClosePageNum !== undefined">
    <div class="butCloseDiv">
    <button class="ann-page-prev" :style="{ cursor: disablePrev ? 'not-allowed':'pointer', backgroundColor: disablePrev ? 'lightgray':''}" @click="prevClosePage" :disabled="disablePrev">Prev</button>
    <button v-for="(page,index) in visibleCloseButton" :key="index" :style="[page === defaultPageClose + 1 ? 'background-color: black':'', page === defaultPageClose + 1 ? 'color: white':'' ]" :class="['ann-page-'+ index]" @click="changeClosePage(page)" class="butPage">{{ page }}</button>
    <button class="ann-page-next" :style="{ cursor: disableNextClose ? 'not-allowed':'pointer', backgroundColor: disableNextClose ? 'lightgray':''}" @click="nextClosePage" :disabled="disableNextClose">Next</button>
    </div>
  </div>
</div>
<!-- Button---------------------------------------------------------------------------------------------------- -->

<!-- No Announcement------------------------------------------------------------------------------------------------ -->
    <h4 class="NoAlert" v-if="activeClose === false && filterAnnActive.totalPages === 0">No Announcements</h4>
    <h4 class="NoAlert" v-if="filterAnnActive.length === 0">Can't load data</h4>
<!-- No Announcement------------------------------------------------------------------------------------------------ -->


</div>
</template>
 
<style scoped>
.divHead {
  margin-left: -10px;
  margin-right: -8px;
}
.Header {
  background-color: #ffff; /* Set your desired background color */
  box-shadow: 0 3px 4px rgba(0, 0, 0, 0.1); /* Adjust the values as needed */
  width: 100%;
  padding-bottom: 20px;
}

.ann-category-filter {
  cursor: pointer;
}

.allContents1 {
  font-family: 'Trebuchet MS', 'Lucida Sans Unicode', 'Lucida Grande', 'Lucida Sans', Arial, sans-serif;
  /* margin-left: 13.5%; */
}

.nav {
  margin-left: 13.5%;
}

.allContents2 {
  font-family: 'Trebuchet MS', 'Lucida Sans Unicode', 'Lucida Grande', 'Lucida Sans', Arial, sans-serif;
}

.timeZone {
  justify-content: start;
  margin-bottom: -5%;
  font-weight: lighter;
  margin-top: 10px;
}

.NoAlert {
  display: flex;
  justify-content: center;
  margin-top: 10%;
}
.butPage{
  margin: 20px 1;
  border-radius: 5px;
  height: 30px;
  padding-top: 9px;
  width: 70px;
}

table {
  margin-top: 100px;
  border-collapse: collapse;
  width: 100%;
  border: 1px solid lightgray;
}
.trHead{
  background-color: #1a1a1d;
  color: #45a29e;
  /* border: transparent; */
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
  border-radius: 5px;
  /* border: transparent; */
}

button{
  padding: 8px;
  /* width: 90px; */
  border-radius: 8px;
  margin: 4px;
  border: 0px;
}

.allButtons1 {
  display: flex;
  position: fixed;
  justify-content: center;
  margin-left: 13.5%;
  margin-bottom: 5px;
  height: 70px;
  left: 0;
  right: 0;
  bottom: 0;
}

.allButtons2 {
  display: flex;
  position: fixed;
  justify-content: center;
  margin-bottom: 5px;
  height: 70px;
  left: 0;
  right: 0;
  bottom: 0;
}

select {
  height: 36px;
  width: 120px;
  padding-left: 10px;
  background-color: #e6e6e6;
  border-radius: 7px;
}

#addDiv{
  float: right;
  margin-bottom: 5px;
}
#addBut{
  /* background-color: lightgreen; */
  /* margin-top: 5px; */
  font-weight: bold;
  transition-duration: 0.3s;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  width: 190px;
}
#addBut:hover{
  /* background-color: mediumseagreen; */
  /* font-weight: bold; */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}
.ann-button{
  text-align: center;
  width: 200px;
  cursor: pointer;
}
button{
  border-radius: 8px;
  width: 90px;
  margin: 4px;
  margin-top: 30px;
  padding: 15px;
  border: 0px;
  transition-duration: 0.4s;
  cursor: pointer;
}
#titleClick{transition: background-color 0.3s;}
#titleClick:hover{
    color: black;
    background-color: lightgray;
    cursor: pointer;
}
.butPage:hover{
background-color: lightgray;
transition-duration: 0.4s;
}
.ann-page-next:hover,
.ann-page-prev:hover {
  background-color: lightgray;
}
.ann-page-next,
.ann-page-prev {
  width: 70px;
  margin: 20px 1;
  border-radius: 5px;
  height: 30px;
  padding-top: 9px;
}
.Header {
  display: flex;
  justify-content: center;
  font-family: 'Trebuchet MS', 'Lucida Sans Unicode', 'Lucida Grande', 'Lucida Sans', Arial, sans-serif;
}
.login {
  margin-right: 10px;
  margin-top: 20px;
  border: 1px solid #45a29e;
  transition-duration: 0.3s;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  background-color: #ffff;
  color: #45a29e;
  font-weight: bold;
}
.login:hover {
  font-weight: bold;
  border: 1px solid #45a29e;
  background-color: #45a29e;
  color: white;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

</style>