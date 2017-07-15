package org.woehlke.twitterwall.oodm.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.woehlke.twitterwall.oodm.entities.Task;
import org.woehlke.twitterwall.oodm.entities.TaskHistory;
import org.woehlke.twitterwall.oodm.dao.TaskHistoryDao;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by tw on 11.07.17.
 */
@Repository
public class TaskHistoryDaoImpl extends DomainDaoImpl<TaskHistory> implements TaskHistoryDao {

    private static final Logger log = LoggerFactory.getLogger(TaskHistoryDaoImpl.class);

    @Override
    public TaskHistory findById(long id) {
        String name = "TaskHistory.findById";
        log.debug(name);
        try {
            TypedQuery<TaskHistory> query = entityManager.createNamedQuery(name, TaskHistory.class);
            query.setParameter("id", id);
            TaskHistory result = query.getSingleResult();
            log.debug(name+" found: " + id);
            return result;
        } catch (EmptyResultDataAccessException e) {
            log.debug(name+" not found: " + id);
            throw e;
        }
    }

    @Override
    public Page<TaskHistory> findByTask(Task oneTask, Pageable pageRequest) {
        String name = "TaskHistory.findByTask";
        log.debug(name);
        try {
            TypedQuery<TaskHistory> query = entityManager.createNamedQuery(name, TaskHistory.class);
            query.setParameter("oneTaskId", oneTask.getId());
            long total = query.getResultList().size();
            query.setMaxResults(pageRequest.getPageSize());
            query.setFirstResult(pageRequest.getOffset());
            List<TaskHistory> result = query.getResultList();
            Page resultPage = new PageImpl(result,pageRequest,total);
            log.debug(name+" found: " + result.size());
            return resultPage;
        } catch (EmptyResultDataAccessException e) {
            log.debug(name+" not found: " + oneTask.toString());
            throw e;
        }
    }
}
