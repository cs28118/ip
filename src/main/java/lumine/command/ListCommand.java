package lumine.command;

import lumine.storage.Storage;
import lumine.task.TaskList;
import lumine.ui.Ui;

/** Command that lists all tasks. */
public class ListCommand extends Command {

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) {
        ui.showMessage(taskList.formatTasks());
    }
}
