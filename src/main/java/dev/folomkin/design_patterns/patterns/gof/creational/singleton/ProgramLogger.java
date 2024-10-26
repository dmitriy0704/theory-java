package dev.folomkin.design_patterns.patterns.gof.creational.singleton;

public class ProgramLogger {
    private static ProgramLogger instance;
    private static String logFile = "This is log file \n\n";

    public static synchronized ProgramLogger getInstance() {
        if (instance == null) {
            instance = new ProgramLogger();
        }
        return instance;
    }

    private ProgramLogger() {
    }

    public void addLogInfo(String logInfo) {
        logFile += logInfo;
    }

    public void getLogInfo() {
        System.out.println("Log: " + logFile);
    }
}
