package org.woehlke.twitterwall.scheduled.service.transform.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.twitter.api.MentionEntity;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.twitterwall.oodm.entities.parts.EntitiesFilter;
import org.woehlke.twitterwall.oodm.entities.Mention;
import org.woehlke.twitterwall.scheduled.service.transform.MentionTransformService;

import java.util.Set;

/**
 * Created by tw on 28.06.17.
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class MentionTransformServiceImpl extends EntitiesFilter implements MentionTransformService {

    private static final Logger log = LoggerFactory.getLogger(MentionTransformServiceImpl.class);

    @Override
    public Mention transform(MentionEntity mention) {
        long idTwitter = mention.getId();
        String screenName = mention.getScreenName();
        String name = mention.getName();
        int[] indices = mention.getIndices();
        Mention myMentionEntity = new Mention(idTwitter, screenName, name, indices);
        return myMentionEntity;
    }

    @Override
    public Set<Mention> findByUser(TwitterProfile userSource) {
        String description = userSource.getDescription();
        Set<Mention> mentionsTarget = findByUserDescription(description);
        return mentionsTarget;
    }
}