<template>
  <div class="glass-card glass-container">
      <h1>Register new account</h1>
      <p>Email :</p>
      <input tabindex="0" role="textbox" type="email" @focus="handleEmailSelection"
             placeholder="Votre Email" v-model="email"/>
        <p>Prefered name :</p>
      <input tabindex="0" role="textbox" @focus="handleEmailSelection"
             placeholder="Votre nom" v-model="name"/>
      <p>Mot de passe :</p>
      <input tabindex="0" role="textbox" type="password"
             @focus="handleEmailSelection" v-model="password"/>
      <p>Confirm password :</p>
      <input tabindex="0" role="textbox" type="password"
             @focus="handleEmailSelection" v-model="confirmPassword"/>
      <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
      <button class="validate-button" @click="handleValidation">
        Valider
      </button>
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
.validate-button{
  --local-color: var(--valid-green);
}

button{
  padding: 0.5em 1em;
  margin-bottom: 1em;
}
.error-message {
  color: red;
  margin-top: 10px;
}
</style>
