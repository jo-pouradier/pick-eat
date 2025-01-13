<!-- eslint-disable vue/multi-word-component-names -->
<template>
  <img class="logo-image" src="@/assets/eating.png" alt="Home page background image" />
  <button class="action-button action-button-primary" @click="handleNewVote" tabindex="0">
    New Vote
  </button>
  <button class="action-button action-button-secondary" @click="handleJoin" tabindex="0">
    Join
  </button>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router';
const router = useRouter();
function handleNewVote() {
  // Handle new vote 
  router.push('/new-vote');

};
function handleJoin() {
  // Handle join action
  router.push('/swipe');
}

</script>

<style scoped>
.logo-image {
  aspect-ratio: 1.37;
  object-fit: contain;
  object-position: center;
  width: 212px;
  border-radius: 23px;
  margin-top: 69px;
  max-width: 100%;
}

.action-button {
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
}

.action-button:focus {
  outline: 2px solid #fff;
  outline-offset: 2px;
}

.action-button-primary {
  background-color: var(--Orange-2, #ffdecf);
  color: var(--Orange-Base, #e95322);
  margin-top: 33px;
}

.action-button-secondary {
  background-color: rgba(179, 38, 30, 1);
  color: var(--Yellow-2, #f3e9b5);
  margin: 48px 0 -34px;
}
</style>
