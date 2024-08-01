## Обработка исключений

__Иерархия исключений:__

    Throwable:
        - Error
        - Exceptions:
          - RuntimeExceptions

### **Непроверяемые исключения, определенные в java.lang**

| Исключения                          | Описание                                                                   |
|-------------------------------------|----------------------------------------------------------------------------|
| **ArithmeticException**             | Арифметическая ошибка, такая как деление на ноль;                          |
| **ArrayIndexOutOfBoundsException**  | Выход за допустимые пределы индекса в массиве;                             |
| **ArrayStoreException**             | Присваивание элементу массива значения несовместимого типа;                |
| **ClassCastException**              | Недопустимое приведение                                                    |
| **EnumConstantNotPresentException** | Попытка использования неопределенного значения перечисления                |
| **IllegalArgumentException**        | Использование недопустимого аргумента при вызове метода                    |
| **IllegalCallerException**          | Метод не может быть законно выполнен вызывающим кодом                      |
| **IllegalMonitorStateException**    | Недопустимая операция монитора, такая как ожидание неблокированного потока |
| **IllegalStateException**           | Некорректное состояние среды или приложения                                |
| **IllegalThreadStateException**     | Несовместимость запрошенной операции с текущим состоянием потока           |
| **IndexOutOfBoundsException**       | Выход за допустимые пределы индекса некоторого вида                        |
| **LayerInstantiationException**     | Невозможность создания уровня модуля                                       |
| **NegativeArraySizeException**      | Создание массива с отрицательным размером                                  |
| **NullPointerException**            | Недопустимое использование ссылки null                                     |
| **NumberFormatException**           | Недопустимое преобразование строки в числовой формат                       |
| **SecurityException**               | Попытка нарушения безопасности.                                            |
| **StringIndexOutOfBoundsException** | Попытка индексации за границами строки                                     |
| **TypeNotPresentException**         | Тип не найден                                                              |
| **UnsupportedOperationException**   | Обнаружение неподдерживаемой операции                                      |

### **Проверяемые исключения, определенные в java.lang**

| Исключения                       | Описание                                                               |
|----------------------------------|------------------------------------------------------------------------|
| **ClassNotFoundException**       | Класс не найден                                                        |
| **CloneNotSupportedException**   | Попытка клонирования объекта, который не реализует интерфейс Cloneable |
| **IllegalAccessException**       | Доступ к классу запрещен                                               |
| **InstantiationException**       | Попытка создания объекта абстрактного класса или интерфейса            |
| **InterruptedException**         | Один поток был прерван другим потоком                                  |
| **NoSuchFieldException**         | Запрошенное поле не существует                                         |
| **NoSuchMethodException**        | Запрошенный метод не существует                                        |
| **ReflectiveOperationException** | Суперкласс исключений, связанных с рефлексией                          |

