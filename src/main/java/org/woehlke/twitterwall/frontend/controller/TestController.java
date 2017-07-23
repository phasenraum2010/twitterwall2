package org.woehlke.twitterwall.frontend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.woehlke.twitterwall.conf.TwitterProperties;
import org.woehlke.twitterwall.conf.TwitterwallFrontendProperties;
import org.woehlke.twitterwall.frontend.controller.common.Symbols;
import org.woehlke.twitterwall.frontend.controller.common.ControllerHelper;
import org.woehlke.twitterwall.oodm.entities.User;
import org.woehlke.twitterwall.oodm.service.UserService;
import org.woehlke.twitterwall.scheduled.mq.endoint.StartTask;
import org.woehlke.twitterwall.scheduled.service.facade.CreateTestData;
import org.woehlke.twitterwall.scheduled.service.facade.FetchUsersFromDefinedUserList;


/**
 * Created by tw on 30.06.17.
 */
@Controller
@RequestMapping("/test")
public class TestController {


    @RequestMapping("/getTestData")
    public String getTestData(Model model) {
        controllerHelper.logEnv();
        model = controllerHelper.setupPage(model,"Test Data Tweets",twitterProperties.getSearchQuery(),Symbols.GET_TEST_DATA.toString());
        String msg = "/getTestData : ";
        if(twitterwallFrontendProperties.getContextTest()){
            model.addAttribute("latestTweets", startTask.createTestDataForTweets());
            model.addAttribute("users", startTask.createTestDataForUser());
        } else {
            model.addAttribute("latestTweets",null);
            model.addAttribute("users",null);
        }
        return "test/getTestData";
    }

    @RequestMapping("/user/onlist/renew")
    public String getOnListRenew(@RequestParam(name= "page" ,defaultValue=""+ ControllerHelper.FIRST_PAGE_NUMBER) int page, Model model) {
        Pageable pageRequest = new PageRequest(page, twitterwallFrontendProperties.getPageSize());
        String msg = "getOnListRenew: ";
        this.startOnListRenew();
        log.info(msg+"START userService.findOnList(): ");
        Page<User> usersOnList = userService.getOnList(pageRequest);
        log.info(msg+"DONE userService.findOnList(): ");
        model.addAttribute("users", usersOnList);
        String symbol = Symbols.LEAF.toString();
        String title = "Renew List of Users On List";
        model = controllerHelper.setupPage(model, title, "Users", symbol);
        return "test/user/onlist/renew";
    }

    private static final Logger log = LoggerFactory.getLogger(TestController.class);

    private final UserService userService;

    private final FetchUsersFromDefinedUserList fetchUsersFromDefinedUserList;

    private final CreateTestData createTestData;

    private final ControllerHelper controllerHelper;

    private final TwitterwallFrontendProperties twitterwallFrontendProperties;

    private final TwitterProperties twitterProperties;

    private final StartTask startTask;

    @Autowired
    public TestController(UserService userService, FetchUsersFromDefinedUserList fetchUsersFromDefinedUserList, CreateTestData createTestData, ControllerHelper controllerHelper, TwitterwallFrontendProperties twitterwallFrontendProperties, TwitterProperties twitterProperties, StartTask startTask) {
        this.userService = userService;
        this.fetchUsersFromDefinedUserList = fetchUsersFromDefinedUserList;
        this.createTestData = createTestData;
        this.controllerHelper = controllerHelper;
        this.twitterwallFrontendProperties = twitterwallFrontendProperties;
        this.twitterProperties = twitterProperties;
        this.startTask = startTask;
    }

    @Async
    protected void startOnListRenew(){
        String msg = "startOnListRenew: ";
        log.info(msg+"START startTask.fetchUsersFromDefinedUserList: ");
        startTask.fetchUsersFromDefinedUserList();
        log.info(msg+"DONE startTask.fetchUsersFromDefinedUserList: ");
    }
}
