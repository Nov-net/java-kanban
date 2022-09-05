package tasksManager.http;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import tasksManager.Managers.ManagerSaveExeption;
import tasksManager.Managers.Managers;
import tasksManager.Managers.TaskManager;
import tasksManager.Tasks.Task;
import tasksManager.server.KVServer;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {
    public static final int PORT = 8080;
    private final HttpServer server;
    private final Gson gson;
    private final TaskManager taskManager;

    public HttpTaskServer () throws IOException, ManagerSaveExeption {
        this(Managers.getDefault());
    }

    public HttpTaskServer (TaskManager taskManager) throws IOException, ManagerSaveExeption {
        this.taskManager = taskManager;
        gson = Managers.getGson();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", this::handler);
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/");
        server.start();
    }

    private void handler(HttpExchange h) {
        try {
            final String path = h.getRequestURI().getPath().substring("/tasks/".length());

            switch (path) {
                case "task":
                    handleTask(h);
                    break;
                case "epic":
                    handleTask(h);
                    break;
                case "subtask":
                    handleTask(h);
                    break;
                case "subtask/epic":
                    if (h.getRequestURI().getQuery() != null) {
                        Integer id = Integer.parseInt(h.getRequestURI().getQuery().substring(3));
                        final String response = gson.toJson(taskManager.getSubtaskId(id));
                        sendText(h, response);
                        break;
                    }
                    handleTask(h);
                    break;
                case "history":
                    handleGetHistory(h);
                    break;
                case "":
                    handleGetPrioritizedTasks(h);
                    break;
                default:
                    System.out.println("Неизвестный запрос " + path);
                    h.sendResponseHeaders(404, 0);
            }
        } catch (Exception e) {
            System.out.println("Ошибка обработки handler() " + e.getMessage());
        }
        h.close();
    }

    // Обработка эндпоинтов /tasks/task, /tasks/epic, /tasks/subtask
    private void handleTask(HttpExchange h) throws IOException {
        Integer id = null;

        if (h.getRequestURI().getQuery() != null) {
            id = Integer.parseInt(h.getRequestURI().getQuery().substring(3));
        }

        switch (h.getRequestMethod()) {
            case "GET":
                if (id == null) {
                    final String response = gson.toJson(taskManager.getTaskList());
                    sendText(h, response);
                    return;
                }
                final String response = gson.toJson(taskManager.getTask(id));
                sendText(h, response);
                break;

            case "POST":
                String json = readText(h);
                if(json.isEmpty()) {
                    System.out.println("Не заполнено тело запроса");
                    h.sendResponseHeaders(404, 0);
                    return;
                }
                final Task task = gson.fromJson(json, Task.class);

                if (task.getTaskId() == null) {
                    int taskId = taskManager.addTask(task);
                    final String response2 = gson.toJson("Задача добавлена \n" + taskManager.getTask(taskId));
                    sendText(h, response2);
                } else {
                    taskManager.updateTask(task);
                    System.out.println("Задача id=" + task.getTaskId() + " обновлена");
                    h.sendResponseHeaders(200, 0);
                }
                break;

            case "DELETE":
                if (id == null) {
                    taskManager.removeAllTask();
                    h.sendResponseHeaders(200, 0);
                    return;
                }
                taskManager.removeTask(id);
                h.sendResponseHeaders(200, 0);
                break;

            default:
                System.out.println("Неизвестный метод " + h.getRequestMethod());
                h.sendResponseHeaders(404, 0);
        }
    }

    // Обработка эндпоинта на возвращение getHistory()
    private void handleGetHistory(HttpExchange h) {
        try {
            if (!h.getRequestMethod().equals("GET")) {
                System.out.println(h.getRequestMethod() + " запрос вместо GET");
                h.sendResponseHeaders(405, 0);
            }
            final String response = gson.toJson("Возврат истории " + taskManager.getHistory());
            sendText(h, response);
        } catch (Exception e) {
            System.out.println("Ошибка обработки getHistory() " + e.getMessage());
        }
        h.close();
    }

    // Обработка эндпоинта на возвращение getPrioritizedTasks()
    private void handleGetPrioritizedTasks(HttpExchange h) {
        try {
            if (!h.getRequestMethod().equals("GET")) {
                System.out.println(h.getRequestMethod() + " запрос вместо GET");
                h.sendResponseHeaders(405, 0);
            }
            final String response = gson.toJson("Возврат списка задач " + taskManager.getPrioritizedTasks());
            sendText(h, response);
        } catch (Exception e) {
            System.out.println("Ошибка обработки getPrioritizedTasks() " + e.getMessage());
        }
        h.close();
    }

    // чтение содержимого тела запроса
    protected String readText(HttpExchange h) throws IOException {
        InputStream inputStream = h.getRequestBody();
        return new String(inputStream.readAllBytes(), UTF_8);
    }

    // отправка ответа в теле ответа
    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }

    public void stop() {
        server.stop(0);
        System.out.println("Остановлен сервер на порту " + PORT);

    }

    public static class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {

        @Override
        public void write(final JsonWriter jsonWriter, final LocalDateTime localDate) throws IOException {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
            jsonWriter.value(localDate.format(formatter));
        }

        @Override
        public LocalDateTime read(final JsonReader jsonReader) throws IOException {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
            return LocalDateTime.parse(jsonReader.nextString(), formatter);
        }
    }

    public static void main(String[] args) throws IOException, ManagerSaveExeption {
        new HttpTaskServer().start();

    }

}