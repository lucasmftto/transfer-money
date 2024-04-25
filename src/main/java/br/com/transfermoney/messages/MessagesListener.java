package br.com.transfermoney.messages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class MessagesListener {

    Logger logger = LoggerFactory.getLogger(MessagesListener.class);

    @RabbitListener(queues = {"${queue.notification.name:notification-queue-local}"})
    public void receiveNotifications(@Payload String msg) {
        logger.info("Message Receive: {}", msg);
    }
}
