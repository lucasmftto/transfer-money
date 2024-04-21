package br.com.transfermoney.service;

import br.com.transfermoney.api.TransferResource;
import br.com.transfermoney.repository.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransferService {

    Logger logger = LoggerFactory.getLogger(TransferService.class);

    @Autowired
    private ClientRepository clientRepository;

    public void transfer(TransferResource transactionResource) {
        this.logger.info("Transfering money from {} to {}", transactionResource.payer(), transactionResource.payee());
    }
}



