# Паттерны проектирования

Популярные:

1. Одиночка (Singleton)
2. Фабричный метод (Factory Method)
3. Абстрактная фабрика ( Abstract Factory)
4. Декоратор (Decorator)
5. Наблюдатель (Observer)
6. Стратегия (Strategy)

Дополнительно:

1. Прототип (Prototype)
2. Строитель (Builder)
3. Фасад (Facade)
4. Заместитель (Proxy)
5. Шаблонный метод(Template Method)
6. Итератор(Iterator)

---

### 1. **Порождающие паттерны** (Creational Patterns)

Отвечают за создание объектов, упрощая их инициализацию и управление.

- **Фабричный метод (Factory Method)**: — это порождающий паттерн
  проектирования, который определяет общий интерфейс для создания объектов в
  суперклассе, позволяя подклассам изменять тип создаваемых объектов.
- **Абстрактная фабрика (Abstract Factory)**: — это
  порождающий паттерн проектирования, который позволяет создавать семейства
  связанных объектов, не привязываясь к конкретным классам создаваемых объектов.
- **Строитель (Builder)**: — это порождающий паттерн проектирования,
  который позволяет создавать сложные объекты пошагово. Строитель даёт
  возможность использовать один и тот же код строительства для получения разных
  представлений объектов.
- **Одиночка (Singleton)**: — это порождающий паттерн проектирования,
  который гарантирует, что у класса есть только один экземпляр, и предоставляет
  к нему глобальную точку доступа.
- **Прототип (Prototype)**: — это порождающий паттерн проектирования,
  который позволяет копировать объекты, не вдаваясь в подробности их реализации.

### 2. **Структурные паттерны** (Structural Patterns)

Определяют, как объекты и классы объединяются в более крупные структуры.

- **Адаптер (Adapter)**: — это структурный паттерн проектирования, который
  позволяет объектам с несовместимыми интерфейсами работать вместе.
  Преобразует интерфейс одного класса в интерфейс,
  ожидаемый клиентом. Пример: адаптация старого API к новому.
- **Мост (Bridge)**: Разделяет абстракцию и реализацию, позволяя изменять их
  независимо. Пример: разделение логики рисования фигур и их рендеринга.
- **Компоновщик (Composite)**: Объединяет объекты в древовидные структуры для
  работы с ними как с единым объектом. Пример: структура GUI-компонентов.
- **Декоратор (Decorator)**: Динамически добавляет обязанности объекту. Пример:
  добавление функционала к потоку ввода-вывода.
- **Фасад (Facade)**: Предоставляет упрощенный интерфейс к сложной подсистеме.
  Пример: упрощение работы с библиотекой.
- **Легковес (Flyweight)**: Экономит память, разделяя состояние объектов.
  Пример: повторное использование символов в текстовом редакторе.
- **Заместитель (Proxy)**: Контролирует доступ к объекту, добавляя
  дополнительную логику. Пример: ленивая загрузка изображений.

### 3. **Поведенческие паттерны** (Behavioral Patterns)

Регулируют взаимодействие и распределение обязанностей между объектами.

- **Цепочка обязанностей (Chain of Responsibility)**: Передает запрос по цепочке
  обработчиков. Пример: обработка событий в GUI.
- **Команда (Command)**: Инкапсулирует запрос как объект, позволяя передавать
  его как параметр. Пример: реализация операций "отмена/повтор".
- **Итератор (Iterator)**: Предоставляет способ последовательного доступа к
  элементам коллекции. Пример: обход списка или массива.
- **Посредник (Mediator)**: Упрощает взаимодействие между объектами, централизуя
  управление. Пример: чат между пользователями.
- **Снимок (Memento)**: Сохраняет состояние объекта для последующего
  восстановления. Пример: сохранение состояния игры.
- **Наблюдатель (Observer)**: Определяет зависимость "один ко многим", уведомляя
  объекты об изменениях. Пример: подписка на события.
- **Состояние (State)**: Позволяет объекту изменять поведение в зависимости от
  состояния. Пример: автомат с напитками.
- **Стратегия (Strategy)**: Определяет семейство алгоритмов и позволяет их
  взаимозаменяемость. Пример: сортировка разными алгоритмами.
- **Шаблонный метод (Template Method)**: Определяет скелет алгоритма, позволяя
  подклассам переопределять шаги. Пример: процесс обработки данных.
- **Посетитель (Visitor)**: Разделяет алгоритм от структуры данных, позволяя
  добавлять новые операции. Пример: обход дерева объектов.


# ПОДРОБНО

## 1. Singleton (Одиночка)

**Паттерн Singleton (Одиночка)** — это порождающий паттерн проектирования,
который гарантирует, что у класса есть только один экземпляр, и предоставляет
глобальную точку доступа к этому экземпляру. Он часто используется в ситуациях,
когда требуется единый доступ к ресурсу или состоянию, например, для логгеров,
конфигураций или пулов соединений.

## 2.1 Фабричный метод

Фабричный метод — это порождающий паттерн проектирования, который определяет
общий интерфейс для создания объектов в суперклассе, позволяя подклассам
изменять тип создаваемых объектов. Он помогает делегировать процесс создания
объектов подклассам, обеспечивая гибкость и расширяемость.

## 2.2 Абстрактная фабрика

Абстрактная фабрика — это порождающий паттерн проектирования, который позволяет
создавать семейства связанных объектов, не привязываясь к конкретным классам
создаваемых объектов. Он позволяет создавать группы объектов, которые работают
вместе, обеспечивая совместимость между ними.

## 3. **Observer (Наблюдатель)**

**Паттерн Observer (Наблюдатель)** — это поведенческий паттерн проектирования,
который устанавливает зависимость "один ко многим" между объектами, чтобы при
изменении состояния одного объекта (субъекта) все зависимые объекты (
наблюдатели) автоматически уведомлялись и обновлялись. Этот паттерн широко
используется в событийно-ориентированных системах, где изменения в одном
компоненте должны отражаться в других.

## 4. Strategy (Стратегия)

Стратегия — это поведенческий паттерн проектирования, который определяет
семейство схожих алгоритмов и помещает каждый из них в собственный класс, после
чего алгоритмы можно взаимозаменять прямо во время исполнения программы.
Он особенно полезен, когда нужно поддерживать разные варианты поведения или
алгоритмов для одной задачи.

## 5. **Decorator (Декоратор)**

**Паттерн Decorator (Декоратор)** — это структурный паттерн проектирования,
который позволяет динамически добавлять новые обязанности или поведение объекту,
оборачивая его в другой объект. Он предоставляет гибкую альтернативу
наследованию для расширения функциональности, сохраняя интерфейс исходного
объекта.

# ===== ДОПОЛНИТЕЛЬНО =====

## Прототип (Prototype)

**Паттерн Prototype (Прототип)** — это порождающий паттерн проектирования,
который позволяет создавать новые объекты путём копирования существующего
объекта, называемого прототипом, без привязки к их конкретным классам. Этот
паттерн полезен, когда создание объекта с нуля дорогостоящее или когда нужно
создать объект с похожими характеристиками, но с небольшими изменениями.

## Строитель (Builder)

Строитель — это порождающий паттерн проектирования, который позволяет создавать
сложные объекты пошагово. Строитель даёт возможность использовать один и тот же
код строительства для получения разных представлений объектов. Он особенно
полезен, когда объект имеет множество параметров, а их комбинации могут
варьироваться, или когда процесс создания требует нескольких этапов.

## Фасад (Facade)

**Паттерн Facade (Фасад)** — это структурный паттерн проектирования, который
предоставляет упрощённый интерфейс к сложной подсистеме, скрывая её внутреннюю
сложность. Он действует как "единая точка входа", упрощая взаимодействие клиента
с набором классов или модулей, которые вместе решают задачу.


## Заместитель (Proxy)

**Паттерн Proxy (Заместитель)** — это структурный паттерн проектирования,
который предоставляет объект-заместитель (прокси) для управления доступом к
другому объекту, добавляя дополнительную функциональность, такую как ленивая
инициализация, контроль доступа, логирование или кэширование, без изменения
исходного объекта. Прокси действует как посредник между клиентом и реальным
объектом.

## Шаблонный метод(Template Method)

**Паттерн Template Method (Шаблонный метод)** — это поведенческий паттерн
проектирования, который определяет общий алгоритм в суперклассе, позволяя
подклассам переопределять отдельные шаги этого алгоритма без изменения его общей
структуры. Он обеспечивает "скелет" алгоритма, где неизменяемая часть остаётся в
суперклассе, а изменяемые части делегируются подклассам.

## Итератор(Iterator)

Итератор — это поведенческий паттерн проектирования, который даёт возможность
последовательно обходить элементы составных объектов, не раскрывая их
внутреннего представления. Он позволяет клиентам обходить элементы коллекции, не
зная, как они хранятся (например, в массиве, списке или хэш-таблице), и
обеспечивает единый интерфейс для работы с разными типами коллекций.
