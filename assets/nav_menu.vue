<template>
  <nav class="navigation-menu" role="navigation" aria-label="Main navigation">
    <div class="navigation-container">
      <h1 class="navigation-title">Menu</h1>
      <ul class="navigation-list">
        <li class="navigation-item">
          <img
            loading="lazy"
            src="https://cdn.builder.io/api/v1/image/assets/TEMP/348ede8cbe1cf700e3a3bf5d12b1ffcc5013d1f113df32a933aa64014d9bed6d?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d"
            class="navigation-icon"
            alt=""
          />
          <span class="navigation-text" tabindex="0">Notifications</span>
        </li>
        <li class="navigation-item">
          <img
            loading="lazy"
            src="https://cdn.builder.io/api/v1/image/assets/TEMP/a93ca3330a0bda50ffa03d40eddcd160441c00ba240a52f8b12c26ee1d76bda4?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d"
            class="navigation-icon"
            alt=""
          />
          <span class="navigation-text" tabindex="0">Facebook</span>
        </li>
        <li class="navigation-item">
          <img
            loading="lazy"
            src="https://cdn.builder.io/api/v1/image/assets/TEMP/f6d4af25fa104fa8fff00d0f30cdb98eaac9e758406502bd56ff266611b2d5c4?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d"
            class="navigation-icon"
            alt=""
          />
          <span class="navigation-text" tabindex="0">Instagram</span>
        </li>
        <li class="navigation-item">
          <img
            loading="lazy"
            src="https://cdn.builder.io/api/v1/image/assets/TEMP/3ddd68d0fa0e74c10fb2fa1bf2381a30264ae2d3899370e8db96829208719c81?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d"
            class="navigation-icon"
            alt=""
          />
          <span class="navigation-text" tabindex="0">Gitlab</span>
        </li>
        <li class="navigation-item">
          <img
            loading="lazy"
            src="https://cdn.builder.io/api/v1/image/assets/TEMP/85ebcf158d4489ad2cad14f6dc2ad0b35770325b6ee5fef8e20acfc8b9ff9656?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d"
            class="navigation-icon"
            alt=""
          />
          <span class="navigation-text" tabindex="0">Help Center</span>
        </li>
        <li class="navigation-item">
          <img
            loading="lazy"
            src="https://cdn.builder.io/api/v1/image/assets/TEMP/261baa17493c3abed92d6b4720030c9ed9b4e40629e7f5c816a18454e3db80e6?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d"
            class="navigation-icon"
            alt=""
          />
          <span class="navigation-text" tabindex="0">Log out</span>
        </li>
      </ul>
    </div>
  </nav>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

export default defineComponent({
  name: 'NavigationMenu'
})
</script>

<style scoped>
.navigation-menu {
  border-radius: 0 20px 20px 0;
  max-width: 249px;
  padding: 8px 0;
  color: var(--Grays-White, #fff);
  font: 500 23px League Spartan, sans-serif;
}

.navigation-container {
  border-radius: 0;
  background-color: rgba(233, 83, 34, 1);
  box-shadow: 0 4px 4px rgba(0, 0, 0, 0.25);
  width: 100%;
  fill: var(--Orange-Base, #e95322);
  padding: 71px 14px 200px;
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
  width: 57px;
  height: 57px;
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