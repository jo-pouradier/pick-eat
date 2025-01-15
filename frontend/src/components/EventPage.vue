<template>
  <div v-if="event" class="event-page-container">
    <div class="event-links-bar">
      <img loading="lazy" src="@/assets/back.svg" class="back-link" alt="Return to event list" @click="goBack"/>
      <img loading="lazy" src="@/assets/copy.svg" class="copy-link" alt="Copy event link" @click="handleCopyLink"/>
    </div>
    <div>
      <h1 class="main-title">{{ event.name }}</h1>
      <p class="event-place">{{ event.address }}</p>
      <p class="event-participants">Participants: {{
          participants.map(
            participant => participant.firstName + ' ' + participant.lastName
          ).join(', ')
        }}</p>
      <p class="event-more-info">More Info: {{ event.description }}</p>
    </div>
    <div>
      <button class="vote-button" @click="handleVote">Vote</button>
    </div>
    <div id="messages-logs-container" class="logs-container">
      <div v-for="log in logs.sort(sortByTime)" :key="log.id" class="log-item">
        <p class="log-time">{{ log.time.toLocaleTimeString() }}</p>
        <p class="log-message">{{ log.message }}</p>
        <img v-if="log.image" :src="log.image" alt="Log image" class="log-image"/>
      </div>
    </div>
    <div>
      <img v-if="newImage" :src="newImage" alt="Image preview" class="image-thumbnail"/>
    </div>
    <div class="message-input-container">
      <input v-model="newMessage" class="message-input" placeholder="Write a message..." @keyup.enter="sendMessage"/>
      <label class="image-upload-label send-button">
        📷
        <input type="file" @change="handleImageUpload" class="image-upload-input"/>
      </label>
      <button class="send-button" @click="sendMessage">Send</button>
    </div>
  </div>
</template>
<script setup lang="ts">
import {onMounted, ref} from 'vue';
import {useRoute, useRouter} from 'vue-router';
import type {EventInfo} from "@/types/EventInfo.ts";
import {getParticipants, loadEvent, loadParticipants} from "@/lib/EventUtils.ts";
import type {Participant} from "@/types/Participant.ts";

const route = useRoute();
const router = useRouter();
const eventId = ref<string | null>(null)
const event = ref<EventInfo>();
const participants = ref<Participant[]>([]);
onMounted(() => {
  if (route.query.eventId) {
    eventId.value = route.query.eventId as string;
    loadEvent(eventId.value).then(response => {
      event.value = response.data;
      console.log('Event loaded:', event.value);
    }).catch(() =>
      router.push("/event-list")
    );
    getParticipants(eventId.value).then(response => {
      loadParticipants(response.data).then(response => {
          participants.value = response.data;
        }
      );
    });
  } else {
    router.push("/event-list");
  }
});

// Invert sort order to show latest logs at the bottom
function sortByTime(a: Log, b: Log): number {
  return b.time.getTime() - a.time.getTime();
}


function handleCopyLink(): void {
  navigator.clipboard.writeText(window.location.host + '/join-event?eventId=' + eventId.value);
}

interface Log {
  id: number;
  time: Date;
  message: string;
  image?: string; // Optional image property
}

const logs = ref<Log[]>([
  {id: 1, time: new Date(), message: 'Event created'},
  {id: 2, time: new Date(), message: 'Participants joined'},
  {id: 3, time: new Date(), message: 'Event started'},
  {id: 4, time: new Date(), message: 'Event ended', image: 'path/to/image.jpg'},
  {id: 1, time: new Date(), message: 'Event created'},
  {id: 2, time: new Date(), message: 'Participants joined'},
  {id: 3, time: new Date(), message: 'Event started'},
  {id: 4, time: new Date(), message: 'Event ended', image: 'path/to/image.jpg'},
  {id: 1, time: new Date(), message: 'Event created'},
]);

const newMessage = ref('');
const newImage = ref<string | null>(null);
const imageUploaded = ref(false);

function goBack(): void {
  router.push({name: 'event-list'});
}

function sendMessage(): void {
  if (newMessage.value.trim() || newImage.value) {
    logs.value.push({
      id: logs.value.length + 1,
      time: new Date(),
      message: newMessage.value,
      image: newImage.value || undefined
    });
    newMessage.value = '';
    newImage.value = null;
    imageUploaded.value = false;
    scrollToBottom();
  }
}

function scrollToBottom(): void {
  // const logsContainer = document.getElementById('messages-logs-container');
  // if (logsContainer) {
  //   logsContainer.scrollTop = logsContainer.clientHeight;
  // }
}

function handleImageUpload(event: Event): void {
  const target = event.target as HTMLInputElement;
  if (target.files && target.files[0]) {
    const reader = new FileReader();
    reader.onload = (e) => {
      newImage.value = e.target?.result as string;
      imageUploaded.value = true;
    };
    reader.readAsDataURL(target.files[0]);
  }
}

function handleVote(): void {
  router.push({name: 'swipe', query: {eventId: eventId.value}});
}
</script>

<style scoped>
.event-page-container {
  display: flex;
  flex-direction: column;
  max-width: 480px;
  width: 100%;
  margin: 0 auto;
  align-items: center;
  background-color: #f3e9b5;
  border-radius: 10px;
  padding: 15px;
  margin-top: calc(5vh + 60px); /* Adjusted for nav bar height */
  height: calc(100vh - 9vh - 60px); /* Full height minus nav bar and margin */
  position: relative; /* Add this to position the marker */
}

.event-card {
  display: flex;
  flex-direction: column;
  border-radius: 20px;
  background: var(--Yellow-2, #f0e196);
  padding: 10px;
  width: 100%;
  max-width: 300px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  align-items: center;
  margin-bottom: 10px;
}

.main-title {
  color: rgb(0, 0, 0);
  letter-spacing: -0.28px;
  text-align: center;
  margin: 5px 0;
  font: 400 24px/1 Lobster, sans-serif;
}

.event-place, .event-participants, .event-more-info {
  font: 400 14px/1 League Spartan, sans-serif;
  color: rgba(0, 0, 0, 1);
  margin: 3px 0;
  text-align: center;
}

.logs-container {
  display: flex;
  flex-direction: column;
  width: 100%;
  padding: 1em;
  overflow-y: scroll;
  flex: 1; /* Take available space */
  flex-direction: column-reverse;
}

.log-item {
  display: flex;
  flex-direction: column;
  margin-bottom: 10px;
  width: 100%;
  background: #f3f3f3;
  border-radius: 10px;
  padding: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  height: fit-content;
}

.log-time {
  font: 400 12px/1 League Spartan, sans-serif;
  color: rgba(0, 0, 0, 0.6);
}

.log-message {
  font: 400 14px/1 League Spartan, sans-serif;
  color: rgba(0, 0, 0, 1);
}

.log-image {
  max-width: 100%;
  border-radius: 10px;
  margin-top: 5px;
}

.message-input-container {
  display: flex;
  width: 100%;
  max-width: 400px;
  margin-top: 5px;
  padding: 5px;
  position: sticky;
  bottom: 0;
  background-color: #f3e9b5; /* Match the background color */
  position: relative; /* Add this to position the marker */
  align-items: center;
}

.message-input {
  flex: 1;
  padding: 10px;
  border-radius: 10px;
  border: 1px solid #ccc;
  font: 400 14px/1 League Spartan, sans-serif;
}

.image-upload-label {
  display: flex;
  align-items: center;
  cursor: pointer;
  margin-left: 10px;
}

.image-upload-icon {
  width: 24px;
  height: 24px;
}

.image-upload-input {
  display: none;
}

.send-button {
  padding: 10px 15px;
  background-color: rgba(179, 38, 30, 1);
  color: var(--Yellow-2, #f3e9b5);
  border: none;
  border-radius: 10px;
  cursor: pointer;
  font: 700 14px/1 League Spartan, sans-serif;
  margin-left: 10px;
}

.image-uploaded-marker {
  position: absolute;
  top: -30px;
  right: 10px;
  background-color: rgba(0, 128, 0, 0.8);
  color: white;
  padding: 5px 10px;
  border-radius: 5px;
  font: 700 14px/1 League Spartan, sans-serif;
}

.fade-enter-active, .fade-leave-active {
  transition: opacity 0.5s;
}

.fade-enter, .fade-leave-to /* .fade-leave-active in <2.1.8 */
{
  opacity: 0;
}

.image-thumbnail {
  max-width: 100%;
  max-height: 100px;
  margin-top: 10px;
}

.event-links-bar {
  display: grid;
  grid-template-columns: 1fr 1fr;
}

.copy-link {
  width: 24px;
  height: 24px;
  cursor: pointer;
  position: absolute;
  top: 10px;
  right: 10px;
}

.back-link {
  width: 24px;
  height: 24px;
  cursor: pointer;
  position: absolute;
  top: 10px;
  left: 10px;
}
</style>
