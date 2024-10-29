package dev.folomkin.design_patterns.patterns.gof.structural.adapter;

public class DataBaseRunner_Client {
    public static void main(String[] args) {
        DataBase dataBase = new AdapterJavaToDataBase();

        dataBase.insert();
        dataBase.select();
        dataBase.delete();
        dataBase.select();
        dataBase.update();
    }
}
