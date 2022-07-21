package Managers;

import Tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    List<Task> history = new ArrayList<>();

    public class Node {
        public Node prev;
        public Task task;
        public Node next;

        public Node(Node prev, Task task, Node next) {
            this.prev = prev;
            this.task = task;
            this.next = next;
        }

        @Override
        public String toString() {
            return  "\n" + "Node{" + "\n" +
                    "task= " + task + "\n" +
                    "prev= " + (prev == null ? null : prev.task.getTaskName())+ "\n" +
                    "next= " + (next == null ? null : next.task.getTaskName()) + "\n" +
                    '}';
        }
    }

    private final Map<Integer, Node> nodeMap = new HashMap<>();
    private Node first;
    private Node last;


    // Записываем историю просмотров
    public void addHistory(Task task){
        if (task == null) {
            return;
        }
        removeHistory(task.getTaskId());
        linkLast(task);
    }

    // Формируем ноду и добавляем в nodeMap
    public void linkLast(Task task) {
        final Node newNode = new Node(last, task, null);
        last = newNode;
        if (first == null) {
            first = newNode;
        } else {
            last.next = newNode;
            last.prev.next = newNode;
        }

        nodeMap.put(task.getTaskId(), newNode); // добавили id задачи и ноду в таблицу nodeMap
    }

    //Удаляем объект из истории просмотров
    // Вызываем метод удаления истории
    public void removeHistory(int id) {
        Node node = nodeMap.get(id);
        removeNode(node);
    }

    // Удаляем объект и меняем привязки в нодах
    public void removeNode(Node node) {
        if (node == null) {
            return;
        }
        int id = -1;
        for (Map.Entry<Integer, Node> key : nodeMap.entrySet()) {
            if (node.equals(key.getValue())) {
                id = key.getValue().task.getTaskId();
            }
        }
        final Node oldNode = nodeMap.remove(id);

        if (oldNode.prev != null){
            oldNode.prev.next = oldNode.next;
            if (oldNode.next == null) {
                last = oldNode.prev;
            } else {
                oldNode.next.prev = oldNode.prev;
            }
        } else {
            first = oldNode.next;
            if (first == null) {
                last = null;
            } else {
                first.prev = null;
            }
        }
    }

    // Получаем историю просмотров
    public List<Task> getHistory(){
        return getTask();
    }

    public List<Task> getTask(){
        List<Task> tasksList = new ArrayList<>();
        for (Map.Entry<Integer, Node> id : nodeMap.entrySet()) {
            tasksList.add(id.getValue().task);
        }
        return tasksList;
    }

    // Печатаем историю просмотров
    public void printHistory() {
        ArrayList<String> listAllTask = new ArrayList<>();
        for (Task task : getHistory()) {
            listAllTask.add(task.getTaskName());
        }
        System.out.println("История просмотров: " + listAllTask + "\n");
    }

    // Печатаем nodeMap
    public void printNodeMap() {
        System.out.println("В nodeMap ноды: ");
        for (Map.Entry<Integer, Node> id : nodeMap.entrySet()) {
            System.out.println(id.getValue() + "\n");
        }
    }
}
