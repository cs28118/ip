import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Scanner;

public class Lumine {
    // main method
    public static void main(String[] args) {
        String banner =
                " ___      __   __  __   __  ___   __    _  _______ \n"
                + "|   |    |  | |  ||  |_|  ||   | |  |  | ||       |\n"
                + "|   |    |  | |  ||       ||   | |   |_| ||    ___|\n"
                + "|   |    |  |_|  ||       ||   | |       ||   |___ \n"
                + "|   |___ |       ||       ||   | |  _    ||    ___|\n"
                + "|       ||       || ||_|| ||   | | | |   ||   |___ \n"
                + "|_______||_______||_|   |_||___| |_|  |__||_______|\n";
        String line = "____________________________________________________________";
        String greeting = line + "\n" + banner
                + "Hello, I'm Lumine!\n"
                + "What can I do for you today?\n"
                + line;

        //greeting
        System.out.println(greeting);

        //get inputs
        Scanner scanner = new Scanner(System.in);
        TaskList taskList;
        try {
            taskList = new TaskList();
        } catch (LumineException e) {
            System.out.println(line);
            System.out.println(e.getMessage());
            System.out.println(line);
            taskList = new TaskList(false);
        }
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            System.out.println(line);

            boolean shouldExit = false;
            try {
                shouldExit = handleCommand(command, taskList);
            } catch (LumineException e) {
                System.out.println(e.getMessage());
            }

            System.out.println(line);

            if (shouldExit) {
                break;
            }
        }
    }


    /** helper methods */

    // command handle helper method
    private static boolean handleCommand(String command, TaskList taskList) {
        String normalizedCommand = command.trim();
        if (normalizedCommand.equals("bye")) {
            System.out.println("Bye. Hope to see you again soon!");
            return true;
        } else if (normalizedCommand.equals("list")) {
            taskList.printTasks();
        } else if (isCommand(normalizedCommand, "date")) {
            taskList.printTasksDueOn(parseDateCommand(normalizedCommand));
        } else if (isCommand(normalizedCommand, "todo")) {
            taskList.addTask(parseTodoCommand(normalizedCommand));
        } else if (isCommand(normalizedCommand, "deadline")) {
            taskList.addTask(parseDeadlineCommand(normalizedCommand));
        } else if (isCommand(normalizedCommand, "event")) {
            taskList.addTask(parseEventCommand(normalizedCommand));
        } else if (isCommand(normalizedCommand, "mark")) {
            markTask(normalizedCommand, taskList);
        } else if (isCommand(normalizedCommand, "unmark")) {
            unmarkTask(normalizedCommand, taskList);
        } else if (isCommand(normalizedCommand, "delete")) {
            deleteTask(normalizedCommand, taskList);
        } else {
            throw new LumineException("Hmmmm, I can't understand what that means. ;-;\n"
                    + "Try entering a command instead.");
        }
        return false;
    }

    //check if the command is known
    private static boolean isCommand(String command, String commandName) {
        return command.equals(commandName) || command.matches(commandName + "\\s+.*");
    }

    // Parse the date filter command without accepting invalid calendar dates.
    private static LocalDate parseDateCommand(String command) {
        String dateText = command.substring("date".length()).trim().replaceAll("\\s+", " ");
        if (!dateText.matches("\\d{4} \\d{2} \\d{2}")) {
            throw invalidDateCommand();
        }
        try {
            return LocalDate.parse(dateText, DateTimeFormatter.ofPattern("uuuu MM dd")
                    .withResolverStyle(ResolverStyle.STRICT));
        } catch (DateTimeParseException e) {
            throw invalidDateCommand();
        }
    }

    private static LumineException invalidDateCommand() {
        return new LumineException("Sorry, I can't understand what date is it. :C\n"
                + "It needs a valid date as the format yyyy mm dd");
    }

    //mark a task and output
    private static void markTask(String command, TaskList taskList) {
        int taskNumber = parseTaskNumber(command, "mark");
        Task task = taskList.markAsDone(taskNumber);
        System.out.println("Nice! I've marked this task as done:");
        System.out.println(task);
    }

    //unmark a task and output
    private static void unmarkTask(String command, TaskList taskList) {
        int taskNumber = parseTaskNumber(command, "unmark");
        Task task = taskList.markAsUndone(taskNumber);
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println(task);
    }

    //delete a task and output
    private static void deleteTask(String command, TaskList taskList) {
        int taskNumber = parseTaskNumber(command, "delete");
        Task task = taskList.deleteTask(taskNumber);
        System.out.println("Noted. I've removed this task:");
        System.out.println(task);
        System.out.println("Now you have " + taskList.size() + " tasks in the list.");
    }

    //check mark/unmark task number (non-int and not valid task number)
    private static int parseTaskNumber(String command, String commandName) {
        String taskNumber = command.substring(commandName.length()).trim();
        try {
            return Integer.parseInt(taskNumber);
        } catch (NumberFormatException e) {
            throw new LumineException("Task not found :<.\nPlease enter a valid task number.");
        }
    }

    //create todo and throw exception if empty
    private static Todo parseTodoCommand(String command) {
        String description = command.trim().substring("todo".length()).trim();
        if (description.isEmpty()) {
            throw new LumineException("Sorry, todo task cannot be empty. :C");
        }
        return new Todo(description);
    }

    //create deadline and throw exception if empty/invalid
    private static Deadline parseDeadlineCommand(String command) {
        String details = command.trim().substring("deadline".length()).trim();
        String[] parts = details.split("\\s+/by\\s+", 2);
        if (parts.length != 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            throw new LumineException(
                    "Sorry, I can't read your deadline task. :C\n"
                            + "It needs a description and a /by time.\n"
                            + "e.g. deadline test /by Mon 2pm");
        }
        String description = parts[0].trim();
        String by = parts[1].trim();
        return new Deadline(description, by);
    }

    //create event and throw exception if empty/invalid
    private static Event parseEventCommand(String command) {
        String details = command.trim().substring("event".length()).trim();
        String[] parts = details.split("\\s+/from\\s+|\\s+/to\\s+", 3);
        if (parts.length != 3 || parts[0].trim().isEmpty()
                || parts[1].trim().isEmpty() || parts[2].trim().isEmpty()) {
            throw new LumineException(
                    "Sorry, I can't read your event task. :C\n"
                            + "It needs a description, /from time, and /to time.\n"
                            + "e.g. event test /from Mon 2pm /to 4pm");
        }
        String description = parts[0].trim();
        String from = parts[1].trim();
        String to = parts[2].trim();
        return new Event(description, from, to);
    }
}
