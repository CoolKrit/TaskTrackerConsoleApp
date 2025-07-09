package model;

import enums.Status;

public class Subtask extends Task {
    private int epicId;

    public Subtask(int epicId, String name, String description) {
        super(name, description);
        this.status = Status.NEW;
        this.epicId = epicId;
    }

    public Subtask(int id, int epicId, String name, String description, Status status) {
        super(name, description);
        this.id = id;
        this.status = status;
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", epicId=" + epicId +
                '}';
    }
}