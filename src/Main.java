import TaskManager.TaskManager;
import TaskManager.Task;
import TaskManager.Epic;
import TaskManager.Subtask;

public class Main {
    /*
    Ростислав, привет!
    Оставила вывод на печать всех результатов для удобства проверки
    */

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        int id = 0;

        // Создаем новые объекты
        taskManager.createNewTask(new Task("Задача 1", "Текст задачи 1", "NEW"));
        taskManager.createNewEpic(new Epic("Эпик 2", "Текст эпика 2",
                "NEW"));
        taskManager.createNewSubtask(new Subtask("Сабтаск 3 к эпику 2", "Текст сабтаска 3",
                "NEW", 2));
        taskManager.createNewTask(new Task("Задача 4", "Текст задачи 4", "NEW"));
        taskManager.createNewEpic(new Epic("Эпик 5", "Текст эпика 5",
                "NEW"));
        taskManager.createNewSubtask(new Subtask("Сабтаск 6 к эпику 2", "Текст сабтаска 6",
                "NEW", 2));
        taskManager.createNewSubtask(new Subtask("Сабтаск 7 к эпику 2", "Текст сабтаска 7",
                "NEW", 2));
        taskManager.createNewSubtask(new Subtask("Сабтаск 8 к эпику 5", "Текст сабтаска 8",
                "NEW", 5));
        taskManager.createNewSubtask(new Subtask("Сабтаск 9 к эпику 5", "Текст сабтаска 9",
                "NEW", 5));

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
        taskManager.getTask(id);
        taskManager.getEpic(id);
        taskManager.getSubtask(id);

        // Проверяем получение объекта по id
        System.out.println("Получили по id задачу 4: " + taskManager.getTask(4) + "\n");
        System.out.println("Получили по id эпик 2: " + taskManager.getEpic(2) + "\n");
        System.out.println("Получили по id подзадачу 8: " + taskManager.getSubtask(8) + "\n");

        // Получаем список всех подзадач определенного эпика
        System.out.println("Проверяем получение списка всех подзадач определенного эпика");
        taskManager.getEpicSubtasks(id);
        // Проверяем на эпике 2
        System.out.println("Список подзадач эпика 2: " + taskManager.getEpicSubtasks(2) + "\n");

        // Обновляем объекты
        System.out.println("Проверяем обновление объектов" + "\n");

        // Обновляем задачу 4
        System.out.println("Старое содержание задачи 4" + taskManager.getTask(4) + "\n");
        taskManager.updateTask(4, new Task("Новая задача 4",
                "Новый текст задачи 4", "IN_PROGRESS"));
        System.out.println("Новое содержание задачи 4" + taskManager.getTask(4) + "\n");

        // Обновляем эпик 2
        System.out.println("Старое содержание эпика 2" + taskManager.getEpic(2) + "\n");
        taskManager.updateEpic(2, new Epic("Новое название эпика 2",
                "Новый текст эпика 2", "IN_PROGRESS"));
        System.out.println("Новое содержание эпика 2 (до перерасчета статуса) " + taskManager.getEpic(2) + "\n");
        // проверяем и обновляем статус эпика по статусам подзадач
        taskManager.setEpicStatus(2);
        System.out.println("Новый статус эпика 2 после перерасчета статуса: " + taskManager.getEpic(2).getTaskStatus() + "\n");

        // Обновляем подзадачу 7
        System.out.println("Старое содержание сабтаска 7" + taskManager.getSubtask(7) + "\n");
        taskManager.updateSubtask(7, new Subtask("New сабтаск 7 к эпику 2",
                "New Текст сабтаска 7", "DONE", 2));
        System.out.println("Новое содержание сабтаска 7" + taskManager.getSubtask(7) + "\n");
        // проверяем и обновляем статус эпика
        taskManager.setEpicStatus(2);
        System.out.println("Новый статус эпика 2: " + taskManager.getEpic(2).getTaskStatus() + "\n");

        // Проверяем расчет статуса эпика на примере эпика 5
        System.out.println("Проверяем обновление статуса эпика" + "\n");
        System.out.println("Текущий статус эпика 5: " + taskManager.getEpic(5).getTaskStatus() + "\n");
        // Обновляем статус подзадачи 8 на IN_PROGRESS
        taskManager.updateSubtask(8, new Subtask("Новая 8",
                "Текст новой 8", "IN_PROGRESS", 5));
        System.out.println("Меняем статус сабтаска 8 на IN_PROGRESS" + taskManager.getSubtaskList().get(8) + "\n");
        taskManager.setEpicStatus(5);
        System.out.println("Новый статус эпика 5: " + taskManager.getEpic(5).getTaskStatus() + "\n");

        // Обновляем статус подзадачи 8 на DONE
        taskManager.updateSubtask(8, new Subtask("New сабтаск 8 к эпику 5",
                "New Текст сабтаска 8", "DONE", 5));
        System.out.println("Меняем статус сабтаска 8 на DONE" + taskManager.getSubtaskList().get(8) + "\n");
        taskManager.setEpicStatus(5);
        System.out.println("Новый статус эпика 5: " + taskManager.getEpic(5).getTaskStatus() + "\n");

        // Обновляем статус подзадачи 9 на DONE
        taskManager.updateSubtask(9, new Subtask("New 9 к эпику 5",
                "New Текст 9", "DONE", 5));
        System.out.println("Меняем статус сабтаска 9 на DONE" + taskManager.getSubtaskList().get(9) + "\n");
        taskManager.setEpicStatus(5);
        System.out.println("Новый статус эпика 5: " + taskManager.getEpic(5).getTaskStatus() + "\n");


        // Удаляем элементы
        taskManager.deleteTask(id);
        taskManager.deleteEpic(id);
        taskManager.deleteSubtask(id);
        // Проверяем удаление
        taskManager.deleteTask(4);
        taskManager.deleteEpic(5);
        taskManager.deleteSubtask(7);
        System.out.println("Список всех задач после удаления задачи 4: " + taskManager.getListAllTask() + "\n");
        System.out.println("Список эпиков после удаления эпика 5: " + taskManager.getListAllEpic() + "\n");
        System.out.println("Список подзадач после удаления сабтаска 7: " + taskManager.getListAllSubtask() + "\n");
        
        // Удаляем все объекты в списках
        taskManager.deleteAllTask();
        taskManager.deleteAllEpic();
        taskManager.deleteAllSubtask();
        System.out.println("Проверяем содержание сохраненных объектов после очистки списков" + "\n");
        System.out.println(taskManager.getTaskList() + "\n");
        System.out.println(taskManager.getEpicList() + "\n");
        System.out.println(taskManager.getSubtaskList() + "\n");

    }

}
