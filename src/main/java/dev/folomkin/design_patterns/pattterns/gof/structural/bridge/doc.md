# Мост (Bridge)

- это структурный паттерн проектирования, который
  разделяет один или несколько классов на две отдельные
  иерархии — абстракцию и реализацию, позволяя изменять
  их независимо друг от друга.

Одна из этих иерархий (абстракция) получит ссылку на объекты другой иерархии (
реализация) и будет делегировать им основную работу. Благодаря тому, что все
реализации будут следовать общему интерфейсу, их можно будет взаимозаменять
внутри абстракции.

**Применимость**: Паттерн Мост особенно полезен когда вам приходится делать
кросс-платформенные приложения, поддерживать несколько типов баз данных или
работать с разными поставщиками похожего API (например, cloud-сервисы,
социальные сети и т. д.)

**Признаки применения паттерна**: Если в программе чётко выделены классы
«управления» и несколько видов классов «платформ», причём управляющие объекты
делегируют выполнение платформам, то можно сказать, что у вас используется Мост.

## Структура

![bridge_structure.png](/img/design_pattern/design_patterns/bridge_structure.png)

1. Абстракция содержит управляющую логику. Код абстракции
   делегирует реальную работу связанному объекту реализации.
2. Реализация задаёт общий интерфейс для всех реализаций.
   Все методы, которые здесь описаны, будут доступны из класса абстракции и его
   подклассов. Интерфейсы абстракции и реализации могут как совпадать, так и
   быть совершенно разными. Но обычно в реализации живут базовые операции, на
   которых строятся сложные операции абстракции.
3. Конкретные реализации содержат платформо-зависимый код.
4. Расширенные абстракции содержат различные вариации управляющей логики. Как и
   родитель, работает с реализациями только через общий интерфейс реализации.
5. Клиент работает только с объектами абстракции. Не считая начального
   связывания абстракции с одной из реализаций, клиентский код не имеет
   прямого доступа к объектам реализации.

## Примеры кода

В этом примере Мост разделяет монолитный код приборов и пультов на две
части: приборы (выступают реализацией) и пульты управления ими (выступают
абстракцией).

Класс пульта имеет ссылку на объект прибора, которым он
управляет. Пульты работают с приборами через общий интерфейс. Это даёт
возможность связать пульты с различными приборами.

![bridge_example.png](/img/design_pattern/design_patterns/bridge_example.png)

Сами пульты можно развивать независимо от приборов. Для этого достаточно создать
новый подкласс абстракции. Вы можете создать как простой пульт с двумя кнопками,
так и более сложный пульт с тач-интерфейсом.

Клиентскому коду остаётся выбрать версию абстракции и реализации, с которым он
хочет работать, и связать их между собой.

```java
package dev.folomkin.design_patterns.pattterns.structural.bridge;

// -> remotes
// -> remotes/Remote.java: Общий интерфейс всех пультов ДУ

interface Remote {
   void power();

   void volumeDown();

   void volumeUp();

   void channelDown();

   void channelUp();
}


// -> remotes/BasicRemote.java: Стандартный пульт

/**
 * Класс пультов имеет ссылку на устройство, которым управляет. 
 * Методы этого класса делегируют работу методам связанного устройства. 
 *
 */
class BasicRemote implements dev.folomkin.design_patterns.pattterns.gof.structural.bridge.Remote {
   protected dev.folomkin.design_patterns.pattterns.gof.structural.bridge.Device device;

   public BasicRemote() {
   }

   public BasicRemote(dev.folomkin.design_patterns.pattterns.gof.structural.bridge.Device device) {
      this.device = device;
   }

   @Override
   public void power() {
      System.out.println("Remote: power toggle");
      if (device.isEnabled()) {
         device.disable();
      } else {
         device.enable();
      }
   }

   @Override
   public void volumeDown() {
      System.out.println("Remote: volume down");
      device.setVolume(device.getVolume() - 10);
   }

   @Override
   public void volumeUp() {
      System.out.println("Remote: volume up");
      device.setVolume(device.getVolume() + 10);
   }

   @Override
   public void channelDown() {
      System.out.println("Remote: channel down");
      device.setChannel(device.getChannel() - 1);
   }

   @Override
   public void channelUp() {
      System.out.println("Remote: channel up");
      device.setChannel(device.getChannel() + 1);
   }
}


// -> remotes/AdvancedRemote.java: Улучшенный пульт

/**
 * Можно расширять класс пультов, не трогая код устройств.
 */
class AdvancedRemote extends dev.folomkin.design_patterns.pattterns.gof.structural.bridge.BasicRemote {
   public AdvancedRemote(dev.folomkin.design_patterns.pattterns.gof.structural.bridge.Device device) {
      super.device = device;
   }

   public void mute() {
      System.out.println("Remote: mute");
      device.setVolume(0);
   }
}


// -> ../devices
// -> ../devices/Device.java: Общий интерфейс всех устройств

/**
 *  Все устройства имеют общий интерфейс. Поэтому с ними может 
 *  работать любой пульт.
 *
 */
interface Device {
   boolean isEnabled();

   void enable();

   void disable();

   int getVolume();

   void setVolume(int percent);

   int getChannel();

   void setChannel(int channel);

   void printStatus();
}


// -> ../devices/Radio.java: Радио

/**
 * Но каждое устройство имеет особую реализацию.
 */

class Radio implements dev.folomkin.design_patterns.pattterns.gof.structural.bridge.Device {
   private boolean on = false;
   private int volume = 30;
   private int channel = 1;

   @Override
   public boolean isEnabled() {
      return on;
   }

   @Override
   public void enable() {
      on = true;
   }

   @Override
   public void disable() {
      on = false;
   }

   @Override
   public int getVolume() {
      return volume;
   }

   @Override
   public void setVolume(int volume) {
      if (volume > 100) {
         this.volume = 100;
      } else if (volume < 0) {
         this.volume = 0;
      } else {
         this.volume = volume;
      }
   }

   @Override
   public int getChannel() {
      return channel;
   }

   @Override
   public void setChannel(int channel) {
      this.channel = channel;
   }

   @Override
   public void printStatus() {
      System.out.println("------------------------------------");
      System.out.println("| I'm radio.");
      System.out.println("| I'm " + (on ? "enabled" : "disabled"));
      System.out.println("| Current volume is " + volume + "%");
      System.out.println("| Current channel is " + channel);
      System.out.println("------------------------------------\n");
   }
}


// ->  devices/Tv.java: ТВ

class Tv implements dev.folomkin.design_patterns.pattterns.gof.structural.bridge.Device {
   private boolean on = false;
   private int volume = 30;
   private int channel = 1;

   @Override
   public boolean isEnabled() {
      return on;
   }

   @Override
   public void enable() {
      on = true;
   }

   @Override
   public void disable() {
      on = false;
   }

   @Override
   public int getVolume() {
      return volume;
   }

   @Override
   public void setVolume(int volume) {
      if (volume > 100) {
         this.volume = 100;
      } else if (volume < 0) {
         this.volume = 0;
      } else {
         this.volume = volume;
      }
   }

   @Override
   public int getChannel() {
      return channel;
   }

   @Override
   public void setChannel(int channel) {
      this.channel = channel;
   }

   @Override
   public void printStatus() {
      System.out.println("------------------------------------");
      System.out.println("| I'm TV set.");
      System.out.println("| I'm " + (on ? "enabled" : "disabled"));
      System.out.println("| Current volume is " + volume + "%");
      System.out.println("| Current channel is " + channel);
      System.out.println("------------------------------------\n");
   }
}


// Demo
public class BridgeExample {
   public static void main(String[] args) {
      testDevice(new dev.folomkin.design_patterns.pattterns.gof.structural.bridge.Tv());
      testDevice(new dev.folomkin.design_patterns.pattterns.gof.structural.bridge.Radio());
   }

   public static void testDevice(dev.folomkin.design_patterns.pattterns.gof.structural.bridge.Device device) {
      System.out.println("Tests with basic remote.");
      dev.folomkin.design_patterns.pattterns.gof.structural.bridge.BasicRemote basicRemote = new dev.folomkin.design_patterns.pattterns.gof.structural.bridge.BasicRemote(device);
      basicRemote.power();
      device.printStatus();

      System.out.println("Tests with advanced remote.");
      dev.folomkin.design_patterns.pattterns.gof.structural.bridge.AdvancedRemote advancedRemote = new dev.folomkin.design_patterns.pattterns.gof.structural.bridge.AdvancedRemote(device);
      advancedRemote.power();
      advancedRemote.mute();
      device.printStatus();
   }
}
```

## Шаги реализации

1. Определите, существует ли в ваших классах два не пересекающихся измерения.
   Это может быть функциональность/платформа, предметная-область/инфраструктура,
   фронт-энд/бэк-энд или интерфейс/реализация.
2. Продумайте, какие операции будут нужны клиентам, и опишите их в базовом
   классе абстракции.
3. Определите поведения, доступные на всех платформах, и
   выделите из них ту часть, которая нужна абстракции. На
   основании этого опишите общий интерфейс реализации.
4. Для каждой платформы создайте свой класс конкретной
   реализации. Все они должны следовать общему интерфейсу,
   который мы выделили перед этим.
5. Добавьте в класс абстракции ссылку на объект реализации.
   Реализуйте методы абстракции, делегируя основную работу
   связанному объекту реализации.
6. Если у вас есть несколько вариаций абстракции, создайте
   для каждой из них свой подкласс.
7. Клиент должен подать объект реализации в конструктор
   абстракции, что бы связать их воедино. После этого он может свободно
   использовать объект абстракции, забыв о реализации.

## Преимущества и недостатки

- \+ Позволяет строить платформо-независимые программы.
- \+ Скрывает лишние или опасные детали реализации от клиентского кода.
- \+ Реализует принцип открытости/закрытости.
- \- Усложняет код программы из-за введения дополнительных классов.