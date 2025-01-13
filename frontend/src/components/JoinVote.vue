<template>
    <div class="event-creation-container">
        <div class="event-creation-wrapper">
            <h1 class="main-title">Join a vote!</h1>
            <p>Vote code :</p>
            <input ref="nameInput" class="name-selector" tabindex="0" role="textbox" @focus="handleNameSelection"
                placeholder="Vote code" />
            <button class="validate-button" @click="handleValidation">
                Valider
            </button>
        </div>
    </div>
</template>

<script setup lang="ts">
function handleNameSelection(): void {
    // Implementation for name selection
};
function handleValidation(): void {
    // Implementation for validation
};


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
</style>