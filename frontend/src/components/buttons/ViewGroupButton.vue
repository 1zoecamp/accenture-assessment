<script setup lang="ts">
import type { SelectButtonChangeEvent } from 'primevue'
import { ref } from 'vue'
import { useRouter } from 'vue-router'

/** Types */
type ChangeEvent = SelectButtonChangeEvent

/** Composables */
const router = useRouter()

/** Helpers */
const options = ref([
  { name: 'Lista de Fornecedores', value: 'fornecedor' },
  { name: 'Lista de Empresas', value: 'empresa' },
])
const value = ref({ name: 'Lista de Fornecedores', value: 'fornecedor' })
</script>
<template>
  <SelectButton
    v-model="value"
    :allowEmpty="false"
    :options="options"
    optionLabel="name"
    :pt="{ pcToggleButton: { root: '!bg-slate-100 text-slate-600' } }"
    @change="
      (e: ChangeEvent) => {
        if (e.value) {
          router.push({ name: e.value.value })
        }
      }
    "
  />
</template>
