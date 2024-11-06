package dev.folomkin.docs.design_patterns.patterns.gof.behavioral.command;

public class UpdateCommand implements Command {

    Database database;

    public UpdateCommand(Database database) {
        this.database = database;
    }

    @Override
    public void execute() {
        database.update();
    }
}
