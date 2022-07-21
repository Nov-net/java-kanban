package Managers;

import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;
import Tasks.TasksStatus;
import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    Integer taskId = 1;
    private final HashMap<Integer, Task> taskList = new HashMap<>();
    private final HashMap<Integer, Epic> epicList = new HashMap<>();
    private final HashMap<Integer, Subtask> subtaskList = new HashMap<>();

    // Получаем объекты по id
    @Override
    public Task getTask(int id) {
        Managers.getDefaultHistory().addHistory(taskList.get(id));
        return taskList.get(id);
    }

    @Override
    public Epic getEpic(int id) {
        Managers.getDefaultHistory().addHistory(epicList.get(id));
        return epicList.get(id);
    }

    @Override
    public Subtask getSubtask(int id) {
        Managers.getDefaultHistory().addHistory(subtaskList.get(id));
        return subtaskList.get(id);
    }

    // Сделала возврат не самого списка, а его копии
    @Override
    public HashMap<Integer, Task> getTaskList() {
        HashMap<Integer, Task> cloneTaskList = (HashMap) taskList.clone();
        return cloneTaskList;
    }
    @Override
    public HashMap<Integer, Epic> getEpicList() {
        HashMap<Integer, Epic> cloneEpicList = (HashMap) epicList.clone();
        return cloneEpicList;
    }
    @Override
    public HashMap<Integer, Subtask> getSubtaskList() {
        HashMap<Integer, Subtask> cloneSubtaskList = (HashMap) subtaskList.clone();
        return cloneSubtaskList;
    }

    // Рассчитываем порядковый номер
    @Override
    public Integer setTaskId(){
        return taskId++;
    }

    // Создаем и сохраняем новые объекты
    @Override
    public void createNewTask(Task task) {
        taskList.put(task.getTaskId(), task);
    }

    @Override
    public void createNewEpic(Epic epic) {
        epicList.put(epic.getTaskId(), epic);
    }

    @Override
    public void createNewSubtask(Subtask subtask) {
        subtaskList.put(subtask.getTaskId(), subtask);
    }

    // Получаем имена всех объектов
    @Override
    public ArrayList<String> getListAllTask() {
        ArrayList<String> listAllTask = new ArrayList<>();
        for (Integer id : taskList.keySet()) {
            Task task = taskList.get(id);
            listAllTask.add(task.getTaskName());
        }
        return listAllTask;
    }

    @Override
    public ArrayList<String> getListAllEpic() {
        ArrayList<String> listAllEpic = new ArrayList<>();
        for (Integer id : epicList.keySet()) {
            Epic epic = epicList.get(id);
            listAllEpic.add(epic.getTaskName());
        }
        return listAllEpic;
    }

    @Override
    public ArrayList<String> getListAllSubtask() {
        ArrayList<String> listAllSubtask = new ArrayList<>();
        for (Integer id : subtaskList.keySet()) {
            Subtask subtask = subtaskList.get(id);
            listAllSubtask.add(subtask.getTaskName());
        }
        return listAllSubtask;
    }

    // Обновляем объекты
    @Override
    public void updateTask(Task task) {
        taskList.put(task.getTaskId(), task);
    }
    @Override
    public void updateEpic(Epic epic) {
        epicList.put(epic.getTaskId(), epic);
    }
    @Override
    public void updateSubtask(Subtask subtask) {
        subtaskList.put(subtask.getTaskId(), subtask);
    }

    // Удаляем объекты по id
    @Override
    public void removeTask(int id) {
        Managers.getDefaultHistory().removeHistory(id); //Удаляем объект из истории просмотров
        taskList.remove(id);
    }
    @Override
    public void removeEpic(int id) {
        Managers.getDefaultHistory().removeHistory(id); //Удаляем объект из истории просмотров
        removeEpicSubtasks(id); // Сначала удаляет свои подзадачи, потом себя
        epicList.remove(id);
    }
    @Override
    public void removeEpicSubtasks(int id) {
        for (Integer taskId : addSubtaskId(id)) { // Сначала удаляет свои подзадачи, потом себя
            removeSubtask(taskId);
        }
    }
    @Override
    public void removeSubtask(int id) {
        Managers.getDefaultHistory().removeHistory(id); //Удаляем объект из истории просмотров
        subtaskList.remove(id);
    }

    // Удаляем все объекты списка
    @Override
    public void removeAllTask() {
        taskList.clear();
    }
    @Override
    public void removeAllEpic() {
        for (Integer id : epicList.keySet()) { // Сначала удаляет свои подзадачи, потом себя
            for (Integer taskId : addSubtaskId(id)) {
                removeEpicSubtasks(taskId);
            }
        }
        epicList.clear();
    }
    @Override
    public void removeAllSubtask() {
        subtaskList.clear();
    }

    // Получаем список подзадач эпика
    @Override
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
    @Override
    public boolean checkEpic(int id) {
        return epicList.get(id) != null;
    }

    // Возвращаем список подзадач эпика, если объект не null
    @Override
    public ArrayList<Integer> getListEpicSubtasks(int id) {
        if (checkEpic(id)) {
            return epicList.get(id).getListEpicSubtasks();
        } else {
            return null;
        }
    }

    // Рассчитываем статус эпика по статусам сабтасков
    @Override
    public void setEpicStatus(int id) {
        int checkStatus = 0;
        for (Integer taskId : addSubtaskId(id)) {
            if (subtaskList.get(taskId).getTaskStatus().equals(TasksStatus.DONE)) {
                checkStatus = checkStatus + 2;
            } else if (subtaskList.get(taskId).getTaskStatus().equals(TasksStatus.IN_PROGRESS)) {
                checkStatus = checkStatus + 1;
            }
        }
        if (checkStatus != 0 && checkStatus == addSubtaskId(id).size() * 2) {
            epicList.get(id).setTaskStatus(TasksStatus.DONE);
        } else if (checkStatus > 0 && checkStatus < addSubtaskId(id).size() * 2) {
            epicList.get(id).setTaskStatus(TasksStatus.IN_PROGRESS);
        } else if (checkStatus == 0){
            epicList.get(id).setTaskStatus(TasksStatus.NEW);
        }
    }

}