package org.woehlke.twitterwall.scheduled.service.backend;

import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.TwitterProfile;

import java.util.List;

/**
 * Created by tw on 19.06.17.
 */
public interface TwitterApiService {

    List<Tweet> findTweetsForSearchQuery();

    Tweet findOneTweetById(long id);

    List<TwitterProfile> getUserProfilesForTwitterIds(long... userProfileTwitterIds);

    TwitterProfile getUserProfileForTwitterId(long userProfileTwitterId);

    TwitterProfile getUserProfileForScreenName(String screenName);

    List<TwitterProfile> findUsersFromDefinedList(String screenName,String fetchUserListName);
}
