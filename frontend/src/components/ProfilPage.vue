<template>
  <div class="profile-container">
    <h1 class="title">Profile Page</h1>
    <p class="user-info">User: {{ userName }}</p>
    <button class="logout-button" @click="handleLogout" tabindex="0">
      Logout
    </button>
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
.profile-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 50px;
}

.title {
  font-size: 2em;
  margin-bottom: 20px;
}

.user-info {
  font-size: 1.2em;
  margin-bottom: 30px;
}

.logout-button {
  border-radius: 100px;
  min-height: 66px;
  width: 169px;
  max-width: 100%;
  text-align: center;
  letter-spacing: -0.14px;
  padding: 23px 12px;
  font: 700 27px/1 "League Spartan", sans-serif;
  border: none;
  cursor: pointer;
  background-color: var(--Orange-2, #ffdecf);
  color: var(--Orange-Base, #e95322);
}
</style>
