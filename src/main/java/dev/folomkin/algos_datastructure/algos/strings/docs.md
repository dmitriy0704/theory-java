# Строки

**Преобразование строки в массив строк и сортировка по алфавиту.**

```java
    public static void main(String[] args) {
    String str = "Java PHP Python JavaScript Kotlin";
    String[] arrString = str.split("\\s");
    for (int j = 0; j < arrString.length; j++) {
        for (int i = j + 1; i < arrString.length; i++) {
            if (arrString[i].compareToIgnoreCase(arrString[j]) < 0) {
                String temp = arrString[j];
                arrString[j] = arrString[i];
                arrString[i] = temp;
            }
        }
    }
    for (String s : arrString) {
        if(!s.isEmpty())
            System.out.println(s);
    }

    Arrays.stream(arrString)
            .filter(s -> !s.isEmpty())
            .sorted(String::compareToIgnoreCase)
            .forEach(System.out::println);
}
```

**Удаление всех пробелов с помощью StringBuilder**:

```java
   public static void main(String[] args) {
        StringBuilder sb = new StringBuilder("
        sb.codePoints()
        .filter(o -> o!=' ')
        .forEach(o -> sb.append((char)o));
}
```


