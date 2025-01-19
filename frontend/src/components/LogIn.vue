<template>
    <div class="event-creation-container">
        <div class="event-creation-wrapper">
            <h1 class="main-title">Log in</h1>
            <p class="input-label">Email :</p>
            <input class="name-selector" tabindex="0" role="textbox" type="email"
                @focus="handleEmailSelection" placeholder="Email" v-model="email" />
            <p class="input-label">Mot de passe :</p>
            <input class="name-selector" tabindex="0" role="textbox" type="password"
                @focus="handleEmailSelection" placeholder="Mot de passe" v-model="password" />
            <button class="validate-button" @click="handleValidation">
                Valider
            </button>
            <p class="input-label">Vous n'avez pas de compte ?</p>
            <button class="validate-button" @click="handleRegister">
                Créer un compte
            </button>
      <p class="input-label">Essayez un compte <a href="auth/oauth2/authorization/github"><b>Github</b></a> ?</p>

        </div>
    </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
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
    axios.post('/auth/login', { email: email.value, password: password.value })
    .then(response => {
        console.log("il est bien connecté",response.data);
        router.push('/');
        // Handle successful response
    })
    .catch(error => {
        // Handle error response
        console.log("il est pas connecté",error);
        alert('Invalid email or password.');
    });
};
function handleRegister(): void {
    // Implementation for validation
    router.push('/register');
};
</script>

<style scoped>
.event-creation-container {
    display: flex;
    flex-direction: column;
    margin: 0 auto;
    padding-top: 40px;
}

.event-creation-wrapper {
    display: flex;
    flex-direction: column;
    width: 100%;
    align-items: center;
}

.event-creation-form {
    display: flex;
    flex-direction: column;
    max-width: 300px;
    align-items: center;
}

.main-title {
    position: relative;
    color: rgba(255, 255, 255, 1);
    letter-spacing: -0.28px;
    text-align: center;
    margin: 32px 0 0;
    font: 400 55px/1 Lobster, sans-serif;
}

.input-label {
    color: rgba(255, 255, 255, 1);
    font: 400 18px/1 League Spartan, sans-serif;
    margin: 5px 0;
}

.name-selector {
    position: relative;
    border-radius: 44px;
    background: var(--Yellow-2, #f3e9b5);
    margin: 10px 0;
    width: 100%;
    max-width: 300px; /* Reduce max-width */
    color: rgba(0, 0, 0, 1);
    text-align: center;
    letter-spacing: -0.18px;
    padding: 10px 15px; /* Reduce padding */
    font: 700 30px/1 League Spartan, sans-serif; /* Reduce font size */
    border: none;
    cursor: pointer;
}

.validate-button {
    position: relative;
    border-radius: 100px;
    background-color: rgba(179, 38, 30, 1);
    margin: 10px 0;
    min-height: 45px; /* Reduce min-height */
    width: fit-content; /* Reduce width */
    color: var(--Yellow-2, #f3e9b5);
    text-align: center;
    letter-spacing: -0.14px;
    padding: 15px 20px; /* Reduce padding */
    font: 700 24px/1 League Spartan, sans-serif; /* Reduce font size */
    border: none;
    cursor: pointer;
}
</style>
