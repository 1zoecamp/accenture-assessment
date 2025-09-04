# Desafio Full-Stack
Desafio para alocação como Desenvolvedor Full Stack na Accenture Brasil

## Tecnologias utilizadas
Este projeto é dividido em duas partes principais: um frontend construído com Vue.js e um backend com Spring Boot.

| Componente | Tecnologia | Versão |
| :--- | :--- | :--- |
| **Frontend** | Vue.js | 3 |
| | Vite | 7.1.3 |
| | Vue Router | 4.5.1 |
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
#### Pré-requisitos
-   **[Git](https://git-scm.com/book/pt-br/v2/Começando-Instalando-o-Git)** 
-   **[Docker e Docker Compose](https://docs.docker.com/desktop)**
  
#### Executar a aplicação
As etapas são:
1. **Clonar o repositório**
```bash
git clone https://github.com/1zoecamp/accenture-assessment.git
cd accenture-assessment
```

2. **Subir os containers**
```bash
docker-compose up --build
```
<br />

**Nota:** para parar a aplicação e remover os containers, pressione Ctrl + C no terminal onde o compose está rodando e depois execute:
```bash
docker-compose down
```

<br />

### Opção 2: instalando manualmente
#### Pré-requisitos
- **[Git](https://git-scm.com/book/pt-br/v2/Começando-Instalando-o-Git)**
- **[NPM](https://docs.npmjs.com/downloading-and-installing-node-js-and-npm)** v.10.9.2
- **[Node.js](https://docs.npmjs.com/downloading-and-installing-node-js-and-npm)** v.22.15.0
- **[JDK](https://www.oracle.com/java/technologies/javase/jdk17-0-13-later-archive-downloads.html) (Java Development Kit)** v.17
- **[Apache Maven](https://maven.apache.org/install.html)** v.3.9.8
- **[PostgreSQL](https://www.postgresql.org/download)** v.16.3

#### Configuração do ambiente
Para configurar o ambiente, as etapas são:
1. **Clonar o repositório**
```bash
git clone https://github.com/1zoecamp/accenture-assessment.git
cd accenture-assessment
```

2. **Configurar o banco de dados**
   
Você precisará de uma instância do PostgreSQL rodando localmente.
- Conecte-se ao seu servidor PostgreSQL (usando psql, DBeaver, ou outra ferramenta de sua preferência).
- Crie o banco de dados para a aplicação:
```SQL
CREATE DATABASE accenture_assessment;
```

3. **Configurar o backend**
- Navegue até o diretório `accenture-assessment/backend`
- Verifique se o arquivo `application.properties` contém as configurações corretas em relação ao seu banco

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

## Acesso à aplicação 
Após a instalação, a aplicação estará disponível nos seguintes endereços:
- Frontend: http://localhost:5173
- Backend (API): http://localhost:8080
- Documentação da API (Swagger): http://localhost:8080/swagger-ui.html
