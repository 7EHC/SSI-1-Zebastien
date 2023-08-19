<script setup>
import { ref, onMounted, computed, onBeforeMount, onUpdated, onBeforeUpdate } from "vue";
import { getAnnouncement, getPageAnn, getClosePageAnn } from "../composable/fetch";
import { changeTime } from "../composable/changeTime"
import { useRouter } from "vue-router";
import { usePageStore } from '../stores/pageStore.js'
import { useModeStore } from '../stores/modeStore.js'
import { useCategoryStore } from '../stores/categoryStore.js'

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
    return 'Closed Announcements'
  } else {
    pageStore.setClosePage(defaultPageClose.value)
    disablePrev.value = defaultPageClose.value === 0;
    disableNextClose.value = defaultPageClose.value === filterAnnClose.value.totalPages - 1;
    return 'Active Announcements'
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
  <div class="Header">
    <h1>SIT Announcement System (SAS)</h1>
  </div>
<div class="allContents">
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
        <button id="addBut" style="float: inline-end;" @click="activeClose =! activeClose;" >{{ activeBut(activeClose) }}</button>
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
<div class="allButtons">
<div v-if="filterAnnActive.totalPages > 1 && activeClose === false && arrPageNum !== undefined">
    <div class="butAcDiv">
    <button class="ann-page-prev" @click="prevPage" :disabled="disablePrev">Prev</button>
    <button v-for="(page,index) in visibleButton" :key="index" :style="[page === defaultPage + 1 ? 'background-color: lightgray':'' ]" :class="['ann-page-'+ index]" @click="changePage(page)" class="butPage">{{ page }}</button>
    <button class="ann-page-next" @click="nextPage" :disabled="disableNext">Next</button>
    </div>
</div>
<div v-if="filterAnnClose.totalPages > 1 && activeClose === true && arrClosePageNum !== undefined">
    <div class="butCloseDiv">
    <button class="ann-page-prev" @click="prevClosePage" :disabled="disablePrev">Prev</button>
    <button v-for="(page,index) in visibleCloseButton" :key="index" :style="[page === defaultPageClose + 1 ? 'background-color: lightgray':'' ]" :class="['ann-page-'+ index]" @click="changeClosePage(page)" class="butPage">{{ page }}</button>
    <button class="ann-page-next" @click="nextClosePage" :disabled="disableNextClose">Next</button>
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
.allContents {
  font-family: 'Trebuchet MS', 'Lucida Sans Unicode', 'Lucida Grande', 'Lucida Sans', Arial, sans-serif;
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
.butPage{
  margin: 20px 0 0 0 ;
  border-radius: 0;
}

table {
  margin-top: 100px;
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

.allButtons {
  display: flex;
  position: relative;
  justify-content: center;
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
  width: 190px;
}
#addBut:hover{
  /* background-color: mediumseagreen; */
  font-weight: bold;
}
.ann-button{
  text-align: center;
  width: 200px;
}
button{
  border-radius: 8px;
  width: 90px;
  margin: 4px;
  margin-top: 30px;
  padding: 15px;
  border: 0px;
}
#titleClick:hover{
    color: black;
    background-color: lightgray;
}
.butPage:hover{
background-color: lightgray;
}
.Header {
  display: flex;
  justify-content: center;
  font-family: 'Trebuchet MS', 'Lucida Sans Unicode', 'Lucida Grande', 'Lucida Sans', Arial, sans-serif;
}

</style>