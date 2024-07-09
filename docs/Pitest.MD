Документация по внедрению и использованию плагина Pitest
1. В Pom.xml добавлен плагин Pitest
```
    <plugin>
        <groupId>org.pitest</groupId>
        <artifactId>pitest-maven</artifactId>
        <version>1.15.2</version>
    <plugin>
```
2. Добавлена зависимость в Pom.xml которая расширяет Pitest для поддержки JUnit 5.
```
    <dependency>
       <groupId>org.pitest</groupId>
       <artifactId>pitest-junit5-plugin</artifactId>
       <version>1.2.1</version>
    </dependency>
```
3. Настройка плагина Pitest
```
<configuration>
    <targetClasses> - определяем какие классы должны быть включены в процесс мутационного тестирования
        <param>ru.practicum.server.*</param>
    </targetClasses>
    <targetTests> - указываем тестовые классы, которые будут использованы для проверки мутаций
        <param>ru.practicum.server.*Test</param>
    </targetTests>
    <excludedClasses> - указываем классы которые хотим исключить
        <param>ru.practicum.server.*MapperImpl</param>
        <param>ru.practicum.server.*Mapper</param>
    </excludedClasses>
</configuration>
```

```
<execution>
    <id>pit-report</id>
    <phase>verify</phase> - указываем фазу выполнения Pitest
    <goals>
        <goal>mutationCoverage</goal>
    </goals>
</execution>
```
4. Запускаем мутационное тетсирование командой:

`   mvn test-compile org.pitest:pitest-maven:mutationCoverage
`
Если нужно запустить тестировние конкретного модуля, то указываем его:

`mvn -pl <имя> test-compile org.pitest:pitest-maven:mutationCoverage
`
5. Изучаем отчет открыв файл `index.html` в браузере находящийся по пути `target/pit-reports/index.html`
