<script setup lang="ts">
import type { CustomFormProps, Empresa } from '@/types'
import { defineEmits } from 'vue'
import { EntityTable } from '@/components'

defineProps<CustomFormProps<Empresa>>()

const emit = defineEmits(['onDelete', 'onEdit', 'onFilter'])
const emitNewEvent = (name: 'onDelete' | 'onEdit' | 'onFilter', event: Event) => {
  emit(name, event)
}
</script>

<template>
  <EntityTable
    v-bind="$props"
    @delete="(e: Event) => emitNewEvent('onDelete', e)"
    @edit="(e: Event) => emitNewEvent('onEdit', e)"
    @filter="(e: Event) => emitNewEvent('onFilter', e)"
  >
    <template #columns>
      <Column field="nomeFantasia" header="Nome Fantasia" class="min-w[16rem]" />
      <Column field="cnpj" header="CNPJ" class="min-w[12rem]" />
      <Column field="endereco.cep" header="CEP" class="min-w[10rem]" />
    </template>
  </EntityTable>
</template>
