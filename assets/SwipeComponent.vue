<template>
    <div class="swipe">
      <div class="main-container">
        <img
          loading="lazy"
          src="https://cdn.builder.io/api/v1/image/assets/TEMP/ae68a0e69004b0aa5096378a272c958961b8e8da63a60b8cb86687bad3d7e9d4?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d"
          alt="Main Image"
          class="main-image"
        />
        <div class="icon-container">
          <img
            loading="lazy"
            src="https://cdn.builder.io/api/v1/image/assets/TEMP/dc15620de14f80430314f99353fbc0c39c7dc99e5334f34061d5eea7ed3642d4?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d"
            alt="Icon 1"
            class="icon-1"
          />
          <img
            loading="lazy"
            src="https://cdn.builder.io/api/v1/image/assets/TEMP/0f3b04805a56cdf18c8db50279aa6933cf92bc9c1eb0327e17456453ca136838?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d"
            alt="Icon 2"
            class="icon-2"
          />
        </div>
        <div class="swipe-text">Swipe !</div>
        <div class="yellow-container">
          <div class="profile-container">
            <img
              loading="lazy"
              src="https://cdn.builder.io/api/v1/image/assets/TEMP/e4762f9fa0a006504c0d5c5ad77f3cf0fd27454d2c3512a3f6edaaf1e4647cef?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d"
              alt="Profile Image"
              class="profile-image"
            />
            <div class="grey-box"></div>
          </div>
          <div class="content-container">
            <div class="icon-list">
              <img
                loading="lazy"
                src="https://cdn.builder.io/api/v1/image/assets/TEMP/fa708cecc73e8b32335f52d05da68f9984267ad789adb3657ccd4c965f3a1297?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d"
                alt="Icon 3"
                class="icon-3"
              />
              <img
                loading="lazy"
                src="https://cdn.builder.io/api/v1/image/assets/TEMP/870b983e2b6bb8a36fe91e6a3fb775338c2a8881a64a9313daf1cbb06d20df68?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d"
                alt="Icon 4"
                class="icon-4"
              />
              <img
                loading="lazy"
                src="https://cdn.builder.io/api/v1/image/assets/TEMP/a2e62b0d627c4de4e8f0a4e2a4b8e77e8383bf8c911dfab2c5b8fe3ac5d7d2d4?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d"
                alt="Icon 5"
                class="icon-5"
              />
            </div>
            <div class="title">Pizza de la Mama</div>
            <div class="divider">
              ____________________<br />_______________
            </div>
          </div>
        </div>
        <div class="footer-icons">
          <img
            loading="lazy"
            src="https://cdn.builder.io/api/v1/image/assets/TEMP/111e4218a44dbfd90cd401a47b84c81472b565ad2adc7bef9c2b3539af9e61c3?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d"
            alt="Footer Icon 1"
            class="footer-icon-1"
          />
          <img
            loading="lazy"
            src="https://cdn.builder.io/api/v1/image/assets/TEMP/c67604ced27306507697b936f0a15be87ad48dddd65a35d4019124bfcd784546?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d"
            alt="Footer Icon 2"
            class="footer-icon-2"
          />
          <img
            loading="lazy"
            src="https://cdn.builder.io/api/v1/image/assets/TEMP/713431fe4e73162f25b6fda33398c8f58f270bf19da1fe5ed2bcde3cbba7f6f7?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d"
            alt="Footer Icon 3"
            class="footer-icon-3"
          />
        </div>
      </div>
    </div>
  </template>
  
  <script lang="ts">
  import { defineComponent } from 'vue';
  
  export default defineComponent({
    name: 'SwipeComponent',
  });
  </script>
  
  <style scoped>
  .swipe {
    display: flex;
    max-width: 480px;
    width: 100%;
    flex-direction: column;
    margin: 0 auto;
  }
  .main-container {
    display: flex;
    flex-direction: column;
    border-radius: 30px;
    position: relative;
    aspect-ratio: 0.461;
    width: 100%;
    padding-top: 26px;
    align-items: center;
  }
  .main-image {
    position: absolute;
    inset: 0;
    height: 100%;
    width: 100%;
    object-fit: cover;
    object-position: center;
  }
  .icon-container {
    position: relative;
    display: flex;
    width: 100%;
    max-width: 349px;
    align-items: start;
    gap: 20px;
    justify-content: space-between;
  }
  .icon-1 {
    aspect-ratio: 1;
    object-fit: contain;
    object-position: center;
    width: 48px;
    border-radius: 0px 20px 0px 0px;
    margin-top: 6px;
  }
  .icon-2 {
    aspect-ratio: 1;
    object-fit: contain;
    object-position: center;
    width: 48px;
  }
  .swipe-text {
    position: relative;
    color: var(--Grays-White, #fff);
    text-align: center;
    letter-spacing: -0.23px;
    margin: 45px 0 0 15px;
    font: 700 46px/0 League Spartan, sans-serif;
  }
  .yellow-container {
    position: relative;
    border-radius: 44px;
    background: var(--Yellow-2, #f3e9b5);
    display: flex;
    margin-top: 4px;
    width: 100%;
    max-width: 358px;
    flex-direction: column;
    padding: 36px 0 77px;
  }
  .profile-container {
    align-self: start;
    display: flex;
  }
  .profile-image {
    aspect-ratio: 1.04;
    object-fit: contain;
    object-position: center;
    width: 79px;
    margin: auto 0;
  }
  .grey-box {
    border-radius: 30px;
    background-color: rgba(217, 217, 217, 1);
    display: flex;
    width: 246px;
    height: 252px;
    aspect-ratio: 1;
  }
  .content-container {
    display: flex;
    margin-top: 14px;
    width: 100%;
    flex-direction: column;
    align-items: center;
    padding: 0 46px;
  }
  .icon-list {
    display: flex;
    width: 225px;
    max-width: 100%;
    gap: 20px;
    justify-content: space-between;
  }
  .icon-3,
  .icon-4,
  .icon-5 {
    aspect-ratio: 1;
    object-fit: contain;
    object-position: center;
    width: 48px;
  }
  .title {
    color: rgba(0, 0, 0, 1);
    letter-spacing: -1.62px;
    text-align: center;
    margin-top: 31px;
    font: 700 36px/1 League Spartan, sans-serif;
  }
  .divider {
    color: rgba(0, 0, 0, 1);
    letter-spacing: -1.62px;
    text-align: center;
    align-self: stretch;
    margin-top: 21px;
    font: 700 36px/38px League Spartan, -apple-system, Roboto, Helvetica,
      sans-serif;
  }
  .footer-icons {
    position: relative;
    border-radius: 0px 0px 30px 30px;
    background-color: rgba(9, 9, 9, 1);
    align-self: stretch;
    display: flex;
    margin-top: 53px;
    width: 100%;
    gap: 20px;
    justify-content: space-between;
    padding: 18px 26px;
  }
  .footer-icon-1,
  .footer-icon-2,
  .footer-icon-3 {
    aspect-ratio: 1;
    object-fit: contain;
    object-position: center;
    width: 48px;
  }
  .footer-icon-2 {
    aspect-ratio: 0.91;
    width: 40px;
    margin: auto 0;
  }
  </style>