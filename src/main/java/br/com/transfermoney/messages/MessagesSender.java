package br.com.transfermoney.messages;

import br.com.transfermoney.feign.NotificationResource;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class MessagesSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RabbitConfiguration rabbitConfiguration;

    public void sendNotification(NotificationResource notificationResource) throws JsonProcessingException {
        String message = this.objectMapper.writeValueAsString(notificationResource);
        this.rabbitTemplate.convertAndSend(this.rabbitConfiguration.getNotificationQueueName(), message);
    }
}
