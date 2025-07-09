package manager;

import manager.interfaces.HistoryManager;
import manager.interfaces.TaskManager;

import java.io.File;

public class Managers {
    private static File file = new File("backUp.txt");

    public static TaskManager getDefault() {
        return new FileBackedTasksManager(file);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}