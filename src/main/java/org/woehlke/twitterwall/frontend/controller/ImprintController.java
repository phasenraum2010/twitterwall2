package org.woehlke.twitterwall.frontend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.woehlke.twitterwall.frontend.common.AbstractTwitterwallController;
import org.woehlke.twitterwall.frontend.common.Symbols;
import org.woehlke.twitterwall.frontend.model.Page;
import org.woehlke.twitterwall.oodm.entities.User;
import org.woehlke.twitterwall.oodm.service.UserService;
import org.woehlke.twitterwall.backend.TwitterApiService;
import org.woehlke.twitterwall.scheduled.PersistDataFromTwitter;

import javax.persistence.NoResultException;
import java.util.Date;

/**
 * Created by tw on 14.06.17.
 */
@Controller
public class ImprintController extends AbstractTwitterwallController {

    private static final Logger log = LoggerFactory.getLogger(ImprintController.class);
    
    @Value("${twitterwall.frontend.menu.appname}")
    private String menuAppName;

    @Value("${twitter.searchQuery}")
    private String searchterm;

    @Value("${twitterwall.frontend.info.webpage}")
    private String infoWebpage;

    @Value("${twitterwall.frontend.theme}")
    private String theme;

    @Value("${twitterwall.context.test}")
    private boolean contextTest;
    
    @Value("${twitterwall.frontend.imprint.screenName}")
    private String imprintScreenName;

    @Value("${twitterwall.frontend.imprint.subtitle}")
    private String imprintSubtitle;

    private final TwitterApiService twitterApiService;

    private final PersistDataFromTwitter persistDataFromTwitter;

    @Autowired
    public ImprintController(TwitterApiService twitterApiService, PersistDataFromTwitter persistDataFromTwitter) {
        this.twitterApiService = twitterApiService;
        this.persistDataFromTwitter = persistDataFromTwitter;
    }

    @RequestMapping("/imprint")
    public String imprint(Model model) {
        log.info("-----------------------------------------");
        logEnv();
        String symbol = Symbols.IMPRINT.toString();
        String title = "Impressum";
        String subtitle = imprintSubtitle;
        model = super.setupPage(model, title, subtitle, symbol);
        String screenName = imprintScreenName;
        super.addUserForScreenName(model,screenName);
        log.info("-----------------------------------------");
        return "imprint";
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
        super.setupAfterPropertiesSetWithTesting(twitterApiService,persistDataFromTwitter,menuAppName,searchterm,infoWebpage,theme,contextTest,imprintScreenName);
    }
}
