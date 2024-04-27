package br.com.transfermoney.service;

import br.com.transfermoney.api.TransferResource;
import br.com.transfermoney.domain.entity.Client;
import br.com.transfermoney.feign.NotificationResource;
import br.com.transfermoney.messages.MessagesSender;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class NotifierService {

    Logger logger = LoggerFactory.getLogger(NotifierService.class);

    @Autowired
    private MessagesSender messagesSender;

    public void notifyClient(Client clientPayer, Client clientPayee, TransferResource transactionResource) throws JsonProcessingException {
        this.logger.info("Notify client to queue");
        NotificationResource notificationResource = new NotificationResource("Message xxx",
                "email@email.com", Map.of("key", "value"));
        this.messagesSender.sendNotification(notificationResource);
    }
}
