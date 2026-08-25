import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TaskList {
    private static final DateTimeFormatter DATE_COMMAND_FORMAT =
            DateTimeFormatter.ofPattern("uuuu MM dd");
    private final Storage storage;
    private final List<Task> tasks = new ArrayList<>();

    public TaskList(Storage storage) {
        this(storage, true);
    }


    TaskList(Storage storage, boolean loadSavedTasks) {
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
        String confirm = "Got it. I've added this task:\n  "
                + task + "\n" + "Now, you have " + tasks.size() + " tasks in the list.";
        System.out.println(confirm);
    }

    public void printTasks() {
        StringBuilder result = new StringBuilder();
        result.append("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            result.append("\n").append(i + 1).append(".").append(tasks.get(i));
        }
        System.out.println(result);
    }

    /** Prints pending deadlines and events whose relevant date matches the requested date. */
    public void printTasksDueOn(LocalDate date) {
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
            System.out.println("You have no task due on " + formattedDate + ".");
        } else {
            System.out.println(result);
        }
    }

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

    public int size() {
        return tasks.size();
    }

    private void saveTasks() {
        storage.save(tasks);
    }
}
