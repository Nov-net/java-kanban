public abstract class Managers {

    public TaskManager taskManager;

    public TaskManager getDefault() {
        return taskManager;
    }

    static HistoryManager historyManager = new InMemoryHistoryManager();
    public static HistoryManager getDefaultHistory() {
        return historyManager;
    }

}
