import java.util.ArrayList;
import java.util.List;

public class TaskList {
    private final Storage storage = new Storage();
    private final List<Task> tasks = new ArrayList<>();

    public TaskList() {
        this(true);
    }


    TaskList(boolean loadSavedTasks) {
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
