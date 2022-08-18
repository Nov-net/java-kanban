package Managers;

import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;
import Tasks.TasksStatus;
import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTasksManager implements TaskManager {
    Integer taskId = 1;
    private final HashMap<Integer, Task> taskList = new HashMap<>();
    private final HashMap<Integer, Epic> epicList = new HashMap<>();
    private final HashMap<Integer, Subtask> subtaskList = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    // Получаем объекты по id
    @Override
    public Task getTask(int id) {
        final Task task = taskList.get(id);
        historyManager.addHistory(task);
        return task;
    }

    @Override
    public Epic getEpic(int id) {
        final Epic epic = epicList.get(id);
        historyManager.addHistory(epic);
        return epic;
    }

    @Override
    public Subtask getSubtask(int id) {
        final Subtask subtask = subtaskList.get(id);
        historyManager.addHistory(subtask);
        return subtask;
    }

    // Печатаем историю просмотров
    public void printHistory() {
        ArrayList<String> listAllTask = new ArrayList<>();
        for (Task task : historyManager.getHistory()) {
            listAllTask.add(task.getTaskName());
        }
        System.out.println("История просмотров: " + listAllTask + "\n");
    }

    // Возвращаем копию списка объектов
    @Override
    public HashMap<Integer, Task> getTaskList() {
        return (HashMap<Integer, Task>) taskList.clone();
    }

    @Override
    public HashMap<Integer, Epic> getEpicList() {
        return (HashMap<Integer, Epic>) epicList.clone();
    }

    @Override
    public HashMap<Integer, Subtask> getSubtaskList() {
        return (HashMap<Integer, Subtask>) subtaskList.clone();
    }

    // Создаем и сохраняем новые объекты
    @Override
    public int addTask(Task task) {
        final int id = taskId++;
        task.setTaskId(id);
        taskList.put(id, task);
        return id;
    }

    @Override
    public int addEpic(Epic epic) {
        final int id = taskId++;
        epic.setTaskId(id);
        epicList.put(id, epic);
        return id;
    }

    @Override
    public int addSubtask(Subtask subtask) {
        final int id = taskId++;
        subtask.setTaskId(id);
        subtaskList.put(id, subtask);
        return id;
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
        historyManager.removeHistory(id);
        taskList.remove(id);
    }

    @Override
    public void removeEpic(int id) {
        final Epic epic = epicList.remove(id);
        if (epic == null) {
            return;
        }
        historyManager.removeHistory(id);
        removeEpicSubtasks(id);
    }

    @Override
    public void removeEpicSubtasks(int id) {
        for (Integer taskId : getSubtaskId(id)) { // Сначала удаляет свои подзадачи, потом себя
            removeSubtask(taskId);
        }
    }

    @Override
    public void removeSubtask(int id) {
        historyManager.removeHistory(id); //Удаляем объект из истории просмотров
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
            for (Integer taskId : getSubtaskId(id)) {
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
    public ArrayList<Integer> getSubtaskId(int id) {
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
    public void updateEpicStatus(int id) {
        int checkStatus = 0;
        for (Integer taskId : getSubtaskId(id)) {
            if (subtaskList.get(taskId).getTaskStatus().equals(TasksStatus.DONE)) {
                checkStatus = checkStatus + 2;
            } else if (subtaskList.get(taskId).getTaskStatus().equals(TasksStatus.IN_PROGRESS)) {
                checkStatus = checkStatus + 1;
            }
        }
        if (checkStatus != 0 && checkStatus == getSubtaskId(id).size() * 2) {
            epicList.get(id).setTaskStatus(TasksStatus.DONE);
        } else if (checkStatus > 0 && checkStatus < getSubtaskId(id).size() * 2) {
            epicList.get(id).setTaskStatus(TasksStatus.IN_PROGRESS);
        } else if (checkStatus == 0){
            epicList.get(id).setTaskStatus(TasksStatus.NEW);
        }
    }

}