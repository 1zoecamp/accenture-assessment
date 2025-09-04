<script setup lang="ts">
import { ViewGroupButton } from '@/components'
</script>

<template>
  <div
    class="app h-[calc(100dvh-3rem)] relative overflow-hidden m-6 p-6 flex flex-col gap-2 border border-slate-300 rounded-xl text-slate-800"
  >
    <div class="header w-full flex justify-between items-center">
      <h1 class="title text-3xl font-semibold">
        Sistema de Gerenciamento de Empresas e Fornecedores
      </h1>
      <span
        class="test-label p-2 rounded-md text-sm bg-purple-100 text-purple-700 whitespace-nowrap"
      >
        Teste Accenture - Desenvolvedor Full Stack
      </span>
    </div>
    <Divider />
    <ViewGroupButton />
    <div class="view pt-4">
      <router-view />
    </div>
  </div>
</template>
