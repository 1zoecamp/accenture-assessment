<script setup lang="ts">
import type { CustomTableFormProps, Fornecedor } from '@/types'
import { defineEmits } from 'vue'
import { EntityTable } from '@/components'
import FornecedorForm from './FornecedorForm.vue'
import VinculoFornecedorEmpresa from './VinculoFornecedorEmpresa.vue'

defineProps<CustomTableFormProps<Fornecedor> & { selectMode?: boolean }>()

const emit = defineEmits(['onDelete', 'onEdit', 'onFilter', 'onSelect'])
const emitNewEvent = (name: 'onDelete' | 'onEdit' | 'onFilter' | 'onSelect', event: Event) => {
  emit(name, event)
}
</script>

<template>
  <EntityTable
    :select-mode="selectMode"
    v-bind="$props"
    @delete="(e: Event) => emitNewEvent('onDelete', e)"
    @filter="(e: Event) => emitNewEvent('onFilter', e)"
    @selected="(e: Event) => emitNewEvent('onSelect', e)"
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
    <template #expanded="{ data }: { data: Fornecedor }">
      <VinculoFornecedorEmpresa :fornecedor="data" :refetch />
    </template>
  </EntityTable>
</template>
