Документация по внедрению и использованию плагина SpotBugs Maven Plugin

1. В Pom.xml добавлен плагин SpotBugs Maven Plugin

```
    <plugin>
        <groupId>com.github.spotbugs</groupId>
        <artifactId>spotbugs-maven-plugin</artifactId>
        <version>4.8.6.2</version>
        <configuration>
            <effort>Max</effort> - более тщательный анализ кода
            <threshold>High</threshold> - поиск ошибок высокой важности
        </configuration>
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
       <groupId>com.github.spotbugs</groupId>
       <artifactId>spotbugs-maven-plugin</artifactId>
   </plugin>
```

3. Конфигурирование отчетов 
```
    <reporting>
        <plugins>
            <plugin>
                <groupId>com.github.spotbugs</groupId>
                <artifactId>spotbugs-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </reporting>
```

4. Создание отчета выполняется командами:

   `mvn clean verify -P check` 

    или

   `mvn clean compile spotbugs:check`

5. После выполнения отчета можно сгенерировать полный отчет командой `mvn site`, после чего открыть в браузере файл `index.html` находящийся по пути `target/site/index.html` 

    

