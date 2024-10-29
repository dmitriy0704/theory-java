# АНТИШАБЛОНЫ

## Магическое число (Magic Number)

Наличие в коде многократно повторяющихся одинаковых чисел или
чисел, объяснение происхождения которых отсутствует.

Магическое число — константа, использованная в коде для чего-либо (чаще всего —
идентификации данных), само число которой не несёт никакого смысла без
соответствующего комментария. Числа не несут абсолютно никакой семантики.

Когда в коде вашего проекта начинают появляться числа, значение которых не
является очевидным, — это очень плохо. Программист, который не является автором
такого кода, с трудностями сможет объяснить, как это работает. Со временем и
автор кода с магическими числами не сможет объяснить его.

Числа затрудняют понимание кода и его рефакторинг. Главные причины этой ошибки —
спешка при разработке и отсутствии практики программирования. Этот анти-паттерн
надо пресекать на корню, оговаривая использование числовых констант перед
началом разработки.

Для решения такой проблемы нужно создать переменную, имя которой объясняет
назначение числовой константы, и присвоить ей требуемое значение.

### Класс бога

Божественный объект — анти-паттерн, который довольно часто встречается у
разработчиков ООП. Такой объект берет на себя слишком много функций и/или хранит
в себе практически все данные. В итоге мы имеем непереносимый код, в котором, к
тому же, сложно разобраться.

К тому же подобный код довольно сложно поддерживать, учитывая, что вся система
зависит практически только от него. Причины этой ошибки: некомпетентность
разработчика, взятие одним разработчиком большой части работы (особенно, когда
объем работы превышает уровень опыта этого разработчика).

Бороться с таким подходом надо, разбивая задачи на подзадачи, которыми смогут
заниматься разные разработчики.

## Преждевременная оптимизация
Преждевременная оптимизация — это оптимизация, которую выполняют до того, как у
программиста есть вся информация, необходимая для принятия взвешенных решений по
поводу того, где и как нужно её проводить.

На практике сложно предсказать, где встретится узкое место. Попытки навести
оптимизацию до получения эмпирических результатов приведут к усложнению кода и
появлению ошибок, а пользы не принесут.

Как избежать? Сначала пиши чистый, читаемый, работающий код, используя известные
и проверенные алгоритмы и инструменты. При необходимости используй инструменты
для профилирования для поиска узких мест. Полагайся на измерения, а не на
догадки и предположения.

Примеры и признаки
Кэширование до того, как провели профилирование. Использование сложных и
недоказанных эвристических правил вместо математически верных алгоритмов. Выбор
новых, непротестированных фреймворков, которые могут повести себя плохо под
нагрузкой.

В чём сложность
Непросто определить, когда оптимизация будет преждевременной. Важно заранее
оставлять место для роста. Нужно выбирать решения и платформы, которые позволят
легко оптимизировать и расти. Также иногда преждевременную оптимизацию
используют в качестве оправдания за плохой код. Например, берут алгоритм O(n2)
только из-за того, что алгоритм был бы O(n) сложнее.

## Изобретение велосипеда
Смысл этого анти-паттерна в том, что программист разрабатывает собственное
решение задачи, для которой уже существуют решения, и зачастую куда более
удачные.

Разработчик считает себя умнее, поэтому для каждой задачи пытается придумать
собственное решение, несмотря на опыт его предшественников. Чаще всего это
приводит только к потере времени и понижению эффективности работы программиста.
Ведь решение скорее всего окажется неоптимальным, если вообще будет найдено.

Конечно, нельзя полностью отбрасывать возможность самостоятельного решения, так
как это прямой дорогой приведет к программированию копипастом. Разработчик
должен ориентироваться в задачах, которые могут предстать перед ним, чтобы
грамотно их решить, используя при этом готовые решение или изобретая
собственные.

Очень часто причиной этого анти-паттерна является банальная нехватка времени. А
время — это деньги.

## Изобретение велосипеда с квадратными колесами

Этот анти-паттерн очень тесно связан с простым изобретением велосипеда — это
создание собственного плохого решения, когда существует более удачное решение.

Этот анти-паттерн вдвойне забирает время: сначала время тратится на изобретение
и реализацию собственного решения, а потом — на его рефакторинг или замену.

Программист должен знать о существовании различных решений для определённых
кругов задач, ориентироваться в их преимуществах и недостатках.

Все проблемы, с которыми ты столкнешься как программист, можно поделить на две
части:

- эту проблему умные люди решили 30 лет назад
- эту проблему умные люди решили 50 лет назад

Большинство проблем в программировании были успешно решены еще до твоего
рождения. Не нужно ничего изобретать — просто изучай опыт других людей (для
этого и пишут книги).

В 2022 году мы можем отпраздновать такие дни рождения:

Языки программирования
- Языку С исполнилось 50 лет (1972)
- Языку Java исполнилось 27 лет (1995)
- Языку Python исполнился 31 год (1991)

Связь
- Интернету исполнилось 39 лет (1983)
- Мобильному телефону исполнилось 49 лет (1973)
- Первую СМС отправили 30 лет назад (1992)

Паттерны
- Паттерну MVC исполнилось 44 года (1978)
- SQL придумали 48 лет назад (1974)
- Java Beans придумали 26 лет назад (1996)

Библиотеки
- Hibernate придумали 21 год назад (2001)
- Spring придумали 20 лет назад (2002)
- Tomcat выпустили 23 года назад (1999)

Операционные системы
- Unix выпустили в 51 год назад (1971)
- Windows увидела свет 37 лет назад (1985)
- Mac OS выпустили 21 год назад (2001)

И все эти вещи были не просто так придуманы, они были разработаны как решения
проблем, которые были очень распространены и актуальны в то время.

**Big ball of mud.**

«Большой Ком Грязи» — термин для системы или просто
программы, которая не имеет хоть немного различимой архитектуры. Как правило,
включает в себя более одного антишаблона. Этим страдают системы, разработанные
людьми без подготовки в области архитектуры ПО.

**Software Bloat.**

«Распухание ПО» — термин, используемый для описания
тенденций развития новейших программ в направлении использования бóльших объемов
системных ресурсов (место на диске, ОЗУ), чем предшествующие версии. В более
общем контексте применяется для описания программ, которые используют больше
ресурсов, чем необходимо.

**Yo-Yo problem.**

«Проблема Йо-Йо» возникает, когда необходимо разобраться в
программе, иерархия наследования и вложенность вызовов методов которой очень
длинны и сложны. Программисту вследствие этого необходимо лавировать между
множеством различных классов и методов, чтобы контролировать поведение
программы. Термин происходит от названия игрушки йо-йо.

**Magic Button.**

Возникает, когда код обработки формы сконцентрирован в одном
месте и, естественно, никак не структурирован.

**Gas Factory.**

«Газовый Завод» — необязательный сложный дизайн для простой задачи.

**Analiys paralisys.**

В разработке ПО «Паралич анализа» проявляет себя через
чрезвычайно длинные фазы планирования проекта, сбора необходимых для этого
артефактов, программного моделирования и дизайна, которые не имеют особого
смысла для достижения итоговой цели.

**Interface Bloat.**

«Распухший Интерфейс» — термин, используемый для описания
интерфейсов, которые пытаются вместить в себя все возможные операции над
данными.

**Smoke And Mirrors.**

Термин «Дым и Зеркала» используется, чтобы описать
программу либо функциональность, которая еще не существует, но выставляется за
таковую. Часто используется для демонстрации финального проекта и его
функционала.

**Improbability Factor.**

«Фактор Неправдоподобия» — ситуация, при которой в
системе наблюдается некоторая проблема. Часто программисты знают о проблеме, но
им не разрешено ее исправить отчасти из-за того, что шанс всплыть наружу у этой
проблемы очень мал. Как правило (следуя закону Мерфи), она всплывает и наносит
ущерб.

**Сreeping featurism.**

Используется для описания ПО, которое выставляет
напоказ вновь разработанные элементы, доводя до высокой степени ущербности по
сравнению с ними другие аспекты дизайна — простоту, компактность и отсутствие
ошибок. Как правило, существует вера в то, что каждая новая маленькая черта
информационной системы увеличит ее стоимость.

**Accidental complexity.**

«Случайная сложность» — проблема в программировании,
которой легко можно было избежать. Возникает вследствие неправильного понимания
проблемы или неэффективного планирования.

**Ambiguous viewpoint.**

Объектно-ориентированные модели анализа и дизайна
представляются без внесения ясности в особенности модели. Изначально эти модели
обозначаются с точки зрения визуализации структуры программы. Двусмысленные
точки зрения не поддерживают фундаментального разделения интерфейсов и деталей
представления.

**Boat anchor.**

«Корабельный Якорь» — часть бесполезного компьютерного
«железа», единственное применение которого — отправить на утилизацию. Этот
термин появился в то время, когда компьютеры были больших размеров. В настоящее
время термин «Корабельный Якорь» стал означать классы и методы, которые по
различным причинам не имеют какого-либо применения в приложении и в принципе
бесполезны. Они только отвлекают внимание от действительно важного кода.

**Busy spin.**

Техника, при которой процесс непрерывно проверяет изменение
некоторого состояния, например, ожидает ввода с клавиатуры или разблокировки
объекта. В результате повышается загрузка процессора, ресурсы которого можно
было бы перенаправить на исполнения другого процесса. Альтернативным
путем является использование сигналов. Большинство ОС поддерживают
погружение потока в состояние «сон» до тех пор, пока ему отправит сигнал другой
поток в результате изменения своего состояния.

**Caching Failure.**

«Кэширование Ошибки» — тип программного бага (bug),
при котором приложение сохраняет (кэширует) результаты, указывающие
на ошибку даже после того, как она исправлена. Программист исправляет
ошибку, но флаг ошибки не меняет своего состояния, поэтому приложение все
еще не работает.