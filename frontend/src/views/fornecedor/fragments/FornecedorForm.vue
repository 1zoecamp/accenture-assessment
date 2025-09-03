<script setup lang="ts">
import type { FormInstance, FormSubmitEvent } from '@primevue/forms'
import type { FormError, Fornecedor } from '@/types'
import { computed, inject, onMounted, ref, useTemplateRef, watch } from 'vue'
import dayjs from 'dayjs'
import { z } from 'zod'
import { useToast } from 'primevue/usetoast'
import { zodResolver } from '@primevue/forms/resolvers/zod'
import { useGet, usePost, usePut } from '@/composables/useApi'
import { isValidCNPJ, isValidCPF } from '@/utils'

/** Types */
type FornecedorData = Omit<Fornecedor, 'id'>
type ViaCepResponse = {
  cep: string
  logradouro: string
  complemento: string
  bairro: string
  localidade: string
  uf: string
  ibge: string
  gia: string
  ddd: string
  siafi: string
  erro?: boolean
}

/** Props e Emits */
const {
  initialData,
  refetch,
  editMode = false,
} = defineProps<{
  refetch: (page?: number) => void
  initialData?: (FornecedorData | Fornecedor) & { tipoDocumento?: string }
  editMode?: boolean
}>()

/** Composables, emits */
const formTemplateRef = useTemplateRef<FormInstance>('formRef')
const toast = useToast()

const { data: response, isLoading: cepLoading, get: fetch } = useGet<ViaCepResponse>()
const { isLoading: postLoading, error: postError, post } = usePost<FornecedorData>()
const { isLoading: putLoading, error: putError, put } = usePut<FornecedorData>()

const emits = defineEmits(['formDelete'])

/** Inject/provide */
const closeDialog = inject('closeDialog') as () => void

/** Form data */
const formData = ref<FornecedorData>({
  documento: '',
  nome: '',
  email: '',
  rg: '',
  dataNascimento: null,
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
    formTemplateRef.value?.setFieldValue(
      'dataNascimento',
      dayjs(initialData.dataNascimento).toDate(),
    )
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

// Documento
const tiposDocumento = ref([
  { label: 'CPF', value: 'cpf' },
  { label: 'CNPJ', value: 'cnpj' },
])

const defineTipoDocumento = () => {
  if (initialData) {
    return initialData.documento.length === 14 ? tiposDocumento.value[1] : tiposDocumento.value[0]
  }
  return tiposDocumento.value[0]
}
const tipoDocumento = ref(defineTipoDocumento())
const documentoMask = computed(() => {
  if (tipoDocumento.value.value === 'cnpj') {
    return '99.999.999/9999-99'
  }
  return '999.999.999-99'
})
const documentoPlaceholder = computed(() => {
  if (tipoDocumento.value.value === 'cnpj') {
    return '__.___.___/____-__'
  }
  return '__.___.___-__'
})

/** Validação  */
const resolver = ref(
  zodResolver(
    z.object({
      nome: z.string().min(1, { message: 'O nome é obrigatório' }),
      documento: z
        .string()
        .min(1, { message: 'O documento é obrigatório' })
        .transform((str) => str.replace(/\D/g, ''))
        .refine(
          (str) => {
            if (str.length == 14) {
              return isValidCNPJ(str)
            }
            if (str.length == 11) {
              return isValidCPF(str)
            }
            return true
          },
          { message: 'CPF/CNPJ inválido' },
        ),
      email: z.email({ message: 'Email inválido' }),
      dataNascimento: z
        .date()
        .nullable()
        .optional()
        .refine(
          (val) => {
            if (tipoDocumento.value.value === 'cpf' && !val) {
              return false
            }
            return true
          },
          { message: 'A data de nascimento é obrigatória' },
        )
        .transform((str) => (str ? dayjs(str).format('YYYY-MM-DD') : null)),
      rg: z
        .string()
        .nullable()
        .optional()
        .refine(
          (val) => {
            if (tipoDocumento.value.value === 'cpf' && val?.length === 0) {
              return false
            }
            return true
          },
          { message: 'O RG é obrigatório' },
        )
        .transform((str) => (str === '' ? null : str)),
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
  const fornecedor = e.values as FornecedorData
  if (success) {
    toast.add({
      severity: 'success',
      summary: edit ? 'Cadastro atualizado com sucesso' : 'Fornecedor cadastrado com sucesso',
      detail: `Cadastro do fornecedor ${fornecedor.nome} (${fornecedor.documento}) ${edit ? 'atualizado' : 'criado'} realizado com sucesso`,
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
      put(`/fornecedores/${(initialData as Fornecedor).id}`, e.values as FornecedorData).then(
        () => {
          submitActions(putError.value ? false : true, true, e)
        },
      )
    } else {
      post(`/fornecedores`, e.values as FornecedorData).then(() => {
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
    <span class="infos-gerais--label font-lg font-semibold text-slate-600">Informações gerais</span>
    <div
      class="infos-gerais--container grid grid-cols-12 gap-y-4 gap-x-2 p-4 rounded-lg bg-slate-100"
    >
      <div class="nome-input col-span-12 w-full flex flex-col gap-1">
        <IftaLabel>
          <InputText id="nome_id" name="nome" type="text" fluid v-model="formData.nome" />
          <label for="nome_id">Nome</label>
        </IftaLabel>
        <Message v-if="$form.nome?.invalid" severity="error" size="small" variant="simple">
          {{ $form.nome.error.message }}
        </Message>
      </div>
      <div class="tipo-documento-input col-span-2 w-full flex flex-col gap-1">
        <IftaLabel>
          <Select
            name="tipo_documento"
            id="tipo_documento"
            v-model="tipoDocumento"
            :options="tiposDocumento"
            optionLabel="label"
            placeholder="Selecione o tipo"
            fluid
          />
          <label for="tipo_documento">Tipo de Documento</label>
        </IftaLabel>
      </div>
      <div class="documento-input col-span-3 w-full flex flex-col gap-1">
        <IftaLabel>
          <InputMask
            id="documento_id"
            name="documento"
            fluid
            :mask="documentoMask"
            :placeholder="documentoPlaceholder"
            :autoClear="false"
            v-model="formData.documento"
          />
          <label for="documento_id">CPF/CNPJ</label>
        </IftaLabel>
        <Message v-if="$form.documento?.invalid" severity="error" size="small" variant="simple">
          {{ $form.documento.error.message }}
        </Message>
      </div>
      <div class="email-input col-span-7 w-full flex flex-col gap-1">
        <IftaLabel>
          <InputText id="email_id" name="email" type="text" fluid v-model="formData.email" />
          <label for="email_id">Email</label>
        </IftaLabel>
        <Message v-if="$form.email?.invalid" severity="error" size="small" variant="simple">
          {{ $form.email.error.message }}
        </Message>
      </div>
      <div
        class="pessoa-fisica-group col-span-12 grid grid-cols-12 gap-2"
        v-if="tipoDocumento.value === 'cpf'"
      >
        <div class="data-nascimento-input col-span-6">
          <IftaLabel>
            <DatePicker
              id="data_nascimento_id"
              name="dataNascimento"
              dateFormat="dd/mm/yy"
              v-model="formData.dataNascimento"
              showIcon
              fluid
              iconDisplay="input"
            />
            <label for="data_nascimento_id">Data de nascimento</label>
          </IftaLabel>
          <Message
            v-if="$form.dataNascimento?.invalid"
            severity="error"
            size="small"
            variant="simple"
          >
            {{ $form.dataNascimento.error.message }}
          </Message>
        </div>
        <div class="rg-input col-span-6 w-full flex flex-col gap-1">
          <IftaLabel>
            <InputText id="rg_id" name="rg" type="text" fluid v-model="formData.rg" />
            <label for="rg_id">RG</label>
          </IftaLabel>
          <Message v-if="$form.rg?.invalid" severity="error" size="small" variant="simple">
            {{ $form.rg.error.message }}
          </Message>
        </div>
      </div>
    </div>
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
