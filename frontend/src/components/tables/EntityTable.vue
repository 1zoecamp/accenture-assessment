<script setup lang="ts" generic="T">
import type { CustomTableFormProps } from '@/types'
import {
  useConfirm,
  type DataTableFilterEvent,
  type DataTableFilterMeta,
  type DataTableRowSelectEvent,
} from 'primevue'
import { ref } from 'vue'
import { FilterMatchMode } from '@primevue/core/api'
import DialogButton from '../buttons/DialogButton.vue'

defineProps<CustomTableFormProps<T> & { selectMode?: boolean }>()

const confirm = useConfirm()
const emit = defineEmits(['delete', 'openEdit', 'filter', 'closeEdit', 'selected'])

const expandedRows = ref({})
const selectedRows = ref<T[]>()

const deleteRegistro = (registro: T) => {
  confirm.require({
    message: 'Tem certeza que deseja excluir o registro selecionado?',
    header: 'Confirmar exclusão',
    icon: 'pi pi-exclamation-triangle',
    rejectProps: {
      label: 'Cancelar',
      severity: 'secondary',
      outlined: true,
    },
    acceptProps: {
      label: 'Excluir',
      severity: 'danger',
    },
    accept: () => {
      emit('delete', registro)
    },
  })
}

const filters = ref<DataTableFilterMeta>({
  nome: { value: null, matchMode: FilterMatchMode.CONTAINS },
  documento: { value: null, matchMode: FilterMatchMode.CONTAINS },
})
</script>

<template>
  <div class="datatable px-2">
    <ErrorMessage v-if="error" :error />

    <div v-else class="datatable-container p-2 bg-slate-100 shadow-sm">
      <DataTable
        v-model:expandedRows="expandedRows"
        v-model:selection="selectedRows"
        v-model:filters="filters"
        dataKey="id"
        :value="registros"
        :loading="loading"
        :totalRecords="response?.totalElements"
        :rows="10"
        filterDisplay="menu"
        lazy
        scrollable
        scrollHeight="35rem"
        stripedRows
        paginator
        paginatorTemplate="FirstPageLink PrevPageLink PageLinks CurrentPageReport NextPageLink LastPageLink RowsPerPageDropdown"
        currentPageReportTemplate="Exibindo {first} a {last} de {totalRecords} registros"
        :rowsPerPageOptions="[10, 25, 50]"
        @page="onPageChange"
        @filter="(e: DataTableFilterEvent) => emit('filter', e)"
        @update:selection="(e: DataTableRowSelectEvent) => emit('selected', e)"
      >
        <!-- Configuração do footer (paginação) -->
        <template #paginatorstart></template>
        <template #paginatorend>
          <Button severity="contrast" icon="pi pi-refresh" text @click="refetch()" />
        </template>

        <!-- Expansão de linha / selectMode -->
        <Column v-if="selectMode" selectionMode="multiple" headerStyle="width: 3rem" />
        <Column v-else expander class="w-[5rem]" />

        <!-- Definição das colunas da tabela -->
        <slot name="columns" />
        <Column v-if="!selectMode" header="Ações" field="tipoPessoa" class="w-32">
          <template #body="{ data }">
            <div class="table-actions-group inline-flex gap-2">
              <DialogButton
                :button="{ tooltip: 'Editar registro' }"
                icon="pi pi-pencil"
                severity="secondary"
                class="!p-1"
                :dialog="{ header: 'Editar registro' }"
                @click="emit('openEdit', data)"
                @close="emit('closeEdit')"
              >
                <slot name="editform" :data />
              </DialogButton>
              <Button
                v-tooltip.top="'Excluir registro'"
                icon="pi pi-times"
                severity="secondary"
                @click="deleteRegistro(data)"
                class="!p-1 !text-red-800 !bg-red-50 hover:!bg-red-200 hover:!border-red-200"
              />
            </div>
          </template>
        </Column>

        <!-- Expansão de linha: conteúdo -->
        <template #expansion="{ data }: { data: T }">
          <div class="bg-slate-100 p-3 rounded-md">
            <slot name="expanded" :data />
          </div>
        </template>
        <!-- Demais estados da tabela -->
        <template #empty>
          <div class="text-center p-4 text-gray-500">Nenhum registro encontrado.</div>
        </template>

        <template #loading>
          <div class="h-96 p-4 flex items-center justify-center">
            <i class="pi pi-spin pi-spinner !text-3xl text-slate-600"></i>
          </div>
        </template>
      </DataTable>
    </div>
  </div>
</template>
