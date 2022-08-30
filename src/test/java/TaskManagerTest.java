import tasksManager.Managers.TaskManager;
import tasksManager.Tasks.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class TaskManagerTest <T extends TaskManager> {

    // Добавляем новые объекты
    @Test
    void addTaskTest() {}

    @Test
    void  addEpicTest() {}

    @Test
    void addSubtaskTest() {}

    // Обновляем объекты
    @Test
    void updateTaskTest() {}

    @Test
    void updateEpicTest() {}

    @Test
    void updateSubtaskTest() {}

    // Получаем список всех объектов
    @Test
    void getTaskListTest() {}

    @Test
    void getEpicListTest() {}

    @Test
    void getSubtaskListTest() {}

    // Получаем список имен всех объектов
    @Test
    void getListAllTaskTest() {}

    @Test
    void getListAllEpicTest() {}

    @Test
    void getListAllSubtaskTest() {}

    // Получаем объекты по id
    @Test
    void getTaskTest() {}

    @Test
    void getEpicTest() {}

    @Test
    void getSubtaskTest() {}

    // Удаляем объекты по id
    @Test
    void removeTaskTest() {}

    @Test
    void removeEpicTest() {}

    @Test
    void removeEpicSubtasksTest() {}

    @Test
    void removeSubtaskTest() {}

    // Удаляем все объекты списка
    @Test
    void removeAllTaskTest() {}

    @Test
    void removeAllEpicTest() {}

    @Test
    void removeAllSubtaskTest() {}

    // Получаем список подзадач эпика
    @Test
    void getSubtaskIdTest() {}

    // Рассчитываем статус эпика по статусам сабтасков
    @Test
    void updateEpicStatusTest() {}

    // Расчет startTime эпика по startTime сабтасков
    @Test
    void updateEpicStartTimeTest() {}

    // Апдейт endTime эпика по endTime сабтасков
    @Test
    void updateEpicEndTimeTest() {}

    // Апдейт duration эпика по сумме duration сабтасков
    @Test
    void updateEpicDurationTest() {}

    // Апдейт всех элементов эпика зависящих от сабтасков
    @Test
    void updateEpicElementsTest() {}
}
