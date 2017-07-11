package org.woehlke.twitterwall.scheduled.service.persist.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.twitterwall.oodm.entities.Entities;
import org.woehlke.twitterwall.oodm.entities.Tweet;
import org.woehlke.twitterwall.oodm.entities.User;
import org.woehlke.twitterwall.oodm.entities.application.Task;
import org.woehlke.twitterwall.oodm.entities.entities.*;
import org.woehlke.twitterwall.oodm.service.TweetService;
import org.woehlke.twitterwall.oodm.service.entities.*;
import org.woehlke.twitterwall.scheduled.service.persist.*;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by tw on 09.07.17.
 */
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
public class StoreOneTweetPerformImpl implements StoreOneTweetPerform {

    private static final Logger log = LoggerFactory.getLogger(StoreOneTweetPerformImpl.class);

    private final TweetService tweetService;

    private final StoreUserProcess storeUserProcess;

    private final StoreEntitiesProcess storeEntitiesProcess;

    @Autowired
    public StoreOneTweetPerformImpl(TweetService tweetService, StoreUserProcess storeUserProcess, StoreEntitiesProcess storeEntitiesProcess) {
        this.tweetService = tweetService;
        this.storeUserProcess = storeUserProcess;
        this.storeEntitiesProcess = storeEntitiesProcess;
    }

    /** Method because of recursive Method Call in this Method **/
    public Tweet storeOneTweetPerform(Tweet tweet, Task task){
        String msg = "storeOneTweetPerform( idTwitter="+tweet.getIdTwitter()+" ) ";
        /** Retweeted Tweet */
        Tweet retweetedStatus = tweet.getRetweetedStatus();
        if (retweetedStatus != null) {
            /** Method because of recursive Method Call in this Method **/
            retweetedStatus = this.storeOneTweetPerform(retweetedStatus, task);
            tweet.setRetweetedStatus(retweetedStatus);
        }
        /** The User */
        User user = tweet.getUser();
        user.setOnDefinedUserList(false);
        user = storeUserProcess.storeUserProcess(user,task);
        tweet.setUser(user);
        /** The Entities */
        Entities entities = tweet.getEntities();
        entities = storeEntitiesProcess.storeEntitiesProcess(entities,task,null);
        tweet.setEntities(entities);
        /** Tweet itself */
        tweet = tweetService.store(tweet,task);
        log.debug(msg+"tweetService.store: "+tweet.toString());
        return tweet;
    }
}
