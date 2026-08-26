package lumine.command;

import lumine.storage.Storage;
import lumine.task.TaskList;
import lumine.ui.Ui;

/** Command that exits the application. */
public class ExitCommand extends Command {

    /** Displays the farewell message before the application shuts down. */
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) {
        ui.showExit();
    }

    /** Always returns {@code true} to signal the main loop to stop. */
    @Override
    public boolean isExit() {
        return true;
    }
}
