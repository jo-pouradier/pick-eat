<script setup lang="ts">

import {defineProps, onMounted, ref} from 'vue';
import type {User} from "@/types/User.ts";
import type {ChatInfo} from "@/types/ChatInfo.ts";
import {getUserCookie} from "@/lib/CookieUtils.ts";
import SwipeCard from "@/components/SwipeCard.vue";
import RestaurantCardComponent from "@/components/RestaurantCardComponent.vue";
import type Restaurant from "@/types/Restaurants.ts";
import axios from "axios";

const props = defineProps<{ chat: ChatInfo, user: Promise<User | null> }>();

const chat = ref<ChatInfo>(props.chat);
const userChat = ref<User | null>(null);
const user = getUserCookie()
const isOwnChat = ref(false);
const restaurant = ref<Restaurant>();
onMounted(async () => {
  userChat.value = await props.user;
  if (user) {
    isOwnChat.value = user?.uuid === chat.value.userId;
  }
  if (chat.value.type === 'resultRestaurant') {
    axios.post('/restaurant/'+chat.value.content)
      .then((response) => {
      restaurant.value = response.data;
    });
  }
});
</script>

<template>
  <div v-if="userChat" :class="['chat-item', chat.type+'-message']">
    <div  v-if="chat.type=='user'" class="chat-header">
      <span class="chat-user">{{ userChat.firstName +' '+ userChat.lastName }}</span>
      <span class="chat-date">{{ chat.date.toLocaleString() }}</span>
    </div>
    <div class="chat-content">
      <p v-if="chat.content">{{ chat.content }}</p>
      <img v-if="chat.imagePath" :src="chat.imagePath" alt="Chat Image" class="chat-image"/>
    </div>
  </div>
  <RestaurantCardComponent :restaurant="restaurant" v-if="restaurant" />

</template>

<style scoped>

span {
  margin: 0 5px;
}
.chat-item {
  display: flex;
  flex-direction: column;
  margin-bottom: 10px;
  padding: 10px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  max-width: 90%;
}

.log-message {
  background: #8b8b8b;
  align-self: center;
  color: #ffffff; /* Set text color to white */
}

.user-message {
  background: v-bind(isOwnChat ? '#d0e7ff': '#f3f3f3');
  align-self: v-bind(isOwnChat ? 'flex-end': 'flex-start');
}

.chat-header {
  display: flex;
  justify-content: space-between;
  font: 400 12px/1 League Spartan, sans-serif;
  color: rgba(0, 0, 0, 0.6);
}

.chat-user {
  font-weight: bold;
}

.chat-date {
  font-style: italic;
}

.chat-content {
  margin-top: 5px;
}

.chat-image {
  max-width: 100%;
  border-radius: 10px;
  margin-top: 5px;
}
</style>
