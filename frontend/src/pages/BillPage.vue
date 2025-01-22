<template>
  <div class="glass-container glass-card">
    <img loading="lazy" src="@/assets/back.svg" class="back-link" alt="Return to event" @click="goBack"/>
    <BillDetails v-if="billExists" :bill="bill"/>
    <BillSend v-if="!billExists" :bill="bill"/>
  </div>
</template>

<script setup lang="ts">
import axios, {type AxiosResponse} from 'axios'
import {onBeforeMount, ref, type Ref} from 'vue'
import {useRoute} from 'vue-router'
import BillDetails from '@/components/BillDetails.vue'
import BillSend from '@/components/BillSend.vue'
import router from '@/router'
import type {BillDTO} from '@/types/BillDTO'

const route = useRoute()
const billExists = ref(false)
const bill: Ref<BillDTO | null> = ref(null)

function goBack() {
  router.push({
    path: '/event-page',
    query: {eventId: bill.value?.eventId}
  });
}

onBeforeMount(async () => {
  // check if bill has been created
  const response: AxiosResponse<BillDTO[], unknown> = await axios.get(
    `/billing/bills/event/${route.query.eventId}`,
  )
  console.log(response)
  if (response.status !== 200) {
    router.go(-1)
  }
  if ((response.data as BillDTO[]).length === 0) {
    router.go(-1)
  }

  if (!response.data[0].path) {
    billExists.value = false
    bill.value = response.data[0]
  } else {
    billExists.value = true
    bill.value = response.data[0]
  }
})
</script>

<style scoped>
.back-link {
  width: 24px;
  height: 24px;
  cursor: pointer;
  position: absolute;
  top: 10px;
  left: 10px;
}
</style>
