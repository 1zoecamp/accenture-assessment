<script setup lang="ts">
import type { FormInstance, FormSubmitEvent } from '@primevue/forms'
import type { Empresa, ViaCepResponse } from '@/types'
import { inject, onMounted, ref, useTemplateRef, watch } from 'vue'
import { z } from 'zod'
import { useToast } from 'primevue/usetoast'
import { zodResolver } from '@primevue/forms/resolvers/zod'
import { useGet, usePost, usePut } from '@/composables/useApi'
import { isValidCNPJ } from '@/utils'

/** Types */
type EmpresaData = Omit<Empresa, 'id'>

/** Props, composables, emits, inject */
const {
  initialData,
  refetch,
  editMode = false,
} = defineProps<{
  refetch: (page?: number) => void
  initialData?: EmpresaData | Empresa
  editMode?: boolean
}>()

const formTemplateRef = useTemplateRef<FormInstance>('formRef')
const toast = useToast()

const { data: response, isLoading: cepLoading, get: fetch } = useGet<ViaCepResponse>()
const { isLoading: postLoading, error: postError, post } = usePost<EmpresaData>()
const { isLoading: putLoading, error: putError, put } = usePut<EmpresaData>()

const emits = defineEmits(['formDelete'])

const closeDialog = inject('closeDialog') as () => void

/** Form data */
const formData = ref<EmpresaData>({
  cnpj: '',
  nomeFantasia: '',
  endereco: {
    cep: '',
    logradouro: '',
    complemento: '',
    bairro: '',
    localidade: '',
    uf: '',
  },
})

onMounted(() => {
  if (initialData) {
    formTemplateRef.value?.setValues(initialData)
    formTemplateRef.value?.setFieldValue('endereco.cep', initialData.endereco.cep)
    formTemplateRef.value?.setFieldValue('endereco.complemento', initialData.endereco.complemento)
  }
})

/** Form config */
// CEP e endereço
const cepError = ref<boolean>(false)
const fetchCEP = (form: any, cep: string | undefined) => {
  if (cep && cep.length === 8) {
    fetch(`https://viacep.com.br/ws/${formData.value.endereco.cep}/json/`).then(() => {
      if (response.value) {
        if (response.value.erro) {
          cepError.value = true
        } else {
          cepError.value = false
        }

        ;((form.endereco.logradouro.value = response.value.logradouro),
          (form.endereco.bairro.value = response.value.bairro),
          (form.endereco.localidade.value = response.value.localidade),
          (form.endereco.uf.value = response.value.uf))
      }
    })
  }
}

watch(
  () => formData.value.endereco.cep,
  (newCep) => {
    if (newCep.length === 0) {
      cepError.value = false
      formData.value.endereco.logradouro = ''
      formData.value.endereco.complemento = ''
      formData.value.endereco.bairro = ''
      formData.value.endereco.localidade = ''
      formData.value.endereco.uf = ''
    }
  },
)

/** Validação  */
const resolver = ref(
  zodResolver(
    z.object({
      nomeFantasia: z.string().min(1, { message: 'O nome fantasia é obrigatório' }),
      cnpj: z
        .string()
        .min(1, { message: 'O CNPJ é obrigatório' })
        .transform((str) => str.replace(/\D/g, ''))
        .refine(
          (str) => {
            if (str.length == 14) {
              return isValidCNPJ(str)
            }
            return true
          },
          { message: 'CNPJ inválido' },
        ),
      endereco: z.object({
        cep: z
          .string({ message: 'O CEP é obrigatório' })
          .transform((val) => val.replace(/\D/g, '')),
        logradouro: z.string(),
        bairro: z.string(),
        localidade: z.string(),
        uf: z.string(),
        complemento: z.string().optional(),
      }),
    }),
  ),
)

/** Submit handler */
const submitActions = (success: boolean, edit: boolean, e: FormSubmitEvent) => {
  const empresa = e.values as EmpresaData
  if (success) {
    toast.add({
      severity: 'success',
      summary: edit ? 'Cadastro atualizado com sucesso' : 'Empresa cadastrado com sucesso',
      detail: `Cadastro do empresa ${empresa.nomeFantasia} (${empresa.cnpj}) ${edit ? 'atualizado' : 'criado'} realizado com sucesso`,
      life: 3000,
    })
    e.reset()
    closeDialog()
    refetch()
  } else {
    toast.add({
      severity: 'error',
      summary: 'Algo deu errado',
      detail: edit ? putError.value?.response?.data : postError.value?.response?.data,
      life: 10000,
    })
  }
}
const onFormSubmit = (e: FormSubmitEvent) => {
  if (e.valid) {
    if (editMode) {
      put(`/empresas/${(initialData as Empresa).id}`, e.values as EmpresaData).then(() => {
        submitActions(putError.value ? false : true, true, e)
      })
    } else {
      post(`/empresas`, e.values as EmpresaData).then(() => {
        submitActions(postError.value ? false : true, false, e)
      })
    }
  }
}
</script>
<template>
  <Form
    ref="formRef"
    v-slot="$form"
    :resolver
    :initial-values="formData"
    validateOnValueUpdate
    validateOnBlur
    @submit="onFormSubmit"
    class="flex flex-col gap-4 w-full"
  >
    <!-- Informações gerais -->
    <span class="infos-gerais--label font-lg font-semibold text-slate-600">Informações gerais</span>
    <div
      class="infos-gerais--container grid grid-cols-12 gap-y-4 gap-x-2 p-4 rounded-lg bg-slate-100"
    >
      <div class="nome-fantasia-input col-span-9 w-full flex flex-col gap-1">
        <IftaLabel>
          <InputText
            id="nomeFantasia_id"
            name="nomeFantasia"
            type="text"
            fluid
            v-model="formData.nomeFantasia"
          />
          <label for="nomeFantasia_id">Nome fantasia</label>
        </IftaLabel>
        <Message v-if="$form.nomeFantasia?.invalid" severity="error" size="small" variant="simple">
          {{ $form.nomeFantasia.error.message }}
        </Message>
      </div>
      <div class="cnpj-input col-span-3 w-full flex flex-col gap-1">
        <IftaLabel>
          <InputMask
            id="cnpj_id"
            name="cnpj"
            fluid
            mask="99.999.999/9999-99"
            placeholder="__.___.___/____-__"
            :autoClear="false"
            v-model="formData.cnpj"
          />
          <label for="documento_id">CNPJ</label>
        </IftaLabel>
        <Message v-if="$form.cnpj?.invalid" severity="error" size="small" variant="simple">
          {{ $form.cnpj.error.message }}
        </Message>
      </div>
    </div>

    <!-- Endereço -->
    <span class="endereco--label font-lg font-semibold text-slate-600 pt-4">Endereço</span>
    <div class="endereco--container flex flex-col gap-4 p-4 rounded-lg bg-slate-100">
      <Message
        severity="info"
        icon="pi pi-info-circle"
        :pt="{ content: 'text-indigo-700 bg-indigo-100 !border-indigo-400' }"
      >
        Digite o CEP para preencher as demais informações do endereço. Inclua o complemento se
        necessário
      </Message>
      <div class="endereco--form grid grid-cols-12 gap-y-4 gap-x-2">
        <div class="cep-input col-span-3 w-full flex flex-col gap-1">
          <IconField>
            <IftaLabel>
              <InputMask
                id="cep_id"
                name="endereco.cep"
                mask="99999-999"
                unmask
                fluid
                v-model="formData.endereco.cep"
                @update:model-value="(cep: string | undefined) => fetchCEP($form, cep)"
              />
              <label for="cep_id">CEP</label>
            </IftaLabel>
            <InputIcon v-if="cepLoading" class="pi pi-spin pi-spinner" />
          </IconField>
          <Message
            v-if="cepError || $form.cep?.invalid"
            severity="error"
            size="small"
            variant="simple"
          >
            {{ cepError ? 'CEP inválido' : $form.cep.error.message }}
          </Message>
        </div>
        <div class="logradouro-input col-span-9 w-full flex flex-col gap-1">
          <IftaLabel>
            <InputText
              id="logradouro_id"
              name="endereco.logradouro"
              type="text"
              fluid
              readonly
              :pt="{ root: '!bg-slate-50' }"
              v-model="formData.endereco.logradouro"
            />
            <label for="logradouro_id">Logradouro</label>
          </IftaLabel>
          <Message v-if="$form.logradouro?.invalid" severity="error" size="small" variant="simple">
            {{ $form.logradouro.error.message }}
          </Message>
        </div>
        <div class="bairro-input col-span-5 w-full flex flex-col gap-1">
          <IftaLabel>
            <InputText
              id="bairro_id"
              name="endereco.bairro"
              type="text"
              fluid
              readonly
              :pt="{ root: '!bg-slate-0' }"
              v-model="formData.endereco.bairro"
            />
            <label for="bairro_id">Bairro</label>
          </IftaLabel>
          <Message v-if="$form.bairro?.invalid" severity="error" size="small" variant="simple">
            {{ $form.bairro.error.message }}
          </Message>
        </div>
        <div class="localidade-input col-span-5 w-full flex flex-col gap-1">
          <IftaLabel>
            <InputText
              id="localidade_id"
              name="endereco.localidade"
              type="text"
              fluid
              readonly
              :pt="{ root: '!bg-slate-0' }"
              v-model="formData.endereco.localidade"
            />
            <label for="localidade_id">Município</label>
          </IftaLabel>
          <Message v-if="$form.localidade?.invalid" severity="error" size="small" variant="simple">
            {{ $form.localidade.error.message }}
          </Message>
        </div>
        <div class="uf-input col-span-2 w-full flex flex-col gap-1">
          <IftaLabel>
            <InputText
              id="uf_id"
              name="endereco.uf"
              type="text"
              fluid
              readonly
              :pt="{ root: '!bg-slate-0' }"
              v-model="formData.endereco.uf"
            />
            <label for="uf_id">UF</label>
          </IftaLabel>
          <Message v-if="$form.uf?.invalid" severity="error" size="small" variant="simple">
            {{ $form.uf.error.message }}
          </Message>
        </div>
        <div class="complemento-input col-span-12 w-full flex flex-col gap-1">
          <IftaLabel>
            <InputText
              id="complemento_id"
              name="endereco.complemento"
              type="text"
              fluid
              v-model="formData.endereco.complemento"
            />
            <label for="complemento_id">Complemento</label>
          </IftaLabel>
          <Message v-if="$form.complemento?.invalid" severity="error" size="small" variant="simple">
            {{ $form.complemento.error.message }}
          </Message>
        </div>
      </div>
    </div>

    <!-- Footer -->
    <div
      :class="[
        'form--footer w-full inline-flex gap-2',
        editMode ? 'justify-between' : 'justify-end',
      ]"
    >
      <Button
        v-if="editMode"
        type="button"
        label="Excluir"
        variant="outlined"
        severity="danger"
        @click="emits('formDelete')"
        class="w-24"
      />
      <div class="right--group inline-flex gap-2">
        <Button
          type="button"
          label="Cancelar"
          severity="secondary"
          @click="closeDialog()"
          class="w-24"
        />
        <Button
          type="submit"
          label="Salvar"
          class="w-24"
          :disabled="cepLoading || postLoading || !$form.valid"
          v-tooltip.top="!$form.valid ? 'Preencha os campos corretamente antes de continuar' : ''"
          :loading="postLoading || putLoading"
        />
      </div>
    </div>
  </Form>
</template>
