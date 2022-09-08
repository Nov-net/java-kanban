package tasksManager;

import tasksManager.Managers.ManagerSaveExeption;
import tasksManager.Managers.http.HttpTaskServer;
import tasksManager.Managers.server.KVServer;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws ManagerSaveExeption, IOException {
        new KVServer().start();
        new HttpTaskServer().start();

    }

}
