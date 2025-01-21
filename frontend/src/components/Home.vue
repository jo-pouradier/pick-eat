<!-- eslint-disable vue/multi-word-component-names -->
<template>
  <div class="home-container">
    <img class="logo-image" src="@/assets/eating.png" alt="Home page background image"/>
    <h1 v-if="userName!='...'" class="title glass-card">Welcome {{ userName }}</h1>
    <button class="new-vote-button" @click="handleNewVote" tabindex="0">
      New Vote
    </button>
    <button class="join-button" @click="handleJoin" tabindex="0">
      Join
    </button>
  </div>
</template>

<script setup lang="ts">
import {useRouter} from 'vue-router';
import {ref} from "vue";

const router = useRouter();
const userName = ref<string>('...');
updateUserName();

function updateUserName() {
  const userCookie = document.cookie.split('; ').find(row => row.startsWith('user='));
  console.debug('User cookie:', userCookie);
  if (userCookie) {
    const user = JSON.parse(decodeURIComponent(userCookie.split('=')[1]));
    console.debug('User:', user);
    userName.value = user.lastName || '...';
  }
}

function handleNewVote() {
  // Handle new vote
  router.push('/new-vote');
};

function handleJoin() {
  // Handle join action
  router.push('/join-event');
}

</script>

<style scoped>
.home-container{
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
}
.title {
  color: var(--white); /* Bright and contrasting color */
  background-color: rgba(0, 0, 0, 0.6); /* Semi-transparent black background */
  padding: 10px 20px; /* Add padding for spacing */
  border-radius: 8px; /* Round the corners */
  text-shadow: 2px 2px 5px rgba(0, 0, 0, 0.5); /* Add shadow for better visibility */
  display: inline-block; /* Make the background wrap the text */
  margin: 0.5em; /* Add spacing from elements above */
}

.logo-image {
  object-fit: contain;
  object-position: center;
  height: 10vh;
  max-width: 100%;
}

.new-vote-button {
  --local-color: var(--accent-orange);
}
.join-button{
  --local-color: var(--light-orange);
}

</style>
