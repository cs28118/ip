package lumine.command;

import lumine.storage.Storage;
import lumine.task.TaskList;
import lumine.ui.Ui;

/** Command that exits the application. */
public class ExitCommand extends Command {

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) {
        ui.showExit();
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
