# SOLID

## Single Responsibility Principle - Принцип единственной ответственности.

> У класса должен быть только один мотив для изменения.

Каждый класс должен отвечать только за одну часть функциональности программы,
причём она должна быть полностью инкапсулирована в этот класс
(читай, скрыта внутри класса).

## Open/Closed Principle - Принцип открытости/закрытости

> Расширяйте классы, но не изменяйте их первоначальный код.

Классы должны быть открыты для расширения, но закрыты для изменения. Главная
идея этого принципа в том, чтобы не ломать существующий код при
внесении изменений в программу.

Класс можно назвать открытым, если он доступен для расширения. Например, у вас
есть возможность расширить набор его операций или добавить к нему новые поля,
создав собственный подкласс.

В то же время, класс можно назвать закрытым(а лучше сказать, законченным), если
он готов для использования другими классами. Это означает, что интерфейс класса
уже окончательно определён и не будет изменяться в будущем.

Если класс уже был написан, одобрен, протестирован, возможно,внесён в библиотеку
и включён в проект, после этого пытаться модифицировать его содержимое
нежелательно. Вместо этого вы можете создать подкласс и расширить в нём базовое
поведение, не изменяя код родительского класса напрямую.

## Liskov Substitution Principle - Принцип подстановки Барбары Лисков

> Подклассы должны дополнять, а не замещать поведение базового класса.

Подклассы должны создаваться таким образом, чтобы их объекты можно было бы
подставлять вместо объектов базового класса, не ломая при этом функциональности
клиентского кода.

Принцип подстановки — это ряд проверок, помогающих предсказать, останется
ли подкласс совместим с остальным кодом программы, который до этого успешно
работал, используя объекты базового класса. Это особенно важно при разработке
библиотеки фреймворков, когда ваши классы используются другими людьми, и вы не
можете повлиять на чужой клиентский код, даже если бы хотели.

В отличие от других принципов, которые определены очень свободно и имеют массу
трактовок, принцип подстановки имеет ряд формальных требований к подклассам, а
точнее, к переопределённым в них методам.

- **Типы параметров метода подкласса должны совпадать или
  быть более абстрактными, чем типы параметров базового метода.**
    - Базовый класс содержит метод feed(Cat c), который умеет кормить домашних
      котов. Клиентский код это знает и всегда передаёт в метод кота.
    - **Хорошо**: Вы создали подкласс и переопределили метод
      кормёжки так, чтобы накормить любое животное:
      feed(Animal c). Если подставить этот подкласс в клиент-
      ский код, то ничего страшного не произойдёт. Клиентский
      код подаст в метод кота, но метод умеет кормить всех
      животных, поэтому накормит и кота.
    - **Плохо**: Вы создали другой подкласс, в котором метод умеет кормить
      только бенгальскую породу котов (подкласс котов): feed(BengalCat c). Что
      будет с клиентским кодом? Он всё так же подаст в метод обычного кота. Но
      метод умеет кормить только бенгалов, поэтому не сможет отработать, сломав
      клиентский код.
- **Тип возвращаемого значения метода подкласса должен совпадать или быть
  подтипом
  возвращаемого значения базового метода.**
    - Базовый метод: buyCat(): Cat. Клиентский код ожидает на выходе любого
      домашнего кота.
    - Хорошо: Метод подкласса: buyCat(): BengalCat. Клиентский код получит
      бенгальского кота, который является домашним котом, поэтому всё будет
      хорошо.
    - Плохо: Метод подкласса: buyCat(): Animal. Клиентский
      код сломается, так как это непонятное животное (возможно, крокодил)
      не поместится в ящике-переноске для кота.
- **Метод не должен выбрасывать исключения, которые не свойственны базовому
  методу.** Типы исключений в переопределённом методе должны совпадать или быть
  подтипами исключений, которые выбрасывает базовый метод. Блоки try-catch в
  клиентском коде нацелены на конкретные типы исключений, выбрасываемые базовым
  методом. Поэтому неожиданное исключение, выброшенное подклассом, может
  проскочить сквозь обработчики клиентского кода и обрушить программу.
- **Метод не должен ужесточать пред-условия.** Например, базовый метод работает
  с параметром типа int. Если подкласс требует, чтобы значение этого параметра к
  тому же было больше нуля, то это ужесточает предусловия. Клиентский код,
  который до этого отлично работал, подавая в метод негативные числа, теперь
  сломается при работе с объектом подкласса.
- **Метод не должен ослаблять пост-условия.** Например, базовый метод требует,
  чтобы по завершению метода все подключения к базе данных были закрыты, а
  подкласс оставляет эти подключения открытыми, чтобы потом повторно
  использовать. Но клиентский код базового класса ничего об этом не знает. Он
  может завершить программу сразу после вызова метода, оставив запущенные
  процессы-призраки в системе.
- **Инварианты класса должны остаться без изменений.** Инвариант — это набор
  условий, при которых объект имеет смысл. Например, инвариант кота — это
  наличие четырёх лап, хвоста, способность мурчать и прочее. Инвариант может
  быть описан не только явным контрактом или проверками в методах класса, но и
  косвенно, например, юнит-тестами или клиентским кодом.
- **Подкласс не должен изменять значения приватных полей базового
  класса.** Этот пункт звучит странно, но в некоторых языках доступ к приватным
  полям можно получить через механизм рефлексии. В некоторых других языках (
  Python, JavaScript) и вовсе нет жёсткой защиты приватных полей.

## Interface Segregation Principle - Принцип разделения интерфейса

> Клиенты не должны зависеть от методов, которые они не используют.

Интерфейсы должны быть достаточно узкими, чтобы классам не приходилось
реализовывать
избыточное поведение.

Принцип разделения интерфейсов говорит о том, что слишком «толстые» интерфейсы
необходимо разделять на более маленькие и специфические, чтобы клиенты маленьких
интерфейсов знали только о методах, которые необходимы им в работе. В итоге при
изменении метода интерфей- са не должны меняться клиенты, которые этот метод не
используют.

Наследование позволяет классу иметь только один суперкласс, но не ограничивает
количество интерфейсов, которые он может реализовать. Большинство объектных
языков программирования позволяют классам реализовывать сразу несколько
интерфейсов, поэтому нет нужды заталкивать в ваш интерфейс больше поведений, чем
он того требует. Вы всегда можете присвоить классу сразу несколько интерфейсов
поменьше.

## Dependency Inversion Principle - Принцип инверсии зависимостей

> Классы верхних уровней не должны зависеть от классов нижних уровней. Оба
> должны зависеть от абстракций. Абстракции не должны зависеть от деталей.
> Детали должны зависеть от абстракций.

Обычно при проектировании программ можно выделить два
уровня классов.

- Классы нижнего уровня реализуют базовые операции вроде работы с диском,
  передачи данных по сети, подключения к базе данных и прочее.
- Классы высокого уровня содержат сложную бизнес-логику программы, которая
  опирается на классы низкого уровня для осуществления более простых операций.

Зачастую вы сперва проектируете классы нижнего уровня, а только потом берётесь
за верхний уровень. При таком подходе классы бизнес-логики становятся зависимыми
от более примитивных низкоуровневых классов. Каждое изменение в низкоуровневом
классе может затронуть классы бизнес-логики, которые его используют. Принцип
инверсии зависимостей предлагает изменить направление, в котором происходит
проектирование.

1. Для начала вам нужно описать интерфейс низкоуровневых операций, которые нужны
   классу бизнес-логики.
2. Это позволит вам убрать зависимость класса бизнес-логики от конкретного
   низкоуровневого класса, заменив её «мягкой» зависимостью от интерфейса.
3. Низкоуровневый класс, в свою очередь, станет зависимым от интерфейса,
   определённого бизнес-логикой. Принцип инверсии зависимостей часто идёт в ногу
   с принципом открытости/закрытости: вы сможете расширять низкоуровневые классы
   и использовать их вместе с классами бизнес-логики, не изменяя код последних.