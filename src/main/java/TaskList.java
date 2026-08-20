import java.util.ArrayList;
import java.util.List;

/**
 * Stores and manages the tasks entered by the user.
 */
public class TaskList {
    private final List<Task> tasks = new ArrayList<>();

    public void addTask(Task task) {
        tasks.add(task);
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
        task.markDone();
        return task;
    }

    public Task markAsUndone(int taskNumber) {
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new LumineException("Task not found :<.\n" +
                    "Please enter a valid task number.");
        }

        Task task = tasks.get(taskNumber - 1);
        task.markUndone();
        return task;
    }

    public Task deleteTask(int taskNumber) {
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new LumineException("Task not found :<.\n" +
                    "Please enter a valid task number.");
        }

        return tasks.remove(taskNumber - 1);
    }

    public int size() {
        return tasks.size();
    }
}
