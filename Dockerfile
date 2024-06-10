# Usar uma imagem base do JDK para compilar e rodar a aplicação
FROM openjdk:17-jdk-alpine

# Definir um diretório de trabalho dentro do container
WORKDIR /app

# Copiar o arquivo JAR gerado para dentro do container
COPY target/Picpay-desafio-SNAPSHOT.jar app.jar

# Expor a porta que a aplicação irá rodar (ajuste conforme necessário)
EXPOSE 8080

# Definir o comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]

