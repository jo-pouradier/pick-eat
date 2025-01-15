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
    error.value = 'Please enter a vote code.';
    return;
  }
  joinEvent(uuid.value).then(response => {
    console.log('Join event response:', response);
    if (response.data) {
      router.push(`/event-list`);
    }
  }).catch(() => {
    error.value = 'Something went wrong.\n Maybe you are already part of this event?';
  });
}

function handleJoin(): void {

}
</script>

<template>
  <div class="event-creation-container">
    <div class="event-creation-wrapper">
      <h1 class="main-title">Join a vote!</h1>
      <p>Vote code :</p>
      <input class="name-selector" tabindex="0" role="textbox"
             placeholder="Vote code" v-model="uuid"/>
      <button class="validate-button" @click="handleValidation">
        Valider
      </button>
      <div v-if="error" class="error-message">
        <span class="error-text">{{ error }}</span>
      </div>
    </div>
  </div>
</template>
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
  aspect-ratio: 0.461;
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
  max-width: 358px;
  color: rgba(0, 0, 0, 1);
  text-align: center;
  letter-spacing: -0.18px;
  padding: 15px 20px;
  font: 700 37px/1 League Spartan, sans-serif;
  border: none;
  cursor: pointer;
}

.validate-button {
  position: relative;
  border-radius: 100px;
  background-color: rgba(179, 38, 30, 1);
  margin: 40px 0 0;
  min-height: 66px;
  width: 169px;
  color: var(--Yellow-2, #f3e9b5);
  text-align: center;
  letter-spacing: -0.14px;
  padding: 23px 12px;
  font: 700 27px/1 League Spartan, sans-serif;
  border: none;
  cursor: pointer;
}

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
