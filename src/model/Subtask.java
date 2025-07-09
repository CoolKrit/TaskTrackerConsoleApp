package model;

import enums.Status;
import enums.Type;

public class Subtask extends Task {
    private int epicId;

    public Subtask(int epicId, String name, String description) {
        super(name, description);
        setStatus(Status.NEW);
        this.epicId = epicId;
    }

    public Subtask(int id, int epicId, String name, String description, Status status, Type type) {
        super(name, description);
        setId(id);
        setStatus(status);
        setType(type);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", epicId=" + epicId +
                '}';
    }
}