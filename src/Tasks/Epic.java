package Tasks;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {

    private ArrayList<Integer> listEpicSubtasks;

    public Epic(String taskName, String taskDescription, String taskStatus, Integer taskId) {
        super(taskName, taskDescription, taskStatus, taskId);
    }

    /*
    По твоему комментарию => У тебя не хватает методов: isEpic,addSubtaskId,getSubtaskIds,removeSubtask,equals

    1) Добавила метод isEpic, но не совсем поняла зачем, т.к. мы нигде его не используем.
    Первоначально у меня даже было отдельное поле в эпике isEpic, но удалила его в итоге из-за невостребованности

    2) getSubtaskIds и addSubtaskId реализованы в TaskManager в методе addSubtaskId,
    т.к. в ТЗ к заданию указано, что все методы должны быть вынесены в TaskManager.
    Добавила сюда список listEpicSubtasks и get и set методы для него.
    set() берет параметр из getEpicSubtasks() в TaskManager

    3) removeSubtask входил в метод deleteEpic в TaskManager, вынесла отдельно в removeEpicSubtasks()
    и оставила его так же в TaskManager

    4) Переопределила equals после добавления поля ArrayList<Integer> listEpicSubtasks

*/

    // Добавила метод isEpic
    public Boolean isEpic(Epic epic) {
        if (this.getClass() == epic.getClass()) {
            return true;
        } else {
            return false;
        }
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

    @Override
    public String toString() {
        return "\n" + "Epic{" + "\n" +
                "epicId='" + getTaskId() + '\'' + "\n" +
                "epicName='" + getTaskName() + '\'' + "\n" +
                "epicDescription='" + getTaskDescription() + '\'' + "\n" +
                "epicStatus='" + getTaskStatus() + '\'' +
                '}';
    }
}
