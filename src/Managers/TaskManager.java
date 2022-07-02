package Managers;

import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface TaskManager {

    // Получаем списки объектов
    HashMap<Integer, Task> getTaskList();
    HashMap<Integer, Epic> getEpicList();
    HashMap<Integer, Subtask> getSubtaskList();

    // Рассчитываем порядковый номер
    Integer setTaskId();

    // Создаем и сохраняем новые объекты
    void createNewTask(Task task);
    void createNewEpic(Epic epic);
    void createNewSubtask(Subtask subtask);

    // Получаем имена всех объектов
    ArrayList<String> getListAllTask();
    ArrayList<String> getListAllEpic();
    ArrayList<String> getListAllSubtask();

    // Получаем объекты по id
    Task getTask(int id);
    Epic getEpic(int id);
    Subtask getSubtask(int id);

    // Обновляем объекты
    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    // Удаляем объекты по id
    void removeTask(int id);
    void removeEpic(int id);
    void removeEpicSubtasks(int id);
    void removeSubtask(int id);

    // Удаляем все объекты списка
    void removeAllTask();
    void removeAllEpic();
    void removeAllSubtask();

    // Получаем список подзадач эпика
    ArrayList<Integer> addSubtaskId(int id);

    // Проверяем возвращение объекта на null
    boolean checkEpic(int id);

    // Возвращаем список подзадач эпика, если объект не null
    ArrayList<Integer> getListEpicSubtasks(int id);

    // Рассчитываем статус эпика по статусам сабтасков
    void setEpicStatus(int id);
}
