<script setup>
import { RouterLink,useRouter } from "vue-router";
import { UserIcon, MegaphoneIcon,ArrowRightOnRectangleIcon } from '@heroicons/vue/24/solid'
import { useTokenStore } from "../stores/tokenStore.js";

const tokenStore = useTokenStore();
const router = useRouter()
const signout = () => {
  tokenStore.setAccessToken('')
  tokenStore.setRefreshToken('')
  router.push("/login");
}
</script>

<template>
  <div class="all">
    <div class="sidenav">
      <div class="text-nav">
        <h1 class="ann-app-title">SAS</h1>
        <hr />
        <!-- <RouterLink :to="{ name: 'Announcement' }"
          ><button class="ann-menu">Announcement</button></RouterLink
        > --><div class="menu-parent">
        <RouterLink :to="{name: 'Announcement'}" class="ann-menu">Announcement</RouterLink><MegaphoneIcon style="
        height: 25px;margin-right: 10px;float: right;"/>
        </div>
        <hr />
        <!-- <RouterLink :to="{ name: 'SasUser' }"
          ><button class="ann-menu">User</button></RouterLink
        > --><div class="menu-parent">
        <RouterLink :to="{name: 'SasUser'}" class="ann-menu">User</RouterLink><UserIcon style="
        height: 25px;margin-right: 10px;float: right;"/>
        </div>
        <hr />
        <!-- <RouterLink :to="{ name: 'Match' }"
          ><button class="ann-menu">Match Password</button></RouterLink
        > --><div class="menu-parent">
        <RouterLink :to="{name: 'Match'}" class="ann-menu">Match Password</RouterLink>
        </div>
        <hr />
        <!-- <RouterLink :to="{name: 'Login'}" style="position: absolute;bottom: 20px;border-top: 2px solid lightgray;width: 85%;" class="ann-menu">Sign-Out</RouterLink> -->
        <button class="signout" style="position: absolute;bottom: 20px;border-top: 1px solid;width: 85%; padding-top: 10px;"
          @click="signout">
        SIGN OUT <ArrowRightOnRectangleIcon style="height: 18px; float: right;"/>
      </button>
      </div>
    </div>
  </div>
</template>

<style scoped>
hr {
  opacity: 35%;
  margin-top: 0%;
  margin-bottom: 0%;
}
.all{
    font-family: 'Trebuchet MS', 'Lucida Sans Unicode', 'Lucida Grande', 'Lucida Sans', Arial, sans-serif;
    color: #45a29e;
}

/* MENU BUTTON ---------------------------------------------------------- */
.ann-menu{
    color: aliceblue;
    text-decoration: none;
    background-color: transparent;
    border: 0;
    font-size: 15px;
    margin-top: 5px;
    margin-bottom: 5px;
    padding-left:0;
    transition: background-color 0.3s;
    padding-left: 10px;
}
.menu-parent{
  width: 100%;
  padding-top: 15px;
  padding-bottom: 15px;
  transition: background-color 0.3s;
}
.menu-parent:hover{background-color: #303030;}
/* MENU BUTTON ---------------------------------------------------------- */

/* NAV BAR ---------------------------------------------------------------- */
.sidenav {
  height: 100%;
  width: 13%;
  position: fixed;
  z-index: 1;
  top: 0;
  left: 0;
  /* background-color: #e6e6e6; */
  background-color: #1A1A1D;
  box-shadow: 2px 0px 4px lightgray ;
  /* overflow-x: hidden; */
  /* padding-top: 10px; */
}
.text-nav {
  padding-left: 15px;
}
/* NAV BAR ---------------------------------------------------------------- */

/* SIGN OUT BUTTON-------------------------------------------------- */
.signout:hover{font-weight: bold;}
.signout{color: aliceblue;
    text-decoration: none;
    background-color: transparent;
    border: 0;
    font-size: 15px;
    margin-top: 5px;
    margin-bottom: 5px;
    padding-left:0;
    transition: background-color 0.3s;
    padding-left: 10px;}
/* SIGN OUT BUTTON-------------------------------------------------- */
</style>
