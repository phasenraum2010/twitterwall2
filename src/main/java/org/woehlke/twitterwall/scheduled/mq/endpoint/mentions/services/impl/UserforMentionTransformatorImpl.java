package org.woehlke.twitterwall.scheduled.mq.endpoint.mentions.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.woehlke.twitterwall.oodm.entities.Task;
import org.woehlke.twitterwall.oodm.entities.User;
import org.woehlke.twitterwall.oodm.service.TaskService;
import org.woehlke.twitterwall.scheduled.mq.endpoint.common.TwitterwallMessageBuilder;
import org.woehlke.twitterwall.scheduled.mq.endpoint.mentions.services.UserforMentionTransformator;
import org.woehlke.twitterwall.scheduled.mq.msg.MentionMessage;
import org.woehlke.twitterwall.scheduled.service.transform.UserTransformService;

@Component("mqUserforMentionTransformator")
public class UserforMentionTransformatorImpl implements UserforMentionTransformator {

    @Override
    public Message<MentionMessage> transformUserforMention(Message<MentionMessage> incomingMessage) {
        MentionMessage receivedMessage = incomingMessage.getPayload();
        long id = receivedMessage.getTaskMessage().getTaskId();
        Task task = taskService.findById(id);
        User user = userTransformService.transform(receivedMessage.getTwitterProfile(),task);
        Message<MentionMessage> mqMessageOut =twitterwallMessageBuilder.buildMentionMessage(incomingMessage,user);
        return mqMessageOut;
    }

    private final UserTransformService userTransformService;

    private final TaskService taskService;

    private final TwitterwallMessageBuilder twitterwallMessageBuilder;

    @Autowired
    public UserforMentionTransformatorImpl(UserTransformService userTransformService, TaskService taskService, TwitterwallMessageBuilder twitterwallMessageBuilder) {
        this.userTransformService = userTransformService;
        this.taskService = taskService;
        this.twitterwallMessageBuilder = twitterwallMessageBuilder;
    }
}
