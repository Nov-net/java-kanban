package Managers; // сделала отдельный пакет для всех менеджеров

import Tasks.Task;
import java.util.List;

public interface HistoryManager {
    void addHistory(Task task);
    List<Task> getHistory(); // изменила на List
    void printHistory();
}
