package Managers;

public abstract class Managers {

    public TaskManager getDefault() {
        return new InMemoryTasksManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    // Ростислав, привет! По твоей рекомендации добавила стандартную реализацию для нового класса
    public TaskManager getDefaultFileBackedTasksManager() {
        return new FileBackedTasksManager();
    }

}
