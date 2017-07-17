package org.woehlke.twitterwall.oodm.entities;

import org.woehlke.twitterwall.oodm.entities.parts.CountedEntities;
import org.woehlke.twitterwall.oodm.entities.parts.TaskStatus;
import org.woehlke.twitterwall.oodm.entities.parts.TaskType;
import org.woehlke.twitterwall.oodm.entities.common.DomainObject;
import org.woehlke.twitterwall.oodm.entities.listener.TaskListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tw on 09.07.17.
 */
@Entity
@Table(name = "task", indexes = {
    @Index(name = "idx_task_time_started", columnList = "time_started"),
    @Index(name = "idx_task_time_finished", columnList = "time_finished"),
    @Index(name = "idx_task_task_type", columnList = "task_type"),
    @Index(name = "idx_task_task_status", columnList = "task_status")
})
/*
@NamedQueries({
    @NamedQuery(
        name = "Task.findById",
        query = "select t from Task as t where t.id=:id"
    ),
    @NamedQuery(
        name = "Task.count",
        query = "select count(t) from Task as t"
    ),
    @NamedQuery(
        name = "Task.getAll",
        query = "select t from Task as t"
    )
})*/
@EntityListeners(TaskListener.class)
public class Task implements DomainObject<Task> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @Column(columnDefinition="text")
    private String description;

    @Column(name="task_type",nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskType taskType;

    @Column(name="task_status",nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="time_started",nullable = false)
    private Date timeStarted;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="time_last_update",nullable = true)
    private Date timeLastUpdate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="time_finished",nullable = true)
    private Date timeFinished;

    @OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER,orphanRemoval=true, mappedBy="task")
    private List<TaskHistory> history = new ArrayList<>();

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "countUser", column = @Column(name = "start_count_user")),
        @AttributeOverride(name = "countTweets", column = @Column(name = "start_count_tweets")),
        @AttributeOverride(name = "countHashTags", column = @Column(name = "start_count_hashtags")),
        @AttributeOverride(name = "countMedia", column = @Column(name = "start_count_media")),
        @AttributeOverride(name = "countMention", column = @Column(name = "start_count_mention")),
        @AttributeOverride(name = "countTickerSymbol", column = @Column(name = "start_count_tickersymbol")),
        @AttributeOverride(name = "countUrl", column = @Column(name = "start_count_url")),
        @AttributeOverride(name = "countUrlCache", column = @Column(name = "start_count_urlcache")),
        @AttributeOverride(name = "countTask", column = @Column(name = "start_count_task")),
        @AttributeOverride(name = "countTaskHistory", column = @Column(name = "start_count_task_history")),
        @AttributeOverride(name = "tweet2hashtag", column = @Column(name = "start_count_tweet2hashtag")),
        @AttributeOverride(name = "tweet2media", column = @Column(name = "start_count_tweet2media")),
        @AttributeOverride(name = "tweet2mention", column = @Column(name = "start_count_tweet2mention")),
        @AttributeOverride(name = "tweet2tickersymbol", column = @Column(name = "start_count_tweet2tickersymbol")),
        @AttributeOverride(name = "tweet2url", column = @Column(name = "start_count_tweet2url")),
        @AttributeOverride(name = "userprofile2hashtag", column = @Column(name = "start_count_userprofile2hashtag")),
        @AttributeOverride(name = "userprofile2media", column = @Column(name = "start_count_userprofile2media")),
        @AttributeOverride(name = "userprofile2mention", column = @Column(name = "start_count_userprofile2mention")),
        @AttributeOverride(name = "userprofile2tickersymbol", column = @Column(name = "start_count_userprofile2tickersymbol")),
        @AttributeOverride(name = "userprofile2url", column = @Column(name = "start_count_userprofile2url"))
    })
    private CountedEntities countedEntitiesAtStart;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "countUser", column = @Column(name = "done_count_user")),
        @AttributeOverride(name = "countTweets", column = @Column(name = "done_count_tweets")),
        @AttributeOverride(name = "countHashTags", column = @Column(name = "done_count_hashtags")),
        @AttributeOverride(name = "countMedia", column = @Column(name = "done_count_media")),
        @AttributeOverride(name = "countMention", column = @Column(name = "done_count_mention")),
        @AttributeOverride(name = "countTickerSymbol", column = @Column(name = "done_count_tickersymbol")),
        @AttributeOverride(name = "countUrl", column = @Column(name = "done_count_url")),
        @AttributeOverride(name = "countUrlCache", column = @Column(name = "done_count_urlcache")),
        @AttributeOverride(name = "countTask", column = @Column(name = "done_count_task")),
        @AttributeOverride(name = "countTaskHistory", column = @Column(name = "done_count_task_history")),
        @AttributeOverride(name = "tweet2hashtag", column = @Column(name = "done_count_tweet2hashtag")),
        @AttributeOverride(name = "tweet2media", column = @Column(name = "done_count_tweet2media")),
        @AttributeOverride(name = "tweet2mention", column = @Column(name = "done_count_tweet2mention")),
        @AttributeOverride(name = "tweet2tickersymbol", column = @Column(name = "done_count_tweet2tickersymbol")),
        @AttributeOverride(name = "tweet2url", column = @Column(name = "done_count_tweet2url")),
        @AttributeOverride(name = "userprofile2hashtag", column = @Column(name = "done_count_userprofile2hashtag")),
        @AttributeOverride(name = "userprofile2media", column = @Column(name = "done_count_userprofile2media")),
        @AttributeOverride(name = "userprofile2mention", column = @Column(name = "done_count_userprofile2mention")),
        @AttributeOverride(name = "userprofile2tickersymbol", column = @Column(name = "done_count_userprofile2tickersymbol")),
        @AttributeOverride(name = "userprofile2url", column = @Column(name = "done_count_userprofile2url"))
    })
    private CountedEntities countedEntitiesAtFinish;

    public Task() {
        taskStatus = TaskStatus.READY;
    }

    public Task(String description,TaskType taskType) {
        this.taskType = taskType;
        this.description = description;
        taskStatus = TaskStatus.READY;
    }

    public Task(String description, TaskType taskType, TaskStatus taskStatus, Date timeStarted, Date timeLastUpdate, Date timeFinished, List<TaskHistory> history, CountedEntities countedEntitiesAtStart, CountedEntities countedEntitiesAtFinish) {
        this.description = description;
        this.taskType = taskType;
        this.taskStatus = taskStatus;
        this.timeStarted = timeStarted;
        this.timeLastUpdate = timeLastUpdate;
        this.timeFinished = timeFinished;
        this.history = history;
        this.countedEntitiesAtStart = countedEntitiesAtStart;
        this.countedEntitiesAtFinish = countedEntitiesAtFinish;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Task task) {
        if (this == task) return true;
        if (!id.equals(task.id)) return false;
        if (taskType != task.taskType) return false;
        return timeStarted.equals(task.timeStarted);
    }

    @Override
    public int compareTo(Task other) {
        return id.compareTo(other.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;

        Task task = (Task) o;

        if (!id.equals(task.id)) return false;
        if (taskType != task.taskType) return false;
        return timeStarted.equals(task.timeStarted);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + taskType.hashCode();
        result = 31 * result + timeStarted.hashCode();
        return result;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public Date getTimeStarted() {
        return timeStarted;
    }

    public void setTimeStarted(Date timeStarted) {
        this.timeStarted = timeStarted;
    }

    public Date getTimeFinished() {
        return timeFinished;
    }

    public void setTimeFinished(Date timeFinished) {
        this.timeFinished = timeFinished;
    }

    public CountedEntities getCountedEntitiesAtStart() {
        return countedEntitiesAtStart;
    }

    public void setCountedEntitiesAtStart(CountedEntities countedEntitiesAtStart) {
        this.countedEntitiesAtStart = countedEntitiesAtStart;
    }

    public CountedEntities getCountedEntitiesAtFinish() {
        return countedEntitiesAtFinish;
    }

    public void setCountedEntitiesAtFinish(CountedEntities countedEntitiesAtFinish) {
        this.countedEntitiesAtFinish = countedEntitiesAtFinish;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<TaskHistory> getHistory() {
        return history;
    }

    public void setHistory(List<TaskHistory> history) {
        this.history = history;
    }

    public void addHistory(TaskHistory history) {
        this.history.add(history);
    }

    public Date getTimeLastUpdate() {
        return timeLastUpdate;
    }

    public void setTimeLastUpdate(Date timeLastUpdate) {
        this.timeLastUpdate = timeLastUpdate;
    }

    @Override
    public String toString() {
        String countedEntitiesAtFinishStr = "null";
        if(countedEntitiesAtFinish != null){
            countedEntitiesAtFinishStr = countedEntitiesAtFinish.toString();
        }
        String countedEntitiesAtStartStr = "null";
        if(countedEntitiesAtStart != null){
            countedEntitiesAtStartStr = countedEntitiesAtStart.toString();
        }
        return "Task{" +
            "id=" + id +
            ", taskType=" + taskType +
            ", taskStatus=" +taskStatus +
            ", timeStarted=" + timeStarted +
            ", timeLastUpdate=" + timeLastUpdate +
            ", timeFinished=" + timeFinished +
            ", countedEntitiesAtStart=" + countedEntitiesAtStartStr +
            ", countedEntitiesAtFinish=" + countedEntitiesAtFinishStr +
            '}';
    }

    public void event(String descriptions) {
        TaskHistory event = new TaskHistory(description,taskStatus,taskStatus);
        event.setTask(this);
        this.timeLastUpdate = new Date();
        this.history.add(event);
    }

    public void event(String description,TaskStatus newTaskStatus) {
        TaskHistory event = new TaskHistory(description,taskStatus,newTaskStatus);
        event.setTask(this);
        this.taskStatus = TaskStatus.READY;
        this.timeLastUpdate = new Date();
        this.history.add(event);
    }

    public void ready() {
        TaskHistory event = new TaskHistory("ready",taskStatus,TaskStatus.READY);
        event.setTask(this);
        this.taskStatus = TaskStatus.READY;
        this.timeLastUpdate = new Date();
        this.history.add(event);
    }

    public void start() {
        TaskHistory event = new TaskHistory("start",taskStatus,TaskStatus.RUNNING);
        event.setTask(this);
        this.taskStatus = TaskStatus.RUNNING;
        this.timeLastUpdate = new Date();
        this.timeStarted = new Date();
        this.history.add(event);
    }

    public void done() {
        TaskHistory event = new TaskHistory("done",taskStatus,TaskStatus.FINISHED);
        event.setTask(this);
        this.taskStatus = TaskStatus.FINISHED;
        this.timeLastUpdate = new Date();
        this.timeFinished = new Date();
        this.history.add(event);
    }

    public void error(Exception e) {
        TaskHistory event = new TaskHistory(e.getMessage(),taskStatus,TaskStatus.ERROR);
        event.setTask(this);
        this.taskStatus = TaskStatus.ERROR;
        this.timeLastUpdate = new Date();
        this.history.add(event);
    }

    public void error(Exception e, String msg) {
        TaskHistory event = new TaskHistory(msg+", "+e.getMessage(),taskStatus,TaskStatus.ERROR);
        event.setTask(this);
        this.taskStatus = TaskStatus.ERROR;
        this.timeLastUpdate = new Date();
        this.history.add(event);
    }

    public void warn(Exception e) {
        TaskHistory event = new TaskHistory(e.getMessage(),taskStatus,TaskStatus.WARN);
        event.setTask(this);
        this.taskStatus = TaskStatus.WARN;
        this.timeLastUpdate = new Date();
        this.history.add(event);
    }

    public void warn(Exception e, String msg) {
        TaskHistory event = new TaskHistory(msg+", "+e.getMessage(),taskStatus,TaskStatus.WARN);
        event.setTask(this);
        this.taskStatus = TaskStatus.WARN;
        this.timeLastUpdate = new Date();
        this.history.add(event);
    }

    public void warn(String description) {
        TaskHistory event = new TaskHistory(description,taskStatus,TaskStatus.WARN);
        event.setTask(this);
        this.taskStatus = TaskStatus.READY;
        this.timeLastUpdate = new Date();
        this.history.add(event);
    }

    public void error(String description) {
        TaskHistory event = new TaskHistory(description,taskStatus,TaskStatus.ERROR);
        event.setTask(this);
        this.taskStatus = TaskStatus.READY;
        this.timeLastUpdate = new Date();
        this.history.add(event);
    }

    @Override
    public boolean isValid() {
        return true;
    }
}