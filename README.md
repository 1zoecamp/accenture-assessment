# Desafio Full-Stack
Desafio para alocação como Desenvolvedor Full Stack na Accenture Brasil

## Tecnologias utilizadas
Este projeto é dividido em duas partes principais: um frontend construído com Vue.js e um backend com Spring Boot.

| Componente | Tecnologia | Versão |
| :--- | :--- | :--- |
| **Frontend** | Vue.js | 3 |
| | Vite | 7.1.3 |
| | Tailwind CSS | 4.1 |
| | PrimeVue | 4 |
| | Node.js | 22.15.0 |
| **Backend** | Java | 17 |
| | Spring Boot | 3.5.5 |
| | Maven | 4.x |
| | Swagger (OpenAPI) | |
| **Banco de Dados** | PostgreSQL | 16.3 |

<br />

## Instalação 
### Opção 1: utilizando o Dockerfile
### Opção 2: instalando manualmente
Antes de iniciar, verifique se as seguintes ferramentas estão instaladas:
- **Git**
- **Node.js** v.22.15.0
- **JDK (Java Development Kit)** v.17
- **Apache Maven** v.4.0.0 ou superior
- **PostgreSQL** v.16.3

##### Configuração do ambiente
Para configurar o ambiente, as etapas são:
1. **Clonar o repositório**
```bash
git clone [https://github.com/1zoecamp/accenture-assessment.git](https://github.com/1zoecamp/accenture-assessment.git)
cd accenture-assessment
```

2. **Configurar o banco de dados**
Você precisará de uma instância do PostgreSQL rodando localmente.
  a. Conecte-se ao seu servidor PostgreSQL (usando psql, DBeaver, ou outra ferramenta de sua preferência).
  b. Crie o banco de dados para a aplicação:
```SQL
CREATE DATABASE accenture_assessment;
```

3. **Configurar o backend**
  a. Navegue até o diretório `accenture-assessment/backend`
  b. Verifique se o arquivo `application.properties` contém as configurações corretas em relação ao seu banco

<br />

## Executar a aplicação
1. **Iniciar o backend**
Abra um terminal e navegue até a pasta do backend:

```bash
# A partir da raiz do projeto (accenture-assessment)
cd backend

# Instalação das dependências e compilação do projeto
mvn clean install

# Inicialização da aplicação
mvn spring-boot:run
```
O servidor backend estará rodando em http://localhost:8080.

2. Iniciar o frontend
Abra um novo terminal e navegue até a pasta do frontend:
``` bash
# A partir da raiz do projeto (accenture-assessment)
cd frontend

# Instalação das dependências do Node.js
npm install

# Inicialização do servidor de desenvolvimento
npm run dev
```
O servidor de desenvolvimento do frontend estará disponível em http://localhost:5173 (ou outra porta indicada pelo Vite). Abra este endereço no seu navegador para ver a aplicação.
