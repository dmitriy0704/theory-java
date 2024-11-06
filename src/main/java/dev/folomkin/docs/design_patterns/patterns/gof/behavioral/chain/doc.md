# Цепочка ответственности (Chain of Responsibility)

**_Цель:_**

Связывание объектов-получателей в цепочку и передача запроса по ней

**_Для чего используется:_**

Помогает избежать привязки отправителя запроса к его получателю, что дает
возможность обработать данный запрос нескольким объектам.

**_Пример использования:_**

- ослабление привязанности (объект не должен знать, кто именно обработает его
  запрос);
- дополнительная гибкость при распределении обязанностей между объектами;

![chain.png](/img/design_pattern/design_patterns/chain.png)

На картинке:

**interface AbstractFactory** - ProjectTeamFactory  
**ConcreteFactory** - WebSIteTeamFactory
**interface AbstractProduct** - Developer
**ConcreteProduct** - PhpDeveloper

