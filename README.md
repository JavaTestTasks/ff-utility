# File filter utility

Программа разработана Java 17; \
Использована системы сборки Apache Maven 3.9.7. \
Для обработки агрументов коммандной строки использована библиотека Apache Commons CLI версии 1.8.0:
<!-- https://mvnrepository.com/artifact/commons-cli/commons-cli -->
        <dependency>
            <groupId>commons-cli</groupId>
            <artifactId>commons-cli</artifactId>
            <version>1.8.0</version>
        </dependency>

## Использование
Данная утилита позволяет отфильтровать содержимое файлов по типам данных и в результате получить отдельные файлы, содержащие Integer, Float или String данные,
с название файлов integer.txt, float.txt и string.txt соответственно. По умолчанию файлы создаются в текущей дирректории и перезаписываются.
- для компиляции исходных файлов и сборки jar файла, находясь в корневом каталоге использовать команду:
> mvn package

- программа позволяет использовать следующие опции:
  - '-a' - дописывать новые данные в файлы
  - '-s' - короткая статискика (содержит только имя файла и количество записанных элементов)
  - '-f' - полная статистика (дополнительно содержит для числовых данных: минимальное, максимальное, среднее значение и сумму записанных элементов; для строк: длины самой длинной и самой короткой строк)
  - '-p' - префикс для имен результирующих файлов
  - '-o' - абсолютный путь для результатов

- пример использования:
> java -jar target/util.jar -p result_ -f -o /some/path test1.txt test2.txt
