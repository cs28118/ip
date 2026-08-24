import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

//Saves the current task list to its fixed on-disk data file.
public class Storage {
    //location
    private static final Path SAVE_FILE = Path.of("data", "lumine.txt");

    //save
    public void save(List<Task> tasks) {
        try {
            Files.createDirectories(SAVE_FILE.getParent());
            String savedTasks = tasks.stream()
                    .map(Task::toFileString)
                    .reduce((first, second) -> first + System.lineSeparator() + second)
                    .orElse("");
            Files.writeString(SAVE_FILE, savedTasks);
        } catch (IOException e) {
            throw new LumineException("Sorry, I couldn't save your tasks. :C");
        }
    }
}
