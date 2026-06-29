package com.codealpha.taskmanager;

/**
 * Smoke test manuel, sans dépendance externe — utile pour valider la logique
 * rapidement dans un environnement sans accès à Maven Central.
 * Les tests "officiels" et complets sont dans TaskManagerTest.java (JUnit 5),
 * exécutés via `./gradlew test` ou dans la CI GitHub Actions.
 */
public class ManualSmokeTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        testAddTaskIncreasesSize();
        testBlankTitleThrows();
        testCompleteTaskMarksCompleted();
        testCompleteInvalidIdThrows();
        testPendingTasksSortedByPriority();
        testPendingExcludesCompleted();
        testRemoveTask();
        testCompletionRate();

        System.out.println("\n" + passed + " test(s) réussi(s), " + failed + " échoué(s).");
        if (failed > 0) {
            System.exit(1);
        }
    }

    private static void check(String name, boolean condition) {
        if (condition) {
            System.out.println("OK   - " + name);
            passed++;
        } else {
            System.out.println("FAIL - " + name);
            failed++;
        }
    }

    private static void testAddTaskIncreasesSize() {
        TaskManager m = new TaskManager();
        m.addTask("Tache 1", "desc", Task.Priority.MEDIUM);
        check("addTask augmente la taille", m.size() == 1);
    }

    private static void testBlankTitleThrows() {
        TaskManager m = new TaskManager();
        boolean threw = false;
        try {
            m.addTask("", "desc", Task.Priority.LOW);
        } catch (IllegalArgumentException e) {
            threw = true;
        }
        check("titre vide leve une exception", threw);
    }

    private static void testCompleteTaskMarksCompleted() {
        TaskManager m = new TaskManager();
        Task t = m.addTask("Tache", null, Task.Priority.HIGH);
        m.completeTask(t.getId());
        check("completeTask marque la tache comme terminee", m.getTask(t.getId()).isCompleted());
    }

    private static void testCompleteInvalidIdThrows() {
        TaskManager m = new TaskManager();
        boolean threw = false;
        try {
            m.completeTask(999);
        } catch (TaskNotFoundException e) {
            threw = true;
        }
        check("completeTask sur id invalide leve TaskNotFoundException", threw);
    }

    private static void testPendingTasksSortedByPriority() {
        TaskManager m = new TaskManager();
        m.addTask("Basse", null, Task.Priority.LOW);
        m.addTask("Critique", null, Task.Priority.CRITICAL);
        m.addTask("Moyenne", null, Task.Priority.MEDIUM);
        var pending = m.getPendingTasks();
        check("tri par priorite decroissante",
                pending.get(0).getPriority() == Task.Priority.CRITICAL
                        && pending.get(1).getPriority() == Task.Priority.MEDIUM
                        && pending.get(2).getPriority() == Task.Priority.LOW);
    }

    private static void testPendingExcludesCompleted() {
        TaskManager m = new TaskManager();
        Task t1 = m.addTask("Tache 1", null, Task.Priority.HIGH);
        m.addTask("Tache 2", null, Task.Priority.LOW);
        m.completeTask(t1.getId());
        check("taches en attente excluent les taches terminees", m.getPendingTasks().size() == 1);
    }

    private static void testRemoveTask() {
        TaskManager m = new TaskManager();
        Task t = m.addTask("A supprimer", null, Task.Priority.LOW);
        boolean removed = m.removeTask(t.getId());
        check("removeTask retourne true et reduit la taille", removed && m.size() == 0);
    }

    private static void testCompletionRate() {
        TaskManager m = new TaskManager();
        Task t1 = m.addTask("Tache 1", null, Task.Priority.LOW);
        m.addTask("Tache 2", null, Task.Priority.LOW);
        m.completeTask(t1.getId());
        check("taux de completion correct (50%)", Math.abs(m.completionRate() - 50.0) < 0.001);
    }
}
