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
        String horizontalLine = "____________________________________________________________\n";
        String greeting = horizontalLine + banner
                + "Hello, I'm Lumine!\n"
                + "What can I do for you?\n"
                + horizontalLine;
        String exitMessage = "Bye, hope to see you again soon!\n" + horizontalLine;

        System.out.println(greeting + exitMessage);
    }
}
