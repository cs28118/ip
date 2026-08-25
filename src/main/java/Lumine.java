public class Lumine {
    private Storage storage;
    private TaskList taskList;
    private Ui ui;
    private Parser parser;

    public Lumine(String filePath) {
        ui = new Ui();
        parser = new Parser();
        storage = new Storage(filePath);
        
        ui.showGreetings();

        try {
            taskList = new TaskList(storage);
        } catch (LumineException e) {
            ui.showSeparator();
            ui.showMessage(e.getMessage());
            ui.showSeparator();
            taskList = new TaskList(storage, false);
        }
    }

    public void run() {
        while (ui.hasNextCommand()) {
            String command = ui.readCommand();
            ui.showSeparator();
            boolean shouldExit = false;
            try {
                shouldExit = handleCommand(command);
            } catch (LumineException e) {
                ui.showMessage(e.getMessage());
            }
            
            ui.showSeparator();

            if (shouldExit) {
                break;
            }
        }
    }

    public static void main(String[] args) {
        new Lumine("data/lumine.txt").run();
    }


    /** helper methods */
    // command handle helper method
    private boolean handleCommand(String command) {
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
            markTask(normalizedCommand);
        } else if (parser.isCommand(normalizedCommand, "unmark")) {
            unmarkTask(normalizedCommand);
        } else if (parser.isCommand(normalizedCommand, "delete")) {
            deleteTask(normalizedCommand);
        } else {
            throw new LumineException("Hmmmm, I can't understand what that means. ;-;\n"
                    + "Try entering a command instead.");
        }
        return false;
    }

    //mark a task and output
    private void markTask(String command) {
        int taskNumber = parser.parseTaskNumber(command, "mark");
        Task task = taskList.markAsDone(taskNumber);
        ui.showMessage("Nice! I've marked this task as done:");
        ui.showMessage(task.toString());
    }

    //unmark a task and output
    private void unmarkTask(String command) {
        int taskNumber = parser.parseTaskNumber(command, "unmark");
        Task task = taskList.markAsUndone(taskNumber);
        ui.showMessage("OK, I've marked this task as not done yet:");
        ui.showMessage(task.toString());
    }

    //delete a task and output
    private void deleteTask(String command) {
        int taskNumber = parser.parseTaskNumber(command, "delete");
        Task task = taskList.deleteTask(taskNumber);
        ui.showMessage("Noted. I've removed this task:");
        ui.showMessage(task.toString());
        ui.showMessage("Now you have " + taskList.size() + " tasks in the list.");
    }

}
