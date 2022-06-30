import Tasks.Task;
import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    ArrayList<Task> history = new ArrayList<>();

    // Получаем историю просмотров
    @Override
    public ArrayList<Task> getHistory(){
        return history;
    }

    // Записываем историю просмотров
    @Override
    public void addHistory(Task task){
        if(history.size() < 10) {
            history.add(task);
        } else {
            history.remove(0);
            history.add(task);
        }
    }

    @Override
    public void printHistory() {
        ArrayList<String> listAllTask = new ArrayList<>();
        for (Task task : history) {
            listAllTask.add(task.getTaskName());
        }
        System.out.println("Список просмотров: " + listAllTask + "\n");
    }
}
