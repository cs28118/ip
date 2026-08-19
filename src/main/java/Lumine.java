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
                System.out.println(" Bye. Hope to see you again soon!");
                System.out.println(line);
                break;
            } else if (command.equals("list")) {
                taskList.printTasks();
            } else {
                taskList.addTask(command);
            }

            System.out.println(line);

        }
    }
}
