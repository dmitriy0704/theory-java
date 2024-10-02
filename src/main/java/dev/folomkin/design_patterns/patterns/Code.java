package dev.folomkin.design_patterns.patterns;


import java.util.Queue;

class BuildTest{
    public void build(Queue<Quest> queue){
        queue.add(makeQuest(параметры));
        // реализация
    }
    private Quest makeQuest(){
        // реализация
        return new Quest(параметры);
    }
}

class Test {
    private int idTest;
    private String testName;
    private int questNumber;
    private long time;
}

class Quest {
    private int idQuest;
    private int idTest;
// реализация конструкторов и методов
}

class CurrentStateTest {
    private int idT;
    private int idS;
    private int idCurrentQuest;
    private long timeRemain;
    private Queue<Long> idListQuest;
// конструкторы и методы
}

public class Code {
    public static void main(String[] args) {

    }
}