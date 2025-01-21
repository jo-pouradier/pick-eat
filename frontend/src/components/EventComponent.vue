<script setup lang="ts">

import {defineProps, onMounted, ref} from 'vue';
import type {EventInfo} from "@/types/EventInfo.ts";
import type {User} from "@/types/User.ts";
import {useRouter} from "vue-router";
import {getParticipants, loadUsers} from "@/lib/EventUtils.ts";

const props = defineProps<{ event: EventInfo }>();
const router = useRouter();

const event = ref<EventInfo>(props.event);
const users = ref<User[]>([]);
const showDetails = ref(false);


onMounted(() => {
  getParticipants(event.value.id).then(participants => {
    const participantsUuids = participants.map(participant => participant.userId);
    loadUsers(participantsUuids).then(response => {
        console.log(response)
        users.value = response;
        console.log('Loaded users:', users.value);
      }
    )
      .catch(error => {
        users.value = [];
        console.error('Failed to load users:', error);
      });
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
  <div class="glass-card">
    <div @click="toggleEventDetails(event.id)">
      <h2 class="">{{ event.name }}</h2>
      <p class="">{{ event.address }}</p>
    </div>
    <hr/>
    <transition name="fade">
      <div v-if="showDetails" class="details-container">
        <p>Participants: {{
            users.map(
              participant => participant.firstName + ' ' + participant.lastName
            ).join(', ')
          }}</p>
        <p>More Info: {{ event.description }}</p>
        <button class="view-event-button" @click="goToEventPage(event.id)">View Event</button>
      </div>
    </transition>
  </div>
</template>

<style scoped>
.glass-card {
  width: 95%;
  display: flex;
  flex-direction: column;
  margin: 0.5em;
}

.view-event-button {
  font-size: 0.8em;
  --local-color: var(--accent-orange);
}

button {
  padding: 0.5em 1em;
  margin-bottom: 1em;
  align-self: center;
  align-content: center;
  align-items: center;
}

.details-container{
  display: flex;
  flex-direction: column;
  align-items: center;
}

div {
  margin: 0;
}
</style>
