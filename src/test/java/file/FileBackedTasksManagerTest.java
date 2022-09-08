package file;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasksManager.Managers.file.FileBackedTasksManager;
import tasksManager.Managers.TaskValidationException;
import tasksManager.Tasks.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTasksManagerTest extends TaskManagerTest {

    private static FileBackedTasksManager backedTasksManager;
    private File file = new File("task_storage.csv");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @BeforeEach
    public void beforeEach() {
        try (Writer fileWriter = new FileWriter(file, StandardCharsets.UTF_8)) {
            fileWriter.write("id,type,name,status,description,startTime,duration,epic\n");

        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден");
        } catch (IOException e) {
            System.out.println("IOException");
        }
        backedTasksManager = new FileBackedTasksManager(file);
    }

    @AfterEach
    public void remove() throws TaskValidationException {
        backedTasksManager.removeAllTask();
    }

    @Test
    void loadFromFileTest() throws TaskValidationException {
        Task task1 = new Task(1, TasksType.TASK, "Task1", TasksStatus.NEW,
                "Description task1", LocalDateTime.parse("01.08.2022 11:00", formatter), 15, null);
        Epic task2 = new Epic(2, TasksType.EPIC, "Epic2", TasksStatus.NEW,
                "Description epic2", LocalDateTime.parse("02.08.2022 11:00", formatter), 60, null);
        Subtask task3 = new Subtask(3, TasksType.SUBTASK, "Sub Task3", TasksStatus.NEW,
                "Description sub task3", LocalDateTime.parse("03.08.2022 11:00", formatter), 60, 2);
        backedTasksManager.addTask(task1);
        backedTasksManager.addTask(task2);
        backedTasksManager.addTask(task3);

        FileBackedTasksManager fileBackedTasksManager;
        try {
            fileBackedTasksManager = new FileBackedTasksManager().loadFromFile(new File ("task_storage.csv"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<Task> list2 = fileBackedTasksManager.getHistory();
        assertNotNull(list2, "Список не возвращается.");
        assertEquals(0, list2.size(), "Неверное количество задач.");

        final Task savedTask1 = fileBackedTasksManager.getTask(1);
        final Task savedTask2 = fileBackedTasksManager.getTask(2);
        final Task savedTask3 = fileBackedTasksManager.getTask(3);

        assertNotNull(savedTask1, "Задача не найдена.");
        assertNotNull(savedTask2, "Задача не найдена.");
        assertNotNull(savedTask3, "Задача не найдена.");
        assertEquals(task1, savedTask1, "Задачи не совпадают.");
        assertEquals(task2, savedTask2, "Задачи не совпадают.");
        assertEquals(task3, savedTask3, "Задачи не совпадают.");

        final HashMap<Integer, Task> tasks = fileBackedTasksManager.getTaskList();

        assertNotNull(tasks, "Список не возвращается.");
        assertEquals(3, tasks.size(), "Неверное количество задач.");
        assertEquals(task1, tasks.get(1), "Задачи не совпадают.");
        assertEquals(task2, tasks.get(2), "Задачи не совпадают.");
        assertEquals(task3, tasks.get(3), "Задачи не совпадают.");

        final TreeSet<Task> list = fileBackedTasksManager.getPrioritizedTasks();
        assertNotNull(list, "Список не возвращается.");
        assertEquals(3, list.size(), "Неверное количество задач.");

    }

    @Test
    void addTaskTest() throws TaskValidationException {
        // одна новая задача без номера
        Task task = new Task(null, TasksType.TASK, "Задача 1", TasksStatus.NEW,
                "Текст задачи 1", LocalDateTime.parse("02.08.2022 11:00", formatter), 15, null);

        final int taskId = backedTasksManager.addTask(task);
        final Task savedTask = backedTasksManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final HashMap<Integer, Task> tasks = backedTasksManager.getTaskList();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(taskId), "Задачи не совпадают.");

        final TreeSet<Task> list1 = backedTasksManager.getPrioritizedTasks();
        assertEquals(1, list1.size(), "Неверное количество задач.");

        // задача с идентичными полями с уже существующим id
        Task task2 = new Task(taskId, TasksType.TASK, "Задача 1", TasksStatus.NEW,
                "Текст задачи 1", LocalDateTime.parse("02.08.2022 11:00", formatter), 15, null);

        final int taskId2 = backedTasksManager.addTask(task2);
        final Task savedTask2 = backedTasksManager.getTask(taskId2);

        assertNotNull(savedTask2, "Задача не найдена.");
        assertEquals(task2, savedTask2, "Задачи не совпадают.");
        assertEquals(savedTask, savedTask2, "Задачи не совпадают.");

        final HashMap<Integer, Task> tasks2 = backedTasksManager.getTaskList();

        assertNotNull(tasks2, "Задачи на возвращаются.");
        assertEquals(1, tasks2.size(), "Неверное количество задач.");
        assertEquals(task2, tasks2.get(taskId2), "Задачи не совпадают.");

        final TreeSet<Task> list2 = backedTasksManager.getPrioritizedTasks();
        assertEquals(1, list2.size(), "Неверное количество задач.");

        // задача с отличающимися полями с уже существующим id
        Task task3 = new Task(taskId, TasksType.TASK, "Задача 1", TasksStatus.NEW,
                "Текст задачи 1", LocalDateTime.parse("03.08.2022 11:00", formatter), 60, null);

        final int taskId3 = backedTasksManager.addTask(task3);
        final Task savedTask3 = backedTasksManager.getTask(taskId3);

        assertNotNull(savedTask3, "Задача не найдена.");
        assertEquals(task3, savedTask3, "Задачи не совпадают.");

        final HashMap<Integer, Task> tasks3 = backedTasksManager.getTaskList();

        assertNotNull(tasks3, "Задачи на возвращаются.");
        assertEquals(1, tasks3.size(), "Неверное количество задач.");
        assertEquals(task3, tasks3.get(taskId3), "Задачи не совпадают.");

        final TreeSet<Task> list3 = backedTasksManager.getPrioritizedTasks();
        assertEquals(1, list3.size(), "Неверное количество задач.");

        // новая задача с отличающимся startTime
        Task task4 = new Task(null, TasksType.TASK, "Задача 2", TasksStatus.NEW,
                "Текст задачи 2", LocalDateTime.parse("05.08.2022 11:00", formatter), 15, null);

        final int taskId4 = backedTasksManager.addTask(task4);
        final Task savedTask4 = backedTasksManager.getTask(taskId4);

        assertNotNull(savedTask4, "Задача не найдена.");
        assertEquals(task4, savedTask4, "Задачи не совпадают.");

        final HashMap<Integer, Task> tasks4 = backedTasksManager.getTaskList();

        assertNotNull(tasks4, "Задачи на возвращаются.");
        assertEquals(2, tasks4.size(), "Неверное количество задач.");
        assertEquals(task4, tasks4.get(taskId4), "Задачи не совпадают.");

        final TreeSet<Task> list4 = backedTasksManager.getPrioritizedTasks();
        assertEquals(2, list4.size(), "Неверное количество задач.");

        // новая задача с одинаковым startTime
        Task task5 = new Task(null, TasksType.TASK, "Задача 2", TasksStatus.NEW,
                "Текст задачи 1", LocalDateTime.parse("03.08.2022 11:00", formatter), 15, null);

        final int taskId5 = backedTasksManager.addTask(task5);
        final Task savedTask5 = backedTasksManager.getTask(taskId5);

        assertNull(savedTask5, "Задача добавлена.");

        final TreeSet<Task> list5 = backedTasksManager.getPrioritizedTasks();
        assertEquals(2, list5.size(), "Неверное количество задач.");

        // новая задача с отличающимся startTime, но пересекающимся endTime
        Task task6 = new Task(null, TasksType.TASK, "Задача 2", TasksStatus.NEW,
                "Текст задачи 1", LocalDateTime.parse("03.08.2022 10:50", formatter), 15, null);

        final int taskId6 = backedTasksManager.addTask(task6);
        final Task savedTask6 = backedTasksManager.getTask(taskId6);

        assertNull(savedTask6, "Задача добавлена.");

        final TreeSet<Task> list6 = backedTasksManager.getPrioritizedTasks();
        assertEquals(2, list6.size(), "Неверное количество задач.");

        // новая задача с отличающимся startTime и endTime = startTime следующей
        Task task7 = new Task(null, TasksType.TASK, "Задача 2", TasksStatus.NEW,
                "Текст задачи 1", LocalDateTime.parse("03.08.2022 10:45", formatter), 15, null);

        final int taskId7 = backedTasksManager.addTask(task7);
        final Task savedTask7 = backedTasksManager.getTask(taskId7);

        assertNotNull(savedTask7, "Задача не найдена.");
        assertEquals(task7, savedTask7, "Задачи не совпадают.");

        final HashMap<Integer, Task> tasks7 = backedTasksManager.getTaskList();

        assertNotNull(tasks7, "Задачи на возвращаются.");
        assertEquals(3, tasks7.size(), "Неверное количество задач.");
        assertEquals(task7, tasks7.get(taskId7), "Задачи не совпадают.");

        final TreeSet<Task> list7 = backedTasksManager.getPrioritizedTasks();
        assertEquals(3, list7.size(), "Неверное количество задач.");

        // проверка сохранения добавления задач в файл
        FileBackedTasksManager fileBackedTasksManager;
        try {
            fileBackedTasksManager = new FileBackedTasksManager().loadFromFile(new File ("task_storage.csv"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        final Task savedTask8 = fileBackedTasksManager.getTask(taskId );
        final Task savedTask9 = fileBackedTasksManager.getTask(taskId4);
        final Task savedTask10 = fileBackedTasksManager.getTask(taskId7);

        assertNotNull(savedTask8, "Задача не найдена.");
        assertNotNull(savedTask9, "Задача не найдена.");
        assertNotNull(savedTask10, "Задача не найдена.");
        assertEquals(savedTask3, savedTask8, "Задачи не совпадают.");
        assertEquals(savedTask4, savedTask9, "Задачи не совпадают.");
        assertEquals(savedTask7, savedTask10, "Задачи не совпадают.");

        final HashMap<Integer, Task> tasks8 = fileBackedTasksManager.getTaskList();

        assertNotNull(tasks8, "Задачи на возвращаются.");
        assertEquals(3, tasks8.size(), "Неверное количество задач.");
        assertEquals(savedTask3, tasks8.get(taskId3), "Задачи не совпадают.");
        assertEquals(savedTask4, tasks8.get(taskId4), "Задачи не совпадают.");
        assertEquals(savedTask7, tasks8.get(taskId7), "Задачи не совпадают.");

        final TreeSet<Task> list8 = fileBackedTasksManager.getPrioritizedTasks();
        assertEquals(3, list8.size(), "Неверное количество задач.");

        List<Task> list9 = fileBackedTasksManager.getHistory();
        assertEquals(3, list9.size(), "Неверное количество задач.");
        assertEquals(savedTask3, list9.get(0), "Задачи не совпадают.");
        assertEquals(savedTask4, list9.get(1), "Задачи не совпадают.");
        assertEquals(savedTask7, list9.get(2), "Задачи не совпадают.");
    }

    // Обновляем объекты
    @Test
    void updateTaskTest() throws TaskValidationException {
        Task task1 = new Task(null, TasksType.TASK, "Задача 1", TasksStatus.NEW,
                "Текст задачи 1", LocalDateTime.parse("02.08.2022 11:00", formatter), 15, null);
        final int taskId = backedTasksManager.addTask(task1);

        Task task2 = new Task(taskId, TasksType.TASK, "New Задача 1", TasksStatus.NEW,
                "Текст задачи 1", LocalDateTime.parse("03.08.2022 11:00", formatter), 60, null);
        backedTasksManager.updateTask(task2);

        final Task savedTask = backedTasksManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task2, savedTask, "Задачи не совпадают.");

        final HashMap<Integer, Task> tasks = backedTasksManager.getTaskList();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task2, tasks.get(taskId), "Задачи не совпадают.");

        final TreeSet<Task> list1 = backedTasksManager.getPrioritizedTasks();
        assertEquals(1, list1.size(), "Неверное количество задач.");

        // проверка сохранения добавления задач в файл
        FileBackedTasksManager fileBackedTasksManager;
        try {
            fileBackedTasksManager = new FileBackedTasksManager().loadFromFile(new File ("task_storage.csv"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        final Task savedTask2 = fileBackedTasksManager.getTask(taskId);

        assertNotNull(savedTask2, "Задача не найдена.");
        assertEquals(task2, savedTask2, "Задачи не совпадают.");

        final HashMap<Integer, Task> tasks2 = fileBackedTasksManager.getTaskList();

        assertNotNull(tasks2, "Задачи на возвращаются.");
        assertEquals(1, tasks2.size(), "Неверное количество задач.");
        assertEquals(savedTask2, tasks2.get(taskId), "Задачи не совпадают.");

        final TreeSet<Task> list2 = fileBackedTasksManager.getPrioritizedTasks();
        assertEquals(1, list2.size(), "Неверное количество задач.");

        List<Task> list3 = fileBackedTasksManager.getHistory();
        assertEquals(1, list3.size(), "Неверное количество задач.");
    }

    // Получаем список всех объектов
    @Test
    void getTaskListTest() throws TaskValidationException {
        // пустой список
        final HashMap<Integer, Task> list1 = backedTasksManager.getTaskList();

        assertNotNull(list1, "Cписок не возвращается.");
        assertEquals(0, list1.size(), "Неверное количество задач.");

        // непустой список
        Task task = new Task(null, TasksType.TASK, "Задача 1", TasksStatus.NEW,
                "Текст задачи 1", LocalDateTime.parse("02.08.2022 11:00", formatter), 15, null);
        backedTasksManager.addTask(task);

        final HashMap<Integer, Task> list2 = backedTasksManager.getTaskList();

        assertNotNull(list2, "Cписок не возвращается.");
        assertEquals(1, list2.size(), "Неверное количество задач.");
    }

    // Получаем список имен всех объектов
    @Test
    void getListAllTaskTest() throws TaskValidationException {
        // пустой список
        final ArrayList<String> list1 = backedTasksManager.getListAllTask();

        assertNotNull(list1, "Cписок не возвращается.");
        assertEquals(0, list1.size(), "Неверное количество задач.");

        // непустой список
        Task task = new Task(null, TasksType.TASK, "Задача 1", TasksStatus.NEW,
                "Текст задачи 1", LocalDateTime.parse("02.08.2022 11:00", formatter), 15, null);
        backedTasksManager.addTask(task);

        final ArrayList<String> list2 = backedTasksManager.getListAllTask();

        assertNotNull(list2, "Cписок не возвращается.");
        assertEquals(1, list2.size(), "Неверное количество задач.");
        assertEquals(task.getTaskName(), list2.get(0), "Задачи не совпадают.");
    }

    // Получаем объекты по id
    @Test
    void getTaskTest() throws TaskValidationException {
        // проверка существующего id
        Task task = new Task(null, TasksType.TASK, "Задача 1", TasksStatus.NEW,
                "Текст задачи 1", LocalDateTime.parse("02.08.2022 11:00", formatter), 15, null);

        final int taskId = backedTasksManager.addTask(task);
        final Task savedTask = backedTasksManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        // проверка несуществующего id
        final Task savedTask2 = backedTasksManager.getTask(4545);
        assertNull(savedTask2, "Вернулась несуществующая задача.");

        final Task savedTask3 = backedTasksManager.getTask(0);
        assertNull(savedTask3, "Вернулась несуществующая задача.");

        List<Task> list3 = backedTasksManager.getHistory();
        assertEquals(1, list3.size(), "Неверное количество задач.");
    }

    // Удаляем объекты по id
    @Test
    void removeTaskTest() throws TaskValidationException {
        // проверка существующего id
        Task task = new Task(null, TasksType.TASK, "Задача 1", TasksStatus.NEW,
                "Текст задачи 1", LocalDateTime.parse("02.08.2022 11:00", formatter), 15, null);

        final int taskId = backedTasksManager.addTask(task);
        final HashMap<Integer, Task> list1 = backedTasksManager.getTaskList();
        assertEquals(1, list1.size(), "Неверное количество задач.");

        final TreeSet<Task> list7 = backedTasksManager.getPrioritizedTasks();
        assertEquals(1, list7.size(), "Неверное количество задач.");

        backedTasksManager.removeTask(taskId);
        final HashMap<Integer, Task> list2 = backedTasksManager.getTaskList();
        assertEquals(0, list2.size(), "Задача не удалена.");

        final TreeSet<Task> list5 = backedTasksManager.getPrioritizedTasks();
        assertEquals(0, list5.size(), "Задача не удалена.");

        // проверка несуществующего id
        backedTasksManager.addTask(task);
        backedTasksManager.removeTask(4545);
        final HashMap<Integer, Task> list3 = backedTasksManager.getTaskList();
        assertEquals(1, list3.size(), "Удалена несуществующая задача.");

        backedTasksManager.removeTask(0);
        final HashMap<Integer, Task> list4 = backedTasksManager.getTaskList();
        assertEquals(1, list4.size(), "Удалена несуществующая задача.");

        final TreeSet<Task> list6 = backedTasksManager.getPrioritizedTasks();
        assertEquals(1, list6.size(), "Задача не удалена.");
    }

    // Удаляем все объекты списка
    @Test
    void removeAllTaskTest() throws TaskValidationException {
        Task task = new Epic(null, TasksType.EPIC, "Задача 1", TasksStatus.NEW,
                "Текст задачи 1", LocalDateTime.parse("02.08.2022 11:00", formatter), 15, null);
        int i = backedTasksManager.addTask(task);
        Subtask task2 = new Subtask(null, TasksType.SUBTASK, "Задача 1", TasksStatus.NEW,
                "Текст задачи 1", LocalDateTime.parse("03.08.2022 11:00", formatter), 15, i);
        backedTasksManager.addTask(task2);

        final HashMap<Integer, Task> list1 = backedTasksManager.getTaskList();
        assertEquals(2, list1.size(), "Неверное количество задач.");

        backedTasksManager.removeAllTask();
        final HashMap<Integer, Task> list2 = backedTasksManager.getTaskList();
        assertEquals(0, list2.size(), "Неверное количество задач.");

        backedTasksManager.removeAllTask();
        final HashMap<Integer, Task> list3 = backedTasksManager.getTaskList();
        assertEquals(0, list3.size(), "Неверное количество задач.");

        final TreeSet<Task> list5 = backedTasksManager.getPrioritizedTasks();
        assertEquals(0, list5.size(), "Задача не удалена.");
    }

    // Получаем список подзадач эпика
    @Test
    void getSubtaskIdTest() throws TaskValidationException {
        // существующий id эпика
        Epic epic = new Epic(null, TasksType.EPIC,"Эпик 1", TasksStatus.NEW,
                "Текст эпика 1",null, null, null);
        final int taskId0 = backedTasksManager.addTask(epic);

        Subtask task1 = new Subtask(null, TasksType.SUBTASK,"Сабтаск к эпику 1",
                TasksStatus.NEW,"Текст сабтаска", LocalDateTime.parse("02.08.2022 11:00", formatter), 30,taskId0);
        final int taskId1 = backedTasksManager.addTask(task1);

        Subtask task2 = new Subtask(null, TasksType.SUBTASK,"New Сабтаск к эпику 1",
                TasksStatus.NEW,"Текст сабтаска", LocalDateTime.parse("03.08.2022 11:00", formatter), 60,taskId0);
        final int taskId2 = backedTasksManager.addTask(task2);

        final ArrayList<Integer> list = backedTasksManager.getSubtaskId(taskId0);

        assertNotNull(list, "Задачи не найдены.");
        assertEquals(2, list.size(), "Неверное количество задач.");
        assertEquals(taskId1, list.get(0), "Неверный id сабтаска.");
        assertEquals(taskId2, list.get(1), "Неверный id сабтаска.");

        // несуществующий id эпика
        final ArrayList<Integer> list1 = backedTasksManager.getSubtaskId(4545);

        assertNotNull(list1, "Не возвращается пустой список.");
        assertEquals(0, list1.size(), "Неверное количество задач.");

        final ArrayList<Integer> list2 = backedTasksManager.getSubtaskId(0);

        assertNotNull(list2, "Не возвращается пустой список.");
        assertEquals(0, list2.size(), "Неверное количество задач.");
    }

    // Рассчитываем статус эпика по статусам сабтасков
    @Test
    void updateEpicStatusTest() throws TaskValidationException {
        // нет сабтасков
        Epic epic = new Epic(null, TasksType.EPIC,"Эпик 1", TasksStatus.NEW,
                "Текст эпика 1",null, null, null);
        final int taskId = backedTasksManager.addTask(epic);

        final TasksStatus epicStatus = backedTasksManager.getTask(taskId).getTaskStatus();

        assertNotNull(epic.getTaskStatus(), "Статус эпика null.");
        assertEquals(TasksStatus.NEW, epicStatus, "Статус не совпадает.");

        // Все подзадачи со статусом NEW
        Subtask task1 = new Subtask(null, TasksType.SUBTASK,"Сабтаск к эпику 1",
                TasksStatus.NEW,"Текст сабтаска", LocalDateTime.parse("02.08.2022 11:00", formatter), 30,taskId);
        final int t1 = backedTasksManager.addTask(task1);

        Subtask task2 = new Subtask(null, TasksType.SUBTASK,"New Сабтаск к эпику 1",
                TasksStatus.NEW,"Текст сабтаска", LocalDateTime.parse("03.08.2022 11:00", formatter), 60,taskId);
        final int t2 = backedTasksManager.addTask(task2);
        backedTasksManager.updateEpicStatus(taskId);

        final TasksStatus epicStatus2 = backedTasksManager.getTask(taskId).getTaskStatus();

        assertNotNull(epic.getTaskStatus(), "Статус эпика null.");
        assertEquals(TasksStatus.NEW, epicStatus2, "Статус не совпадает.");

        // Подзадачи со статусом IN_PROGRESS
        task1 = new Subtask(t1, TasksType.SUBTASK,"Сабтаск к эпику 1",
                TasksStatus.NEW,"Текст сабтаска", LocalDateTime.parse("02.08.2022 11:00", formatter), 30,taskId);
        backedTasksManager.updateTask(task1);

        task2 = new Subtask(t2, TasksType.SUBTASK,"New Сабтаск к эпику 1",
                TasksStatus.IN_PROGRESS,"Текст сабтаска", LocalDateTime.parse("03.08.2022 11:00", formatter), 60,taskId);
        backedTasksManager.updateTask(task2);
        backedTasksManager.updateEpicStatus(taskId);

        final TasksStatus epicStatus3 = backedTasksManager.getTask(taskId).getTaskStatus();

        assertNotNull(epic.getTaskStatus(), "Статус эпика null.");
        assertEquals(TasksStatus.IN_PROGRESS, epicStatus3, "Статус не совпадает.");

        // Подзадачи со статусами NEW и DONE
        task1 = new Subtask(t1, TasksType.SUBTASK,"Сабтаск к эпику 1",
                TasksStatus.NEW,"Текст сабтаска", LocalDateTime.parse("02.08.2022 11:00", formatter), 30,taskId);
        backedTasksManager.updateTask(task1);

        task2 = new Subtask(t2, TasksType.SUBTASK,"New Сабтаск к эпику 1",
                TasksStatus.DONE,"Текст сабтаска", LocalDateTime.parse("03.08.2022 11:00", formatter), 60,taskId);
        backedTasksManager.updateTask(task2);
        backedTasksManager.updateEpicStatus(taskId);

        final TasksStatus epicStatus4 = backedTasksManager.getTask(taskId).getTaskStatus();

        assertNotNull(epic.getTaskStatus(), "Статус эпика null.");
        assertEquals(TasksStatus.IN_PROGRESS, epicStatus4, "Статус не совпадает.");

        // Все подзадачи со статусом DONE
        task1 = new Subtask(t1, TasksType.SUBTASK,"Сабтаск к эпику 1",
                TasksStatus.DONE,"Текст сабтаска", LocalDateTime.parse("02.08.2022 11:00", formatter), 30,taskId);
        backedTasksManager.updateTask(task1);

        task2 = new Subtask(t2, TasksType.SUBTASK,"New Сабтаск к эпику 1",
                TasksStatus.DONE,"Текст сабтаска", LocalDateTime.parse("03.08.2022 11:00", formatter), 60,taskId);
        backedTasksManager.updateTask(task2);
        backedTasksManager.updateEpicStatus(taskId);

        final TasksStatus epicStatus5 = backedTasksManager.getTask(taskId).getTaskStatus();

        assertNotNull(epic.getTaskStatus(), "Статус эпика null.");
        assertEquals(TasksStatus.DONE, epicStatus5, "Статус не совпадает.");

    }

    // Расчет startTime эпика по startTime сабтасков
    @Test
    void updateEpicStartTimeTest() throws TaskValidationException {
        Epic epic = new Epic(null, TasksType.EPIC,"Эпик 1", TasksStatus.NEW,
                "Текст эпика 1",null, null, null);
        final int taskId = backedTasksManager.addTask(epic);

        assertNull(epic.getStartTime(), "StartTime эпика не null.");

        // StartTime сабтаска null
        Subtask task0 = new Subtask(null, TasksType.SUBTASK,"Сабтаск к эпику 1",
                TasksStatus.DONE,"Текст сабтаска", null, null,taskId);
        final int i = backedTasksManager.addTask(task0);
        backedTasksManager.updateEpicStartTime(taskId);

        assertNull(epic.getStartTime(), "StartTime эпика не null.");

        // StartTime сабтаска не null
        Subtask task1 = new Subtask(i, TasksType.SUBTASK,"Сабтаск к эпику 1",
                TasksStatus.DONE,"Текст сабтаска", LocalDateTime.parse("02.08.2022 11:00", formatter), 30,taskId);
        Subtask task2 = new Subtask(null, TasksType.SUBTASK,"New Сабтаск к эпику 1",
                TasksStatus.DONE,"Текст сабтаска", LocalDateTime.parse("03.08.2022 11:00", formatter), 60,taskId);

        backedTasksManager.updateTask(task1);
        backedTasksManager.addTask(task2);
        backedTasksManager.updateEpicStartTime(taskId);

        final LocalDateTime epicStartTime = backedTasksManager.getTask(taskId).getStartTime();

        assertNotNull(epic.getStartTime(), "StartTime эпика null.");
        assertEquals(backedTasksManager.getTask(i).getStartTime(), epicStartTime,
                "StartTime эпика и сабтаска не совпадает.");
    }

    // Апдейт endTime эпика по endTime сабтасков
    @Test
    void updateEpicEndTimeTest() throws TaskValidationException {
        Epic epic = new Epic(1, TasksType.EPIC,"Эпик 1", TasksStatus.NEW,
                "Текст эпика 1",null, null, null);
        final int taskId = backedTasksManager.addTask(epic);

        assertNull(epic.getEpicEndTime(), "EndTime эпика не null.");

        Subtask task1 = new Subtask(2, TasksType.SUBTASK,"Сабтаск к эпику 1",
                TasksStatus.DONE,"Текст сабтаска", LocalDateTime.parse("02.08.2022 11:00", formatter), 30,taskId);
        Subtask task2 = new Subtask(3, TasksType.SUBTASK,"New Сабтаск к эпику 1",
                TasksStatus.DONE,"Текст сабтаска", LocalDateTime.parse("03.08.2022 11:00", formatter), 60,taskId);

        final int taskId1 = backedTasksManager.addTask(task1);
        final int taskId2 = backedTasksManager.addTask(task2);
        backedTasksManager.updateEpicEndTime(taskId);

        final LocalDateTime epicEndTime = backedTasksManager.getTask(taskId).getEpicEndTime();

        assertNotNull(epic.getEpicEndTime(), "EndTime эпика null.");
        assertEquals(backedTasksManager.getTask(taskId2).getEndTime(), epicEndTime,
                "EndTime эпика и сабтаска не совпадает.");
    }

    // Апдейт duration эпика по сумме duration сабтасков
    @Test
    void updateEpicDurationTest() throws TaskValidationException {
        Epic epic = new Epic(null, TasksType.EPIC,"Эпик 1", TasksStatus.NEW,
                "Текст эпика 1",null, null, null);
        final int taskId = backedTasksManager.addTask(epic);

        assertNull(epic.getDuration(), "Duration эпика не null.");
        assertNull(backedTasksManager.getTask(taskId).getDuration(), "Duration не совпадает.");

        Subtask task1 = new Subtask(null, TasksType.SUBTASK,"Сабтаск к эпику 1",
                TasksStatus.DONE,"Текст сабтаска", LocalDateTime.parse("02.08.2022 11:00", formatter), 30,taskId);
        Subtask task2 = new Subtask(null, TasksType.SUBTASK,"New Сабтаск к эпику 1",
                TasksStatus.DONE,"Текст сабтаска", LocalDateTime.parse("03.08.2022 11:00", formatter), 60,taskId);

        backedTasksManager.addTask(task1);
        backedTasksManager.addTask(task2);
        backedTasksManager.updateEpicDuration(taskId);

        assertNotNull(epic.getDuration(), "Duration эпика null.");
        assertEquals(90, backedTasksManager.getTask(taskId).getDuration(), "Duration не совпадает.");

    }

    // Апдейт всех элементов эпика зависящих от сабтасков
    @Test
    void updateEpicElementsTest() throws TaskValidationException {
        Epic epic = new Epic(null, TasksType.EPIC,"Эпик 1", TasksStatus.NEW,
                "Текст эпика 1",null, null, null);
        final int taskId = backedTasksManager.addTask(epic);

        Subtask task1 = new Subtask(null, TasksType.SUBTASK,"Сабтаск к эпику 1",
                TasksStatus.DONE,"Текст сабтаска", LocalDateTime.parse("02.08.2022 11:00", formatter), 30,taskId);
        Subtask task2 = new Subtask(null, TasksType.SUBTASK,"New Сабтаск к эпику 1",
                TasksStatus.DONE,"Текст сабтаска", LocalDateTime.parse("03.08.2022 11:00", formatter), 60,taskId);

        final int i1 = backedTasksManager.addTask(task1);
        final int i2 = backedTasksManager.addTask(task2);
        backedTasksManager.updateEpicElements(taskId);

        assertNotNull(epic.getTaskStatus(), "Статус эпика null.");
        assertEquals(TasksStatus.DONE, backedTasksManager.getTask(taskId).getTaskStatus(), "Статус не совпадает.");

        assertNotNull(epic.getStartTime(), "StartTime эпика null.");
        assertEquals(backedTasksManager.getTask(i1).getStartTime(), backedTasksManager.getTask(taskId).getStartTime(),
                "StartTime эпика и сабтаска не совпадает.");

        assertNotNull(epic.getEpicEndTime(), "EndTime эпика null.");
        assertEquals(backedTasksManager.getTask(i2).getEndTime(), backedTasksManager.getTask(taskId).getEpicEndTime(),
                "EndTime эпика и сабтаска не совпадает.");

        assertNotNull(epic.getDuration(), "Duration эпика null.");
        assertEquals(90, backedTasksManager.getTask(taskId).getDuration(), "Duration не совпадает.");

    }

    @Test
    void getPrioritizedTasks() throws TaskValidationException {
        Task task = new Task(null, TasksType.TASK, "Задача 1", TasksStatus.NEW,
                "Текст задачи 1", LocalDateTime.parse("02.08.2022 11:00", formatter), 15, null);
        final int taskId = backedTasksManager.addTask(task);

        final HashMap<Integer, Task> tasks = backedTasksManager.getTaskList();

        final TreeSet<Task> list1 = backedTasksManager.getPrioritizedTasks();
        assertEquals(1, list1.size(), "Неверное количество задач.");

    }

    @Test
    void getHistory() throws TaskValidationException {
        // проверка существующего id
        backedTasksManager.removeAllTask();
        Task task = new Task(null, TasksType.TASK, "Задача 1", TasksStatus.NEW,
                "Текст задачи 1", LocalDateTime.parse("02.08.2022 11:00", formatter), 15, null);
        final int taskId = backedTasksManager.addTask(task);
        backedTasksManager.getTask(taskId);

        List<Task> list1 = backedTasksManager.getHistory();
        assertEquals(1, list1.size(), "Неверное количество задач.");

        // проверка несуществующего id
        final Task savedTask2 = backedTasksManager.getTask(4545);
        assertNull(savedTask2, "Вернулась несуществующая задача.");

        final Task savedTask3 = backedTasksManager.getTask(0);
        assertNull(savedTask3, "Вернулась несуществующая задача.");

        List<Task> list3 = backedTasksManager.getHistory();
        assertEquals(1, list3.size(), "Неверное количество задач.");

    }
}
