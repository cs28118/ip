package lumine.task;

import java.time.LocalDate;

/**
 * A task that must be completed by a specific deadline.
 *
 * <p>The deadline can be a free-text string, a calendar date ({@code yyyy MM dd}),
 * or a date-time ({@code yyyy MM dd HHmm}).  Structured values are parsed on
 * construction and formatted nicely for display (e.g. {@code Nov 09 2019 18:00}).</p>
 */
public class Deadline extends Task {
    private final TaskDateTime deadline;

    /**
     * Creates a new deadline task.
     *
     * @param description The task description (must not be blank).
     * @param by          the deadline string; may be free text, {@code yyyy MM dd},
     *                    or {@code yyyy MM dd HHmm}.
     */
    public Deadline(String description, String by) {
        super(description, TaskType.DEADLINE);
        this.deadline = new TaskDateTime(requireText(by, "deadline time"));
    }

    /**
     * Returns the pipe-delimited storage representation, including the deadline field.
     * Structured dates are normalised back to the canonical input format before saving.
     */
    @Override
    public String toFileString() {
        return super.toFileString() + " | " + escapeStorageField(deadline.formatForStorage());
    }

    /** Returns the human-readable representation, appending {@code (by: <deadline>)}. */
    @Override
    public String toString() {
        return super.toString() + " (by: " + deadline.formatForDisplay() + ")";
    }

    /** Returns the calendar date of this deadline, or null when it is plain text. */
    public LocalDate getDueDate() {
        return deadline.toLocalDate();
    }

    @Override
    public boolean isDueOn(LocalDate date) {
        return date != null && date.equals(deadline.toLocalDate());
    }
}
