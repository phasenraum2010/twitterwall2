package org.woehlke.twitterwall.scheduled.mq.endpoint.users.services;

import org.springframework.messaging.Message;
import org.woehlke.twitterwall.scheduled.mq.msg.UserMessage;
import org.woehlke.twitterwall.scheduled.mq.msg.results.UserResultList;

import java.util.List;

public interface UserFinisher {

    Message<UserResultList> finish(Message<List<UserMessage>> incomingMessageList);

    void finishAsnyc(Message<List<UserMessage>> incomingMessageList);

    void finishOneUserAsnyc(Message<UserMessage> incomingMessage);
}
