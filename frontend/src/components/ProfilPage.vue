<template>
  <div class="glass-card glass-container">
    <h1>Profile Page</h1>
    <p>User: {{ userName }}</p>
    <button class="logout-button" @click="handleLogout" tabindex="0">Logout</button>
  </div>
</template>

<script setup lang="ts">
import {useRouter} from 'vue-router';
import {onMounted, ref} from "vue";
import axios from 'axios';

const router = useRouter();
const userName = ref<string>('...');

onMounted(() => {
  const userCookie = document.cookie.split('; ').find(row => row.startsWith('user='));
  if (!userCookie) {
    router.push('/login');
  }
});

updateUserName();

async function updateUserName() {
  const userCookie = document.cookie.split('; ').find(row => row.startsWith('user='));
  console.debug('User cookie:', userCookie);
  if (userCookie) {
    const user = JSON.parse(decodeURIComponent(userCookie.split('=')[1]));
    userName.value = user.lastName || '...';
  }
}

function handleLogout() {
  axios.get('/auth/logout').then(() => {
    console.log('Logged out');
    router.push('/login');
  });
}
</script>

<style scoped>
button {
  padding: 0.3em;
  font-size: 0.8em;
}
.logout-button{
  --local-color: var(--red-wrong);
}
</style>
