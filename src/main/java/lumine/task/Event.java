package lumine.task;

import java.time.LocalDate;

/**
 * A task that spans a time range, with an explicit start ({@code /from}) and
 * end ({@code /to}) time.
 *
 * <p>Each time field can be free text, a calendar date ({@code yyyy MM dd}),
 * or a date-time ({@code yyyy MM dd HHmm}); structured values are formatted
 * nicely for display.</p>
 */
public class Event extends Task {
    private final TaskDateTime startTime;
    private final TaskDateTime endTime;

    /**
     * Creates a new event task.
     *
     * @param description The task description (must not be blank).
     * @param from        The start time string (must not be blank).
     * @param to          The end time string (must not be blank).
     */
    public Event(String description, String from, String to) {
        super(description, TaskType.EVENT);
        this.startTime = new TaskDateTime(requireText(from, "event start time"));
        this.endTime = new TaskDateTime(requireText(to, "event end time"));
    }

    /**
     * Returns the pipe-delimited storage representation, including the
     * {@code from} and {@code to} fields.
     */
    @Override
    public String toFileString() {
        return super.toFileString() + " | " + escapeStorageField(startTime.formatForStorage())
                + " | " + escapeStorageField(endTime.formatForStorage());
    }

    /** Returns the human-readable representation, appending {@code (from: ... to: ...)}. */
    @Override
    public String toString() {
        return super.toString() + " (from: " + startTime.formatForDisplay()
                + " to: " + endTime.formatForDisplay() + ")";
    }

    /** Returns the calendar date on which this event ends, or null when it is plain text. */
    public LocalDate getToDate() {
        return endTime.toLocalDate();
    }

    @Override
    public boolean isDueOn(LocalDate date) {
        return date != null && date.equals(endTime.toLocalDate());
    }
}
