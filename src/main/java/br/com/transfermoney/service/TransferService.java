package br.com.transfermoney.service;

import br.com.transfermoney.api.TransferResource;
import br.com.transfermoney.domain.entity.Client;
import br.com.transfermoney.domain.entity.PersonType;
import br.com.transfermoney.infra.exception.NotAuthorizedException;
import br.com.transfermoney.repository.ClientRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mongodb.client.result.UpdateResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransferService {

    public static final String FIELD_BALANCE = "balance";
    Logger logger = LoggerFactory.getLogger(TransferService.class);

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private ExternalService externalService;
    @Autowired
    private NotifierService notifierService;
    @Autowired
    private MongoTemplate mongoTemplate;

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
            this.updateBalanceAndSendNotify(clientPayer, clientPayee, transactionResource);
        }

    }

    public void updateBalanceAndSendNotify(Client clientPayer, Client clientPayee, TransferResource transactionResource)
            throws JsonProcessingException {
        //TODO: Lock clientPayer and clientPayee
        clientPayer.setBalance(clientPayer.getBalance().subtract(transactionResource.value()));
        clientPayee.setBalance(clientPayee.getBalance().add(transactionResource.value()));
        this.transactionService.addTransaction(clientPayer, clientPayee, transactionResource);

        updateBalance(clientPayer, clientPayee, transactionResource);

        this.notifierService.notifyClient(clientPayer, clientPayee, transactionResource);
    }

    private void updateBalance(Client clientPayer, Client clientPayee, TransferResource transactionResource) {
        Query queryPayer = new Query().addCriteria(Criteria
                .where("id").is(clientPayer.getId())
                .and(FIELD_BALANCE).gte(transactionResource.value()));
        Update updatePayer = new Update().inc(FIELD_BALANCE, transactionResource.value().negate());

        UpdateResult updateResultPayer = this.mongoTemplate.updateFirst(queryPayer, updatePayer, Client.class);

        if (updateResultPayer.getModifiedCount() == 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        Query queryPayee = new Query().addCriteria(Criteria.where("id").is(clientPayee.getId()));
        Update updatePayee = new Update().inc(FIELD_BALANCE, transactionResource.value());

        UpdateResult updateResultPayee = this.mongoTemplate.updateFirst(queryPayee, updatePayee, Client.class);

        if (updateResultPayee.getModifiedCount() == 0) {
            throw new IllegalArgumentException("Error updating balance for Payee");
        }
    }
}



