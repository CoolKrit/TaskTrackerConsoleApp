import enums.Status;
import model.Epic;
import model.Subtask;
import model.Task;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();

        // === Создание обычных задач ===
        Task task1 = new Task("Переезд", "Переезд в новую квартиру");
        Task task2 = new Task("Покупка",  "Купить продукты");
        manager.createTask(task1);
        manager.createTask(task2);

        // === Создание эпиков и подзадач ===
        Epic epic1 = new Epic("Ремонт", "Ремонт квартиры");
        manager.createEpic(epic1);
        Subtask sub1 = new Subtask(epic1.getId(), "Купить краску", "Для стен");
        Subtask sub2 = new Subtask(epic1.getId(), "Покрасить стены", "Белой краской");
        manager.createSubtask(sub1);
        manager.createSubtask(sub2);

        Epic epic2 = new Epic("Отпуск", "Организация отпуска");
        manager.createEpic(epic2);
        Subtask sub3 = new Subtask(epic2.getId(), "Забронировать отель", "Через сайт");
        manager.createSubtask(sub3);

        // === Вывод всех сущностей ===
        printState("Создано:", manager);

        // === Обновление задачи целиком ===
        Task updatedTask = new Task(task1.getId(), "Переезд", "Переезд в другой город", Status.IN_PROGRESS);
        manager.updateTask(updatedTask);

        // === Обновление подзадачи и статуса эпика ===
        sub1.setStatus(Status.DONE);
        manager.updateSubtask(sub1);
        sub2.setStatus(Status.IN_PROGRESS);
        manager.updateSubtask(sub2);

        // === Перемещение подзадачи sub2 из epic1 в epic2 ===
        manager.updateSubtask(new Subtask(sub1.getId(), epic2.getId(), sub1.getName(), sub1.getDescription(), Status.NEW));
        manager.updateSubtask(new Subtask(sub2.getId(), epic2.getId(), sub2.getName(), sub2.getDescription(), Status.IN_PROGRESS));

        printState("После обновлений и переноса подзадачи:", manager);

        // === Получение подзадач конкретного эпика ===
        System.out.println("Подзадачи Epic1:");
        for (Subtask s : manager.getSubtasksByEpicId(epic1.getId())) {
            System.out.println(s);
        }

        System.out.println("Подзадачи Epic2:");
        for (Subtask s : manager.getSubtasksByEpicId(epic2.getId())) {
            System.out.println(s);
        }

        // === Удаление задачи и эпика ===
        manager.removeTaskById(task2.getId());
        manager.removeEpicById(epic1.getId()); // удаляет и sub1

        printState("После удаления задачи и эпика:", manager);
    }

    private static void printState(String header, Manager manager) {
        System.out.println("\n=== " + header + " ===");

        System.out.println("Задачи:");
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