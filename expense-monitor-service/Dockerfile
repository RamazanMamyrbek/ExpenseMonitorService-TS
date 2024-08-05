FROM openjdk:21-oracle
COPY target/*.jar expense-monitor-service.jar
EXPOSE 9990
ENTRYPOINT ["sh", "-c", "sleep 10 && java -jar expense-monitor-service.jar"]