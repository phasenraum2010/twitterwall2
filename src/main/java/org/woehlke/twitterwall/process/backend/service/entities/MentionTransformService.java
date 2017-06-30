package org.woehlke.twitterwall.process.backend.service.entities;

import org.springframework.social.twitter.api.MentionEntity;
import org.woehlke.twitterwall.oodm.entities.User;
import org.woehlke.twitterwall.oodm.entities.entities.Mention;
import org.woehlke.twitterwall.process.backend.common.TransformService;

import java.util.Set;

/**
 * Created by tw on 28.06.17.
 */
public interface MentionTransformService extends TransformService<Mention, MentionEntity> {

    Set<Mention> findByUser(User user);
}
