/** Command that marks a task as done. */
public class MarkCommand extends Command {
    private final int taskNumber;

    public MarkCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) {
        Task task = taskList.markAsDone(taskNumber);
        ui.showMessage("Nice! I've marked this task as done:");
        ui.showMessage(task.toString());
    }
}
