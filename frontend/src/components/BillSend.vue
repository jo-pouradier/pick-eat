<template>
    <h1 >Upload Picture</h1>
    <div class="upload-options">
      <button @click="triggerFileInput" class="upload-button">Choose a Picture</button>
      <button @click="capturePhoto" class="upload-button">Take a Picture</button>
    </div>
    <input type="file" ref="fileInput" accept="image/*" @change="previewImage" hidden />
    <div v-if="previewSrc" class="preview-section">
      <h2>Preview</h2>
      <img :src="previewSrc" alt="Preview" class="preview-image" />
    </div>
    <button @click="uploadImage" class="upload-button" :disabled="!selectedFile">
      Upload Picture
    </button>
</template>

<script setup lang="ts">
import type { BillDTO } from '@/types/BillDTO'
import axios from 'axios'
import { ref } from 'vue'

const fileInput = ref<HTMLInputElement | null>(null)
const previewSrc = ref<string | null>(null)
const selectedFile = ref<File | null>(null)

const props = defineProps<{
  bill: BillDTO | null
}>()

function triggerFileInput() {
  fileInput.value?.click()
}

function previewImage(event: Event) {
  const target = event.target as HTMLInputElement
  const file = target.files?.[0]
  if (file) {
    selectedFile.value = file
    previewSrc.value = URL.createObjectURL(file)
  }
}

function capturePhoto() {
  // Logic for capturing photo using camera can be added here.
  alert('Camera functionality not implemented yet.')
}

async function uploadImage() {
  if (!selectedFile.value) return

  const formData = new FormData()
  formData.append('file', selectedFile.value)

  const response = await axios.post(`/billing/bills/image/${props.bill?.id}`, formData)
  if (response.status !== 200) {
    throw new Error('Failed to upload image')
  }

  alert('Image uploaded successfully!')
  previewSrc.value = null
  selectedFile.value = null
}
</script>

<style scoped>

.upload-options {
  display: flex;
  justify-content: space-around;
}

.upload-button {
  padding: 10px 16px;
  font-size: 16px;
  color: var(--black);
  background-color: var(--accent-orange);
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.upload-button:disabled {
  background-color: var(--valid-green);
  cursor: not-allowed;
}


.preview-section {
  text-align: center;
  margin-bottom: 16px;
}

.preview-image {
  max-width: 100%;
  max-height: 200px;
  border-radius: 8px;
  object-fit: contain;
}
</style>
