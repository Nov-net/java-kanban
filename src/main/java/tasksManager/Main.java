package tasksManager;

import tasksManager.Tasks.*;
import tasksManager.Managers.InMemoryTasksManager;

public class Main {

    public static void main(String[] args) {
        InMemoryTasksManager taskManager = new InMemoryTasksManager();

        // Создаем новые объекты
        taskManager.addTask(new Task(null, TasksType.TASK, "Задача 1", TasksStatus.NEW,
                "Текст задачи 1", "01.09.2022 10:00", 15, null));
        taskManager.addEpic(new Epic(null, TasksType.EPIC,"Эпик 2", TasksStatus.NEW,
                "Текст эпика 2",null, null, null));
        taskManager.addSubtask(new Subtask(null, TasksType.SUBTASK,"Сабтаск 3 к эпику 2",
                TasksStatus.NEW,"Текст сабтаска 3", "01.09.2022 11:00", 30,2));

        // Проверяем, что создали, печатаем содержание всех задач
        System.out.println( "\n" + "Проверяем содержание сохраненных объектов" + "\n");
        System.out.println(taskManager.getTaskList() + "\n");
        System.out.println(taskManager.getEpicList() + "\n");
        System.out.println(taskManager.getSubtaskList() + "\n");

    }

}
