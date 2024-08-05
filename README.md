# ExpenseMonitorService-TS

## О проекте

Expense Monitor Service - это приложение на базе Spring Boot для управления расходами. Оно использует PostgreSQL в качестве базы данных и предоставляет API для работы с лимитами и транзакциями.

## Ссылка на задеплойенную документацию Swagger: 
http://142.93.104.229:9990/swagger-ui/index.html

## Стек технологий

- Spring Boot
- PostgreSQL
- Docker
- Docker Compose

## Порт приложения
- 9990

## Установка и запуск

Для запуска приложения с помощью Docker Compose выполните следующие шаги:

### 1. Клонируйте репозиторий
git clone https://github.com/RamazanMamyrbek/ExpenseMonitorService-TS.git
### 2. Создайте базы данных для приложения:
####1. Db name - expense-monitor-service
####2. User - postgres
####3. Password - postgres
### 3. Запустите приложение(ExpenseMonitorServiceApplication.class)

# Пример запуска через docker-compose
### 1. Скачайте docker, docker-compose, mvn
### 2. Запустите команду в папке expense-monitor-service
```bash
mvn clean package
docker-compose up -d --build










