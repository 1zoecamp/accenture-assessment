<script setup lang="ts">
import type { Fornecedor, PaginatedResponse } from '@/types'
import { onMounted, computed } from 'vue'
import { useGet } from '@/composables/useApi'

const {
  data: response,
  isLoading,
  error,
  get: fetchFornecedores,
} = useGet<PaginatedResponse<Fornecedor>>()

onMounted(() => {
  fetchFornecedores('/fornecedores')
})

const fornecedores = computed(() => response.value?.content || [])
</script>

<template>
  <div class="fornecedor-container">
    <h1>Lista de Fornecedores</h1>

    <div v-if="isLoading" class="loading">Carregando fornecedores...</div>

    <div v-else-if="error" class="error">
      <h3>Ocorreu um erro ao buscar os dados</h3>
      <p>{{ error.message }}</p>
      <pre>{{ error.response?.data }}</pre>
    </div>

    <div v-else-if="response">
      <div class="pagination-info">
        <p>
          Página {{ response.pageNumber + 1 }} de {{ response.totalPages }} | Exibindo
          {{ fornecedores.length }} de {{ response.totalElements }} fornecedores.
        </p>
      </div>

      <table class="fornecedor-table">
        <thead>
          <tr>
            <th>Nome / Razão Social</th>
            <th>Documento</th>
            <th>Email</th>
            <th>Cidade</th>
            <th>Tipo</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="fornecedor in fornecedores" :key="fornecedor.id">
            <td>{{ fornecedor.nome }}</td>
            <td>{{ fornecedor.documento }}</td>
            <td>{{ fornecedor.email }}</td>
            <td>{{ fornecedor.endereco.localidade }} - {{ fornecedor.endereco.uf }}</td>
            <td>{{ fornecedor.tipoPessoa === 'PESSOA_FISICA' ? 'P. Física' : 'P. Jurídica' }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<style scoped>
.fornecedor-container {
  font-family: sans-serif;
  padding: 20px;
}
.loading,
.error {
  padding: 20px;
  border-radius: 8px;
  text-align: center;
}
.loading {
  background-color: #eef;
}
.error {
  background-color: #fdd;
  color: #c00;
}
.pagination-info {
  margin-bottom: 16px;
  color: #555;
}
.fornecedor-table {
  width: 100%;
  border-collapse: collapse;
}
.fornecedor-table th,
.fornecedor-table td {
  border: 1px solid #ddd;
  padding: 12px;
  text-align: left;
}
.fornecedor-table th {
  background-color: #f2f2f2;
}
.fornecedor-table tbody tr:nth-child(even) {
  background-color: #f9f9f9;
}
</style>
