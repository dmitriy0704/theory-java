# Шаблонный метод (Template)

**_Цель:_**

Определение основы класса и создание возможности подклассам переопределять его
части.

**_Для чего используется:_**

Определяет основу класса и позволяет подклассам переопределять некоторые его
части не изменяя его структуру в целом.

**_Пример использования:_**

- однократное использование различных частей класса, оставляя реализацию
  изменяющегося поведения на усмотрение подкласса;
- вычисление и локализация общего для всех подклассов поведения в родительском;\
- управление расширениями подклассов.

![template-method.png](/img/design_pattern/design_patterns/template_method.png)

На картинке:

**** -   

