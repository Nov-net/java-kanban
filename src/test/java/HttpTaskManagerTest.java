import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.*;
import tasksManager.Managers.FileBackedTasksManager;
import tasksManager.Managers.ManagerSaveExeption;
import tasksManager.Managers.Managers;
import tasksManager.Tasks.Task;
import tasksManager.http.HttpTaskManager;
import tasksManager.http.HttpTaskServer;
import tasksManager.http.KVTaskClient;
import tasksManager.server.KVServer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTaskManagerTest extends FileBackedTasksManagerTest {
    KVServer kvServer = new KVServer();
    HttpTaskServer httpTaskServer = new HttpTaskServer();

    public HttpTaskManagerTest() throws IOException, ManagerSaveExeption {
    }

    @BeforeAll
    public void beforeAll() throws IOException, ManagerSaveExeption {
        kvServer.start();
        httpTaskServer.start();
    }

    @AfterAll
    public void remove() {
        kvServer.stop();
        httpTaskServer.stop();
    }

/*    protected void addTasks(ArrayList<Task> tasks) {
        if (tasks != null) {
            for (Task task : tasks) {
                addTask(task);
            }
        }
    }*/

    @Test
    private void loadTest() throws ManagerSaveExeption, IOException {
        //new HttpTaskServer().start();
        HttpTaskManager taskManager = new HttpTaskManager(KVServer.PORT, true);
        final List<String> tasks = taskManager.getListAllTask();
        assertNotNull(tasks, "Возвращает не пустой список");
        assertEquals(1, tasks.size(), "Возвращает не пустой список");
    }

    /*@Override
    protected void save() {
        try {
            client.put("tasks", gson.toJson(new ArrayList<Task>(taskList.values())));
            client.put("history", gson.toJson(historyToString(historyManager)));
        } catch (ManagerSaveExeption e) {
            throw new RuntimeException(e);
        }
    }*/
}
