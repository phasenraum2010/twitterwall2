package org.woehlke.twitterwall.oodm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.twitterwall.oodm.entities.MyTwitterProfile;
import org.woehlke.twitterwall.oodm.repository.MyTwitterProfileRepository;

import javax.persistence.NoResultException;

/**
 * Created by tw on 11.06.17.
 */
@Service
@Transactional(propagation= Propagation.REQUIRED,readOnly = true)
public class MyTwitterProfileServiceImpl implements MyTwitterProfileService {

    private final MyTwitterProfileRepository myTwitterProfileRepository;

    @Autowired
    public MyTwitterProfileServiceImpl(MyTwitterProfileRepository myTwitterProfileRepository) {
        this.myTwitterProfileRepository = myTwitterProfileRepository;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRES_NEW,readOnly = false)
    public MyTwitterProfile persist(MyTwitterProfile myTwitterProfile) {
        return myTwitterProfileRepository.persist(myTwitterProfile);
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRES_NEW,readOnly = false)
    public MyTwitterProfile update(MyTwitterProfile myTwitterProfile) {
        return myTwitterProfileRepository.update(myTwitterProfile);
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRES_NEW,readOnly = true)
    public MyTwitterProfile findByIdTwitter(long idTwitter) {
        return myTwitterProfileRepository.findByIdTwitter(idTwitter);
    }
}
