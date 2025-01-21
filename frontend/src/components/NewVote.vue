<template>
  <div class="glass-card glass-container">
    <h1 class="create-vote-title">Create a vote!</h1>
    <p class="input-label">Event name :</p>
    <input class="name-selector" tabindex="0" role="textbox"
           placeholder="Select a name"
           v-model="eventData.name"/>
    <p>Description :</p>
    <textarea class="description-textarea" placeholder="Enter description" v-model="eventData.description"></textarea>
    <p class="input-label">Time :</p>
    <input class="name-selector" tabindex="0" role="textbox"
           type="datetime-local"
           v-model="eventData.date.setTime"/>
    <p>Select types :</p>
    <select multiple v-model="eventData.types">
      <option value="asian_restaurant">chinois</option>
      <option value="bar">Bar</option>
      <option value="indian_restaurant">indien</option>
      <option value="mexican_restaurant">mexicain</option>
      <option value="pizza_restaurant">pizza</option>
      <option value="japanese_restaurant">sushi</option>
      <option value="hamburger_restaurant">burger</option>
      <option value="fast_food_restaurant">fast food</option>
      <option value="dessert_shop">dessert</option>
      <option value="sandwich_shop">sandwich</option>
    </select>
    <div class="map-select-container">
      <div class="range-container">
          <span id="rs-bullet" class="rs-label"
                v-bind:style="{'left': getRangeSpanPosition()+'px'}">{{
              eventData.radius / 1000
            }}</span>
        <input ref="rangeInput" type="range" v-model.number="eventData.radius" min="100" :max="maxSliderValue" step="100"
               class="range-slider"/>
      </div>
      <MapComponent @locationSelected="handleLocationSelected" :selectedRange="eventData.radius"
                    :selectedCoords="eventData.getCoords()"/>
    </div>
    <p v-if="eventData.address" class="input-label">Address : {{ eventData.address }}</p>
    <hr/>
    <button class="validate-button" @click="handleValidation">
      Confirm
    </button>
  </div>
</template>

<script setup lang="ts">
import {useRouter} from 'vue-router';
import {ref, useTemplateRef} from 'vue';
import MapComponent from './MapComponent.vue'; // Import the new MapComponent
import axios from 'axios';
import type {EventInfo} from '@/types/EventInfo';
import {getUserCookie} from "@/lib/CookieUtils.ts";

const rangeInput = useTemplateRef('rangeInput');
const router = useRouter();
const isLoading = ref(false);
const eventData = ref<EventInfo>({
  id: '',
  name: '',
  date: new Date(),
  address: '',
  latitude: 0,
  longitude: 0,
  radius: 100,
  description: '',
  organizerId: null,
  voteFinished: false,
  types: [],
  getCoords() {
    return [this.latitude, this.longitude];
  },
  setCoords(latOrCoords: number | [number, number], long?: number) {
    if (Array.isArray(latOrCoords)) {
      this.latitude = latOrCoords[0];
      this.longitude = latOrCoords[1];
    } else if (typeof latOrCoords === 'number' && typeof long === 'number') {
      this.latitude = latOrCoords;
      this.longitude = long;
    }
  }
});
const maxSliderValue = 5000;

function getRangeSpanPosition(): number {
  const ratio = (eventData.value.radius - 100) / maxSliderValue;
  console.log(ratio);
  const c = (rangeInput.value ? rangeInput.value.offsetWidth + rangeInput.value.offsetLeft : 0);
  console.log(c);
  return c * ratio;
}

function handleValidation(): void {
  if (eventData.value.address && eventData.value.name !== '') {
    isLoading.value = true;
    console.log('Selected coordinates:', eventData.value.getCoords());
    axios.post('/event/create',
      eventData.value
    ).then(response => {
      console.log(response.data);
      console.log('Event created');
      axios.post('/billing/bills/',
        {
          eventId: response.data,
          userId: getUserCookie()?.uuid
        }
      ).then(() => {
        console.log('Bill created');
        router.push({
          path: '/event-page',
          query: {eventId: response.data}
        });
      }).finally(() => {
        isLoading.value = false
      })

    }).catch(error => {
      alert('Error creating event' + error);
      console.error('Error creating event:', error);
      isLoading.value = false;
    });
  } else {
    if (eventData.value.name === '') {
      alert('Please select a name for the vote.');
    } else
      alert('Please select a place on the map.');
  }

}

function updateAddress(): void {
  if (eventData.value.getCoords()) {
    axios.get(`https://nominatim.openstreetmap.org/reverse?lat=${eventData.value.latitude}&lon=${eventData.value.longitude}`)
      .then((response) => {
        const parser = new DOMParser();
        const xmlDoc = parser.parseFromString(response.data, "application/xml");
        const road = xmlDoc.getElementsByTagName("road")[0]?.textContent || '';
        const city = xmlDoc.getElementsByTagName("city")[0]?.textContent || '';
        eventData.value.address = `${road}, ${city}`;
      });
  }
}

function handleLocationSelected(coords: [number, number]): void {
  eventData.value.setCoords(coords);
  updateAddress();
}

</script>

<style scoped>
.map-select-container {
  width: 100%;
  margin: 0;
}

.rs-label {
  position: relative;
  transform-origin: center center;
  display: block;
  width: 2.5em;
  height: 2.5em;
  background: transparent;
  border-radius: 50%;
  text-align: center;
  align-self: center;
  font-weight: bold;
  padding-top: 0.3em;
  box-sizing: border-box;
  border: 2px solid var(--black);
  color: var(--black);
  font-style: normal;
  line-height: normal;
  font-size: 0.8em;

  &::after {
    content: "km";
    display: block;
    font-size: 0.5em;
    letter-spacing: 0.07em;
    margin-top: -2px;
  }
}

.range-slider {
  accent-color: var(--black);
  width: 50%;
  align-self: center;
  height: 1em;
  border-radius: 0.5em;
  margin-top: 0;
  padding: 0;
}
</style>
