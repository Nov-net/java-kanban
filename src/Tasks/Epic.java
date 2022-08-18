package Tasks;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {

    private ArrayList<Integer> listEpicSubtasks;

    public Epic(Integer taskId, TasksType taskType, String taskName, TasksStatus taskStatus,
                String taskDescription, Integer epicId) {
        super(taskId, taskType, taskName, taskStatus, taskDescription, epicId);
    }

    public Boolean isEpic(Epic epic) {
        return this.getClass() == epic.getClass();
    }

    public ArrayList<Integer> getListEpicSubtasks() {
        return listEpicSubtasks;
    }

    public void setListEpicSubtasks(ArrayList<Integer> listEpicSubtasks) {
        if (listEpicSubtasks != null) {
            this.listEpicSubtasks = listEpicSubtasks;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(listEpicSubtasks, epic.listEpicSubtasks);
    }

}
