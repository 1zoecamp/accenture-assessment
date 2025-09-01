<script setup lang="ts">
import { EntityLayout } from '@/components'
import FornecedorList from './fragments/FornecedorList.vue'
</script>

<template>
  <EntityLayout entity="Fornecedor" title="Lista de Fornecedores">
    <FornecedorList />
  </EntityLayout>
</template>
