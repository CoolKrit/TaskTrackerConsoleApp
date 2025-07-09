package model;

import enums.Status;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Subtask> subtasks;

    public Epic(String name, String description) {
        super(name, description);
        this.status = Status.NEW;
        this.subtasks = new ArrayList<>();
    }

    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks);
    }

    public void setSubtasks(List<Subtask> subtasks) {
        this.subtasks.clear();
        if (subtasks != null) {
            this.subtasks.addAll(subtasks);
        }
        updateStatus();
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
        updateStatus();
    }

    public void removeSubtask(Subtask subtask) {
        subtasks.removeIf(st -> st.getId() == subtask.getId());
        updateStatus();
    }

    public void removeAllSubtasks() {
        subtasks.clear();
        updateStatus();
    }

    public void replaceSubtask(Subtask updated) {
        for (int i = 0; i < subtasks.size(); i++) {
            if (subtasks.get(i).getId() == updated.getId()) {
                subtasks.set(i, updated);
                break;
            }
        }
        updateStatus();
    }

    public void updateStatus() {
        if (subtasks.isEmpty()) {
            this.status = Status.NEW;
            return;
        }

        int countNew = 0;
        int countDone = 0;

        for (Subtask subtask : subtasks) {
            if (subtask.getStatus() == Status.NEW) countNew++;
            else if (subtask.getStatus() == Status.DONE) countDone++;
        }

        if (countNew == subtasks.size()) {
            this.status = Status.NEW;
        } else if (countDone == subtasks.size()) {
            this.status = Status.DONE;
        } else {
            this.status = Status.IN_PROGRESS;
        }
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", subtasksCount=" + subtasks.size() +
                '}';
    }
}