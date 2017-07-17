package org.woehlke.twitterwall.frontend.controller.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.social.RateLimitExceededException;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.woehlke.twitterwall.oodm.entities.Task;
import org.woehlke.twitterwall.oodm.entities.parts.TaskType;
import org.woehlke.twitterwall.oodm.service.TaskService;
import org.woehlke.twitterwall.scheduled.service.backend.TwitterApiService;
import org.woehlke.twitterwall.frontend.model.Page;
import org.woehlke.twitterwall.oodm.entities.Tweet;
import org.woehlke.twitterwall.oodm.entities.User;
import org.woehlke.twitterwall.oodm.service.UserService;
import org.woehlke.twitterwall.scheduled.service.persist.StoreOneTweet;
import org.woehlke.twitterwall.scheduled.service.persist.StoreUserProfile;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tw on 01.07.17.
 */
public abstract class AbstractTwitterwallController implements InitializingBean {

    public final static int FIRST_PAGE_NUMBER = 0;

    public final static long[] ID_TWITTER_TO_FETCH_FOR_TWEET_TEST = {
            876329508009279488L,
            876356335784394752L,
            876676270913880066L,
            876566077836337152L,
            876563676395962368L,
            876514968933478400L,
            876514568671023104L,
            876513930478313472L,
            876510758632386563L,
            876496934676180992L
    };
    public final static long[] ID_TWITTER_TO_FETCH_FOR_PROFILE_CONTROLLER_TEST = {
            876433563561938944L, // t3c_berlin
            876456051016597504L, // Codemonkey1988
            876420491329892354L, // Walter_kran
            876425220147564544L, // tobenschmidt
            876819177746649088L, // Oliver1973
            876514968933478400L, // wowa_TYPO3
            876441863191920641L, // dirscherl17
            876441015523192832L, // Markus306
            876440419416109056L  // mattLefaux
    };

    private static final Logger log = LoggerFactory.getLogger(AbstractTwitterwallController.class);

    private TwitterApiService twitterApiService;

    private StoreOneTweet storeOneTweet;

    private StoreUserProfile storeUserProfile;

    private UserService userService;

    private TaskService taskService;

    private String menuAppName;

    private String searchterm;

    private String infoWebpage;

    private String theme;

    private boolean contextTest;

    private String imprintScreenName;

    private String idGoogleAnalytics;

    protected void logEnv(){
        log.info("--------------------------------------------------------------------");
        log.info("twitterwall.frontend.menu.appname = "+menuAppName);
        log.info("twitter.searchQuery = "+searchterm);
        log.info("twitterwall.frontend.info.webpage = "+infoWebpage);
        log.info("twitterwall.frontend.theme = "+theme);
        log.info("twitterwall.context.test = "+contextTest);
        log.info("twitterwall.frontend.imprint.screenName = "+imprintScreenName);
        log.info("twitterwall.frontend.idGoogleAnalytics = "+idGoogleAnalytics);
        log.info("--------------------------------------------------------------------");
    }

    protected ModelAndView setupPage(ModelAndView mav, String title, String subtitle, String symbol) {
        Page page = new Page();
        page = setupPage(page, title, subtitle, symbol);
        log.info("page: "+page.toString());
        mav.addObject("page", page);
        return mav;
    }

    protected Model setupPage(Model model, String title, String subtitle, String symbol) {
        Page page = new Page();
        page = setupPage(page, title, subtitle, symbol);
        log.info("page: "+page.toString());
        model.addAttribute("page", page);
        return model;
    }

    private Page setupPage(Page page,String title, String subtitle, String symbol)  {
        page.setTitle(title);
        page.setSubtitle(subtitle);
        page.setSymbol(symbol);
        page.setMenuAppName(menuAppName);
        page.setTwitterSearchTerm(searchterm);
        page.setInfoWebpage(infoWebpage);
        page.setTheme(theme);
        page.setContextTest(contextTest);
        page.setHistoryBack(true);
        if(!idGoogleAnalytics.isEmpty()){
            String html = GOOGLE_ANALYTICS_SCRIPT_HTML;
            html = html.replace("###GOOGLE_ANALYTICS_ID###",idGoogleAnalytics);
            page.setGoogleAnalyticScriptHtml(html);
        } else {
            page.setGoogleAnalyticScriptHtml("");
        }
        logEnv();
        log.info("--------------------------------------------------------------------");
        log.info("setupPage = "+page.toString());
        log.info("--------------------------------------------------------------------");
        return page;
    }

    protected void getTestDataTweets(String msg,Model model){
        Task task = taskService.create(msg, TaskType.CONTROLLER_GET_TESTDATA_TWEETS);
        List<Tweet> latest =  new ArrayList<>();
        try {
            log.info(msg + "--------------------------------------------------------------------");
            int loopId = 0;
            for (long idTwitter : ID_TWITTER_TO_FETCH_FOR_TWEET_TEST) {
                try {
                    org.springframework.social.twitter.api.Tweet tweet = twitterApiService.findOneTweetById(idTwitter);
                    loopId++;
                    log.info(msg + loopId);
                    org.woehlke.twitterwall.oodm.entities.Tweet persTweet = this.storeOneTweet.storeOneTweet(tweet, task);
                    log.info(msg + "--------------------------------------------------------------------");
                    log.info(msg + persTweet.toString());
                    log.info(msg + "--------------------------------------------------------------------");
                    latest.add(persTweet);
                } catch (EmptyResultDataAccessException e) {
                    log.warn(msg + e.getMessage());
                } catch (NoResultException e) {
                    log.warn(msg + e.getMessage());
                }
            }
        } catch (RateLimitExceededException e) {
            log.info(msg + e.getMessage());
        } catch (Exception e) {
            log.warn(msg + e.getMessage());
        } finally {
            log.info(msg + "--------------------------------------------------------------------");
        }
        int numberOfTwweets = latest.size();
        if(numberOfTwweets > 0 ) {
            Pageable pageRequest = new PageRequest(0, numberOfTwweets);
            org.springframework.data.domain.Page<Tweet> result = new PageImpl(latest, pageRequest, numberOfTwweets);
            model.addAttribute("latestTweets", result);
        } else {
            model.addAttribute("latestTweets",null);
        }
    }

    protected void getTestDataUser(String msg,Model model){
        Task task = taskService.create(msg, TaskType.CONTROLLER_GET_TESTDATA_USER);
        List<org.woehlke.twitterwall.oodm.entities.User> user =  new ArrayList<>();
        try {
            int loopId = 0;
            for (long idTwitter : ID_TWITTER_TO_FETCH_FOR_PROFILE_CONTROLLER_TEST) {
                try {
                    TwitterProfile twitterProfile = twitterApiService.getUserProfileForTwitterId(idTwitter);
                    loopId++;
                    log.info(msg + loopId);
                    org.woehlke.twitterwall.oodm.entities.User persUser = this.storeUserProfile.storeUserProfile(twitterProfile,task);
                    user.add(persUser);
                } catch (EmptyResultDataAccessException e) {
                    log.warn(msg + e.getMessage());
                } catch (NoResultException e) {
                    log.warn(msg + e.getMessage());
                }
            }
        } catch (RateLimitExceededException e) {
            log.info(msg + e.getMessage());
        } catch (Exception e) {
            log.warn(msg + e.getMessage());
        }
        taskService.done(task);
        model.addAttribute("user", user);

        int numberOfUser = user.size();
        if(numberOfUser > 0) {
            Pageable pageRequest = new PageRequest(0, numberOfUser);
            org.springframework.data.domain.Page<org.woehlke.twitterwall.oodm.entities.User> result = new PageImpl(user, pageRequest, numberOfUser);
            model.addAttribute("user", result);
        } else {
            model.addAttribute("user",null);
        }
    }

    protected void addUserForScreenName(Model model, String screenName) {
        String msg="addUserForScreenName "+screenName+": ";
        Task task = taskService.create(msg, TaskType.CONTROLLER_ADD_USER_FOR_SCREEN_NAME);
        log.info("--------------------------------------------------------------------");
        log.info("screenName = "+ screenName);
        User user = userService.findByScreenName(screenName);
        if(user!=null){
            log.info("userService.findByScreenName: found User = "+user.toString());
            model.addAttribute("user", user);
            log.info("model.addAttribute user = "+user.toString());
        } else {
            String msg2 = "EmptyResultDataAccessException at userService.findByScreenName for screenName="+screenName;
            task = taskService.warn(task,msg2);
            TwitterProfile twitterProfile = twitterApiService.getUserProfileForScreenName(screenName);
            String msg3 = "twitterApiService.getUserProfileForScreenName: found TwitterProfile = "+twitterProfile.toString();
            task = taskService.event(task,msg3);
            log.info("try: persistDataFromTwitter.storeUserProfile for twitterProfile = "+twitterProfile.toString());
            User user2 = storeUserProfile.storeUserProfile(twitterProfile,task);
            if(user2!=null){
                log.info("persistDataFromTwitter.storeUserProfile: stored User = "+user2.toString());
                model.addAttribute("user", user2);
                log.info("model.addAttribute user = "+user2.toString());
            } else {
                log.warn("persistDataFromTwitter.storeUserProfile raised EmptyResultDataAccessException: ");
                User user3 = User.getDummyUserForScreenName(screenName);
                model.addAttribute("user", user3);
                log.info("model.addAttribute user = "+user3.toString());
            }
        }
        taskService.done(task);
        log.info("... finally done ...");
        log.info("--------------------------------------------------------------------");
    }

    protected void setupAfterPropertiesSetWithTesting(TaskService taskService, TwitterApiService twitterApiService, StoreOneTweet storeOneTweet, StoreUserProfile storeUserProfile, UserService userService, String menuAppName, String searchterm, String infoWebpage, String theme, boolean contextTest ,String imprintScreenName,String idGoogleAnalytics) {
        this.twitterApiService = twitterApiService;
        this.storeOneTweet = storeOneTweet;
        this.storeUserProfile = storeUserProfile;
        this.userService = userService;
        this.taskService=taskService;
        this.setupAfterPropertiesSet(menuAppName, searchterm, infoWebpage, theme, contextTest, imprintScreenName, idGoogleAnalytics);
    }

    protected void setupAfterPropertiesSet(String menuAppName, String searchterm, String infoWebpage, String theme, boolean contextTest ,String imprintScreenName,String idGoogleAnalytics) {
        this.menuAppName = menuAppName;
        this.searchterm = searchterm;
        this.infoWebpage = infoWebpage;
        this.theme = theme;
        this.contextTest = contextTest;
        this.imprintScreenName = imprintScreenName;
        this.idGoogleAnalytics = idGoogleAnalytics;
        logEnv();
    }

    private final static String GOOGLE_ANALYTICS_SCRIPT_HTML = "<script>\n" +
            "        (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){\n" +
            "                (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),\n" +
            "            m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)\n" +
            "        })(window,document,'script','https://www.google-analytics.com/analytics.js','ga');\n" +
            "\n" +
            "        ga('create', '###GOOGLE_ANALYTICS_ID###', 'auto');\n" +
            "        ga('send', 'pageview');\n" +
            "    </script>";
}