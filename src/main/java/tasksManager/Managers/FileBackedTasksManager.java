package tasksManager.Managers;

import tasksManager.Tasks.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class FileBackedTasksManager extends InMemoryTasksManager {

    // Обычный конструктор
    public FileBackedTasksManager() {
    }

    // Конструктор, который принимает файл для автосохранения
    public FileBackedTasksManager(File file) {
        super();
    }

    public static Integer taskId = 0;
    private final static HashMap<Integer, Task> taskList = new HashMap<>();
    private final static HistoryManager historyManager = Managers.getDefaultHistory();
    Comparator<Task> taskComparator = Comparator.comparing(Task::getStartTime,
            Comparator.nullsLast(Comparator.naturalOrder())).thenComparing(Task::getTaskName);
    TreeSet<Task> prioritizedTasks = new TreeSet<>(taskComparator);

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
    private ArrayList<String> fileReader (File file) {
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
    private static Task fromSring (String value) {
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
            default:
                System.out.println("Задача не найдена");
        }

        Task task = null;

        switch (splitTask[1]) {
            case "TASK":
                task = new Task (Integer.parseInt(splitTask[0]), TasksType.TASK, splitTask[2], status, splitTask[4],
                        splitTask[5], Integer.parseInt(splitTask[6]), null);
                if(Integer.parseInt(splitTask[0]) > taskId) {
                    taskId = Integer.parseInt(splitTask[0]);
                }
                break;
            case "EPIC":
                task =  new Epic (Integer.parseInt(splitTask[0]), TasksType.EPIC, splitTask[2], status, splitTask[4],
                        splitTask[5], Integer.parseInt(splitTask[6]), null);
                if(Integer.parseInt(splitTask[0]) > taskId) {
                    taskId = Integer.parseInt(splitTask[0]);
                }
                break;
            case "SUBTASK":
                task =  new Subtask(Integer.parseInt(splitTask[0]), TasksType.SUBTASK, splitTask[2], status, splitTask[4],
                        splitTask[5], Integer.parseInt(splitTask[6]), Integer.parseInt(splitTask[7]));
                if(Integer.parseInt(splitTask[0]) > taskId) {
                    taskId = Integer.parseInt(splitTask[0]);
                }
                break;
            default:
                System.out.println("Тип задачи не задан");
        }
        return task;
    }

    // Запись задачи в строку
    private String toString(Task task) {
        String line;
        if(task.getEpicId() != null) {
            line = String.format("%s,%s,%s,%s,%s,%s,%s,%s\n", task.getTaskId(), task.getTaskType(), task.getTaskName(),
                    task.getTaskStatus(), task.getTaskDescription(), task.getStartTimeToString(), task.getDuration(), task.getEpicId());
        } else {
            line = String.format("%s,%s,%s,%s,%s,%s,%s\n", task.getTaskId(), task.getTaskType(), task.getTaskName(),
                    task.getTaskStatus(), task.getTaskDescription(), task.getStartTimeToString(), task.getDuration());
        }

        return line;
    }

    // Создание списка просмотров из строки
    private static List<Integer> historyFromString(String value) {
        ArrayList<Integer> list = new ArrayList<>();
        String[] number = value.split(",");
        for(String i : number) {
            list.add(Integer.parseInt(i));
        }
        return list;
    }

    // Запись списка просмотров в historyManager
    private static void recoveryHistory(List<Integer> list) {
        for(Integer i : list) {
            historyManager.addHistory(taskList.get(i));
        }
    }

    // Запись в строку истории просмотров
    private static String historyToString(HistoryManager manager) {
        ArrayList<Integer> list = new ArrayList<>();
        for (Task task : manager.getHistory()) {
            list.add(task.getTaskId());
        }
        String line = list.stream()
                         .map(Object::toString)
                         .collect(Collectors.joining(","));
        return line;
    }

    // Сохранение в файл задач и истории просмотров
    private void save() {
        try (Writer fileWriter = new FileWriter("task_storage.csv", StandardCharsets.UTF_8)) {
            fileWriter.write("id,type,name,status,description,startTime,duration,epic\n");
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

    // Добавление новой задачи
    @Override
    public int addTask(Task task) {
        if (task.getTaskType() != TasksType.EPIC) {
            if (checkFreeTime(task)) {
                if (task.getTaskId() == null) {
                    taskId++;
                    task.setTaskId(taskId);
                }
                addTaskInPrioritizedList(task);
                taskList.put(task.getTaskId(), task);
                if (task.getTaskType() == TasksType.SUBTASK) {
                    updateEpicElements(task.getEpicId());
                }
            } else {
                System.out.println("Время занято, задача не может быть добавлена, укажите другое время");
            }
        } else {
            if (task.getTaskId() == null) {
                taskId++;
                task.setTaskId(taskId);
            }
            addTaskInPrioritizedList(task);
            taskList.put(task.getTaskId(), task);
        }
        save();
        if(task.getTaskId() != null) {
            return task.getTaskId();
        } else {
            return 0;
        }
    }

    // Обновление объектов
    @Override
    public void updateTask(Task task) {
        prioritizedTasks.remove(taskList.get(task.getTaskId()));
        if (task.getTaskType() != TasksType.EPIC) {
            if (checkFreeTime(task)) {
                addTaskInPrioritizedList(task);
                taskList.put(task.getTaskId(), task);

                if (task.getTaskType() == TasksType.SUBTASK) {
                    updateEpicElements(task.getEpicId());
                }
            } else {
                System.out.println("Время занято, задача не может быть обновлена, укажите другое время");
            }
        } else {
            addTaskInPrioritizedList(task);
            taskList.put(task.getTaskId(), task);
            updateEpicElements(task.getEpicId());
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
        if (taskList.get(id) != null) {
            for (Task task : taskList.values()) {
                if (task.getEpicId()!= null && task.getEpicId() == id){
                    listSubtask.add(task.getTaskId());
                }
            }
        }
        return listSubtask;
    }

    // Апдейт startTime эпика по startTime сабтасков
    @Override
    public void updateEpicStartTime(int id) {
        LocalDateTime checkStartTime = null;
        for (Integer taskId : getSubtaskId(id)) {
            if (taskList.get(taskId).getStartTime() == null && checkStartTime == null) {
                checkStartTime = null;
            } else if (taskList.get(taskId).getStartTime() != null && checkStartTime == null) {
                checkStartTime = taskList.get(taskId).getStartTime();
            } else if (taskList.get(taskId).getStartTime().isBefore(checkStartTime)) {
                checkStartTime = taskList.get(taskId).getStartTime();
            }
        }
        if (checkStartTime != null) {
            taskList.get(id).setStartTime(checkStartTime);
        }
    }

    // Апдейт endTime эпика по endTime сабтасков
    @Override
    public void updateEpicEndTime(int id) {
        LocalDateTime checkEndTime = null;
        for (Integer taskId : getSubtaskId(id)) {
            if (taskList.get(taskId).getEndTime() == null && checkEndTime == null) {
                checkEndTime = null;
            } else if (taskList.get(taskId).getEndTime() != null && checkEndTime == null) {
                checkEndTime = taskList.get(taskId).getEndTime();
            } else if (taskList.get(taskId).getEndTime().isAfter(checkEndTime)) {
                checkEndTime = taskList.get(taskId).getEndTime();
            }
        }
        if (checkEndTime != null) {
            taskList.get(id).setEndTime(checkEndTime);
        }
    }

    // Апдейт duration эпика по сумме duration сабтасков
    @Override
    public void updateEpicDuration(int id) {
        Integer duration = 0;
        for (Integer taskId : getSubtaskId(id)) {
            if (taskList.get(taskId).getDuration() != null) {
                duration += taskList.get(taskId).getDuration();
            } else {
                duration = 0;
            }
        }
        if (duration != null) {
            taskList.get(id).setDuration(duration);
        }
    }

    // Апдейт всех элементов эпика зависящих от сабтасков
    @Override
    public void updateEpicElements(int id) {
        if (prioritizedTasks.contains(taskList.get(id))) {
            prioritizedTasks.remove(taskList.get(id));
        }
        updateEpicStatus(id);
        updateEpicStartTime(id);
        updateEpicEndTime(id);
        updateEpicDuration(id);
        addTaskInPrioritizedList(taskList.get(id));
    }

    // Удаление объектов по id
    @Override
    public void removeTask(int id) {
        if (taskList.get(id) != null) {
            if (taskList.get(id).getTaskType() == TasksType.EPIC) {
                for (Integer taskId : this.getSubtaskId(id)) {
                    historyManager.removeHistory(taskId);
                    prioritizedTasks.remove(taskList.get(taskId));
                    taskList.remove(taskId);
                }
            }
            historyManager.removeHistory(id);
            prioritizedTasks.remove(taskList.get(id));
            taskList.remove(id);
        }
    }

    // Удаление всех объектов списка
    @Override
    public void removeAllTask() {
        prioritizedTasks.clear();
        taskList.clear();
    }

    // Печать истории просмотров
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    // Приоретизация списка задач
    private void addTaskInPrioritizedList (Task task) {
        if (task.getTaskId() != null) {
            for (Task t : prioritizedTasks) {
                if (t.getTaskId() == task.getTaskId()) {
                    prioritizedTasks.remove(t);
                }
            }
        }
        prioritizedTasks.add(task);
    }

    public TreeSet<Task> getPrioritizedTasks() {
        return (TreeSet<Task>) prioritizedTasks.clone();
    }

    private boolean checkFreeTime(Task task) {
        boolean freeTime = true;
        for(Task t : getPrioritizedTasks()) {
            if (t.getTaskType() != TasksType.EPIC && t.getStartTime() != null && t.getTaskId() != task.getTaskId()) {
                if ((t.getStartTime().isBefore(task.getStartTime()) && t.getEndTime().isAfter(task.getStartTime()))
                    || t.getStartTime().equals(task.getStartTime()) || t.getEndTime().equals(task.getEndTime())
                    || (t.getStartTime().isAfter(task.getStartTime()) && t.getStartTime().isBefore(task.getEndTime()))
                    || (t.getStartTime().isAfter(task.getStartTime()) && t.getEndTime().equals(task.getEndTime()))
                    || (t.getStartTime().isAfter(task.getStartTime()) && task.getEndTime().isBefore(task.getEndTime()))) {
                    freeTime = false;
                }
            }
        }
        return freeTime;
    }


    public static void main(String[] args) throws IOException, NullPointerException {

        File file = new File("task_storage.csv");
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager().loadFromFile(file);
    }
}
