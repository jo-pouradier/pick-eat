<template>
    <div id="map" class="map-container"></div>
</template>

<script setup lang="ts">
import { onMounted, defineEmits } from 'vue';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';

const emits = defineEmits(['locationSelected']);

onMounted(() => {
    const map = L.map('map').setView([51.505, -0.09], 13);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    }).addTo(map);

    let marker: L.Marker | null = null;

    map.on('click', (e: L.LeafletMouseEvent) => {
        const { lat, lng } = e.latlng;
        if (marker) {
            marker.setLatLng(e.latlng);
        } else {
            marker = L.marker(e.latlng).addTo(map);
        }
        console.log('Selected location:', lat, lng);
        emits('locationSelected', [lat, lng]);
    });
});
</script>

<style scoped>
.map-container {
    height: 500px; /* Increase height */
    width: 100%;
    border-radius: 10px;
}
</style>
