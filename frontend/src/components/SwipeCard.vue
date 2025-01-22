<template>
  <div class="card-show" :class="{isNext: isNext, isLast: isLast, isCurrent: isCurrent}"
       :style="{transform: transformString}">
    <div class="glass-card glass-container menu-card">
      <h3>{{ restaurant.name }}</h3>
      <div class="pizza-showcase">
        <img
          loading="lazy"
          src="https://cdn.builder.io/api/v1/image/assets/TEMP/e4762f9fa0a006504c0d5c5ad77f3cf0fd27454d2c3512a3f6edaaf1e4647cef?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d"
          class="pizza-thumbnail"
          alt="Pizza variety showcase thumbnail"
        />
        <div>
          <img :src="pictureLink()" class="pizza-image" alt="Pizza variety showcase thumbnail"/>
        </div>
        <img
          loading="lazy"
          src="https://cdn.builder.io/api/v1/image/assets/TEMP/6bb35bb0cada82ac439a3eb29df1c6c13a26edcb5f26238ccc798a6341f77bba?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d"
          class="pizza-thumbnail"
          alt="Pizza variety showcase thumbnail"
        />
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
      <h4>{{ restaurant.address }}</h4>
      <hr/>
      <div class="description">{{ restaurant.description }}</div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {defineEmits, defineProps, onMounted, ref, watch} from 'vue';
import interact from 'interactjs';
import type Restaurant from '@/types/Restaurants';

const interactPosition = {
  x: 0,
  y: 0,
}

const emit = defineEmits(['removeCardRight', 'removeCardLeft']);
const maxLimits: {
  interactMaxRotation: number,
  interactOutOfSightXCoordinate: number,
  interactOutOfSightYCoordinate: number,
  interactYThreshold: number,
  interactXThreshold: number
} = {
  interactMaxRotation: 15,
  interactOutOfSightXCoordinate: 450,
  interactOutOfSightYCoordinate: 600,
  interactYThreshold: 150,
  interactXThreshold: window.innerWidth / 3,
}
const props = defineProps<{
  restaurant: Restaurant;
  isCurrent: boolean;
  isNext: boolean;
  isLast: boolean;
}>();
const transformString = ref<string>('translate3D(0,0,0) rotate(0deg)');
const pictureLink = () => {
  return "/images/restaurants_photos/" + props.restaurant.picture;
}

function setTransformString(x: number, y: number, rotation: number) {
  if (props.isCurrent) {
    let element = document.querySelector('.isCurrent') as HTMLElement;
    if (element) {
      element.style.transform = `translate3D(${x}px, ${y}px, 0) rotate(${rotation}deg)`;
    }
  }
}

function resetTransformString() {
  let element = document.querySelector('.isCurrent') as HTMLElement;
  if (element) {
    element.style.transform = 'translate3D(0, 0, 0) rotate(0deg)';
  }
}

function initializeInteract() {
  interact('.isCurrent')
    .draggable({
      startAxis: 'x',
      lockAxis: 'x',
      inertia: true,
      listeners: {
        move(event) {
          console.log(event.dx, event.dy);
          if (isMouseDown) {
            interactPosition.y += event.dy;
            interactPosition.x += event.dx;
          }
          let rotation = maxLimits.interactMaxRotation * (interactPosition.x / maxLimits.interactXThreshold);

          if (rotation > maxLimits.interactMaxRotation) rotation = maxLimits.interactMaxRotation;
          else if (rotation < -maxLimits.interactMaxRotation)
            rotation = -maxLimits.interactMaxRotation;

          if (interactPosition.x >= maxLimits.interactOutOfSightXCoordinate) {
            setTransformString(maxLimits.interactOutOfSightXCoordinate, interactPosition.y, rotation);
          } else if (interactPosition.x <= -maxLimits.interactOutOfSightXCoordinate) {
            setTransformString(-maxLimits.interactOutOfSightXCoordinate, interactPosition.y, rotation);
          } else {
            setTransformString(interactPosition.x, interactPosition.y, rotation);
          }
        },
        end() {
          console.log('endIS te', props.restaurant.name, interactPosition.x, maxLimits.interactXThreshold);
          if (interactPosition.x > maxLimits.interactXThreshold) {
            console.log('emit remove card', props.restaurant.name);
            emit('removeCardRight', props.restaurant.name);
          } else if (interactPosition.x < -maxLimits.interactXThreshold) {
            emit('removeCardLeft', props.restaurant.name);
          } else {
            resetTransformString();
          }
        },
      },
    });
}

function destroyInteract() {
  interact('.isCurrent').unset();
  resetTransformString();
}

watch(
  () => props.isCurrent,
  (newVal) => {
    if (newVal) {
      initializeInteract();
    } else {
      destroyInteract();
    }
  },
  {immediate: true}
);

const isMouseDown = ref(false)
const xPosAtRelease = ref(0)
onMounted(() => {
  if (props.isCurrent) {
    initializeInteract();
    addEventListener("mousedown", (event) => {
        isMouseDown.value = true
      }
    )
    addEventListener("mouseup", (event) => {
      isMouseDown.value = false
      console.log("x pos at release", interactPosition.x)
      xPosAtRelease.value = interactPosition.x
    })
  }
});


</script>

<style scoped>
.card-show {
  display: none;
}

.menu-card {
  display: flex; /* Hide all cards by default */
  transition: transform 0.3s ease-in-out; /* Add transition for swipe animation */
  user-select: none;
  touch-action: none;
  overflow: hidden;
  align-items: center;
  background-color: var(--light-orange);
  width: 70vw; /* Set fixed width */
  height: 60svh; /* Set fixed height */
  max-width: 500px; /* Set max width */
  max-height: 60svh; /* Set max height */
  box-sizing: border-box; /* Include padding in the width */
  flex-direction: column;
  justify-content: space-between; /* Ensure content is spaced evenly */
}

.card-show.isCurrent {
  display: flex; /* Show only the current card */
  z-index: 11;
  position: absolute;
}

.card-show.isNext {
  display: flex; /* Show only the next card */
  z-index: 10;
}

.card-show.isLast {
  position: relative; /* Change position to relative for the last card */
}


.pizza-showcase {
  display: flex;
  justify-content: space-between; /* Distribute space between items */
  align-items: center; /* Center items vertically */
}

.pizza-thumbnail {
  max-width: 3.5em;
  max-height: 5em;
  object-fit: cover; /* Ensure images cover the area without stretching */
}

.pizza-image {
  max-width: 10em;
  max-height: 10em;
  object-fit: cover;
  border-radius: 10px;
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
  width: 25px; /* Reduce width */
  min-width: 25px; /* Reduce min-width */
  min-height: 25px; /* Reduce min-height */
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

.content div {
  margin: 0;
}

h3, h4{
  margin: 0;
}
</style>
