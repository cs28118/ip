/** Command that marks a task as not done. */
public class UnmarkCommand extends Command {
    private final int taskNumber;

    public UnmarkCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) {
        Task task = taskList.markAsUndone(taskNumber);
        ui.showMessage("OK, I've marked this task as not done yet:");
        ui.showMessage(task.toString());
    }
}
