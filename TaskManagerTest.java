package com.codealpha.taskmanager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskManagerTest {

    private TaskManager manager;

    @BeforeEach
    void setUp() {
        manager = new TaskManager();
    }

    @Test
    @DisplayName("Ajouter une tâche augmente la taille du gestionnaire")
    void addTask_increasesSize() {
        manager.addTask("Tâche 1", "Description", Task.Priority.MEDIUM);
        assertEquals(1, manager.size());
    }

    @Test
    @DisplayName("Ajouter une tâche avec un titre vide lève une exception")
    void addTask_withBlankTitle_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> manager.addTask("", "Description", Task.Priority.LOW));
    }

    @Test
    @DisplayName("Compléter une tâche met à jour son statut")
    void completeTask_marksAsCompleted() {
        Task task = manager.addTask("Tâche", null, Task.Priority.HIGH);
        manager.completeTask(task.getId());
        assertTrue(manager.getTask(task.getId()).isCompleted());
    }

    @Test
    @DisplayName("Compléter une tâche inexistante lève TaskNotFoundException")
    void completeTask_withInvalidId_throwsException() {
        assertThrows(TaskNotFoundException.class, () -> manager.completeTask(999));
    }

    @Test
    @DisplayName("Les tâches en attente sont triées par priorité décroissante")
    void getPendingTasks_sortedByPriorityDescending() {
        manager.addTask("Basse", null, Task.Priority.LOW);
        manager.addTask("Critique", null, Task.Priority.CRITICAL);
        manager.addTask("Moyenne", null, Task.Priority.MEDIUM);

        List<Task> pending = manager.getPendingTasks();

        assertEquals(Task.Priority.CRITICAL, pending.get(0).getPriority());
        assertEquals(Task.Priority.MEDIUM, pending.get(1).getPriority());
        assertEquals(Task.Priority.LOW, pending.get(2).getPriority());
    }

    @Test
    @DisplayName("Les tâches complétées sont exclues de la liste des tâches en attente")
    void getPendingTasks_excludesCompletedTasks() {
        Task t1 = manager.addTask("Tâche 1", null, Task.Priority.HIGH);
        manager.addTask("Tâche 2", null, Task.Priority.LOW);
        manager.completeTask(t1.getId());

        assertEquals(1, manager.getPendingTasks().size());
    }

    @Test
    @DisplayName("Supprimer une tâche existante retourne true")
    void removeTask_existingTask_returnsTrue() {
        Task task = manager.addTask("À supprimer", null, Task.Priority.LOW);
        assertTrue(manager.removeTask(task.getId()));
        assertEquals(0, manager.size());
    }

    @Test
    @DisplayName("Supprimer une tâche inexistante retourne false")
    void removeTask_nonExistingTask_returnsFalse() {
        assertFalse(manager.removeTask(42));
    }

    @Test
    @DisplayName("Le taux de complétion est calculé correctement")
    void completionRate_calculatedCorrectly() {
        Task t1 = manager.addTask("Tâche 1", null, Task.Priority.LOW);
        manager.addTask("Tâche 2", null, Task.Priority.LOW);
        manager.completeTask(t1.getId());

        assertEquals(50.0, manager.completionRate(), 0.001);
    }

    @Test
    @DisplayName("Le taux de complétion est 0 quand il n'y a aucune tâche")
    void completionRate_withNoTasks_isZero() {
        assertEquals(0.0, manager.completionRate(), 0.001);
    }

    @Test
    @DisplayName("getTasksByPriority filtre correctement")
    void getTasksByPriority_filtersCorrectly() {
        manager.addTask("T1", null, Task.Priority.HIGH);
        manager.addTask("T2", null, Task.Priority.HIGH);
        manager.addTask("T3", null, Task.Priority.LOW);

        assertEquals(2, manager.getTasksByPriority(Task.Priority.HIGH).size());
        assertEquals(1, manager.getTasksByPriority(Task.Priority.LOW).size());
    }
}
