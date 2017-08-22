package org.woehlke.twitterwall.backend.service.persist;

import org.woehlke.twitterwall.oodm.model.Tweet;
import org.woehlke.twitterwall.oodm.model.Task;

/**
 * Created by tw on 09.07.17.
 */
public interface StoreOneTweetPerform {

    Tweet storeOneTweetPerform(Tweet tweet, Task task);

}
