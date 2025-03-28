# Фасад (Facade)

— это структурный паттерн, который предоставляет простой (но урезанный)
интерфейс к сложной системе объектов, библиотеке или фреймворку.

Кроме того, что Фасад позволяет снизить общую сложность программы, он также
помогает вынести код, зависимый от внешней системы в единственное место.

**Применимость**: Паттерн часто встречается в клиентских приложениях, написанных
на
Java, которые используют классы-фасады для упрощения работы со сложными
библиотеки или API.

Примеры Фасадов в стандартных библиотеках Java:

javax.faces.context.FacesContext использует «под капотом» классы LifeCycle,
ViewHandler, NavigationHandler и многие другие, но клиенты об этом даже не
знают (что не мешает заменить эти классы другими с помощью инъекций).

javax.faces.context.ExternalContext использует внутри классы ServletContext,
HttpSession, HttpServletRequest, HttpServletResponse, и так далее.

**Признаки применения паттерна**: Фасад угадывается в классе, который имеет
простой
интерфейс, но делегирует основную часть работы другим классам. Чаще всего,
фасады сами следят за жизненным циклом объектов сложной системы.

# Структура

![facade_structure.png](/img/design_pattern/design_patterns/facade_structure.png)

1. **Фасад** предоставляет быстрый доступ к определённой функциональности
   подсистемы. Он «знает», каким классам нужно переадресовать запрос, и какие
   данные для этого нужны.
2. **Дополнительный фасад** можно ввести, чтобы не «захламлять» единственный
   фасад
   разнородной функциональностью. Он может использоваться как клиентом, так и
   другими фасадами.
3. **Сложная подсистема** состоит из множества разнообразных
   классов. Для того чтобы заставить их что-то делать, нужно
   знать подробности устройства подсистемы, порядок инициализации объектов и
   так далее.

   Классы подсистемы не знают о существовании фасада и работают друг с другом
   напрямую.
4. **Клиент** использует фасад вместо прямой работы с объектами сложной
   подсистемы.

## Пример кода

В этом примере Фасад упрощает работу клиента со сложной библиотекой
видеоконвертации.

![facade_example.png](/img/design_pattern/design_patterns/facade_example.png)

Фасад предоставляет пользователю лишь один простой метод, скрывая за собой целую
систему с видеокодеками, аудиомикшерами и другими не менее сложными объектами.

```java
package dev.folomkin.design_patterns.pattterns2.structural.facade;

import java.io.File;


// -> some_complex_media_library:
/**
 * Сложная библиотека видеоконвертации
 */


// <!--  Классы сложного стороннего фреймворка конвертации видео. Мы
// не контролируем этот код, поэтому не можем его упростить. -->


//-> some_complex_media_library/VideoFile.java:

/**
 * Класс видеофайла. 
 */
class VideoFile {
    private String name;
    private String codecType;

    public VideoFile(String name) {
        this.name = name;
        this.codecType = name.substring(name.indexOf(".") + 1);
    }

    public String getCodecType() {
        return codecType;
    }

    public String getName() {
        return name;
    }
}

//-> some_complex_media_library/Codec.java:

/**
 * Интерфейс кодека
 */
interface Codec {
}

//-> some_complex_media_library/MPEG4CompressionCodec.java:

/**
 * Кодек MPEG4
 */
class MPEG4CompressionCodec implements Codec {
    public String type = "mp4";
}

//->some_complex_media_library/OggCompressionCodec.java:

/**
 * Кодек Ogg
 */
class OggCompressionCodec implements Codec {
    public String type = "ogg";
}

//-> some_complex_media_library/CodecFactory.java:

/**
 * Фабрика видеокодеков кодеков
 */
class CodecFactory {
    public static Codec extract(VideoFile file) {
        String type = file.getCodecType();
        if (type.equals("mp4")) {
            System.out.println("CodecFactory: extracting mpeg audio...");
            return new MPEG4CompressionCodec();
        } else {
            System.out.println("CodecFactory: extracting ogg audio...");
            return new OggCompressionCodec();
        }
    }
}

//-> some_complex_media_library/BitrateReader.java:

/**
 * Bitrate-конвертер
 */
class BitrateReader {
    public static VideoFile read(VideoFile file, Codec codec) {
        System.out.println("BitrateReader: reading file...");
        return file;
    }

    public static VideoFile convert(VideoFile buffer, Codec codec) {
        System.out.println("BitrateReader: writing file...");
        return buffer;
    }
}

//-> some_complex_media_library/AudioMixer.java:

/**
 * Микширование аудио
 */
class AudioMixer {
    public File fix(VideoFile result) {
        System.out.println("AudioMixer: fixing audio...");
        return new File("tmp");
    }
}

// <!-- -->


// facade
// facade/VideoConversionFacade.java:

/**
 * Фасад библиотеки работы с видео. 
 * Создаём Фасад — простой интерфейс для работы со сложным фреймворком. Фасад 
 * не имеет всей функциональности фреймворка, но зато скрывает его сложность 
 * от клиентов.
 */


class VideoConversionFacade {
    public File convertVideo(String fileName, String format) {
        System.out.println("VideoConversionFacade: conversion started.");
        VideoFile file = new VideoFile(fileName);
        Codec sourceCodec = CodecFactory.extract(file);
        Codec destinationCodec;
        if (format.equals("mp4")) {
            destinationCodec = new MPEG4CompressionCodec();
        } else {
            destinationCodec = new OggCompressionCodec();
        }
        VideoFile buffer = BitrateReader.read(file, sourceCodec);
        VideoFile intermediateResult = BitrateReader.convert(buffer, destinationCodec);
        File result = (new AudioMixer()).fix(intermediateResult);
        System.out.println("VideoConversionFacade: conversion completed.");
        return result;
    }
}


/**
 * Клиентский код
 */

public class Code {
    public static void main(String[] args) {
        VideoConversionFacade converter = new VideoConversionFacade();
        File mp4Video = converter.convertVideo("youtubevideo.ogg", "mp4");
        // ...
    }
}


```

## Шаги реализации

1. Определите, можно ли создать более простой интерфейс, чем тот, который
   предоставляет сложная подсистема. Вы на правильном пути, если этот интерфейс
   избавит клиента от необходимости знать о подробностях подсистемы.
2. Создайте класс фасада, реализующий этот интерфейс. Он должен переадресовывать
   вызовы клиента нужным объектам подсистемы. Фасад должен будет позаботиться о
   том, чтобы правильно инициализировать объекты подсистемы.
3. Вы получите максимум пользы, если клиент будет работать только с фасадом. В
   этом случае изменения в подсистеме будут затрагивать только код фасада, а
   клиентский код останется рабочим.
4. Если ответственность фасада начинает размываться, подумайте о введении
   дополнительных фасадов.