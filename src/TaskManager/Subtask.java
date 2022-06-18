package TaskManager;
import java.util.Objects;

public class Subtask extends Epic {

    private Integer epicId;

    public Subtask(String taskName, String taskDescription, String taskStatus, Integer epicId) {
        super(taskName, taskDescription, taskStatus);
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return epicId.equals(subtask.epicId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }

    @Override
    public String toString() {
        return "\n" + "Subtask{" + "\n" +
                "subtaskName='" + getTaskName() + '\'' + "\n" +
                "subtaskDescription='" + getTaskDescription() + '\'' + "\n" +
                "subtaskStatus='" + getTaskStatus() + '\'' +
                "epicId=" + epicId +
                '}';
    }
}
