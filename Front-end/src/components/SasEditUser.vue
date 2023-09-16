<script setup>
import { ref, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import { getUserById, getAllUsers } from "../composable/fetch";
import { changeTime } from "../composable/changeTime";

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
const allConditionsMet = ref(true);

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
    const res = await fetch(`${API_ROOT}/users/${params.id}`, {
      // const res = await fetch(`http://localhost:8080/api/users/${params.id}`, {
      method: "PUT", // put = replace all record, all field
      // patch = edit some field
      headers: { "content-type": "application/json" },
      body: JSON.stringify(updateUser), //edit backend
    });
    // method put. if it success. it will return status 200
    if (res.status === 200) {
      router.push("/admin/user");
    } else if (res.status === 500) {
      throw new Error("Cannot edit");
    }
  } catch (err) {
    console.log(err);
  }
};

function validateEmail(email) {

  if (email.length === 0) {
    allConditionsMet.value = false;
    return (validateEmailMsg.value = "Please fill out this field.");
  }
  validateEmailMsg.value = ''
  // Additional format checks
  let atIndex = email.indexOf("@");

  if (atIndex === 0) {
    // email starts with '@'
    allConditionsMet.value = false;
    return (validateEmailMsg.value = "Please enter a part followed by '@'.");
  }

  if (atIndex === email.length - 1) {
    // email ends with '@'
    allConditionsMet.value = false;
    return (validateEmailMsg.value = "Please enter a part following '@'.");
  }

  let parts = email.split("@");
  if (parts.length !== 2 || parts[0] === "" || parts[1] === "") {
    // not in format 'xxxx@xxxx'
    allConditionsMet.value = false;
    validateEmailMsg.value =
      "Please include an '@' in the email address."
  } else if (parts[1].includes("@")) {
    // more than one '@' after the first one
    allConditionsMet.value = false;
    return validateEmailMsg.value =
      "A part following '@' should not contain the symbol '@'.";
  } 
  allConditionsMet.value = true
}

const submit = async () => {
  existData.value = await getAllUsers();

  const objToSent = {
    username: ogData.value.username,
    name: ogData.value.name,
    email: ogData.value.email,
    role: ogData.value.role,
  };

  validateUsernameMsg.value = "";
  validateNameMsg.value = "";

  validateEmail(ogData.value.email);

  for (let i = 0; i < existData.value.length; i++) {
    if (
      newData.value.username.toLowerCase() !==
      ogData.value.username.toLowerCase()
    ) {
      if (
        ogData.value.username.length === 0 ||
        ogData.value.username === null ||
        ogData.value.username === undefined
      ) {
        validateUsernameMsg.value = "Please fill out this field.";
        allConditionsMet.value = false;
      } else if (
        existData.value[i].username.toLowerCase() ===
        ogData.value.username.toLowerCase()
      ) {
        allConditionsMet.value = false;
        validateUsernameMsg.value = "does not unique";
      }
    }

    if (newData.value.name.toLowerCase() !== ogData.value.name.toLowerCase()) {
      if (
        ogData.value.name.length === 0 ||
        ogData.value.name === null ||
        ogData.value.name === undefined
      ) {
        validateNameMsg.value = "Please fill out this field.";
        allConditionsMet.value = false;
      } else if (
        existData.value[i].name.toLowerCase() ===
        ogData.value.name.toLowerCase()
      ) {
        allConditionsMet.value = false;
        validateNameMsg.value = "does not unique";
      }
    }

    if (
      newData.value.email.toLowerCase() !== ogData.value.email.toLowerCase()
    ) {
      if (
        existData.value[i].email.toLowerCase() ===
        ogData.value.email.toLowerCase()
      ) {
        allConditionsMet.value = false;
        validateEmailMsg.value = "does not unique";
      }
    }
  }
  if (allConditionsMet.value) {
    allConditionsMet.value = true
    editUser(objToSent);
  }
};

onMounted(async () => {
  // console.log(params.id)
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
});
</script>

<template>
  <div class="all">
    <div class="form">
      <h1>User Detail:</h1>

      <div>
        <b>Username</b>
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
        <b>Name</b>
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
        <b>Email</b>
        <input
          class="ann-email"
          v-model.trim="ogData.email"
          type="email"
          v-on:input="validateEmail(ogData.email), hasDataChanged(Event)"
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
          v-model.trim="ogData.role"
          v-on:change="hasDataChanged"
        >
          <option id="1" value="announcer" selected>announcer</option>
          <option id="2" value="admin">admin</option>
        </select>
      </div>
      <div class="div-form">
        <b
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
