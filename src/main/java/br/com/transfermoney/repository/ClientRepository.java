package br.com.transfermoney.repository;

import br.com.transfermoney.domain.entity.Client;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClientRepository extends MongoRepository<Client, String> {

}
