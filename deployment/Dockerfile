FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp

# Copiar el archivo .jar a la raíz del contenedor (ajusta la ruta si es necesario)
COPY build/libs/*.jar FranchiseManager.jar

ENV JAVA_OPTS=" -Xshareclasses:name=cacheapp,cacheDir=/cache,nonfatal -XX:+UseContainerSupport -XX:MaxRAMPercentage=70 -Djava.security.egd=file:/dev/./urandom"

EXPOSE 8080

# Ejecutar el JAR
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /FranchiseManager.jar" ]