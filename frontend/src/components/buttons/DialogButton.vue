<script setup lang="ts">
import type { ButtonProps } from 'primevue'
import { provide, ref, watch } from 'vue'

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

/** Controle de fechamento da modal */
provide('closeDialog', () => (visible.value = false))

// Alternativa
const visibility = defineModel<boolean>()
watch(
  () => visibility.value,
  (newValue) => {
    if (newValue == false) visible.value = false
  },
)
</script>
<template>
  <Button
    :label="button?.label"
    v-tooltip.top="button?.tooltip ?? null"
    @click="handleClick()"
    v-bind="$attrs"
  />
  <Dialog
    v-model:visible="visible"
    modal
    :header="dialog.header"
    :draggable="false"
    class="!w-[90%] xl:!w-[70rem]"
    :style="{ width: '70rem' }"
    @after-hide="emits('close')"
    @show="visibility = true"
  >
    <slot />
  </Dialog>
</template>
