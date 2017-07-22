package org.woehlke.twitterwall.oodm.entities;

import org.hibernate.validator.constraints.SafeHtml;
import org.woehlke.twitterwall.oodm.entities.common.DomainObjectMinimal;
import org.woehlke.twitterwall.oodm.entities.parts.CountedEntities;
import org.woehlke.twitterwall.oodm.entities.parts.TaskStatus;
import org.woehlke.twitterwall.oodm.entities.parts.TaskType;
import org.woehlke.twitterwall.oodm.entities.listener.TaskListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.EAGER;

/**
 * Created by tw on 09.07.17.
 */
@Entity
@Table(
    name = "task",
    uniqueConstraints = {
        @UniqueConstraint(name="unique_task",columnNames = {"task_type","time_started"})
    },
    indexes = {
        @Index(name = "idx_task_time_finished", columnList = "time_finished"),
        @Index(name = "idx_task_task_status", columnList = "task_status"),
        @Index(name = "idx_task_task_type", columnList = "task_type")
    }
)
@NamedQueries({
    @NamedQuery(
        name="Task.findByUniqueId",
        query="select t from Task t where t.taskType=:taskType and t.timeStarted=:timeStarted"
    )
})
@EntityListeners(TaskListener.class)
public class Task implements DomainObjectMinimal<Task> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @SafeHtml
    @NotNull
    @Column(columnDefinition="text",nullable = false)
    private String description = "NULL";

    @NotNull
    @Column(name="task_type",nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskType taskType = TaskType.NULL;

    @NotNull
    @Column(name="task_status",nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus = TaskStatus.READY;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="time_started",nullable = false)
    private Date timeStarted = new Date();

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="time_last_update",nullable = false)
    private Date timeLastUpdate = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="time_finished")
    private Date timeFinished = null;

    @NotNull
    @OneToMany(cascade = {DETACH, REFRESH, REMOVE}, fetch = EAGER,orphanRemoval=true, mappedBy="task")
    private List<TaskHistory> history = new ArrayList<>();

    @NotNull
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "countUser", column = @Column(name = "start_count_user",nullable=false)),
        @AttributeOverride(name = "countTweets", column = @Column(name = "start_count_tweets",nullable=false)),
        @AttributeOverride(name = "countHashTags", column = @Column(name = "start_count_hashtags",nullable=false)),
        @AttributeOverride(name = "countMedia", column = @Column(name = "start_count_media",nullable=false)),
        @AttributeOverride(name = "countMention", column = @Column(name = "start_count_mention",nullable=false)),
        @AttributeOverride(name = "countTickerSymbol", column = @Column(name = "start_count_tickersymbol",nullable=false)),
        @AttributeOverride(name = "countUrl", column = @Column(name = "start_count_url",nullable=false)),
        @AttributeOverride(name = "countUrlCache", column = @Column(name = "start_count_urlcache",nullable=false)),
        @AttributeOverride(name = "countTask", column = @Column(name = "start_count_task",nullable=false)),
        @AttributeOverride(name = "countTaskHistory", column = @Column(name = "start_count_task_history",nullable=false)),
        @AttributeOverride(name = "tweet2hashtag", column = @Column(name = "start_count_tweet2hashtag",nullable=false)),
        @AttributeOverride(name = "tweet2media", column = @Column(name = "start_count_tweet2media",nullable=false)),
        @AttributeOverride(name = "tweet2mention", column = @Column(name = "start_count_tweet2mention",nullable=false)),
        @AttributeOverride(name = "tweet2tickersymbol", column = @Column(name = "start_count_tweet2tickersymbol",nullable=false)),
        @AttributeOverride(name = "tweet2url", column = @Column(name = "start_count_tweet2url",nullable=false)),
        @AttributeOverride(name = "userprofile2hashtag", column = @Column(name = "start_count_userprofile2hashtag",nullable=false)),
        @AttributeOverride(name = "userprofile2media", column = @Column(name = "start_count_userprofile2media",nullable=false)),
        @AttributeOverride(name = "userprofile2mention", column = @Column(name = "start_count_userprofile2mention",nullable=false)),
        @AttributeOverride(name = "userprofile2tickersymbol", column = @Column(name = "start_count_userprofile2tickersymbol",nullable=false)),
        @AttributeOverride(name = "userprofile2url", column = @Column(name = "start_count_userprofile2url",nullable=false))
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

    private Task() {
    }

    public Task(String description,TaskType taskType) {
        this.taskType = taskType;
        this.description = description;
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
    public String getUniqueId() {
        return "" + taskType.name() +"_"+ timeStarted.getTime();
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

    public void setTimeLastUpdate() {
        this.timeLastUpdate = new Date();
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

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public int compareTo(Task other) {
        return getUniqueId().compareTo(other.getUniqueId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;

        Task task = (Task) o;

        if (getId() != null ? !getId().equals(task.getId()) : task.getId() != null) return false;
        if (getTaskType() != task.getTaskType()) return false;
        return getTimeStarted() != null ? getTimeStarted().equals(task.getTimeStarted()) : task.getTimeStarted() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getTaskType() != null ? getTaskType().hashCode() : 0);
        result = 31 * result + (getTimeStarted() != null ? getTimeStarted().hashCode() : 0);
        return result;
    }
}
