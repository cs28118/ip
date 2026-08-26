package lumine.command;

import lumine.storage.Storage;
import lumine.task.TaskList;
import lumine.ui.Ui;

/** Command that lists all tasks. */
public class ListCommand extends Command {

    /** Displays all tasks in the task list. */
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) {
        ui.showMessage(taskList.formatTasks());
    }
}
