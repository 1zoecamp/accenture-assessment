<script setup lang="ts">
import type { CustomTableFormProps, Empresa, Fornecedor, PaginatedResponse } from '@/types'
import { defineEmits, ref } from 'vue'
import {
  useToast,
  type DataTableFilterEvent,
  type DataTableFilterMetaData,
  type DataTablePageEvent,
} from 'primevue'
import { EntityTable, DialogButton } from '@/components'
import { useDelete, useGet, usePost } from '@/composables/useApi'
import FornecedorList from '@/views/fornecedor/fragments/FornecedorList.vue'
import EmpresaForm from './EmpresaForm.vue'

/** Types */
type VincularFornecedorResponse = {
  empresa: Empresa
  avisos?: string[]
}
type VincularFornecedorBody = {
  fornecedorIds: string[]
}

/** Props, composables, emits */
const { refetch } = defineProps<CustomTableFormProps<Empresa>>()

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

const refetchFornecedor = (page = 0) => {
  fetchFornecedores(page)
  fornecedores.value = response.value?.content || []
}

const onPageChange = (event: DataTablePageEvent) => {
  fetchFornecedores(event.page)
  page.value = event.page || 0
}

const updateFilter = (e: DataTableFilterEvent) => {
  filter.value = {
    nome: (e.filters?.nome as DataTableFilterMetaData)?.value ?? '',
    documento: (e.filters?.documento as DataTableFilterMetaData).value ?? '',
  }
  fetchFornecedores()
}

/** Vincular fornecedores */
const fornecedoresSelecionados = ref<Fornecedor[]>()
const {
  post,
  data,
  isLoading: postLoading,
  error: postError,
} = usePost<VincularFornecedorResponse, VincularFornecedorBody>()
const handleVincularFornecedor = (empresa: Empresa) => {
  const body = {
    fornecedorIds: fornecedoresSelecionados.value?.map((f) => f.id) ?? [],
  }
  post(`/empresas/${empresa.id}/fornecedores`, body).then(() => {
    if (!postError.value) {
      if (data.value?.avisos) {
        toast.add({
          severity: 'warn',
          summary: `Aviso: falha ao vincular fornecedor`,
          detail: 'Erro durante a operação \n\n' + '-' + data.value.avisos.join('\n - '),
          life: 10000,
        })
      } else {
        toast.add({
          severity: 'success',
          summary: `Incluir fornecedor`,
          detail: `Fornecedor(es) vinculados com sucesso`,
          life: 3000,
        })
      }

      fornecedoresSelecionados.value = []
    }
    refetch(page.value)
  })
}

/** Desvincular fornecedor */
const { del, isLoading: deleteLoading, error: deleteError } = useDelete<Fornecedor>()

const desvincularFornecedor = async (empresaId: string, fornecedorId: string) => {
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

const dialogVisibility = ref(false)
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
            v-model="dialogVisibility"
            ref="dialog"
            :button="{ label: 'Incluir fornecedor' }"
            variant="outlined"
            severity="contrast"
            :dialog="{ header: 'Vincular fornecedor' }"
            @click="fetchFornecedores(page)"
          >
            <div class="dialog-container flex flex-col gap-4">
              <FornecedorList
                selectMode
                :error
                :registros="fornecedores"
                :loading
                :response
                :onPageChange
                :refetch="refetchFornecedor"
                @on-select="(f: Fornecedor[]) => (fornecedoresSelecionados = f)"
                @on-filter="(e: DataTableFilterEvent) => updateFilter(e)"
              />
              <div class="footer w-full inline-flex justify-end gap-2">
                <Button
                  label="Cancelar"
                  severity="secondary"
                  @click="dialogVisibility = !dialogVisibility"
                  class="w-24"
                />
                <Button
                  label="Salvar"
                  class="w-24"
                  :disabled="postLoading || !fornecedoresSelecionados"
                  v-tooltip.top="
                    !fornecedoresSelecionados ? 'Selecione ao menos um fornecedor' : null
                  "
                  :loading="postLoading"
                  @click="handleVincularFornecedor(data)"
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
                @click="desvincularFornecedor(data.id, fornecedorData.id)"
                class="!text-red-800 !hover:!bg-red-200 hover:!border-red-200 whitespace-nowrap"
              />
            </template>
          </Column>
          <template #empty>Nenhum fornecedor vinculado</template>
        </DataTable>
      </div>
    </template>
  </EntityTable>
</template>
