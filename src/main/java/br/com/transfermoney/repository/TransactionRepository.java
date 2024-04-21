package br.com.transfermoney.repository;

import br.com.transfermoney.domain.entity.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
}
