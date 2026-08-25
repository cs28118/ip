package lumine.command;

import java.time.LocalDate;

import lumine.storage.Storage;
import lumine.task.TaskList;
import lumine.ui.Ui;

/** Command that lists pending tasks due on a given date. */
public class DateCommand extends Command {
    private final LocalDate date;

    public DateCommand(LocalDate date) {
        this.date = date;
    }

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) {
        ui.showMessage(taskList.formatTasksDueOn(date));
    }
}
