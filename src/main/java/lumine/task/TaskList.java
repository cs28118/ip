package lumine.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import lumine.LumineException;
import lumine.storage.Storage;

/**
 * Manages the in-memory list of tasks and keeps storage in sync after every change.
 *
 * <p>All mutating operations ({@link #addTask}, {@link #markAsDone}, {@link #markAsUndone},
 * {@link #deleteTask}) save the list to disk atomically and roll back the in-memory
 * state if saving fails, so the two sources of truth never diverge.</p>
 */
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

    /**
     * Adds the given task to the list and persists the change to storage.
     * If saving fails the task is removed from the list and the exception is re-thrown.
     *
     * @param task the task to add (must not be {@code null})
     * @throws LumineException if {@code task} is null or storage cannot be written
     */
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

    /**
     * Marks the task at the given 1-based position as done and saves the list.
     *
     * @param taskNumber 1-based index of the task to mark
     * @return the task that was marked
     * @throws LumineException if the task number is out of range or saving fails
     */
    //throw exception if task number is out of range
    public Task markAsDone(int taskNumber) {
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new LumineException("Task not found :<.\n" +
                    "Please enter a valid task number.");
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

    /**
     * Marks the task at the given 1-based position as not done and saves the list.
     *
     * @param taskNumber 1-based index of the task to unmark
     * @return the task that was unmarked
     * @throws LumineException if the task number is out of range or saving fails
     */
    public Task markAsUndone(int taskNumber) {
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new LumineException("Task not found :<.\n" +
                    "Please enter a valid task number.");
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

    /**
     * Removes the task at the given 1-based position and saves the list.
     *
     * @param taskNumber 1-based index of the task to delete
     * @return the task that was removed
     * @throws LumineException if the task number is out of range or saving fails
     */
    public Task deleteTask(int taskNumber) {
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new LumineException("Task not found :<.\n" +
                    "Please enter a valid task number.");
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

    /** Returns the number of tasks currently in the list. */
    public int size() {
        return tasks.size();
    }

    /** Persists the current task list to storage; called after every mutating operation. */
    private void saveTasks() {
        storage.save(tasks);
    }
}
