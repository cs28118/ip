import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/** Saves and loads the task list from its fixed on-disk data file. */
public class Storage {
    /** Location of the save file relative to the project root. */
    private static final Path SAVE_FILE = Path.of("data", "lumine.txt");

    /**
     * Replaces the save file with the supplied current task list.
     *
     * @param tasks tasks to write to disk
     */
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

    /**
     * Loads all saved tasks, returning an empty list when no save file exists.
     *
     * @return tasks reconstructed from the save file
     */
    public List<Task> load() {
        if (!Files.exists(SAVE_FILE)) {
            return new ArrayList<>();
        }

        try {
            List<Task> tasks = new ArrayList<>();
            for (String line : Files.readAllLines(SAVE_FILE)) {
                if (!line.isBlank()) {
                    tasks.add(parseTask(line));
                }
            }
            return tasks;
        } catch (IOException e) {
            throw new LumineException("Sorry, I couldn't load your tasks. :C");
        }
    }

    /** Reconstructs one task from a line of the save format. */
    private Task parseTask(String line) {
        String[] parts = line.split("\\s*\\|\\s*", -1);
        if (parts.length < 3 || (!parts[1].equals("0") && !parts[1].equals("1"))) {
            throw new LumineException("Sorry, I couldn't load your tasks. :C");
        }

        Task task;
        switch (parts[0]) {
        case "T":
            if (parts.length != 3) {
                throw new LumineException("Sorry, I couldn't load your tasks. :C");
            }
            task = new Todo(parts[2]);
            break;
        case "D":
            if (parts.length != 4) {
                throw new LumineException("Sorry, I couldn't load your tasks. :C");
            }
            task = new Deadline(parts[2], parts[3]);
            break;
        case "E":
            if (parts.length != 5) {
                throw new LumineException("Sorry, I couldn't load your tasks. :C");
            }
            task = new Event(parts[2], parts[3], parts[4]);
            break;
        default:
            throw new LumineException("Sorry, I couldn't load your tasks. :C");
        }

        if (parts[1].equals("1")) {
            task.markDone();
        }
        return task;
    }
}
