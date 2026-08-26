package lumine;

import lumine.command.Command;
import lumine.parser.Parser;
import lumine.storage.Storage;
import lumine.task.TaskList;
import lumine.ui.Ui;

/**
 * Main application class for Lumine, a personal task-management chatbot.
 *
 * <p>Wires together the {@link Ui}, {@link Parser}, {@link Storage}, and
 * {@link TaskList} components, then drives the read-parse-execute loop
 * until the user exits.</p>
 */
public class Lumine {
    private Storage storage;
    private TaskList taskList;
    private Ui ui;
    private Parser parser;
    private String loadError;

    /**
     * Creates a new Lumine instance backed by the given save file.
     * If the file cannot be loaded, the error message is stored and
     * shown to the user when the main loop starts.
     *
     * @param filePath path to the task save file (created if absent)
     */
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

    /**
     * Starts the main input loop: reads commands from the user,
     * parses and executes each one, and repeats until an
     * {@link lumine.command.ExitCommand} is encountered or input ends.
     */
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

    /** Application entry point; starts a new Lumine session with the default save file. */
    public static void main(String[] args) {
        new Lumine("data/lumine.txt").run();
    }
}
