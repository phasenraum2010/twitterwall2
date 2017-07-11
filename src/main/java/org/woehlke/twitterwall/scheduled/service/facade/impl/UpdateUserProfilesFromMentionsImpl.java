package org.woehlke.twitterwall.scheduled.service.facade.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.RateLimitExceededException;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;
import org.woehlke.twitterwall.oodm.entities.application.Task;
import org.woehlke.twitterwall.oodm.entities.application.parts.TaskType;
import org.woehlke.twitterwall.oodm.service.application.TaskService;
import org.woehlke.twitterwall.scheduled.service.backend.TwitterApiService;
import org.woehlke.twitterwall.oodm.entities.User;
import org.woehlke.twitterwall.oodm.entities.entities.Mention;
import org.woehlke.twitterwall.oodm.service.entities.MentionService;
import org.woehlke.twitterwall.scheduled.service.facade.UpdateUserProfilesFromMentions;
import org.woehlke.twitterwall.scheduled.service.persist.CountedEntitiesService;
import org.woehlke.twitterwall.scheduled.service.persist.StoreUserProfile;
import org.woehlke.twitterwall.scheduled.service.persist.StoreUserProfileForScreenName;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by tw on 09.07.17.
 */
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
public class UpdateUserProfilesFromMentionsImpl implements UpdateUserProfilesFromMentions {


    private static final Logger log = LoggerFactory.getLogger(UpdateUserProfilesFromMentionsImpl.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Value("${twitterwall.backend.twitter.millisToWaitBetweenTwoApiCalls}")
    private int millisToWaitBetweenTwoApiCalls;

    @Value("${twitterwall.scheduler.fetchUserList.name}")
    private String fetchUserListName;

    @Value("${twitterwall.frontend.imprint.screenName}")
    private String imprintScreenName;

    private final StoreUserProfile storeUserProfile;

    private final TwitterApiService twitterApiService;

    private final MentionService mentionService;

    private final TaskService taskService;

    private final StoreUserProfileForScreenName storeUserProfileForScreenName;

    @Autowired
    public UpdateUserProfilesFromMentionsImpl(TwitterApiService twitterApiService, StoreUserProfile storeUserProfile, MentionService mentionService, CountedEntitiesService countedEntitiesService, TaskService taskService, StoreUserProfileForScreenName storeUserProfileForScreenName) {
        this.twitterApiService = twitterApiService;
        this.storeUserProfile = storeUserProfile;
        this.mentionService = mentionService;
        this.taskService = taskService;
        this.storeUserProfileForScreenName = storeUserProfileForScreenName;
    }

    @Override
    public void updateUserProfilesFromMentions(){
        String msg = "update User Profiles from Mentions: ";
        log.debug(msg + "START - The time is now {}", dateFormat.format(new Date()));
        Task task = this.taskService.create(msg, TaskType.UPDATE_USER_PROFILES_FROM_MENTIONS);
        try {
            List<Mention> allPersMentions =  mentionService.getAll();
            for(Mention onePersMentions :allPersMentions){
                String screenName = onePersMentions.getScreenName();
                if((screenName != null) && (!screenName.isEmpty())) {
                    try {
                        TwitterProfile twitterProfile = this.twitterApiService.getUserProfileForScreenName(screenName);
                        User user = storeUserProfile.storeUserProfile(twitterProfile,task);
                        for(Mention mention:user.getMentions()){
                            try {
                                User userFromMention = storeUserProfileForScreenName.storeUserProfileForScreenName(mention.getScreenName(),task);
                                log.debug(msg+userFromMention.toString());
                            } catch (IllegalArgumentException exe){
                                log.debug(msg+exe.getMessage());
                            }
                        }
                        log.debug(msg + user.toString());
                        log.debug(msg + "-----------------------------------------------------");
                        log.debug(msg + "Start SLEEP for "+millisToWaitBetweenTwoApiCalls+" ms");
                        Thread.sleep(millisToWaitBetweenTwoApiCalls);
                        log.debug(msg + "Done SLEEP for "+millisToWaitBetweenTwoApiCalls+" ms");
                        log.debug(msg + "-----------------------------------------------------");
                    } catch (RateLimitExceededException e) {
                        log.warn(msg + e.getMessage());
                        Throwable t = e.getCause();
                        while(t != null){
                            log.warn(msg + t.getMessage());
                            t = t.getCause();
                        }
                        throw e;
                    } catch (InterruptedException ex){
                        log.warn(msg + ex.getMessage());
                        Throwable t = ex.getCause();
                        while(t != null){
                            log.warn(msg + t.getMessage());
                            t = t.getCause();
                        }
                    } finally {
                        log.debug(msg +"---------------------------------------");
                    }
                }
            }
        } catch (ResourceAccessException e) {
            log.error(msg + " ResourceAccessException: " + e.getMessage());
            Throwable t = e.getCause();
            while(t != null){
                log.warn(msg + " caused by: "+ t.getMessage());
                t = t.getCause();
            }
            log.error(msg + " check your Network Connection!");
            task = taskService.error(task,e);
            throw e;
        } catch (RateLimitExceededException e) {
            log.error(msg + " RateLimitExceededException: " + e.getMessage());
            task = taskService.error(task,e);
            throw e;
        } finally {
            this.taskService.done(task);
        }
        log.debug(msg +"---------------------------------------");
        log.debug(msg + "DONE - The time is now {}", dateFormat.format(new Date()));
        log.debug(msg+"---------------------------------------");
    }
}