<script setup lang="ts">
import type { Empresa, PaginatedResponse } from '@/types'
import { useToast, type DataTablePageEvent } from 'primevue'
import { ref, onMounted } from 'vue'
import { EntityLayout } from '@/components'
import { useDelete, useGet } from '@/composables/useApi'
import EmpresaList from './fragments/EmpresaList.vue'
import EmpresaForm from './fragments/EmpresaForm.vue'

const page = ref(0)
const registros = ref<Empresa[]>([])

const toast = useToast()

/** Obtenção dos registros */
const {
  data: response,
  isLoading: loading,
  error,
  get: fetch,
} = useGet<PaginatedResponse<Empresa>>()

const fetchData = (page = 0) => {
  fetch(`/empresas?page=${page}&size=10`).then(() => {
    registros.value = response.value?.content || []
  })
}

const refetch = (page = 0) => {
  fetchData(page)
  registros.value = response.value?.content || []
}

/** Controle da deleção */
const { del, isLoading: deleteLoading, error: deleteError } = useDelete<Empresa>()

const deleteEmpresa = async (empresa: Empresa) => {
  await del(`/empresas/${empresa.id}`).then(() => {
    toast.add({
      severity: 'success',
      summary: `Empresa excluída`,
      detail: `Empresa ${empresa.nomeFantasia} (${empresa.cnpj}) excluída com sucesso`,
      life: 3000,
    })
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
  <EntityLayout entity="Empresa" title="Lista de Empresas">
    <template #list>
      <EmpresaList
        :error
        :registros
        :loading
        :response
        :onPageChange
        :refetch
        @on-delete="(e: Empresa) => deleteEmpresa(e)"
      />
    </template>
    <template #form><EmpresaForm :refetch /></template>
  </EntityLayout>
</template>
