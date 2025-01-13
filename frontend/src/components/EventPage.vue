<template>
    <div class="event-page-container">
        <h1 class="main-title">{{ eventData.name }}</h1>
        <p class="event-place">{{ eventData.place }}</p>
        <p class="event-participants">Participants: {{ eventData.participants.join(', ') }}</p>
        <p class="event-more-info">More Info: {{ eventData.moreInfo }}</p>
        <button class="back-button" @click="goBack">Back to Event List</button>
        <div class="logs-container">
            <div v-for="log in logs" :key="log.id" class="log-item">
                <p class="log-time">{{ log.time }}</p>
                <p class="log-message">{{ log.message }}</p>
                <img v-if="log.image" :src="log.image" alt="Log image" class="log-image" />
            </div>
        </div>
        <div class="message-input-container">
            <input v-model="newMessage" class="message-input" placeholder="Write a message..." @keyup.enter="sendMessage" />
            <label class="image-upload-label send-button">
                📷
               <input type="file" @change="handleImageUpload" class="image-upload-input" />
            </label>
            <button class="send-button" @click="sendMessage">Send</button>
            <div v-if="imageUploaded" class="image-uploaded-marker">Image Ready to Send!</div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const route = useRoute();
const router = useRouter();

interface EventInfo {
    id: number;
    name: string;
    place: string;
    participants: string[];
    moreInfo: string;
}

interface Log {
    id: number;
    time: string;
    message: string;
    image?: string; // Optional image property
}

const eventData = ref<EventInfo>({
    id: 0,
    name: '',
    place: '',
    participants: [],
    moreInfo: ''
});

const logs = ref<Log[]>([
    { id: 1, time: '10:00 AM', message: 'Event created' },
    { id: 2, time: '10:30 AM', message: 'Participants joined' },
    { id: 3, time: '11:00 AM', message: 'Event started' },
    // { id: 4, time: '12:00 PM', message: 'Event ended', image: 'path/to/image.jpg' },
    // { id: 1, time: '10:00 AM', message: 'Event created' },
    // { id: 2, time: '10:30 AM', message: 'Participants joined' },
    // { id: 3, time: '11:00 AM', message: 'Event started' },
    // { id: 4, time: '12:00 PM', message: 'Event ended', image: 'path/to/image.jpg' },
    // { id: 1, time: '10:00 AM', message: 'Event created' },
    // { id: 2, time: '10:30 AM', message: 'Participants joined' },
    // { id: 3, time: '11:00 AM', message: 'Event started' },
]);

const newMessage = ref('');
const newImage = ref<string | null>(null);
const imageUploaded = ref(false);

onMounted(() => {
    const eventId = Number(route.params.id);
    // Fetch event details based on eventId
    // This is a placeholder, replace with actual data fetching logic
    const fetchedEvent: EventInfo = {
        id: eventId || 0,
        name: `Event ${eventId}`,
        place: `Place ${eventId}`,
        participants: ['Alice', 'Bob'],
        moreInfo: `Details about Event ${eventId}`
    };
    eventData.value = fetchedEvent;
});

function goBack(): void {
    router.push({ name: 'event-list' });
}

function sendMessage(): void {
    if (newMessage.value.trim() || newImage.value) {
        logs.value.push({
            id: logs.value.length + 1,
            time: new Date().toLocaleTimeString(),
            message: newMessage.value,
            image: newImage.value || undefined
        });
        newMessage.value = '';
        newImage.value = null;
        imageUploaded.value = false;
    }
}

function handleImageUpload(event: Event): void {
    const target = event.target as HTMLInputElement;
    if (target.files && target.files[0]) {
        const reader = new FileReader();
        reader.onload = (e) => {
            newImage.value = e.target?.result as string;
            imageUploaded.value = true;
        };
        reader.readAsDataURL(target.files[0]);
    }
}
</script>

<style scoped>
.event-page-container {
    display: flex;
    flex-direction: column;
    max-width: 480px;
    width: 100%;
    margin: 0 auto;
    align-items: center;
    background-color: #f3e9b5;
    border-radius: 10px;
    padding: 15px;
    margin-top: calc(5vh + 60px); /* Adjusted for nav bar height */
    height: calc(100vh - 9vh - 60px); /* Full height minus nav bar and margin */
}

.event-card {
    display: flex;
    flex-direction: column;
    border-radius: 20px;
    background: var(--Yellow-2, #f0e196);
    padding: 10px;
    width: 100%;
    max-width: 300px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    align-items: center;
    margin-bottom: 10px;
}

.main-title {
    color: rgb(0, 0, 0);
    letter-spacing: -0.28px;
    text-align: center;
    margin: 5px 0;
    font: 400 24px/1 Lobster, sans-serif;
}

.event-place, .event-participants, .event-more-info {
    font: 400 14px/1 League Spartan, sans-serif;
    color: rgba(0, 0, 0, 1);
    margin: 3px 0;
    text-align: center;
}

.logs-container {
    display: flex;
    flex-direction: column;
    width: 100%;
    padding: 10px;
    overflow-y: auto;
    flex: 1; /* Take available space */
}

.log-item {
    display: flex;
    flex-direction: column;
    margin-bottom: 10px;
    width: 100%;
    background: #f3f3f3;
    border-radius: 10px;
    padding: 10px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    height: fit-content;
}

.log-time {
    font: 400 12px/1 League Spartan, sans-serif;
    color: rgba(0, 0, 0, 0.6);
}

.log-message {
    font: 400 14px/1 League Spartan, sans-serif;
    color: rgba(0, 0, 0, 1);
}

.log-image {
    max-width: 100%;
    border-radius: 10px;
    margin-top: 5px;
}

.message-input-container {
    display: flex;
    width: 100%;
    max-width: 400px;
    margin-top: 5px;
    padding: 5px;
    position: sticky;
    bottom: 0;
    background-color: #f3e9b5; /* Match the background color */
    position: relative; /* Add this to position the marker */
}

.message-input {
    flex: 1;
    padding: 10px;
    border-radius: 10px;
    border: 1px solid #ccc;
    font: 400 14px/1 League Spartan, sans-serif;
}

.image-upload-label {
    display: flex;
    align-items: center;
    cursor: pointer;
    margin-left: 10px;
}

.image-upload-icon {
    width: 24px;
    height: 24px;
}

.image-upload-input {
    display: none;
}

.send-button {
    padding: 10px 15px;
    background-color: rgba(179, 38, 30, 1);
    color: var(--Yellow-2, #f3e9b5);
    border: none;
    border-radius: 10px;
    cursor: pointer;
    font: 700 14px/1 League Spartan, sans-serif;
    margin-left: 10px;
}

.back-button {
    margin-top: 10px;
    margin-bottom: 5px;
    padding: 10px 15px;
    background-color: rgba(179, 38, 30, 1);
    color: var(--Yellow-2, #f3e9b5);
    border: none;
    border-radius: 10px;
    cursor: pointer;
    font: 700 16px/1 League Spartan, sans-serif;
}

.image-uploaded-marker {
    position: absolute;
    top: -30px;
    right: 10px;
    background-color: rgba(0, 128, 0, 0.8);
    color: white;
    padding: 5px 10px;
    border-radius: 5px;
    font: 700 14px/1 League Spartan, sans-serif;
}

.fade-enter-active, .fade-leave-active {
    transition: opacity 0.5s;
}
.fade-enter, .fade-leave-to /* .fade-leave-active in <2.1.8 */ {
    opacity: 0;
}
</style>
