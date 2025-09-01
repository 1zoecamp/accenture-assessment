<script setup lang="ts">
import { EntityTable } from '@/components'
</script>

<template>
  <EntityTable url="fornecedores">
    <template #columns>
      <Column field="nome" header="Nome / Razão Social" sortable style="min-width: 16rem"></Column>

      <Column field="documento" header="Documento" style="min-width: 10rem"></Column>

      <Column field="email" header="Email" style="min-width: 14rem"></Column>

      <Column header="Cidade" style="min-width: 12rem">
        <template #body="slotProps">
          <span>{{ slotProps.data.endereco.localidade }} - {{ slotProps.data.endereco.uf }}</span>
        </template>
      </Column>

      <Column header="Tipo" sortable field="tipoPessoa" style="min-width: 8rem">
        <template #body="slotProps">
          <span
            :class="[
              'px-2 py-1 text-xs font-semibold rounded-full',
              slotProps.data.tipoPessoa === 'PESSOA_FISICA'
                ? 'bg-blue-100 text-blue-800'
                : 'bg-green-100 text-green-800',
            ]"
          >
            {{ slotProps.data.tipoPessoa === 'PESSOA_FISICA' ? 'P. Física' : 'P. Jurídica' }}
          </span>
        </template>
      </Column>
    </template>
  </EntityTable>
</template>
