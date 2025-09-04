# -----------------------------------------------------------------------------
# ESTÁGIO 1: Build da Aplicação com Node.js
#
# Este estágio usa uma imagem do Node.js para instalar as dependências
# e compilar os arquivos de produção (HTML, CSS, JS).
# -----------------------------------------------------------------------------
FROM node:22-alpine AS build

# Define o diretório de trabalho
WORKDIR /app

# Copia o package.json e o package-lock.json (se existir)
# A wildcard (*) garante que ambos sejam copiados.
COPY package*.json ./

# Instala todas as dependências (incluindo devDependencies necessárias para o build)
RUN npm install

# Copia todo o resto do código-fonte do frontend
COPY . .

# Executa o script de build definido no package.json
RUN npm run build


# -----------------------------------------------------------------------------
# ESTÁGIO 2: Imagem Final de Produção com Nginx
#
# Este estágio usa uma imagem superleve do Nginx para servir os arquivos
# estáticos que foram gerados no estágio anterior.
# -----------------------------------------------------------------------------
FROM nginx:stable-alpine

# Copia os arquivos de produção da pasta /app/dist do estágio 'build'
# para a pasta padrão do Nginx que serve conteúdo web.
COPY --from=build /app/dist /usr/share/nginx/html

# Copia o arquivo de configuração customizado do Nginx
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Expõe a porta 80, que o Nginx usa por padrão
EXPOSE 80

# Comando para iniciar o servidor Nginx em modo 'foreground'
CMD ["nginx", "-g", "daemon off;"]