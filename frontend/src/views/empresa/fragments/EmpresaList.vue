<script setup lang="ts">
import type { CustomFormProps, Empresa, Fornecedor, PaginatedResponse } from '@/types'
import { defineEmits, ref, useTemplateRef } from 'vue'
import { EntityTable } from '@/components'
import EmpresaForm from './EmpresaForm.vue'
import DialogButton from '@/components/buttons/DialogButton.vue'
import { useDelete, useGet } from '@/composables/useApi'
import FornecedorList from '@/views/fornecedor/fragments/FornecedorList.vue'
import { useToast, type DataTablePageEvent } from 'primevue'

const { refetch } = defineProps<CustomFormProps<Empresa>>()

const toast = useToast()

const emit = defineEmits(['onDelete', 'onEdit', 'onFilter'])
const emitNewEvent = (name: 'onDelete' | 'onEdit' | 'onFilter', event: Event) => {
  emit(name, event)
}

/** Lista de fornecedores */
const {
  data: response,
  isLoading: loading,
  error,
  get: fetch,
} = useGet<PaginatedResponse<Fornecedor>>()

const fornecedores = ref<Fornecedor[]>([])
const page = ref(0)
const filter = ref({
  nome: '',
  documento: '',
})

const fetchFornecedores = (page = 0) => {
  const strNome = filter.value.nome ? `nome=${filter.value.nome}&` : ''
  const strDocumento = filter.value.documento ? `documento=${filter.value.documento}&` : ''
  const filterValue = strNome + strDocumento

  fetch(`/fornecedores?${filterValue}page=${page}&size=10`).then(() => {
    fornecedores.value = response.value?.content || []
  })
}

const onPageChange = (event: DataTablePageEvent) => {
  fetchFornecedores(event.page)
  page.value = event.page || 0
}

const handleFornecedorList = (data: Fornecedor[] | undefined) => {
  fetchFornecedores(page.value)
  /* fornecedores.value = fornecedores.value.filter((x) => !data?.some((f) => f.id === x.id))
   */
}

/** Desvincular fornecedor */
const { del, isLoading: deleteLoading, error: deleteError } = useDelete<Fornecedor>()

const removerFornecedor = async (empresaId: string, fornecedorId: string) => {
  await del(`/empresas/${empresaId}/fornecedores?fornecedorIds=${fornecedorId}`).then(() => {
    if (!deleteError.value) {
      toast.add({
        severity: 'success',
        summary: `Fornecedor removido`,
        detail: `Fornecedor desvinculado com sucesso`,
        life: 3000,
      })
    }
    refetch(page.value)
  })
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
      <div class="fornecedores-list flex flex-col gap-3">
        <div class="header inline-flex gap-6 justify-between items-center">
          <span class="text-xl font-semibold">Fornecedores vinculados</span>
          <DialogButton
            ref="dialog"
            :button="{ label: 'Incluir fornecedor', variant: 'outlined' }"
            :dialog="{ header: 'Vincular fornecedor' }"
            @click="handleFornecedorList(data.fornecedores)"
          >
            <div class="dialog-container flex flex-col gap-4">
              <FornecedorList
                selectMode
                :error
                :registros="fornecedores"
                :loading
                :response
                :onPageChange
                :refetch
              />
              <div class="footer w-full inline-flex justify-end gap-2">
                <Button
                  label="Cancelar"
                  severity="secondary"
                  @click="console.log('cancelar')"
                  class="w-24"
                />
                <Button
                  label="Salvar"
                  class="w-24"
                  :disabled="loading"
                  :loading="loading"
                  @click="console.log('salvar')"
                />
              </div>
            </div>
          </DialogButton>
        </div>
        <DataTable
          :value="data.fornecedores"
          :pt="{
            root: 'min-w-[50rem]',
            column: {
              headerCell: '!bg-slate-200 !border-slate-300 !py-2 !px-2',
            },
          }"
        >
          <Column field="nome" header="Nome"></Column>
          <Column field="documento" header="Documento"></Column>
          <Column field="email" header="Email"></Column>
          <Column field="acao">
            <template #body="{ data: fornecedorData }: { data: Fornecedor }">
              <Button
                label="Desvincular fornecedor"
                severity="secondary"
                variant="outlined"
                size="small"
                @click="removerFornecedor(data.id, fornecedorData.id)"
                class="!text-red-800 !hover:!bg-red-200 hover:!border-red-200"
              />
            </template>
          </Column>
          <template #empty>Nenhum fornecedor vinculado</template>
        </DataTable>
      </div>
    </template>
  </EntityTable>
</template>
