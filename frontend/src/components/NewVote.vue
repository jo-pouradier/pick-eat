<template>
    <div class="event-creation-container">
        <div class="event-creation-wrapper">
            <h1 class="main-title">Create a vote!</h1>
            <p class="input-label">Vote name :</p>
            <input class="name-selector" tabindex="0" role="textbox" 
            placeholder="Select a name"
            v-model="eventName" />
            <p>Select a place :</p>
            <button class="open-map-button" @click="openMapModal">Open Map</button>
            <dialog v-if="showMapModal" class="modal-dialog" @close="showMapModal = false">
                <div class="modal-content">
                    <MapComponent @locationSelected="handleLocationSelected" />
                    <button class="close-map-button" @click="closeMapModal">Close Map</button>
                </div>
            </dialog>
            <button class="validate-button" @click="handleValidation">
                Valider
            </button>
        </div>
    </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router';
import { ref, nextTick } from 'vue';
import MapComponent from './MapComponent.vue'; // Import the new MapComponent

const router = useRouter();
const showMapModal = ref(false);
const selectedCoords = ref<[number, number] | null>(null);
const eventName = ref('');


function handleValidation(): void {
    console.log(eventName.value);
    if (selectedCoords.value && eventName.value !== '') {
        console.log('Selected coordinates:', selectedCoords.value);
        router.push('/event-page');
    } else {
        if (eventName.value === '') {
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
}

function handleLocationSelected(coords: [number, number]): void {
    selectedCoords.value = coords;
}

</script>

<style scoped>
.event-creation-container {
    display: flex;
    max-width: 480px;
    width: 100%;
    flex-direction: column;
    margin: 0 auto;
}

.event-creation-wrapper {
    display: flex;
    flex-direction: column;
    border-radius: 30px;
    position: relative;
    width: 100%;
    padding: 26px 0 0;
    align-items: center;
}


.main-title {
    position: relative;
    color: rgba(255, 255, 255, 1);
    letter-spacing: -0.28px;
    text-align: center;
    margin: 32px 0 0;
    font: 400 55px/1 Lobster, sans-serif;
}

.name-selector {
    position: relative;
    border-radius: 44px;
    background: var(--Yellow-2, #f3e9b5);
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
</style>