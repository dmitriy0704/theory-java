package dev.folomkin.docs.design_patterns.patterns.gof.structural.adapter;

public class AdapterJavaToDataBase extends JavaApplication implements DataBase {
    @Override
    public void insert() {
        saveObject();
    }

    @Override
    public void update() {
        updateObject();
    }

    @Override
    public void select() {
        loadObject();
    }

    @Override
    public void delete() {
        deleteObject();
    }
}
