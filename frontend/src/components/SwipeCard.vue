<template>
  <div class="menu-card" :class="{isCurrent: isCurrent, isNext : isNext, isLast: isLast}" :style="{transform: transformString}">
    <div class="pizza-gallery">
      <div class="pizza-showcase">
        <img
          loading="lazy"
          src="https://cdn.builder.io/api/v1/image/assets/TEMP/e4762f9fa0a006504c0d5c5ad77f3cf0fd27454d2c3512a3f6edaaf1e4647cef?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d"
          class="pizza-thumbnail"
          alt="Pizza variety showcase thumbnail"
        />
        <div class="pizza-display">
          <img :src="pictureLink()" class="pizza-image" alt="Pizza variety showcase thumbnail" />
        </div>
        <img
          loading="lazy"
          src="https://cdn.builder.io/api/v1/image/assets/TEMP/6bb35bb0cada82ac439a3eb29df1c6c13a26edcb5f26238ccc798a6341f77bba?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d"
          class="pizza-thumbnail"
          alt="Pizza variety showcase thumbnail"
        />
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
    <h1 class="menu-title">{{ restaurant.name }}</h1>
    <div class="description">{{restaurant.description}}</div>
</div>
</template>

<script setup lang="ts">
import { defineProps,onMounted,ref, watch, defineEmits, computed } from 'vue';
import interact from 'interactjs';
import type Restaurant from '@/types/Restaurants';

const interactPosition = {
    x: 0,
    y: 0,
}

const emit = defineEmits(['removeCardRight', 'removeCardLeft']);
const maxLimits : {
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
    interactXThreshold:  window.innerWidth/3,
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
    let   element = document.querySelector('.isCurrent') as HTMLElement;
    if (element) {
      element.style.transform =`translate3D(${x}px, ${y}px, 0) rotate(${rotation}deg)` ;
    }
  }
}

function resetTransformString() {
  let element = document.querySelector('.isCurrent') as HTMLElement;
  if (element) {
    element.style.transform ='translate3D(0, 0, 0) rotate(0deg)';
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
          console.log(event.dx,event.dy);
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
        console.log('endIS te',props.restaurant.name,interactPosition.x,maxLimits.interactXThreshold);
        if (interactPosition.x > maxLimits.interactXThreshold) {
            console.log('emit remove card',props.restaurant.name);
            emit('removeCardRight', props.restaurant.name);
        } else if (interactPosition.x < -maxLimits.interactXThreshold) {
            emit('removeCardLeft', props.restaurant.name);
        }
        else {
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
  { immediate: true }
);

const isMouseDown = ref(false)
const xPosAtRelease = ref(0)
onMounted(() => {
  if (props.isCurrent) {
    initializeInteract();
    addEventListener("mousedown", (event) => {
      isMouseDown.value =true
      }
    )
    addEventListener("mouseup",(event) => {
      isMouseDown.value =false
      console.log("x pos at release",interactPosition.x)
      xPosAtRelease.value = interactPosition.x
    })
  }
});



</script>

<style scoped>
.menu-card {
  border-radius: 35px;
  background: var(--Yellow-2, #f3e9b5);
  display: none; /* Hide all cards by default */
  flex-direction: column;
  align-items: center;
  padding: 21px 0 0px;
  transition: transform 0.3s ease-in-out; /* Add transition for swipe animation */
  user-select: none;
  touch-action: none;
  height: 100%;
  width: 70vw;
  overflow: hidden;
}

.menu-card.isCurrent {
  display: flex; /* Show only the current card */
  z-index: 11;
  position: absolute;
}

.menu-card.isNext {
  display: flex; /* Show only the next card */
  z-index: 10;
  width: 70vw;
  border-radius: 35px;

  background-color: #4CAF50;
}

.menu-card.isLast {
  position: relative; /* Change position to relative for the last card */
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

.pizza-thumbnail {
  object-fit: contain;
  object-position: center;
  width: 55px;
  margin: auto 0;
}

.pizza-display {
  border-radius: 24px;
  background-color: rgba(217, 217, 217, 1);
  width: 100%;
  height: auto;
  max-height: 300px;
  max-width: 300px;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden;
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
  justify-content: center;
}

.option-thumbnail {
  aspect-ratio: 1;
  object-fit: contain;
  object-position: center;
  display: table;
  width: 30px;
  min-width: 30px;
  min-height: 30px;
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

.description {
  color: rgba(0, 0, 0, 1);
  letter-spacing: -1.3px;
  text-align: center;
  margin-top: 9px;
  font: 400 clamp(10px, 2vw, 29px)/31px League Spartan, sans-serif;
}
</style>
