package org.woehlke.twitterwall.scheduled.mq.endpoint.tweets.splitter.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.stereotype.Component;
import org.woehlke.twitterwall.oodm.model.Task;
import org.woehlke.twitterwall.oodm.model.parts.CountedEntities;
import org.woehlke.twitterwall.oodm.service.CountedEntitiesService;
import org.woehlke.twitterwall.oodm.service.TaskService;
import org.woehlke.twitterwall.scheduled.mq.endpoint.tweets.splitter.FavoritesSplitter;
import org.woehlke.twitterwall.scheduled.mq.msg.TaskMessage;
import org.woehlke.twitterwall.scheduled.mq.msg.TweetMessage;
import org.woehlke.twitterwall.scheduled.mq.msg.builder.TweetMessageBuilder;
import org.woehlke.twitterwall.scheduled.service.remote.TwitterApiService;

import java.util.ArrayList;
import java.util.List;

@Component("mqFavoritesSplitter")
public class FavoritesSplitterImpl implements FavoritesSplitter {

    private final TwitterApiService twitterApiService;

    private final TaskService taskService;

    private final CountedEntitiesService countedEntitiesService;

    private final TweetMessageBuilder tweetMessageBuilder;

    @Autowired
    public FavoritesSplitterImpl(TwitterApiService twitterApiService, TaskService taskService, CountedEntitiesService countedEntitiesService, TweetMessageBuilder tweetMessageBuilder) {
        this.twitterApiService = twitterApiService;
        this.taskService = taskService;
        this.countedEntitiesService = countedEntitiesService;
        this.tweetMessageBuilder = tweetMessageBuilder;
    }

    @Override
    public List<Message<TweetMessage>> splitTweetMessage(Message<TaskMessage> message) {
        CountedEntities countedEntities = countedEntitiesService.countAll();
        List<Message<TweetMessage>> tweets = new ArrayList<>();
        TaskMessage msgIn = message.getPayload();
        long id = msgIn.getTaskId();
        Task task = taskService.findById(id);
        task =  taskService.start(task,countedEntities);
        List<Tweet> twitterTweets = twitterApiService.getFavorites();
        int loopId = 0;
        int loopAll = twitterTweets.size();
        for (Tweet tweet: twitterTweets) {
            loopId++;
            Message<TweetMessage> mqMessageOut = tweetMessageBuilder.buildTweetMessage(message,tweet,loopId,loopAll);
            tweets.add(mqMessageOut);
        }
        return tweets;
    }
}
