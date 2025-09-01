<script setup lang="ts">
import { EntityLayout } from '@/components'
import EmpresaList from './fragments/EmpresaList.vue'
</script>

<template>
  <EntityLayout entity="Empresa" title="Lista de Empresas">
    <EmpresaList />
  </EntityLayout>
</template>
