FROM maven:3.9.9-eclipse-temurin-24
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

# novos paths copiados para dentro do container
#Tivemos que mudar isto porque quisemos manter as Migrations no restaurant-service
#decisao ainda por avaliar




COPY src ./src
COPY migrations ./migrations

CMD ["mvn", "spring-boot:run"]
