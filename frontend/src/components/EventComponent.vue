<script setup lang="ts">

import {defineProps, onMounted, ref} from 'vue';
import type {EventInfo} from "@/types/EventInfo.ts";
import type {Participant} from "@/types/Participant.ts";
import {useRouter} from "vue-router";
import {getParticipants, loadParticipants} from "@/lib/EventUtils.ts";

const props = defineProps<{ event: EventInfo }>();
const router = useRouter();

const event = ref<EventInfo>(props.event);
const participants = ref<Participant[]>([]);
const showDetails = ref(false);


onMounted(() => {
  getParticipants(event.value.id).then(response => {
    loadParticipants(response.data).then(response => {
        participants.value = response.data;
      }
    );
  });
});

function toggleEventDetails(eventId: string): void {
  if (event.value.id === eventId) {
    showDetails.value = !showDetails.value;
  }
}

function goToEventPage(eventId: string): void {
  console.log('Navigating to event page:', eventId);
  router.push({
    path: '/event-page',
    query: {eventId: event.value.id}
  });
}

</script>

<template>
  <div class="event-header" @click="toggleEventDetails(event.id)">
    <p class="event-name">{{ event.name }}</p>
    <p class="event-place">{{ event.address }}</p>
  </div>
  <transition name="fade">
    <div v-if="showDetails" class="event-details">
      <p>Participants: {{
          participants.map(
            participant => participant.firstName + ' ' + participant.lastName
          ).join(', ')
        }}</p>
      <p>More Info: {{ event.description }}</p>
      <button class="event-button" @click="goToEventPage(event.id)">View Event</button>
    </div>
  </transition>
</template>

<style scoped>
.event-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 15px;
}

.event-name, .event-place {
  font: 700 20px/1 League Spartan, sans-serif;
  color: rgba(0, 0, 0, 1);
}

.event-details {
  margin-top: 10px;
  font: 400 16px/1 League Spartan, sans-serif;
  color: rgba(0, 0, 0, 0.8);
  background-color: #c9c095;
  padding: 10px 15px;
  border-radius: 10px;
  transition: all 0.3s ease;
}

.event-button {
  margin-top: 10px;
  padding: 10px 15px;
  background-color: rgba(179, 38, 30, 1);
  color: var(--Yellow-2, #f3e9b5);
  border: none;
  border-radius: 10px;
  cursor: pointer;
  font: 700 16px/1 League Spartan, sans-serif;
}

.fade-enter-active, .fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter, .fade-leave-to {
  opacity: 0;
}
</style>
