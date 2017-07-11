package org.woehlke.twitterwall.scheduled.service.persist.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.twitterwall.oodm.entities.User;
import org.woehlke.twitterwall.oodm.entities.application.Task;
import org.woehlke.twitterwall.oodm.entities.entities.Mention;
import org.woehlke.twitterwall.scheduled.service.persist.StoreUserFromMention;
import org.woehlke.twitterwall.scheduled.service.persist.StoreUserProfileForScreenName;

/**
 * Created by tw on 11.07.17.
 */
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
public class StoreUserFromMentionImpl implements StoreUserFromMention {

    private static final Logger log = LoggerFactory.getLogger(StoreUserProfileImpl.class);

    private final StoreUserProfileForScreenName storeUserProfileForScreenName;

    @Autowired
    public StoreUserFromMentionImpl(StoreUserProfileForScreenName storeUserProfileForScreenName1) {
        this.storeUserProfileForScreenName = storeUserProfileForScreenName1;
    }

    @Override
    public User storeUserFromMention(User user, Task task) {
        String msg = "storeUserFromMention: ";
        for(Mention mention:user.getMentions()){
            String screenName = mention.getScreenName();
            User userFromMention = storeUserProfileForScreenName.storeUserProfileForScreenName(screenName, task);
            log.debug(msg + " userFromScreenName: "+userFromMention.toString());
        }
        return user;
    }
}