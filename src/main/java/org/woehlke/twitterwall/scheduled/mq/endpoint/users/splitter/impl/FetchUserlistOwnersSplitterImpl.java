package org.woehlke.twitterwall.scheduled.mq.endpoint.users.splitter.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.Message;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Component;
import org.woehlke.twitterwall.conf.properties.TwitterProperties;
import org.woehlke.twitterwall.oodm.entities.Task;
import org.woehlke.twitterwall.oodm.entities.User;
import org.woehlke.twitterwall.oodm.entities.UserList;
import org.woehlke.twitterwall.oodm.entities.parts.CountedEntities;
import org.woehlke.twitterwall.oodm.service.CountedEntitiesService;
import org.woehlke.twitterwall.oodm.service.TaskService;
import org.woehlke.twitterwall.oodm.service.UserListService;
import org.woehlke.twitterwall.oodm.service.UserService;
import org.woehlke.twitterwall.scheduled.mq.endpoint.common.TwitterwallMessageBuilder;
import org.woehlke.twitterwall.scheduled.mq.endpoint.mentions.splitter.impl.UpdateUsersFromMentionsSplitterImpl;
import org.woehlke.twitterwall.scheduled.mq.endpoint.users.splitter.FetchUserlistOwnersSplitter;
import org.woehlke.twitterwall.scheduled.mq.msg.TaskMessage;
import org.woehlke.twitterwall.scheduled.mq.msg.UserMessage;
import org.woehlke.twitterwall.scheduled.service.remote.TwitterApiService;

import java.util.ArrayList;
import java.util.List;

import static org.woehlke.twitterwall.frontend.controller.common.ControllerHelper.FIRST_PAGE_NUMBER;

@Component("mqFetchUserlistOwnersSplitter")
public class FetchUserlistOwnersSplitterImpl implements FetchUserlistOwnersSplitter {



    @Override
    public List<Message<UserMessage>> splitUserMessage(Message<TaskMessage> incomingTaskMessage) {
        String msg ="splitTweetMessage: ";
        log.debug(msg+ " START");
        CountedEntities countedEntities = countedEntitiesService.countAll();
        TaskMessage msgIn = incomingTaskMessage.getPayload();
        long id = msgIn.getTaskId();
        Task task = taskService.findById(id);
        task =  taskService.start(task,countedEntities);
        List<Message<UserMessage>>  userProfileList = new ArrayList<>();
        List<String> screenNames = new ArrayList<>();
        int lfdNr = 0;
        int all = 0;
        boolean hasNext=true;
        Pageable pageRequest = new PageRequest(FIRST_PAGE_NUMBER, twitterProperties.getPageSize());
        while (hasNext) {
            Page<UserList> userListPage = userListService.getAll(pageRequest);
            hasNext = userListPage.hasNext();
            for (UserList oneUserList : userListPage.getContent()) {
                String screenName = oneUserList.getListOwnersScreenName();
                User foundUser = userService.findByScreenName(screenName);
                if(foundUser == null) {
                    lfdNr++;
                    all++;
                    log.debug("### mentionService.getAll from DB (" + lfdNr + "): " + screenName);
                    screenNames.add(screenName);
                } else {
                    foundUser = userService.store(foundUser,task);
                    log.debug("### updated User with screenName = " + foundUser.getUniqueId());
                }
            }
            pageRequest = pageRequest.next();
        }
        lfdNr = 0;
        for(String screenName:screenNames){
            lfdNr++;
            log.debug("### twitterApiService.getUserProfileForScreenName("+screenName+") from Twiiter API ("+lfdNr+" of "+all+")");
            TwitterProfile userProfile = twitterApiService.getUserProfileForScreenName(screenName);
            if(userProfile!=null) {
                Message<UserMessage> mqMessageOut = twitterwallMessageBuilder.buildUserMessage(incomingTaskMessage,userProfile,lfdNr,all);
                userProfileList.add(mqMessageOut);
            }
        }
        return userProfileList;
    }

    private static final Logger log = LoggerFactory.getLogger(UpdateUsersFromMentionsSplitterImpl.class);

    private final TwitterProperties twitterProperties;

    private final TwitterApiService twitterApiService;

    private final TaskService taskService;

    private final UserService userService;

    private final UserListService userListService;

    private final CountedEntitiesService countedEntitiesService;

    private final TwitterwallMessageBuilder twitterwallMessageBuilder;

    @Autowired
    public FetchUserlistOwnersSplitterImpl(TwitterProperties twitterProperties, TwitterApiService twitterApiService, TaskService taskService, UserService userService, UserListService userListService, CountedEntitiesService countedEntitiesService, TwitterwallMessageBuilder twitterwallMessageBuilder) {
        this.twitterProperties = twitterProperties;
        this.twitterApiService = twitterApiService;
        this.taskService = taskService;
        this.userService = userService;
        this.userListService = userListService;
        this.countedEntitiesService = countedEntitiesService;
        this.twitterwallMessageBuilder = twitterwallMessageBuilder;
    }
}
