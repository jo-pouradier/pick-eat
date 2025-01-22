<template>
  <div v-if="event" class="glass-card glass-container event-page-container">
    <div class="event-header">
      <div>
        <img loading="lazy" src="@/assets/back.svg" class="back-link" alt="Return to event list" @click="goBack"/>
        <!-- <img loading="lazy" src="@/assets/copy.svg" class="copy-link" alt="Copy event link" @click="handleCopyLink"/> -->
        <img loading="lazy" :src="copyIcon" class="copy-link" alt="Copy event link" @click="handleCopyLink"/>
      </div>
      <div>
        <h1>{{ event.name }}</h1>
        <p>{{ event.address }}</p>
        <p><b>Participants:</b> {{
            users.map(
              participant => participant.firstName + ' ' + participant.lastName
            ).join(', ')
          }}</p>
        <p class="event-more-info"><b>More Info:</b> {{ event.description }}</p>
      </div>
      <hr>
      <div class="buttons-container">
        <button v-if="!hasVoted" class="vote-button" @click="handleVote">Vote</button>
        <div v-else>
          <p>You have already voted!</p>
        </div>
        <button v-if="user!=null && event.organizerId==user.uuid && !event.voteFinished" class="close-vote-button"
                @click="handleCloseVote">Close
          vote
        </button>
        <button class="bill-button" @click="handleBill">Go to bill</button>
      </div>
    </div>
    <div class="chat-container">
      <div class="messages-container">
        <ChatComponent v-for="log in logs.sort(sortByTime)" :key="log.chatId" :chat="log"
                       :user="retrieveUser(log.userId)"/>
      </div>
      <div class="chat-bottom-bar">
        <div>
          <img v-if="newImage" :src="newImage" alt="Image preview" class="image-thumbnail"/>
          <span v-if="imageLoadError" class="error-text">{{ imageLoadError }}</span>
        </div>
        <hr>
        <div class="message-input-container">
          <input v-model="newMessage" class="message-input" placeholder="Write a message..."
                 @keyup.enter="sendMessage"/>
          <div class="send-image-container">
            <label class="image-upload-label send-image-button">
              📷
              <input type="file" @change="handleImageUpload" class="image-upload-input"
                     accept="image/png, image/jpeg, image/jpg, image/gif"/>
            </label>
          </div>
          <button class="send-message-button" @click="sendMessage">Send</button>
        </div>
      </div>
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

import COPY_ICON from "@/assets/copy.svg";
import COPIED_ICON from "@/assets/copied.svg";

const route = useRoute();
const router = useRouter();
const eventId = ref<string>("")
const event = ref<EventInfo>();
const users = ref<User[]>([]);
const hasVoted = ref(false);
const user = ref<User>();
const logs = ref<ChatInfo[]>([]);
const copyIcon = ref(COPY_ICON);

function init() {
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

}

onMounted(() => {
  init();
});

onUnmounted(() => {
  socket.off('new_message', handleNewMessageReceived);
});

async function retrieveUser(userId: string): Promise<User | null> {
  if (!userId) {
    return Promise.resolve(null);
  }
  const part = users.value.find(participant => participant.uuid === userId) as User;
  if (part === undefined || !part) {
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
    const participantsUuids = response.map(participant => participant.userId);
    hasVoted.value = response.filter(participant => participant.userId === user.value?.uuid && participant.votes.length > 0).length > 0;
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
  copyIcon.value = COPIED_ICON;
  setTimeout(() => {
    copyIcon.value = COPY_ICON;
  }, 1000);
  navigator.clipboard.writeText(window.location.origin + '/join-event?eventId=' + eventId.value);
}

function handleCloseVote(): void {
  console.log('Close vote');
  axios.get("/event/close/" + eventId.value)
    .then((response) => {
      console.log('Vote closed', response);
      init()
    })
}

function handleNewMessageReceived(message: any): void {
  console.log('New message received:', message);
  const chat = message.content as ChatInfo;
  chat.date = new Date(chat.date);
  if (chat.eventId !== eventId.value) {
    return;
  }
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

function handleBill(): void {
  console.log('Go to bill', eventId.value);
  router.push({name: 'bill', query: {eventId: eventId.value}});
}

function handleVote(): void {
  router.push({name: 'swipe', query: {eventId: eventId.value}});
}
</script>
<style scoped>
.event-header {
  z-index: 1;
}

.buttons-container{
  display: flex;
  justify-content: space-between;
}

.chat-container {
  display: flex;
  z-index: -1;
  width: 100%;
  margin-top: 1em;
  padding-bottom: 2em;
  background-color: #fdfdfd;
  flex-direction: column;
  overflow: hidden;
}

.chat-bottom-bar {
  display: flex;
  align-items: center;
  z-index: 1;
  flex-direction: column;
}

.messages-container {
  margin-top: 1em;
  z-index: -1;
  width: max-content;
  display: flex;
  flex-direction: column-reverse;
  overflow-y: auto;
  max-height: 50vh;
  max-width: 90%;
}

.message-input-container {
  display: flex;
  flex-direction: row;
}

.message-input {
  flex: 1;
  padding: 10px;
  border-radius: 10px;
  border: 1px solid #ccc;
  font: 400 14px/1 League Spartan, sans-serif;
  height: fit-content;
}

.image-upload-input {
  display: none;
}

.send-image-container{
  aspect-ratio: 1/1;
  background-color: var(--accent-orange);
  border-radius: 5px;
  height: 100%;
  justify-content: center;
  margin: 0 0.2em;
  padding: 0.1em 0.1em;
  align-content: center;
  border: 1px solid #ccc;
}

.send-image-button {
  align-items: center;
  cursor: pointer;
}

.image-thumbnail {
  max-width: 100%;
  max-height: 100px;
  margin-top: 10px;
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

.vote-button {
  --local-color: var(--accent-orange);
}

.close-vote-button {
  --local-color: var(--red-wrong);

}

.bill-button {
  --local-color: var(--accent-orange);
}

button {
  font-size: 0.8em;
  padding: 0.2em 0.3em;
}

div {
  margin: 0;
  align-items: center;
}

hr {
  margin: 0;
  margin-bottom: 0.5em;
}

.event-page-container {
  overflow: hidden;
  height: max-content;
}
</style>
