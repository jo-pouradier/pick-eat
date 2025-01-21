<template>
  <div class="glass-card glass-container">
    <h1>Event List</h1>
    <div v-for="event in events" :key="event.id" class="events-container">
      <EventComponent :event="event"/>
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
.events-container {
  width: 100%;
  align-items: center;
  display: flex;
  flex-direction: column;
  margin: 0;
}
div{
  margin: 1rem 0;
  align-items: center;
}
</style>
