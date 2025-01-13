<template>
    <main class="landing-container">
      <div class="landing-content">
        <img
          loading="lazy"
          src="https://cdn.builder.io/api/v1/image/assets/TEMP/91973f3b5ad2879af6b2e898f171b1106b62b3104c13dc9a255f59049d33dc70?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d"
          class="background-image"
          alt=""
        />
        <nav class="navigation-bar">
          <img
            loading="lazy"
            src="https://cdn.builder.io/api/v1/image/assets/TEMP/f03d532287467b8e0948752ca756e28b03a1a2363bb4b6db3c5c1920e80ed949?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d"
            class="nav-icon"
            alt="Navigation menu icon"
          />
          <img
            loading="lazy"
            src="https://cdn.builder.io/api/v1/image/assets/TEMP/deecbaecfe24e355c6771029bbac40bf1d8e90971525e5fdcd52e5e207642f9c?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d"
            class="nav-icon"
            alt="User profile icon"
          />
        </nav>
        <img
          loading="lazy"
          src="https://cdn.builder.io/api/v1/image/assets/TEMP/372c911841cbd01baa60c0777c1812eebdc68864be2a41430aff73861060bf93?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d"
          class="logo-image"
          alt="Company logo"
        />
        <button 
          class="action-button action-button-primary"
          @click="handleNewVote"
          tabindex="0"
        >
          New Vote
        </button>
        <button 
          class="action-button action-button-secondary"
          @click="handleJoin"
          tabindex="0"
        >
          Join
        </button>
      </div>
    </main>
  </template>
  
  <script lang="ts">
  import { defineComponent } from 'vue'
  
  export default defineComponent({
    name: 'LandingPage',
    methods: {
      handleNewVote() {
        // Handle new vote action
      },
      handleJoin() {
        // Handle join action
      }
    }
  })
  </script>
  
  <style scoped>
  .landing-container {
    display: flex;
    max-width: 480px;
    width: 100%;
    flex-direction: column;
    margin: 0 auto;
  }
  
  .landing-content {
    display: flex;
    flex-direction: column;
    border-radius: 30px;
    position: relative;
    aspect-ratio: 0.461;
    width: 100%;
    padding-bottom: 170px;
    align-items: center;
  }
  
  .background-image {
    position: absolute;
    inset: 0;
    height: 100%;
    width: 100%;
    object-fit: cover;
    object-position: center;
  }
  
  .navigation-bar {
    position: relative;
    border-radius: 30px 30px 0 0;
    background-color: rgba(9, 9, 9, 1);
    align-self: stretch;
    display: flex;
    width: 100%;
    gap: 20px;
    overflow: hidden;
    justify-content: space-between;
    padding: 20px 31px;
  }
  
  .nav-icon {
    aspect-ratio: 1;
    object-fit: contain;
    object-position: center;
    width: 48px;
  }
  
  .logo-image {
    aspect-ratio: 1.37;
    object-fit: contain;
    object-position: center;
    width: 212px;
    border-radius: 23px;
    margin-top: 126px;
    max-width: 100%;
  }
  
  .action-button {
    position: relative;
    align-self: stretch;
    border-radius: 100px;
    min-height: 66px;
    width: 169px;
    max-width: 100%;
    text-align: center;
    letter-spacing: -0.14px;
    padding: 23px 12px;
    font: 700 27px/1 "League Spartan", sans-serif;
    border: none;
    cursor: pointer;
  }
  
  .action-button:focus {
    outline: 2px solid #fff;
    outline-offset: 2px;
  }
  
  .action-button-primary {
    background-color: var(--Orange-2, #ffdecf);
    color: var(--Orange-Base, #e95322);
    margin-top: 133px;
  }
  
  .action-button-secondary {
    background-color: rgba(179, 38, 30, 1);
    color: var(--Yellow-2, #f3e9b5);
    margin: 48px 0 -34px;
  }
  
  .visually-hidden {
    position: absolute;
    width: 1px;
    height: 1px;
    padding: 0;
    margin: -1px;
    overflow: hidden;
    clip: rect(0, 0, 0, 0);
    white-space: nowrap;
    border: 0;
  }
  </style>