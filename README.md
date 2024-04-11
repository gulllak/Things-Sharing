# Things Sharing

Приложение по обмену вещей между пользователями. 

1) Сервис-шлюз
   * валидирует запросы и обрабатывает их, перед передачей в основной сервис
   * сообщает пользователю, если что-то не так
   * передает запрос пользователя в основной сервис
   * сервисы общаются с помощью RestTemplate
2) Основной сервис
   * добавление вещей
   * редактирование
   * создание запросов на вещь
   * бронирование
   
## 🎬 Как запустить

1. Склонировать проект.
    ```bash
   $ git clone https://github.com/gulllak/Things-Sharing.git
   ```
2. Установить Docker и docker-compose
3. Собрать проект
    ```
    mvn clean package
   ```
4. Запустить проект
   ```
   docker compose up -d
   ```

### 🏄 Стек: 
Java 11, SpringBoot, Docker, PostgreSQL, Spring Data JPA, REST API, Lombok, MapStruct
