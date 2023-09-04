<script setup>
import { RouterLink } from "vue-router";
</script>

<template>
  <div class="all">
    <div class="sidenav">
      <div class="text-nav">
        <h1 class="ann-app-title">SAS</h1>
        <hr />
        <RouterLink :to="{ name: 'Announcement' }"
          ><button class="ann-menu">Announcement</button></RouterLink
        >
        <hr />
        <RouterLink :to="{ name: 'SasUser' }"
          ><button class="ann-menu">User</button></RouterLink
        >
        <hr />
        <RouterLink :to="{ name: 'Match' }"
          ><button class="ann-menu">Match Password</button></RouterLink
        >
        <hr />
      </div>
    </div>
  </div>
</template>

<style scoped>
.all{
    font-family: 'Trebuchet MS', 'Lucida Sans Unicode', 'Lucida Grande', 'Lucida Sans', Arial, sans-serif;
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
.sidenav {
  height: 100%;
  width: 13%;
  position: fixed;
  z-index: 1;
  top: 0;
  left: 0;
  background-color: #e6e6e6;
  /* overflow-x: hidden; */
  /* padding-top: 10px; */
}
.text-nav {
  padding-left: 15px;
}
</style>
