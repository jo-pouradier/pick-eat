<template>
  <section class="menu-container">
    <SwipeCard v-for="(card, index) in cards" 
            :key="index" 
            :title="card.title" 
            :isCurrent="index === currentCardIndex"
            :is-next="index === currentCardIndex + 1"
            :is-last="index === cards.length - 1"
            @remove-card-right="removeCard(index)"
            @remove-card-left="removeCard(index)"
            />
  </section>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import SwipeCard from '@/components/SwipeCard.vue';
import { useRoute } from 'vue-router';
import axios from 'axios';
import { type EventInfo } from '@/types/EventInfo';

const route = useRoute();

const eventId = ref(null);
const eventData = ref<EventInfo>();

onMounted(() => {
  console.log('Event ID:', route.query.data);
    if (route.query.data) {
        if (typeof route.query.data === 'string') {
            eventId.value = JSON.parse(route.query.data);

            axios.get(`/events/${eventId.value}`)
            .then((response) => {
                eventData.value = response.data;
                axios.get(`restaurants/generate-restaurants-for-event`,
                {
                    params: {
                        event: eventId.value,
                    },
                }
                )
            })
            .then(() => {
                console.log('Event data:', eventData.value);
            })
            .catch((error) => {
                console.error('Error fetching event data:', error);
            });
        }
    }
  console.log('SwipePage mounted');
});


const cards = [
  { title: 'Pizza de la mama de dalbeigue' },
  { title: 'Pasta' },
  { title: 'Salad' },
  { title: 'Dessert' },
  { title: 'Drinks' },
];

const currentCardIndex = ref(0);

function removeCard(index: number) {
  currentCardIndex.value = index + 1;

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
}
</style>