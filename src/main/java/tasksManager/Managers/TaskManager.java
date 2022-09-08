package tasksManager.Managers;

import tasksManager.Tasks.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public interface TaskManager {

    // Получаем списки объектов
    HashMap<Integer, Task> getTaskList();
    HashMap<Integer, Epic> getEpicList();
    HashMap<Integer, Subtask> getSubtaskList();

    // Создаем и сохраняем новые объекты
    int addTask(Task task) throws TaskValidationException;
    int addEpic(Epic epic);
    int addSubtask(Subtask subtask);

    // Получаем имена всех объектов
    ArrayList<String> getListAllTask();
    ArrayList<String> getListAllEpic();
    ArrayList<String> getListAllSubtask();

    // Получаем объекты по id
    Task getTask(int id) throws TaskValidationException;
    Epic getEpic(int id);
    Subtask getSubtask(int id);

    // Обновляем объекты
    void updateTask(Task task) throws TaskValidationException;

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    // Удаляем объекты по id
    void removeTask(int id) throws TaskValidationException;
    void removeEpic(int id);
    void removeEpicSubtasks(int id);
    void removeSubtask(int id);

    // Удаляем все объекты списка
    void removeAllTask() throws TaskValidationException;
    void removeAllEpic();
    void removeAllSubtask();

    // Получаем список подзадач эпика
    ArrayList<Integer> getSubtaskId(int id);

    // Рассчитываем статус эпика по статусам сабтасков
    void updateEpicStatus(int id) throws TaskValidationException;

    // Расчет startTime эпика по startTime сабтасков
    void updateEpicStartTime(int id) throws TaskValidationException;

    // Апдейт endTime эпика по endTime сабтасков
    void updateEpicEndTime(int id) throws TaskValidationException;

    // Апдейт duration эпика по сумме duration сабтасков
    void updateEpicDuration(int id) throws TaskValidationException;

    // Апдейт всех элементов эпика зависящих от сабтасков
    void updateEpicElements(int id) throws TaskValidationException;

    TreeSet<Task> getPrioritizedTasks();

    public List<Task> getHistory();

    Task fromSring(String task);
}
