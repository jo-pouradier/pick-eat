<script setup lang="ts">
import {RouterView} from 'vue-router'
import navbar from './components/NavBar.vue';
</script>

<template>
  <div id="app">
    <navbar class="navbar"/>
    <div class="content">
      <div class="landing-container">
        <RouterView/>
      </div>
    </div>
  </div>
</template>

<style>
.content {
  flex: 1;
  overflow-y: auto; /* Défilement uniquement vertical */
  overflow-x: hidden; /* Évite le défilement horizontal */
  margin-top: calc(var(--navbar-height) + 1em);
  padding: 0.8em;
}
</style>
<style scoped>
#app {
  height: 100svh;
  display: flex;
  flex-direction: column;
}
</style>
