<script setup>
import { ref, onMounted, onBeforeMount } from "vue";
import { useRoute, useRouter } from "vue-router";
import { getUserById, getAllUsers, reqAccessToken } from "../composable/fetch";
import { changeTime } from "../composable/changeTime";
import navBar from "./nav.vue";
import { useTokenStore } from "../stores/tokenStore.js";

// const accessToken = localStorage.getItem("accessToken");
// const allConditionsMet = ref(true);
const API_ROOT = import.meta.env.VITE_ROOT_API;
const ogData = ref({});
const { params } = useRoute();
const disSave = ref(true);
const cloneOgData = ref({});
const router = useRouter();
const newData = ref({});
const validateUsernameMsg = ref("");
const validateNameMsg = ref("");
const validateEmailMsg = ref("");
const existData = ref({});
const errorMsg = ref([]);

const hasDataChanged = () => {
  if (JSON.stringify(cloneOgData.value) === JSON.stringify(ogData.value)) {
    disSave.value = true;
  } else {
    disSave.value = false;
  }
  // console.log(ogData.value.username)
};

const editUser = async (updateUser) => {
  try {
    const tokenStore = useTokenStore();
    const accessToken = ref(tokenStore.accessToken);
    const res = await fetch(`${API_ROOT}/users/${params.id}`, {
      // const res = await fetch(`http://localhost:8080/api/users/${params.id}`, {
      method: "PUT", // put = replace all record, all field
      // patch = edit some field
      headers: {
        "content-type": "application/json",
        Authorization: `Bearer ${accessToken.value}`,
      },
      body: JSON.stringify(updateUser), //edit backend
    });
    // method put. if it success. it will return status 200
    if (res.status === 200) {
      router.push("/admin/user");
    } else if (res.status === 400 || res.status === 500) {
      const error = await res.json();
      errorMsg.value = error.detail;
      console.log(errorMsg.value);
      validateUsernameMsg.value = "";
      validateNameMsg.value = "";
      validateEmailMsg.value = "";
      for (const err of errorMsg.value) {
        if (err.errorMessage === "does not unique") {
          switch (err.field) {
            case "username":
              if (newData.value.username !== ogData.value.username) {
                validateUsernameMsg.value = err.errorMessage;
              }
              break;
            case "name":
              if (newData.value.name !== ogData.value.name) {
                validateNameMsg.value = err.errorMessage;
              }
              break;
            case "email":
              if (newData.value.email !== ogData.value.email) {
                validateEmailMsg.value = err.errorMessage;
              }
              break;
          }
        } else if (err.errorMessage === "must not be blank") {
          switch (err.field) {
            case "username":
              validateUsernameMsg.value = err.errorMessage;
              break;
            case "name":
              validateNameMsg.value = err.errorMessage;
              break;
            case "email":
              validateEmailMsg.value = err.errorMessage;
              break;
          }
        } else if (
          ogData.value.email.length > 0 &&
          err.errorMessage.includes("valid")
        ) {
          validateEmailMsg.value = err.errorMessage;
        }
      }
    } else if (res.status === 401) {
      const chekky = await reqAccessToken();
      // console.log(chekky);
      if (chekky === 'refresh expried'){
      alert("Session has expried, please try again.");
      router.push('/login')
    }
    }
  } catch (err) {
    console.log(err);
  }
};

const submit = async () => {
  existData.value = await getAllUsers();

  const objToSent = {
    username: ogData.value.username,
    name: ogData.value.name,
    email: ogData.value.email,
    role: ogData.value.role,
  };
  editUser(objToSent);
};

// onBeforeMount(async() => {
//   window.location.reload(1)
// })

onMounted(async () => {
  // console.log(params.id)
  const check = await getUserById(params.id);
  if (typeof check === "object" || check === "new token success") {
    ogData.value = await getUserById(params.id);
    // console.log(ogData.value.username)

    newData.value = {
      username: ogData.value.username,
      name: ogData.value.name,
      email: ogData.value.email,
      role: ogData.value.role,
      // createdOn:ogData.value.createdOn,
      // updatedOn:ogData.value.updatedOn
    };

    cloneOgData.value = Object.assign({}, ogData.value);
  } else if (check === "refresh expried") {
    alert("Session has expried, please try again.");
    router.push("/login");
  } else if (check === "Access Denied") {
    alert("Access Denied!");
    router.push("/");
  }
});
</script>

<template>
  <div class="all">
    <navBar />
    <div class="form">
      <h1 style="color: #45a29e;">User Detail:</h1>

      <div>
        <b style="color: #45a29e;">Username</b>
        <input
          class="ann-username"
          v-model.trim="ogData.username"
          type="text"
          v-on:input="hasDataChanged"
          maxlength="45"
          required
        />
        <p class="ann-error-username">{{ validateUsernameMsg }}</p>
      </div>
      <div class="div-form">
        <b style="color: #45a29e;">Name</b>
        <input
          class="ann-name"
          v-model.trim="ogData.name"
          type="text"
          v-on:input="hasDataChanged"
          maxlength="100"
          required
        />
        <p class="ann-error-name">{{ validateNameMsg }}</p>
      </div>
      <div class="div-form">
        <b style="color: #45a29e;">Email</b>
        <input
          class="ann-email"
          v-model.trim="ogData.email"
          type="email"
          v-on:input="hasDataChanged"
          maxlength="150"
          required
        />
        <p class="ann-error-email">{{ validateEmailMsg }}</p>
      </div>
      <div class="div-form">
        <b style="color: #45a29e;">Role</b><br />
        <select
          name="roleName"
          class="ann-role"
          v-model.trim="ogData.role"
          v-on:change="hasDataChanged"
        >
          <option id="1" value="announcer" selected>announcer</option>
          <option id="2" value="admin">admin</option>
        </select>
      </div>
      <div class="div-form">
        <b style="color: #45a29e;"
          >Created On
          <span class="ann-created-on" style="font-weight: normal">{{
            changeTime(ogData.createdOn)
          }}</span>
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Updated On
          <span class="ann-updated-on" style="font-weight: normal">{{
            changeTime(ogData.updatedOn)
          }}</span></b
        >
      </div>
      <div class="ann-div-button">
        <button
          class="ann-button"
          type="submit"
          @click="submit"
          :disabled="disSave"
        >
          Save
        </button>
        <RouterLink :to="{ name: 'SasUser' }">
          <button class="ann-button">Cancel</button>
        </RouterLink>
      </div>
    </div>
  </div>
</template>

<style scoped>
.ann-error-username,
.ann-error-name,
.ann-error-email {
  font-weight: normal;
  font-size: small;
  color: red;
  padding-left: 5px;
}
.all {
  font-family: "Trebuchet MS", "Lucida Sans Unicode", "Lucida Grande",
    "Lucida Sans", Arial, sans-serif;
    padding-left: 2.5%;
}

.form {
  border: lightgray solid 1px;
  border-radius: 8px;
  margin-top: 60px;
  margin-left: 13%;
  margin-right: 30px;
  padding: 15px;
  font-weight: bold;
  background-color: #1a1a1d;
}

.div-form,
select {
  margin-top: 15px;
}
.ann-username,
.ann-name,
.ann-email {
  width: 98%;
  height: 36px;
  padding-left: 10px;
  margin-top: 10px;
  border: lightgray 1px solid;
  border-radius: 6px;
}
select {
  height: 36px;
  width: 25%;
  padding-left: 10px;
  background-color: #e6e6e6;
  border-radius: 7px;
}
.ann-button {
  margin-top: 25px;
  margin-right: 10px;
  width: 100px;
  height: 36px;
  border-radius: 7px;
  border: 0px;
  transition: background-color 0.3s;
}
.ann-button:hover {
  background-color: #787878;
  color: white;
  font-weight: bold;
}
.ann-menu {
  color: black;
  text-decoration: none;
  background-color: transparent;
  border: 0;
  font-size: 15px;
  margin-top: 5px;
  margin-bottom: 5px;
  padding-left: 0;
}
.ann-menu:hover {
  font-weight: bold;
}
</style>
