package org.woehlke.twitterwall.scheduled.service.persist;

import org.springframework.social.twitter.api.TwitterProfile;
import org.woehlke.twitterwall.oodm.entities.User;

/**
 * Created by tw on 09.07.17.
 */
public interface StoreUserProfileForUserList {

    User storeUserProfileForUserList(TwitterProfile twitterProfile);
}