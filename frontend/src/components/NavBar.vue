<template>
  <div class="navigation">
    <nav class="navigation-bar">
      <div class="toggle-wrap" :class="{ active: isActive }" @click="isActive = !isActive">
        <span class="toggle-bar"></span>
      </div>
      <img class="logo-image" v-if="!isHomeRoute" src="@/assets/eating.png" alt="Home page background image"
           @click="handleHome"/>
      <img loading="lazy" src="@/assets/profile.svg" class="nav-icon" alt="User profile icon" @click="handleAccount"/>
    </nav>
    <div class="navigation-menu" :class="{ active: isActive }" role="navigation" aria-label="Main navigation">
      <div class="navigation-container">
        <ul class="navigation-list">
          <li class="navigation-item">
            <img loading="lazy"
                 src="https://i.pinimg.com/originals/db/c8/ca/dbc8ca135fe568c03bd135dc5c134453.png"
                 class="navigation-icon" alt=""/>
            <span class="navigation-text" tabindex="0" @click="handleHome">Home</span>
          </li>
          <li class="navigation-item">
            <img loading="lazy"
                 src="https://cdn.builder.io/api/v1/image/assets/TEMP/348ede8cbe1cf700e3a3bf5d12b1ffcc5013d1f113df32a933aa64014d9bed6d?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d"
                 class="navigation-icon" alt=""/>
            <span class="navigation-text" tabindex="0" @click="handleEventList">My events</span>
          </li>
          <li class="navigation-item">
            <img loading="lazy"
                 src="https://cdn.builder.io/api/v1/image/assets/TEMP/261baa17493c3abed92d6b4720030c9ed9b4e40629e7f5c816a18454e3db80e6?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d"
                 class="navigation-icon" alt=""/>
            <span class="navigation-text" tabindex="0" @click="handleLogout">Log out</span>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">

import {computed, defineEmits, onMounted, ref, watch} from 'vue';
import {useRoute, useRouter} from 'vue-router';
import {socket} from "@/socket.ts";
import axios from "axios";

const emit = defineEmits(['update:isActive'])
const isActive = ref<boolean>(false)
const router = useRouter()

const route = useRoute();
const isHomeRoute = computed(() => route.path === '/');
onMounted(() => {
  if (!socket.connected) {
    socket.connect();
  }
});
watch(isActive, (newValue) => {
  console.log(isActive)
  emit('update:isActive', newValue)
})

function handleAccount(): void {
  // if log in to account settings .... for now :
  console.log('Account clicked')
  const userCookie = document.cookie.split('; ').find(row => row.startsWith('user='));

  if (userCookie) {
    const user = JSON.parse(decodeURIComponent(userCookie.split('=')[1]));
    console.debug('User:', user);
    router.push('/profile')
  } else
    router.push('/login')

}

function handleHome(): void {
  // if log in to account settings .... for now :
  console.log('Home clicked')
  router.push('/')
}

function handleLogout() {
  axios.get('/auth/logout').then(() => {
    console.log('Logged out');
    router.push('/login');
  });
  isActive.value = !isActive.value
}

function handleEventList() {
  router.push('/event-list')
  isActive.value = !isActive.value
}
</script>


<style scoped>
.navigation {
  height: var(--navbar-height);
  max-height: var(--navbar-height);
}

.navigation-bar {
  height: var(--navbar-height);
  max-height: var(--navbar-height);
  background-color: var(--black, #f5bd27);
  align-self: center;
  align-items: center;
  display: flex;
  width: 100%;
  gap: 20px;
  overflow: hidden;
  justify-content: space-between;
  margin-top: 0px;
  padding: 3vh 0px;
  z-index: 999;
  position: fixed;
}

.nav-icon {
  object-fit: contain;
  object-position: left;
  width: 48px;
}

.logo-image {
  object-fit: contain;
  object-position: center;
  width: 212px;
  border-radius: 23px;
  margin-top: 126px;
  max-width: 100%;
  max-height: 45px;
}


/* Animation for toggle button */
.toggle-wrap {
  padding: 10px;
  position: relative;
  cursor: pointer;
  float: left;
}

.toggle-bar,
.toggle-bar::before,
.toggle-bar::after,
.toggle-wrap.active .toggle-bar,
.toggle-wrap.active .toggle-bar::before,
.toggle-wrap.active .toggle-bar::after {
  transition: all 0.2s ease-in-out;
}

.toggle-bar {
  width: 38px;
  margin: 10px 0;
  position: relative;
  border-top: 6px solid #a55d0a;
  display: block;
  border-radius: 10px;
}

.toggle-bar::before,
.toggle-bar::after {
  content: '';
  display: block;
  background: #ffffff;
  height: 6px;
  width: 38px;
  position: absolute;
  top: -16px;
  transform-origin: 13%;
}

.toggle-bar::after {
  top: 4px;
}

.toggle-wrap.active .toggle-bar {
  border-top: 6px solid transparent;
}

.toggle-wrap.active .toggle-bar::before {
  transform: rotate(45deg);
}

.toggle-wrap.active .toggle-bar::after {
  transform: rotate(-45deg);
}

/* End of animation for toggle button */
.navigation-menu {
  position: absolute;
  width: fit-content;
  height: 100%;
  left: -310px;
  transition: all 0.4s ease;
  margin-bottom: 0;
  color: var(--neutral-grey, #fff);
  font: 500 23px League Spartan, sans-serif;

}

.navigation-menu.active {
  transform: translateX(310px);
  z-index: 1;
}

.navigation-container {
  border-radius: 0;
  background-color: #f5bd27;
  box-shadow: 0 4px 4px rgba(0, 0, 0, 0.25);
  width: 100%;
  fill: var(--accent-orange, #f5bd27);
  padding: 41px 14px 2px;
  height: 100%;
}

.navigation-list {
  list-style: none;
  margin: 0;
  padding-top: 10vh;
}

.logo-image {
  object-fit: contain;
  object-position: center;
  width: 120px;
  /* Adjust width to fit within the nav bar */
  height: auto;
  /* Maintain aspect ratio */
  border-radius: 23px;
  margin: 0;
  /* Remove top margin */
  max-width: 100%;
}

.navigation-item {
  display: flex;
  align-items: center;
  gap: 13px;
  margin-bottom: 33px;
  cursor: pointer;
}

.navigation-item:last-child {
  margin-bottom: 0;
}

.navigation-icon {
  width: 37px;
  height: 37px;
  object-fit: contain;
  object-position: center;
}


.navigation-text {
  white-space: nowrap;
  outline: none;
}

.navigation-text:focus {
  text-decoration: underline;
}
</style>
