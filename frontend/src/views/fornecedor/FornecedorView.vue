<script setup lang="ts">
import {
  useToast,
  type DataTableFilterEvent,
  type DataTableFilterMetaData,
  type DataTablePageEvent,
} from 'primevue'
import type { Fornecedor, PaginatedResponse } from '@/types'
import { onMounted, ref } from 'vue'
import { EntityLayout } from '@/components'
import { useDelete, useGet } from '@/composables/useApi'
import FornecedorList from './fragments/FornecedorList.vue'
import FornecedorForm from './fragments/FornecedorForm.vue'

const page = ref(0)
const filter = ref({
  nome: '',
  documento: '',
})
const registros = ref<Fornecedor[]>([])

const toast = useToast()

/** Obtenção dos registros */
const {
  data: response,
  isLoading: loading,
  error,
  get: fetch,
} = useGet<PaginatedResponse<Fornecedor>>()

const fetchData = (page = 0) => {
  const strNome = filter.value.nome ? `nome=${filter.value.nome}&` : ''
  const strDocumento = filter.value.documento ? `documento=${filter.value.documento}&` : ''
  const filterValue = strNome + strDocumento

  fetch(`/fornecedores?${filterValue}page=${page}&size=10`).then(() => {
    registros.value = response.value?.content || []
  })
}

const refetch = (page = 0) => {
  fetchData(page)
  registros.value = response.value?.content || []
}

/** Controle dos filtros */
const updateFilter = (e: DataTableFilterEvent) => {
  filter.value = {
    nome: (e.filters?.nome as DataTableFilterMetaData)?.value ?? '',
    documento: (e.filters?.documento as DataTableFilterMetaData).value ?? '',
  }
  fetchData()
}

/** Controle da deleção */
const { del, error: deleteError } = useDelete<Fornecedor>()

const deleteFornecedor = async (fornecedor: Fornecedor) => {
  await del(`/fornecedores/${fornecedor.id}`).then(() => {
    if (!deleteError.value) {
      toast.add({
        severity: 'success',
        summary: `Fornecedor excluído`,
        detail: `Fornecedor ${fornecedor.nome} (${fornecedor.documento}) excluído com sucesso`,
        life: 3000,
      })
    } else {
      toast.add({
        severity: 'error',
        summary: 'Algo deu errado',
        detail: deleteError.value?.response?.data,
        life: 10000,
      })
    }

    refetch(page.value)
  })
}

/** Controle da paginação */
const onPageChange = (event: DataTablePageEvent) => {
  fetchData(event.page)
  page.value = event.page || 0
}

onMounted(() => {
  fetchData()
})
</script>

<template>
  <EntityLayout entity="Fornecedor" title="Lista de Fornecedores">
    <template #list>
      <FornecedorList
        :error
        :registros
        :loading
        :response
        :onPageChange
        :refetch
        @on-delete="(f: Fornecedor) => deleteFornecedor(f)"
        @on-filter="(e: DataTableFilterEvent) => updateFilter(e)"
      />
    </template>
    <template #form><FornecedorForm :refetch /></template>
  </EntityLayout>
</template>
