<script setup lang="ts">
import type { ButtonProps, DialogProps } from 'primevue'
import { ref } from 'vue'

const visible = ref(false)

defineProps<{ button: { label: string }; dialog: { header: string } }>()
</script>
<template>
  <Button :label="button.label" @click="visible = true" />
  <Dialog v-model:visible="visible" modal :header="dialog.header" :style="{ width: '60rem' }">
    <slot />
    <div class="flex justify-end gap-2">
      <Button type="button" label="Cancelar" severity="secondary" @click="visible = false" />
      <Button type="button" label="Salvar" @click="visible = false" />
    </div>
  </Dialog>
</template>
