<script setup lang="ts">
import {computed, defineProps} from "vue";
import type Restaurant from "@/types/Restaurants.ts";

const props = defineProps<{
  restaurant: Restaurant;
}>();
console.log(props.restaurant);
const priceLevelString = computed(() => {
  return '€'.repeat(props.restaurant.price_level);
});
const pictureLink = () => {
  return "/images/restaurants_photos/" + props.restaurant.picture;
}
</script>
<template>
  <div class="menu-card">
    <div class="pizza-gallery">
      <div class="pizza-showcase">
        <div class="pizza-display">
          <img
            :src="restaurant.picture ? pictureLink() : 'https://images.deliveryhero.io/image/talabat/restaurants/image638562230761380411.jpg?width=180'"
            class="pizza-image" alt="Pizza variety showcase thumbnail"/>
        </div>
      </div>
    </div>
    <div class="pizza-options">
      <div v-for="icon in restaurant.icons" :key="icon">
        <img
          loading="lazy"
          :src="'/images/icons/' + icon"
          class="option-thumbnail"
          alt="Pizza topping option"
        />
      </div>
    </div>
    <h3>{{ restaurant.name }}</h3>
    <h4>{{ restaurant.address }}</h4>
    <div class="description">{{restaurant.description}}</div>
  </div>
</template>

<style scoped>
.menu-card {
  border-radius: 35px;
  background: var(--light-orange);
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 21px 0 0px;
  width: 100%;
  height: 100%;
  margin: 1vh 0;
}
.pizza-options {
  display: flex;
  align-items: center;
  margin: 0;
  max-width: 100%;
  gap: 16px;
}

.option-thumbnail {
  object-fit: contain;
  object-position: center;
  display: table;
  width: 30px;
  min-width: 30px;
  min-height: 30px;
}

.pizza-gallery {
  display: flex;
  gap: 4px;
  justify-content: center; /* Center content horizontally */
}

.pizza-showcase {
  display: flex;
  flex-grow: 1;
  flex-basis: auto;
  justify-content: center; /* Center content horizontally */
  align-items: center; /* Center content vertically */
}


.pizza-display {
  padding: 1em;
}

.pizza-image {
  max-width: 100%;
  max-height: 100%;
  object-fit: cover;
}

.pizza-options {
  display: flex;
  align-items: center;
  margin: 0;
  max-width: 100%;
  gap: 16px;
}

.option-thumbnail {
  aspect-ratio: 1;
  object-fit: contain;
  object-position: center;
  width: 22px;
}

.description {
  color: rgba(0, 0, 0, 1);
  letter-spacing: -1.3px;
  text-align: center;
  font: var(--secondary-font);
  word-wrap: break-word;
  max-width: 80%;
  overflow-y: scroll;
}

h3, h4 {
  margin: 0;
  padding: 0;
  text-align: center;
}

div{
  margin: 0;
}
</style>
