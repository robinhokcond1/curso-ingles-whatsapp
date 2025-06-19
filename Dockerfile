FROM eclipse-temurin:17-jdk

WORKDIR /app
COPY . /app

RUN ./gradlew build

CMD ["java", "-jar", "build/libs/curso-ingles-whatsapp-0.0.1-SNAPSHOT.jar"]
