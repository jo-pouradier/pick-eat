
<template>
  <div class="bill-container">
    <h1>Bill Details</h1>
    <div class="bill-info">
      <p v-if="!eventName"><span>Event ID:</span> {{ props.bill?.eventId }}</p>
      <p v-else><span>Event Name:</span> {{ eventName }}</p>
      <p><span>Bill ID:</span> {{ props.bill?.id }}</p>
    </div>
    <img :src="`/billing/bills/image/${bill?.id}`" alt="bill image" class="bill-image" />
    <p class="total-price"><span>Total Price:</span> {{ bill?.total_price }}</p>
    <div class="parts-section">
      <h2>Parts</h2>

      <ul v-if="props.bill?.parts?.length" class="parts-list">
        <li v-for="part in bill?.parts" :key="part.id ?? ''" class="part-item">
          <p><span>Part ID:</span> {{ part.id }}</p>
          <p><span>Bill ID:</span> {{ part.billId }}</p>
          <p><span>User ID:</span> {{ part.userId }}</p>
          <p><span>Price:</span> {{ part.price }}€</p>
          <p><span>Description:</span> {{ part.text }}</p>
          <div :style="{display: 'flex', gap: '1rem'}">
            <input type="checkbox" @change="setPartUserId(part)" :checked="part.userId == props.bill?.userId"
              :disabled="part.userId !== props.bill?.userId && part.userId !== null"
            />
            <div>{{ selectPartText(part) }}</div>
          </div>
        </li>
      </ul>
      <p v-else>The bill has not been processed yet, come back in a few minutes</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { BillDTO, BillPartDTO } from '@/types/BillDTO';
import axios from 'axios';
import {onMounted, ref} from "vue";
import {loadEvent} from "@/lib/EventUtils.ts";

const props = defineProps<{
  bill: BillDTO | null
}>()

const eventName = ref<string | null>(null)
onMounted( () => {
  if( props.bill ) {
    console.log('Bill:', props.bill)
    loadEvent(props.bill.eventId).then(response => {
      eventName.value = response.data.name
      console.log('Event:', response.data)
    })
  }})

const setPartUserId = async (part: BillPartDTO) => {
  if (!props.bill) {
    console.error('No bill found')
    return
  }
  part.userId = part.userId === props.bill?.userId ? null : props.bill?.userId
  const response = await axios.put(`/billing/bills/${part.id}`, part)
  console.log(response)
  if (response.status !== 200) {
    console.error('Part not updated')
    // todo notification
    return
  }
  // update bill
  props.bill.parts?.forEach((p) => {
    if (p.id === part.id) {
      p.userId = part.userId
    }
  })
}

function selectPartText(part: BillPartDTO) {
  if (!props.bill) {
    return 'No bill found'
  }
  if (part.userId === props.bill.userId) {
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
