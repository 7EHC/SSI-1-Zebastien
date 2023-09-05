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
const validateUsernameMsg = ref('')
const validateNameMsg = ref('')
const validateEmailMsg = ref('')
const existData = ref({})
const existUsername = ref([])

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

const submit = async() => {
  existData.value = await getAllUsers()

  existUsername.value = existData.value.map(user => user.username.toLowerCase());
  // console.log(existUsername.value);

  const objToSent = {
    username: ogData.value.username,
    name: ogData.value.name,
    email: ogData.value.email,
    role: ogData.value.role,
  };

  let allConditionsMet = true;

  validateUsernameMsg.value = "";
  if (ogData.value.username.length > 45 ) {
    validateUsernameMsg.value = "Username size must be between 1 and 45.";
    allConditionsMet = false;
  } else if (
    ogData.value.username.length === 0 ||
    ogData.value.username === null ||
    ogData.value.username === undefined ||
    (/^\s*$/.test(ogData.value.username))
  ) {
    validateUsernameMsg.value = "Username is required!";
    allConditionsMet = false;
  } else if (existUsername.value.includes(ogData.value.username)) {
    validateUsernameMsg.value = 'Username is not unique.'
    allConditionsMet = false
  }

  validateNameMsg.value = "";
  if (ogData.value.name.length > 100) {
    validateNameMsg.value = "Name size must be between 1 and 100.";
    allConditionsMet = false;
  } else if (
    ogData.value.name.length === 0 ||
    ogData.value.name === null ||
    ogData.value.name === undefined ||
    (/^\s*$/.test(ogData.value.name))
  ) {
    validateNameMsg.value = "Name is required!";
    allConditionsMet = false;
  }

  validateEmailMsg.value = "";
  const regex = /^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$/i;
  if (ogData.value.email.length > 150) {
    validateEmailMsg.value = "Email size must be between 1 and 150.";
    allConditionsMet = false;
  } else if (
    ogData.value.email.length === 0 ||
    ogData.value.email === null ||
    ogData.value.email === undefined ||
    (/^\s*$/.test(ogData.value.email))
  ) {
    validateEmailMsg.value = "Email is required!";
    allConditionsMet = false;
  } else if (!regex.test(ogData.value.email)) {
    validateEmailMsg.value = "Email is invalid.";
    allConditionsMet = false;
  }

  if (allConditionsMet) {
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
          v-model="ogData.username"
          type="text"
          v-on:input="hasDataChanged"
        />
        <p class="validateMsg" v-if=!ogData.username>{{ validateUsernameMsg }}</p>
        <p class="validateMsg" v-else=!ogData.username>{{ validateUsernameMsg }}</p>
      </div>
      <div class="div-form">
        <b>Name</b>
        <input
          class="ann-name"
          v-model="ogData.name"
          type="text"
          v-on:input="hasDataChanged"
        />
        <p class="validateMsg" v-if=!ogData.name>{{ validateNameMsg }}</p>
        <p class="validateMsg" v-else=!ogData.name>{{ validateNameMsg }}</p>
      </div>
      <div class="div-form">
        <b>Email</b>
        <input
          class="ann-email"
          v-model="ogData.email"
          type="text"
          v-on:input="hasDataChanged"
        />
        <p class="validateMsg" v-if=!ogData.email>{{ validateEmailMsg }}</p>
        <p class="validateMsg" v-else=!ogData.email>{{ validateEmailMsg }}</p>
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
.validateMsg {
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
