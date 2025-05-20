# Заместитель (Proxy)

- это структурный паттерн проектирования, который позволяет подставлять вместо
  реальных объектов специальные объекты-заменители. Эти объекты перехватывают
  вызовы к оригинальному объекту, позволяя сделать что-то до или после передачи
  вызова оригиналу. это объект, который выступает прослойкой между клиентом и
  реальным сервисным объектом. Заместитель получает вызовы от клиента, выполняет
  свою функцию (контроль доступа, кеширование, изменение запроса и прочее), а
  затем передаёт вызов сервисному объекту.

Заместитель имеет тот же интерфейс, что и реальный объект, поэтому для клиента
нет разницы — работать через заместителя или напрямую.

**Применимость**: Паттерн Заместитель применяется в Java коде тогда, когда надо
заменить настоящий объект его суррогатом, причём незаметно для клиентов
настоящего объекта. Это позволит выполнить какие-то добавочные поведения до или
после основного поведения настоящего объекта.

Примеры Заместителя в стандартных библиотеках Java:

java.lang.reflect.Proxy
java.rmi.*
javax.ejb.EJB (см. комментарии)
javax.inject.Inject (см. комментарии)
javax.persistence.PersistenceContext

**Признаки применения паттерна**: Класс заместителя чаще всего делегирует всю
настоящую работу своему реальному объекту. Заместители часто сами следят за
жизненным циклом своего реального объекта.

## Структура

![proxy_structure.png](/img/design_pattern/design_patterns/proxy_structure.png)

1. Интерфейс сервиса определяет общий интерфейс для сервиса и заместителя.
   Благодаря этому, объект заместителя можно использовать там, где ожидается
   объект сервиса.
2. Сервис содержит полезную бизнес-логику.
3. Заместитель хранит ссылку на объект сервиса. После того как заместитель
   заканчивает свою работу (например, инициализацию, логирование, защиту или
   другое), он передаёт вызовы вложенному сервису. Заместитель может сам
   отвечать за создание и удаление объекта сервиса.
4. Клиент работает с объектами через интерфейс сервиса. Благодаря этому, его
   можно «одурачить», подменив объект сервиса объектом заместителя.

## Пример кода

Кеширующий заместитель

Этот пример показывает как с помощью Заместителя можно сделать более эффективной
коммуникацию по сети с внешним видеосервисом, кешируя повторяющиеся запросы.

Заместитель особенно полезен, если у вас нет доступа к коду служебных классов,
поведение которых хочется улучшить. Он позволяет изменить поведение реального
объекта, прозрачно для клиентского кода. В этом примере заместитель и сам
реальный объект взаимозаменяемы.

```java
package dev.folomkin.design_patterns.pattterns.structural.proxy;


//-> some_cool_media_library
//-> some_cool_media_library/ThirdPartyYouTubeLib.java:

import java.util.HashMap;

/**
 * Интерфейс удалённого сервиса
 */

interface ThirdPartyYouTubeLib {
    HashMap<String, proxy.structural.gof.dev.folomkin.pro.design_patterns.Video> popularVideos();

    proxy.structural.gof.dev.folomkin.pro.design_patterns.Video getVideo(String videoId);
}


//-> some_cool_media_library/ThirdPartyYouTubeClass.java:

/**
 * Реализация удалённого сервиса
 *
 * Методы этого класса запрашивают у YouTube различную информацию.
 * Скорость запроса зависит не только от качества интернет-канала 
 * пользователя, но и от состояния самого YouTube. Значит, чем 
 * больше будет вызовов к сервису, тем менее отзывчивой станет 
 * программа.
 */
class ThirdPartyYouTubeClass implements proxy.structural.gof.dev.folomkin.pro.design_patterns.ThirdPartyYouTubeLib {

    @Override
    public HashMap<String, proxy.structural.gof.dev.folomkin.pro.design_patterns.Video> popularVideos() {
        connectToServer("http://www.youtube.com");
        return getRandomVideos();
    }

    @Override
    public proxy.structural.gof.dev.folomkin.pro.design_patterns.Video getVideo(String videoId) {
        connectToServer("http://www.youtube.com/" + videoId);
        return getSomeVideo(videoId);
    }

    // -----------------------------------------------------------------------
    // Fake methods to simulate network activity. They as slow as a real life.

    private int random(int min, int max) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }

    private void experienceNetworkLatency() {
        int randomLatency = random(5, 10);
        for (int i = 0; i < randomLatency; i++) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void connectToServer(String server) {
        System.out.print("Connecting to " + server + "... ");
        experienceNetworkLatency();
        System.out.print("Connected!" + "\n");
    }

    private HashMap<String, proxy.structural.gof.dev.folomkin.pro.design_patterns.Video> getRandomVideos() {
        System.out.print("Downloading populars... ");

        experienceNetworkLatency();
        HashMap<String, proxy.structural.gof.dev.folomkin.pro.design_patterns.Video> hmap = new HashMap<String, proxy.structural.gof.dev.folomkin.pro.design_patterns.Video>();
        hmap.put("catzzzzzzzzz", new proxy.structural.gof.dev.folomkin.pro.design_patterns.Video("sadgahasgdas", "Catzzzz.avi"));
        hmap.put("mkafksangasj", new proxy.structural.gof.dev.folomkin.pro.design_patterns.Video("mkafksangasj", "Dog play with ball.mp4"));
        hmap.put("dancesvideoo", new proxy.structural.gof.dev.folomkin.pro.design_patterns.Video("asdfas3ffasd", "Dancing video.mpq"));
        hmap.put("dlsdk5jfslaf", new proxy.structural.gof.dev.folomkin.pro.design_patterns.Video("dlsdk5jfslaf", "Barcelona vs RealM.mov"));
        hmap.put("3sdfgsd1j333", new proxy.structural.gof.dev.folomkin.pro.design_patterns.Video("3sdfgsd1j333", "Programing lesson#1.avi"));

        System.out.print("Done!" + "\n");
        return hmap;
    }

    private proxy.structural.gof.dev.folomkin.pro.design_patterns.Video getSomeVideo(String videoId) {
        System.out.print("Downloading video... ");

        experienceNetworkLatency();
        proxy.structural.gof.dev.folomkin.pro.design_patterns.Video video = new proxy.structural.gof.dev.folomkin.pro.design_patterns.Video(videoId, "Some video title");

        System.out.print("Done!" + "\n");
        return video;
    }

}


// some_cool_media_library/Video.java:

/**
 * Видеофайл
 */

class Video {
    public String id;
    public String title;
    public String data;

    Video(String id, String title) {
        this.id = id;
        this.title = title;
        this.data = "Random video.";
    }
}


//-> proxy
//->  proxy/YouTubeCacheProxy.java:

/**
 * Сервисный класс для работы с кешем
 *
 * С другой стороны, можно кешировать запросы к YouTube и не 
 * повторять их какое-то время, пока кеш не устареет. Но внести 
 * этот код напрямую в сервисный класс нельзя, так как он находится 
 * в сторонней библиотеке. Поэтому мы поместим логику кеширования
 * в отдельный класс-обёртку. Он будет делегировать запросы к 
 * сервисному объекту, только если нужно непосредственно выслать
 * запрос.
 */

class YouTubeCacheProxy implements proxy.structural.gof.dev.folomkin.pro.design_patterns.ThirdPartyYouTubeLib {
    private proxy.structural.gof.dev.folomkin.pro.design_patterns.ThirdPartyYouTubeLib youtubeService;
    private HashMap<String, proxy.structural.gof.dev.folomkin.pro.design_patterns.Video> cachePopular = new HashMap<String, proxy.structural.gof.dev.folomkin.pro.design_patterns.Video>();
    private HashMap<String, proxy.structural.gof.dev.folomkin.pro.design_patterns.Video> cacheAll = new HashMap<String, proxy.structural.gof.dev.folomkin.pro.design_patterns.Video>();

    public YouTubeCacheProxy() {
        this.youtubeService = new proxy.structural.gof.dev.folomkin.pro.design_patterns.ThirdPartyYouTubeClass();
    }

    @Override
    public HashMap<String, proxy.structural.gof.dev.folomkin.pro.design_patterns.Video> popularVideos() {
        if (cachePopular.isEmpty()) {
            cachePopular = youtubeService.popularVideos();
        } else {
            System.out.println("Retrieved list from cache.");
        }
        return cachePopular;
    }

    @Override
    public proxy.structural.gof.dev.folomkin.pro.design_patterns.Video getVideo(String videoId) {
        proxy.structural.gof.dev.folomkin.pro.design_patterns.Video video = cacheAll.get(videoId);
        if (video == null) {
            video = youtubeService.getVideo(videoId);
            cacheAll.put(videoId, video);
        } else {
            System.out.println("Retrieved video '" + videoId + "' from cache.");
        }
        return video;
    }

    public void reset() {
        cachePopular.clear();
        cacheAll.clear();
    }
}


//-> downloader
//-> downloader/YouTubeDownloader.java:

/**
 * Загрузчик файлов
 *
 * Класс GUI, который использует сервисный объект. Вместо
 * реального сервиса, мы подсунем ему объект-заместитель. Клиент
 * ничего не заметит, так как заместитель имеет тот же
 * интерфейс, что и сервис.
 */
class YouTubeDownloader {
    private proxy.structural.gof.dev.folomkin.pro.design_patterns.ThirdPartyYouTubeLib api;

    public YouTubeDownloader(proxy.structural.gof.dev.folomkin.pro.design_patterns.ThirdPartyYouTubeLib api) {
        this.api = api;
    }

    public void renderVideoPage(String videoId) {
        proxy.structural.gof.dev.folomkin.pro.design_patterns.Video video = api.getVideo(videoId);
        System.out.println("\n-------------------------------");
        System.out.println("Video page (imagine fancy HTML)");
        System.out.println("ID: " + video.id);
        System.out.println("Title: " + video.title);
        System.out.println("Video: " + video.data);
        System.out.println("-------------------------------\n");
    }

    public void renderPopularVideos() {
        HashMap<String, proxy.structural.gof.dev.folomkin.pro.design_patterns.Video> list = api.popularVideos();
        System.out.println("\n-------------------------------");
        System.out.println("Most popular videos on YouTube (imagine fancy HTML)");
        for (proxy.structural.gof.dev.folomkin.pro.design_patterns.Video video : list.values()) {
            System.out.println("ID: " + video.id + " / Title: " + video.title);
        }
        System.out.println("-------------------------------\n");
    }
}


/**
 * Конфигурационная часть приложения создаёт и передаёт клиентам 
 * объект заместителя.
 */

public class Code {
    public static void main(String[] args) {
        proxy.structural.gof.dev.folomkin.pro.design_patterns.YouTubeDownloader naiveDownloader = new proxy.structural.gof.dev.folomkin.pro.design_patterns.YouTubeDownloader(new proxy.structural.gof.dev.folomkin.pro.design_patterns.ThirdPartyYouTubeClass());
        proxy.structural.gof.dev.folomkin.pro.design_patterns.YouTubeDownloader smartDownloader = new proxy.structural.gof.dev.folomkin.pro.design_patterns.YouTubeDownloader(new proxy.structural.gof.dev.folomkin.pro.design_patterns.YouTubeCacheProxy());

        long naive = test(naiveDownloader);
        long smart = test(smartDownloader);
        System.out.print("Time saved by caching proxy: " + (naive - smart) + "ms");

    }

    private static long test(proxy.structural.gof.dev.folomkin.pro.design_patterns.YouTubeDownloader downloader) {
        long startTime = System.currentTimeMillis();

        // User behavior in our app:
        downloader.renderPopularVideos();
        downloader.renderVideoPage("catzzzzzzzzz");
        downloader.renderPopularVideos();
        downloader.renderVideoPage("dancesvideoo");
        // Users might visit the same page quite often.
        downloader.renderVideoPage("catzzzzzzzzz");
        downloader.renderVideoPage("someothervid");

        long estimatedTime = System.currentTimeMillis() - startTime;
        System.out.print("Time elapsed: " + estimatedTime + "ms\n");
        return estimatedTime;
    }
}

```

![proxy_example.png](/img/design_pattern/design_patterns/proxy_example.png)

Пример кеширования результатов работы реального сервиса с
помощью заместителя.

Оригинальный объект начинал загрузку по сети, даже если пользователь запрашивал
одно и то же видео. Заместитель же загружает видео только один раз, используя
для этого служебный объект, но в остальных случаях возвращает закешированный
файл.

## Шаги реализации

1. Определите интерфейс, который бы сделал заместитель и оригинальный объект
   взаимозаменяемыми.
2. Создайте класс заместителя. Он должен содержать ссылку на сервисный объект.
   Чаще всего, сервисный объект создаётся самим заместителем. В редких случаях
   заместитель получает готовый сервисный объект от клиента через конструктор.
3. Реализуйте методы заместителя в зависимости от его предназначения. В
   большинстве случаев, проделав какую-то полезную работу, методы заместителя
   должны передать запрос сервисному объекту.
4. Подумайте о введении фабрики, которая решала бы, какой из объектов
   создавать — заместитель или реальный сервисный объект. Но, с другой стороны,
   эта логика может быть помещена в создающий метод самого заместителя.
5. Подумайте, не реализовать ли вам ленивую инициализацию сервисного объекта при
   первом обращении клиента к методам заместителя.