# Usar imagen oficial de OpenJDK
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app

# Copiar archivos de Maven y el wrapper
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline

# Copiar el código fuente y construir el jar
COPY src ./src
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Render asigna un puerto dinámico mediante la variable de entorno PORT
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=${PORT}"]