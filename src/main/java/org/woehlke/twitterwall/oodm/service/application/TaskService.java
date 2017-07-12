package org.woehlke.twitterwall.oodm.service.application;

import org.woehlke.twitterwall.oodm.entities.application.Task;
import org.woehlke.twitterwall.oodm.entities.application.parts.TaskType;
import org.woehlke.twitterwall.oodm.service.common.DomainService;

/**
 * Created by tw on 09.07.17.
 */
public interface TaskService extends DomainService<Task> {

    Task create(String msg,TaskType type);

    Task done(Task task);

    Task error(Task task, Exception e);

    Task error(Task task, Exception e, String msg);

    Task warn(Task task, Exception e);

    Task warn(Task task, Exception e, String msg);

    Task event(Task task, String msg);

    Task findById(long id);

}
