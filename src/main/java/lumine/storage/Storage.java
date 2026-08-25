package lumine.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lumine.LumineException;
import lumine.task.Deadline;
import lumine.task.Event;
import lumine.task.Task;
import lumine.task.Todo;

public class Storage {
    //Error: not loaded
    private static final String LOAD_ERROR = "Sorry, I couldn't load your tasks. :C";
    private static final String SAVE_ERROR = "Sorry, I couldn't save your tasks. :C";

    private final Path saveFile;

    public Storage(String filePath) {
        this.saveFile = Path.of(filePath);
    }

    public void save(List<Task> tasks) {
        if (tasks == null || tasks.stream().anyMatch(task -> task == null)) {
            throw new LumineException(SAVE_ERROR);
        }

        Path temporaryFile = null;
        try {
            Path parent = saveFile.getParent();
            Files.createDirectories(parent);
            if (Files.exists(saveFile) && !Files.isRegularFile(saveFile)) {
                throw new IOException("Save path is not a regular file");
            }
            String savedTasks = tasks.stream()
                    .map(Task::toFileString)
                    .collect(Collectors.joining(System.lineSeparator()));
            temporaryFile = Files.createTempFile(parent, "lumine-", ".tmp");
            Files.writeString(temporaryFile, savedTasks);
            Files.move(temporaryFile, saveFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException | SecurityException e) {
            throw new LumineException(SAVE_ERROR);
        } finally {
            if (temporaryFile != null) {
                try {
                    Files.deleteIfExists(temporaryFile);
                } catch (IOException | SecurityException ignored) {
                }
            }
        }
    }

    public List<Task> load() {
        try {
            if (!Files.exists(saveFile)) {
                return new ArrayList<>();
            }
            if (!Files.isRegularFile(saveFile)) {
                throw new LumineException(LOAD_ERROR);
            }

            List<Task> tasks = new ArrayList<>();
            List<String> lines = Files.readAllLines(saveFile);
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                if (!line.isBlank()) {
                    tasks.add(parseTask(line, i + 1));
                }
            }
            return tasks;
        } catch (IOException | SecurityException e) {
            throw new LumineException(LOAD_ERROR);
        }
    }

    /** Helper functions */

    private Task parseTask(String line, int lineNumber) {
        List<String> parts = splitFields(line, lineNumber);
        if (parts.size() < 3 || (!parts.get(1).equals("0") && !parts.get(1).equals("1"))) {
            throw invalidLine(lineNumber);
        }

        Task task = switch (parts.get(0)) {
            case "T" -> {
                if (parts.size() != 3 || parts.get(2).isBlank()) {
                    throw invalidLine(lineNumber);
                }
                yield new Todo(parts.get(2));
            }
            case "D" -> {
                if (parts.size() != 4 || parts.get(2).isBlank() || parts.get(3).isBlank()) {
                    throw invalidLine(lineNumber);
                }
                yield new Deadline(parts.get(2), parts.get(3));
            }
            case "E" -> {
                if (parts.size() != 5 || parts.get(2).isBlank()
                        || parts.get(3).isBlank() || parts.get(4).isBlank()) {
                    throw invalidLine(lineNumber);
                }
                yield new Event(parts.get(2), parts.get(3), parts.get(4));
            }
            default -> throw invalidLine(lineNumber);
        };

        if (parts.get(1).equals("1")) {
            task.markDone();
        }
        return task;
    }

    private List<String> splitFields(String line, int lineNumber) {
        List<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean escaped = false;
        for (int i = 0; i < line.length(); i++) {
            char character = line.charAt(i);
            if (escaped) {
                switch (character) {
                case 'n':
                    field.append('\n');
                    break;
                case 'r':
                    field.append('\r');
                    break;
                default:
                    field.append(character);
                    break;
                }
                escaped = false;
            } else if (character == '\\') {
                escaped = true;
            } else if (character == '|') {
                fields.add(field.toString().trim());
                field.setLength(0);
            } else {
                field.append(character);
            }
        }
        if (escaped) {
            throw invalidLine(lineNumber);
        }
        fields.add(field.toString().trim());
        return fields;
    }

    private LumineException invalidLine(int lineNumber) {
        return new LumineException(LOAD_ERROR + "\nInvalid saved task on line " + lineNumber + ".");
    }
}
