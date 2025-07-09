package manager;

import manager.interfaces.HistoryManager;
import utility.CustomLinkedList;
import utility.Node;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private final CustomLinkedList<Task> history = new CustomLinkedList<>();
    private final Map<Integer, Node<Task>> historyMap = new HashMap<>();

    @Override
    public void add(Task task) {
        if (task == null) return;

        remove(task.getId());

        Node<Task> node = new Node<>(task);
        history.linkLast(node);
        historyMap.put(task.getId(), node);
    }

    @Override
    public void remove(int id) {
        Node<Task> node = historyMap.get(id);
        if (node != null) {
            history.removeNode(node);
            historyMap.remove(id);
        }
    }

    @Override
    public List<Task> getHistory() {
        Node<Task> current = history.getHead();
        List<Task> historyInfo = new ArrayList<>();
        while (current != null) {
            historyInfo.add(current.getData());
            current = current.getNext();
        }
        return historyInfo;
    }
}