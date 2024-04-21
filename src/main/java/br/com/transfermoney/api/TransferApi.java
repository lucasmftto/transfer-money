package br.com.transfermoney.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/transfer")
public interface TransferApi {

    @Tag(name = "Transfer", description = "Transfer Api")
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Void> transfer(@RequestBody TransferResource transactionResource);

}
