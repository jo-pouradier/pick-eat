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
                 src="https://cdn.builder.io/api/v1/image/assets/TEMP/348ede8cbe1cf700e3a3bf5d12b1ffcc5013d1f113df32a933aa64014d9bed6d?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d"
                 class="navigation-icon" alt=""/>
            <span class="navigation-text" tabindex="0">Notifications</span>
          </li>
          <li class="navigation-item">
            <img loading="lazy"
                 src="https://cdn.builder.io/api/v1/image/assets/TEMP/a93ca3330a0bda50ffa03d40eddcd160441c00ba240a52f8b12c26ee1d76bda4?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d"
                 class="navigation-icon" alt=""/>
            <span class="navigation-text" tabindex="0">Facebook</span>
          </li>
          <li class="navigation-item">
            <img loading="lazy"
                 src="https://cdn.builder.io/api/v1/image/assets/TEMP/f6d4af25fa104fa8fff00d0f30cdb98eaac9e758406502bd56ff266611b2d5c4?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d"
                 class="navigation-icon" alt=""/>
            <span class="navigation-text" tabindex="0">Instagram</span>
          </li>
          <li class="navigation-item">
            <img loading="lazy"
                 src="https://cdn.builder.io/api/v1/image/assets/TEMP/3ddd68d0fa0e74c10fb2fa1bf2381a30264ae2d3899370e8db96829208719c81?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d"
                 class="navigation-icon" alt=""/>
            <span class="navigation-text" tabindex="0">Gitlab</span>
          </li>
          <li class="navigation-item">
            <img loading="lazy"
                 src="https://cdn.builder.io/api/v1/image/assets/TEMP/85ebcf158d4489ad2cad14f6dc2ad0b35770325b6ee5fef8e20acfc8b9ff9656?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d"
                 class="navigation-icon" alt=""/>
            <span class="navigation-text" tabindex="0">Help Center</span>
          </li>
          <li class="navigation-item">
            <img loading="lazy"
                 src="https://cdn.builder.io/api/v1/image/assets/TEMP/261baa17493c3abed92d6b4720030c9ed9b4e40629e7f5c816a18454e3db80e6?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d"
                 class="navigation-icon" alt=""/>
            <span class="navigation-text" tabindex="0">Log out</span>
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

</script>


<style scoped>
.navigation-bar {
  /* border-radius: 30px 30px 0 0; */
  background-color: rgba(9, 9, 9, 1);
  align-self: stretch;
  display: flex;
  width: 100%;
  gap: 20px;
  overflow: hidden;
  justify-content: space-between;
  padding: 20px 31px;
  z-index: 999;
  position: fixed;
  max-height:15vh;
}

.nav-icon {
  object-fit: contain;
  object-position: center;
  width: 48px;
}

.logo-image {
  object-fit: contain;
  object-position: center;
  width: 212px;
  border-radius: 23px;
  margin-top: 126px;
  max-width: 100%;
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
  height: 100%;
  color: var(--Grays-White, #fff);
  font: 500 23px League Spartan, sans-serif;

}

.navigation-menu.active {
  transform: translateX(310px);
  z-index: 1;
}

.navigation-container {
  border-radius: 0;
  background-color: rgba(233, 83, 34, 1);
  box-shadow: 0 4px 4px rgba(0, 0, 0, 0.25);
  width: 100%;
  fill: var(--Orange-Base, #e95322);
  padding: 41px 14px 2px;
  height: 100%;
}

.navigation-title {
  text-align: center;
  font-size: 40px;
  font-weight: 700;
  line-height: 1;
  text-transform: capitalize;
  margin: 0 0 49px;
}

.navigation-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.logo-image {
  aspect-ratio: 1.97;
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
