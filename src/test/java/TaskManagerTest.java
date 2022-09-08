import tasksManager.Managers.TaskManager;
import tasksManager.Managers.TaskValidationException;
import org.junit.jupiter.api.Test;

public abstract class TaskManagerTest <T extends TaskManager> {

    // Добавляем новые объекты
    @Test
    void addTaskTest() throws TaskValidationException {}

    @Test
    void  addEpicTest() {}

    @Test
    void addSubtaskTest() {}

    // Обновляем объекты
    @Test
    void updateTaskTest() throws TaskValidationException {}

    @Test
    void updateEpicTest() {}

    @Test
    void updateSubtaskTest() {}

    // Получаем список всех объектов
    @Test
    void getTaskListTest() throws TaskValidationException {}

    @Test
    void getEpicListTest() {}

    @Test
    void getSubtaskListTest() {}

    // Получаем список имен всех объектов
    @Test
    void getListAllTaskTest() throws TaskValidationException {}

    @Test
    void getListAllEpicTest() {}

    @Test
    void getListAllSubtaskTest() {}

    // Получаем объекты по id
    @Test
    void getTaskTest() throws TaskValidationException {}

    @Test
    void getEpicTest() {}

    @Test
    void getSubtaskTest() {}

    // Удаляем объекты по id
    @Test
    void removeTaskTest() throws TaskValidationException {}

    @Test
    void removeEpicTest() {}

    @Test
    void removeEpicSubtasksTest() {}

    @Test
    void removeSubtaskTest() {}

    // Удаляем все объекты списка
    @Test
    void removeAllTaskTest() throws TaskValidationException {}

    @Test
    void removeAllEpicTest() {}

    @Test
    void removeAllSubtaskTest() {}

    // Получаем список подзадач эпика
    @Test
    void getSubtaskIdTest() throws TaskValidationException {}

    // Рассчитываем статус эпика по статусам сабтасков
    @Test
    void updateEpicStatusTest() throws TaskValidationException {}

    // Расчет startTime эпика по startTime сабтасков
    @Test
    void updateEpicStartTimeTest() throws TaskValidationException {}

    // Апдейт endTime эпика по endTime сабтасков
    @Test
    void updateEpicEndTimeTest() throws TaskValidationException {}

    // Апдейт duration эпика по сумме duration сабтасков
    @Test
    void updateEpicDurationTest() throws TaskValidationException {}

    // Апдейт всех элементов эпика зависящих от сабтасков
    @Test
    void updateEpicElementsTest() throws TaskValidationException {}
}
