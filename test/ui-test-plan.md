# UI Test Plan

This plan defines console tests for the Lumine application. Commands are run from the repository root with Java 25.

### Test Case: Compile application with Java 25
Aim: Verify that all current Java sources compile successfully using the required Java 25 compiler.

Command:
```text
javac -d out\production\ip src\main\java\Deadline.java src\main\java\Event.java src\main\java\Lumine.java src\main\java\LumineException.java src\main\java\Task.java src\main\java\TaskList.java src\main\java\Todo.java && echo BUILD_OK
```

Input:
```text
<empty>
```

Expected output:
```text
BUILD_OK
```

### Test Case: Application greeting and exit
Aim: Verify that the application prints its greeting and exits with the expected farewell when the user enters `bye`.

Command:
```text
java -cp out\production\ip Lumine
```

Input:
```text
bye
```

Expected output:
```text
____________________________________________________________
 ___      __   __  __   __  ___   __    _  _______ 
|   |    |  | |  ||  |_|  ||   | |  |  | ||       |
|   |    |  | |  ||       ||   | |   |_| ||    ___|
|   |    |  |_|  ||       ||   | |       ||   |___ 
|   |___ |       ||       ||   | |  _    ||    ___|
|       ||       || ||_|| ||   | | | |   ||   |___ 
|_______||_______||_|   |_||___| |_|  |__||_______|
Hello, I'm Lumine!
What can I do for you today?
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

### Test Case: Handle empty todo and unknown command
Aim: Verify that an empty todo and an unrecognised command produce the required error messages and that the application continues running.

Command:
```text
java -cp out\production\ip Lumine
```

Input:
```text
todo
blah
bye
```

Expected output:
```text
____________________________________________________________
 ___      __   __  __   __  ___   __    _  _______ 
|   |    |  | |  ||  |_|  ||   | |  |  | ||       |
|   |    |  | |  ||       ||   | |   |_| ||    ___|
|   |    |  |_|  ||       ||   | |       ||   |___ 
|   |___ |       ||       ||   | |  _    ||    ___|
|       ||       || ||_|| ||   | | | |   ||   |___ 
|_______||_______||_|   |_||___| |_|  |__||_______|
Hello, I'm Lumine!
What can I do for you today?
____________________________________________________________
____________________________________________________________
Sorry, todo task cannot be empty. :C
____________________________________________________________
____________________________________________________________
Hmmmm, I can't understand what that means. ;-;
Try entering a command instead.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

### Test Case: Handle empty deadline and event
Aim: Verify that deadline and event commands without the required details produce helpful error messages and do not terminate the application.

Command:
```text
java -cp out\production\ip Lumine
```

Input:
```text
deadline
event
bye
```

Expected output:
```text
____________________________________________________________
 ___      __   __  __   __  ___   __    _  _______ 
|   |    |  | |  ||  |_|  ||   | |  |  | ||       |
|   |    |  | |  ||       ||   | |   |_| ||    ___|
|   |    |  |_|  ||       ||   | |       ||   |___ 
|   |___ |       ||       ||   | |  _    ||    ___|
|       ||       || ||_|| ||   | | | |   ||   |___ 
|_______||_______||_|   |_||___| |_|  |__||_______|
Hello, I'm Lumine!
What can I do for you today?
____________________________________________________________
____________________________________________________________
Sorry, deadline task must include a description and a /by time. :C
____________________________________________________________
____________________________________________________________
Sorry, event task must include a description, /from time, and /to time. :C
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

### Test Case: Handle invalid task number
Aim: Verify that marking a task outside the current list is handled through `LumineException` without crashing the application.

Command:
```text
java -cp out\production\ip Lumine
```

Input:
```text
mark 1
bye
```

Expected output:
```text
____________________________________________________________
 ___      __   __  __   __  ___   __    _  _______ 
|   |    |  | |  ||  |_|  ||   | |  |  | ||       |
|   |    |  | |  ||       ||   | |   |_| ||    ___|
|   |    |  |_|  ||       ||   | |       ||   |___ 
|   |___ |       ||       ||   | |  _    ||    ___|
|       ||       || ||_|| ||   | | | |   ||   |___ 
|_______||_______||_|   |_||___| |_|  |__||_______|
Hello, I'm Lumine!
What can I do for you today?
____________________________________________________________
____________________________________________________________
Task not found :<.
Please enter a valid task number.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

### Test Case: Create, list, mark, and unmark task types
Aim: Verify that todo, deadline, and event commands create the correct task subtypes, that listing preserves their order, and that marking and unmarking preserve each task's subtype information.

Command:
```text
java -cp out\production\ip Lumine
```

Input:
```text
todo borrow book
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
list
mark 2
unmark 2
list
bye
```

Expected output:
```text
____________________________________________________________
 ___      __   __  __   __  ___   __    _  _______ 
|   |    |  | |  ||  |_|  ||   | |  |  | ||       |
|   |    |  | |  ||       ||   | |   |_| ||    ___|
|   |    |  |_|  ||       ||   | |       ||   |___ 
|   |___ |       ||       ||   | |  _    ||    ___|
|       ||       || ||_|| ||   | | | |   ||   |___ 
|_______||_______||_|   |_||___| |_|  |__||_______|
Hello, I'm Lumine!
What can I do for you today?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] borrow book
Now, you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [D][ ] return book (by: Sunday)
Now, you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [E][ ] project meeting (from: Mon 2pm to: 4pm)
Now, you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] borrow book
2.[D][ ] return book (by: Sunday)
3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
[D][X] return book (by: Sunday)
____________________________________________________________
____________________________________________________________
OK, I've marked this task as not done yet:
[D][ ] return book (by: Sunday)
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] borrow book
2.[D][ ] return book (by: Sunday)
3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```
