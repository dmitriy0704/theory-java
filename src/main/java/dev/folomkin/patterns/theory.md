# Шаблоны проектирования

## Отношения классов:

![i1.png](/img/design_pattern/class_relations/i1.png)

Агрегация (aggregation) — описывает связь «часть»–«целое», в котором «часть»
может существовать отдельно от «целого». Ромб указывается со стороны «целого».

![i1.png](/img/design_pattern/class_relations/i2.png)

Композиция (composition) — подвид агрегации, в которой «части» не могут
существовать отдельно от «целого».

![i1.png](/img/design_pattern/class_relations/i3.png)

Зависимость (dependency) — изменение в одной сущности (независимой) может влиять
на состояние или поведение другой сущности (зависимой). Со стороны стрелки
указывается независимая сущность.

![i1.png](/img/design_pattern/class_relations/i4.png)

Обобщение (generalization) — отношение наследования или реализации интерфейса.
Со стороны стрелки находится суперкласс или интерфейс.

## Виды паттернов:

- Behavioral (B) - поведенческие;
- Creational (C) - порождающие;
- Structural (S) - структурные;

## Список шаблонов:

- Хранитель (memento) - B
  ![01_memento.png](/img/design_pattern/design_patterns/01_memento.png)
- Цепочка обязанностей (chain of responsibility) - B
  ![02_chain.png](/img/design_pattern/design_patterns/02_chain.png)
- Наблюдатель (observer) - B
  ![03_observer.png](/img/design_pattern/design_patterns/03_observer.png)
- Команда (command) - B
  ![04_command.png](/img/design_pattern/design_patterns/04_command.png)
- Состояние (state) - B
  ![05_state.png](/img/design_pattern/design_patterns/05_state.png)
- Интерпретатор (interpreter) - B
  ![06_interpreter.png](/img/design_pattern/design_patterns/06_interpreter.png)
- Стратегия (strategy) - B
  ![07_strategy.png](/img/design_pattern/design_patterns/07_strategy.png)
- Итератор (iterator) - B
  ![08_iterator.png](/img/design_pattern/design_patterns/08_iterator.png)
- Шаблонный метод (template method) - B
  ![09_templatemethod.png](/img/design_pattern/design_patterns/09_templatemethod.png)
- Посетитель (visitor) - B
  ![10_visitor.png](/img/design_pattern/design_patterns/10_visitor.png)
- Посредник (mediator) - B
  ![11_mediator.png](/img/design_pattern/design_patterns/11_mediator.png)
- Строитель (builder) - C
  ![17_builder.png](/img/design_pattern/design_patterns/17_builder.png)
- Фабричный метод (factory method) - C
  ![19_factorymethod.png](/img/design_pattern/design_patterns/19_factorymethod.png)
- Абстрактная фабрика (abstract factory) - C
  ![14_abstractfactory.png](/img/design_pattern/design_patterns/14_abstractfactory.png)
- Прототип (prototype) - C
  ![20_prototype.png](/img/design_pattern/design_patterns/20_prototype.png)
- Одиночка (singleton) - C
  ![23_singleton.png](/img/design_pattern/design_patterns/23_singleton.png)
- Адаптер (adapter) - S
  ![12_adapter.png](/img/design_pattern/design_patterns/12_adapter.png)
- Мост (bridge) - S
  ![15_bridge.png](/img/design_pattern/design_patterns/15_bridge.png)
- Компоновщик (composite) - S
  ![16_composite.png](/img/design_pattern/design_patterns/16_composite.png)
- Декоратор (decorator) - S
  ![18_decorator.png](/img/design_pattern/design_patterns/18_decorator.png)
- Фасад (facade) - S
  ![21_facade.png](/img/design_pattern/design_patterns/21_facade.png)
- Приспособленец (flyweight) - S
  ![22_flyweight.png](/img/design_pattern/design_patterns/22_flyweight.png)
- Прокси (proxy) - S
  ![13_proxy.png](/img/design_pattern/design_patterns/13_proxy.png)

## Шаблоны подробно

### Одиночка (Singleton, порождающий)

В этом шаблоне можно создать только один экземпляр класса. Даже если создано
несколько ссылочных переменных, все они будут указывать на один и тот же объект.

```java
class Probe {
    // Instance variables
    // Important methods

    // Конструктор приватный, чтобы не создавалось несколько объектов
    private Probe() {
        // Initialize variables here
    }

    //Каждый раз, когда вызывается этот метод, он возвращает один и тот же
    // объект. Таким образом, шаблон способен блокировать создание нескольких
    // объектов.
    private static Probe getInstance(Probe probe) {
        if (probe == null)
            probe = new Probe();
        return probe;
    }
}
```

### - Наблюдатель (observer, поведенческий)


