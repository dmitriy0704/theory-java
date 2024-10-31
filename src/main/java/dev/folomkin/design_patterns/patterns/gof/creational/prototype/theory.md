# Прототип (Prototype)

**_Цель:_**
Определить вид создаваемых объектов с помощью экземпляра - прототипа и создавать
новые объекты, копируя этот прототип.

**_Для чего используется:_**

Для создания копий заданного объекта.

**_Пример использования:_**

- класса, экземпляры которых необходимо создать определяются во время выполнения
  программы;
- для избежания построения иерархии классов, фабрик или параллельных иерархий
  классов;
- экземпляры класса могут находиться в одном из немногих возможных состояний

![prototype.png](/img/design_pattern/design_patterns/prototype.png)
На картинке:

**interface Builder** - в примере это интерфейс WebSiteBuilder  
**ConcreteBuilder** - в примере это класс конкретной реализации(
VisitWebSiteBuilder)  
**Director** - помогает строить сайт. Строит между классом-клиентом и классами
описывающими построение сайтов.