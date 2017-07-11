package org.woehlke.twitterwall.scheduled.service.persist.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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

    private final TickerSymbolService tickerSymbolService;

    private final MentionService mentionService;

    private final MediaService mediaService;

    private final HashTagService hashTagService;

    private final TweetService tweetService;

    private final StoreUserProcess storeUserProcess;

    private final CreatePersistentUrl createPersistentUrl;


    @Autowired
    public StoreOneTweetPerformImpl(TickerSymbolService tickerSymbolService, MentionService mentionService, MediaService mediaService, HashTagService hashTagService, TweetService tweetService, StoreUserProcess storeUserProcess, CreatePersistentUrl createPersistentUrl) {
        this.tickerSymbolService = tickerSymbolService;
        this.mentionService = mentionService;
        this.mediaService = mediaService;
        this.hashTagService = hashTagService;
        this.createPersistentUrl = createPersistentUrl;
        this.tweetService = tweetService;
        this.storeUserProcess = storeUserProcess;
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
        Set<Url> urls = new LinkedHashSet<>();
        Set<HashTag> tags = new LinkedHashSet<HashTag>();
        Set<Mention> mentions = new LinkedHashSet<Mention>();
        Set<Media> media = new LinkedHashSet<Media>();
        Set<TickerSymbol> tickerSymbols = new LinkedHashSet<TickerSymbol>();
        for (TickerSymbol tickerSymbol : tweet.getTickerSymbols()) {
            if(tickerSymbol.isValid()){
                tickerSymbols.add(tickerSymbolService.storeTickerSymbol(tickerSymbol,task));
            }
        }
        for (Mention mention : tweet.getMentions()) {
            if(mention.isValid()){
                mentions.add(mentionService.store(mention,task));
            }
        }
        for (Media medium : tweet.getMedia()) {
            if(medium.isValid()){
                media.add(mediaService.store(medium,task));
            }
        }
        for (HashTag hashTag : tweet.getTags()) {
            if(hashTag.isValid()){
                tags.add(hashTagService.store(hashTag,task));
            }
        }
        for (Url url : tweet.getUrls()) {
            if(url == null) {
                log.debug(msg+"tweet.getUrls() -> url==null");
            } else {
                String urlStr = url.getUrl();
                if(urlStr == null){
                    log.debug(msg+"tweet.getUrls() -> url.getUrl() == null");
                } else {
                    Url urlObj = createPersistentUrl.getPersistentUrlFor(urlStr,task);
                    if((urlObj != null)&&(urlObj.isValid())){
                        urls.add(urlObj);
                    } else {
                        log.debug(msg+"urlService.getPersistentUrlFor("+urlStr+") == null");
                    }
                }
            }
        }
        tweet.setUrls(urls);
        tweet.setTags(tags);
        tweet.setMentions(mentions);
        tweet.setMedia(media);
        tweet.setTickerSymbols(tickerSymbols);
        /** Tweet itself */
        tweet = tweetService.store(tweet,task);
        log.debug(msg+"tweetService.store: "+tweet.toString());
        return tweet;
    }
}