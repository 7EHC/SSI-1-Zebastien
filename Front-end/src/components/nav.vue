<script setup>
import { RouterLink, useRouter, useRoute } from "vue-router";
import {
  UserCircleIcon,
  UserIcon,
  MegaphoneIcon,
  ArrowRightOnRectangleIcon,
  CheckCircleIcon,
  RectangleStackIcon,
} from "@heroicons/vue/24/solid";
import { useTokenStore } from "../stores/tokenStore.js";
import jwt_decode from "jwt-decode";
import { getAnnouncement } from "../composable/fetch";

const route = useRoute();

const isActive = (routeName) => {
  return route.name === routeName;
};

const { accessToken } = useTokenStore();
const decodedUsername = jwt_decode(accessToken);
const { userRole } = useTokenStore();
// console.log(userRole);
// console.log(decodedUsername.sub)
// console.log(accessToken)
const tokenStore = useTokenStore();
const router = useRouter();

const signout = () => {
  tokenStore.setAccessToken("");
  tokenStore.setRefreshToken("");
  tokenStore.setRole("");
  tokenStore.setUserLogin("");
  window.location.reload();
  router.push("/announcement");
};
</script>

<template>
  <div class="all">
    <div class="sidenav">
      <div class="text-nav">
        <h1 class="ann-app-title">SAS</h1>
        <div style="display: flex; align-items: center">
          <UserCircleIcon style="height: 35px; margin: 10px" />
          <h3 style="display: inline-block; margin: 5px; color: white">
            {{ decodedUsername.sub }}
          </h3>
        </div>
        <hr />
        <!-- <RouterLink :to="{ name: 'Announcement' }"
          ><button class="ann-menu">Announcement</button></RouterLink
        > -->
        <div class="menu-parent">
          <RouterLink
            :to="{ name: 'User' }"
            class="ann-menu"
            :class="{ active: isActive('User') }"
            >All Announcements</RouterLink
          ><RectangleStackIcon
            style="height: 25px; margin-right: 10px; float: right"
          />
        </div>
        <hr />
        <!-- <RouterLink :to="{ name: 'Announcement' }"
          ><button class="ann-menu">Announcement</button></RouterLink
        > -->
        <div class="menu-parent">
          <RouterLink
            :to="{ name: 'Announcement' }"
            class="ann-menu"
            :class="{ active: isActive('Announcement') }"
            >Announcement</RouterLink
          ><MegaphoneIcon
            style="height: 25px; margin-right: 10px; float: right"
          />
        </div>
        <hr />
        <!-- <RouterLink :to="{ name: 'SasUser' }"
          ><button class="ann-menu">User</button></RouterLink
        > -->
        <div class="menu-parent" v-if="userRole === 'admin'">
          <RouterLink
            :to="{ name: 'SasUser' }"
            class="ann-menu"
            :class="{ active: isActive('SasUser') }"
            >User</RouterLink
          ><UserIcon style="height: 25px; margin-right: 10px; float: right" />
        </div>
        <hr v-if="userRole === 'admin'" />
        <!-- <RouterLink :to="{ name: 'Match' }"
          ><button class="ann-menu">Match Password</button></RouterLink
        > -->
        <div class="menu-parent" v-if="userRole === 'admin'">
          <RouterLink
            :to="{ name: 'Match' }"
            class="ann-menu"
            :class="{ active: isActive('Match') }"
            >Match Password</RouterLink
          ><CheckCircleIcon
            style="height: 25px; margin-right: 10px; float: right"
          />
        </div>
        <hr v-if="userRole === 'admin'" />
        <!-- <RouterLink :to="{name: 'Login'}" style="position: absolute;bottom: 20px;border-top: 2px solid lightgray;width: 85%;" class="ann-menu">Sign-Out</RouterLink> -->
        <button
          class="signout"
          style="
            position: absolute;
            bottom: 20px;
            /* border-top: 1px solid; */
            width: 85%;
            padding-top: 10px;
            padding-bottom: 5px;
          "
          @click="signout"
        >
          Log Out
          <ArrowRightOnRectangleIcon style="height: 18px; float: right"/>
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
.all {
  font-family: "Trebuchet MS", "Lucida Sans Unicode", "Lucida Grande",
    "Lucida Sans", Arial, sans-serif;
  color: #45a29e;
}

/* MENU BUTTON ---------------------------------------------------------- */
.ann-menu {
  color: aliceblue;
  text-decoration: none;
  background-color: transparent;
  border: 0;
  font-size: 15px;
  margin-top: 5px;
  margin-bottom: 5px;
  padding-left: 0;
  transition: background-color 0.3s;
  padding-left: 10px;
}
.menu-parent {
  width: 100%;
  padding-top: 15px;
  padding-bottom: 15px;
  transition: background-color 0.3s;
}
.menu-parent:hover {
  background-color: #303030;
}
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
  background-color: #1a1a1d;
  box-shadow: 2px 0px 4px lightgray;
  /* overflow-x: hidden; */
  /* padding-top: 10px; */
}
.text-nav {
  padding-left: 15px;
}
/* NAV BAR ---------------------------------------------------------------- */

/* SIGN OUT BUTTON-------------------------------------------------- */
.signout:hover {
  color: black;
  background-color: #ffbaba;
  /* border: 0; */
  border-radius: 10px;
  font-weight: bold;
}
.signout {
  border-radius: 10px;
  /* background-color: #ffbaba; */
  background-color: #45a29e;
  /* color: aliceblue; */
  font-weight: bold;
  color: white;
  text-decoration: none;
  /* background-color: transparent; */
  border: 0;
  font-size: 15px;
  margin-top: 5px;
  margin-bottom: 5px;
  transition-duration: 0.3s;
  padding-left: 10px;
  cursor: pointer;
}
/* SIGN OUT BUTTON-------------------------------------------------- */
.active {
  font-weight: bold;
  color: #45a29e;
}
</style>
