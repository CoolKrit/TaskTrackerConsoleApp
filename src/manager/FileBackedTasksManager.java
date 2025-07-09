package manager;

import model.Epic;
import model.Subtask;
import model.Task;
import utility.StringConverter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private File fileName;

    public FileBackedTasksManager(File fileName) {
        this.fileName = fileName;
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        save();
    }

    @Override
    public void removeSubtaskById(int id) {
        super.removeSubtaskById(id);
        save();
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    @Override
    public void removeAllSubtasks() {
        super.removeAllSubtasks();
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    private void save() {
        Path path = Paths.get(fileName.toString());
        String header = "id,type,name,status,description,epic\n";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(header);

        for (Task task : super.getAllTasks()) {
            stringBuilder.append(StringConverter.toString(task));
        }
        for (Epic task : super.getAllEpics()) {
            stringBuilder.append(StringConverter.toString(task));
        }
        for (Subtask task : super.getAllSubtasks()) {
            stringBuilder.append(StringConverter.toString(task));
        }
        stringBuilder.append('\n');
        stringBuilder.append(StringConverter.historyToString(getHistoryManager()));
        try (FileWriter writer = new FileWriter(path.toString())) {
            writer.write(stringBuilder.toString());
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи в файл");
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager manager = new FileBackedTasksManager(file);
        List<String> lines;

        try {
            lines = java.nio.file.Files.readAllLines(file.toPath());
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла");
        }

        int separatorIndex = lines.indexOf(""); // индекс пустой строки — начало истории
        for (int i = 1; i < separatorIndex; i++) { // пропускаем заголовок (i = 1)
            Task task = StringConverter.fromString(lines.get(i));
            switch (task.getType()) {
                case TASK:
                    manager.getTasks().put(task.getId(), task);
                    break;
                case EPIC:
                    Epic epic = (Epic) task;
                    manager.getEpics().put(epic.getId(), epic);
                    break;
                case SUBTASK:
                    Subtask subtask = (Subtask) task;
                    manager.getSubtasks().put(subtask.getId(), subtask);

                    Epic parent = manager.getEpics().get(subtask.getEpicId());
                    if (parent != null) {
                        parent.addSubtask(subtask);
                    }
                    break;
            }
            manager.setNextId(Math.max(manager.getNextId(), task.getId() + 1));
        }

        if (separatorIndex + 1 < lines.size()) {
            String historyLine = lines.get(separatorIndex + 1);
            List<Integer> historyIds = StringConverter.historyFromString(historyLine);

            for (Integer id : historyIds) {
                if (manager.getTasks().containsKey(id)) {
                    manager.getHistoryManager().add(manager.getTasks().get(id));
                } else if (manager.getSubtasks().containsKey(id)) {
                    manager.getHistoryManager().add(manager.getSubtasks().get(id));
                } else if (manager.getEpics().containsKey(id)) {
                    manager.getHistoryManager().add(manager.getEpics().get(id));
                }
            }
        }

        return manager;
    }
}
