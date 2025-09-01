import axios, {
    type AxiosInstance, 
    type AxiosResponse,
    type AxiosError
} from 'axios';

// Cria uma instância do Axios com configurações base
const apiClient: AxiosInstance = axios.create({
    baseURL: 'http://localhost:8080',
    headers: {
        'Content-Type': 'application/json',
    },
});

// TODO: verificar se essa estrutura pode ser utilizada no tratamento global de erros
apiClient.interceptors.response.use(
    (response: AxiosResponse) => {
        return response;
    },
    (error: AxiosError) => {
        if (error.response) {
            console.error(error);
        }
        return Promise.reject(error);
    }
);

export default apiClient;