package com.codealpha.taskmanager;

/**
 * Démonstration en ligne de commande du TaskManager.
 * Exécuter avec : ./gradlew run
 */
public class Main {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

        manager.addTask("Configurer le pipeline CI/CD", "Mettre en place GitHub Actions", Task.Priority.HIGH);
        manager.addTask("Écrire la documentation", "README + guide de déploiement", Task.Priority.MEDIUM);
        manager.addTask("Corriger le bug de healthcheck", "Le conteneur Docker ne répond pas", Task.Priority.CRITICAL);
        manager.addTask("Revue de code", null, Task.Priority.LOW);

        manager.completeTask(2);

        System.out.println("=== Toutes les tâches ===");
        manager.getAllTasks().forEach(System.out::println);

        System.out.println("\n=== Tâches en attente (triées par priorité) ===");
        manager.getPendingTasks().forEach(System.out::println);

        System.out.println("\n=== Statistiques ===");
        System.out.println("Répartition par priorité : " + manager.countByPriority());
        System.out.printf("Taux de complétion : %.1f%%%n", manager.completionRate());
    }
}
