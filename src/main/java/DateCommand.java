import java.time.LocalDate;

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
