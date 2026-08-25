public class Lumine {
    private Storage storage;
    private TaskList taskList;
    private Ui ui;
    private Parser parser;
    private String loadError;

    public Lumine(String filePath) {
        ui = new Ui();
        parser = new Parser();
        storage = new Storage(filePath);
        try {
            taskList = new TaskList(storage);
        } catch (LumineException e) {
            loadError = e.getMessage();
            taskList = new TaskList(storage, false);
        }
    }

    public void run() {
        ui.showGreetings();

        if (loadError != null) {
            ui.showSeparator();
            ui.showMessage(loadError);
            ui.showSeparator();
        }

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
            ui.showMessage(taskList.formatTasks());
        } else if (parser.isCommand(normalizedCommand, "date")) {
            ui.showMessage(taskList.formatTasksDueOn(parser.parseDateCommand(normalizedCommand)));
        } else if (parser.isCommand(normalizedCommand, "todo")) {
            addTask(parser.parseTodoCommand(normalizedCommand));
        } else if (parser.isCommand(normalizedCommand, "deadline")) {
            addTask(parser.parseDeadlineCommand(normalizedCommand));
        } else if (parser.isCommand(normalizedCommand, "event")) {
            addTask(parser.parseEventCommand(normalizedCommand));
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

    //add a task and output confirmation
    private void addTask(Task task) {
        taskList.addTask(task);
        ui.showMessage("Got it. I've added this task:\n  "
                + task + "\nNow, you have " + taskList.size() + " tasks in the list.");
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
