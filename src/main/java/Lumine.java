import java.util.Scanner;

public class Lumine {
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

        //getting inputs
        Scanner scanner = new Scanner(System.in);
        TaskList taskList = new TaskList();
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            System.out.println(line);

            if (command.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println(line);
                break;
            } else if (command.equals("list")) {
                taskList.printTasks();
            } else if (isMarkCommand(command)) {
                String normalizedCommand = command.trim();
                String taskNumberStr = normalizedCommand.substring("mark".length()).trim();
                try {
                    int taskNumber = Integer.parseInt(taskNumberStr);
                    Task task = taskList.markAsDone(taskNumber);
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println(task);
                } catch (IllegalArgumentException e) {
                    System.out.println("Please provide a valid task number.");
                }
            } else if (isUnmarkCommand(command)) {
                String normalizedCommand = command.trim();
                String taskNumberStr = normalizedCommand.substring("unmark".length()).trim();
                try {
                    int taskNumber = Integer.parseInt(taskNumberStr);
                    Task task = taskList.markAsUndone(taskNumber);
                    System.out.println("OK, I've marked this task as not done yet:");
                    System.out.println(task);
                } catch (IllegalArgumentException e) {
                    System.out.println("Please provide a valid task number.");
                }
            } else {
                taskList.addTask(command);
            }

            System.out.println(line);
        }
    }

    private static boolean isMarkCommand(String command) {
        return command.trim().matches("mark\\s+([1-9]\\d?|100)");
    }

    private static boolean isUnmarkCommand(String command) {
        return command.trim().matches("unmark\\s+([1-9]\\d?|100)");
    }
}
