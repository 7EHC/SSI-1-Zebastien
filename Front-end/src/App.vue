<script setup>
// import Announcement from "./components/Announcement.vue";
import { RouterView } from "vue-router";
</script>

<template>
  <!-- <Announcement /> -->
  <!-- <div class="Header">
    <h1>SIT Announcement System (SAS)</h1>
  </div> -->
  <RouterView />
</template>

<style scoped>
.Header {
  display: flex;
  justify-content: center;
  font-family: 'Trebuchet MS', 'Lucida Sans Unicode', 'Lucida Grande', 'Lucida Sans', Arial, sans-serif;
}
</style>

