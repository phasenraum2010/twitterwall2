package org.woehlke.twitterwall.oodm.entities;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.woehlke.twitterwall.oodm.entities.tasks.TaskSendType;
import org.woehlke.twitterwall.oodm.entities.tasks.TaskStatus;
import org.woehlke.twitterwall.oodm.entities.tasks.TaskType;

import java.util.Date;

public class TweetTest implements DomainObjectMinimalTest  {

    private static final Logger log = LoggerFactory.getLogger(TweetTest.class);

    @Test
    @Override
    public void getUniqueIdTest() throws Exception {
        String descriptionTask = "start: ";
        TaskType type = TaskType.FETCH_TWEETS_FROM_SEARCH;
        TaskSendType taskSendType = TaskSendType.NO_MQ;
        TaskStatus taskStatus = TaskStatus.READY;
        Date timeStarted = new Date();
        Date timeLastUpdate = timeStarted;
        Date timeFinished = null;
        Task task = new Task(descriptionTask,type,taskStatus, taskSendType,timeStarted,timeLastUpdate,timeFinished);

        String msg = "getUniqueIdTest: ";
        Task createdBy = task;
        Task updatedBy = null;
        Long idTwitter = 57646546476L;
        String idStr = idTwitter.toString();
        String text  = "";
        Date createdAt = new Date();
        Tweet tweet = new Tweet(createdBy,updatedBy,idTwitter,idStr,text,createdAt);
        Assert.assertEquals(msg,idTwitter.toString(),tweet.getUniqueId());
    }

    @Test
    @Override
    public void isValidTest() throws Exception {
        String msg = "isValidTest: ";

        String descriptionTask = "start: ";
        TaskType type = TaskType.FETCH_TWEETS_FROM_SEARCH;
        TaskSendType taskSendType = TaskSendType.NO_MQ;
        TaskStatus taskStatus = TaskStatus.READY;
        Date timeStarted = new Date();
        Date timeLastUpdate = timeStarted;
        Date timeFinished = null;
        Task task = new Task(descriptionTask,type,taskStatus, taskSendType,timeStarted,timeLastUpdate,timeFinished);

        Task createdBy = task;
        Task updatedBy = null;
        Long idTwitter = 57646546476L;
        String idStr = idTwitter.toString();
        String text  = "";
        Date createdAt = new Date();
        Tweet tweet = new Tweet(createdBy,updatedBy,idTwitter,idStr,text,createdAt);
        Assert.assertEquals(msg,idTwitter.toString(),tweet.getUniqueId());
        tweet.setIdTwitter(null);
        Assert.assertFalse(msg,tweet.isValid());
    }
}
