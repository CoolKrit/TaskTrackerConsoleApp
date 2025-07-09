package manager.interfaces;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.List;

public interface TaskManager {
    void createTask(Task task);
    List<Task> getAllTasks();
    Task getTaskById(int id);
    void updateTask(Task task);
    void removeTaskById(int id);
    void removeAllTasks();

    void createEpic(Epic epic);
    List<Epic> getAllEpics();
    Epic getEpicById(int id);
    void updateEpic(Epic epic);
    void removeEpicById(int id);
    void removeAllEpics();

    void createSubtask(Subtask subtask);
    List<Subtask> getAllSubtasks();
    Subtask getSubtaskById(int id);
    void updateSubtask(Subtask subtask);
    void removeSubtaskById(int id);
    void removeAllSubtasks();

    List<Subtask> getSubtasksByEpicId(int epicId);

    List<Task> getHistory();
}