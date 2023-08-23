<script setup>
import navBar from "./components/nav.vue";
import { RouterView } from "vue-router";
</script>

<template>
  <div>
    <div>
      <navBar />
    </div>
    <div>
      <RouterView />
    </div>
  </div>
</template>

<style scoped>
</style>
