# Ввод вывод

## Введение

В Java определены две полноценные системы ввода-вывода: одна для байтового
ввода-вывода, а другая для символьного ввода-вывода.

**Ввод-вывод в Java основан на потоках**  
Ввод-вывод в программах на Java выполняется через потоки данных. Поток данных (
stream) —это абстракция, которая либо производит, либо потребляет информацию.
Поток связан с физическим устройством посредством системы ввода-вывода Java. Все
потоки ведут себя одинаково, даже если фактические физические устройства, с
которыми они связаны, различаются. Таким образом, одни и те же классы и методы
ввода-вывода могут применяться к разным ти- пам устройств. Например, те же
методы, которые используются для записи на консоль, можно применять для записи в
дисковый файл . Потоки данных Java реализованы внутри иерархий классов,
определенных в пакете java.io.

### **Классы потоков байтовых данных**

Потоки байтовых данных определяются с применением двух иерархий классов.
Вверху находятся два абстрактных класса: InputStream и OutputStream. В классе
InputStream определены характеристики, общие для потоков ввода байтовых данных,
а класс OutputStream описывает поведение потоков вывода байтовых данных.

**Классы потоков байтовых данных в java.io, которые не объявлены
нерекомендуемыми**

- **BufferedInputStream** - Буферизованный поток ввода
- **BufferedOutputStream** - Буферизованный поток вывода
- **ByteArrayInputStream** - Поток ввода, который выполняет чтение из байтового
  массива
- **ByteArrayOutputStream** - Поток вывода, который выполняет запись в байтовый
  массив
- **DataInputStream** - Поток ввода, который содержит методы для чтения
  стандартных
  типов данных Java
- **DataOutputStream** - Поток ввода, который содержит методы для записи
  стандартных
  типов данных Java
- **FileInputStream** - Поток ввода, который выполняет чтение из файла
- **FileOutputStream** - Поток вывода, который выполняет запись в файл
- **FilterInputStream** - Реализует InputStream
- **FilterOutputStream** - Реализует OutputStream
- **InputStream**- Абстрактный класс, который описывает поток ввода
- **ObjectInputStream** - Поток ввода для объектов
- **ObjectOutputStream** - Поток вывода для объектов
- **OutputStream** - Абстрактный класс, который описывает поток вывода
- **PipedInputStream** - Канал ввода
- **PipedOutputStream** - Канал вывода
- **PrintStream** - Поток вывода,который содержит методы print() и println()
- **PushbackInputStream** - Поток ввода, который позволяет возвращать байты в
  этот
  поток ввода
- **SequenceInputStream** - Поток ввода, являющийся комбинацией двух и более
  потоков
  ввода, которые будут читаться последователь- но друг за другом

### **Классы потоков символьных данных**

Потоки символьных данных определяются с помощью двух иерархий классов, на
вершине которых находятся два абстрактных класса: Reader и Writer. Класс Reader
используется для ввода, а класс Writer —для вывода. Конкретные классы,
производные от Reader и Writer, оперируют на потоках символов Unicode.
С Reader и Writer связано несколько конкретных подклассов, обрабатывающих
различные ситуации ввода-вывода. Как правило, классы, основанные на символах,
аналогичны классам, основанным на байтах.

**Классы символьных данных в java .io**

- **BufferedReader** - Буферизованный поток ввода символьных данных
- **BufferedWriter** - Буферизованный поток вывода символьных данных
- **CharArrayReader** -Поток ввода, который выполняет чтение из символьного
  массива
- **CharArrayWriter** - Поток вывода, который выполняет запись в символьный
  массив
- **FileReader** - Поток ввода, который выполняет чтение из файла
- **FileWriter** - Поток вывода, который выполняет запись в файл
- **FilterReader** - Фильтрующее средство чтения
- **FilterWriter** - Фильтрующее средство записи
- **InputStreamReader** - Поток ввода, который выполняет трансляцию байтов в
  символы
- **LineNumberReader** - Поток ввода, который подсчитывает строки
- **OutputStreamWriter** - Поток вывода, который выполняет трансляцию символов в
  байты
- **PipedReader** - Канал ввода
- **PipedWriter** - Канал вывода
- **PrintWriter** - Поток вывода, который содержит методы print() и println()
- **PushbackReader** - Поток ввода, который позволяет возвращать байты в этот
  поток ввода
- **Reader** - Абстрактный класс, описывающий поток ввода символьных данных
- **StringReader** - Поток ввода, который выполняет чтение из строки
- **StringWriter** - Поток вывода, который выполняет запись в строку
- **Writer** - Абстрактный класс, описывающий поток вывода символьных данных

**Методы, определенные в классе InputStream**

- **int available()** - Возвращает количество байтов входных данных, до- ступных
  в текущий момент для чтения
- **void close()** - Закрывает источник ввода. Дальнейшие попытки чте- ния приведут
  к генерации исключения IOException
- **void mark(int numBytes)** - Помещает метку в текущую позицию потока ввода,
  которая будет оставаться действительной до тех пор, пока не будет прочитано
  numBytes байтов
- **boolean markSupported()** - Возвращает true, если вызывающий поток поддерживает
  методы mark()/reset()
- **static InputStream nullInputStream()** - Возвращает открытый, но пустой поток
  ввода, т.е. по- ток, не содержащий данных. Таким образом, текущая позиция
  всегда находится в конце потока, и никакие входные данные не могут быть
  получены. Однако поток можно закрыть
- **int read()** - Возвращает целочисленное представление следующего доступного
  байта во входных данных. При попытке чтения в конце потока возвращает -1
- **int read(byte[] buffer)** - Пытается прочитать вплоть до buffer, length байтов в
  буфер, указанный в buffer, и возвращает фактическое количество успешно
  прочитанных байтов. При попытке чтения в конце потока возвращает -1
- **int read(byte[] buffer, int offset, int numBytes)** - Пытается прочитать вплоть
  до numBytes байтов в бу- фер buffer, начиная с buffer[offset], и возвраща- ет
  фактическое количество успешно прочитанных бай- тов. При попытке чтения в
  конце потока возвращает -1
- **byte[] readAllBytes() byte[] readNBytes(int numBytes)** - Начиная с текущей
  позиции, читает до конца потока и возвращает байтовый массив, который содержит
  входные данные
- **int readNBytes( byte[] buffer, int offset, int numBytes)** - Пытается прочитать
  numBytes байтов и возвращает результат в байтовом массиве. Если конец потока
  достигнут до того, как было прочитано numBytes байтов, тогда возвращаемый
  массив будет содержать меньше, чем numBytes байтов
- **void reset()** - Переустанавливает указатель ввода на ранее установленную
  метку
- **long skip(long numBytes)** - Игнорирует (т.е. пропускает) numBytes байтов вход-
  ных данных и возвращает фактическое количество пропущенных байтов
- **void skipNBytes(long numBytes)** - Игнорирует (т.е. пропускает) numBytes байтов
  вход- ных данных. Генерирует исключение EOFException, если достигнут конец
  потока до того, как было пропущено numBytes байтов, либо исключение
  IOException при возникновении какой-то ошибки ввода-вывода
- **long transferTo( OutputStream strm)** - Копирует байты из вызывающего потока в
  strm и возвращает количество скопированных байтов

**Методы, определенные в классе OutputStream**

- **void close()** - Закрывает поток вывода. Дальнейшие попытки записи приведут к
  генерации исключения IOException
- **void flush()** - Финализирует состояние вывода, так что любые буферы
  очищаются, т.е. сбрасывает буферы вывода
- **static OutputStream nullOutputStream()** - Возвращает открытый, но пустой поток
  вывода, т.е. поток, в который никакие выходные данные фактически не
  записывались. Таким образом, методы вывода могут быть вызваны, но они не
  производят какой-либо вывод. Однако поток можно закрыть
- **void write(int b)** - Записывает одиночный байт в поток вывода. Обратите
  внимание, что параметр имеет тип int, чтопозволяетвызыватьwrite() свыражением
  без необходимости в приведении к типу byte
- **void write(byte[] buffer)** - Записывает полный байтовый массив в поток вывода
  Записывает поддиапазон длиной numBytes байтов из буфера buffer типа байтового
  массива, начиная с buffer[offset]void write(byte [] int offset, int
  numBytes) buffer,