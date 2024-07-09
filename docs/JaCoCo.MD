Документация по внедрению и использованию плагина JaCoCo 

1. В Pom.xml добавлен плагин JaCoCo
```
    <plugin>
        <groupId>org.jacoco</groupId>
        <artifactId>jacoco-maven-plugin</artifactId>
        <version>0.8.12</version>
    <plugin>
```
2. Добавлена зависимость в Pom.xml
```
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-test</artifactId>
       <scope>test</scope>
   </dependency>
```
3. Настройка плагина JaCoCo

В блоке `<executions>`  прописаны исполнения для разных фаз сборки проекта.
```
Инициализация JaCoCo агента
<execution>
    <id>jacoco-initialize</id>
    <goals>
        <goal>prepare-agent</goal>
    </goals>
</execution>
```

```
Проверка покрытия кода JaCoCo
<execution>
<id>jacoco-check</id>
<goals>
    <goal>check</goal>
</goals>
<configuration>
    <rules>
        <rule>
            <element>BUNDLE</element>
            <limits>
                <limit>
                    <counter>LINE</counter> - Проверяет покрытие строк кода
                    <value>COVEREDRATIO</value>
                    <minimum>0.5</minimum>
                </limit>
                <limit>
                    <counter>BRANCH</counter> - Проверяет покрытие ветвлений в коде
                    <value>COVEREDRATIO</value>
                    <minimum>0.4</minimum>
                </limit>
                <limit>
                    <counter>METHOD</counter> - Проверяет покрытие методов кода
                    <value>COVEREDRATIO</value>
                    <minimum>0.5</minimum>
                </limit>
            </limits>
        </rule>
    </rules>
    Исключаем проверку классов мапперов сгенерированых MapStruct
    <excludes>
        <exclude>**/*MapperImpl.class</exclude>
        <exclude>**/*Mapper.class</exclude>
    </excludes>
</configuration>
</execution>
```

```
Генерирует подробный отчет.
<execution>
    <id>jacoco-report</id>
    <phase>test</phase>
    <goals>
        <goal>report</goal>
    </goals>
</execution>
```

4. Добавили профиль coverage
```
<profiles>
    <profile>
        <id>coverage</id>
        <build>
            <plugins>
                <plugin>
                    <groupId>org.jacoco</groupId>
                    <artifactId>jacoco-maven-plugin</artifactId>
                </plugin>
            </plugins>
        </build>
    </profile>
</profiles>
```
5. Активируем профиль и выполняем команду `mvn clean test`
6. Изучаем отчет о покрытии, открыв файл `index.html` в браузере находящийся по пути `target/site/jacoco/index.html`
