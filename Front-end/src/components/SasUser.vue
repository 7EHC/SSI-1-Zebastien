<script setup>
import { useRoute, useRouter } from "vue-router";
import { ref, onMounted } from "vue";
import { getAllUsers, reqAccessToken } from "../composable/fetch";
import { changeTime } from "../composable/changeTime";
import navBar from "./nav.vue";
import { useTokenStore } from "../stores/tokenStore.js";
import {
  UserPlusIcon,
  PencilSquareIcon,
  TrashIcon,
  UserIcon,
} from "@heroicons/vue/24/solid";

// const accessToken = localStorage.getItem("accessToken");
// const refreshToken = localStorage.getItem("refreshToken");
const router = useRouter();
const timeZone = Intl.DateTimeFormat().resolvedOptions().timeZone;
const user = ref([]);
const confirmDelete = ref(false);
const idToDelete = ref();
const API_ROOT = import.meta.env.VITE_ROOT_API;
const { userLogin } = useTokenStore();
const { userRole } = useTokenStore();
const usertoDelete = ref([]);

const deleteUser = async (id) => {
  try {
    const tokenStore = useTokenStore();
    const accessToken = ref(tokenStore.accessToken);
    const res = await fetch(
      `${API_ROOT}/users/${id}`, // Include adminUsername as a query parameter
      {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${accessToken.value}`,
        },
      }
    );
    if (res.ok) {
      changeConfirm();
      user.value = user.value.filter((usr) => usr.id !== id);
      router.push("/admin/user");
    } else if (res.status === 401) {
      const chekky = await reqAccessToken();
      if (chekky === "refresh expired") {
        alert("Session has expired, please try again.");
        router.push("/login");
      }
    } else if (res.status === 403) {
      alert(`[ERROR] Cannot delete your own account.`);
      window.location.reload();
    } else {
      throw new Error(`Cannot delete`);
    }
  } catch (err) {
    alert(`${err}`);
  }
};

const changeConfirm = (id) => {
  confirmDelete.value = !confirmDelete.value;
  idToDelete.value = id;
};
const deleteWarning = (id) => {
  let warning;
  usertoDelete.value = user.value.filter((usr) => usr.id == id);
  usertoDelete.value.forEach((usr) => {
    if (usr.username !== userLogin) {
      warning = window.confirm("The announcements owned by this user will be transfered to you. Do you still want to delete this user?");
      if (warning) {
        deleteUser(id)
      } else {
        window.location.reload() 
      }
    } else {
      deleteUser(id);
    }
  });
};

const goToEdit = (id) => {
  router.push({ name: "SasEditUser", params: { id: id } });
};

onMounted(async () => {
  const check = await getAllUsers();
  if (typeof check === "object") {
    user.value = check;
    user.value.sort(
      (a, b) =>
        a.role.localeCompare(b.role) || a.username.localeCompare(b.username)
    );
    if (!user.value) {
      user.value = [];
    }
  } else if (check === "new token success") {
    user.value = await getAllUsers();
    // console.log(user.value);
    user.value.sort(
      (a, b) =>
        a.role.localeCompare(b.role) || a.username.localeCompare(b.username)
    );

    if (!user.value) {
      user.value = [];
    }
  } else if (check === "refresh expried") {
    alert("Session has expried, please try again.");
    router.push("/login");
  } else if (check === "Access Denied") {
    alert("Access Denied!");
    router.push("/");
  }
});

const checkRole = (role) => {
  if (role === "Admin") {
    return true;
  } else {
    router.push("/announcement");
    return false;
  }
};
</script>

<template>
  <div class="all">
    <navBar />
    <div class="popup">
      <Teleport to="body">
        <div
          class="modal"
          style="background-color: rgba(0, 0, 0, 0.3); width: 100%"
          v-if="confirmDelete === true"
        >
          <!-- <div class="modal" style="background-color: rgba(0, 0, 0, 0.3); width: 100%;"> -->
          <div
            class="window"
            style="height: 40%; width: 50%; background-color: white"
          >
            <p style="font-size: 48px">Delete</p>
            <hr />
            <p style="font-size: 24px; margin-top: 30px">
              Do you want to delete these records ?
            </p>
            <button class="viewBut" @click="changeConfirm">Cancel</button
            ><RouterLink :to="{ name: 'SasUser' }">
              <button @click="deleteWarning(idToDelete)" class="deleteButPopup">
                Delete
              </button></RouterLink
            >
          </div>
        </div>
      </Teleport>
    </div>
    <!-- <div class="profile" style="float: right;border-radius: 10px;background-color: #1a1a1d;">
          <p style="padding: 10px;padding-top: 0;padding-bottom: 0;color: #45a29e;"><UserIcon style="height: 15px;margin-right: 10px;" />User: Sanit</p>
        </div> -->

    <div class="parent-container">
      <h1
        class="user-man"
        style="
          border-radius: 10px;
          height: 10px;
          padding-bottom: 25px;
          margin-top: 15px;
        "
      >
        User Management
      </h1>
    </div>
    <div class="table-parent" style="margin-right: 15px">
      <h4 class="ann-timezone">Date/Time shown in Timezone: {{ timeZone }}</h4>

      <div class="ann-button" id="addDiv">
        <RouterLink :to="{ name: 'SasAddUser' }"
          ><button id="addBut" style="float: right">
            <UserPlusIcon style="height: 20px" /></button
        ></RouterLink>
      </div>
      <div class="bigTable">
        <table>
          <tr class="tableHead">
            <th class="trHead">No.</th>
            <th class="trHead">Username</th>
            <th class="trHead">Name</th>
            <th class="trHead">Email</th>
            <th class="trHead">Role</th>
            <th class="trHead">Created On</th>
            <th class="trHead">Updated On</th>
            <th class="trHead">Action</th>
          </tr>

          <tr v-for="(usr, index) in user" :key="index" class="ann-item">
            <td>{{ index + 1 }}</td>
            <td class="ann-username">{{ usr.username }}</td>
            <td class="ann-name">{{ usr.name }}</td>
            <td class="ann-email">{{ usr.email }}</td>
            <td class="ann-role">{{ usr.role }}</td>
            <td class="ann-created-on">{{ changeTime(usr.createdOn) }}</td>
            <td class="ann-updated-on">{{ changeTime(usr.updatedOn) }}</td>
            <td class="ann-button-td">
              <button
                class="ann-button"
                id="edit-button"
                @click="goToEdit(usr.id)"
              >
                <PencilSquareIcon style="height: 15px" />
              </button>
              <button
                class="ann-button"
                id="delete-button"
                @click="changeConfirm(usr.id)"
              >
                <TrashIcon style="height: 15px" />
              </button>
            </td>
          </tr>
        </table>
      </div>
    </div>
    <h4 class="NoAlert" v-if="user.length === 0">No user</h4>
  </div>
</template>

<style scoped>
button {
  transition-duration: 0.3s;
  cursor: pointer;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
button:hover {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}
body {
  background-color: #1a1a1d;
}
.ann-timezone {
  justify-content: start;
  margin-bottom: -4%;
  font-weight: lighter;
  margin-top: 10px;
}
.NoAlert {
  display: flex;
  justify-content: center;
  margin-top: 10%;
  margin-left: 13.5%;
}
.ann-timezone,
.bigTable {
  padding-left: 14.5%;
}
.parent-container {
  margin-left: -10px;
  margin-right: -8px;
  margin-bottom: 10px;
  display: flex;
  padding-left: 50%;
  box-shadow: 0 3px 4px rgba(0, 0, 0, 0.1);
  /* align-items: center; */
}
.all {
  font-family: "Trebuchet MS", "Lucida Sans Unicode", "Lucida Grande",
    "Lucida Sans", Arial, sans-serif;
}

.trHead {
  background-color: #1a1a1d;
  color: #45a29e;
}
table {
  /* margin-top: 30px; */
  border-collapse: collapse;
  width: 100%;
  border: 1px solid lightgray;
}

th {
  background-color: lightgray;
  border: 1px solid lightgray;
  height: 35px;
  font-size: large;
}

td {
  padding: 15px;
  /* border: 1px solid lightgray; */
  border: transparent;
  border-bottom: 1px solid lightgray;
}
.ann-button-td {
  width: 220px;
}
#delete-button:hover {
  background-color: #cd7474;
  font-weight: bold;
}
#delete-button {
  margin-left: 15px;
}
#delete-button::after {
  content: "delete";
  display: none;
  color: black;
  transform: translateX(-30%);
}
#delete-button:hover::after {
  display: block;
  float: right;
  padding-top: 3px;
}
#edit-button:hover {
  background-color: khaki;
  font-weight: bold;
}
#edit-button::after {
  content: "edit";
  display: none;
  color: black;
  transform: translateX(-70%);
}
#edit-button:hover::after {
  display: block;
  float: right;
  padding-top: 3px;
}
#addBut,
#edit-button,
#delete-button {
  width: 100px;
  height: 30px;
  border: 0px;
  border-radius: 10px;
  background-color: #e6e6e6;
  font-weight: normal;
  transition: background-color 0.3s;
  /* border: 1px solid black; */
}
#addBut:hover {
  background-color: #45a29e;
  font-weight: bold;
  /* border: 1px solid black; */
  /* opacity: 0.5; */
}
#addBut::after {
  content: "add";
  display: none;
  color: black;
  transform: translateX(-70%);
  /* transition: opacity 0.3s; */
  /* opacity: 0; */
}
#addBut:hover::after {
  display: block;
  float: right;
  padding-top: 4px;
  /* opacity: 1; */
}
.ann-button {
  text-align: center;
}
#addDiv {
  float: right;
  margin-bottom: 10px;
}

.deleteButPopup {
  /* background-color: coral; */
  background-color: salmon;
  width: 100px;
  height: 30px;
  border: 0px;
  border-radius: 6px;
  padding-top: 15px;
  padding-bottom: 27px;
  margin-left: 10px;
}
.deleteButPopup:hover {
  background-color: indianred;
}

.modal {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  /* background-color: #0000; */
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}
.modal > div {
  /* background-color: rgb(250 204 21); */
  padding: 10px;
  border-radius: 10px;
  /* width: 50%;
    height: 30%; */
  border: 0px;
}
.window {
  text-align: center;
  font-family: "Trebuchet MS", "Lucida Sans Unicode", "Lucida Grande",
    "Lucida Sans", Arial, sans-serif;
}
.viewBut {
  width: 100px;
  height: 30px;
  border: 0px;
  border-radius: 6px;
  background-color: #e6e6e6;
  padding-top: 15px;
  padding-bottom: 27px;
}
.viewBut:hover {
  background-color: lightgray;
}

.ann-timezone,
#addDiv {
  display: inline-block;
  padding-bottom: 0%;
}
#addDiv {
  padding-top: 18px;
}
</style>
