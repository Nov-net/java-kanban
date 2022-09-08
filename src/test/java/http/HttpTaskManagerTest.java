package http;

import file.FileBackedTasksManagerTest;
import org.junit.jupiter.api.*;
import tasksManager.Managers.ManagerSaveExeption;
import tasksManager.Tasks.*;
import tasksManager.Managers.http.HttpTaskServer;
import tasksManager.Managers.server.KVServer;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HttpTaskManagerTest extends FileBackedTasksManagerTest {

    static KVServer kvServer;

    static HttpTaskServer httpTaskServer;

    static TestHttpClient client;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @BeforeAll
    static void beforeAll() throws IOException, ManagerSaveExeption {
        kvServer = new KVServer();
        kvServer.start();
        httpTaskServer= new HttpTaskServer();
        httpTaskServer.start();
        client = new TestHttpClient(HttpTaskServer.PORT);
    }

    @AfterAll
    static void afterAll() {
        kvServer.stop();
        httpTaskServer.stop();
    }

    @Test
    public void addTask(){
        Task task = new Task(123, TasksType.TASK, "Задача 1", TasksStatus.NEW,
                "Текст задачи 1", LocalDateTime.parse("02.08.2022 11:00", formatter), 15, null);
        System.out.println("storing task: " + task);
        client.addTask(task);
        Task task1 = client.getTask(123);
        System.out.println("store and request the task, task: " + task1);
        System.out.println("success: " + task.equals(task1));

    }

}
