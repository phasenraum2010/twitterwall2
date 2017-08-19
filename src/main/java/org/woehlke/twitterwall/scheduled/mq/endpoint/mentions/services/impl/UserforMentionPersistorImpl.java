package org.woehlke.twitterwall.scheduled.mq.endpoint.mentions.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.woehlke.twitterwall.oodm.entities.Task;
import org.woehlke.twitterwall.oodm.entities.User;
import org.woehlke.twitterwall.oodm.service.TaskService;
import org.woehlke.twitterwall.scheduled.mq.endpoint.common.TwitterwallMessageBuilder;
import org.woehlke.twitterwall.scheduled.mq.endpoint.mentions.services.UserforMentionPersistor;
import org.woehlke.twitterwall.scheduled.mq.msg.MentionMessage;
import org.woehlke.twitterwall.scheduled.service.persist.StoreUserProcess;

@Component("mqUserforMentionPersistor")
public class UserforMentionPersistorImpl implements UserforMentionPersistor {

    @Override
    public Message<MentionMessage> persistUserforMention(Message<MentionMessage> incomingMessage) {
        MentionMessage receivedMessage = incomingMessage.getPayload();
        if(receivedMessage.getTwitterProfile()==null) {
            return MessageBuilder.withPayload(receivedMessage)
                    .copyHeaders(incomingMessage.getHeaders())
                    .setHeader("persisted",Boolean.TRUE)
                    .build();
        } else {
            long taskId = receivedMessage.getTaskMessage().getTaskId();
            Task task = taskService.findById(taskId);
            User user = receivedMessage.getUser();
            User userPers = storeUserProcess.storeUserProcess(user, task);
            Message<MentionMessage> mqMessageOut =twitterwallMessageBuilder.buildMentionMessage(incomingMessage,userPers);
            return mqMessageOut;
        }
    }

    private final TaskService taskService;

    private final StoreUserProcess storeUserProcess;

    private final TwitterwallMessageBuilder twitterwallMessageBuilder;

    @Autowired
    public UserforMentionPersistorImpl(TaskService taskService, StoreUserProcess storeUserProcess, TwitterwallMessageBuilder twitterwallMessageBuilder) {
        this.taskService = taskService;
        this.storeUserProcess = storeUserProcess;
        this.twitterwallMessageBuilder = twitterwallMessageBuilder;
    }
}
