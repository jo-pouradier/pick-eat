<template>
  <div class="event-list-container">
    <div class="event-list-wrapper">
      <h1 class="main-title">Event List</h1>
      <div v-for="event in events" :key="event.id" class="event-item">
        <EventComponent :event="event"/>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {onMounted, ref} from 'vue';
import EventComponent from "@/components/EventComponent.vue";
import axios from "axios";
import type {EventInfo} from "@/types/EventInfo.ts";

const events = ref<EventInfo[]>([]);
onMounted(() => {
  getEvents();
});

function getEvents() {
  axios.get('/event/history').then(response => {
    console.log('Fetched events:', response.data);
    events.value = response.data;
  });
}
</script>

<style scoped>
.event-list-container {
  display: flex;
  max-width: 480px;
  width: 100%;
  flex-direction: column;
  margin: 0 auto;
}

.event-list-wrapper {
  display: flex;
  flex-direction: column;
  border-radius: 30px;
  position: relative;
  width: 100%;
  padding: 26px 0 0;
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

.event-item {
  width: 100%;
  max-width: 300px;
  margin: 10px 0;
  background: var(--Yellow-2, #f3e9b5);
  border-radius: 10px;
  cursor: pointer;
}
</style>
