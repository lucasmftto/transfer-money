package br.com.transfermoney.service;

import br.com.transfermoney.api.TransferResource;
import br.com.transfermoney.domain.entity.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotifierService {

    Logger logger = LoggerFactory.getLogger(NotifierService.class);

    public void notifyClient(Client clientPayer, Client clientPayee, TransferResource transactionResource) {
        this.logger.info("Notify client to queue");
    }
}
