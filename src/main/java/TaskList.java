public class TaskList {
    private static final int MAX_TASKS = 100;
    private final Task[] tasks = new Task[MAX_TASKS];
    private int taskCount = 0;

    public void addTask(String task) {
        if (taskCount < MAX_TASKS) {
            tasks[taskCount] = new Task(task);
            taskCount++;
            System.out.println(" added: " + task);
        }
    }

    public void printTasks() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < taskCount; i++) {
            result.append(" ").append(i + 1).append(". ").append(tasks[i]);
            if (i < taskCount - 1) {
                result.append("\n");
            }
        }
        System.out.println(result);
    }
}
