package Tasks;

public class Subtask extends Epic {

    public Subtask(Integer taskId, TasksType taskType, String taskName, TasksStatus taskStatus,
                   String taskDescription, Integer epicId) {
        super(taskId, taskType, taskName, taskStatus, taskDescription, epicId);
    }

}
