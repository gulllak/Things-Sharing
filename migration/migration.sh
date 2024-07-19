#!/bin/bash
echo "Выполняется 'mvn clean package' для модуля migration..."
mvn -f ./migration/pom.xml clean package
if [ $? -ne 0 ]; then
    echo "Ошибка"
    exit 1
fi

echo "Обновляю Docker-образ"
docker build -t migration:1.0.0 ./migration
if [ $? -ne 0 ]; then
    echo "Ошибка"
    exit 1
fi

echo "Обновляю Helm-чарт"
helm upgrade --install migration migration-chart
if [ $? -ne 0 ]; then
    echo "Ошибка"
    exit 1
fi

echo "Готово"