<template>
    <div id="map" class="map-container"></div>
</template>

<script setup lang="ts">
import { onMounted, defineEmits, ref,watch } from 'vue';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';

const emits = defineEmits(['locationSelected']);
const props = defineProps<{ selectedRange: number, selectedCoords: [number, number] | null }>();
const DefaultRange = ref<number>(
  props.selectedRange && props.selectedRange > 0
    ? props.selectedRange
    : 50
)
const SelectedCoords = ref<[number, number]>(
  props.selectedCoords && (props.selectedCoords[0] !== 0 || props.selectedCoords[1] !== 0)
    ? props.selectedCoords
    : [45.7484600, 4.8467100]
);// let marker: L.Marker | null = null;
// let circle: L.Circle | null = null;
onMounted(() => {
  console.log('SelectedCoords:', SelectedCoords.value);
  let map = L.map('map').setView(SelectedCoords.value, 13); // Set base map view to specified location
  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
  }).addTo(map);

  let marker: L.Marker | null = null;
  let circle: L.Circle | null = null;

  const updateMap = (coords: [number, number]) => {
    if (coords && (coords[0] !== 0 || coords[1] !== 0)) {
      if (marker) {
        marker.setLatLng(coords);
      } else {
        marker = L.marker(coords).addTo(map);
      }
      if (circle) {
        circle.setLatLng(coords);
      } else {
        circle = L.circle(coords, { radius: DefaultRange.value }).addTo(map);
      }
      console.log('Updating map to:', coords);
      map.setView(coords, 13);
    }
  };
  updateMap(SelectedCoords.value);



  map.on('click', (e: L.LeafletMouseEvent) => {
    const { lat, lng } = e.latlng;
    updateMap([lat, lng]);
    SelectedCoords.value = [lat, lng];
    emits('locationSelected', [lat, lng]);
  });

  watch(() => props.selectedCoords, (newCoords: [number, number] | null) => {
    if (newCoords) {
      updateMap(newCoords);
      SelectedCoords.value = newCoords;
    }
  });

  watch(() => props.selectedRange, (newRadius: number) => {
    if (circle) {
      circle.setRadius(newRadius);
      DefaultRange.value = newRadius;
    }
  });
  setTimeout(() => {
    map.invalidateSize(); // Ensure the map is properly resized
  }, 0);
});
</script>

<style scoped>
.map-container {
    height: 500px; /* Increase height */
    width: 100%;
    border-radius: 10px;
}
</style>
