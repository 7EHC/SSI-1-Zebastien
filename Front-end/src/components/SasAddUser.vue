<script setup>
import { ref, onMounted, computed } from "vue";
import { useRoute, useRouter } from "vue-router";
import { getAllUsers } from "../composable/fetch";

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
const existData = ref({});
const allConditionsMet = ref(true);

const hasDataChanged = () => {
  newUserToSend.value = {
    username: newUsername.value,
    name: newName.value,
    email: newEmail.value,
    role: newRole.value,
    createdOn: newCreatedOn.value,
    updatedOn: newUpdateOn.value,
    password: newPassword.value
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
    const res = await fetch(`${API_ROOT}/users`, {
      // const res = await fetch('http://localhost:8080/api/users', {
      method: "POST",
      headers: { "content-type": "application/json" },
      body: JSON.stringify(newUserToSend),
    }); //Add account at backend
    // method post. if it success, it will return status 201 / other methods return status 200
    if (res.status === 200) {
      // console.log('Okay')
      const AddUser = await res.json(); //keep info that added from backend
      router.push("/admin/user");
    } else if (res.status === 400 || res.status === 500) {
      throw new Error("Cannot add");
    }
  } catch (err) {
    console.log(err);
  }
};

const validatePassword = () => {
  validatePwMsg.value = "";
  validateCfPwMsg.value = "";
  const regexPw =
    /^(?!.*\s)(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@+?;:/`#$%^&*_-]).+$/;

    if (
    newPassword.value.length === 0 ||
    newPassword.value === null ||
    newPassword.value === undefined ||
    newPassword.value.includes(' ')
  ) {
    validatePwMsg.value = "Please fill out this field.";
    allConditionsMet.value = false;
  } else if (
    newPassword.value.length > 0 && newPassword.value.length < 8 ||
    newPassword.value.length > 14 || newPassword.value.length > 0 && newPassword.value.includes(' ')
  ) {
    validatePwMsg.value = "Password size must be between 8 and 14";
    allConditionsMet.value = false;
  } else if (!regexPw.test(newPassword.value)) {
    validatePwMsg.value = "must be 8-14 characters long, at least 1 of uppercase, lowercase, number and special characters"
    allConditionsMet.value = false;
  } else if (cfPassword.value !== newPassword.value && cfPassword.value.length > 0) {
    validatePwMsg.value = "The password DOES NOT match";
    allConditionsMet.value = false;
  }

  if (
    cfPassword.value.length === 0 ||
    cfPassword.value === null ||
    cfPassword.value === undefined ||
    cfPassword.value.includes(' ')
  ) {
    validateCfPwMsg.value = "Please fill out this field.";
    allConditionsMet.value = false;
  } else if (
    cfPassword.value.length > 0 && cfPassword.value.length < 8 ||
    cfPassword.value.length > 14
  ) {
    validateCfPwMsg.value = "Confirm password size must be between 8 and 14";
    allConditionsMet.value = false;
  } else if (!regexPw.test(cfPassword.value)) {
    validateCfPwMsg.value = "Confirm password must be 8-14 characters long, at least 1 of uppercase, lowercase, number and special characters"
    allConditionsMet.value = false;
  } 
  else if (cfPassword.value === newPassword.value) {
    allConditionsMet.value = true;
  }
};

function validateEmail(email) {
    if(email.length === 0){
      allConditionsMet.value = false
      return validateEmailMsg.value = 'Please fill out this field.'
    }

    // Additional format checks
    let atIndex = email.indexOf('@');

    if (atIndex === 0) {
        // email starts with '@'
        allConditionsMet.value = false
        return validateEmailMsg.value = "Please enter a part followed by '@'."
    }

    if (atIndex === email.length - 1) {
        // email ends with '@'
        allConditionsMet.value = false
        return validateEmailMsg.value = "Please enter a part following '@'."
    }

    let parts = email.split('@');
    if (parts.length <= 1) {
        // not in format 'xxxx@xxxx'
        allConditionsMet.value = false
        return validateEmailMsg.value = "Please include an '@' in the email address."
    }

    else if (parts.length > 2) {
        // more than one '@' after the first one
        allConditionsMet.value = false
        return validateEmailMsg.value = "A part following '@' should not contain the symbol '@'."
    }
    // allConditionsMet.value = true
    return validateEmailMsg.value = ''
}

const submit = async () => {
  existData.value = await getAllUsers();

  newCreatedOn.value = new Date().toISOString();
  newUpdateOn.value = new Date().toISOString();
  newUserToSend.value = {
    username: newUsername.value,
    name: newName.value,
    email: newEmail.value,
    role: newRole.value,
    createdOn: newCreatedOn.value,
    updatedOn: newUpdateOn.value,
    password: newPassword.value
  };

  validateUsernameMsg.value = "";
  validateNameMsg.value = "";
  // validateEmailMsg.value = "";

  validatePassword();
  validateEmail(newEmail.value)

  for (let i = 0; i < existData.value.length; i++) {
    if (
      newUsername.value.length === 0 ||
      newUsername.value === null ||
      newUsername.value === undefined
    ) {
      validateUsernameMsg.value = "Please fill out this field.";
      allConditionsMet.value = false;
    } else if (newUsername.value.length > 45) {
      validateUsernameMsg.value = "Username size must be between 1 and 45.";
      allConditionsMet.value = false;
    } else if (
      existData.value[i].username.toLowerCase() ===
      newUsername.value.toLowerCase()
    ) {
      validateUsernameMsg.value = "does not unique.";
      allConditionsMet.value = false;
    }

    if (newName.value.length > 100) {
      validateNameMsg.value = "Name size must be between 1 and 100.";
      allConditionsMet.value = false;
    } else if (
      newName.value.length === 0 ||
      newName.value === null ||
      newName.value === undefined
    ) {
      validateNameMsg.value = "Name is required!";
      allConditionsMet.value = false;
    } else if (
      existData.value[i].name.toLowerCase() === newName.value.toLowerCase()
    ) {
      validateNameMsg.value = "does not unique.";
      allConditionsMet.value = false;
    }

    if (
      existData.value[i].email.toLowerCase() === newEmail.value.toLowerCase()
    ) {
      validateEmailMsg.value = "does not unique.";
      allConditionsMet.value = false;
    }
  }
  if (allConditionsMet.value) {
    addNewUser(newUserToSend.value);
  }
};

onMounted(async () => {
  newUserToSend.value = {
    username: newUsername.value,
    name: newName.value,
    email: newEmail.value,
    role: newRole.value,
    createdOn: newCreatedOn.value,
    updatedOn: newUpdateOn.value,
    password: newPassword.value
  };
  // console.log(newUserToSend.value.role)

  cloneNewUser.value = Object.assign({}, newUserToSend.value);
});
</script>

<template>
  <div class="all">
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
          v-on:input="validatePassword(Event), hasDataChanged(Event)"
          maxlength="14"
          required
        />
        <p class="ann-error-password">{{ validatePwMsg }}</p>
      </div>
      <div class="div-form">
        <b>Confirm password<span style="color: red"> *</span></b>
        <input
          class="ann-confirm-password"
          type="password"
          v-model.trim="cfPassword"
          placeholder="Enter match password"
          v-on:input="validatePassword"
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
          v-on:input="validateEmail(newEmail, Event), hasDataChanged(Event)"
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
