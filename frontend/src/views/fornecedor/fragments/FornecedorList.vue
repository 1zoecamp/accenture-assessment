<script setup lang="ts">
import type { CustomFormProps, Fornecedor } from '@/types'
import { defineEmits } from 'vue'
import { EntityTable } from '@/components'
import FornecedorForm from './FornecedorForm.vue'

defineProps<CustomFormProps<Fornecedor>>()

const emit = defineEmits(['onDelete', 'onEdit', 'onFilter'])
const emitNewEvent = (name: 'onDelete' | 'onEdit' | 'onFilter', event: Event) => {
  emit(name, event)
}
</script>

<template>
  <EntityTable
    v-bind="$props"
    @delete="(e: Event) => emitNewEvent('onDelete', e)"
    @filter="(e: Event) => emitNewEvent('onFilter', e)"
    @open-edit="(e: Event) => emitNewEvent('onEdit', e)"
  >
    <template #columns>
      <Column
        field="nome"
        header="Nome / Razão Social"
        class="min-w-[16rem]"
        :show-filter-match-modes="false"
      >
        <template #filter="{ filterModel, filterCallback }">
          <InputText
            v-model="filterModel.value"
            type="text"
            placeholder="Busca por nome"
            @input="filterCallback()"
          />
        </template>
      </Column>

      <Column
        field="documento"
        header="Documento"
        class="min-w-[10rem]"
        :show-filter-match-modes="false"
      >
        <template #filter="{ filterModel, filterCallback }">
          <InputText
            v-model="filterModel.value"
            type="text"
            @input="filterCallback()"
            placeholder="Busca por documento"
          />
        </template>
      </Column>
      <Column field="email" header="Email" class="min-w-[14rem]" />
      <Column field="endereco.cep" header="CEP" class="min-w-[8rem]" />
      <Column header="Cidade" class="min-w-[10rem]">
        <template #body="{ data }">
          <span>{{ data.endereco.localidade }} - {{ data.endereco.uf }}</span>
        </template>
      </Column>
    </template>
    <template #editform="{ data }">
      <FornecedorForm
        :refetch
        :initial-data="data"
        :edit-mode="true"
        @form-delete="emitNewEvent('onDelete', data)"
      />
    </template>
  </EntityTable>
</template>
