package dev.folomkin.design_patterns.pattterns.gof.structural.facade;

import java.io.File;


// -> some_complex_media_library:
/**
 * Сложная библиотека видеоконвертации
 */

//-> some_complex_media_library/VideoFile.java:

/**
 * Класс видеофайла
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

// facade
// facade/VideoConversionFacade.java:

/**
 * Фасад библиотеки работы с видео
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

