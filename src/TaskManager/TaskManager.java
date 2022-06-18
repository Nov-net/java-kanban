package TaskManager;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    Integer taskId = 0;
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

    // Создаем и сохраняем новые объекты
    public void createNewTask(Task task) {
        taskId++; // определяем следующий порядковый номер
        taskList.put(taskId, task);
    }

    public void createNewEpic(Epic epic) {
        taskId++;
        epicList.put(taskId, epic);
    }

    public void createNewSubtask(Subtask subtask) {
        taskId++;
        subtaskList.put(taskId, subtask);
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

    // Получаем объекты по id
    public Task getTask(int id) {
        Task task = null;

        for (Integer taskId : taskList.keySet()) {
            if (taskId == id) {
                task = taskList.get(id);
            }
        }
        return task;
    }

    public Epic getEpic(int id) {
        Epic epic = null;

        for (Integer taskId : epicList.keySet()) {
            if (taskId == id) {
                epic = epicList.get(id);
            }
        }
        return epic;
    }

    public Subtask getSubtask(int id) {
        Subtask subtask = null;

        for (Integer taskId : subtaskList.keySet()) {
            if (taskId == id) {
                subtask = subtaskList.get(id);
            }
        }
        return subtask;
    }

    // Обновляем объекты
    public void updateTask(int id, Task task) {
        taskList.put(id, task);
    }

    public void updateEpic(int id, Epic epic) {
        epicList.put(id, epic);
    }

    public void updateSubtask(int id, Subtask subtask) {
        subtaskList.put(id, subtask);
    }

    // Удаляем объекты по id
    public void deleteTask(int id) {
        taskList.remove(id);
    }

    public void deleteEpic(int id) {
        for (Integer taskId : getEpicSubtasks(id)) { // Сначала удаляет свои подзадачи, потом себя
            subtaskList.remove(taskId);
        }
        epicList.remove(id);
    }

    public void deleteSubtask(int id) {
        subtaskList.remove(id);
    }

    // Удаляем все объекты списка
    public void deleteAllTask() {
        taskList.clear();
    }

    public void deleteAllEpic() {
        for (Integer id : epicList.keySet()) { // Сначала удаляет свои подзадачи, потом себя
            for (Integer taskId : getEpicSubtasks(id)) {
                subtaskList.remove(taskId);
            }
        }
        epicList.clear();
    }

    public void deleteAllSubtask() {
        subtaskList.clear();
    }

    // Получаем список подзадач эпика
    public ArrayList<Integer> getEpicSubtasks(int id) {
        ArrayList<Integer> listSubtask = new ArrayList<>();
        for (Integer taskId : subtaskList.keySet()) {
            if (subtaskList.get(taskId).getEpicId() == id) {
                listSubtask.add(taskId);
            }
        }
        return listSubtask;
    }

    // Рассчитываем статус эпика по статусам сабтасков
    public void setEpicStatus(int id) {
        int checkStatus = 0;
        for (Integer taskId : getEpicSubtasks(id)) {
            if (subtaskList.get(taskId).getTaskStatus().equals("DONE")) {
                checkStatus = checkStatus + 2;
            } else if (subtaskList.get(taskId).getTaskStatus().equals("IN_PROGRESS")) {
                checkStatus = checkStatus + 1;
            }
        }
        if (checkStatus != 0 && checkStatus == getEpicSubtasks(id).size() * 2) {
            epicList.get(id).setTaskStatus("DONE");
        } else if (checkStatus > 0 && checkStatus < getEpicSubtasks(id).size() * 2) {
            epicList.get(id).setTaskStatus("IN_PROGRESS");
        } else if (checkStatus == 0){
            epicList.get(id).setTaskStatus("NEW");
        }
    }

}