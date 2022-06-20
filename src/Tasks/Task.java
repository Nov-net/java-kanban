package Tasks;
import java.util.Objects;

public class Task {

    private String taskName;
    private String taskDescription;
    private String taskStatus;
    private Integer taskId;

    public Task (String taskName, String taskDescription, String taskStatus, Integer taskId) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = taskStatus;
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(taskName, task.taskName) && Objects.equals(taskDescription, task.taskDescription)
                && Objects.equals(taskStatus, task.taskStatus) && taskId.equals(task.taskId);
    }

    @Override
    public String toString() {
        return  "\n" + "Task{" + "\n" +
                "taskId='" + taskId + '\'' + "\n" +
                "taskName='" + taskName + '\'' + "\n" +
                "taskDescription='" + taskDescription + '\'' + "\n" +
                "taskStatus='" + taskStatus + '\'' +
                '}';
    }

}
