package lumine.command;

import lumine.storage.Storage;
import lumine.task.TaskList;
import lumine.ui.Ui;

/**
 * Represents a command to find tasks by searching for a keyword.
 */
public class FindCommand extends Command {
    private final String keyword;

    /**
     * Creates a command that will find tasks containing the given keyword.
     *
     * @param keyword the search keyword to filter tasks by
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    /** Finds and displays all tasks that contain the search keyword. */
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) {
        ui.showMessage(taskList.formatMatchingTasks(keyword));
    }
}
