<script setup lang="ts">
import type { CustomTableFormProps, Empresa } from '@/types'
import { defineEmits } from 'vue'
import { EntityTable } from '@/components'
import EmpresaForm from './EmpresaForm.vue'
import VinculoEmpresaFornecedor from './VinculoEmpresaFornecedor.vue'

/** Props, composables, emits */
const { refetch } = defineProps<CustomTableFormProps<Empresa>>()

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
      <Column header="Cidade" class="min-w-[10rem]">
        <template #body="{ data }">
          <span>{{ data.endereco.localidade }} - {{ data.endereco.uf }}</span>
        </template>
      </Column>
    </template>
    <template #editform="{ data }">
      <EmpresaForm
        :refetch
        :initial-data="data"
        :edit-mode="true"
        @form-delete="emitNewEvent('onDelete', data)"
      />
    </template>
    <template #expanded="{ data }: { data: Empresa }">
      <VinculoEmpresaFornecedor :empresa="data" :refetch />
    </template>
  </EntityTable>
</template>
