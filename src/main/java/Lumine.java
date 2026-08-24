import java.time.LocalDate;

public class Lumine {
    // main method
    public static void main(String[] args) {
        Ui ui = new Ui();
        Parser parser = new Parser();

        //greeting
        ui.showGreetings();

        //get inputs
        TaskList taskList;
        try {
            taskList = new TaskList();
        } catch (LumineException e) {
            ui.showSeparator();
            ui.showMessage(e.getMessage());
            ui.showSeparator();
            taskList = new TaskList(false);
        }
        while (ui.hasNextCommand()) {
            String command = ui.readCommand();
            ui.showSeparator();
            boolean shouldExit = false;
            try {
                shouldExit = handleCommand(command, taskList, ui, parser);
            } catch (LumineException e) {
                ui.showMessage(e.getMessage());
            }
            
            ui.showSeparator();

            if (shouldExit) {
                break;
            }
        }
    }


    /** helper methods */
    // command handle helper method
    private static boolean handleCommand(String command, TaskList taskList, Ui ui, Parser parser) {
        String normalizedCommand = parser.normalize(command);
        if (normalizedCommand.equals("bye")) {
            ui.showExit();
            return true;
        } else if (normalizedCommand.equals("list")) {
            taskList.printTasks();
        } else if (parser.isCommand(normalizedCommand, "date")) {
            taskList.printTasksDueOn(parser.parseDateCommand(normalizedCommand));
        } else if (parser.isCommand(normalizedCommand, "todo")) {
            taskList.addTask(parser.parseTodoCommand(normalizedCommand));
        } else if (parser.isCommand(normalizedCommand, "deadline")) {
            taskList.addTask(parser.parseDeadlineCommand(normalizedCommand));
        } else if (parser.isCommand(normalizedCommand, "event")) {
            taskList.addTask(parser.parseEventCommand(normalizedCommand));
        } else if (parser.isCommand(normalizedCommand, "mark")) {
            markTask(normalizedCommand, taskList, parser);
        } else if (parser.isCommand(normalizedCommand, "unmark")) {
            unmarkTask(normalizedCommand, taskList, parser);
        } else if (parser.isCommand(normalizedCommand, "delete")) {
            deleteTask(normalizedCommand, taskList, parser);
        } else {
            throw new LumineException("Hmmmm, I can't understand what that means. ;-;\n"
                    + "Try entering a command instead.");
        }
        return false;
    }

    //mark a task and output
    private static void markTask(String command, TaskList taskList, Parser parser) {
        int taskNumber = parser.parseTaskNumber(command, "mark");
        Task task = taskList.markAsDone(taskNumber);
        System.out.println("Nice! I've marked this task as done:");
        System.out.println(task);
    }

    //unmark a task and output
    private static void unmarkTask(String command, TaskList taskList, Parser parser) {
        int taskNumber = parser.parseTaskNumber(command, "unmark");
        Task task = taskList.markAsUndone(taskNumber);
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println(task);
    }

    //delete a task and output
    private static void deleteTask(String command, TaskList taskList, Parser parser) {
        int taskNumber = parser.parseTaskNumber(command, "delete");
        Task task = taskList.deleteTask(taskNumber);
        System.out.println("Noted. I've removed this task:");
        System.out.println(task);
        System.out.println("Now you have " + taskList.size() + " tasks in the list.");
    }

}
