package lumine.task;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import lumine.LumineException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link Event}.
 *
 * <p>Like Deadline, Event has a three-branch datetime parser applied
 * independently to both the /from and /to fields:
 * <ol>
 *   <li>Plain text  (stored and displayed as-is)</li>
 *   <li>Date only   "yyyy MM dd" (displayed as "MMM dd yyyy")</li>
 *   <li>Date + time "yyyy MM dd HHmm" (displayed as "MMM dd yyyy HH:mm")</li>
 * </ol>
 * Each branch affects {@code toString}, {@code toFileString}, and
 * {@code getToDate}.
 */
class EventTest {

    // -------------------------------------------------------------------------
    // toString
    // -------------------------------------------------------------------------

    @Test
    void toString_plainTextTimes_displayedAsIs() {
        Event e = new Event("test", "Mon 2pm", "4pm");
        assertEquals("[E][ ] test (from: Mon 2pm to: 4pm)", e.toString());
    }

    @Test
    void toString_dateOnlyTimes_formatsToMmmDdYyyy() {
        Event e = new Event("test", "2025 12 31", "2026 01 01");
        assertEquals("[E][ ] test (from: Dec 31 2025 to: Jan 01 2026)", e.toString());
    }

    @Test
    void toString_dateTimeTimes_formatsToMmmDdYyyyHhMm() {
        Event e = new Event("test", "2025 12 31 1400", "2025 12 31 1600");
        assertEquals("[E][ ] test (from: Dec 31 2025 14:00 to: Dec 31 2025 16:00)", e.toString());
    }

    @Test
    void toString_doneEvent_showsXStatusIcon() {
        Event e = new Event("test", "Mon 2pm", "4pm");
        e.markDone();
        assertEquals("[E][X] test (from: Mon 2pm to: 4pm)", e.toString());
    }

    @Test
    void toString_undoneEvent_hideXStatusIcon() {
        Event e = new Event("test", "Mon 2pm", "4pm");
        e.markDone();
        assertEquals("[E][X] test (from: Mon 2pm to: 4pm)", e.toString());
        e.markUndone();
        assertEquals("[E][ ] test (from: Mon 2pm to: 4pm)", e.toString());
    }

    @Test
    void toString_mixedFromAndTo_eachFieldIndependentlyFormatted() {
        Event e = new Event("test", "Mon 2pm", "2025 12 31");
        assertEquals("[E][ ] test (from: Mon 2pm to: Dec 31 2025)", e.toString());
    }

    // -------------------------------------------------------------------------
    // toFileString
    // -------------------------------------------------------------------------

    @Test
    void toFileString_plainTextTimes_storedAsIs() {
        Event e = new Event("test", "Mon 2pm", "4pm");
        assertEquals("E | 0 | test | Mon 2pm | 4pm", e.toFileString());
    }

    @Test
    void toFileString_dateOnlyTimes_storedInYyyyMmDdFormat() {
        Event e = new Event("test", "2025 12 31", "2026 01 01");
        assertEquals("E | 0 | test | 2025 12 31 | 2026 01 01", e.toFileString());
    }

    @Test
    void toFileString_dateTimeTimes_storedInYyyyMmDdHhmmFormat() {
        Event e = new Event("test", "2025 12 31 1400", "2025 12 31 1600");
        assertEquals("E | 0 | test | 2025 12 31 1400 | 2025 12 31 1600", e.toFileString());
    }

    @Test
    void toFileString_doneEvent_containsOneFlag() {
        Event e = new Event("test", "Mon 2pm", "4pm");
        e.markDone();
        assertEquals("E | 1 | test | Mon 2pm | 4pm", e.toFileString());
    }

    @Test
    void toFileString_undoneEvent_containsNoFlag() {
        Event e = new Event("test", "Mon 2pm", "4pm");
        e.markDone();
        assertEquals("E | 1 | test | Mon 2pm | 4pm", e.toFileString());
        e.markUndone();
        assertEquals("E | 0 | test | Mon 2pm | 4pm", e.toFileString());
    }

    // -------------------------------------------------------------------------
    // getToDate
    // -------------------------------------------------------------------------

    @Test
    void getToDate_plainTextTo_returnsNull() {
        // When /to is free text there is no calendar date to return.
        Event e = new Event("test", "Mon 2pm", "4pm");
        assertNull(e.getToDate());
    }

    @Test
    void getToDate_dateOnlyTo_returnsCorrectLocalDate() {
        Event e = new Event("test", "Mon 2pm", "2025 12 31");
        assertEquals(LocalDate.of(2025, 12, 31), e.getToDate());
    }

    @Test
    void getToDate_dateTimeTo_returnsDatePortionOnly() {
        Event e = new Event("test", "2025 12 31 1400", "2025 12 31 1600");
        assertEquals(LocalDate.of(2025, 12, 31), e.getToDate());
    }

    // -------------------------------------------------------------------------
    // constructor validation
    // -------------------------------------------------------------------------

    @Test
    void constructor_emptyFrom_throwsLumineException() {
        assertThrows(LumineException.class, () -> new Event("test", "  ", "4pm"));
    }

    @Test
    void constructor_emptyTo_throwsLumineException() {
        assertThrows(LumineException.class, () -> new Event("test", "Mon 2pm", "  "));
    }

    @Test
    void constructor_nullDescription_throwsLumineException() {
        assertThrows(LumineException.class, () -> new Event(null, "Mon 2pm", "4pm"));
    }
}