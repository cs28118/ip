public class Event extends Task {
    protected String from;
    protected String to;

    public Event(String description, String from, String to) {
        super(description, TaskType.EVENT);
        this.from = requireText(from, "event start time");
        this.to = requireText(to, "event end time");
    }

    @Override
    public String toFileString() {
        return super.toFileString() + " | " + escapeStorageField(from)
                + " | " + escapeStorageField(to);
    }

    @Override
    public String toString() {
        return super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
