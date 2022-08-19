import Managers.InMemoryTasksManager;
import Tasks.*;

public class Main {

    public static void main(String[] args) {
        InMemoryTasksManager taskManager = new InMemoryTasksManager();

        // Создаем новые объекты
        taskManager.addTask(new Task(null, TasksType.TASK, "Задача 1", TasksStatus.NEW,
                "Текст задачи 1", null));
        taskManager.addEpic(new Epic(null, TasksType.EPIC,"Эпик 2", TasksStatus.NEW,
                "Текст эпика 2", null));
        taskManager.addSubtask(new Subtask(null, TasksType.SUBTASK,"Сабтаск 3 к эпику 2",
                TasksStatus.NEW,"Текст сабтаска 3", 2));
        taskManager.addTask(new Task(null, TasksType.TASK, "Задача 4", TasksStatus.NEW,
                "Текст задачи 4", null));
        taskManager.addEpic(new Epic(null, TasksType.EPIC,"Эпик 5", TasksStatus.NEW,
                "Текст эпика 5", null));
        taskManager.addSubtask(new Subtask(null, TasksType.SUBTASK,"Сабтаск 6 к эпику 2",
                 TasksStatus.NEW, "Текст сабтаска 6", 2));
        taskManager.addSubtask(new Subtask(null, TasksType.SUBTASK,"Сабтаск 7 к эпику 2",
                TasksStatus.NEW, "Текст сабтаска 7", 2));
        taskManager.addSubtask(new Subtask(null, TasksType.SUBTASK,"Сабтаск 8 к эпику 5",
                TasksStatus.NEW, "Текст сабтаска 8", 5));
        taskManager.addSubtask(new Subtask(null, TasksType.SUBTASK,"Сабтаск 9 к эпику 5",
                TasksStatus.NEW, "Текст сабтаска 9", 5));
        taskManager.addTask(new Task(null, TasksType.TASK, "Задача 10", TasksStatus.NEW,
                "Текст задачи 10", null));
        taskManager.addEpic(new Epic(null, TasksType.EPIC,"Эпик 11", TasksStatus.NEW,
                "Текст эпика 11", null));
        taskManager.addSubtask(new Subtask(null, TasksType.SUBTASK,"Сабтаск 12 к эпику 5",
                TasksStatus.NEW, "Текст сабтаска 12", 5));

        // Получаем объект по id и проверяем список просмотров
        System.out.println("Вызываем задачи");
        taskManager.getTask(1);
        taskManager.getEpic(2);
        taskManager.getSubtask(3);
        taskManager.getTask(4);
        taskManager.getEpic(5);
        taskManager.getSubtask(6);
        taskManager.getSubtask(7);
        taskManager.getSubtask(8);
        taskManager.getSubtask(9);
        taskManager.getTask(10);
        taskManager.getEpic(11);
        taskManager.getSubtask(12);

        taskManager.printHistory();
        System.out.println("Вызываем задачу 3 еще раз");
        taskManager.getSubtask(3);
        taskManager.printHistory();

        // Проверяем, что создали, печатаем содержание всех задач
        System.out.println( "\n" + "Проверяем содержание сохраненных объектов" + "\n");
        System.out.println(taskManager.getTaskList() + "\n");
        System.out.println(taskManager.getEpicList() + "\n");
        System.out.println(taskManager.getSubtaskList() + "\n");

        // Получаем список всех задач
        System.out.println("Проверяем возвращение списков объектов" + "\n");
        System.out.println("Список задач: " + taskManager.getListAllTask() + "\n");
        System.out.println("Список эпиков: " + taskManager.getListAllEpic() + "\n");
        System.out.println("Список подзадач: " + taskManager.getListAllSubtask() + "\n");

        // Получаем список всех подзадач эпика 2
        System.out.println("Проверяем получение списка всех подзадач определенного эпика");
        taskManager.getEpicList().get(2).setListEpicSubtasks(taskManager.getSubtaskId(2));
        System.out.println("Получаем список подзадач эпика 2: "
                + taskManager.getListEpicSubtasks(2) + "\n");

        // Обновляем объекты
        System.out.println("Проверяем обновление объектов" + "\n");

        // Обновляем задачу 4
        System.out.println("Старое содержание задачи 4" + taskManager.getTask(4) + "\n");
        taskManager.updateTask(new Task(4, TasksType.TASK, "New Задача 4", TasksStatus.NEW,
                "New Текст задачи 4", null));
        System.out.println("Новое содержание задачи 4" + taskManager.getTask(4) + "\n");

        // Проверяем расчет статуса эпика на примере эпика 5
        System.out.println("Проверяем обновление статуса эпика" + "\n");
        System.out.println("Текущий статус эпика 5: " + taskManager.getEpic(5).getTaskStatus() + "\n");
        // Обновляем статус подзадачи 8 на DONE
        taskManager.updateSubtask(new Subtask(8, TasksType.SUBTASK,"New Сабтаск 8 к эпику 5",
                TasksStatus.DONE, "Текст сабтаска", 5));
        System.out.println("Меняем статус сабтаска 8 на DONE" + taskManager.getSubtaskList().get(8) + "\n");
        taskManager.updateEpicStatus(5);
        System.out.println("Новый статус эпика 5: " + taskManager.getEpic(5).getTaskStatus() + "\n");

        System.out.println("Меняем статус сабтасков 9 и 12 на DONE");
        taskManager.updateSubtask(new Subtask(9, TasksType.SUBTASK,"New Сабтаск 9 к эпику 5",
                TasksStatus.DONE, "Текст сабтаска", 5));
        taskManager.updateSubtask(new Subtask(12, TasksType.SUBTASK,"New Сабтаск 12 к эпику 5",
                TasksStatus.DONE, "Текст сабтаска", 5));
        taskManager.updateEpicStatus(5);
        System.out.println("Новый статус эпика 5: " + taskManager.getEpic(5).getTaskStatus() + "\n");

        // Удаляем элементы
        taskManager.removeTask(4);
        System.out.println("Удалили задачу 4");
        taskManager.printHistory();

        taskManager.removeTask(1);
        System.out.println("Удалили задачу 1");
        taskManager.printHistory();

        taskManager.removeSubtask(7);
        System.out.println("Удалили сабтаск 7");
        taskManager.printHistory();

        taskManager.removeEpic(5);
        System.out.println("Удалили эпик 5");
        taskManager.printHistory();

        // Удаляем все объекты в списках
        taskManager.removeAllTask();
        taskManager.removeAllEpic();
        taskManager.removeAllSubtask();
        System.out.println("Проверяем содержание сохраненных объектов после очистки списков" + "\n");
        System.out.println(taskManager.getTaskList() + "\n");
        System.out.println(taskManager.getEpicList() + "\n");
        System.out.println(taskManager.getSubtaskList() + "\n");

    }

}
