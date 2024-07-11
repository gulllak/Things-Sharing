Документация по внедрению и использованию плагина Maven PMD Plugin

1. В Pom.xml добавлен плагин Maven PMD Plugin
```
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-pmd-plugin</artifactId>
        <version>3.23.0</version>
        <executions>
            <execution>
                <goals>
                    <goal>check</goal>
                </goals>
            </execution>
        </executions>
    </plugin>
```
2. Добавлен в профиль check
```
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-pmd-plugin</artifactId>
    </plugin>
```
3. Конфигурирование отчетов 
```
    <reporting>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-pmd-plugin</artifactId>
            </plugin>
        </plugins>
    </reporting>
```
4. Создание отчета выполняется командами:

   `mvn pmd:pmd`

    или

   `mvn clean verify -P check`
5. Посмотреть отчет можно открыв в браузере файл `index.html` находящийся по пути `target/site/pmd.html` 
