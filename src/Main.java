import manager.FileBackedTasksManager;
import manager.Managers;
import manager.interfaces.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;

import java.io.File;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        File file = new File("backUp.txt");

        // === Создание менеджера и добавление задач ===
        TaskManager manager = Managers.getDefault();

        Task task1 = new Task("Переезд", "Собрать вещи");
        Task task2 = new Task("Покупки", "Купить продукты");
        manager.createTask(task1);
        manager.createTask(task2);

        Epic epic1 = new Epic("Переезд", "Переезд в новую квартиру");
        manager.createEpic(epic1);

        Subtask sub1 = new Subtask(epic1.getId(), "Упаковать коробки", "Упаковать вещи в коробки");
        Subtask sub2 = new Subtask(epic1.getId(), "Нанять грузчиков", "Позвонить в компанию");
        manager.createSubtask(sub1);
        manager.createSubtask(sub2);

        Epic epic2 = new Epic("Отпуск", "Съездить в отпуск");
        manager.createEpic(epic2);
        Subtask sub3 = new Subtask(epic2.getId(), "Забронировать отель", "Через Booking");
        manager.createSubtask(sub3);

        // Просмотр задач (для истории)
        manager.getTaskById(task1.getId());
        manager.getTaskById(task2.getId());
        manager.getEpicById(epic1.getId());
        manager.getSubtaskById(sub1.getId());
        manager.getSubtaskById(sub2.getId());
        manager.getEpicById(epic2.getId());
        manager.getSubtaskById(sub3.getId());

        // Удалим задачу и эпик для проверки удаления из истории
        manager.removeTaskById(task2.getId());
        manager.removeEpicById(epic1.getId());

        System.out.println("=== Состояние ДО восстановления ===");
        printManagerState(manager);

        // === Восстановление из файла ===
        System.out.println("\n=== Восстановление менеджера из файла ===");
        FileBackedTasksManager restoredManager = FileBackedTasksManager.loadFromFile(file);
        printManagerState(restoredManager);
    }

    private static void printManagerState(TaskManager manager) {
        System.out.println("\nЗадачи:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }

        System.out.println("\nЭпики:");
        for (Epic epic : manager.getAllEpics()) {
            System.out.println(epic);
        }

        System.out.println("\nПодзадачи:");
        for (Subtask subtask : manager.getAllSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("\nИстория:");
        List<Task> history = manager.getHistory();
        for (Task task : history) {
            System.out.println(task);
        }
    }
}