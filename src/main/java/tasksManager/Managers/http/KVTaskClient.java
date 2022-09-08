package tasksManager.Managers.http;

import tasksManager.Managers.ManagerSaveExeption;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final String url;
    private final String apiToken;

    public KVTaskClient (int port) throws ManagerSaveExeption {
        url = "http://localhost:" + port + "/";
        apiToken = register(url); //"DEBUG";
    }

    private String register(String url) throws ManagerSaveExeption {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                                             .GET()
                                             .uri(URI.create(url + "register"))
                                             .build();
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            HttpResponse<String> response = client.send(request, handler);

            if (response.statusCode() != 200) {
                throw new ManagerSaveExeption("Невозможно обработать запрос, status code " + response.statusCode());
            }
            return response.body();

        } catch (IOException | InterruptedException | IllegalArgumentException e){
            throw new ManagerSaveExeption("Невозможно обработать запрос " + e.getMessage());
        }
    }

    public void put (String key, String json) throws ManagerSaveExeption {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                                             .POST(HttpRequest.BodyPublishers.ofString(json))
                                             .uri(URI.create(url + "save/" + key + "?API_TOKEN=" + apiToken))
                                             .build();
            HttpResponse.BodyHandler<Void> handler = HttpResponse.BodyHandlers.discarding();
            HttpResponse<Void> response = client.send(request, handler);

            if (response.statusCode() != 200) {
                throw new ManagerSaveExeption("Невозможно обработать запрос, status code " + response.statusCode());
            }

        } catch (IOException | InterruptedException | IllegalArgumentException e){
            throw new ManagerSaveExeption("Невозможно обработать запрос " + e.getMessage());
        }
    }

    public String load (String key) throws ManagerSaveExeption {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                                             .GET()
                                             .uri(URI.create(url + "load/" + key + "?API_TOKEN=" + apiToken))
                                             .build();
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            HttpResponse<String> response = client.send(request, handler);

            if (response.statusCode() != 200) {
                System.out.println("Невозможно обработать запрос, status code " + response.statusCode());
            }
            return response.body();

        } catch (IOException | InterruptedException | IllegalArgumentException e){
            throw new ManagerSaveExeption("Невозможно обработать запрос " + e.getMessage());
        }
    }

}
