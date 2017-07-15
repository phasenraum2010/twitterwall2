package org.woehlke.twitterwall.frontend.rest.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.woehlke.twitterwall.oodm.entities.parts.AbstractTwitterObject;
import org.woehlke.twitterwall.scheduled.service.persist.CountedEntitiesService;

/**
 * Created by tw on 03.07.17.
 */
@Controller
@RequestMapping("/rest/common")
public class CountedEntitiesResource {

    @RequestMapping(path="/count",method= RequestMethod.GET)
    public @ResponseBody
    AbstractTwitterObject.CountedEntities countAll(){
        return this.countedEntitiesService.countAll();
    }


    private final CountedEntitiesService countedEntitiesService;

    @Autowired
    public CountedEntitiesResource(CountedEntitiesService countedEntitiesService) {
        this.countedEntitiesService = countedEntitiesService;
    }
}
