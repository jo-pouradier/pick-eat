<script setup lang="ts">
import {onMounted, ref} from 'vue';
import {useRouter} from "vue-router";
import {joinEvent} from "@/lib/EventUtils.ts";

const router = useRouter();
const uuid = ref<string | null>(null);
const error = ref<string>('');
onMounted(() => {
  const urlParams = new URLSearchParams(window.location.search);
  const uuidParam = urlParams.get('eventId');
  if (uuidParam) {
    uuid.value = uuidParam;
    handleValidation();
  }
});

function handleValidation(): void {
  if (!uuid.value) {
    error.value = 'Please enter a code.';
    return;
  }
  joinEvent(uuid.value).then(response => {
    console.log('Join event response:', response);
    if (response.data) {
      router.push(`/event-page?eventId=${uuid.value}`);
    }
  }).catch(() => {
    error.value = 'Something went wrong.\n Maybe you are already part of this event?';
  });
}

function handleJoin(): void {

}
</script>

<template>
  <div class="glass-container glass-card">
    <h1 class="main-title">Join a vote!</h1>
    <p>Code</p>
    <input class="name-selector" tabindex="0" role="textbox"
           placeholder="Code" v-model="uuid"/>
    <div v-if="error" class="error-message">
      <span class="error-text">{{ error }}</span>
    </div>
    <hr/>
    <button class="validate-button" @click="handleValidation">
      Valider
    </button>
  </div>
</template>
<style scoped>

.error-message {
  background-color: #fce4e4;
  border: 1px solid #fcc2c3;
  float: left;
  padding: 20px 30px;
}

.error-text {
  color: #cc0033;
  font-family: Helvetica, Arial, sans-serif;
  font-size: 13px;
  font-weight: bold;
  line-height: 20px;
  text-shadow: 1px 1px rgba(250, 250, 250, .3);
}
</style>
