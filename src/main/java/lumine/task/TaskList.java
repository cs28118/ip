package lumine.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import lumine.LumineException;
import lumine.storage.Storage;

public class TaskList {
    private static final DateTimeFormatter DATE_COMMAND_FORMAT =
            DateTimeFormatter.ofPattern("uuuu MM dd");
    private final Storage storage;
    private final List<Task> tasks = new ArrayList<>();

    /**
     * Constructs a task list containing the tasks saved in the given storage.
     *
     * @param storage the storage to load from and save to
     */
    public TaskList(Storage storage) {
        this(storage, true);
    }

    /**
     * Constructs a task list, optionally attempting to load saved tasks.
     *
     * @param storage the storage to load from and save to
     * @param loadSavedTasks whether to load tasks from storage
     */
    public TaskList(Storage storage, boolean loadSavedTasks) {
        this.storage = storage;
        if (loadSavedTasks) {
            tasks.addAll(storage.load());
        }
    }

    public void addTask(Task task) {
        if (task == null) {
            throw new LumineException("Sorry, task cannot be empty. :C");
        }
        tasks.add(task);
        try {
            saveTasks();
        } catch (LumineException e) {
            tasks.removeLast();
            throw e;
        }
    }

    /** Returns a formatted listing of all tasks. */
    public String formatTasks() {
        StringBuilder result = new StringBuilder();
        result.append("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            result.append("\n").append(i + 1).append(".").append(tasks.get(i));
        }
        return result.toString();
    }

    /** Returns a formatted listing of pending deadlines and events due on the given date. */
    public String formatTasksDueOn(LocalDate date) {
        String formattedDate = date.format(DATE_COMMAND_FORMAT);
        StringBuilder result = new StringBuilder("Here is your list of pending task due on ")
                .append(formattedDate).append(":");
        int matchCount = 0;
        for (Task task : tasks) {
            boolean matches = false;
            if (!task.isDone && task instanceof Deadline deadline) {
                matches = date.equals(deadline.getDueDate());
            } else if (!task.isDone && task instanceof Event event) {
                matches = date.equals(event.getToDate());
            }
            if (matches) {
                result.append("\n").append(++matchCount).append(".").append(task);
            }
        }
        if (matchCount == 0) {
            return "You have no task due on " + formattedDate + ".";
        } else {
            return result.toString();
        }
    }

    //throw exception if task number is out of range
    public Task markAsDone(int taskNumber) {
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new LumineException("Task not found :<.\n"
                    + "Please enter a valid task number.");
        }

        Task task = tasks.get(taskNumber - 1);
        boolean wasDone = task.isDone;
        task.markDone();
        try {
            saveTasks();
        } catch (LumineException e) {
            task.isDone = wasDone;
            throw e;
        }
        return task;
    }

    public Task markAsUndone(int taskNumber) {
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new LumineException("Task not found :<.\n"
                    + "Please enter a valid task number.");
        }

        Task task = tasks.get(taskNumber - 1);
        boolean wasDone = task.isDone;
        task.markUndone();
        try {
            saveTasks();
        } catch (LumineException e) {
            task.isDone = wasDone;
            throw e;
        }
        return task;
    }

    public Task deleteTask(int taskNumber) {
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new LumineException("Task not found :<.\n"
                    + "Please enter a valid task number.");
        }

        int taskIndex = taskNumber - 1;
        Task removedTask = tasks.remove(taskIndex);
        try {
            saveTasks();
        } catch (LumineException e) {
            tasks.add(taskIndex, removedTask);
            throw e;
        }
        return removedTask;
    }

    public int size() {
        return tasks.size();
    }

    private void saveTasks() {
        storage.save(tasks);
    }
}
