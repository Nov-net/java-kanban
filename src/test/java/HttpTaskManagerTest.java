import org.junit.jupiter.api.*;
import tasksManager.Managers.ManagerSaveExeption;
import tasksManager.Tasks.*;
import tasksManager.http.HttpTaskManager;
import tasksManager.http.HttpTaskServer;
import tasksManager.server.KVServer;

import java.io.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTaskManagerTest extends FileBackedTasksManagerTest {
/*    static KVServer kvServer;

    static {
        try {
            kvServer = new KVServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static HttpTaskServer httpTaskServer;

    static {
        try {
            httpTaskServer = new HttpTaskServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ManagerSaveExeption e) {
            throw new RuntimeException(e);
        }
    }

    static HttpTaskManager taskManager;
    static int idTask;
    static int idSubask;
    static int idEpic;

    public HttpTaskManagerTest() {
    }

    @BeforeAll
    public static void beforeAll() throws IOException, ManagerSaveExeption {
        kvServer.start();
        httpTaskServer.start();
        taskManager = new HttpTaskManager(KVServer.PORT, false);
        idTask = taskManager.addTask(new Task(1, TasksType.TASK, "Task1", TasksStatus.NEW,
                "Description task1", "01.08.2022 10:00", 15, null));
        idEpic = taskManager.addTask(new Epic(2, TasksType.EPIC, "Epic2", TasksStatus.NEW,
                "Description epic2", "02.08.2022 11:00", 60, null));;
        idSubask = taskManager.addTask(new Subtask(3, TasksType.SUBTASK, "Subtask3", TasksStatus.NEW,
                "Description subtask3", "03.08.2022 15:00", 120, 2));
        taskManager.getTask(idTask);
    }

    @AfterAll
    public void stop() {
        kvServer.stop();
        httpTaskServer.stop();
    }

    @Test
    void loadTest() throws ManagerSaveExeption, IOException {
        HttpTaskManager taskManager2 = new HttpTaskManager(KVServer.PORT, true);
        final List<String> tasks = taskManager2.getListAllTask();
        assertNotNull(tasks, "Возвращает пустой список");
        assertEquals(3, tasks.size(), "Не соответствует количество задач");
        final List<Task> history = taskManager2.getHistory();
        assertNotNull(tasks, "Возвращает пустой список");
        assertEquals(1, tasks.size(), "Не соответствует количество задач");
    }

    @Test
    void saveTest() throws ManagerSaveExeption {
        int id = taskManager.addTask(new Task(4, TasksType.TASK, "Task4", TasksStatus.NEW,
                "Description task4", "01.09.2022 10:00", 15, null));
        taskManager.getTask(id);

        HttpTaskManager taskManager2 = new HttpTaskManager(KVServer.PORT, true);
        final List<String> tasks = taskManager2.getListAllTask();
        assertNotNull(tasks, "Возвращает пустой список");
        assertEquals(4, tasks.size(), "Не соответствует количество задач");
        final List<Task> history = taskManager2.getHistory();
        assertNotNull(tasks, "Возвращает пустой список");
        assertEquals(2, tasks.size(), "Не соответствует количество задач");
    }*/


}
