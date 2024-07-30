package dev.folomkin.algos;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ExampleCode {
    public static void main(String[] args) {
        String[] init = {"Один", "Два", "Три", "Один", "Два", "Три"};
        List<String> list = new ArrayList<>(Arrays.asList(init));
        System.out.println("Начальное значение списка " + list);
        //Сортировка списка
        Collections.sort(list);
        System.out.println("Список после сортировки: " + list);
    }
}
