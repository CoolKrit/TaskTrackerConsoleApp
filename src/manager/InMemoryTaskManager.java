package manager;

import enums.Status;
import manager.interfaces.HistoryManager;
import manager.interfaces.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private int nextId = 1;

    private int generateId() {
        return nextId++;
    }

    // ===== ЗАДАЧИ =====
    @Override
    public void createTask(Task task) {
        int id = generateId();
        task.setId(id);
        tasks.put(id, task);
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void removeTaskById(int id) {
        tasks.remove(id);
    }

    @Override
    public void removeAllTasks() {
        tasks.clear();
    }

    // ===== ЭПИКИ =====
    @Override
    public void createEpic(Epic epic) {
        int id = generateId();
        epic.setId(id);
        epics.put(id, epic);
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public void updateEpic(Epic epic) {
        Epic storedEpic = epics.get(epic.getId());
        if (storedEpic != null) {
            storedEpic.setName(epic.getName());
            storedEpic.setDescription(epic.getDescription());
            storedEpic.setStatus(epic.getStatus());
            if (epic.getStatus() != Status.IN_PROGRESS) {
                for (Subtask subtask : storedEpic.getSubtasks()) {
                    subtask.setStatus(storedEpic.getStatus());
                }
            } else {
                storedEpic.updateStatus();
            }
        }
    }

    @Override
    public void removeEpicById(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            for (Subtask sub : epic.getSubtasks()) {
                subtasks.remove(sub.getId());
            }
        }
    }

    @Override
    public void removeAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    // ===== ПОДЗАДАЧИ =====
    @Override
    public void createSubtask(Subtask subtask) {
        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);
        if (epic != null) {
            int id = generateId();
            subtask.setId(id);
            subtasks.put(id, subtask);
            epic.addSubtask(subtask);
        }
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
        }
        return subtask;
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (!subtasks.containsKey(subtask.getId())) {
            return;
        }

        Subtask oldSubtask = subtasks.get(subtask.getId());
        int oldEpicId = oldSubtask.getEpicId();
        int newEpicId = subtask.getEpicId();

        if (oldEpicId != newEpicId) {
            Epic oldEpic = epics.get(oldEpicId);
            if (oldEpic != null) {
                oldEpic.removeSubtask(oldSubtask);
            }

            Epic newEpic = epics.get(newEpicId);
            if (newEpic != null) {
                newEpic.addSubtask(subtask);
            }
        } else {
            Epic epic = epics.get(newEpicId);
            if (epic != null) {
                epic.replaceSubtask(subtask);
            }
        }

        subtasks.put(subtask.getId(), subtask);
    }

    @Override
    public void removeSubtaskById(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.removeSubtask(subtask);
            }
        }
    }

    @Override
    public void removeAllSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.removeAllSubtasks();
        }
    }

    @Override
    public List<Subtask> getSubtasksByEpicId(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic != null) {
            return new ArrayList<>(epic.getSubtasks());
        }
        return Collections.emptyList();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}