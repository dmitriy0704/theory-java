# Фабрика или фабричный метод (Factory Method)

**_Цель:_**

Это порождающий паттерн, который гарантирует, что у класса есть только один 
экземпляр.

**_Для чего используется:_**

**_Пример использования:_**

![singleton.png](/img/design_pattern/design_patterns/singleton.png)
На картинке:

**interface Product** - в примере это интерфейс Developer  
**ConcreteProduct** - в примере это класс конкретной реализации(JavaDeveloper)  
**Creator** - в примере это интерфейс DeveloperFactory  
**ConcreteCreator** - в примере это класс реализующий этот интерфейс -
JavaDeveloperFactory

![factory_method_impl.png](/img/design_pattern/design_patterns/impl/factory_method_impl.png)