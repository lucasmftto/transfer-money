package br.com.transfermoney.service;

import br.com.transfermoney.api.TransferResource;
import br.com.transfermoney.domain.entity.Client;
import br.com.transfermoney.domain.entity.PersonType;
import br.com.transfermoney.repository.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransferService {

    Logger logger = LoggerFactory.getLogger(TransferService.class);

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private TransactionService transactionService;

    @Transactional
    public void transfer(TransferResource transactionResource) {
        this.logger.info("Transfering money from {} to {}", transactionResource.payer(), transactionResource.payee());

        Client clientPayer = this.clientRepository.findById(transactionResource.payer()).orElseThrow();

        if (clientPayer.getPersonType() == PersonType.LEGAL_PERSON) {
            throw new IllegalArgumentException("Legal person cannot transfer money");
        }

        Client clientPayee = this.clientRepository.findById(transactionResource.payee()).orElseThrow();

        if (clientPayer.getBalance().compareTo(transactionResource.value()) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        this.updateBalance(clientPayer, clientPayee, transactionResource);

    }

    public void updateBalance(Client clientPayer, Client clientPayee, TransferResource transactionResource) {
        clientPayer.setBalance(clientPayer.getBalance().subtract(transactionResource.value()));
        clientPayee.setBalance(clientPayee.getBalance().add(transactionResource.value()));
        this.transactionService.addTransaction(clientPayer, clientPayee, transactionResource);
        this.clientRepository.save(clientPayer);
        this.clientRepository.save(clientPayee);
    }
}



