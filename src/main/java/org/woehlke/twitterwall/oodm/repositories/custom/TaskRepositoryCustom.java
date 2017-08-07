package org.woehlke.twitterwall.oodm.repositories.custom;

import org.woehlke.twitterwall.oodm.entities.Task;
import org.woehlke.twitterwall.oodm.repositories.common.DomainObjectMinimalRepository;

public interface TaskRepositoryCustom extends DomainObjectMinimalRepository<Task> {

    Task findByUniqueId(Task domainObject);

    void deleteAllDomainData();

}
