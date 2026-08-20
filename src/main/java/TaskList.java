public class TaskList {
    private static final int MAX_TASKS = 100;
    private final Task[] tasks = new Task[MAX_TASKS];
    private int taskCount = 0;

    public void addTask(String task) {
        if (taskCount < MAX_TASKS) {
            tasks[taskCount] = new Task(task);
            taskCount++;
            System.out.println("added: " + task);
        }
    }

    public void printTasks() {
        StringBuilder result = new StringBuilder();
        result.append("Here are the tasks in your list:");
        for (int i = 0; i < taskCount; i++) {
            result.append("\n").append(i + 1).append(".").append(tasks[i]);
        }
        System.out.println(result);
    }

    //throw exception if task number is out of range
    public Task markAsDone(int taskNumber) {
        if (taskNumber < 1 || taskNumber > taskCount) {
            throw new IllegalArgumentException("Task number is out of range.");
        }

        Task task = tasks[taskNumber - 1];
        task.markDone();
        return task;
    }

    public Task markAsUndone(int taskNumber) {
        if (taskNumber < 1 || taskNumber > taskCount) {
            throw new IllegalArgumentException("Task number is out of range.");
        }

        Task task = tasks[taskNumber - 1];
        task.markUndone();
        return task;
    }
}
