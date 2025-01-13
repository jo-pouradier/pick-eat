<template>
    <section class="menu-container">
      <article class="menu-card">
        <div class="pizza-gallery">
          <div class="pizza-showcase">
            <img
              loading="lazy"
              src="https://cdn.builder.io/api/v1/image/assets/TEMP/e4762f9fa0a006504c0d5c5ad77f3cf0fd27454d2c3512a3f6edaaf1e4647cef?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d"
              class="pizza-thumbnail"
              alt="Pizza variety showcase thumbnail"
            />
            <div class="pizza-display"></div>
            <img
              loading="lazy"
              src="https://cdn.builder.io/api/v1/image/assets/TEMP/6bb35bb0cada82ac439a3eb29df1c6c13a26edcb5f26238ccc798a6341f77bba?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d"
              class="pizza-thumbnail"
              alt="Pizza variety showcase thumbnail"
            />
          </div>
        </div>
        <div class="pizza-options">
          <img
            loading="lazy"
            src="https://cdn.builder.io/api/v1/image/assets/TEMP/fa708cecc73e8b32335f52d05da68f9984267ad789adb3657ccd4c965f3a1297?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d"
            class="option-thumbnail"
            alt="Pizza topping option"
          />
          <img
            loading="lazy"
            src="https://cdn.builder.io/api/v1/image/assets/TEMP/870b983e2b6bb8a36fe91e6a3fb775338c2a8881a64a9313daf1cbb06d20df68?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d"
            class="option-thumbnail"
            alt="Pizza topping option"
          />
          <img
            loading="lazy"
            src="https://cdn.builder.io/api/v1/image/assets/TEMP/a2e62b0d627c4de4e8f0a4e2a4b8e77e8383bf8c911dfab2c5b8fe3ac5d7d2d4?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d"
            class="option-thumbnail"
            alt="Pizza topping option"
          />
        </div>
        <h1 class="menu-title">Pizza de la Mama</h1>
        <div class="menu-divider">____________________<br />_______________</div>
      </article>
    </section>
  </template>
  
  <script lang="ts">
  import { defineComponent } from 'vue'
  
  export default defineComponent({
    name: 'PizzaMenu'
  })
  </script>
  
  <style scoped>
  .menu-container {
    display: flex;
    max-width: 441px;
    flex-direction: column;
    padding: 0 18px;
  }
  
  .menu-card {
    border-radius: 44px;
    background: var(--Yellow-2, #f3e9b5);
    display: flex;
    width: 100%;
    flex-direction: column;
    align-items: center;
    margin-right: 17px;
    padding: 36px 0 77px;
  }
  
  .pizza-gallery {
    align-self: start;
    display: flex;
    gap: 5px;
  }
  
  .pizza-showcase {
    display: flex;
    flex-grow: 1;
    flex-basis: auto;
  }
  
  .pizza-thumbnail {
    aspect-ratio: 1.04;
    object-fit: contain;
    object-position: center;
    width: 79px;
    margin: auto 0;
  }
  
  .pizza-display {
    border-radius: 30px;
    background-color: rgba(217, 217, 217, 1);
    width: 229px;
    height: 252px;
  }
  
  .pizza-options {
    display: flex;
    margin-top: 14px;
    width: 225px;
    max-width: 100%;
    gap: 20px;
    justify-content: space-between;
  }
  
  .option-thumbnail {
    aspect-ratio: 1;
    object-fit: contain;
    object-position: center;
    width: 48px;
  }
  
  .menu-title {
    color: rgba(0, 0, 0, 1);
    letter-spacing: -1.62px;
    text-align: center;
    margin-top: 31px;
    font: 700 36px/1 League Spartan, sans-serif;
  }
  
  .menu-divider {
    color: rgba(0, 0, 0, 1);
    letter-spacing: -1.62px;
    text-align: center;
    margin-top: 21px;
    font: 700 36px/38px League Spartan, -apple-system, Roboto, Helvetica, sans-serif;
  }
  </style>