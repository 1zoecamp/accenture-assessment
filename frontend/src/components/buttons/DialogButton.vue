<script setup lang="ts">
import type { ButtonProps } from 'primevue'
import { provide, ref } from 'vue'

defineProps<{
  button?: ButtonProps & { tooltip?: string }
  dialog: { header: string }
}>()

const visible = ref(false)
const emits = defineEmits(['open', 'close'])

const handleClick = () => {
  visible.value = true
  emits('open')
}

provide('closeDialog', () => (visible.value = false))
provide('openDialog', () => (visible.value = true))
</script>
<template>
  <Button
    :label="button?.label"
    v-tooltip.top="`${button?.tooltip}`"
    @click="handleClick()"
    v-bind="$attrs"
  />
  <Dialog
    v-model:visible="visible"
    modal
    :header="dialog.header"
    :style="{ width: '70rem' }"
    @after-hide="emits('close')"
  >
    <slot />
  </Dialog>
</template>
