package org.woehlke.twitterwall.scheduled;

import org.springframework.social.twitter.api.TwitterProfile;
import org.woehlke.twitterwall.frontend.model.CountedEntities;
import org.woehlke.twitterwall.oodm.entities.Tweet;
import org.woehlke.twitterwall.oodm.entities.User;

/**
 * Created by tw on 11.06.17.
 */
public interface PersistDataFromTwitter {

    Tweet storeOneTweet(org.springframework.social.twitter.api.Tweet tweet);

    User storeUserProfile(TwitterProfile userProfile);

    User storeUserProfileForUserList(TwitterProfile twitterProfile);

    long countTweets();

    long countUsers();

    User findUserByScreenName(String screenName);

    CountedEntities countAll();
}
