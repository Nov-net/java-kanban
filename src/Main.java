import Managers.InMemoryTaskManager;
import Managers.Managers;
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

        /*
        Ростислав, привет!
        Вернула все проверки, они были в прошлом спринте, но удалила их, чтобы не перегружать.
        */

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

        // Получаем объект по id
        System.out.println("Получили по id задачу 4: " + taskManager.getTask(4) + "\n");
        System.out.println("Получили по id эпик 2: " + taskManager.getEpic(2) + "\n");
        System.out.println("Получили по id подзадачу 8: " + taskManager.getSubtask(8) + "\n");

        // Получаем список всех подзадач эпика 2
        System.out.println("Проверяем получение списка всех подзадач определенного эпика");
        taskManager.getEpicList().get(2).setListEpicSubtasks(taskManager.addSubtaskId(2));
        System.out.println("Получаем список подзадач эпика 2: "
                + taskManager.getListEpicSubtasks(2) + "\n");

        // Обновляем объекты
        System.out.println("Проверяем обновление объектов" + "\n");

        // Обновляем задачу 4
        System.out.println("Старое содержание задачи 4" + taskManager.getTask(4) + "\n");
        taskManager.updateTask(new Task("Новая задача 4",
                "Новый текст задачи 4", TasksStatus.IN_PROGRESS, 4));
        System.out.println("Новое содержание задачи 4" + taskManager.getTask(4) + "\n");

        // Обновляем эпик 2
        System.out.println("Старое содержание эпика 2" + taskManager.getEpic(2) + "\n");
        taskManager.updateEpic(new Epic("Новое название эпика 2",
                "Новый текст эпика 2", TasksStatus.IN_PROGRESS, 2));
        System.out.println("Новое содержание эпика 2 (до перерасчета статуса) " + taskManager.getEpic(2) + "\n");
        // проверяем и обновляем статус эпика по статусам подзадач
        taskManager.setEpicStatus(2);
        System.out.println("Новый статус эпика 2 после перерасчета статуса: " + taskManager.getEpic(2).getTaskStatus() + "\n");

        // Обновляем подзадачу 7
        System.out.println("Старое содержание сабтаска 7" + taskManager.getSubtask(7) + "\n");
        taskManager.updateSubtask(new Subtask("New сабтаск 7 к эпику 2",
                "New Текст сабтаска 7", TasksStatus.DONE, 7, 2));
        System.out.println("Новое содержание сабтаска 7" + taskManager.getSubtask(7) + "\n");
        // проверяем и обновляем статус эпика
        taskManager.setEpicStatus(2);
        System.out.println("Новый статус эпика 2: " + taskManager.getEpic(2).getTaskStatus() + "\n");

        // Проверяем расчет статуса эпика на примере эпика 5
        System.out.println("Проверяем обновление статуса эпика" + "\n");
        System.out.println("Текущий статус эпика 5: " + taskManager.getEpic(5).getTaskStatus() + "\n");
        // Обновляем статус подзадачи 8 на IN_PROGRESS
        taskManager.updateSubtask(new Subtask("Новая 8",
                "Текст новой 8", TasksStatus.IN_PROGRESS, 8, 5));
        System.out.println("Меняем статус сабтаска 8 на IN_PROGRESS" + taskManager.getSubtaskList().get(8) + "\n");
        taskManager.setEpicStatus(5);
        System.out.println("Новый статус эпика 5: " + taskManager.getEpic(5).getTaskStatus() + "\n");

        // Обновляем статус подзадачи 8 на DONE
        taskManager.updateSubtask(new Subtask("New сабтаск 8 к эпику 5",
                "New Текст сабтаска 8", TasksStatus.DONE, 8, 5));
        System.out.println("Меняем статус сабтаска 8 на DONE" + taskManager.getSubtaskList().get(8) + "\n");
        taskManager.setEpicStatus(5);
        System.out.println("Новый статус эпика 5: " + taskManager.getEpic(5).getTaskStatus() + "\n");

        // Обновляем статус подзадачи 9 на DONE
        taskManager.updateSubtask(new Subtask("New 9 к эпику 5",
                "New Текст 9", TasksStatus.DONE, 9, 5));
        System.out.println("Меняем статус сабтаска 9 на DONE" + taskManager.getSubtaskList().get(9) + "\n");
        taskManager.setEpicStatus(5);
        System.out.println("Новый статус эпика 5: " + taskManager.getEpic(5).getTaskStatus() + "\n");

        // Удаляем элементы
        taskManager.removeTask(4);
        taskManager.removeEpic(5);
        taskManager.removeSubtask(7);
        System.out.println("Список всех задач после удаления задачи 4: " + taskManager.getListAllTask() + "\n");
        System.out.println("Список эпиков после удаления эпика 5: " + taskManager.getListAllEpic() + "\n");
        System.out.println("Список подзадач после удаления сабтаска 7: " + taskManager.getListAllSubtask() + "\n");

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
