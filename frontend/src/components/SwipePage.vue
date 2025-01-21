<template>
  <section class="menu-container">
    <SwipeCard v-for="(card, index) in restaurantData"
               :key="index"
               :is-last="index === restaurantData.length - 1"
               :is-next="index === currentCardIndex + 1"
               :isCurrent="index === currentCardIndex"
               :restaurant="card"
               @remove-card-right="swipeRight(index)"
               @remove-card-left="swipeLeft(index)"
    />
  </section>
</template>

<script lang="ts" setup>
import {onMounted, ref} from 'vue';
import SwipeCard from '@/components/SwipeCard.vue';
import {useRoute, useRouter} from 'vue-router';
import axios from 'axios';
import {type EventInfo} from '@/types/EventInfo';
import type Restaurant from '@/types/Restaurants';
import type VoteResult from '@/types/VoteResult';

const route = useRoute();
const router = useRouter();
const eventId = ref('');
const eventData = ref<EventInfo>();
const pictureLink = 'https://images.deliveryhero.io/image/talabat/restaurants/image638562230761380411.jpg?width=180'
const restaurantData = ref<Restaurant[]>([]);
const voteResult = ref<VoteResult[]>([]);

  function decodeSpecialChars(data: string): string {
    try {
      return decodeURIComponent(escape(data));
    } catch (e) {
      console.error('Error decoding special characters:', e);
      return data;
    }
  }

onMounted(() => {
  console.log('Event ID:', route.query.eventId);
  console.log('Event ID:', typeof (route.query.eventId));
  if (route.query.eventId) {
    if (typeof (route.query.eventId) === 'string') {
      eventId.value = route.query.eventId;
    }
    console.log('Event ID: swipe', eventId.value);
    axios.get(`/event/${eventId.value}`)
      .then((response) => {
        console.log(response);
        eventData.value = response.data;
        console.log('Event data:', eventData.value);
        axios.post(`/restaurant/generate-restaurants-for-event`,
          {
            ...eventData.value,
          }
        ).then((response) => {
          console.log('Response: restaurant event', response);
          restaurantData.value = response.data.slice(0, 10).map((item: Restaurant) => ({
            ...item,
            name: decodeSpecialChars(item.name),
            picture: pictureLink,
          }));
        })
      })
      .then(() => {
        console.log('Event data:', eventData.value);
      })
      .catch((error) => {
        console.error('Error fetching event data:', error);
      });
  } else {
    console.error('No event data found in route query');
  }
  console.log('SwipePage mounted');
});


const currentCardIndex = ref(0);

function swipeRight(index: number) {
  console.log('swipe right', index);
  voteResult.value.push({
    eventId: eventId.value,
    restaurantId: restaurantData.value[index].id,
    like: true,
  });
  removeCard(index);
}

function swipeLeft(index: number) {
  console.log('swipe left', index);
  voteResult.value.push({
    eventId: eventId.value,
    restaurantId: restaurantData.value[index].id,
    like: false,
  });
  removeCard(index);
}

function removeCard(index: number) {
  currentCardIndex.value = index + 1;
  if (currentCardIndex.value >= restaurantData.value.length) {
    console.log('All cards have been swiped');
    axios.post(`/event/swipe/${eventId.value}`, voteResult.value)
      .then((response) => {
        console.log('Response: swipe event', response);
      })
      .catch((error) => {
        console.error('Error swiping event:', error);
      });
    router.push({
      path: '/event-page',
      query: {eventId: eventId.value}
    });
  }
}
</script>

<style scoped>
.menu-container {
  display: flex;
  max-width: 441px;
  flex-direction: column;
  padding: 0 18px;
  margin: 0 auto; /* Center horizontally */
  align-items: center; /* Center content */
  margin-top: 30px;
  overflow: hidden;
  touch-action: none;

}
</style>
