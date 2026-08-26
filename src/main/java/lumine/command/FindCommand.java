package lumine.command;

import lumine.storage.Storage;
import lumine.task.TaskList;
import lumine.ui.Ui;

/**
 * Represents a command to find tasks by searching for a keyword.
 */
public class FindCommand extends Command {
    private final String keyword;

    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) {
        ui.showMessage(taskList.formatMatchingTasks(keyword));
    }
}
