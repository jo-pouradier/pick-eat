<template>
  <div class="event-creation-container">
    <div class="event-creation-wrapper">
      <h1 class="main-title">Register new account</h1>
      <p class="second-title">Email :</p>
      <input class="name-selector" tabindex="0" role="textbox" type="email" @focus="handleEmailSelection"
             placeholder="Votre Email" v-model="email"/>
        <p>Prefered name :</p>
      <input class="name-selector" tabindex="0" role="textbox" @focus="handleEmailSelection"
             placeholder="Votre nom" v-model="name"/>
      <p>Mot de passe :</p>
      <input class="name-selector" tabindex="0" role="textbox" type="password"
             @focus="handleEmailSelection" v-model="password"/>
      <p>Confirm password :</p>
      <input class="name-selector" tabindex="0" role="textbox" type="password"
             @focus="handleEmailSelection" v-model="confirmPassword"/>
      <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
      <button class="validate-button" @click="handleValidation">
        Valider
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
// import { useRouter } from 'vue-router';
import {ref} from 'vue';
import axios from "axios";
import {useRouter} from "vue-router";

const email = ref<string>('');
const name = ref<string>('');
const password = ref<string>('');
const confirmPassword = ref<string>('');
const errorMessage = ref<string>('');
const router = useRouter();

function handleEmailSelection(): void {
  // Implementation for name selection
};

function handleValidation(): void {
  if (password.value !== confirmPassword.value) {
    errorMessage.value = 'Les mots de passe ne correspondent pas !';
  } else {
    axios.post('/auth/register',
      {
        email: email.value,
        password: password.value,
        lastName: name.value,
        firstName: name.value,
      })
      .then(response => {
        console.log("il est bien connecté", response.data);
        router.push('/');
        // Handle successful response
      })
      .catch(error => {
        alert(`Invalid email or password : ${error}`);
      });
  }
};
// function handleRegister() {
//     // Implementation for validation
//     router.push('/register');
// };


</script>

<style scoped>
.event-creation-container {
  display: flex;
  border-radius: 35px;
  padding: 21px 0 46px;
  background: var(--Yellow-2, #f3e9b5);
  max-width: 480px;
  width: 100%;
  flex-direction: column;
  margin-top: 15vh;
}


.event-creation-wrapper {
  display: flex;
  flex-direction: column;
  position: relative;
  width: 100%;
  align-items: center;
}


.main-title {
  position: relative;
  color: rgba(0, 0, 0, 1);
  letter-spacing: -0.28px;
  text-align: center;
  font: 400 40px/1 Lobster, sans-serif; /* Reduced font size */
  border-color: black;
  background: var(--Yellow-2, #f3e9b5);
  width: fit-content;
}


.name-selector {
  position: relative;
  background: var(--Yellow-2, #f3e9b5);
  margin: 10px 0 0; /* Reduced margin */
  width: 100%;
  max-width: 300px; /* Reduced max-width */
  color: rgba(0, 0, 0, 1);
  text-align: center;
  letter-spacing: -0.18px;
  padding: 10px  15px 0; /* Reduced padding */
  font: 700 20px/1 League Spartan, sans-serif; /* Reduced font size */
  border: none;
  cursor: pointer;
  border-bottom:black solid;

}
.second-title{
   font: 200 20px/1 Lobster, sans-serif; /* Reduced font size */

}
.validate-button {
  position: relative;
  border-radius: 50px; /* Reduced border-radius */
  background-color: rgba(179, 38, 30, 1);
  margin: 20px 0 0; /* Reduced margin */
  min-height: 40px; /* Reduced min-height */
  width: 120px; /* Reduced width */
  color: var(--Yellow-2, #f3e9b5);
  text-align: center;
  letter-spacing: -0.14px;
  padding: 10px 8px; /* Reduced padding */
  font: 700 16px/1 League Spartan, sans-serif; /* Reduced font size */
  border: none;
  cursor: pointer;
}

.error-message {
  color: red;
  margin-top: 10px;
}
</style>
