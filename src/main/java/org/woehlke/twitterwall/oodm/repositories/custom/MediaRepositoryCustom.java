package org.woehlke.twitterwall.oodm.repositories.custom;

import org.woehlke.twitterwall.oodm.entities.Media;
import org.woehlke.twitterwall.oodm.repositories.common.DomainObjectMinimalRepository;

public interface MediaRepositoryCustom extends DomainObjectMinimalRepository<Media>{

    Media findByUniqueId(Media domainObject);
}