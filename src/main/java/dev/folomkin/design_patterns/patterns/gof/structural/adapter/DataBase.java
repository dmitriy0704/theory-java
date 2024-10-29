package dev.folomkin.design_patterns.patterns.gof.structural.adapter;

public interface DataBase {
    void insert();

    void update();

    void select();

    void delete();
}
