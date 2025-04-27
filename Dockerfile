FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp

# Copiar el archivo .jar a la raíz del contenedor
COPY applications/app-service/build/libs/*.jar FranchiseManager.jar

# Eliminamos la opción -Xshareclasses, que no es compatible con Temurin/Hotspot
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=70 -Djava.security.egd=file:/dev/./urandom"

EXPOSE 8080

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /FranchiseManager.jar" ]
