package Tasks;
import java.util.Objects;

public class Task {

    private Integer taskId;
    private TasksType taskType;
    private String taskName;
    private TasksStatus taskStatus;
    private String taskDescription;
    private Integer epicId;

    public Task (Integer taskId, TasksType taskType, String taskName, TasksStatus taskStatus,
                 String taskDescription, Integer epicId) {
        this.taskId = taskId;
        this.taskType = taskType;
        this.taskName = taskName;
        this.taskStatus = taskStatus;
        this.taskDescription = taskDescription;
        this.epicId = epicId;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public Integer getTaskId() {
        return taskId;
    }
    public void setTaskId(int id) {
        this.taskId = id;
    }

    public TasksStatus getTaskStatus() {
        return taskStatus;
    }
    public void setTaskStatus(TasksStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public TasksType getTaskType() { return taskType; }
    public void setTaskType(TasksType taskType) { this.taskType = taskType; }

    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(taskName, task.taskName) && Objects.equals(taskDescription, task.taskDescription)
                && Objects.equals(taskStatus, task.taskStatus) && taskId.equals(task.taskId)
                && Objects.equals(taskType, task.taskType) && Objects.equals(epicId, task.epicId);
    }

    @Override
    public String toString() {
        return  "\n" + "Task{" + "\n" +
                "id='" + taskId + '\'' + "\n" +
                "type='" + taskType + '\'' + "\n" +
                "name='" + taskName + '\'' + "\n" +
                "status='" + taskStatus + '\'' + "\n" +
                "description='" + taskDescription + '\'' + "\n" +
                "epicId='" + epicId + '\'' +
                '}';
    }

}
