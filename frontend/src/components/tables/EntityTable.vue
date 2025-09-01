<script setup lang="ts" generic="T">
import type { DataTablePageEvent } from 'primevue/datatable'
import type { PaginatedResponse } from '@/types'
import { onMounted, computed, ref } from 'vue'
import { useGet } from '@/composables/useApi'

const { url } = defineProps<{ url: string }>()
const { data: response, isLoading, error, get: fetch } = useGet<PaginatedResponse<T>>()

// Obtenção dos dados
const fetchData = (page = 0) => {
  fetch(`/${url}?page=${page}&size=10`)
}

onMounted(() => {
  fetchData(0)
})

// Armazena o estado da paginação e do filtro
const page = ref(0)
// const filter = ref('');

// Lista de registros da entidade
const registros = computed(() => response.value?.content || [])

// Controle de paginação
const onPageChange = (event: DataTablePageEvent) => {
  fetchData(event.page)
  page.value = event.page || 0
}
</script>

<template>
  <div class="datatable px-2">
    <ErrorMessage v-if="error" :error />

    <div v-else class="datatable-container p-2 bg-slate-100 shadow-sm">
      <DataTable
        dataKey="id"
        :value="registros"
        :loading="isLoading"
        :totalRecords="response?.totalElements"
        :rows="10"
        paginator
        @page="onPageChange"
        paginatorTemplate="FirstPageLink PrevPageLink PageLinks CurrentPageReport NextPageLink LastPageLink RowsPerPageDropdown"
        currentPageReportTemplate="Exibindo {first} a {last} de {totalRecords} registros"
        :rowsPerPageOptions="[10, 25, 50]"
        removableSort
        stripedRows
      >
        <!-- Configuração do footer (paginação) -->
        <template #paginatorstart></template>
        <template #paginatorend>
          <Button severity="contrast" icon="pi pi-refresh" text @click="fetchData(page)" />
        </template>

        <!-- Definição das colunas da tabela -->
        <slot name="columns" />

        <!-- Demais estados da tabela -->
        <template #empty>
          <div class="text-center p-4 text-gray-500">Nenhum registro encontrado.</div>
        </template>

        <template #loading>
          <div class="p-4">
            <div v-for="n in 5" :key="n" class="flex items-center mb-4">
              <Skeleton height="2rem" width="100%" />
            </div>
          </div>
        </template>
      </DataTable>
    </div>
  </div>
</template>
