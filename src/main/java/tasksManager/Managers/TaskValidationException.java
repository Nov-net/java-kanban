package tasksManager.Managers;

import tasksManager.Managers.ManagerSaveExeption;

public class TaskValidationException extends Throwable {
    public TaskValidationException(ManagerSaveExeption e) {
    }
}
