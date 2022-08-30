package tasksManager.Tasks;

import java.time.LocalDateTime;

public class Subtask extends Epic {

    public Subtask(Integer taskId, TasksType taskType, String taskName, TasksStatus taskStatus,
                   String taskDescription, String startTime, Integer duration, Integer epicId) {
        super(taskId, taskType, taskName, taskStatus, taskDescription, startTime, duration, epicId);
    }

}
