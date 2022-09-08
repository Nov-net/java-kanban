package tasksManager.Managers.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import tasksManager.Managers.ManagerSaveExeption;
import tasksManager.Managers.Managers;
import tasksManager.Managers.TaskManager;
import tasksManager.Tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class HttpTaskServer {
    public static final int PORT = 8082;
    private final HttpServer server;
    private final Gson gson;
    private final TaskManager taskManager;
    private final String url;

    public HttpTaskServer () throws IOException, ManagerSaveExeption {
        this(Managers.getDefault());
    }

    public HttpTaskServer (TaskManager taskManager) throws IOException, ManagerSaveExeption {
        this.taskManager = taskManager;
        gson = Managers.getGson();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", this::handler);
        url = "http://localhost:" + PORT + "/";
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Открой http://localhost:" + PORT + "/");
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

        if (h.getRequestURI().getRawQuery() != null) {
            id = Integer.parseInt(h.getRequestURI().getRawQuery().substring(3));
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
        return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    }

    // отправка ответа в теле ответа
    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().set("Content-Type", "application/json"); //h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        //h.getResponseBody().write(resp);

        try (OutputStream os = h.getResponseBody()) {
            os.write(resp);
        }
    }

    public void stop() {
        server.stop(0);
        System.out.println("Остановлен сервер на порту " + PORT);

    }



    public static void main(String[] args) throws IOException, ManagerSaveExeption {
        new HttpTaskServer().start();

    }

}

