import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    Integer taskId = 1;
    HashMap<Integer, Task> taskList = new HashMap<>();
    HashMap<Integer, Epic> epicList = new HashMap<>();
    HashMap<Integer, Subtask> subtaskList = new HashMap<>();

    public HashMap<Integer, Task> getTaskList() {
        return taskList;
    }

    public HashMap<Integer, Epic> getEpicList() {
        return epicList;
    }

    public HashMap<Integer, Subtask> getSubtaskList() {
        return subtaskList;
    }

    // Рассчитываем порядковый номер
    public Integer setTaskId(){
        return taskId++;
    }

    // Создаем и сохраняем новые объекты
    public void createNewTask(Task task) {
        taskList.put(task.getTaskId(), task);
    }

    public void createNewEpic(Epic epic) {
        //taskId++;
        epicList.put(epic.getTaskId(), epic);
    }

    public void createNewSubtask(Subtask subtask) {
        //taskId++;
        subtaskList.put(subtask.getTaskId(), subtask);
    }

    // Получаем списки объектов
    public ArrayList<String> getListAllTask() {
        ArrayList<String> listAllTask = new ArrayList<>();
        for (Integer id : taskList.keySet()) {
            Task task = taskList.get(id);
            listAllTask.add(task.getTaskName());
        }
        return listAllTask;
    }

    public ArrayList<String> getListAllEpic() {
        ArrayList<String> listAllEpic = new ArrayList<>();
        for (Integer id : epicList.keySet()) {
            Epic epic = epicList.get(id);
            listAllEpic.add(epic.getTaskName());
        }
        return listAllEpic;
    }

    public ArrayList<String> getListAllSubtask() {
        ArrayList<String> listAllSubtask = new ArrayList<>();
        for (Integer id : subtaskList.keySet()) {
            Subtask subtask = subtaskList.get(id);
            listAllSubtask.add(subtask.getTaskName());
        }
        return listAllSubtask;
    }

    // Поправила получение объектов по id
    public Task getTask(int id) {
        return taskList.get(id);
    }

    public Epic getEpic(int id) {
        return epicList.get(id);
    }

    public Subtask getSubtask(int id) {
        return subtaskList.get(id);
    }

    // Обновляем объекты
    public void updateTask(Task task) {
        taskList.put(task.getTaskId(), task);
    }

    public void updateEpic(Epic epic) {
        epicList.put(epic.getTaskId(), epic);
    }

    public void updateSubtask(Subtask subtask) {
        subtaskList.put(subtask.getTaskId(), subtask);
    }

    // Удаляем объекты по id
    public void removeTask(int id) {
        taskList.remove(id);
    }

    public void removeEpic(int id) {
        removeEpicSubtasks(id); // Сначала удаляет свои подзадачи, потом себя
        epicList.remove(id);
    }

    public void removeEpicSubtasks(int id) {
        for (Integer taskId : addSubtaskId(id)) { // Сначала удаляет свои подзадачи, потом себя
            removeSubtask(taskId);
        }
    }

    public void removeSubtask(int id) {
        subtaskList.remove(id);
    }

    // Удаляем все объекты списка
    public void removeAllTask() {
        taskList.clear();
    }

    public void removeAllEpic() {
        for (Integer id : epicList.keySet()) { // Сначала удаляет свои подзадачи, потом себя
            for (Integer taskId : addSubtaskId(id)) {
                removeEpicSubtasks(taskId);
            }
        }
        epicList.clear();
    }

    public void removeAllSubtask() {
        subtaskList.clear();
    }

    // Получаем список подзадач эпика
    public ArrayList<Integer> addSubtaskId(int id) {
        ArrayList<Integer> listSubtask = new ArrayList<>();
        for (Integer taskId : subtaskList.keySet()) {
            if (subtaskList.get(taskId).getEpicId() == id) {
                listSubtask.add(taskId);
            }
        }
        return listSubtask;

    }


    // Проверяем возвращение объекта на null
    public boolean checkEpic(int id) {
        if (epicList.get(id) != null) {
            return true;
        } else {
            return false;
        }
    }

    // Возвращаем список подзадач эпика, если объект не null
    public ArrayList<Integer> getListEpicSubtasks(int id) {
        if (checkEpic(id)) {
            return epicList.get(id).getListEpicSubtasks();
        } else {
            return null;
        }
    }



    // Рассчитываем статус эпика по статусам сабтасков
    public void setEpicStatus(int id) {
        int checkStatus = 0;
        for (Integer taskId : addSubtaskId(id)) {
            if (subtaskList.get(taskId).getTaskStatus().equals("DONE")) {
                checkStatus = checkStatus + 2;
            } else if (subtaskList.get(taskId).getTaskStatus().equals("IN_PROGRESS")) {
                checkStatus = checkStatus + 1;
            }
        }
        if (checkStatus != 0 && checkStatus == addSubtaskId(id).size() * 2) {
            epicList.get(id).setTaskStatus("DONE");
        } else if (checkStatus > 0 && checkStatus < addSubtaskId(id).size() * 2) {
            epicList.get(id).setTaskStatus("IN_PROGRESS");
        } else if (checkStatus == 0){
            epicList.get(id).setTaskStatus("NEW");
        }
    }

}