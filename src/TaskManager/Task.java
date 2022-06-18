package TaskManager;
import java.util.Objects;

public class Task {

    private String taskName;
    private String taskDescription;
    private String taskStatus;

    public Task (String taskName, String taskDescription, String taskStatus) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = taskStatus;
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

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(taskName, task.taskName) && Objects.equals(taskDescription, task.taskDescription) && Objects.equals(taskStatus, task.taskStatus);
    }

    @Override
    public int hashCode() {

        return Objects.hash(taskName, taskDescription, taskStatus);
    }

    @Override
    public String toString() {
        return  "\n" + "Task{" + "\n" +
                "taskName='" + taskName + '\'' + "\n" +
                "taskDescription='" + taskDescription + '\'' + "\n" +
                "taskStatus='" + taskStatus + '\'' +
                '}';
    }

}
