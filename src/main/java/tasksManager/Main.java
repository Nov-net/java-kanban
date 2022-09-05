package tasksManager;

import tasksManager.Managers.ManagerSaveExeption;
import tasksManager.Tasks.*;
import tasksManager.Managers.InMemoryTasksManager;
import tasksManager.http.HttpTaskServer;
import tasksManager.server.KVServer;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws ManagerSaveExeption, IOException {
        new KVServer().start();
        new HttpTaskServer().start();

    }

}
