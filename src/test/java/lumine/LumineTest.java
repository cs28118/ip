package lumine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class LumineTest {
    @TempDir
    private Path temporaryDirectory;

    @Test
    void getGreeting_newSession_returnsGreeting() {
        Lumine lumine = createLumine();

        assertEquals("Hello, I'm Lumine!\nWhat can I do for you today?", lumine.getGreeting());
    }

    @Test
    void getResponse_addTodoCommand_returnsCommandOutput() {
        Lumine lumine = createLumine();

        assertEquals("Got it. I've added this task:\n  [T][ ] read textbook\n"
                        + "Now, you have 1 tasks in the list.",
                lumine.getResponse("todo read textbook"));
    }

    @Test
    void getResponse_exitCommand_returnsFarewellAndRequestsExit() {
        Lumine lumine = createLumine();

        assertEquals("Bye. Hope to see you again soon!", lumine.getResponse("bye"));
        assertTrue(lumine.isExitRequested());
    }

    private Lumine createLumine() {
        return new Lumine(temporaryDirectory.resolve("lumine.txt").toString());
    }
}
