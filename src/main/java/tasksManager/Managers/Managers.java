package tasksManager.Managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import tasksManager.Managers.file.FileBackedTasksManager;
import tasksManager.Managers.history.HistoryManager;
import tasksManager.Managers.history.InMemoryHistoryManager;
import tasksManager.Managers.http.HttpTaskManager;
import tasksManager.Managers.adapter.LocalDateTimeSerializer;
import tasksManager.Managers.memory.InMemoryTasksManager;
import tasksManager.Managers.server.KVServer;

import java.io.IOException;
import java.time.LocalDateTime;

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
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.serializeNulls();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        return gsonBuilder.create();
    }

}
