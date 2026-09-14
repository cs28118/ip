package lumine.command;

import lumine.task.TaskList;
import lumine.ui.Ui;

/**
 * Lists all tasks.
 */
public class ListCommand extends Command {

    /** Displays all tasks in the task list. */
    @Override
    public void execute(TaskList taskList, Ui ui) {
        ui.showMessage(taskList.formatTasks());
    }
}
