<script setup>
import { computed, ref } from "vue";
import navBar from "./components/nav.vue";
import { RouterView } from "vue-router";

const barShow = ref(true)
const checkUrl = computed(() => {
  if(window.location.pathname === '/announcement'){
    barShow.value = false
  }
  return barShow.value
})

</script>

<template>
  <div>
    <div v-show=checkUrl>
      <navBar />
    </div>
    <div>
      <RouterView />
    </div>
  </div>
</template>

<style scoped>
</style>
