package tasksManager.Managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import tasksManager.http.HttpTaskManager;
import tasksManager.http.HttpTaskServer;
import tasksManager.server.KVServer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class Managers {

    public TaskManager getDefaultInMemoryTasksManager() {
        return new InMemoryTasksManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public TaskManager getDefaultFileBackedTasksManager() {
        return new FileBackedTasksManager();
    }

    public static TaskManager getDefault() throws ManagerSaveExeption {
        return new HttpTaskManager(KVServer.PORT);
    }

    public static KVServer getDefaultKVServer() throws IOException {
        final KVServer kvServer = new KVServer();
        kvServer.start();
        return kvServer;
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new HttpTaskServer.LocalDateTimeAdapter());
        return gsonBuilder.create();
    }

}
