<script setup lang="ts">
import {computed, defineProps} from "vue";
import type Restaurant from "@/types/Restaurants.ts";

const props = defineProps<{
  restaurant: Restaurant;
}>();

const priceLevelString = computed(() => {
  return '€'.repeat(props.restaurant.price_level);
});
</script>

<template>
  <div class="menu-card">
    <div class="pizza-gallery">
      <div class="pizza-showcase">
        <div class="pizza-display">
          <img :src="restaurant.picture ? restaurant.picture : 'https://images.deliveryhero.io/image/talabat/restaurants/image638562230761380411.jpg?width=180'"
                 class="pizza-image" alt="Pizza variety showcase thumbnail" />
        </div>
      </div>
    </div>
    <div class="pizza-options">
      <p class="price-level">{{ priceLevelString }}</p>
      <img
        loading="lazy"
        src="https://cdn.builder.io/api/v1/image/assets/TEMP/fa708cecc73e8b32335f52d05da68f9984267ad789adb3657ccd4c965f3a1297?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d"
        class="option-thumbnail"
        alt="Pizza topping option"
      />
      <img
        loading="lazy"
        src="https://cdn.builder.io/api/v1/image/assets/TEMP/870b983e2b6bb8a36fe91e6a3fb775338c2a8881a64a9313daf1cbb06d20df68?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d"
        class="option-thumbnail"
        alt="Pizza topping option"
      />
      <img
        loading="lazy"
        src="https://cdn.builder.io/api/v1/image/assets/TEMP/a2e62b0d627c4de4e8f0a4e2a4b8e77e8383bf8c911dfab2c5b8fe3ac5d7d2d4?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d"
        class="option-thumbnail"
        alt="Pizza topping option"
      />
    </div>
    <h1 class="menu-title">{{ restaurant.name }}</h1>
    <div class="menu-divider">____________________<br />_______________</div>
  </div>
</template>

<style scoped>
.menu-card {
  border-radius: 35px;
  background: var(--Yellow-2,#e9cc32);
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 21px 0 0px;
  width: 100%;
  height: 100%;
  margin:1vh 0 ;
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
  border-radius: 24px;
  background-color: rgba(217, 217, 217, 1);
  width: 175px;
  height: 186px;
}

.pizza-image {
  max-width: 100%;
  max-height: 100%;
  object-fit: cover;
}
.pizza-options {
  display: flex;
  margin-top: 7px;
  width: 156px;
  max-width: 100%;
  gap: 16px;
  justify-content: space-between;
}

.option-thumbnail {
  aspect-ratio: 1;
  object-fit: contain;
  object-position: center;
  width: 22px;
}

.price-level {
  font-size: clamp(1em, 1.2vw, 1.2em);
  font-weight: bold; /* Make text bold */
}

.menu-title {
  color: rgba(0, 0, 0, 1);
  letter-spacing: -1.3px;
  text-align: center;
  margin-top: 17px;
  width: 100%;
  font: 700 clamp(20px, 5vw, 29px)/1 League Spartan, sans-serif;
  word-wrap: break-word; /* Ensure long titles wrap to the next line */
}

.menu-divider {
  color: rgba(0, 0, 0, 1);
  letter-spacing: -1.3px;
  text-align: center;
  margin-top: 9px;
  font: 700 clamp(20px, 4vw, 29px)/31px League Spartan, sans-serif;
}
</style>
