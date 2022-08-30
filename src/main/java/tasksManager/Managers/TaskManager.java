package tasksManager.Managers;

import tasksManager.Tasks.*;
import java.util.ArrayList;
import java.util.HashMap;

public interface TaskManager {

    // Получаем списки объектов
    HashMap<Integer, Task> getTaskList();
    HashMap<Integer, Epic> getEpicList();
    HashMap<Integer, Subtask> getSubtaskList();

    // Создаем и сохраняем новые объекты
    int addTask(Task task);
    int addEpic(Epic epic);
    int addSubtask(Subtask subtask);

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
    ArrayList<Integer> getSubtaskId(int id);

    // Рассчитываем статус эпика по статусам сабтасков
    void updateEpicStatus(int id);

    // Расчет startTime эпика по startTime сабтасков
    void updateEpicStartTime(int id);

    // Апдейт endTime эпика по endTime сабтасков
    void updateEpicEndTime(int id);

    // Апдейт duration эпика по сумме duration сабтасков
    void updateEpicDuration(int id);

    // Апдейт всех элементов эпика зависящих от сабтасков
    void updateEpicElements(int id);
}
