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
import { ref } from 'vue';
import SwipeCard from '@/components/SwipeCard.vue';

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