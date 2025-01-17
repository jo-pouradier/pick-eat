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
          users.map(
            participant => participant.firstName + ' ' + participant.lastName
          ).join(', ')
        }}</p>
      <p class="event-more-info">More Info: {{ event.description }}</p>
    </div>
    <div>
      <button v-if="!hasVoted" class="vote-button" @click="handleVote">Vote</button>
      <button v-if="user!=null && event.organizerId==user.uuid" class="close-vote-button" @click="handleCloseVote">Close
        vote
      </button>
    </div>
    <div id="messages-logs-container" class="logs-container">
      <ChatComponent v-for="log in logs.sort(sortByTime)" :key="log.chatId" :chat="log"
                     :user="retrieveUser(log.userId)"/>
    </div>
    <div>
      <img v-if="newImage" :src="newImage" alt="Image preview" class="image-thumbnail"/>
      <span v-if="imageLoadError" class="error-text">{{ imageLoadError }}</span>
    </div>
    <div class="message-input-container">
      <input v-model="newMessage" class="message-input" placeholder="Write a message..." @keyup.enter="sendMessage"/>
      <label class="image-upload-label send-button">
        📷
        <input type="file" @change="handleImageUpload" class="image-upload-input"
               accept="image/png, image/jpeg, image/jpg, image/gif"/>
      </label>
      <button class="send-button" @click="sendMessage">Send</button>
    </div>
  </div>
</template>
<script setup lang="ts">
import {onMounted, onUnmounted, ref} from 'vue';
import {useRoute, useRouter} from 'vue-router';
import type {EventInfo} from "@/types/EventInfo.ts";
import {getParticipants, loadEvent, loadUser, loadUsers} from "@/lib/EventUtils.ts";
import type {User} from "@/types/User.ts";
import {socket} from "@/socket.ts";
import axios from "axios";
import {type ChatInfo} from "@/types/ChatInfo.ts";
import {getUserCookie} from "@/lib/CookieUtils.ts";
import ChatComponent from "@/components/ChatComponent.vue";
import type {Participant} from "@/types/Participant.ts";

const route = useRoute();
const router = useRouter();
const eventId = ref<string>("")
const event = ref<EventInfo>();
const users = ref<User[]>([]);
const hasVoted = ref(false);
const user = ref<User>();
const logs = ref<ChatInfo[]>([]);
onMounted(() => {
  if (socket.connected) {
    socket.on('new_message', handleNewMessageReceived);
  } else {
    socket.on('connect', () => {
      socket.on('new_message', handleNewMessageReceived);
    });
  }
  user.value = getUserCookie();
  if (!user.value) {
    router.push('/login');
  }
  if (route.query.eventId) {
    eventId.value = route.query.eventId as string;
    loadEvent(eventId.value).then(response => {
      event.value = response.data;
    }).catch(() =>
      router.push("/event-list")
    );
    reloadParticipants(eventId.value);
  } else {
    router.push("/event-list");
  }
});

onUnmounted(() => {
  socket.off('new_message', handleNewMessageReceived);
});

async function retrieveUser(userId: string): Promise<User | null> {
  if (!userId) {
    return Promise.resolve(null);
  }
  const part = users.value.find(participant => participant.uuid === userId) as User;
  if (part === undefined || part) {
    return loadUser(userId).then(response => {
      const participant = response as User;
      if (!users.value.find(participant => participant.uuid === userId)) {
        users.value.push(participant);
      }
      return response;
    });
  }
  return Promise.resolve(part);
}

async function reloadParticipants(eventId: string): Promise<void> {
  return getParticipants(eventId).then(response => {
    console.log('Participants:', response);
    const participantsUuids = response.map(participant => participant.userId);
    hasVoted.value = response.filter(participant => participant.uuid === user.value?.uuid && participant.votes.length > 0).length > 0;
    loadUsers(participantsUuids).then(response => {
      users.value = response;
    }).then(() => {
      axios.get('/event/' + eventId + '/messages').then(response => {
        const datas = response.data as ChatInfo[];
        datas.forEach(data => {
          data.date = new Date(data.date);
        });
        logs.value = datas.map(formatMessage);
      }).catch(error => {
        console.error('Error loading messages:', error);
      });
    });
  });
}

// Invert sort order to show latest logs at the bottom
function sortByTime(a: ChatInfo, b: ChatInfo): number {
  return b.date.getTime() - a.date.getTime();
}


function handleCopyLink(): void {
  navigator.clipboard.writeText(window.location.host + '/join-event?eventId=' + eventId.value);
}

function handleCloseVote(): void {
  console.log('Close vote');
}

function handleNewMessageReceived(message: any): void {
  console.log('New message received:', message);
  const chat = message.content as ChatInfo;
  if (chat.eventId !== eventId.value) {
    return;
  }
  chat.date = new Date(chat.date);
  if (!users.value.find(participant => participant.uuid === chat.userId)) {
    retrieveUser(chat.userId).then(() => {
      logs.value.push(formatMessage(chat));
    });
  } else {
    logs.value.push(formatMessage(chat));
  }
}

function formatMessage(message: ChatInfo): ChatInfo {
  if (users.value) {
    users.value.forEach(participant => {
      message.content = message.content.replace(`{${participant.uuid}}`, participant.firstName + ' ' + participant.lastName);
    });
  }
  return message;
}

const newMessage = ref('');
const newImage = ref<string | null>(null);
const imageUploaded = ref(false);
const imageLoadError = ref<string | null>(null);

function goBack(): void {
  router.push({name: 'event-list'});
}

function sendMessage(): void {
  if (newMessage.value.trim() || newImage.value) {
    const formData = new FormData();
    formData.append('message', newMessage.value);
    if (newImage.value) {
      formData.append('file', newImage.value);
    }
    axios.post('/event/' + eventId.value + '/message', formData).catch(error => {
      console.error('Error sending message:', error);
    });
    newMessage.value = '';
    newImage.value = null;
    imageUploaded.value = false;
  }
}

function handleImageUpload(event: Event): void {
  const target = event.target as HTMLInputElement;
  if (target.files && target.files[0]) {
    const size = target.files?.[0].size;
    console.log('Image size:', size);
    if (size && size > 10000000) {
      imageLoadError.value = 'Image size is too large. Please upload an image smaller than 10MB.';
      return;
    }
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

.vote-button, .close-vote-button {
  padding: 10px 15px;
  background-color: rgba(179, 38, 30, 1);
  color: var(--Yellow-2, #f3e9b5);
  border: none;
  border-radius: 10px;
  cursor: pointer;
  font: 700 14px/1 League Spartan, sans-serif;
  margin-top: 10px;
}
</style>
