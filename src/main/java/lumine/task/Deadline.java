package lumine.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;

import lumine.LumineException;

/**
 * A task that must be completed by a specific deadline.
 *
 * <p>The deadline can be a free-text string, a calendar date ({@code yyyy MM dd}),
 * or a date-time ({@code yyyy MM dd HHmm}).  Structured values are parsed on
 * construction and formatted nicely for display (e.g. {@code Nov 09 2019 18:00}).</p>
 */
public class Deadline extends Task {
    private static final DateTimeFormatter INPUT_DATE_FORMAT = DateTimeFormatter
            .ofPattern("uuuu MM dd")
            .withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter INPUT_DATE_TIME_FORMAT = DateTimeFormatter
            .ofPattern("uuuu MM dd HHmm")
            .withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter DISPLAY_DATE_FORMAT = DateTimeFormatter
            .ofPattern("MMM dd uuuu", Locale.ENGLISH);
    private static final DateTimeFormatter DISPLAY_DATE_TIME_FORMAT = DateTimeFormatter
            .ofPattern("MMM dd uuuu HH:mm", Locale.ENGLISH);

    protected String by;
    private LocalDate dueDate;
    private LocalDateTime dueDateTime;

    /**
     * Creates a new deadline task.
     *
     * @param description the task description (must not be blank)
     * @param by          the deadline string; may be free text, {@code yyyy MM dd},
     *                    or {@code yyyy MM dd HHmm}
     */
    public Deadline(String description, String by) {
        super(description, TaskType.DEADLINE);
        this.by = requireText(by, "deadline time");
        parseDateTime();
    }

    /**
     * Returns the pipe-delimited storage representation, including the deadline field.
     * Structured dates are normalised back to the canonical input format before saving.
     */
    @Override
    public String toFileString() {
        return super.toFileString() + " | " + escapeStorageField(getStorageDeadline());
    }

    /** Returns the human-readable representation, appending {@code (by: <deadline>)}. */
    @Override
    public String toString() {
        return super.toString() + " (by: " + getDisplayDeadline() + ")";
    }


    /**
     * Attempts to parse {@link #by} as a structured date ({@code yyyy MM dd})
     * or date-time ({@code yyyy MM dd HHmm}).  If parsing fails or the format
     * is unrecognised, {@code dueDate} and {@code dueDateTime} remain {@code null}
     * and the raw text is kept as-is for display.
     */
    private void parseDateTime() {
        String normalizedBy = by.replaceAll("\\s+", " ");
        try {
            if (normalizedBy.matches("\\d{4} \\d{2} \\d{2}")) {
                dueDate = LocalDate.parse(normalizedBy, INPUT_DATE_FORMAT);
            } else if (normalizedBy.matches("\\d{4} \\d{2} \\d{2} \\d{4}")) {
                dueDateTime = LocalDateTime.parse(normalizedBy, INPUT_DATE_TIME_FORMAT);
            }
        } catch (DateTimeParseException ignored) {
        }
    }

    /**
     * Returns the deadline value normalised to canonical input format for storage
     * (e.g. {@code 2019 11 09} or {@code 2019 11 09 1800}), or the raw {@link #by}
     * string when no structured date was parsed.
     */
    private String getStorageDeadline() {
        if (dueDateTime != null) {
            return dueDateTime.format(INPUT_DATE_TIME_FORMAT);
        }
        if (dueDate != null) {
            return dueDate.format(INPUT_DATE_FORMAT);
        }
        return by;
    }

    /**
     * Returns the deadline formatted for display to the user
     * (e.g. {@code Nov 09 2019} or {@code Nov 09 2019 18:00}), or the raw
     * {@link #by} string when no structured date was parsed.
     */
    private String getDisplayDeadline() {
        if (dueDateTime != null) {
            return dueDateTime.format(DISPLAY_DATE_TIME_FORMAT);
        }
        if (dueDate != null) {
            return dueDate.format(DISPLAY_DATE_FORMAT);
        }
        return by;
    }

    /** Returns the calendar date of this deadline, or null when it is plain text. */
    public LocalDate getDueDate() {
        if (dueDateTime != null) {
            return dueDateTime.toLocalDate();
        }
        return dueDate;
    }
}
