package br.com.transfermoney.controller;

import br.com.transfermoney.api.TransferApi;
import br.com.transfermoney.api.TransferResource;
import br.com.transfermoney.service.TransferService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransferController implements TransferApi {

    @Autowired
    private TransferService transferService;

    @Override
    public ResponseEntity<Void> transfer(TransferResource transactionResource) throws JsonProcessingException {

        this.transferService.transfer(transactionResource);

        return ResponseEntity.ok().build();
    }
}
