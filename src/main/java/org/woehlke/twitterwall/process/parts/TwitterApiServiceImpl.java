package org.woehlke.twitterwall.process.parts;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;
import org.woehlke.twitterwall.oodm.exceptions.remote.TwitterApiException;

import java.util.List;

/**
 * Created by tw on 19.06.17.
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class TwitterApiServiceImpl implements TwitterApiService {

    @Value("${twitter.consumerKey}")
    private String twitterConsumerKey;

    @Value("${twitter.consumerSecret}")
    private String twitterConsumerSecret;

    @Value("${twitter.accessToken}")
    private String twitterAccessToken;

    @Value("${twitter.accessTokenSecret}")
    private String twitterAccessTokenSecret;

    @Value("${twitter.pageSize}")
    private int pageSize;

    @Value("${twitterwall.twitter.search.sinceId}")
    private long sinceId;

    @Value("${twitterwall.twitter.search.maxId}")
    private long maxId;

    @Value("${twitter.searchQuery}")
    private String searchQuery;

    private Twitter getTwitterProxy() {
        Twitter twitter = new TwitterTemplate(
                twitterConsumerKey,
                twitterConsumerSecret,
                twitterAccessToken,
                twitterAccessTokenSecret);
        return twitter;
    }

    @Override
    public List<Tweet> findTweetsForSearchQuery() {
        String msg = "findTweetsForSearchQuery: ";
        try {
            return getTwitterProxy().searchOperations().search(searchQuery, pageSize).getTweets();
        } catch (ResourceAccessException e) {
            throw new TwitterApiException(msg + " check your Network Connection!", e);
        }
    }

    @Override
    public Tweet findOneTweetById(long id) {
        String msg = "findOneTweetById: "+id;
        try {
            return getTwitterProxy().timelineOperations().getStatus(id);
        } catch (ResourceAccessException e) {
            throw new TwitterApiException(msg + " check your Network Connection!", e);
        }
    }

    @Override
    public TwitterProfile getUserProfileForTwitterId(long userProfileTwitterId) {
        String msg = "findOneTweetById: "+userProfileTwitterId;
        try {
            return getTwitterProxy().userOperations().getUserProfile(userProfileTwitterId);
        } catch (ResourceAccessException e) {
            throw new TwitterApiException(msg + " check your Network Connection!", e);
        }
    }

    @Override
    public TwitterProfile getUserProfileForScreenName(String screenName) {
        String msg = "getUserProfileForScreenName: "+screenName;
        try {
            return getTwitterProxy().userOperations().getUserProfile(screenName);
        } catch (ResourceAccessException e) {
            throw new TwitterApiException(msg + " check your Network Connection!", e);
        }
    }
}
