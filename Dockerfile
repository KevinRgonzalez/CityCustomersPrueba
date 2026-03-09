FROM eclipse-temurin:17-jre-focal

# Puerto estándar de Spring Boot
EXPOSE 8080

# Copiar el JAR generado
COPY target/*.jar app.jar

# Configuración de usuario no-root para seguridad en AKS
RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring:spring

ENTRYPOINT ["java", "-jar", "/app.jar"]
