Документация по внедрению и использованию плагина Apache Maven Checkstyle Plugin

1. В Pom.xml добавлен плагин checkstyle
```
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-checkstyle-plugin</artifactId>
        <version>3.4.0</version>
    </plugin>
```
2. В корневой каталог добавлен файл `checkstyle.xml` содержащий конфигурацию проверки.
3. В корневой каталог добавлен файл `suppressions.xml` содержащий конфигурацию подавлений для Checkstyle
4. Добавили профиль check
```
   <profile>
			<id>check</id>
			<build>
				<plugins>
					<plugin>
						<groupId>org.apache.maven.plugins</groupId>
						<artifactId>maven-checkstyle-plugin</artifactId>
					</plugin>
				</plugins>
			</build>
		</profile>
```
4. Выполняем команду `mvn clean validate -P check` 
5. Изучаем отчет, который сформируется в папке `target/`
