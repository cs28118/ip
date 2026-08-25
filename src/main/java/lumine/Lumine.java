package lumine;

import lumine.command.Command;
import lumine.parser.Parser;
import lumine.storage.Storage;
import lumine.task.TaskList;
import lumine.ui.Ui;

public class Lumine {
    private Storage storage;
    private TaskList taskList;
    private Ui ui;
    private Parser parser;
    private String loadError;

    public Lumine(String filePath) {
        ui = new Ui();
        parser = new Parser();
        storage = new Storage(filePath);
        try {
            taskList = new TaskList(storage);
        } catch (LumineException e) {
            loadError = e.getMessage();
            taskList = new TaskList(storage, false);
        }
    }

    public void run() {
        ui.showGreetings();

        if (loadError != null) {
            ui.showSeparator();
            ui.showMessage(loadError);
            ui.showSeparator();
        }

        boolean isExit = false;
        while (!isExit && ui.hasNextCommand()) {
            String fullCommand = ui.readCommand();
            ui.showSeparator();
            try {
                Command command = parser.parse(fullCommand);
                command.execute(taskList, ui, storage);
                isExit = command.isExit();
            } catch (LumineException e) {
                ui.showMessage(e.getMessage());
            }
            ui.showSeparator();
        }
    }

    public static void main(String[] args) {
        new Lumine("data/lumine.txt").run();
    }
}
