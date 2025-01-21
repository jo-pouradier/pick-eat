<template>
  <div class="glass-card glass-container">
    <h1>Log in</h1>
    <p>Email :</p>
    <input tabindex="0" role="textbox" type="email"
           @focus="handleEmailSelection" placeholder="Email" v-model="email"/>
    <p>Mot de passe :</p>
    <input tabindex="0" role="textbox" type="password"
           @focus="handleEmailSelection" placeholder="Mot de passe" v-model="password"/>
    <button class="login-button" @click="handleValidation">
      Login
    </button>
    <p class="input-label">Vous n'avez pas de compte ?</p>
    <button class="register-button" @click="handleRegister">
      Créer un compte
    </button>
    <p>Se connecter avec <a href="auth/oauth2/authorization/github"><b>Github</b></a> ?</p>
  </div>
</template>

<script setup lang="ts">
import {ref} from 'vue';
import {useRouter} from 'vue-router';
import axios from 'axios';

const router = useRouter();
const email = ref('');
const password = ref('');

function handleEmailSelection(): void {
  // Implementation for name selection
};


function handleValidation(): void {
  console.log('Validation');
  // Perform validation
  if (!email.value || !password.value) {
    alert('Please fill in both fields.');
    return;
  }
  axios.post('/auth/login', {email: email.value, password: password.value})
    .then(response => {
      console.log("il est bien connecté", response.data);
      router.push('/');
      // Handle successful response
    })
    .catch(error => {
      // Handle error response
      console.log("il est pas connecté", error);
      alert('Invalid email or password.');
    });
}

function handleRegister(): void {
  // Implementation for validation
  router.push('/register');
}

</script>

<style scoped>
.login-button {
  --local-color: var(--valid-green);
}

.register-button {
  --local-color: var(--accent-orange);
}

button{
  padding: 0.5em 1em;
  margin-bottom: 1em;
}
</style>
