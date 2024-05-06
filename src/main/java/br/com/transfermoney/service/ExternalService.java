package br.com.transfermoney.service;

import br.com.transfermoney.api.TransferResource;
import br.com.transfermoney.domain.entity.Client;
import br.com.transfermoney.feign.AuthorizationData;
import br.com.transfermoney.feign.ExternalFeignClient;
import br.com.transfermoney.feign.NotificationResource;
import br.com.transfermoney.infra.exception.NotAuthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ExternalService {

    Logger logger = LoggerFactory.getLogger(ExternalService.class);

    @Autowired
    private ExternalFeignClient externalFeignClient;

    public boolean isAuthorized(Client clientPayer, Client clientPayee) {
        AuthorizationData authorizationData = new AuthorizationData(clientPayer.getId(), clientPayee.getId());
        try {
            return this.externalFeignClient.isAuthorizing(authorizationData).get("message").equals("Autorizado");
        } catch (Exception e) {
            logger.error("Error authorizing transaction", e);
            throw new NotAuthorizedException("Transfer not authorized");
        }
}

    public void sendNotification(Client clientPayer, Client clientPayee, TransferResource transactionResource) {
        Map<String, Object> fields = Map.of("payer", clientPayer.getId(), "payee", clientPayee.getId(),
                "value", transactionResource.value());

        NotificationResource notificationResource = new NotificationResource("msg mock",
                "email@mock.com", fields);
        this.externalFeignClient.sendNotification(notificationResource);
    }
}
