import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasksManager.Managers.InMemoryTasksManager;
import tasksManager.Tasks.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTasksManagerTest extends TaskManagerTest {
    private static InMemoryTasksManager taskManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = new InMemoryTasksManager();
    }

    @Test
    void addTaskTest() {
        Task task = new Task(null, TasksType.TASK, "Задача 1", TasksStatus.NEW,
                "Текст задачи 1", "01.09.2022 10:00", 15, null);

        final int taskId = taskManager.addTask(task);
        final Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final HashMap<Integer, Task> tasks = taskManager.getTaskList();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(taskId), "Задачи не совпадают.");
    }

    @Test
    void  addEpicTest() {
        Epic task = new Epic(null, TasksType.EPIC,"Эпик 2", TasksStatus.NEW,
                "Текст эпика 2",null, null, null);

        final int taskId = taskManager.addEpic(task);
        final Epic savedTask = taskManager.getEpic(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final HashMap<Integer, Epic> tasks = taskManager.getEpicList();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(1), "Задачи не совпадают.");
    }

    @Test
    void addSubtaskTest() {
        Subtask task = new Subtask(null, TasksType.SUBTASK,"Сабтаск 3 к эпику 2",
                TasksStatus.NEW,"Текст сабтаска 3", "01.09.2022 11:00", 30,2);

        final int taskId = taskManager.addSubtask(task);
        final Subtask savedTask = taskManager.getSubtask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final HashMap<Integer, Subtask> tasks = taskManager.getSubtaskList();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(1), "Задачи не совпадают.");
    }

    // Обновляем объекты
    @Test
    void updateTaskTest() {
        Task task1 = new Task(null, TasksType.TASK, "Задача 1", TasksStatus.NEW,
                "Текст задачи 1", "01.09.2022 10:00", 15, null);
        final int taskId = taskManager.addTask(task1);

        Task task2 = new Task(taskId, TasksType.TASK, "New Задача 1", TasksStatus.NEW,
                "Текст задачи 1", "01.09.2022 10:00", 60, null);
        taskManager.updateTask(task2);

        final Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task2, savedTask, "Задачи не совпадают.");

        final HashMap<Integer, Task> tasks = taskManager.getTaskList();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task2, tasks.get(1), "Задачи не совпадают.");
    }

    @Test
    void updateEpicTest() {
        Epic task1 = new Epic(null, TasksType.EPIC,"Эпик 2", TasksStatus.NEW,
                "Текст эпика 2",null, null, null);
        final int taskId = taskManager.addEpic(task1);

        Epic task2 = new Epic(taskId, TasksType.EPIC,"New Эпик 2", TasksStatus.NEW,
                "Текст эпика 2","01.01.2022 10:00", 10, null);
        taskManager.updateEpic(task2);

        final Epic savedTask = taskManager.getEpic(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task2, savedTask, "Задачи не совпадают.");

        final HashMap<Integer, Epic> tasks = taskManager.getEpicList();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task2, tasks.get(1), "Задачи не совпадают.");
    }

    @Test
    void updateSubtaskTest() {
        Epic epic = new Epic(null, TasksType.EPIC,"Эпик 1", TasksStatus.NEW,
                "Текст эпика 1",null, null, null);
        taskManager.addEpic(epic);

        Subtask task1 = new Subtask(null, TasksType.SUBTASK,"Сабтаск к эпику 1",
                TasksStatus.NEW,"Текст сабтаска", "01.09.2022 11:00", 30,1);
        final int taskId = taskManager.addSubtask(task1);

        Subtask task2 = new Subtask(taskId, TasksType.SUBTASK,"New Сабтаск к эпику 1",
                TasksStatus.NEW,"Текст сабтаска", "03.09.2022 15:00", 60,1);
        taskManager.updateSubtask(task2);

        final Subtask savedTask = taskManager.getSubtask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task2, savedTask, "Задачи не совпадают.");

        final HashMap<Integer, Subtask> tasks = taskManager.getSubtaskList();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task2, tasks.get(2), "Задачи не совпадают.");

        // при отсутствии эпика
        Subtask task3 = new Subtask(null, TasksType.SUBTASK,"Сабтаск к эпику 1",
                TasksStatus.NEW,"Текст сабтаска", "01.09.2022 11:00", 30,15);
        final int taskId3 = taskManager.addSubtask(task1);

        Subtask task4 = new Subtask(taskId3, TasksType.SUBTASK,"New Сабтаск к эпику 1",
                TasksStatus.NEW,"Текст сабтаска", "03.09.2022 15:00", 60,15);
        taskManager.updateSubtask(task4);

        final Subtask savedTask2 = taskManager.getSubtask(taskId3);

        assertNotNull(savedTask2, "Задача не найдена.");
        assertEquals(task4, savedTask2, "Задачи не совпадают.");

        final HashMap<Integer, Subtask> tasks2 = taskManager.getSubtaskList();

        assertNotNull(tasks2, "Задачи на возвращаются.");
        assertEquals(2, tasks2.size(), "Неверное количество задач.");
        assertEquals(task4, tasks2.get(3), "Задачи не совпадают.");
    }

    // Получаем список всех объектов
    @Test
    void getTaskListTest() {
        // пустой список
        final HashMap<Integer, Task> list1 = taskManager.getTaskList();

        assertNotNull(list1, "Cписок не возвращается.");
        assertEquals(0, list1.size(), "Неверное количество задач.");

        // непустой список
        Task task = new Task(null, TasksType.TASK, "Задача 1", TasksStatus.NEW,
                "Текст задачи 1", "01.09.2022 10:00", 15, null);
        taskManager.addTask(task);

        final HashMap<Integer, Task> list2 = taskManager.getTaskList();

        assertNotNull(list2, "Cписок не возвращается.");
        assertEquals(1, list2.size(), "Неверное количество задач.");
    }

    @Test
    void getEpicListTest() {
        // пустой список
        final HashMap<Integer, Epic> list1 = taskManager.getEpicList();

        assertNotNull(list1, "Cписок не возвращается.");
        assertEquals(0, list1.size(), "Неверное количество задач.");

        // непустой список
        Epic task = new Epic(null, TasksType.EPIC,"Эпик 2", TasksStatus.NEW,
                "Текст эпика 2",null, null, null);
        taskManager.addEpic(task);

        final HashMap<Integer, Epic> list2 = taskManager.getEpicList();

        assertNotNull(list2, "Cписок не возвращается.");
        assertEquals(1, list2.size(), "Неверное количество задач.");
    }

    @Test
    void getSubtaskListTest() {
        // пустой список
        final HashMap<Integer, Subtask> list1 = taskManager.getSubtaskList();

        assertNotNull(list1, "Cписок не возвращается.");
        assertEquals(0, list1.size(), "Неверное количество задач.");

        // непустой список
        Subtask task = new Subtask(null, TasksType.SUBTASK,"Сабтаск 3 к эпику 2",
                TasksStatus.NEW,"Текст сабтаска 3", "01.09.2022 11:00", 30,2);
        taskManager.addSubtask(task);

        final HashMap<Integer, Subtask> list2 = taskManager.getSubtaskList();

        assertNotNull(list2, "Cписок не возвращается.");
        assertEquals(1, list2.size(), "Неверное количество задач.");
    }

    // Получаем список имен всех объектов
    @Test
    void getListAllTaskTest() {
        // пустой список
        final ArrayList<String> list1 = taskManager.getListAllTask();

        assertNotNull(list1, "Cписок не возвращается.");
        assertEquals(0, list1.size(), "Неверное количество задач.");

        // непустой список
        Task task = new Task(null, TasksType.TASK, "Задача 1", TasksStatus.NEW,
                "Текст задачи 1", "01.09.2022 10:00", 15, null);
        taskManager.addTask(task);

        final ArrayList<String> list2 = taskManager.getListAllTask();

        assertNotNull(list2, "Cписок не возвращается.");
        assertEquals(1, list2.size(), "Неверное количество задач.");
        assertEquals(task.getTaskName(), list2.get(0), "Задачи не совпадают.");
    }

    @Test
    void getListAllEpicTest() {
        // пустой список
        final ArrayList<String> list1 = taskManager.getListAllEpic();

        assertNotNull(list1, "Cписок не возвращается.");
        assertEquals(0, list1.size(), "Неверное количество задач.");

        // непустой список
        Epic task = new Epic(null, TasksType.EPIC,"Эпик 2", TasksStatus.NEW,
                "Текст эпика 2",null, null, null);
        taskManager.addEpic(task);

        final ArrayList<String> list2 = taskManager.getListAllEpic();

        assertNotNull(list2, "Cписок не возвращается.");
        assertEquals(1, list2.size(), "Неверное количество задач.");
        assertEquals(task.getTaskName(), list2.get(0), "Задачи не совпадают.");
    }

    @Test
    void getListAllSubtaskTest() {
        // пустой список
        final ArrayList<String> list1 = taskManager.getListAllSubtask();

        assertNotNull(list1, "Cписок не возвращается.");
        assertEquals(0, list1.size(), "Неверное количество задач.");

        // непустой список
        Subtask task = new Subtask(null, TasksType.SUBTASK,"Сабтаск 3 к эпику 2",
                TasksStatus.NEW,"Текст сабтаска 3", "01.09.2022 11:00", 30,2);
        taskManager.addSubtask(task);
        final ArrayList<String> list2 = taskManager.getListAllSubtask();

        assertNotNull(list2, "Cписок не возвращается.");
        assertEquals(1, list2.size(), "Неверное количество задач.");
        assertEquals(task.getTaskName(), list2.get(0), "Задачи не совпадают.");
    }

    // Получаем объекты по id
    @Test
    void getTaskTest() {
        // проверка существующего id
        Task task = new Task(null, TasksType.TASK, "Задача 1", TasksStatus.NEW,
                "Текст задачи 1", "01.09.2022 10:00", 15, null);

        final int taskId = taskManager.addTask(task);
        final Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        // проверка несуществующего id
        final Task savedTask2 = taskManager.getTask(4545);
        assertNull(savedTask2, "Вернулась несуществующая задача.");

        final Task savedTask3 = taskManager.getTask(0);
        assertNull(savedTask3, "Вернулась несуществующая задача.");
    }

    @Test
    void getEpicTest() {
        // проверка существующего id
        Epic task = new Epic(null, TasksType.EPIC,"Эпик 2", TasksStatus.NEW,
                "Текст эпика 2",null, null, null);

        final int taskId = taskManager.addEpic(task);
        final Epic savedTask = taskManager.getEpic(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        // проверка несуществующего id
        final Epic savedTask2 = taskManager.getEpic(4545);
        assertNull(savedTask2, "Вернулась несуществующая задача.");

        final Epic savedTask3 = taskManager.getEpic(0);
        assertNull(savedTask3, "Вернулась несуществующая задача.");
    }

    @Test
    void getSubtaskTest() {
        // проверка существующего id
        Subtask task = new Subtask(null, TasksType.SUBTASK,"Сабтаск 3 к эпику 2",
                TasksStatus.NEW,"Текст сабтаска 3", "01.09.2022 11:00", 30,2);

        final int taskId = taskManager.addSubtask(task);
        final Subtask savedTask = taskManager.getSubtask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        // проверка несуществующего id
        final Subtask savedTask2 = taskManager.getSubtask(4545);
        assertNull(savedTask2, "Вернулась несуществующая задача.");

        final Subtask savedTask3 = taskManager.getSubtask(0);
        assertNull(savedTask3, "Вернулась несуществующая задача.");
    }

    // Удаляем объекты по id
    @Test
    void removeTaskTest() {
        // проверка существующего id
        Task task = new Task(null, TasksType.TASK, "Задача 1", TasksStatus.NEW,
                "Текст задачи 1", "01.09.2022 10:00", 15, null);

        final int taskId = taskManager.addTask(task);
        final HashMap<Integer, Task> list1 = taskManager.getTaskList();
        assertEquals(1, list1.size(), "Неверное количество задач.");

        taskManager.removeTask(taskId);
        final HashMap<Integer, Task> list2 = taskManager.getTaskList();
        assertEquals(0, list2.size(), "Задача не удалена.");

        // проверка несуществующего id
        taskManager.addTask(task);
        taskManager.removeTask(4545);
        final HashMap<Integer, Task> list3 = taskManager.getTaskList();
        assertEquals(1, list3.size(), "Удалена несуществующая задача.");

        taskManager.removeTask(0);
        final HashMap<Integer, Task> list4 = taskManager.getTaskList();
        assertEquals(1, list4.size(), "Удалена несуществующая задача.");
    }

    @Test
    void removeEpicTest() {
        // проверка существующего id
        Epic task = new Epic(null, TasksType.EPIC,"Эпик 2", TasksStatus.NEW,
                "Текст эпика 2",null, null, null);
        final int taskId = taskManager.addEpic(task);

        Subtask task1 = new Subtask(null, TasksType.SUBTASK,"Сабтаск к эпику 1",
                TasksStatus.NEW,"Текст сабтаска", "01.09.2022 11:00", 30,taskId);
        final int taskId1 = taskManager.addSubtask(task1);

        Subtask task2 = new Subtask(null, TasksType.SUBTASK,"New Сабтаск к эпику 1",
                TasksStatus.NEW,"Текст сабтаска", "03.09.2022 15:00", 60,taskId);
        final int taskId2 = taskManager.addSubtask(task2);

        final HashMap<Integer, Epic> list1 = taskManager.getEpicList();
        assertNotNull(list1, "Задачи не найдены.");
        assertEquals(1, list1.size(), "Неверное количество задач.");

        final ArrayList<Integer> list = taskManager.getSubtaskId(taskId);
        assertNotNull(list, "Задачи не найдены.");
        assertEquals(2, list.size(), "Неверное количество задач.");

        taskManager.removeEpic(taskId);
        final HashMap<Integer, Epic> list2 = taskManager.getEpicList();
        assertEquals(0, list2.size(), "Задача не удалена.");

        final HashMap<Integer, Subtask> list5 = taskManager.getSubtaskList();
        assertNotNull(list5, "Не возвращается пустой список.");
        assertEquals(0, list5.size(), "Неверное количество задач.");

        final ArrayList<Integer> list6 = taskManager.getSubtaskId(taskId);
        assertNotNull(list6, "Не возвращается пустой список.");
        assertEquals(0, list6.size(), "Неверное количество задач.");

        // проверка несуществующего id
        taskManager.addEpic(task);
        taskManager.removeEpic(4545);
        final HashMap<Integer, Epic> list3 = taskManager.getEpicList();
        assertEquals(1, list3.size(), "Удалена несуществующая задача.");

        taskManager.removeEpic(0);
        final HashMap<Integer, Epic> list4 = taskManager.getEpicList();
        assertEquals(1, list4.size(), "Удалена несуществующая задача.");


    }

    @Test
    void removeEpicSubtasksTest() {
        // существующий id эпика
        Epic epic = new Epic(null, TasksType.EPIC,"Эпик 1", TasksStatus.NEW,
                "Текст эпика 1",null, null, null);
        final int taskId0 = taskManager.addEpic(epic);

        Subtask task1 = new Subtask(null, TasksType.SUBTASK,"Сабтаск к эпику 1",
                TasksStatus.NEW,"Текст сабтаска", "01.09.2022 11:00", 30,taskId0);
        final int taskId1 = taskManager.addSubtask(task1);

        Subtask task2 = new Subtask(null, TasksType.SUBTASK,"New Сабтаск к эпику 1",
                TasksStatus.NEW,"Текст сабтаска", "03.09.2022 15:00", 60,taskId0);
        final int taskId2 = taskManager.addSubtask(task2);

        final ArrayList<Integer> list = taskManager.getSubtaskId(taskId0);

        assertNotNull(list, "Задачи не найдены.");
        assertEquals(2, list.size(), "Неверное количество задач.");

        taskManager.removeEpicSubtasks(taskId0);
        final HashMap<Integer, Subtask> list1 = taskManager.getSubtaskList();
        assertNotNull(list1, "Не возвращается пустой список.");
        assertEquals(0, list1.size(), "Неверное количество задач.");

        final ArrayList<Integer> list2 = taskManager.getSubtaskId(taskId0);
        assertNotNull(list2, "Не возвращается пустой список.");
        assertEquals(0, list2.size(), "Неверное количество задач.");

        taskManager.removeEpicSubtasks(4545);
        final ArrayList<Integer> list3 = taskManager.getSubtaskId(4545);

        assertNotNull(list3, "Не возвращается пустой список.");
        assertEquals(0, list3.size(), "Неверное количество задач.");

        taskManager.removeEpicSubtasks(0);
        final ArrayList<Integer> list4 = taskManager.getSubtaskId(0);

        assertNotNull(list4, "Не возвращается пустой список.");
        assertEquals(0, list4.size(), "Неверное количество задач.");
    }

    @Test
    void removeSubtaskTest() {
        // проверка существующего id
        Subtask task = new Subtask(null, TasksType.SUBTASK,"Сабтаск 3 к эпику 2",
                TasksStatus.NEW,"Текст сабтаска 3", "01.09.2022 11:00", 30,2);
        final int taskId = taskManager.addSubtask(task);
        final HashMap<Integer, Subtask> list1 = taskManager.getSubtaskList();
        assertEquals(1, list1.size(), "Неверное количество задач.");

        taskManager.removeSubtask(taskId);
        final HashMap<Integer, Subtask> list2 = taskManager.getSubtaskList();
        assertEquals(0, list2.size(), "Задача не удалена.");

        // проверка несуществующего id
        taskManager.addSubtask(task);
        taskManager.removeSubtask(4545);
        final HashMap<Integer, Subtask> list3 = taskManager.getSubtaskList();
        assertEquals(1, list3.size(), "Удалена несуществующая задача.");

        taskManager.removeSubtask(0);
        final HashMap<Integer, Subtask> list4 = taskManager.getSubtaskList();
        assertEquals(1, list4.size(), "Удалена несуществующая задача.");

    }

    // Удаляем все объекты списка
    @Test
    void removeAllTaskTest() {
        Task task = new Task(null, TasksType.TASK, "Задача 1", TasksStatus.NEW,
                "Текст задачи 1", "01.09.2022 10:00", 15, null);
        Task task2 = new Task(null, TasksType.TASK, "Задача 1", TasksStatus.NEW,
                "Текст задачи 1", "01.09.2022 10:00", 15, null);
        taskManager.addTask(task);
        taskManager.addTask(task2);

        final HashMap<Integer, Task> list1 = taskManager.getTaskList();
        assertEquals(2, list1.size(), "Неверное количество задач.");

        taskManager.removeAllTask();
        final HashMap<Integer, Task> list2 = taskManager.getTaskList();
        assertEquals(0, list2.size(), "Неверное количество задач.");

        taskManager.removeAllTask();
        final HashMap<Integer, Task> list3 = taskManager.getTaskList();
        assertEquals(0, list3.size(), "Неверное количество задач.");
    }

    @Test
    void removeAllEpicTest() {
        Epic epic = new Epic(null, TasksType.EPIC,"Эпик 1", TasksStatus.NEW,
                "Текст эпика 1",null, null, null);
        Epic epic2 = new Epic(null, TasksType.EPIC,"Эпик 2", TasksStatus.NEW,
                "Текст эпика 2",null, null, null);
        final int taskId0 = taskManager.addEpic(epic);
        taskManager.addEpic(epic2);

        Subtask task1 = new Subtask(null, TasksType.SUBTASK,"Сабтаск к эпику 1",
                TasksStatus.NEW,"Текст сабтаска", "01.09.2022 11:00", 30,taskId0);
        Subtask task2 = new Subtask(null, TasksType.SUBTASK,"New Сабтаск к эпику 1",
                TasksStatus.NEW,"Текст сабтаска", "03.09.2022 15:00", 60,taskId0);
        taskManager.addSubtask(task1);
        taskManager.addSubtask(task2);

        final ArrayList<Integer> list = taskManager.getSubtaskId(taskId0);
        assertNotNull(list, "Задачи не найдены.");
        assertEquals(2, list.size(), "Неверное количество задач.");

        taskManager.removeAllEpic();
        final HashMap<Integer, Subtask> list1 = taskManager.getSubtaskList();
        assertNotNull(list1, "Не возвращается пустой список.");
        assertEquals(0, list1.size(), "Неверное количество задач.");

        final ArrayList<Integer> list2 = taskManager.getSubtaskId(taskId0);
        assertNotNull(list2, "Не возвращается пустой список.");
        assertEquals(0, list2.size(), "Неверное количество задач.");

        final HashMap<Integer, Epic> list3 = taskManager.getEpicList();
        assertEquals(0, list3.size(), "Неверное количество задач.");

        taskManager.removeAllTask();
        final HashMap<Integer, Epic> list4 = taskManager.getEpicList();
        assertEquals(0, list4.size(), "Неверное количество задач.");
    }

    @Test
    void removeAllSubtaskTest() {
        Subtask task = new Subtask(null, TasksType.SUBTASK,"Сабтаск 3 к эпику 2",
                TasksStatus.NEW,"Текст сабтаска 3", "01.09.2022 11:00", 30,2);
        Subtask task2 = new Subtask(null, TasksType.SUBTASK,"Сабтаск 3 к эпику 2",
                TasksStatus.NEW,"Текст сабтаска 3", "01.09.2022 11:00", 30,2);
        taskManager.addSubtask(task);
        taskManager.addSubtask(task2);

        final HashMap<Integer, Subtask> list1 = taskManager.getSubtaskList();
        assertEquals(2, list1.size(), "Неверное количество задач.");

        taskManager.removeAllSubtask();
        final HashMap<Integer, Subtask> list2 = taskManager.getSubtaskList();
        assertEquals(0, list2.size(), "Задача не удалена.");

        taskManager.removeAllSubtask();
        final HashMap<Integer, Subtask> list3 = taskManager.getSubtaskList();
        assertEquals(0, list3.size(), "Задача не удалена.");
    }

    // Получаем список подзадач эпика
    @Test
    void getSubtaskIdTest() {
        // существующий id эпика
        Epic epic = new Epic(null, TasksType.EPIC,"Эпик 1", TasksStatus.NEW,
                "Текст эпика 1",null, null, null);
        final int taskId0 = taskManager.addEpic(epic);

        Subtask task1 = new Subtask(null, TasksType.SUBTASK,"Сабтаск к эпику 1",
                TasksStatus.NEW,"Текст сабтаска", "01.09.2022 11:00", 30,taskId0);
        final int taskId1 = taskManager.addSubtask(task1);

        Subtask task2 = new Subtask(null, TasksType.SUBTASK,"New Сабтаск к эпику 1",
                TasksStatus.NEW,"Текст сабтаска", "03.09.2022 15:00", 60,taskId0);
        final int taskId2 = taskManager.addSubtask(task1);

        final ArrayList<Integer> list = taskManager.getSubtaskId(taskId0);

        assertNotNull(list, "Задачи не найдены.");
        assertEquals(2, list.size(), "Неверное количество задач.");
        assertEquals(taskId1, list.get(0), "Неверный id сабтаска.");
        assertEquals(taskId2, list.get(1), "Неверный id сабтаска.");

        // несуществующий id эпика
        final ArrayList<Integer> list1 = taskManager.getSubtaskId(4545);

        assertNotNull(list1, "Не возвращается пустой список.");
        assertEquals(0, list1.size(), "Неверное количество задач.");

        final ArrayList<Integer> list2 = taskManager.getSubtaskId(0);

        assertNotNull(list2, "Не возвращается пустой список.");
        assertEquals(0, list2.size(), "Неверное количество задач.");
    }

    // Рассчитываем статус эпика по статусам сабтасков
    // Пустой список подзадач
    @Test
    void updateEpicStatusNullSubtaskTest() {
        // нет сабтасков
        Epic epic = new Epic(null, TasksType.EPIC,"Эпик 1", TasksStatus.NEW,
                "Текст эпика 1",null, null, null);
        final int taskId = taskManager.addEpic(epic);

        final TasksStatus epicStatus = taskManager.getEpic(taskId).getTaskStatus();

        assertNotNull(epic.getTaskStatus(), "Статус эпика null.");
        assertEquals(TasksStatus.NEW, epicStatus, "Статус не совпадает.");

        // Все подзадачи со статусом NEW
        Subtask task1 = new Subtask(null, TasksType.SUBTASK,"Сабтаск к эпику 1",
                TasksStatus.NEW,"Текст сабтаска", "01.09.2022 11:00", 30,taskId);
        final int t1 = taskManager.addSubtask(task1);

        Subtask task2 = new Subtask(null, TasksType.SUBTASK,"New Сабтаск к эпику 1",
                TasksStatus.NEW,"Текст сабтаска", "03.09.2022 15:00", 60,taskId);
        final int t2 = taskManager.addSubtask(task2);
        taskManager.updateEpicStatus(taskId);

        final TasksStatus epicStatus2 = taskManager.getEpic(taskId).getTaskStatus();

        assertNotNull(epic.getTaskStatus(), "Статус эпика null.");
        assertEquals(TasksStatus.NEW, epicStatus2, "Статус не совпадает.");

        // Подзадачи со статусом IN_PROGRESS
        task1 = new Subtask(t1, TasksType.SUBTASK,"Сабтаск к эпику 1",
                TasksStatus.NEW,"Текст сабтаска", "01.09.2022 11:00", 30,taskId);
        taskManager.updateSubtask(task1);

        task2 = new Subtask(t2, TasksType.SUBTASK,"New Сабтаск к эпику 1",
                TasksStatus.IN_PROGRESS,"Текст сабтаска", "03.09.2022 15:00", 60,taskId);
        taskManager.updateSubtask(task2);
        taskManager.updateEpicStatus(taskId);

        final TasksStatus epicStatus3 = taskManager.getEpic(taskId).getTaskStatus();

        assertNotNull(epic.getTaskStatus(), "Статус эпика null.");
        assertEquals(TasksStatus.IN_PROGRESS, epicStatus3, "Статус не совпадает.");

        // Подзадачи со статусами NEW и DONE
        task1 = new Subtask(t1, TasksType.SUBTASK,"Сабтаск к эпику 1",
                TasksStatus.NEW,"Текст сабтаска", "01.09.2022 11:00", 30,taskId);
        taskManager.updateSubtask(task1);

        task2 = new Subtask(t2, TasksType.SUBTASK,"New Сабтаск к эпику 1",
                TasksStatus.DONE,"Текст сабтаска", "03.09.2022 15:00", 60,taskId);
        taskManager.updateSubtask(task2);
        taskManager.updateEpicStatus(taskId);

        final TasksStatus epicStatus4 = taskManager.getEpic(taskId).getTaskStatus();

        assertNotNull(epic.getTaskStatus(), "Статус эпика null.");
        assertEquals(TasksStatus.IN_PROGRESS, epicStatus4, "Статус не совпадает.");

        // Все подзадачи со статусом DONE
        task1 = new Subtask(t1, TasksType.SUBTASK,"Сабтаск к эпику 1",
                TasksStatus.DONE,"Текст сабтаска", "01.09.2022 11:00", 30,taskId);
        taskManager.updateSubtask(task1);

        task2 = new Subtask(t2, TasksType.SUBTASK,"New Сабтаск к эпику 1",
                TasksStatus.DONE,"Текст сабтаска", "03.09.2022 15:00", 60,taskId);
        taskManager.updateSubtask(task2);
        taskManager.updateEpicStatus(taskId);

        final TasksStatus epicStatus5 = taskManager.getEpic(taskId).getTaskStatus();

        assertNotNull(epic.getTaskStatus(), "Статус эпика null.");
        assertEquals(TasksStatus.DONE, epicStatus5, "Статус не совпадает.");

    }

    // Расчет startTime эпика по startTime сабтасков
    @Test
    void updateEpicStartTimeTest() {
        Epic epic = new Epic(null, TasksType.EPIC,"Эпик 1", TasksStatus.NEW,
                "Текст эпика 1",null, null, null);
        final int taskId = taskManager.addEpic(epic);

        assertNull(epic.getStartTime(), "StartTime эпика не null.");

        // StartTime сабтаска null
        Subtask task0 = new Subtask(null, TasksType.SUBTASK,"Сабтаск к эпику 1",
                TasksStatus.DONE,"Текст сабтаска", null, null,taskId);
        taskManager.addSubtask(task0);
        taskManager.updateEpicStartTime(taskId);

        assertNull(epic.getStartTime(), "StartTime эпика не null.");

        // StartTime сабтаска не null
        Subtask task1 = new Subtask(null, TasksType.SUBTASK,"Сабтаск к эпику 1",
                TasksStatus.DONE,"Текст сабтаска", "01.09.2022 11:00", 30,taskId);
        Subtask task2 = new Subtask(null, TasksType.SUBTASK,"New Сабтаск к эпику 1",
                TasksStatus.DONE,"Текст сабтаска", "03.09.2022 15:00", 60,taskId);

        taskManager.addSubtask(task1);
        taskManager.addSubtask(task2);
        taskManager.updateEpicStartTime(taskId);

        final LocalDateTime epicStartTime = taskManager.getEpic(taskId).getStartTime();

        assertNotNull(epic.getStartTime(), "StartTime эпика null.");
        assertEquals(taskManager.getSubtask(3).getStartTime(), epicStartTime,
                "StartTime эпика и сабтаска не совпадает.");
    }

    // Апдейт endTime эпика по endTime сабтасков
    @Test
    void updateEpicEndTimeTest() {
        Epic epic = new Epic(null, TasksType.EPIC,"Эпик 1", TasksStatus.NEW,
                "Текст эпика 1",null, null, null);
        final int taskId = taskManager.addEpic(epic);

        assertNull(epic.getEpicEndTime(), "EndTime эпика не null.");

        Subtask task1 = new Subtask(null, TasksType.SUBTASK,"Сабтаск к эпику 1",
                TasksStatus.DONE,"Текст сабтаска", "01.09.2022 11:00", 30,taskId);
        Subtask task2 = new Subtask(null, TasksType.SUBTASK,"New Сабтаск к эпику 1",
                TasksStatus.DONE,"Текст сабтаска", "03.09.2022 15:00", 60,taskId);

        taskManager.addSubtask(task1);
        taskManager.addSubtask(task2);
        taskManager.updateEpicEndTime(taskId);

        final LocalDateTime epicEndTime = taskManager.getEpic(taskId).getEpicEndTime();

        assertNotNull(epic.getEpicEndTime(), "EndTime эпика null.");
        assertEquals(taskManager.getSubtask(3).getEndTime(), epicEndTime,
                "EndTime эпика и сабтаска не совпадает.");
    }

    // Апдейт duration эпика по сумме duration сабтасков
    @Test
    void updateEpicDurationTest() {
        Epic epic = new Epic(null, TasksType.EPIC,"Эпик 1", TasksStatus.NEW,
                "Текст эпика 1",null, null, null);
        final int taskId = taskManager.addEpic(epic);

        assertNotNull(epic.getDuration(), "Duration эпика null.");
        assertEquals(0, taskManager.getEpic(taskId).getDuration(), "Duration не совпадает.");

        Subtask task1 = new Subtask(null, TasksType.SUBTASK,"Сабтаск к эпику 1",
                TasksStatus.DONE,"Текст сабтаска", "01.09.2022 11:00", 30,taskId);
        Subtask task2 = new Subtask(null, TasksType.SUBTASK,"New Сабтаск к эпику 1",
                TasksStatus.DONE,"Текст сабтаска", "03.09.2022 15:00", 60,taskId);

        taskManager.addSubtask(task1);
        taskManager.addSubtask(task2);
        taskManager.updateEpicDuration(taskId);

        assertNotNull(epic.getDuration(), "Duration эпика null.");
        assertEquals(90, taskManager.getEpic(taskId).getDuration(), "Duration не совпадает.");
    }

    // Апдейт всех элементов эпика зависящих от сабтасков
    @Test
    void updateEpicElementsTest() {
        Epic epic = new Epic(null, TasksType.EPIC,"Эпик 1", TasksStatus.NEW,
                "Текст эпика 1",null, null, null);
        final int taskId = taskManager.addEpic(epic);

        Subtask task1 = new Subtask(null, TasksType.SUBTASK,"Сабтаск к эпику 1",
                TasksStatus.DONE,"Текст сабтаска", "01.09.2022 11:00", 30,taskId);
        Subtask task2 = new Subtask(null, TasksType.SUBTASK,"New Сабтаск к эпику 1",
                TasksStatus.DONE,"Текст сабтаска", "03.09.2022 15:00", 60,taskId);

        taskManager.addSubtask(task1);
        taskManager.addSubtask(task2);
        taskManager.updateEpicElements(taskId);

        assertNotNull(epic.getTaskStatus(), "Статус эпика null.");
        assertEquals(TasksStatus.DONE, taskManager.getEpic(taskId).getTaskStatus(), "Статус не совпадает.");

        assertNotNull(epic.getStartTime(), "StartTime эпика null.");
        assertEquals(taskManager.getSubtask(2).getStartTime(), taskManager.getEpic(taskId).getStartTime(),
                "StartTime эпика и сабтаска не совпадает.");

        assertNotNull(epic.getEpicEndTime(), "EndTime эпика null.");
        assertEquals(taskManager.getSubtask(3).getEndTime(), taskManager.getEpic(taskId).getEpicEndTime(),
                "EndTime эпика и сабтаска не совпадает.");

        assertNotNull(epic.getDuration(), "Duration эпика null.");
        assertEquals(90, taskManager.getEpic(taskId).getDuration(), "Duration не совпадает.");
    }

}
