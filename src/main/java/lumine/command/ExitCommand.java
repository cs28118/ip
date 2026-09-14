package lumine.command;

import lumine.task.TaskList;
import lumine.ui.Ui;

/**
 * Exits the application.
 */
public class ExitCommand extends Command {

    /** Displays the farewell message before the application shuts down. */
    @Override
    public void execute(TaskList taskList, Ui ui) {
        ui.showExit();
    }

    /** Always returns {@code true} to signal the main loop to stop. */
    @Override
    public boolean isExit() {
        return true;
    }
}
