package history;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasksManager.Managers.history.HistoryManager;
import tasksManager.Managers.Managers;
import tasksManager.Tasks.Task;
import tasksManager.Tasks.TasksStatus;
import tasksManager.Tasks.TasksType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {

    private static HistoryManager historyManager;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @BeforeEach
    public void beforeEach() {
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    void addHistoryTest() {
        Task task = new Task(1, TasksType.TASK, "Задача 1", TasksStatus.NEW,
                "Текст задачи 1", LocalDateTime.parse("02.08.2022 11:00", formatter), 15, null);
        historyManager.addHistory(task);
        final List<Task> history = historyManager.getHistory();

        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
        assertEquals(task, history.get(0), "Задачи не совпадают.");
    }

    @Test
    void removeHistoryTest() {
        Task task = new Task(1, TasksType.TASK, "Задача 1", TasksStatus.NEW,
                "Текст задачи 1", LocalDateTime.parse("02.08.2022 11:00", formatter), 15, null);
        Task task2 = new Task(2, TasksType.TASK, "Задача 2", TasksStatus.NEW,
                "Текст задачи 2", LocalDateTime.parse("02.08.2022 11:00", formatter), 30, null);
        historyManager.addHistory(task);
        historyManager.addHistory(task2);

        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(2, history.size(), "История не пустая.");
        assertEquals(task, history.get(0), "Задачи не совпадают.");
        assertEquals(task2, history.get(1), "Задачи не совпадают.");

        historyManager.removeHistory(1);

        final List<Task> history1 = historyManager.getHistory();
        assertNotNull(history1, "История не пустая.");
        assertEquals(1, history1.size(), "История не пустая.");

        historyManager.removeHistory(2);

        final List<Task> history2 = historyManager.getHistory();
        assertNotNull(history2, "История не пустая.");
        assertEquals(0, history2.size(), "История не пустая.");
    }

    @Test
    void getHistoryTest() {
        final List<Task> history = historyManager.getHistory();

        assertNotNull(history, "История не возвращается.");

        Task task = new Task(1, TasksType.TASK, "Задача 1", TasksStatus.NEW,
                "Текст задачи 1", LocalDateTime.parse("02.08.2022 11:00", formatter), 15, null);
        historyManager.addHistory(task);
        final List<Task> history1 = historyManager.getHistory();

        assertNotNull(history1, "История не пустая.");
        assertEquals(1, history1.size(), "История не пустая.");
        assertEquals(task, history1.get(0), "Задачи не совпадают.");
    }
}
