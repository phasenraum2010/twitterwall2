package org.woehlke.twitterwall.oodm.service.entities;

import org.woehlke.twitterwall.model.HashTagCounted;
import org.woehlke.twitterwall.oodm.entities.entities.HashTag;

import java.util.List;

/**
 * Created by tw on 12.06.17.
 */
public interface HashTagService {
    HashTag store(HashTag tag);

    List<HashTagCounted> getHashTags();

    HashTag update(HashTag tag);

    HashTag findByText(String text);

}