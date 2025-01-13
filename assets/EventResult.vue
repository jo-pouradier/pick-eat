<template>
    <div class="event-result">
      <div class="event-content">
        <img src="https://cdn.builder.io/api/v1/image/assets/TEMP/ae68a0e69004b0aa5096378a272c958961b8e8da63a60b8cb86687bad3d7e9d4?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d" alt="" class="main-image" />
        <div class="sub-image-container">
          <img src="https://cdn.builder.io/api/v1/image/assets/TEMP/dc15620de14f80430314f99353fbc0c39c7dc99e5334f34061d5eea7ed3642d4?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d" alt="Sub image 1" class="sub-image"/>
          <img src="https://cdn.builder.io/api/v1/image/assets/TEMP/0f3b04805a56cdf18c8db50279aa6933cf92bc9c1eb0327e17456453ca136838?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d" alt="Sub image 2" class="sub-image"/>
        </div>
        <div class="title">It’s decided!</div>
        <div class="result-container">
          <img src="https://cdn.builder.io/api/v1/image/assets/TEMP/c25c0aa9fe685f35dd493bf0e1ea6d6a4d6bfe7b8207c002cc3e837528a339c1?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d" alt="Primary result" class="result-image"/>
          <div class="result-thumbnail"></div>
          <div class="result-icons">
            <img src="https://cdn.builder.io/api/v1/image/assets/TEMP/fa708cecc73e8b32335f52d05da68f9984267ad789adb3657ccd4c965f3a1297?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d" alt="Icon 1" class="icon"/>
            <img src="https://cdn.builder.io/api/v1/image/assets/TEMP/870b983e2b6bb8a36fe91e6a3fb775338c2a8881a64a9313daf1cbb06d20df68?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d" alt="Icon 2" class="icon"/>
            <img src="https://cdn.builder.io/api/v1/image/assets/TEMP/a2e62b0d627c4de4e8f0a4e2a4b8e77e8383bf8c911dfab2c5b8fe3ac5d7d2d4?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d" alt="Icon 3" class="icon"/>
          </div>
          <div class="result-title">Pizza de la Mama</div>
          <div class="result-separator">____________________<br />_______________</div>
        </div>
        <div class="icon-bar">
          <img src="https://cdn.builder.io/api/v1/image/assets/TEMP/111e4218a44dbfd90cd401a47b84c81472b565ad2adc7bef9c2b3539af9e61c3?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d" alt="Icon 1 in bar" class="bar-icon"/>
          <img src="https://cdn.builder.io/api/v1/image/assets/TEMP/c67604ced27306507697b936f0a15be87ad48dddd65a35d4019124bfcd784546?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d" alt="Icon 2 in bar" class="bar-icon tall-icon"/>
          <img src="https://cdn.builder.io/api/v1/image/assets/TEMP/e6af8f32f2f3e825b8a010e73b2be7487382ac23345a0bcde0102f167b70308e?placeholderIfAbsent=true&apiKey=e6ddd9cad30b4b528d92a08d5f92673d" alt="Icon 3 in bar" class="bar-icon"/>
        </div>
      </div>
    </div>
  </template>
  
  
  
  <style scoped>
  .event-result {
    display: flex;
    max-width: 480px;
    width: 100%;
    flex-direction: column;
    margin: 0 auto;
  }
  .event-content {
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
  .sub-image-container {
    position: relative;
    display: flex;
    width: 100%;
    max-width: 341px;
    gap: 20px;
    justify-content: space-between;
  }
  .sub-image {
    aspect-ratio: 1;
    object-fit: contain;
    object-position: center;
    width: 48px;
  }
  .title {
    position: relative;
    color: rgba(255, 255, 255, 1);
    letter-spacing: -0.28px;
    text-align: center;
    margin-top: 22px;
    font: 400 55px/0 Lobster, sans-serif;
  }
  .result-container {
    position: relative;
    border-radius: 44px;
    background: var(--Yellow-2, #f3e9b5);
    background-color: var(--Yellow-2, #f3e9b5);
    display: flex;
    margin-top: 12px;
    width: 100%;
    max-width: 358px;
    flex-direction: column;
    align-items: center;
    padding: 10px 43px 77px;
  }
  .result-image {
    aspect-ratio: 1.84;
    object-fit: contain;
    object-position: center;
    width: 136px;
    max-width: 100%;
  }
  .result-thumbnail {
    border-radius: 30px;
    background-color: rgba(217, 217, 217, 1);
    display: flex;
    width: 246px;
    height: 224px;
  }
  .result-icons {
    display: flex;
    margin-top: 14px;
    width: 225px;
    max-width: 100%;
    gap: 20px;
    justify-content: space-between;
  }
  .icon {
    aspect-ratio: 1;
    object-fit: contain;
    object-position: center;
    width: 48px;
  }
  .result-title {
    color: rgba(0, 0, 0, 1);
    letter-spacing: -1.62px;
    text-align: center;
    margin-top: 31px;
    font: 700 36px/1 League Spartan, sans-serif;
  }
  .result-separator {
    color: rgba(0, 0, 0, 1);
    letter-spacing: -1.62px;
    text-align: center;
    align-self: stretch;
    margin-top: 21px;
    font: 700 36px/38px League Spartan, -apple-system, Roboto, Helvetica, sans-serif;
  }
  .icon-bar {
    position: relative;
    border-radius: 0px 0px 30px 30px;
    background-color: rgba(9, 9, 9, 1);
    align-self: stretch;
    display: flex;
    margin-top: 53px;
    width: 100%;
    align-items: start;
    gap: 20px;
    justify-content: space-between;
    padding: 17px 21px;
  }
  .bar-icon {
    aspect-ratio: 1;
    object-fit: contain;
    object-position: center;
    width: 48px;
  }
  .tall-icon {
    aspect-ratio: 0.91;
    object-fit: contain;
    object-position: center;
    width: 40px;
    align-self: stretch;
    margin: auto 0;
  }
  </style>