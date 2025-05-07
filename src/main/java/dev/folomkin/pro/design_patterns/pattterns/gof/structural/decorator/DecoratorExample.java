package dev.folomkin.pro.design_patterns.pattterns.gof.structural.decorator;

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

class FileDataSource implements DataSource {
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

abstract class DataSourceDecorator implements DataSource {
    private DataSource wrappee;

    DataSourceDecorator(DataSource source) {
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

class EncryptionDecorator extends DataSourceDecorator {

    public EncryptionDecorator(DataSource source) {
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

class CompressionDecorator extends DataSourceDecorator {
    private int compLevel = 6;

    public CompressionDecorator(DataSource source) {
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
        DataSourceDecorator encoded = new CompressionDecorator(
                new EncryptionDecorator(
                        new FileDataSource("out/OutputDemo.txt")));
        encoded.writeData(salaryRecords);
        DataSource plain = new FileDataSource("out/OutputDemo.txt");

        System.out.println("- Input ----------------");
        System.out.println(salaryRecords);
        System.out.println("- Encoded --------------");
        System.out.println(plain.readData());
        System.out.println("- Decoded --------------");
        System.out.println(encoded.readData());
    }
}
