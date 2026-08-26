package lumine.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import lumine.LumineException;

/**
 * Tests for {@link Deadline}.
 *
 * <p>The key complexity in Deadline is its three-branch /by parsing:
 * <ol>
 *   <li>Plain text  (stored and displayed as-is)</li>
 *   <li>Date only   "yyyy MM dd" (displayed as "MMM dd yyyy")</li>
 *   <li>Date + time "yyyy MM dd HHmm" (displayed as "MMM dd yyyy HH:mm")</li>
 * </ol>
 * Each branch affects {@code toString}, {@code toFileString}, and
 * {@code getDueDate}, so all three outcomes are verified for each method.
 */
class DeadlineTest {

    // -------------------------------------------------------------------------
    // toString
    // -------------------------------------------------------------------------

    @Test
    void toString_plainTextBy_displayedAsIs() {
        Deadline d = new Deadline("test", "Monday 5pm");
        assertEquals("[D][ ] test (by: Monday 5pm)", d.toString());
    }

    @Test
    void toString_dateOnlyBy_formatsToMmmDdYyyy() {
        Deadline d = new Deadline("test", "2026 01 01");
        assertEquals("[D][ ] test (by: Jan 01 2026)", d.toString());
    }

    @Test
    void toString_dateTimeBy_formatsToMmmDdYyyyHhMm() {
        Deadline d = new Deadline("test", "2026 01 01 1200");
        assertEquals("[D][ ] test (by: Jan 01 2026 12:00)", d.toString());
    }

    @Test
    void toString_doneDeadline_showsXStatusIcon() {
        Deadline d = new Deadline("test", "Monday");
        d.markDone();
        assertEquals("[D][X] test (by: Monday)", d.toString());
    }

    @Test
    void toString_undoneDeadline_hideXStatusIcon() {
        Deadline d = new Deadline("test", "Monday");
        d.markDone();
        assertEquals("[D][X] test (by: Monday)", d.toString());
        d.markUndone();
        assertEquals("[D][ ] test (by: Monday)", d.toString());
    }

    // -------------------------------------------------------------------------
    // toFileString
    // -------------------------------------------------------------------------

    @Test
    void toFileString_plainTextBy_storedAsIs() {
        Deadline d = new Deadline("test", "Monday 2pm");
        assertEquals("D | 0 | test | Monday 2pm", d.toFileString());
    }

    @Test
    void toFileString_dateOnlyBy_storedInYyyyMmDdFormat() {
        Deadline d = new Deadline("test", "2026 01 01");
        assertEquals("D | 0 | test | 2026 01 01", d.toFileString());
    }

    @Test
    void toFileString_dateTimeBy_storedInYyyyMmDdHhmmFormat() {
        Deadline d = new Deadline("test", "2026 01 01 1200");
        assertEquals("D | 0 | test | 2026 01 01 1200", d.toFileString());
    }

    @Test
    void toFileString_doneDeadline_containsOneFlag() {
        Deadline d = new Deadline("test", "Monday");
        d.markDone();
        assertEquals("D | 1 | test | Monday", d.toFileString());
    }

    @Test
    void toFileString_undoneDeadline_containsNoFlag() {
        Deadline d = new Deadline("test", "Monday");
        d.markDone();
        assertEquals("D | 1 | test | Monday", d.toFileString());
        d.markUndone();
        assertEquals("D | 0 | test | Monday", d.toFileString());
    }

    // -------------------------------------------------------------------------
    // getDueDate
    // -------------------------------------------------------------------------

    @Test
    void getDueDate_plainTextBy_returnsNull() {
        Deadline d = new Deadline("test", "Monday 2pm");
        assertNull(d.getDueDate());
    }

    @Test
    void getDueDate_dateOnlyBy_returnsCorrectLocalDate() {
        Deadline d = new Deadline("test", "2026 01 01");
        assertEquals(LocalDate.of(2026, 1, 1), d.getDueDate());
    }

    @Test
    void getDueDate_dateTimeBy_returnsDatePortionOnly() {
        Deadline d = new Deadline("test", "2026 01 01 1200");
        assertEquals(LocalDate.of(2026, 1, 1), d.getDueDate());
    }

    // -------------------------------------------------------------------------
    // constructor validation
    // -------------------------------------------------------------------------

    @Test
    void constructor_emptyBy_throwsLumineException() {
        assertThrows(LumineException.class, () -> new Deadline("test", "  "));
    }

    @Test
    void constructor_nullDescription_throwsLumineException() {
        assertThrows(LumineException.class, () -> new Deadline(null, "Monday"));
    }
}
