<template>
  <div class="event-creation-container">
    <div class="event-creation-wrapper">
      <h1 class="main-title">Create a vote!</h1>
      <p class="input-label">Vote name :</p>
      <input class="name-selector" tabindex="0" role="textbox"
             placeholder="Select a name"
             v-model="eventData.name"/>
      <p>Description :</p>
      <textarea class="description-textarea" placeholder="Enter description" v-model="eventData.description"></textarea>
      <p>Select a place :</p>
      <button class="open-map-button" @click="openMapModal">Open Map</button>
      <h1 class="second-title" v-if="eventData.address"> Selected place :
        <br> {{ eventData.address }}</h1>
      <dialog v-if="showMapModal" class="modal-dialog" @close="showMapModal = false">
        <div class="modal-content">
          <MapComponent @locationSelected="handleLocationSelected" :selectedRange="eventData.radius"
                        :selectedCoords="eventData.getCoords()"/>
          <button class="close-map-button" @click="closeMapModal">Close Map</button>
        </div>
        <div class="range-container">
          <p>Select a range (metres):</p>
          <input type="range" v-model.number="eventData.radius" min="1" max="1000" class="range-slider"/>
          <p>Selected range: {{ eventData.radius }}</p>
        </div>
      </dialog>
    </div>
  </div>
  <button class="validate-button" @click="handleValidation">
    Valider
  </button>
</template>

<script setup lang="ts">
import {useRouter} from 'vue-router';
import {nextTick, ref} from 'vue';
import MapComponent from './MapComponent.vue'; // Import the new MapComponent
import axios from 'axios';
import type {EventInfo} from '@/types/EventInfo';
import {getUserCookie} from "@/lib/CookieUtils.ts";

const router = useRouter();
const showMapModal = ref(false);
const eventData = ref<EventInfo>({
  id: '',
  name: '',
  date: new Date(),
  address: '',
  latitude: 0,
  longitude: 0,
  radius: 50,
  description: '',
  organizerId: null,
  voteFinished: false,
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

function handleValidation(): void {
  if (eventData.value.address && eventData.value.name !== '') {
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
      }
      )

    });
  } else {
    if (eventData.value.name === '') {
      alert('Please select a name for the vote.');
    } else
      alert('Please select a place on the map.');
  }

};

function openMapModal(): void {
  showMapModal.value = true;
  nextTick(() => {
    const dialog = document.querySelector('.modal-dialog') as HTMLDialogElement;
    dialog.showModal();
  });
}

function closeMapModal(): void {
  showMapModal.value = false;
  const dialog = document.querySelector('.modal-dialog') as HTMLDialogElement;
  dialog.close();
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
}

</script>

<style scoped>
.event-creation-container {
  display: flex;
  max-width: 480px;
  width: 100%;
  flex-direction: column;
  background-color: #f3e9b5;
  border-radius: 10px;
}

.event-creation-wrapper {
  display: flex;
  flex-direction: column;
  border-radius: 30px;
  position: relative;
  width: 100%;
  padding: 26px 0;
  align-items: center;
}


.main-title {
  position: relative;
  color: rgb(0, 0, 0);
  letter-spacing: -0.28px;
  text-align: center;
  font: 400 55px/1 Lobster, sans-serif;
}

.second-title {
  position: relative;
  color: rgb(0, 0, 0);
  letter-spacing: -0.28px;
  text-align: center;
  font: 400 30px/1 Lobster, sans-serif;
}

.name-selector {
  position: relative;
  border-radius: 44px;
  background: var(--Yellow-2, #696343);
  margin: 17px 0 0;
  width: 100%;
  max-width: 300px; /* Reduce max-width */
  color: rgba(0, 0, 0, 1);
  text-align: center;
  letter-spacing: -0.18px;
  padding: 10px 15px; /* Reduce padding */
  font: 700 30px/1 League Spartan, sans-serif; /* Reduce font size */
  border: none;
  cursor: pointer;
}

.description-textarea {
  width: 100%;
  max-width: 300px;
  height: 100px;
  margin: 17px 0 0;
  border-radius: 10px;
  padding: 10px;
  font: 400 16px/1 League Spartan, sans-serif;
  border: 1px solid #ccc;
  resize: none;
}

.validate-button {
  border-radius: 100px;
  background-color: rgba(179, 38, 30, 1);
  margin: 40px 0 0;
  min-height: 55px;
  width: 169px;
  color: var(--Yellow-2, #f3e9b5);
  text-align: center;
  letter-spacing: -0.14px;
  padding: 9px 12px;
  font: 700 27px/1 League Spartan, sans-serif;
  border: none;
  cursor: pointer;
}

.map-selector {
  height: 300px;
  max-width: 300px;
  margin: 17px 0 0;
  border-radius: 10px;
}

.modal-dialog {
  border: none;
  border-radius: 10px;
  padding: 0;
  background: transparent;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background: white;
  padding: 20px;
  border-radius: 10px;
  width: 90vw; /* Adjust width to make it responsive */
  max-width: 800px; /* Set a maximum width */
  height: 80vh; /* Adjust height to make it responsive */
  max-height: 600px; /* Set a maximum height */
}

.open-map-button, .close-map-button {
  background-color: rgba(179, 38, 30, 1);
  color: var(--Yellow-2, #f3e9b5);
  border: none;
  border-radius: 10px;
  padding: 10px 20px;
  cursor: pointer;
  margin-top: 10px;
}

.range-slider {
  -webkit-appearance: none;
  width: 100%;
  max-width: 300px;
  height: 8px;
  background: #ddd;
  outline: none;
  opacity: 0.7;
  transition: opacity 0.2s;
  margin: 10px 0;
}

.range-slider:hover {
  opacity: 1;
}

.range-slider::-webkit-slider-thumb {
  -webkit-appearance: none;
  appearance: none;
  width: 25px;
  height: 25px;
  background: #b3261e;
  cursor: pointer;
  border-radius: 50%;
}

.range-slider::-moz-range-thumb {
  width: 25px;
  height: 25px;
  background: #b3261e;
  cursor: pointer;
  border-radius: 50%;
}
</style>
