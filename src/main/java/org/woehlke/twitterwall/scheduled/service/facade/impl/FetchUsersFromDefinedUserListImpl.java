package org.woehlke.twitterwall.scheduled.service.facade.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.twitterwall.oodm.entities.application.Task;
import org.woehlke.twitterwall.oodm.entities.application.parts.TaskType;
import org.woehlke.twitterwall.oodm.entities.entities.Mention;
import org.woehlke.twitterwall.oodm.service.application.TaskService;
import org.woehlke.twitterwall.scheduled.service.backend.TwitterApiService;
import org.woehlke.twitterwall.oodm.entities.User;
import org.woehlke.twitterwall.scheduled.service.facade.FetchUsersFromDefinedUserList;
import org.woehlke.twitterwall.scheduled.service.persist.StoreUserProfileForScreenName;
import org.woehlke.twitterwall.scheduled.service.persist.StoreUserProfileForUserList;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by tw on 09.07.17.
 */
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
public class FetchUsersFromDefinedUserListImpl implements FetchUsersFromDefinedUserList {

    private static final Logger log = LoggerFactory.getLogger(FetchUsersFromDefinedUserListImpl.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Value("${twitterwall.backend.twitter.millisToWaitForFetchTweetsFromTwitterSearch}")
    private int millisToWaitForFetchTweetsFromTwitterSearch;

    @Value("${twitterwall.scheduler.fetchUserList.name}")
    private String fetchUserListName;

    @Value("${twitterwall.frontend.imprint.screenName}")
    private String imprintScreenName;

    private final StoreUserProfileForUserList storeUserProfileForUserList;

    private final TwitterApiService twitterApiService;

    private final TaskService taskService;

    private final StoreUserProfileForScreenName storeUserProfileForScreenName;

    @Autowired
    public FetchUsersFromDefinedUserListImpl(StoreUserProfileForUserList storeUserProfileForUserList, TwitterApiService twitterApiService, TaskService taskService, StoreUserProfileForScreenName storeUserProfileForScreenName) {
        this.storeUserProfileForUserList = storeUserProfileForUserList;
        this.twitterApiService = twitterApiService;
        this.taskService = taskService;
        this.storeUserProfileForScreenName = storeUserProfileForScreenName;
    }

    @Override
    public void fetchUsersFromDefinedUserList() {
        String msg = "update Tweets: ";
        Task task = taskService.create(msg, TaskType.FETCH_USERS_FROM_DEFINED_USER_LIST);
        log.debug(msg + "---------------------------------------");
        log.debug(msg + "START The time is now {}", dateFormat.format(new Date()));
        log.debug(msg + "---------------------------------------");
        List<TwitterProfile> userProfiles = twitterApiService.findUsersFromDefinedList(imprintScreenName,fetchUserListName);
        for(TwitterProfile twitterProfile:userProfiles) {
            User user = storeUserProfileForUserList.storeUserProfileForUserList(twitterProfile,task);
            for(Mention mention:user.getMentions()){
                try {
                    User userFromMention = storeUserProfileForScreenName.storeUserProfileForScreenName(mention.getScreenName(),task);
                    log.debug(msg+userFromMention.toString());
                } catch (IllegalArgumentException exe){
                    log.debug(msg+exe.getMessage());
                }
            }
        }
        taskService.done(task);
        log.debug(msg + "---------------------------------------");
        log.debug(msg + "DONE The time is now {}", dateFormat.format(new Date()));
        log.debug(msg + "---------------------------------------");
    }
}
