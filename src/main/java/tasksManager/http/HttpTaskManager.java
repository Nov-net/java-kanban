package tasksManager.http;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import tasksManager.Managers.FileBackedTasksManager;
import tasksManager.Managers.ManagerSaveExeption;
import tasksManager.Managers.Managers;
import tasksManager.Tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {
    private final Gson gson;
    private final KVTaskClient client;

    public HttpTaskManager(int port) throws ManagerSaveExeption {
        this(port, false);
    }

    public HttpTaskManager(int port, boolean load) throws ManagerSaveExeption {
        super(null);
        gson = Managers.getGson();
        client = new KVTaskClient(port);
        if(load) {
            load();
        }
    }

    protected void addTasks(ArrayList<Task> tasks) {
        if (tasks != null) {
            for (Task task : tasks) {
                addTask(task);
            }
        }
    }

    private void load() throws ManagerSaveExeption {
        ArrayList<Task> tasks = gson.fromJson(client.load("tasks"), new TypeToken<ArrayList<Task>>() {}.getType());
        addTasks(tasks);

        List<Integer> history = gson.fromJson(client.load("history"), new TypeToken<List<Integer>>() {}.getType());
        if (history != null) {
            recoveryHistory(history);
        }
    }

    @Override
    protected void save() {
        try {
            client.put("tasks", gson.toJson(new ArrayList<Task>(taskList.values())));
            client.put("history", gson.toJson(historyToString(historyManager)));
        } catch (ManagerSaveExeption e) {
            throw new RuntimeException(e);
        }
    }

}
