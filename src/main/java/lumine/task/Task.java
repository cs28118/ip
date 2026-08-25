package lumine.task;

import lumine.LumineException;

public class Task {
    protected String description;
    protected boolean isDone;
    protected TaskType taskType;

    public Task(String description) {
        this(description, TaskType.TODO);
    }

    public Task(String description, TaskType taskType) {
        this.description = requireText(description, "description");
        this.isDone = false;
        if (taskType == null) {
            throw new LumineException("Sorry, task type cannot be empty. :C");
        }
        this.taskType = taskType;
    }

    public void markDone() {
        this.isDone = true;
    }

    public void markUndone() {
        this.isDone = false;
    }

    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    public String toFileString() {
        return taskType.getSymbol() + " | " + (isDone ? "1" : "0") + " | "
                + escapeStorageField(description);
    }

    //error handling: special symbol
    protected static String escapeStorageField(String value) {
        return value.replace("\\", "\\\\")
                .replace("|", "\\|")
                .replace("\r", "\\r")
                .replace("\n", "\\n");
    }

    //error handling: validate empty input
    protected static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new LumineException("Sorry, task " + fieldName + " cannot be empty. :C");
        }
        return value;
    }

    @Override
    public String toString() {
        return "[" + taskType.getSymbol() + "][" + this.getStatusIcon() + "] " + description;
    }
}
