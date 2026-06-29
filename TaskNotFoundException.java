package com.codealpha.taskmanager;

/**
 * Exception levée lorsqu'une opération référence une tâche inexistante.
 */
public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(int taskId) {
        super("Aucune tâche trouvée avec l'identifiant : " + taskId);
    }
}
