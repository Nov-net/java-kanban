package tasksManager.Managers;

public abstract class Managers {

    public TaskManager getDefault() {
        return new InMemoryTasksManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public TaskManager getDefaultFileBackedTasksManager() {
        return new FileBackedTasksManager();
    }

}
