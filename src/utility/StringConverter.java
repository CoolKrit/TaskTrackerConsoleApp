package utility;

import enums.Status;
import enums.Type;
import manager.interfaces.HistoryManager;
import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.List;

public class StringConverter {
    public static String toString(Task task) {
        String likeHeader = task.getId() + "," + task.getType() + "," + task.getName() + "," + task.getStatus() + "," + task.getDescription() + ",";
        if (task.getType() == Type.SUBTASK) {
            Subtask subtask = (Subtask) task;
            return likeHeader + subtask.getEpicId() + "\n";
        }
        return likeHeader + "\n";
    }

    public static Task fromString(String value) {
        String[] parts = value.split(",");
        int id = Integer.parseInt(parts[0]);
        Type type = Type.valueOf(parts[1]);
        String name = parts[2];
        Status status = Status.valueOf(parts[3]);
        String description = parts[4];

        if (type == Type.SUBTASK) {
            int epicId = Integer.parseInt(parts[5]);
            return new Subtask(id, epicId, name, description, status, type);
        } else if (type == Type.EPIC) {
            return new Epic(id, name, description, status, type);
        } else {
            return new Task(id, name, description, status, type);
        }
    }

    public static String historyToString(HistoryManager manager) {
        StringBuilder stringBuilder = new StringBuilder();
        List<Task> history = manager.getHistory();
        int t = 0;

        while (t < history.size()) {
            stringBuilder.append(history.get(t).getId());
            t++;
            if (t != history.size())
                stringBuilder.append(',');
        }
        return stringBuilder.toString();
    }

    public static List<Integer> historyFromString(String values) {
        List<Integer> history = new ArrayList<>();
        for (String value : values.split(",")) {
            history.add(Integer.parseInt(value));
        }
        return history;
    }
}