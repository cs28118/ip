package lumine.command;

import lumine.storage.Storage;
import lumine.task.Task;
import lumine.task.TaskList;
import lumine.ui.Ui;

/** Command that deletes a task by its displayed number. */
public class DeleteCommand extends Command {
    private final int taskNumber;

    public DeleteCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) {
        Task task = taskList.deleteTask(taskNumber);
        ui.showMessage("Noted. I've removed this task:");
        ui.showMessage(task.toString());
        ui.showMessage("Now you have " + taskList.size() + " tasks in the list.");
    }
}
