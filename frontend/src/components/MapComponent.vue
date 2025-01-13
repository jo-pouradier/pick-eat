<template>
    <div id="map" class="map-container"></div>
</template>

<script setup lang="ts">
import { onMounted, defineEmits,defineProps, watch, ref } from 'vue';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';

const emits = defineEmits(['locationSelected']);
const props = defineProps<{ selectedRange: number }>();
const DefaultRange = ref(50);
onMounted(() => {
    const map = L.map('map').setView([45.7484600, 4.8467100], 13); // Set base map view to specified location

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    }).addTo(map);

    let marker: L.Marker | null = null;
    let circle: L.Circle | null = null;

    map.on('click', (e: L.LeafletMouseEvent) => {
        const { lat, lng } = e.latlng;
        if (marker) {
            marker.setLatLng(e.latlng);
        } else {
            marker = L.marker(e.latlng).addTo(map);
        }
        if (circle) {
            circle.setLatLng(e.latlng);
        } else {
            circle = L.circle(e.latlng, { radius: DefaultRange.value }).addTo(map);
        }
        circle.setRadius(DefaultRange.value);
        console.log('Selected location:', lat, lng);
        console.log('Selected range:', DefaultRange.value);
        emits('locationSelected', [lat, lng]);
    });

    watch(() => props.selectedRange, (newRadius) => {
        console.log('Selected range: circle', newRadius);
        if (circle) {
            circle.setRadius(newRadius);
            DefaultRange.value = newRadius;
        }
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
