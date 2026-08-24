public class Deadline extends Task {
    protected String by;

    public Deadline(String description, String by) {
        super(description, TaskType.DEADLINE);
        this.by = requireText(by, "deadline time");
    }

    @Override
    public String toFileString() {
        return super.toFileString() + " | " + escapeStorageField(by);
    }

    @Override
    public String toString() {
        return super.toString() + " (by: " + by + ")";
    }
}
