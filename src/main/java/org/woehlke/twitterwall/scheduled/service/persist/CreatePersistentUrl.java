package org.woehlke.twitterwall.scheduled.service.persist;

import org.woehlke.twitterwall.oodm.entities.Task;
import org.woehlke.twitterwall.oodm.entities.Url;

/**
 * Created by tw on 09.07.17.
 */
public interface CreatePersistentUrl {

    Url getPersistentUrlFor(String url, Task task);
}
