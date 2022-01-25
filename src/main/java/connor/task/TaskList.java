package connor.task;

import connor.Connor;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class TaskList {
    private static final String INDENT = Connor.INDENT;
    private static final String LINE = Connor.LINE;
    private static final String EMPTY_TASK_LIST = "Your task list is empty.";
    private static final String SHOW_TASKS = "Here are your current tasks: ";
    private static final String ADD_NEW_TASK = "Alright, I've added a new task: ";
    private static final String DELETE_TASK = "Alright, I've deleted the task: ";
    private static final String CLEAR_TASKS_CONFIRM = "Are you REALLY sure you want to clear all your tasks?\n"
            + "Type 'yes' to confirm, type anything else (or nothing) to cancel.";
    private static final String CLEAR_TASKS_CONFIRMED = "Poof! All your tasks are cleared!";
    private static final String CLEAR_TASKS_CANCEL = "Cancelled clearing all tasks. Phew!";
    private static final String MARK_TASK = "Good job! I've marked the following task as completed: ";
    private static final String UNMARK_TASK = "Understood. I've unmarked the following task: ";
    private static final String ERROR_EMPTY_INDEX = "Error! Index cannot be empty.";
    private static final String ERROR_EMPTY_TASK_DESC = "Error! Tasks cannot have an empty description.";
    private static final String ERROR_EMPTY_DL_DESC = "Error! Deadlines cannot have empty descriptions or dates.";
    private static final String ERROR_EMPTY_EVENT_DESC = "Error! Events cannot have empty descriptions or dates.";
    private static final String ERROR_INDEX_NOT_INTEGER = "Error! Index must be a valid integer.";
    private static final String ERROR_INDEX_OUT_OF_RANGE = "Error! Given index is out of range.";
    private static final String ERROR_INVALID_DL_FORMAT = "Error! Wrong format for deadlines.\n\n"
            + "Deadline tasks must include \"by\" in the description.\nExample: \n"
            + ">>> deadline Finish project /by Monday Morning";
    private static final String ERROR_INVALID_EVENT_FORMAT = "Error! Wrong format for events.\n\n"
            + "Event tasks must include \"at\" in the description.\nExample: \n"
            + ">>> event Birthday Party at May 5th";
    private static final String ERROR_INVALID_TASK_TYPE = "Oh no! Incorrect Task type!";
    private static final String ERROR_MARK_EMPTY = "Error! I can't mark an empty task list!";
    private static final String ERROR_UNMARK_EMPTY = "Error! I can't unmark an empty task list!";
    
    private static ArrayList<Task> tasks;

    public static void setTasks(ArrayList<Task> t) {
        tasks = t;
    }

    public static ArrayList<Task> getTasks() {
        return tasks;
    }

    public static void viewTasks() {
        if (tasks.size() == 0) {
            print(EMPTY_TASK_LIST);
            return;
        }
        print(SHOW_TASKS);
        for (int i = 1; i <= tasks.size(); i++) {
            print(INDENT + i + ". " + tasks.get(i - 1));
        }
    }

    public static void addTask(TaskType taskType, String desc) {
        if (desc.isEmpty()) {
            print(ERROR_EMPTY_TASK_DESC);
            return;
        }
        switch (taskType) {
        case TODO:
            ToDo todo = new ToDo(desc);
            tasks.add(todo);
            print(ADD_NEW_TASK);
            print(INDENT + todo);
            break;
        case DEADLINE: {
            if (!desc.contains("/by")) {
                print(ERROR_INVALID_DL_FORMAT);
                return;
            }
            String[] phrase = desc.split("/by", 2);
            String thing = phrase[0].trim();
            String when  = phrase[1].trim();
            if (thing.isBlank() || when.isBlank()) {
                print(ERROR_EMPTY_DL_DESC);
                return;
            }
            try {
                // Check if 'when' is a valid date
                LocalDate ld = LocalDate.parse(when);
                Deadline deadline = new Deadline(thing, ld);
                tasks.add(deadline);
                print(ADD_NEW_TASK);
                print(INDENT + deadline);
            } catch (DateTimeParseException e) {
                // Otherwise, treat it as a normal String
                Deadline deadline = new Deadline(thing, when);
                tasks.add(deadline);
                print(ADD_NEW_TASK);
                print(INDENT + deadline);
            }
            break;
        }
        case EVENT: {
            if (!desc.contains("/at")) {
                print(ERROR_INVALID_EVENT_FORMAT);
                return;
            }
            String[] phrase = desc.split("/at", 2);
            String thing = phrase[0].trim();
            String when  = phrase[1].trim();
            if (thing.isBlank() || when.isBlank()) {
                print(ERROR_EMPTY_EVENT_DESC);
                return;
            }
            try {
                // Check if 'when' is a valid date
                LocalDate ld = LocalDate.parse(when);
                Event event = new Event(thing, ld);
                tasks.add(event);
                print(ADD_NEW_TASK);
                print(INDENT + event);
            } catch (DateTimeParseException e) {
                // Otherwise, treat it as a normal String
                Event event = new Event(thing, when);
                tasks.add(event);
                print(ADD_NEW_TASK);
                print(INDENT + event);
            }
            break;
        }
        default:
            // Something has gone wrong
            print(ERROR_INVALID_TASK_TYPE);
            return;
        }
        // After task is added show current no. of tasks
        print("");
        getNoOfTasks();
    }

    public static void deleteTask(int index) {
        try {
            Task t = tasks.get(index);
            tasks.remove(index);
            print(DELETE_TASK);
            print(INDENT + t);
        } catch (IndexOutOfBoundsException e) {
            print(ERROR_INDEX_OUT_OF_RANGE);
        }
    }

    public static void clearTasks() {
        tasks.clear();
        print(CLEAR_TASKS_CONFIRMED);
    }

    public static void markStatus(TaskStatus ts, int index) {
        switch (ts) {
        case MARK: {
            try {
                Task t = tasks.get(index);
                t.mark();
                print(MARK_TASK);
                print(INDENT + t);
            } catch (IndexOutOfBoundsException e) {
                if (tasks.isEmpty()) {
                    print(ERROR_MARK_EMPTY);
                    return;
                }
                print(ERROR_INDEX_OUT_OF_RANGE);
                getNoOfTasks();
            }
            break;
        }
        case UNMARK: {
            try {
                Task t = tasks.get(index);
                t.unmark();
                print(UNMARK_TASK);
                print(INDENT + t);
            } catch (IndexOutOfBoundsException e) {
                if (tasks.isEmpty()) {
                    print(ERROR_UNMARK_EMPTY);
                    return;
                }
                print(ERROR_INDEX_OUT_OF_RANGE);
                getNoOfTasks();
            }
            break;
        }
        }
    }

    private static void getNoOfTasks() {
        String plurality = tasks.size() == 1 ? "" : "s";
        print("You have " + tasks.size() + " task" + plurality + ".");
    }

    private static void print(String s) {
        System.out.println(s);
    }
}
