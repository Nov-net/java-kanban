package tasksManager.Managers;

import tasksManager.Tasks.*;
import java.util.List;

public interface HistoryManager {
    void addHistory(Task task);
    void removeHistory(int id);
    List<Task> getHistory();

    void removeALLHistory();
}
