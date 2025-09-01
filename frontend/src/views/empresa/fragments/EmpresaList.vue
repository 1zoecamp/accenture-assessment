<script setup lang="ts">
import { EntityTable } from '@/components'
</script>

<template>
  <EntityTable url="empresas">
    <template #columns>
      <Column field="nomeFantasia" header="Nome Fantasia" sortable style="min-width: 16rem">
      </Column>
      <Column field="cnpj" header="CNPJ" style="min-width: 12rem"> </Column>
      <Column field="endereco.cep" header="CEP" style="min-width: 10rem"> </Column>
    </template>
  </EntityTable>
</template>
