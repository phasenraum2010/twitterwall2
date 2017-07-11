package org.woehlke.twitterwall.scheduled.service.transform.entities;

import org.springframework.social.twitter.api.MediaEntity;
import org.woehlke.twitterwall.oodm.entities.User;
import org.woehlke.twitterwall.oodm.entities.entities.Media;
import org.woehlke.twitterwall.scheduled.service.transform.common.TransformService;

import java.util.Set;

/**
 * Created by tw on 28.06.17.
 */
public interface MediaTransformService extends TransformService<Media,MediaEntity> {


    Set<Media> getMediaFor(User user);
}
