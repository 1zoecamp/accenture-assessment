import type { Ref } from 'vue';
import { ref } from 'vue';
import { AxiosError } from 'axios';
import apiClient from '@/services/axios';

/**
 * Composable para realizar requisições GET.
 * @template T - O tipo de dado esperado na resposta.
 * @template P - O tipo dos parâmetros da URL (query params).
 */
export function useGet<T, P extends Record<string, any> = Record<string, any>>() {
  const data: Ref<T | null> = ref(null);
  const isLoading: Ref<boolean> = ref(false);
  const error: Ref<AxiosError | null> = ref(null);

  const get = async (url: string, params?: P) => {
    isLoading.value = true;
    error.value = null;
    try {
      const response = await apiClient.get<T>(url, { params });
      data.value = response.data;
    } catch (err) {
      if (err instanceof AxiosError) {
        error.value = err;
      }
    } finally {
      isLoading.value = false;
    }
  };

  return { data, isLoading, error, get };
}

/**
 * Composable para realizar requisições POST.
 * @template T - O tipo de dado esperado na resposta (ex: o objeto criado).
 * @template P - O tipo do payload (corpo da requisição) enviado.
 */
export function usePost<T, P>() {
  const data: Ref<T | null> = ref(null);
  const isLoading: Ref<boolean> = ref(false);
  const error: Ref<AxiosError | null> = ref(null);

  const post = async (url: string, payload: P) => {
    isLoading.value = true;
    error.value = null;
    data.value = null; // Limpa dados anteriores
    try {
      const response = await apiClient.post<T>(url, payload);
      data.value = response.data;
    } catch (err) {
      if (err instanceof AxiosError) {
        error.value = err;
      }
    } finally {
      isLoading.value = false;
    }
  };

  return { data, isLoading, error, post };
}

/**
 * Composable para realizar requisições PUT.
 * @template T - O tipo de dado esperado na resposta (ex: o objeto atualizado).
 * @template P - O tipo do payload (corpo da requisição) enviado.
 */
export function usePut<T, P>() {
  const data: Ref<T | null> = ref(null);
  const isLoading: Ref<boolean> = ref(false);
  const error: Ref<AxiosError | null> = ref(null);

  const put = async (url: string, payload: P) => {
    isLoading.value = true;
    error.value = null;
    try {
      const response = await apiClient.put<T>(url, payload);
      data.value = response.data;
    } catch (err) {
      if (err instanceof AxiosError) {
        error.value = err;
      }
    } finally {
      isLoading.value = false;
    }
  };

  return { data, isLoading, error, put };
}


/**
 * Composable para realizar requisições DELETE.
 * @template T - O tipo de dado esperado na resposta (geralmente vazio ou uma mensagem de sucesso).
 */
export function useDelete<T>() {
  const data: Ref<T | null> = ref(null);
  const isLoading: Ref<boolean> = ref(false);
  const error: Ref<AxiosError | null> = ref(null);

  const del = async (url: string) => {
    isLoading.value = true;
    error.value = null;
    try {
      const response = await apiClient.delete<T>(url);
      data.value = response.data;
    } catch (err) {
      if (err instanceof AxiosError) {
        error.value = err;
      }
    } finally {
      isLoading.value = false;
    }
  };

  return { data, isLoading, error, del };
}
