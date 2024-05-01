package br.com.transfermoney;

import br.com.transfermoney.api.TransferResource;
import br.com.transfermoney.domain.entity.Client;
import br.com.transfermoney.repository.ClientRepository;
import br.com.transfermoney.repository.TransactionRepository;
import br.com.transfermoney.service.ExternalService;
import br.com.transfermoney.service.TransferService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@Testcontainers
class TransferServiceTest {

    @Container
    @ServiceConnection
    static MongoDBContainer mongoDbContainer = new MongoDBContainer("mongo:latest");

    @Container
    @ServiceConnection
    static RabbitMQContainer rabbitContainer = new RabbitMQContainer("rabbitmq:latest");

    @Autowired
    private TransferService transferService;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @MockBean
    private ExternalService externalService;

    @Test
    void transferTest() throws JsonProcessingException {
        when(this.externalService.isAuthorized(any(), any())).thenReturn(true);

        TransferResource transactionResource = new TransferResource(BigDecimal.valueOf(10), "1", "2");

        assertEquals(this.transactionRepository.count(), 0);

        this.transferService.transfer(transactionResource);

        Client client1 = this.clientRepository.findById("1").get();
        Assertions.assertEquals(BigDecimal.valueOf(990.0), client1.getBalance());

        Client client2 = this.clientRepository.findById("2").get();
        Assertions.assertEquals(BigDecimal.valueOf(2010.0), client2.getBalance());

        assertEquals(this.transactionRepository.count(), 2);
    }

    @Test
    void notAuthorizedTest() throws JsonProcessingException {
        when(this.externalService.isAuthorized(any(), any())).thenReturn(false);

        TransferResource transactionResource = new TransferResource(BigDecimal.TEN, "3", "4");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            this.transferService.transfer(transactionResource);
        });

        assertEquals("Transfer not authorized", exception.getMessage());

        Client client3 = this.clientRepository.findById("3").get();
        Assertions.assertEquals(BigDecimal.valueOf(3000.0), client3.getBalance());

        Client client4 = this.clientRepository.findById("4").get();
        Assertions.assertEquals(BigDecimal.valueOf(4000.0), client4.getBalance());
    }
}
