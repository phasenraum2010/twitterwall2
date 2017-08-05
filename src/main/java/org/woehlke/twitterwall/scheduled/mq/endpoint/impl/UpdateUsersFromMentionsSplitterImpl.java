package org.woehlke.twitterwall.scheduled.mq.endpoint.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Component;
import org.woehlke.twitterwall.conf.properties.TwitterProperties;
import org.woehlke.twitterwall.oodm.entities.Mention;
import org.woehlke.twitterwall.oodm.entities.Task;
import org.woehlke.twitterwall.oodm.entities.User;
import org.woehlke.twitterwall.oodm.entities.parts.CountedEntities;
import org.woehlke.twitterwall.oodm.service.MentionService;
import org.woehlke.twitterwall.oodm.service.TaskService;
import org.woehlke.twitterwall.oodm.service.UserService;
import org.woehlke.twitterwall.scheduled.mq.endpoint.UpdateUsersFromMentionsSplitter;
import org.woehlke.twitterwall.scheduled.mq.msg.TaskMessage;
import org.woehlke.twitterwall.scheduled.mq.msg.UserMessage;
import org.woehlke.twitterwall.scheduled.service.remote.TwitterApiService;
import org.woehlke.twitterwall.oodm.service.CountedEntitiesService;

import java.util.ArrayList;
import java.util.List;

import static org.woehlke.twitterwall.frontend.controller.common.ControllerHelper.FIRST_PAGE_NUMBER;

@Component("mqUpdateUserFromMentionsSplitter")
public class UpdateUsersFromMentionsSplitterImpl implements UpdateUsersFromMentionsSplitter {

    private static final Logger log = LoggerFactory.getLogger(UpdateUsersFromMentionsSplitterImpl.class);

    private final TwitterProperties twitterProperties;

    private final TwitterApiService twitterApiService;

    private final TaskService taskService;

    private final MentionService mentionService;

    private final UserService userService;

    private final CountedEntitiesService countedEntitiesService;

    public UpdateUsersFromMentionsSplitterImpl(TwitterProperties twitterProperties, TwitterApiService twitterApiService, TaskService taskService, MentionService mentionService, UserService userService, CountedEntitiesService countedEntitiesService) {
        this.twitterProperties = twitterProperties;
        this.twitterApiService = twitterApiService;
        this.taskService = taskService;
        this.mentionService = mentionService;
        this.userService = userService;
        this.countedEntitiesService = countedEntitiesService;
    }

    @Override
    public List<Message<UserMessage>> splitMessage(Message<TaskMessage> incomingTaskMessage) {
        String msg ="splitMessage: ";
        log.debug(msg+ " START");
        CountedEntities countedEntities = countedEntitiesService.countAll();
        List<Message<UserMessage>>  userProfileList = new ArrayList<>();
        TaskMessage msgIn = incomingTaskMessage.getPayload();
        long id = msgIn.getTaskId();
        Task task = taskService.findById(id);
        task =  taskService.start(task,countedEntities);
        List<String> screenNames = new ArrayList<>();
        int lfdNr = 0;
        int all = 0;
        boolean hasNext=true;
        Pageable pageRequest = new PageRequest(FIRST_PAGE_NUMBER, twitterProperties.getPageSize());
        while (hasNext) {
            Page<Mention> allPersMentions = mentionService.getAllWithoutPersistentUser(pageRequest);
            hasNext = allPersMentions.hasNext();
            for (Mention onePersMention : allPersMentions) {
                if (!onePersMention.hasPersistentUser()) {
                    String screenName = onePersMention.getScreenName();
                    User foundUser = userService.findByScreenName(screenName);
                    if(foundUser == null) {
                        lfdNr++;
                        all++;
                        log.debug("### mentionService.getAll from DB (" + lfdNr + "): " + screenName);
                        screenNames.add(screenName);
                    } else {
                        onePersMention.setUser(foundUser);
                        onePersMention.setIdTwitterOfUser(foundUser.getIdTwitter());
                        onePersMention = mentionService.store(onePersMention,task);
                        log.debug("### updated Mention with screenName = " + screenName);
                    }
                }
            }
            pageRequest = pageRequest.next();
        }
        int millisToWaitBetweenTwoApiCalls = twitterProperties.getMillisToWaitBetweenTwoApiCalls();
        lfdNr = 0;
        for(String screenName:screenNames){
            lfdNr++;
            log.debug("### twitterApiService.getUserProfileForScreenName("+screenName+") from Twiiter API ("+lfdNr+" of "+all+")");
            TwitterProfile userProfile = twitterApiService.getUserProfileForScreenName(screenName);
            if(userProfile!=null) {
                UserMessage userMsg = new UserMessage(msgIn, userProfile);
                Message<UserMessage> mqMessageOut =
                        MessageBuilder.withPayload(userMsg)
                                .copyHeaders(incomingTaskMessage.getHeaders())
                                .setHeader("tw_lfd_nr",lfdNr)
                                .setHeader("tw_all",all)
                                .build();
                userProfileList.add(mqMessageOut);
            }
            log.debug(msg+"### waiting now for (ms): "+millisToWaitBetweenTwoApiCalls);
            try {
                Thread.sleep(millisToWaitBetweenTwoApiCalls);
            } catch (InterruptedException e) {
            }
        }
        return userProfileList;
    }
}
