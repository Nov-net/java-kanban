package TaskManager;

public class Epic extends Task {

    public Epic(String taskName, String taskDescription, String taskStatus) {
        super(taskName, taskDescription, taskStatus);
    }

    @Override
    public String toString() {
        return "\n" + "Epic{" + "\n" +
                "epicName='" + getTaskName() + '\'' + "\n" +
                "epicDescription='" + getTaskDescription() + '\'' + "\n" +
                "epicStatus='" + getTaskStatus() + '\'' +
                '}';
    }
}
