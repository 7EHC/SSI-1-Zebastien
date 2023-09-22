<script setup>
import { computed, ref } from "vue";
import navBar from "./components/nav.vue";
import { RouterView } from "vue-router";

const barShow = ref(false)
const checkUrl = computed(() => {
  if(window.location.pathname.includes('/admin')){
    barShow.value = true
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
