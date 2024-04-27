package br.com.transfermoney.service;

import br.com.transfermoney.api.TransferResource;
import br.com.transfermoney.domain.entity.Client;
import br.com.transfermoney.domain.entity.Transaction;
import br.com.transfermoney.domain.entity.TransactionType;
import br.com.transfermoney.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public void addTransaction(Client clientPayer, Client clientPayee, TransferResource transactionResource) {
        Transaction transactionPayer = new Transaction();
        transactionPayer.setClientId(clientPayer.getId());
        transactionPayer.setTransactionType(TransactionType.DEBIT);
        transactionPayer.setValue(transactionResource.value());
        transactionPayer.setDescription("Transfer to ");
        transactionPayer.setDate(LocalDate.now());

        Transaction transactionPayee = new Transaction();
        transactionPayee.setClientId(clientPayee.getId());
        transactionPayee.setTransactionType(TransactionType.CREDIT);
        transactionPayee.setValue(transactionResource.value());
        transactionPayee.setDescription("Transfer to ");
        transactionPayee.setDate(LocalDate.now());

        this.transactionRepository.saveAll(List.of(transactionPayer, transactionPayee));

    }
}
