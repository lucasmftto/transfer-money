package br.com.transfermoney.service;

import br.com.transfermoney.api.TransferResource;
import br.com.transfermoney.domain.entity.Client;
import br.com.transfermoney.domain.entity.PersonType;
import br.com.transfermoney.repository.ClientRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransferService {

    Logger logger = LoggerFactory.getLogger(TransferService.class);

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private ExternalService externalService;
    @Autowired
    private NotifierService notifierService;

    @Transactional
    public void transfer(TransferResource transactionResource) throws JsonProcessingException {
        this.logger.info("Transfering money from {} to {}", transactionResource.payer(), transactionResource.payee());

        Client clientPayer = this.clientRepository.findById(transactionResource.payer()).orElseThrow();

        if (clientPayer.getPersonType() == PersonType.LEGAL_PERSON) {
            throw new IllegalArgumentException("Legal person cannot transfer money");
        }

        Client clientPayee = this.clientRepository.findById(transactionResource.payee()).orElseThrow();

        if (clientPayer.getBalance().compareTo(transactionResource.value()) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        if (this.externalService.isAuthorized(clientPayer, clientPayee)) {
            this.updateBalance(clientPayer, clientPayee, transactionResource);
        } else {
            throw new IllegalArgumentException("Transfer not authorized");
        }

    }

    public void updateBalance(Client clientPayer, Client clientPayee, TransferResource transactionResource) throws JsonProcessingException {
        //TODO: Lock clientPayer and clientPayee
        clientPayer.setBalance(clientPayer.getBalance().subtract(transactionResource.value()));
        clientPayee.setBalance(clientPayee.getBalance().add(transactionResource.value()));
        this.transactionService.addTransaction(clientPayer, clientPayee, transactionResource);
        this.clientRepository.saveAll(List.of(clientPayer, clientPayee));

        this.notifierService.notifyClient(clientPayer, clientPayee, transactionResource);
    }
}



