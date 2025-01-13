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
        <div class="pizza-display"></div>
        <img
          loading="lazy"
          src="https://cdn.builder.io/api/v1/image/assets/TEMP/6bb35bb0cada82ac439a3eb29df1c6c13a26edcb5f26238ccc798a6341f77bba?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d"
          class="pizza-thumbnail"
          alt="Pizza variety showcase thumbnail"
        />
      </div>
    </div>
    <div class="pizza-options">
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
    <h1 class="menu-title">{{ props.title }}</h1>
    <div class="menu-divider">____________________<br />_______________</div>
</div>
</template>

<script setup lang="ts">
import { defineProps,onMounted,ref, watch, defineEmits } from 'vue';
import interact from 'interactjs';

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
    interactXThreshold: 400
}
const props = defineProps<{
  title: string;
  isCurrent: boolean;
  isNext: boolean;
  isLast: boolean;
}>();
const transformString = ref<string>('translate3D(0,0,0) rotate(0deg)');

function setTransformString(x: number, y: number, rotation: number) {
    console.log('seting transform string',props.title,props.isCurrent);
    if (props.isCurrent) {
        transformString.value = `translate3D(${x}px, ${y}px, 0) rotate(${rotation}deg)`;
    }
    
}

function resetTransformString() {
    transformString.value = 'translate3D(0, 0, 0) rotate(0deg)';
}
   
function initializeInteract() {  
interact('.isCurrent')
  .draggable({
    startAxis: 'x',
    lockAxis: 'x',
    listeners: {
        move(event) {
        interactPosition.x += event.dx;
        interactPosition.y += event.dy;
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
        console.log('endIS te',props.title,interactPosition.x,maxLimits.interactXThreshold);
        if (interactPosition.x > maxLimits.interactXThreshold) {
            console.log('emit remove card',props.title);
            emit('removeCardRight', props.title);
        } else if (interactPosition.x < -maxLimits.interactXThreshold) {
            emit('removeCardLeft', props.title);
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
  { immediate: true }
);



onMounted(() => {
  if (props.isCurrent) {
    initializeInteract();
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
  padding: 21px 0 46px;
  transition: transform 0.3s ease-in-out; /* Add transition for swipe animation */
  user-select: none;
}

.menu-card.isCurrent {
  display: flex; /* Show only the current card */
  z-index: 11;
  position: absolute;
}

.menu-card.isNext {
  display: flex; /* Show only the next card */
  z-index: 10;
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
  width: 175px;
  height: 186px;
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

.menu-title {
  color: rgba(0, 0, 0, 1);
  letter-spacing: -1.3px;
  text-align: center;
  margin-top: 17px;
  width: fit-content;
  font: 700 29px/1 League Spartan, sans-serif;
  word-wrap: break-word; /* Ensure long titles wrap to the next line */
  max-width: 80%; /* Ensure title does not overflow the card */
}

.menu-divider {
  color: rgba(0, 0, 0, 1);
  letter-spacing: -1.3px;
  text-align: center;
  margin-top: 9px;
  font: 700 29px/31px League Spartan, -apple-system, Roboto, Helvetica, sans-serif;
}
</style>
