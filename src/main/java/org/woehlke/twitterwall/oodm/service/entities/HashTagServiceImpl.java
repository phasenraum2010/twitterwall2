package org.woehlke.twitterwall.oodm.service.entities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.social.twitter.api.HashTagEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.twitterwall.oodm.entities.User;
import org.woehlke.twitterwall.oodm.entities.common.AbstractFormattedText;
import org.woehlke.twitterwall.oodm.entities.entities.HashTag;
import org.woehlke.twitterwall.oodm.exceptions.oodm.FindHashTagByTextException;
import org.woehlke.twitterwall.oodm.repository.entities.HashTagRepository;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.woehlke.twitterwall.oodm.entities.entities.HashTag.HASHTAG_TEXT_PATTERN;

/**
 * Created by tw on 12.06.17.
 */
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
public class HashTagServiceImpl implements HashTagService {

    private static final Logger log = LoggerFactory.getLogger(HashTagServiceImpl.class);

    private final HashTagRepository hashTagRepository;

    @Autowired
    public HashTagServiceImpl(HashTagRepository hashTagRepository) {
        this.hashTagRepository = hashTagRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public HashTag create(HashTag tag) {
        return this.hashTagRepository.persist(tag);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public HashTag update(HashTag tag) {
        return this.hashTagRepository.update(tag);
    }

    @Override
    public HashTag findByText(String text) {
        return this.hashTagRepository.findByText(text);
    }

    @Override
    public List<HashTag> getAll() {
        return this.hashTagRepository.getAll(HashTag.class);
    }

    @Override
    public long count() {
        return this.hashTagRepository.count(HashTag.class);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public HashTag store(HashTag hashTag) {
        try {
            HashTag tagPers = this.hashTagRepository.findByText(hashTag.getText());
            log.info("found: "+tagPers.toString());
            return tagPers;
            /*
            tagPers.setText(hashTag.getText());
            tagPers.setIndices(hashTag.getIndices());
            return this.hashTagRepository.update(tagPers);
            */
        } catch (EmptyResultDataAccessException e) {
            log.info("try to persist: "+hashTag.toString());
            HashTag tagPers = this.hashTagRepository.persist(hashTag);
            log.info("persisted: "+tagPers.toString());
            return tagPers;
        }
    }
    
}
