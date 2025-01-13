<template>
    <div class="event-list-container">
        <div class="event-list-wrapper">
            <h1 class="main-title">Event List</h1>
            <div v-for="event in events" :key="event.id" class="event-item">
                <div class="event-header" @click="toggleEventDetails(event.id)">
                    <p class="event-name">{{ event.name }}</p>
                    <p class="event-place">{{ event.place }}</p>
                </div>
                <transition name="fade">
                    <div v-if="event.showDetails" class="event-details">
                        <p>Participants: {{ event.participants.join(', ') }}</p>
                        <p>More Info: {{ event.moreInfo }}</p>
                        <button class="event-button" @click="goToEventPage(event.id)">View Event</button>
                    </div>
                </transition>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const events = ref([
    { id: 1, name: 'Event 1', place: 'Place 1', participants: ['Alice', 'Bob'], moreInfo: 'Details about Event 1', showDetails: false },
    { id: 2, name: 'Event 2', place: 'Place 2', participants: ['Charlie', 'Dave'], moreInfo: 'Details about Event 2', showDetails: false },
    { id:3, name: "Event 3", place: "Place 3", participants: ["Eve", "Frank"], moreInfo: "Details about Event 3", showDetails: false }
]);

function toggleEventDetails(eventId: number): void {
    const event = events.value.find(e => e.id === eventId);
    if (event) {
        event.showDetails = !event.showDetails;
    }
}

function goToEventPage(eventId: number): void {
    router.push({ name: 'event-page', params: { id: eventId } });
}
</script>

<style scoped>
.event-list-container {
    display: flex;
    max-width: 480px;
    width: 100%;
    flex-direction: column;
    margin: 0 auto;
}

.event-list-wrapper {
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

.event-item {
    width: 100%;
    max-width: 300px;
    margin: 10px 0;
    background: var(--Yellow-2, #f3e9b5);
    border-radius: 10px;
    cursor: pointer;
}

.event-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px 15px;
}

.event-name, .event-place {
    font: 700 20px/1 League Spartan, sans-serif;
    color: rgba(0, 0, 0, 1);
}

.event-details {
    margin-top: 10px;
    font: 400 16px/1 League Spartan, sans-serif;
    color: rgba(0, 0, 0, 0.8);
    background-color: #c9c095;
    padding: 10px 15px;
    border-radius: 10px;
    transition: all 0.3s ease;
}

.event-button {
    margin-top: 10px;
    padding: 10px 15px;
    background-color: rgba(179, 38, 30, 1);
    color: var(--Yellow-2, #f3e9b5);
    border: none;
    border-radius: 10px;
    cursor: pointer;
    font: 700 16px/1 League Spartan, sans-serif;
}

.fade-enter-active, .fade-leave-active {
    transition: opacity 0.3s ease;
}

.fade-enter, .fade-leave-to {
    opacity: 0;
}
</style>
