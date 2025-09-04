<script setup lang="ts">
import type { CustomTableFormProps, Empresa, Fornecedor, PaginatedResponse } from '@/types'
import { defineEmits, ref } from 'vue'
import { EntityTable } from '@/components'
import FornecedorForm from './FornecedorForm.vue'
import { useGet } from '@/composables/useApi'
import type { DataTablePageEvent } from 'primevue'

defineProps<CustomTableFormProps<Fornecedor> & { selectMode?: boolean }>()

const emit = defineEmits(['onDelete', 'onEdit', 'onFilter', 'onSelect'])
const emitNewEvent = (name: 'onDelete' | 'onEdit' | 'onFilter' | 'onSelect', event: Event) => {
  emit(name, event)
}

/** Lista de empresas */
const {
  data: response,
  isLoading: loading,
  error,
  get: fetch,
} = useGet<PaginatedResponse<Empresa>>()

const empresas = ref<Empresa[]>([])
const page = ref(0)

const fetchEmpresas = (page = 0) => {
  fetch(`/empresas?page=${page}&size=10`).then(() => {
    empresas.value = response.value?.content || []
  })
}

const onPageChange = (event: DataTablePageEvent) => {
  fetchEmpresas(event.page)
  page.value = event.page || 0
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
      <div class="empresas-list flex flex-col gap-3">
        <span class="text-xl font-semibold">Empresas associadas</span>
        <DataTable
          :value="data.empresas"
          :pt="{
            root: 'min-w-[50rem]',
            column: {
              headerCell: '!bg-slate-200 !border-slate-300 !py-2 !px-2',
            },
          }"
        >
          <Column field="nomeFantasia" header="Nome Fantasia" class="min-w[16rem]" />
          <Column field="cnpj" header="CNPJ" class="min-w[12rem]" />
          <Column field="endereco.cep" header="CEP" class="min-w[10rem]" />
          <Column header="Cidade" class="min-w-[10rem]">
            <template #body="{ data }">
              <span>{{ data.endereco.localidade }} - {{ data.endereco.uf }}</span>
            </template>
          </Column>
          <template #empty>Nenhuma empresa associada</template>
        </DataTable>
      </div>
    </template>
  </EntityTable>
</template>
