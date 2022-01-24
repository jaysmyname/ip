public class Parser {
    private static final String ERROR_INDEX_NOT_INTEGER = "Error! Index must be a valid integer.";
    private static final String ERROR_INVALID_COMMAND_START = "My apologies, I don't understand what '";
    private static final String ERROR_INVALID_COMMAND_END = "' means.";

    CommandType ct;
    Command command;
    boolean canActivate = true;

    public Parser(String s) {
        String[] statement = s.trim().concat(" ").split(" ", 2);
        String x = statement[0].toLowerCase();
        String desc = statement[1].trim();
        switch (x) {
        case "exit":
        case "bye": {
            this.ct = CommandType.BYE;
            this.command = new ByeCommand();
            break;
        }
        case "list": {
            this.ct = CommandType.LIST;
            this.command = new ListCommand();
            break;
        }
        case "todo": {
            this.ct = CommandType.ADD;
            this.command = new AddCommand(TaskType.TODO, desc);
            break;
        }
        case "deadline": {
            this.ct = CommandType.ADD;
            this.command = new AddCommand(TaskType.DEADLINE, desc);
            break;
        }
        case "event": {
            this.ct = CommandType.ADD;
            this.command = new AddCommand(TaskType.EVENT, desc);
            break;
        }
        case "delete": {
            this.ct = CommandType.DELETE;
            try {
                int taskNo = Integer.parseInt(desc) - 1;
                this.command = new DeleteCommand(taskNo);
            } catch (NumberFormatException e) {
                System.out.println(ERROR_INDEX_NOT_INTEGER);
                canActivate = false;
            }
            break;
        }
        case "clear": {
            this.ct = CommandType.CLEAR;
            this.command = new ClearCommand();
            break;
        }
        case "mark": {
            this.ct = CommandType.CHANGE_STATUS;
            try {
                int taskNo = Integer.parseInt(desc) - 1;
                this.command = new ChangeStatusCommand(MarkStatus.MARK, taskNo);
            } catch (NumberFormatException e) {
                System.out.println(ERROR_INDEX_NOT_INTEGER);
                canActivate = false;
            }
            break;
        }
        case "unmark": {
            this.ct = CommandType.CHANGE_STATUS;
            try {
                int taskNo = Integer.parseInt(desc) - 1;
                this.command = new ChangeStatusCommand(MarkStatus.UNMARK, taskNo);
            } catch (NumberFormatException e) {
                System.out.println(ERROR_INDEX_NOT_INTEGER);
                canActivate = false;
            }
            break;
        }
        default: {
            this.ct = CommandType.UNKNOWN;
            System.out.println(ERROR_INVALID_COMMAND_START + statement[0] + ERROR_INVALID_COMMAND_END);
            canActivate = false;
        }
        }
    }

    public void parse() {
        if (!canActivate) {
            return;
        }
        command.activate();
    }

}
