package dev.folomkin.docs.design_patterns.patterns.gof.creational.singleton;

public class ProgramRunner {
    public static void main(String[] args) {

        ProgramLogger.getInstance().addLogInfo("First Log.. \n");
        ProgramLogger.getInstance().addLogInfo("Second Log.. \n");
        ProgramLogger.getInstance().addLogInfo("Third Log.. \n");

        ProgramLogger.getInstance().getLogInfo();

    }
}
