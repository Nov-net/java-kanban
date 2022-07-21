package Managers;

import Tasks.Task;
import java.util.List;

public interface HistoryManager {
    void addHistory(Task task);
    void linkLast(Task task);
    void removeHistory(int id);
    List<Task> getHistory();
    void printHistory();
    void printNodeMap();
}
