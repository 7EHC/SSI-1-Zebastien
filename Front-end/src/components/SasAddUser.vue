<script setup>
import { ref, onMounted, computed, onBeforeMount } from "vue";
import { useRoute, useRouter } from "vue-router";
import { getAllUsers, reqAccessToken } from "../composable/fetch";
import { useTokenStore } from "../stores/tokenStore.js";
import navBar from "./nav.vue";

const API_ROOT = import.meta.env.VITE_ROOT_API;
const router = useRouter();
const newUserToSend = ref({});
const newUsername = ref("");
const newPassword = ref("");
const cfPassword = ref("");
const newName = ref("");
const newEmail = ref("");
const newRole = ref("announcer");
const newCreatedOn = ref("");
const newUpdateOn = ref("");
const cloneNewUser = ref({});
const disSave = ref(true);
const validateUsernameMsg = ref("");
const validatePwMsg = ref("");
const validateCfPwMsg = ref("");
const validateNameMsg = ref("");
const validateEmailMsg = ref("");
const allConditionsMet = ref(true);
const notMatch = ref("");
const errorMsg = ref([]);

const hasDataChanged = () => {
  newUserToSend.value = {
    username: newUsername.value,
    name: newName.value,
    email: newEmail.value,
    role: newRole.value,
    createdOn: newCreatedOn.value,
    updatedOn: newUpdateOn.value,
    password: newPassword.value,
  };
  if (
    JSON.stringify(cloneNewUser.value) === JSON.stringify(newUserToSend.value)
  ) {
    disSave.value = true;
  } else {
    disSave.value = false;
  }
};

const addNewUser = async (newUserToSend) => {
  try {
    // checkUpdateAccount(newAccount)
    const tokenStore = useTokenStore();
    const accessToken = ref(tokenStore.accessToken);
    const res = await fetch(`${API_ROOT}/users`, {
      // const res = await fetch('http://localhost:8080/api/users', {
      method: "POST",
      headers: {
        "content-type": "application/json",
        Authorization: `Bearer ${accessToken.value}`,
      },
      body: JSON.stringify(newUserToSend),
    }); //Add account at backend
    // method post. if it success, it will return status 201 / other methods return status 200
    if (res.status === 200) {
      // console.log('Okay')
      const AddUser = await res.json(); //keep info that added from backend
      router.push("/admin/user");
    } else if (res.status === 400 || res.status === 500) {
      const error = await res.json();
      errorMsg.value = error.detail;
      console.log(errorMsg.value);
      validateUsernameMsg.value = "";
      validateNameMsg.value = "";
      validateEmailMsg.value = "";
      validatePwMsg.value = "";
      for (const err of errorMsg.value) {
        if (err.errorMessage === "does not unique") {
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
        } else if (err.errorMessage === "must not be blank") {
          switch (err.field) {
            case "username":
              validateUsernameMsg.value = err.errorMessage;
              break;
            case "name":
              validateNameMsg.value = err.errorMessage;
              break;
            case "password":
              validatePwMsg.value = err.errorMessage;
              break;
            case "email":
              validateEmailMsg.value = err.errorMessage;
              break;
          }
        } else if (
          newEmail.value.length > 0 &&
          err.errorMessage.includes("valid")
        ) {
          validateEmailMsg.value = err.errorMessage;
        } else if (err.errorMessage.includes("size must be between 8 and 14")) {
          if (newPassword.value.length < 8 && newPassword.value.length !== 0) {
            validatePwMsg.value = `Password ${err.errorMessage}`;
          }
          if (cfPassword.value.length < 8 && cfPassword.value.length !== 0)
            validateCfPwMsg.value = `Confirm password ${err.errorMessage}`;
        } else if (
          err.errorMessage.includes(
            "must be 8-14 characters long, at least 1 of uppercase, lowercase, number and special characters"
          )
        ) {
          if (newPassword.value.length >= 8) {
            validatePwMsg.value = `${err.errorMessage}`;
          }
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
    // console.log(err);
  }
};

const checkPw = computed(() => {
  if (cfPassword.value !== newPassword.value) {
    notMatch.value = "The password DOES NOT match";
    allConditionsMet.value = false;
  } else {
    notMatch.value = "";
    allConditionsMet.value = true;
  }
});

const submit = async () => {
  newCreatedOn.value = new Date().toISOString();
  newUpdateOn.value = new Date().toISOString();
  newUserToSend.value = {
    username: newUsername.value,
    name: newName.value,
    email: newEmail.value,
    role: newRole.value,
    createdOn: newCreatedOn.value,
    updatedOn: newUpdateOn.value,
    password: newPassword.value,
  };
  if (allConditionsMet.value) {
    addNewUser(newUserToSend.value);
  }
};

onMounted(async () => {
  const check = await getAllUsers()
  if (typeof check === "object" || check === "new token success") {
    newUserToSend.value = {
      username: newUsername.value,
      name: newName.value,
      email: newEmail.value,
      role: newRole.value,
      createdOn: newCreatedOn.value,
      updatedOn: newUpdateOn.value,
      password: newPassword.value,
    };
    // console.log(newUserToSend.value.role)
  
    cloneNewUser.value = Object.assign({}, newUserToSend.value);
  } else if (check === 'refresh expried'){
    alert("Session has expried, please try again.");
    router.push('/login')
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
      <h1>User Detail:</h1>
      <div>
        <b>Username<span style="color: red"> *</span></b>
        <input
          class="ann-username"
          v-model.trim="newUsername"
          type="text"
          placeholder="Enter less than 45 characters"
          v-on:input="hasDataChanged"
          maxlength="45"
          required
        />
        <p class="ann-error-username">{{ validateUsernameMsg }}</p>
      </div>
      <!-- Change -------------------------------------- -->
      <div class="div-form">
        <b>Password<span style="color: red"> *</span></b>
        <input
          class="ann-password"
          type="password"
          v-model.trim="newPassword"
          placeholder="Enter 8-14 characters"
          v-on:input="hasDataChanged(Event)"
          maxlength="14"
          required
        />
        <p class="ann-error-password">{{ validatePwMsg }}</p>
      </div>
      <div class="div-form">
        <b
          >Confirm password<span style="color: red"> *</span
          ><span class="ann-error-password">{{ notMatch }}</span></b
        >
        <input
          class="ann-confirm-password"
          type="password"
          v-model.trim="cfPassword"
          placeholder="Enter match password"
          v-on:input="hasDataChanged(Event), checkPw"
          maxlength="14"
          required
        />
        <p class="ann-error-confirm-password">{{ validateCfPwMsg }}</p>
      </div>
      <!-- --------------------------------------------- -->
      <div class="div-form">
        <b>Name<span style="color: red"> *</span></b>
        <input
          class="ann-name"
          v-model.trim="newName"
          type="text"
          placeholder="Enter less than 100 characters"
          v-on:input="hasDataChanged"
          maxlength="100"
          required
        />
        <p class="ann-error-name">{{ validateNameMsg }}</p>
      </div>
      <div class="div-form">
        <b>Email<span style="color: red"> *</span></b>
        <input
          class="ann-email"
          v-model.trim="newEmail"
          type="email"
          placeholder="Enter less than 150 characters"
          v-on:input="hasDataChanged"
          maxlength="150"
          required
        />
        <p class="ann-error-email">{{ validateEmailMsg }}</p>
      </div>
      <div class="div-form">
        <b>Role</b><br />
        <select
          name="roleName"
          class="ann-role"
          v-model.trim="newRole"
          v-on:change="hasDataChanged"
        >
          <option id="1" value="announcer" selected>announcer</option>
          <option id="2" value="admin">admin</option>
        </select>
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
.ann-error-email,
.ann-error-password,
.ann-error-confirm-password {
  font-weight: normal;
  font-size: small;
  color: red;
  padding-left: 5px;
}
.all {
  font-family: "Trebuchet MS", "Lucida Sans Unicode", "Lucida Grande",
    "Lucida Sans", Arial, sans-serif;
}

.form {
  border: lightgray solid 1px;
  border-radius: 8px;
  margin-top: 60px;
  margin-left: 13%;
  margin-right: 30px;
  padding: 15px;
  font-weight: bold;
}

.div-form,
select {
  margin-top: 15px;
}
.ann-username,
.ann-name,
.ann-email,
.ann-password,
.ann-confirm-password {
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
}
.ann-button:hover {
  background-color: lightgrey;
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
