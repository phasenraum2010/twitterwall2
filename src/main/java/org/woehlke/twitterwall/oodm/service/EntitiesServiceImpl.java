package org.woehlke.twitterwall.oodm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.twitterwall.oodm.entities.Entities;
import org.woehlke.twitterwall.oodm.repository.EntitiesRepository;

/**
 * Created by tw on 12.06.17.
 */
@Service
@Transactional(propagation= Propagation.REQUIRED,readOnly = true)
public class EntitiesServiceImpl implements EntitiesService {

    private final EntitiesRepository entitiesRepository;

    @Autowired
    public EntitiesServiceImpl(EntitiesRepository entitiesRepository) {
        this.entitiesRepository = entitiesRepository;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRES_NEW,readOnly = false)
    public Entities store(Entities myEntities) {
        return this.entitiesRepository.persist(myEntities);
    }
}