# -----------------------------------------------------------------------------
# ESTÁGIO 1: Build da Aplicação com Maven e JDK 17
#
# Este estágio usa uma imagem completa com Maven e JDK para compilar o projeto
# e gerar o arquivo .jar.
# -----------------------------------------------------------------------------
FROM maven:3.9-eclipse-temurin-17 AS build

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copia o pom.xml para baixar as dependências primeiro (otimização de cache do Docker)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o resto do código-fonte do projeto
COPY src ./src

# Compila o projeto e cria o pacote .jar, pulando os testes
RUN mvn package -DskipTests


# -----------------------------------------------------------------------------
# ESTÁGIO 2: Imagem Final de Execução
#
# Este estágio usa uma imagem leve, apenas com o Java Runtime (JRE),
# para executar a aplicação. A imagem final não contém o Maven nem o JDK.
# -----------------------------------------------------------------------------
FROM eclipse-temurin:17-jre-alpine

# Define o diretório de trabalho
WORKDIR /app

# Copia o arquivo .jar gerado no estágio anterior (build) para a imagem final
# e renomeia para app.jar para facilitar a execução.
COPY --from=build /app/target/assessment-0.0.1-SNAPSHOT.jar app.jar

# Expõe a porta 8080, que é a porta padrão do Spring Boot
EXPOSE 8080

# Comando que será executado quando o contêiner iniciar
ENTRYPOINT ["java", "-jar", "app.jar"]