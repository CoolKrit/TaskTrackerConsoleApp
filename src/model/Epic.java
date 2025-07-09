package model;

import enums.Status;
import enums.Type;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Subtask> subtasks;

    public Epic(String name, String description) {
        super(name, description);
        setStatus(Status.NEW);
        this.subtasks = new ArrayList<>();
    }

    public Epic(int id, String name, String description, Status status, Type type) {
        super(name, description);
        setId(id);
        setStatus(status);
        setType(type);
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
            setStatus(Status.NEW);
            return;
        }

        int countNew = 0;
        int countDone = 0;

        for (Subtask subtask : subtasks) {
            if (subtask.getStatus() == Status.NEW) countNew++;
            else if (subtask.getStatus() == Status.DONE) countDone++;
        }

        if (countNew == subtasks.size()) {
            setStatus(Status.NEW);
        } else if (countDone == subtasks.size()) {
            setStatus(Status.DONE);
        } else {
            setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", subtasksCount=" + subtasks.size() +
                '}';
    }
}