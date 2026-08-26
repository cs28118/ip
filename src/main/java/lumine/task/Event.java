package lumine.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;

/**
 * A task that spans a time range, with an explicit start ({@code /from}) and
 * end ({@code /to}) time.
 *
 * <p>Each time field can be free text, a calendar date ({@code yyyy MM dd}),
 * or a date-time ({@code yyyy MM dd HHmm}); structured values are formatted
 * nicely for display.</p>
 */
public class Event extends Task {
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

    protected String from;
    protected String to;
    private LocalDate fromDate;
    private LocalDateTime fromDateTime;
    private LocalDate toDate;
    private LocalDateTime toDateTime;

    /**
     * Creates a new event task.
     *
     * @param description the task description (must not be blank)
     * @param from        the start time string (must not be blank)
     * @param to          the end time string (must not be blank)
     */
    public Event(String description, String from, String to) {
        super(description, TaskType.EVENT);
        this.from = requireText(from, "event start time");
        this.to = requireText(to, "event end time");
        parseDateTime();
    }

    /**
     * Returns the pipe-delimited storage representation, including the
     * {@code from} and {@code to} fields.
     */
    @Override
    public String toFileString() {
        return super.toFileString() + " | " + escapeStorageField(getStorageFrom())
                + " | " + escapeStorageField(getStorageTo());
    }

    /** Returns the human-readable representation, appending {@code (from: ... to: ...)}. */
    @Override
    public String toString() {
        return super.toString() + " (from: " + getDisplayFrom() + " to: " + getDisplayTo() + ")";
    }

    /**
     * Attempts to parse {@link #from} and {@link #to} independently as a structured
     * date ({@code yyyy MM dd}) or date-time ({@code yyyy MM dd HHmm}).
     * Fields that cannot be parsed are kept as raw text for display.
     */
    private void parseDateTime() {
        String normalizedFrom = from.replaceAll("\\s+", " ");
        String normalizedTo = to.replaceAll("\\s+", " ");
        try {
            if (normalizedFrom.matches("\\d{4} \\d{2} \\d{2}")) {
                fromDate = LocalDate.parse(normalizedFrom, INPUT_DATE_FORMAT);
            } else if (normalizedFrom.matches("\\d{4} \\d{2} \\d{2} \\d{4}")) {
                fromDateTime = LocalDateTime.parse(normalizedFrom, INPUT_DATE_TIME_FORMAT);
            }
        } catch (DateTimeParseException ignored) {
        }

        try {
            if (normalizedTo.matches("\\d{4} \\d{2} \\d{2}")) {
                toDate = LocalDate.parse(normalizedTo, INPUT_DATE_FORMAT);
            } else if (normalizedTo.matches("\\d{4} \\d{2} \\d{2} \\d{4}")) {
                toDateTime = LocalDateTime.parse(normalizedTo, INPUT_DATE_TIME_FORMAT);
            }
        } catch (DateTimeParseException ignored) {
        }
    }

    /**
     * Returns the start time normalised to canonical input format for storage,
     * or the raw {@link #from} string when no structured date was parsed.
     */
    private String getStorageFrom() {
        if (fromDateTime != null) {
            return fromDateTime.format(INPUT_DATE_TIME_FORMAT);
        }
        if (fromDate != null) {
            return fromDate.format(INPUT_DATE_FORMAT);
        }
        return from;
    }

    /**
     * Returns the end time normalised to canonical input format for storage,
     * or the raw {@link #to} string when no structured date was parsed.
     */
    private String getStorageTo() {
        if (toDateTime != null) {
            return toDateTime.format(INPUT_DATE_TIME_FORMAT);
        }
        if (toDate != null) {
            return toDate.format(INPUT_DATE_FORMAT);
        }
        return to;
    }

    /**
     * Returns the start time formatted for display to the user,
     * or the raw {@link #from} string when no structured date was parsed.
     */
    private String getDisplayFrom() {
        if (fromDateTime != null) {
            return fromDateTime.format(DISPLAY_DATE_TIME_FORMAT);
        }
        if (fromDate != null) {
            return fromDate.format(DISPLAY_DATE_FORMAT);
        }
        return from;
    }

    /**
     * Returns the end time formatted for display to the user,
     * or the raw {@link #to} string when no structured date was parsed.
     */
    private String getDisplayTo() {
        if (toDateTime != null) {
            return toDateTime.format(DISPLAY_DATE_TIME_FORMAT);
        }
        if (toDate != null) {
            return toDate.format(DISPLAY_DATE_FORMAT);
        }
        return to;
    }

    /** Returns the calendar date on which this event ends, or null when it is plain text. */
    public LocalDate getToDate() {
        if (toDateTime != null) {
            return toDateTime.toLocalDate();
        }
        return toDate;
    }
}
