# Декоратор (Decorator)

- это структурный паттерн, который позволяет добавлять объектам новые
  поведения на лету, помещая их в объекты-обёртки.

Декоратор позволяет оборачивать объекты бесчисленное количество раз благодаря
тому, что и обёртки, и реальные оборачиваемые объекты имеют общий интерфейс.

**Применимость**: Паттерн можно часто встретить в Java-коде, особенно в коде,
работающем с потоками данных.

Примеры Декораторов в стандартных библиотеках Java:

Все подклассы java.io.InputStream, OutputStream, Reader и Writer имеют
конструктор, принимающий объекты этих же классов.

java.util.Collections, методы checkedXXX(), synchronizedXXX() и
unmodifiableXXX().

javax.servlet.http.HttpServletRequestWrapper и HttpServletResponseWrapper

**Признаки применения паттерна**: Декоратор можно распознать по создающим
методам, которые принимают в параметрах объекты того же абстрактного типа или
интерфейса, что и текущий класс.

## Структура

![decorator_structure.png](/img/design_pattern/design_patterns/decorator_structure.png)

1. Компонент задаёт общий интерфейс обёрток и оборачиваемых объектов.
2. Конкретный компонент определяет класс оборачиваемых объектов. Он содержит
   какое-то базовое поведение, которое потом изменяют декораторы.
3. Базовый декоратор хранит ссылку на вложенный объект-компонент. Им может быть
   как конкретный компонент,так и
   один из конкретных декораторов. Базовый декоратор делегирует все свои
   операции вложенному объекту. Дополнительное поведение будет жить в конкретных
   декораторах.
4. Конкретные декораторы — это различные вариации декораторов, которые содержат
   добавочное поведение. Оно выполняется до или после вызова аналогичного
   поведения обёрнутого объекта.
5. Клиент может оборачивать простые компоненты и декораторы в другие декораторы,
   работая со всеми объектами через общий интерфейс компонентов.

## Пример кода

**Шифрование и сжатие данных**

Пример показывает, как можно добавить новую функциональность объекту, не меняя
его класса.

Сначала класс бизнес-логики мог считывать и записывать только чистые данные
напрямую из/в файлы. Применив паттерн Декоратор, мы создали небольшие
классы-обёртки, которые добавляют новые поведения до или после основной работы
вложенного объекта бизнес-логики.

Первая обёртка шифрует и расшифрует данные, а вторая — сжимает и распакует их.

Мы можем использовать обёртки как отдельно друг от друга, так и все вместе,
обернув один декоратор другим.

Приложение оборачивает класс данных в шифрующую и сжимающую обёртки, которые при
чтении выдают оригинальные данные, а при записи — зашифрованные и сжатые.

Декораторы, как и сам класс данных, имеют общий интерфейс. Поэтому клиентскому
коду неважно, с чем работать c «чистым» объектом данных или с «обёрнутым».

```java
package dev.folomkin.design_patterns.pattterns.structural.decorator;

import java.io.*;
import java.util.Base64;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;


// -> decorators
// -> decorators/DataSource.java:

/**
 * Общий интерфейс компонентов.
 * Интерфейс, задающий базовые операции чтения и записи данных
 */


interface DataSource {
   void writeData(String data);

   String readData();
}

// -> decorators/FileDataSource.java:

/**
 * Один из конкретных компонентов реализует базовую
 * функциональность.
 * Класс, реализующий прямое чтение и запись данных
 */

class FileDataSource implements dev.folomkin.design_patterns.pattterns.gof.structural.decorator.DataSource {
   private String name;

   public FileDataSource(String name) {
      this.name = name;
   }

   @Override
   public void writeData(String data) { // -> Записать данные в файл.
      File file = new File(name);
      try (OutputStream fos = new FileOutputStream(file)) {
         fos.write(data.getBytes(), 0, data.length());
      } catch (IOException ex) {
         System.out.println(ex.getMessage());
      }
   }

   @Override
   public String readData() { // -> Прочитать данные из файла.
      char[] buffer = null;
      File file = new File(name);
      try (FileReader reader = new FileReader(file)) {
         buffer = new char[(int) file.length()];
         reader.read(buffer);
      } catch (IOException ex) {
         System.out.println(ex.getMessage());
      }
      return new String(buffer);
   }
}


// -> decorators/DataSourceDecorator.java:

/**
 * Базовый декоратор. Родитель всех декораторов содержит код обёртывания.
 */

abstract class DataSourceDecorator implements dev.folomkin.design_patterns.pattterns.gof.structural.decorator.DataSource {
   private dev.folomkin.design_patterns.pattterns.gof.structural.decorator.DataSource wrappee;

   DataSourceDecorator(dev.folomkin.design_patterns.pattterns.gof.structural.decorator.DataSource source) {
      this.wrappee = source;
   }

   @Override
   public void writeData(String data) {
      wrappee.writeData(data);
   }

   @Override
   public String readData() {
      return wrappee.readData();
   }
}

// -> decorators/EncryptionDecorator.java:

/**
 * Конкретные декораторы добавляют что-то своё к базовому
 * поведению обёрнутого компонента.
 * Декоратор шифрования
 */

class EncryptionDecorator extends dev.folomkin.design_patterns.pattterns.gof.structural.decorator.DataSourceDecorator {

   public EncryptionDecorator(dev.folomkin.design_patterns.pattterns.gof.structural.decorator.DataSource source) {
      super(source);
   }


   // 1. Зашифровать поданные данные.
   // 2. Передать зашифрованные данные в метод writeData
   // обёрнутого объекта (wrappee).
   @Override
   public void writeData(String data) {
      super.writeData(encode(data));
   }


   // 1. Получить данные из метода readData обёрнутого
   // объекта (wrappee).
   // 2. Расшифровать их, если они зашифрованы.
   // 3. Вернуть результат.

   @Override
   public String readData() {
      return decode(super.readData());
   }

   private String encode(String data) {
      byte[] result = data.getBytes();
      for (int i = 0; i < result.length; i++) {
         result[i] += (byte) 1;
      }
      return Base64.getEncoder().encodeToString(result);
   }

   private String decode(String data) {
      byte[] result = Base64.getDecoder().decode(data);
      for (int i = 0; i < result.length; i++) {
         result[i] -= (byte) 1;
      }
      return new String(result);
   }
}


// -> decorators/CompressionDecorator.java:

/**
 * Декоратор сжатия. Декорировать можно не только базовые компоненты, но и уже
 * обёрнутые объекты.
 */

class CompressionDecorator extends dev.folomkin.design_patterns.pattterns.gof.structural.decorator.DataSourceDecorator {
   private int compLevel = 6;

   public CompressionDecorator(dev.folomkin.design_patterns.pattterns.gof.structural.decorator.DataSource source) {
      super(source);
   }

   public int getCompressionLevel() {
      return compLevel;
   }

   public void setCompressionLevel(int value) {
      compLevel = value;
   }


   // 1. Запаковать поданные данные.
   // 2. Передать запакованные данные в метод writeData
   // обёрнутого объекта (wrappee).
   @Override
   public void writeData(String data) {
      super.writeData(compress(data));
   }


   // 1. Получить данные из метода readData обёрнутого
   // объекта (wrappee).
   // 2. Распаковать их, если они запакованы.
   // 3. Вернуть результат.
   @Override
   public String readData() {
      return decompress(super.readData());
   }

   private String compress(String stringData) {
      byte[] data = stringData.getBytes();
      try {
         ByteArrayOutputStream bout = new ByteArrayOutputStream(512);
         DeflaterOutputStream dos = new DeflaterOutputStream(bout, new Deflater(compLevel));
         dos.write(data);
         dos.close();
         bout.close();
         return Base64.getEncoder().encodeToString(bout.toByteArray());
      } catch (IOException ex) {
         return null;
      }
   }

   private String decompress(String stringData) {
      byte[] data = Base64.getDecoder().decode(stringData);
      try {
         InputStream in = new ByteArrayInputStream(data);
         InflaterInputStream iin = new InflaterInputStream(in);
         ByteArrayOutputStream bout = new ByteArrayOutputStream(512);
         int b;
         while ((b = iin.read()) != -1) {
            bout.write(b);
         }
         in.close();
         iin.close();
         bout.close();
         return new String(bout.toByteArray());
      } catch (IOException ex) {
         return null;
      }
   }
}

public class DecoratorExample {
   public static void main(String[] args) {
      String salaryRecords = "Name,Salary\nJohn Smith,100000\nSteven Jobs,912000";
      dev.folomkin.design_patterns.pattterns.gof.structural.decorator.DataSourceDecorator encoded = new dev.folomkin.design_patterns.pattterns.gof.structural.decorator.CompressionDecorator(
              new dev.folomkin.design_patterns.pattterns.gof.structural.decorator.EncryptionDecorator(
                      new dev.folomkin.design_patterns.pattterns.gof.structural.decorator.FileDataSource("out/OutputDemo.txt")));
      encoded.writeData(salaryRecords);
      dev.folomkin.design_patterns.pattterns.gof.structural.decorator.DataSource plain = new dev.folomkin.design_patterns.pattterns.gof.structural.decorator.FileDataSource("out/OutputDemo.txt");

      System.out.println("- Input ----------------");
      System.out.println(salaryRecords);
      System.out.println("- Encoded --------------");
      System.out.println(plain.readData());
      System.out.println("- Decoded --------------");
      System.out.println(encoded.readData());
   }
}

```

## Шаги реализации

1. Убедитесь, что в вашей задаче есть один основной компонент и
   несколько опциональных дополнений или надстроек над ним.
2. Создайте интерфейс компонента, который описывал бы
   общие методы как для основного компонента, так и для его
   дополнений.
3. Создайте класс конкретного компонента и поместите в него
   основную бизнес-логику.
4. Создайте базовый класс декораторов. Он должен иметь
   поле для хранения ссылки на вложенный объект-компонент.
   Все методы базового декоратора должны делегировать действие
   вложенному объекту.
5. И конкретный компонент, и базовый декоратор должны следовать
   одному и тому же интерфейсу компонента.
6. Теперь создайте классы конкретных декораторов, наследуя
   их от базового декоратора. Конкретный декоратор должен
   выполнять свою добавочную функцию, а затем (или перед
   этим) вызывать эту же операцию обёрнутого объекта.
7. Клиент берёт на себя ответственность за конфигурацию и
   порядок обёртывания объектов.