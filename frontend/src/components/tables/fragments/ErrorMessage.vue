<script setup lang="ts">
import type { AxiosError } from 'axios'

defineProps<{ error: AxiosError<unknown, any> | null }>()
</script>
<template>
  <div
    v-if="error"
    class="bg-red-100 border-l-4 border-red-500 text-red-700 p-4 rounded-md"
    role="alert"
  >
    <p class="font-bold">Ocorreu um erro ao buscar os dados</p>
    <span>{{ error.message }}</span>
    <pre v-if="error.response?.data" class="mt-2 text-sm bg-red-50 p-2 rounded">
      {{ JSON.stringify(error.response.data, null, 2) }}
    </pre>
  </div>
</template>
