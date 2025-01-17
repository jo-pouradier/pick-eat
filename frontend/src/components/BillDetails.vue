
<template>
  <div class="bill-container">
    <h1 class="bill-title">Bill Details</h1>
    <div class="bill-info">
      <p><span>Event ID:</span> {{ eventId }}</p>
      <p><span>Bill ID:</span> {{ bill?.id }}</p>
      <p><span>User ID:</span> {{ bill?.userId }}</p>
    </div>
    <img :src="`/bills/image/${bill?.path}`" alt="bill image" class="bill-image" />
    <p class="total-price"><span>Total Price:</span> {{ bill?.total_price }}</p>
    <div class="parts-section">
      <h2>Parts</h2>
      <ul class="parts-list">
        <li v-for="part in bill?.parts" :key="part.id ?? ''" class="part-item">
          <p><span>Part ID:</span> {{ part.id }}</p>
          <p><span>Bill ID:</span> {{ part.billId }}</p>
          <p><span>User ID:</span> {{ part.userId }}</p>
          <p><span>Price:</span> {{ part.price }}€</p>
          <p><span>Description:</span> {{ part.text }}</p>
          <div :style="{display: 'flex', gap: '1rem'}">
            <input type="checkbox" @change="setPartUserId(part)" :checked="part.userId == userId"
              :disabled="part.userId !== userId && part.userId !== null"
            />
            <div>{{ selectPartText(part) }}</div>
          </div>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { BillDTO, BillPartDTO } from '@/types/BillDTO'
import axios from 'axios'
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const userId = ref('3fa85f64-5717-4562-b3fc-2c963f66afa6') // todo get from auth
// get id from url
const eventId = ref(route.params.eventId)
console.log(eventId.value)
const bill = ref<BillDTO | null>(null)

onMounted(async () => {
  // const response = await axios.get("/bills/event/" + eventId.value)
  // if (!response.data) {
  //   console.log("No data found")
  // }
  // console.log(response.data)
  const response: {data: BillDTO} = {
    data: {
      id: '3fa85f64-5717-4562-b3fc-2c963f66afa6',
      userId: '3fa88f64-5712-4558-b6fc-6c968f70afa44',
      bucketName: 'string',
      path: 'string',
      parts: [
        {
          id: '3fa85f64-5717-4562-b3fc-2c963f66afa6',
          billId: '3fa85f64-5717-4562-b3fc-2c963f66afa6',
          userId: null,
          price: 12,
          text: 'Biere',
        },
        {
          id: '3fa85f64-5717-4562-b3fc-2c963f66afa7',
          billId: '3fa85f64-5717-4562-b3fc-2c963f66afa6',
          userId: '3fa85f64-5717-4562-b3fc-2c963f66afa6',
          price: 9,
          text: 'Poulet',
        },
        {
          id: '3fa85f64-5717-4562-b3fc-2c963f66afa8',
          billId: '3fa85f64-5717-4562-b3fc-2c963f66afa6',
          userId: '6fa85f64-5717-4562-b3fc-2c963f66afa6',
          price: 5,
          text: 'Frites',
        },
        {
          id: '3fa85f64-5717-4562-b3fc-2c963f66afa9',
          billId: '3fa85f64-5717-4562-b3fc-2c963f66afa6',
          userId: null,
          price: 3,
          text: 'Coca',
        }
      ],
      total_price: 0,
      eventId: '5fa85f64-5717-4562-b3fc-2c963f66afa8'
    },
  }
  bill.value = response.data
})

const setPartUserId = async (part: BillPartDTO) => {
  part.userId = part.userId === userId.value ? null : userId.value
  const response = await axios.put(`/bills/${part.id}`, part)
  console.log(response)
  if (response.status !== 200) {
    console.error('Part not updated')
    // todo notification
    return
  }
  // update bill
  bill.value?.parts?.forEach((p) => {
    if (p.id === part.id) {
      p.userId = part.userId
    }
  })
}

function selectPartText(part: BillPartDTO) {
  if (part.userId === userId.value) {
    return 'For me!'
  } else if (part.userId) {
    return 'For someone else'
  }
  return 'Who ate this?'
}
</script>

<style scoped>
.bill-container {
  padding: 16px;
  font-family: Arial, sans-serif;
  background-color: #f9f9f9;
  color: #333;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  max-width: 480px;
  margin: 16px auto;
  overflow-y: auto;
  max-height: calc(100vh - 80px);
}

.bill-title {
  font-size: 24px;
  font-weight: bold;
  color: #4CAF50;
  text-align: center;
  margin-bottom: 16px;
}

.bill-info p {
  font-size: 14px;
  margin: 4px 0;
}

.bill-info span {
  font-weight: bold;
}

.bill-image {
  display: block;
  width: 100%;
  max-height: 200px;
  object-fit: contain;
  border-radius: 8px;
  margin: 16px 0;
}

.total-price {
  font-size: 18px;
  font-weight: bold;
  color: #FF5722;
  text-align: center;
  margin-bottom: 16px;
}

.parts-section {
  margin-top: 16px;
}

.parts-section h2 {
  font-size: 18px;
  color: #4CAF50;
  margin-bottom: 8px;
}

.parts-list {
  list-style: none;
  padding: 0;
}

.part-item {
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 8px 12px;
  margin-bottom: 12px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.part-item p {
  font-size: 14px;
  margin: 4px 0;
}

.part-item span {
  font-weight: bold;
  color: #333;
}
@media (max-width: 768px) {
  .bill-container {
    padding-top: 120px;
    max-height: 100dvh;
  }
}
</style>
