<script setup lang="ts">
import { RouterView } from 'vue-router'
import navbar from './components/NavBar.vue';
</script>

<template>
  <div id="app">
    <navbar />
    <div class="content">
      <div class="landing-container">
        <div class="landing-content">
          <RouterView />
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
header {
  background-color: #333;
  color: white;
  padding: 1rem;
  text-align: center;
}

.content {
  background-image: url('@/assets/homepage_background.jpg');
  background-size: cover;
  background-repeat: no-repeat;
  height: 100vh; /* Changed to always take full height of the screen */
  display: flex;
  justify-content: center;
  align-items: center;
}

.landing-container {
  display: flex;
  max-width: 480px;
  width: 100%;
  flex-direction: column;
  margin: 0 auto;
}

.landing-content {
  display: flex;
  flex-direction: column;
  border-radius: 30px;
  position: relative;
  width: 100%;
  align-items: center;
}
</style>
