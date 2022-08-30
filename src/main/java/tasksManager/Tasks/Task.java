package tasksManager.Tasks;
import java.util.Objects;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {

    private Integer taskId;
    private TasksType taskType;
    private String taskName;
    private TasksStatus taskStatus;
    private String taskDescription;
    private Integer epicId;
    String startTime;
    Integer duration;
    LocalDateTime endTime;

    public Task (Integer taskId, TasksType taskType, String taskName, TasksStatus taskStatus,
                 String taskDescription, String startTime, Integer duration, Integer epicId) {
        this.taskId = taskId;
        this.taskType = taskType;
        this.taskName = taskName;
        this.taskStatus = taskStatus;
        this.taskDescription = taskDescription;
        this.startTime = startTime;
        this.duration = duration;
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
        if(epicId != null) {
            return epicId;
        } else {
            return null;
        }
    }

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public LocalDateTime getStartTime() {
        if (startTime != null) {
            return LocalDateTime.parse(this.startTime, formatter);
        } else {
            return null;
        }
    }

    // запись времени окончания в строку в файл
    public String getStartTimeToString() {
        String formatDateTime = null;
        if (getStartTime() != null) {
            formatDateTime = getStartTime().format(formatter);
        }
        return formatDateTime;
    }

    public LocalDateTime getEpicEndTime() {
        return this.endTime;
    }


    public void setStartTime(LocalDateTime time) {
        this.startTime = time.format(formatter);
    }

    public LocalDateTime getEndTime() {
        if (getStartTime() != null && duration != null) {
            return getStartTime().plusMinutes(this.duration);
        } else if (getStartTime() != null && duration == null) {
            return getStartTime();
        } else {
            return null;
        }
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Integer getDuration() {
        return this.duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(taskName, task.taskName) && Objects.equals(taskDescription, task.taskDescription)
                && Objects.equals(taskStatus, task.taskStatus) && taskId.equals(task.taskId)
                && Objects.equals(taskType, task.taskType) && Objects.equals(startTime, task.startTime)
                && Objects.equals(duration, task.duration) && Objects.equals(epicId, task.epicId);
    }

    @Override
    public String toString() {
        return  "\n" + "Task{" + "\n" +
                "id='" + taskId + '\'' + "\n" +
                "type='" + taskType + '\'' + "\n" +
                "name='" + taskName + '\'' + "\n" +
                "status='" + taskStatus + '\'' + "\n" +
                "description='" + taskDescription + '\'' + "\n" +
                "startTime='" + startTime + '\'' + "\n" +
                "duration='" + duration + '\'' + "\n" +
                "epicId='" + epicId + '\'' +
                '}';
    }

}
