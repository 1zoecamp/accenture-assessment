<script setup lang="ts">
import type { Empresa, Fornecedor, PaginatedResponse } from '@/types'
import type { DataTablePageEvent } from 'primevue'
import { ref } from 'vue'
import { useGet } from '@/composables/useApi'

const { refetch } = defineProps<{ fornecedor: Fornecedor; refetch: (page?: number) => void }>()

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
  <div class="empresas-list flex flex-col gap-3">
    <span class="text-xl font-semibold">Empresas associadas</span>
    <DataTable
      :value="fornecedor.empresas"
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
