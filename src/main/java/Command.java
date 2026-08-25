/**
 * Represents a user command that can be executed against the application's
 * task list, UI, and storage components.
 *
 * <p>Each concrete subclass encapsulates one type of user action
 * (e.g., adding a task, deleting a task, exiting). This lets the main
 * loop delegate behaviour to the command object without a long if-else chain.</p>
 */
public abstract class Command {

    /**
     * Executes this command.
     *
     * @param taskList the task list to operate on
     * @param ui       the UI to display output through
     * @param storage  the storage (available for future use by subclasses)
     */
    public abstract void execute(TaskList taskList, Ui ui, Storage storage);

    /**
     * Returns whether the application should exit after this command.
     * Only {@link ExitCommand} overrides this to return {@code true}.
     */
    public boolean isExit() {
        return false;
    }
}
