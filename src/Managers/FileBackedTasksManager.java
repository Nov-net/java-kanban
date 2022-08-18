package Managers;

import Tasks.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.stream.Collectors;

public class FileBackedTasksManager extends InMemoryTasksManager {

    // Обычный конструктор
    public FileBackedTasksManager() {
    }

    // Конструктор, который принимает файл для автосохранения
    public FileBackedTasksManager(File file) {
        super();
    }

    public static Integer taskId = 1;
    private final static HashMap<Integer, Task> taskList = new HashMap<>();
    private final static HistoryManager historyManager = Managers.getDefaultHistory();

    // Метод создания экземпляра класса с восстановленным списком задач и историей просмотров
    public FileBackedTasksManager loadFromFile(File file) throws IOException, NullPointerException, NumberFormatException {
        final FileBackedTasksManager taskManager = new FileBackedTasksManager(file);

        for (String line : taskManager.fileReader(file)) {
            if(line.isEmpty() || line.isBlank()) {
            } else if (line.contains("TASK") || line.contains("EPIC")){
                final Task task = taskManager.fromSring(line);
                try {
                    taskManager.addTask(task);
                } catch (NullPointerException e) {
                    System.out.println("Ошибка добавления задачи при восстановлении файла");
                }
            } else {
                try {
                    taskManager.recoveryHistory(taskManager.historyFromString(line));
                } catch (NumberFormatException e) {
                    System.out.println("Символ не является числом");
                }

            }
        }
        return taskManager;
    }

    // Чтение файла по строкам, возвращает список строк
    public ArrayList<String> fileReader (File file) {
        ArrayList<String> lines = new ArrayList<>();

        try (FileReader reader = new FileReader(file.getName());
             BufferedReader br = new BufferedReader(reader)) {

            while (br.ready()) {
                String line = br.readLine();
                lines.add(line);
            }

        } catch (IOException e) {
            System.out.println("Ошибка чтения файла");
        }

        lines.remove(0); // удаление заголовка
        return lines;
    }

    // Создание задачи из строки
    public static Task fromSring (String value) {
        String[] splitTask = value.split(",");
        TasksStatus status = null;
        switch (splitTask[3]) {
            case "NEW":
                status = TasksStatus.NEW;
                break;
            case "IN_PROGRESS":
                status = TasksStatus.IN_PROGRESS;
                break;
            case "DONE":
                status = TasksStatus.DONE;
                break;
        }

        Task task = null;

        switch (splitTask[1]) {
            case "TASK":
                task = new Task (Integer.parseInt(splitTask[0]), TasksType.TASK, splitTask[2], status, splitTask[4], null);
                if(Integer.parseInt(splitTask[0]) > taskId) {
                    taskId = Integer.parseInt(splitTask[0]);
                }
                break;
            case "EPIC":
                task =  new Epic (Integer.parseInt(splitTask[0]), TasksType.EPIC, splitTask[2], status, splitTask[4], null);
                if(Integer.parseInt(splitTask[0]) > taskId) {
                    taskId = Integer.parseInt(splitTask[0]);
                }
                break;
            case "SUBTASK":
                task =  new Subtask(Integer.parseInt(splitTask[0]), TasksType.SUBTASK, splitTask[2], status, splitTask[4],
                        Integer.parseInt(splitTask[5]));
                if(Integer.parseInt(splitTask[0]) > taskId) {
                    taskId = Integer.parseInt(splitTask[0]);
                }
                break;
        }
        return task;
    }

    // Запись задачи в строку
    public String toString(Task task) {
        String line;
        if(task.getEpicId() != null) {
            line = String.format("%s,%s,%s,%s,%s,%s\n", task.getTaskId(), task.getTaskType(), task.getTaskName(),
                    task.getTaskStatus(), task.getTaskDescription(), task.getEpicId());
        } else {
            line = String.format("%s,%s,%s,%s,%s\n", task.getTaskId(), task.getTaskType(), task.getTaskName(),
                    task.getTaskStatus(), task.getTaskDescription());
        }

        return line;
    }

    // Создание списка просмотров из строки
    public static List<Integer> historyFromString(String value) {
        ArrayList<Integer> list = new ArrayList<>();
        String[] number = value.split(",");
        for(String i : number) {
            list.add(Integer.parseInt(i));
        }
        return list;
    }

    // Запись списка просмотров в historyManager
    public static void recoveryHistory(List<Integer> list) {
        for(Integer i : list) {
            historyManager.addHistory(taskList.get(i));
        }
    }

    // Запись в строку истории просмотров
    public static String historyToString(HistoryManager manager) {
        ArrayList<Integer> list = new ArrayList<>();
        for (Task task : manager.getHistory()) {
            list.add(task.getTaskId());
        }
        String line = list.stream()
                         .map(Object::toString)
                         .collect(Collectors.joining(","));
        return line;
    }

    // Печать истории просмотров в строку
    public void printHistoryToString() {
        System.out.println(historyToString(historyManager));
    }

    // Сохранение в файл задач и истории просмотров
    public void save() {
        try (Writer fileWriter = new FileWriter("task_storage.csv", StandardCharsets.UTF_8)) {
            fileWriter.write("id,type,name,status,description,epic\n");
            for(Task task : taskList.values()) {
                fileWriter.write(toString(task));
            }
            fileWriter.write("\n" + historyToString(historyManager));

        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден");
        } catch (IOException e) {
            System.out.println("IOException");
        }
    }

    // Печать tasksList в строку
    public void taskListToString() {
        for(Task task : taskList.values()) {
            System.out.print(toString(task));
        }
    }

    // Добавление новой задачи
    @Override
    public int addTask(Task task) {
        if (task.getTaskId() == null) {
            taskId++;
            task.setTaskId(taskId);
        }
        taskList.put(task.getTaskId(), task);
        save();
        return task.getTaskId();
    }

    // Обновление объектов
    @Override
    public void updateTask(Task task) {
        taskList.put(task.getTaskId(), task);
        if (task.getTaskType() == TasksType.SUBTASK) {
            updateEpicStatus(task.getEpicId());
        }
        save();
    }

    // Получение объекта по id
    @Override
    public Task getTask(int id) {
        final Task task = taskList.get(id);
        historyManager.addHistory(task);
        save();
        return task;
    }

    // Возврат копии списка задач
    @Override
    public HashMap<Integer, Task> getTaskList() {
        return (HashMap<Integer, Task>) taskList.clone();
    }

    // Получение названий всех сохраненных задач
    @Override
    public ArrayList<String> getListAllTask() {
        ArrayList<String> listAllTask = new ArrayList<>();
        for (Integer id : taskList.keySet()) {
            Task task = taskList.get(id);
            listAllTask.add(task.getTaskName());
        }
        return listAllTask;
    }

    // Обновление статуса эпика по статусам сабтасков
    @Override
    public void updateEpicStatus(int id) {
        int checkStatus = 0;
        for (Integer taskId : getSubtaskId(id)) {
            if (taskList.get(taskId).getTaskStatus().equals(TasksStatus.DONE)) {
                checkStatus = checkStatus + 2;
            } else if (taskList.get(taskId).getTaskStatus().equals(TasksStatus.IN_PROGRESS)) {
                checkStatus = checkStatus + 1;
            }
        }
        if (checkStatus != 0 && checkStatus == getSubtaskId(id).size() * 2) {
            taskList.get(id).setTaskStatus(TasksStatus.DONE);
        } else if (checkStatus > 0 && checkStatus < getSubtaskId(id).size() * 2) {
            taskList.get(id).setTaskStatus(TasksStatus.IN_PROGRESS);
        } else if (checkStatus == 0){
            taskList.get(id).setTaskStatus(TasksStatus.NEW);
        }
        save();
    }

    // Получение списка сабтасков по id эпика
    @Override
    public ArrayList<Integer> getSubtaskId(int id) {
        ArrayList<Integer> listSubtask = new ArrayList<>();
        for (Task task : taskList.values()) {
            if (task.getEpicId() != null && task.getEpicId() == id) {
                listSubtask.add(task.getTaskId());
            }
        }
        return listSubtask;
    }

    // Удаление объектов по id
    @Override
    public void removeTask(int id) {
        if (taskList.get(taskId).getTaskType() == TasksType.EPIC) {
            for (Integer taskId : this.getSubtaskId(id)) {
                historyManager.removeHistory(taskId);
                taskList.remove(taskId);
            }
        }
        historyManager.removeHistory(id);
        taskList.remove(id);
    }

    // Удаление всех объектов списка
    @Override
    public void removeAllTask() {
        taskList.clear();
    }

    // Печать истории просмотров
    public void printHistory() {
        ArrayList<String> listAllTask = new ArrayList<>();
        for (Task task : historyManager.getHistory()) {
            listAllTask.add(task.getTaskName());
        }
        System.out.println("История просмотров: " + listAllTask + "\n");
    }




    public static void main(String[] args) throws IOException, NullPointerException {

        File file = new File("task_storage.csv");
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager().loadFromFile(file);

        System.out.println("Список восстановленных задач:");
        fileBackedTasksManager.taskListToString();
        System.out.println("\nВосстановленный список просмотров:");
        fileBackedTasksManager.printHistoryToString();
        System.out.println("\nПоследний taskId: " + fileBackedTasksManager.taskId + "\n");
        System.out.println("Добавляем новые задачи:");
        fileBackedTasksManager.addTask(new Epic(null, TasksType.EPIC, ("Эпик" + (fileBackedTasksManager.taskId+1)),
                TasksStatus.NEW, "Описание эпика", null));
        fileBackedTasksManager.addTask(new Subtask(null, TasksType.SUBTASK,
                ("Сабтаск к эпику " + fileBackedTasksManager.taskId), TasksStatus.NEW,
                "Текст сабтаска", (fileBackedTasksManager.taskId)));
        System.out.println("Новый список задач:");
        fileBackedTasksManager.taskListToString();
        System.out.println("\nПоследний taskId: " + fileBackedTasksManager.taskId + "\n");

        System.out.println("Вызываем задачи " + (fileBackedTasksManager.taskId - 1) + " и "
                + (fileBackedTasksManager.taskId - 3) );
        fileBackedTasksManager.getTask((fileBackedTasksManager.taskId - 1));
        fileBackedTasksManager.getTask((fileBackedTasksManager.taskId - 3));
        System.out.println("Новый список просмотров:");
        fileBackedTasksManager.printHistoryToString();

        // Обновляем сабтаски эпика 2
        System.out.println("Обновляем сабтаск 3");
        fileBackedTasksManager.updateTask(new Task(3, TasksType.SUBTASK, "New сабтаск 3 к эпику 2",
                TasksStatus.DONE, "Текст new сабтаска 3", 2));
        System.out.println("Новое содержание сабтаска 3" + fileBackedTasksManager.getTask(3) + "\n");
        System.out.println("Новый статус эпика 2: " + fileBackedTasksManager.getTask(2).getTaskStatus() + "\n");
        System.out.println("Обновляем сабтаск 5");
        fileBackedTasksManager.updateTask(new Task(5, TasksType.SUBTASK, "New сабтаск 5 к эпику 2",
                TasksStatus.DONE, "Текст new сабтаска 5", 2));
        System.out.println("Новое содержание сабтаска 5" + fileBackedTasksManager.getTask(5) + "\n");
        System.out.println("Новый статус эпика 2: " + fileBackedTasksManager.getTask(2).getTaskStatus() + "\n");

        System.out.println("Итоговый список задач: ");
        fileBackedTasksManager.taskListToString();
        System.out.println("\nИтоговый список просмотров: ");
        fileBackedTasksManager.printHistoryToString();

        System.out.println("\nВосстанавливаем еще одну копию менеджера, " +
                "проверяем сохранение и восстановление предыдущей версии");
        FileBackedTasksManager fileBackedTasksManager1 = new FileBackedTasksManager().loadFromFile(file);

        System.out.println("Список восстановленных задач:");
        fileBackedTasksManager1.taskListToString();
        System.out.println("\nВосстановленный список просмотров:");
        fileBackedTasksManager.printHistoryToString();
        System.out.println("\nПоследний taskId: " + fileBackedTasksManager1.taskId + "\n");

    }
}
