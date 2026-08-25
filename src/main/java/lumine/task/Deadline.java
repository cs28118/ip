package lumine.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;

import lumine.LumineException;

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

    public Deadline(String description, String by) {
        super(description, TaskType.DEADLINE);
        this.by = requireText(by, "deadline time");
        parseDateTime();
    }

    @Override
    public String toFileString() {
        return super.toFileString() + " | " + escapeStorageField(getStorageDeadline());
    }

    @Override
    public String toString() {
        return super.toString() + " (by: " + getDisplayDeadline() + ")";
    }


    /** Helper functions */
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

    private String getStorageDeadline() {
        if (dueDateTime != null) {
            return dueDateTime.format(INPUT_DATE_TIME_FORMAT);
        }
        if (dueDate != null) {
            return dueDate.format(INPUT_DATE_FORMAT);
        }
        return by;
    }

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
