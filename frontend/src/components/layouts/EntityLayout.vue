<script setup lang="ts" generic="T">
import DialogButton from '../buttons/DialogButton.vue'

type EntityLayoutProps = {
  entity: string
  title: string
}

defineProps<EntityLayoutProps>()
</script>
<template>
  <div class="header w-full inline-flex justify-between items-center gap-4 px-4 mt-2 mb-6">
    <h1 class="text-2xl sm:text-3xl font-bold text-gray-800">{{ title }}</h1>
    <DialogButton
      :button="{ label: `Cadastrar ${entity.toLowerCase()}` }"
      :dialog="{ header: `Cadastro de ${entity.toLowerCase()}` }"
    >
      <slot name="form" />
    </DialogButton>
  </div>
  <br />
  <slot name="list" />
</template>
