## Инструкция для развертывания в Docker

1) Запустить `docker-compose up`

На этом этапе создадутся и запустятся 3 контейнера ShareItGateway, ShareItServer и ShareItDB

2) Далее необходимо накатить миграции Flyway на контейнер ShareItDB выполнив следующие команды:

    `docker build -t migration:1.0.0 ./migration`

Затем запустить данный образ:

    `docker run --rm --name ShareItMigration \  
    --network container:ShareItDB \
    -e FLYWAY_URL=jdbc:postgresql://db:5432/share \
    -e FLYWAY_USER=evgenii \
    -e FLYWAY_PASSWORD=12345678 \
    migration:1.0.0`

Проверить, создались ли таблицы в ShareItDB

   `docker exec -it ShareItDB psql -U evgenii -d share
   `

Приложение готово к работе по адресу http://localhost:8080

## Инструкция для развертывания в Minikube

1) Запустить Minikube

   `minikube start`
2) Выполнить команду 

   `eval $(minikube docker-env)`
3) Установить образы приложения в minikube командой 

   `docker compose build`  
4) Установить образ миграции в minikube командой 

   `docker build -t migration:1.0.0 ./migration`
5) Запустить чарт shareit

   `helm install shareit shareit-chart` 
6) Запустить чарт migration

   `helm install migration migration-chart`
7) После того как чарт накатил миграции на базу данных, можно проверить выполнив запрос в поде

   `kubectl exec -it <имя пода> -- /bin/bash`

   `psql -U evgenii -d share`

   `SELECT * FROM users;`

## Установка новой миграции (1 способ)
1) Добавляем файл миграции в папку `src/main/resources`
2) Пересобираем образ

   `docker build -t migration:1.0.0 ./migration`
3) Запускам чарт migration

   `helm upgrade --install migration ./migrations`
4) Миграция будет установлена на базу данны share сервиса db-service

## Установка новой миграции (2 способ)
1) Добавляем файл миграции в папку `src/main/resources`
2) Запустить скрипт 

   `./migration/migration.sh`

3) Миграция будет установлена на базу данных share сервиса db-service