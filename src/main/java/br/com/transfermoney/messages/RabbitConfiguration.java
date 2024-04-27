package br.com.transfermoney.messages;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitConfiguration {

    @Value("${queue.notification.name:notification-queue-local}")
    private String notificationQueueName;

    @Bean
    public Queue notificationQueue() {
        return QueueBuilder.durable(this.notificationQueueName).build();
    }

    public String getNotificationQueueName() {
        return this.notificationQueueName;
    }

}
