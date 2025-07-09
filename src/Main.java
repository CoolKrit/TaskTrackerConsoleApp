import enums.Status;
import manager.interfaces.TaskManager;
import manager.Managers;
import model.Epic;
import model.Subtask;
import model.Task;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        // Создание задач
        Task task1 = new Task("Переезд", "Переезд в новую квартиру");
        Task task2 = new Task("Покупка", "Купить продукты");
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        // Создание эпиков и подзадач
        Epic epic1 = new Epic("Ремонт", "Ремонт квартиры");
        taskManager.createEpic(epic1);
        Subtask sub1 = new Subtask(epic1.getId(), "Купить краску", "Для стен");
        Subtask sub2 = new Subtask(epic1.getId(), "Покрасить стены", "Белой краской");
        taskManager.createSubtask(sub1);
        taskManager.createSubtask(sub2);

        Epic epic2 = new Epic("Отпуск", "Организация отпуска");
        taskManager.createEpic(epic2);
        Subtask sub3 = new Subtask(epic2.getId(), "Забронировать отель", "Через сайт");
        taskManager.createSubtask(sub3);

        // Обновление задачи
        Task updatedTask = new Task(task1.getId(), "Переезд", "Переезд в другой город", Status.IN_PROGRESS);
        taskManager.updateTask(updatedTask);

        // Обновление статусов подзадач
        sub1.setStatus(Status.DONE);
        taskManager.updateSubtask(sub1);
        sub2.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(sub2);

        // Перемещение подзадачи sub2 из epic1 в epic2
        Subtask movedSub2 = new Subtask(sub2.getId(), epic2.getId(), sub2.getName(), sub2.getDescription(), sub2.getStatus());
        taskManager.updateSubtask(movedSub2);

        // Просмотр задач для формирования истории
        taskManager.getTaskById(task1.getId());
        taskManager.getTaskById(task2.getId());
        taskManager.getEpicById(epic1.getId());
        taskManager.getSubtaskById(sub1.getId());
        taskManager.getSubtaskById(sub2.getId());
        taskManager.getEpicById(epic2.getId());
        taskManager.getSubtaskById(sub3.getId());
        taskManager.getTaskById(task1.getId());

        // Удаление задачи и эпика
        taskManager.removeTaskById(task2.getId());
        taskManager.removeEpicById(epic1.getId());

        // === Вывод результатов ===
        printState("Текущее состояние трекера:", taskManager);

        System.out.println("\nИстория просмотров задач:");
        List<Task> history = taskManager.getHistory();
        for (Task t : history) {
            System.out.println(t);
        }
    }

    private static void printState(String header, TaskManager manager) {
        System.out.println("\n=== " + header + " ===");

        System.out.println("Обычные задачи:");
        for (Task t : manager.getAllTasks()) {
            System.out.println(t);
        }

        System.out.println("Эпики:");
        for (Epic e : manager.getAllEpics()) {
            System.out.println(e);
        }

        System.out.println("Подзадачи:");
        for (Subtask s : manager.getAllSubtasks()) {
            System.out.println(s);
        }
    }
}