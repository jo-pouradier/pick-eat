<template>
  <div class="event-creation-container">
    <div class="event-creation-wrapper">
      <img
        loading="lazy"
        src="https://cdn.builder.io/api/v1/image/assets/TEMP/ae68a0e69004b0aa5096378a272c958961b8e8da63a60b8cb86687bad3d7e9d4?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d"
        class="background-image"
        alt=""
      />
      <div class="navigation-icons">
        <img
          loading="lazy"
          src="https://cdn.builder.io/api/v1/image/assets/TEMP/dc15620de14f80430314f99353fbc0c39c7dc99e5334f34061d5eea7ed3642d4?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d"
          class="nav-icon"
          alt="Navigation icon"
        />
        <img
          loading="lazy"
          src="https://cdn.builder.io/api/v1/image/assets/TEMP/0f3b04805a56cdf18c8db50279aa6933cf92bc9c1eb0327e17456453ca136838?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d"
          class="nav-icon"
          alt="Navigation icon"
        />
      </div>
      <h1 class="main-title">Create a vote!</h1>
      <button 
        class="name-selector"
        tabindex="0"
        role="button"
        @click="handleNameSelection"
      >
        Select a name
      </button>
      <button 
        class="validate-button"
        @click="handleValidation"
      >
        Valider
      </button>
      <div 
        class="share-container"
        role="button"
        tabindex="0"
        @click="handleShare"
        @keydown.enter="handleShare"
      >
        <img
          loading="lazy"
          src="https://cdn.builder.io/api/v1/image/assets/TEMP/646ed9b6c7378c5e27c365f5b3d0ae5ba805bd7899a4a8e6c191ed2538be19c2?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d"
          class="share-icon"
          alt="Share icon"
        />
        <span class="share-text">Share link</span>
      </div>
      <div class="footer-navigation">
        <img
          loading="lazy"
          src="https://cdn.builder.io/api/v1/image/assets/TEMP/111e4218a44dbfd90cd401a47b84c81472b565ad2adc7bef9c2b3539af9e61c3?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d"
          class="footer-icon"
          alt="Footer navigation icon"
        />
        <img
          loading="lazy"
          src="https://cdn.builder.io/api/v1/image/assets/TEMP/c67604ced27306507697b936f0a15be87ad48dddd65a35d4019124bfcd784546?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d"
          class="footer-icon"
          alt="Footer navigation icon"
        />
        <img
          loading="lazy"
          src="https://cdn.builder.io/api/v1/image/assets/TEMP/5a1ce1325f5184a053e0a7a255ffe7aefbc904e0da338228eae653d343c67bd0?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d"
          class="footer-icon"
          alt="Footer navigation icon"
        />
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

export default defineComponent({
  name: 'EventCreation',
  methods: {
    handleNameSelection() {
      // Implementation for name selection
    },
    handleValidation() {
      // Implementation for validation
    },
    handleShare() {
      // Implementation for sharing
    }
  }
})
</script>

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

.background-image {
  position: absolute;
  inset: 0;
  height: 100%;
  width: 100%;
  object-fit: cover;
  object-position: center;
}

.navigation-icons {
  position: relative;
  display: flex;
  width: 100%;
  max-width: 341px;
  gap: 20px;
  justify-content: space-between;
}

.nav-icon {
  aspect-ratio: 1;
  object-fit: contain;
  object-position: center;
  width: 48px;
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
  margin: 171px 0 0;
  width: 100%;
  max-width: 358px;
  color: rgba(0, 0, 0, 1);
  text-align: center;
  letter-spacing: -0.18px;
  padding: 31px 68px;
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

.share-container {
  position: relative;
  border-radius: 20px;
  background-color: rgba(248, 248, 248, 1);
  display: flex;
  margin: 123px 0 0;
  width: 100%;
  max-width: 281px;
  gap: 7px;
  padding: 11px 29px;
  cursor: pointer;
}

.share-icon {
  aspect-ratio: 1.19;
  object-fit: contain;
  object-position: center;
  width: 38px;
  align-self: start;
}

.share-text {
  flex-grow: 1;
  width: 170px;
  color: rgba(0, 0, 0, 1);
  text-align: center;
  letter-spacing: -0.21px;
  font: 400 41px/1 Lobster, sans-serif;
}

.footer-navigation {
  position: relative;
  border-radius: 0 0 30px 30px;
  background-color: rgba(9, 9, 9, 1);
  align-self: stretch;
  display: flex;
  margin: 75px 0 0;
  width: 100%;
  align-items: start;
  gap: 20px;
  justify-content: space-between;
  padding: 17px 21px;
}

.footer-icon {
  aspect-ratio: 1;
  object-fit: contain;
  object-position: center;
  width: 48px;
}

.footer-icon:nth-child(2) {
  aspect-ratio: 0.91;
  width: 40px;
  align-self: center;
}
</style>