package nutshell.server.service.task;

import lombok.RequiredArgsConstructor;
import nutshell.server.domain.Task;
import nutshell.server.domain.User;
import nutshell.server.repository.TaskRepository;
import nutshell.server.service.user.UserRetriever;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRetriever taskRetriever;
    private final UserRetriever userRetriever;
    private final TaskRemover taskRemover;

    public void removeTask(final Long userId, final Long taskId){
        User user= userRetriever.findByUserId(userId);
        Task task = taskRetriever.findByUserAndId(user, taskId);
        taskRemover.deleteTask(task);
    }

}
