import Tasks.Task;
import Tasks.Epic;
import Tasks.Subtask;
import Tasks.TasksStatus;

public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        // Создаем новые объекты
        taskManager.createNewTask(new Task("Задача 1", "Текст задачи 1", TasksStatus.NEW,
                taskManager.setTaskId()));
        taskManager.createNewEpic(new Epic("Эпик 2", "Текст эпика 2",
                TasksStatus.NEW, taskManager.setTaskId()));
        taskManager.createNewSubtask(new Subtask("Сабтаск 3 к эпику 2", "Текст сабтаска 3",
                TasksStatus.NEW, taskManager.setTaskId(), 2));
        taskManager.createNewTask(new Task("Задача 4", "Текст задачи 4", TasksStatus.NEW,
                taskManager.setTaskId()));
        taskManager.createNewEpic(new Epic("Эпик 5", "Текст эпика 5",
                TasksStatus.NEW, taskManager.setTaskId()));
        taskManager.createNewSubtask(new Subtask("Сабтаск 6 к эпику 2", "Текст сабтаска 6",
                TasksStatus.NEW, taskManager.setTaskId(), 2));
        taskManager.createNewSubtask(new Subtask("Сабтаск 7 к эпику 2", "Текст сабтаска 7",
                TasksStatus.NEW, taskManager.setTaskId(), 2));
        taskManager.createNewSubtask(new Subtask("Сабтаск 8 к эпику 5", "Текст сабтаска 8",
                TasksStatus.NEW, taskManager.setTaskId(), 5));
        taskManager.createNewSubtask(new Subtask("Сабтаск 9 к эпику 5", "Текст сабтаска 9",
                TasksStatus.NEW, taskManager.setTaskId(), 5));
        taskManager.createNewTask(new Task("Задача 10", "Текст задачи", TasksStatus.NEW,
                taskManager.setTaskId()));
        taskManager.createNewEpic(new Epic("Эпик 11", "Текст эпика",
                TasksStatus.NEW, taskManager.setTaskId()));
        taskManager.createNewSubtask(new Subtask("Сабтаск 12", "Текст сабтаска",
                TasksStatus.NEW, taskManager.setTaskId(), 5));

        // Получаем объект по id и проверяем список просмотров
        System.out.println("Вызываем задачи 1-5");
        taskManager.getTask(1);
        taskManager.getEpic(2);
        taskManager.getSubtask(3);
        taskManager.getTask(4);
        taskManager.getEpic(5);
        System.out.println("Печатаем список");
        Managers.getDefaultHistory().printHistory();

        System.out.println("Вызываем задачи 6-9");
        taskManager.getSubtask(6);
        taskManager.getSubtask(7);
        taskManager.getSubtask(8);
        taskManager.getSubtask(9);
        System.out.println("Печатаем список");
        Managers.getDefaultHistory().printHistory();

        System.out.println("Вызываем задачу 10");
        taskManager.getTask(10);
        System.out.println("Печатаем список");
        Managers.getDefaultHistory().printHistory();

        System.out.println("Вызываем задачу 11");
        taskManager.getEpic(11);
        System.out.println("Печатаем список");
        Managers.getDefaultHistory().printHistory();
        
        System.out.println("Вызываем задачу 12");
        taskManager.getSubtask(12);
        System.out.println("Печатаем список");
        Managers.getDefaultHistory().printHistory();

        System.out.println("Печатаем полный список через getHistory() " + Managers.getDefaultHistory().getHistory());

    }

}
