package org.woehlke.twitterwall.scheduled.service.transform.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.twitterwall.oodm.entities.parts.Entities;
import org.woehlke.twitterwall.oodm.entities.User;
import org.woehlke.twitterwall.scheduled.service.transform.EntitiesTransformService;
import org.woehlke.twitterwall.scheduled.service.transform.UserTransformService;

import java.util.Date;

/**
 * Created by tw on 28.06.17.
 */
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
public class UserTransformServiceImpl implements UserTransformService {

    private static final Logger log = LoggerFactory.getLogger(UserTransformServiceImpl.class);


    private final EntitiesTransformService entitiesTransformService;

    @Autowired
    public UserTransformServiceImpl(EntitiesTransformService entitiesTransformService) {
        this.entitiesTransformService = entitiesTransformService;
    }

    @Override
    public User transform(TwitterProfile userSource) {
        String msg = "User.transform for "+userSource.getId();
        long idTwitter = userSource.getId();
        String screenName = userSource.getScreenName();
        String name = userSource.getName();
        String url = userSource.getUrl();
        if (url == null || userSource.getUrl().isEmpty()) {
            url = null;
        }
        String profileImageUrl = userSource.getProfileImageUrl();
        String description = userSource.getDescription();
        if (userSource.getDescription().isEmpty()) {
            description = null;
        }
        String location = userSource.getLocation();
        if (userSource.getLocation().isEmpty()) {
            location = null;
        }
        Date createdDate = userSource.getCreatedDate();
        User userTarget = new User(idTwitter, screenName, name, url, profileImageUrl, description, location, createdDate);
        userTarget.setTweeting(true);
        userTarget.setLanguage(userSource.getLanguage());
        userTarget.setStatusesCount(userSource.getStatusesCount());
        userTarget.setFriendsCount(userSource.getFriendsCount());
        userTarget.setFollowersCount(userSource.getFollowersCount());
        userTarget.setFavoritesCount(userSource.getFavoritesCount());
        userTarget.setListedCount(userSource.getListedCount());
        userTarget.setFollowing(userSource.isFollowing());
        userTarget.setFollowRequestSent(userSource.isFollowRequestSent());
        userTarget.setProtected(userSource.isProtected());
        userTarget.setNotificationsEnabled(userSource.isNotificationsEnabled());
        userTarget.setVerified(userSource.isVerified());
        userTarget.setGeoEnabled(userSource.isGeoEnabled());
        userTarget.setContributorsEnabled(userSource.isContributorsEnabled());
        userTarget.setTranslator(userSource.isTranslator());
        userTarget.setTimeZone(userSource.getTimeZone());
        userTarget.setUtcOffset(userSource.getUtcOffset());
        userTarget.setSidebarBorderColor(userSource.getSidebarBorderColor());
        userTarget.setSidebarFillColor(userSource.getSidebarFillColor());
        userTarget.setBackgroundColor(userSource.getBackgroundColor());
        userTarget.setUseBackgroundImage(userSource.useBackgroundImage());
        userTarget.setBackgroundImageUrl(userSource.getBackgroundImageUrl());
        userTarget.setBackgroundImageTiled(userSource.isBackgroundImageTiled());
        userTarget.setTextColor(userSource.getTextColor());
        userTarget.setLinkColor(userSource.getLinkColor());
        userTarget.setShowAllInlineMedia(userSource.showAllInlineMedia());
        userTarget.setProfileBannerUrl(userSource.getProfileBannerUrl());
        Entities entities = this.entitiesTransformService.transformEntitiesForUser(userSource);
        log.debug(msg+" entities = "+entities.toString());
        userTarget.setEntities(entities);
        log.debug(msg+" userTarget = "+userTarget.toString());
        return userTarget;
    }

}
