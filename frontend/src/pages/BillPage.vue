<template>
  <div>
    <BillDetails v-if="billExists" :bill="bill" />
    <BillSend v-if="!billExists" />
  </div>
</template>

<script setup lang="ts">
import axios from 'axios';
import { onBeforeMount, onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import BillDetails from '@/components/BillDetails.vue'
import BillSend from '@/components/BillSend.vue'

const route = useRoute()
const billExists = ref(false)
const bill = ref(null)

onBeforeMount(async () => {
  console.log(route.params.eventId)
  // check if bill has been created
  const response = await axios.get("/bills/event/" + route.params.eventId)
  console.log(response)
  if (response.status === 200 && !response.data.include("HTML")) {
    billExists.value = true
    bill.value = response.data
  }
})
</script>
