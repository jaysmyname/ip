package connor.command;

import connor.task.TaskType;
import connor.task.TaskList;

public class AddCommand extends Command {
    TaskType tt;
    String desc;

    public AddCommand(TaskType tt, String desc) {
        this.tt = tt;
        this.desc = desc;
    }

    @Override
    public void activate() {
        TaskList.addTask(tt, desc);
    }


}