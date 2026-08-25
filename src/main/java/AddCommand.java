/** Command that adds a task (todo, deadline, or event) to the task list. */
public class AddCommand extends Command {
    private final Task task;

    public AddCommand(Task task) {
        this.task = task;
    }

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) {
        taskList.addTask(task);
        ui.showMessage("Got it. I've added this task:\n  "
                + task + "\nNow, you have " + taskList.size() + " tasks in the list.");
    }
}
