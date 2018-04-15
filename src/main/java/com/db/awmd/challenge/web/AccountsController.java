package com.db.awmd.challenge.web;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.TransferDetails;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;
import com.db.awmd.challenge.exception.InvalidAccountDetailsException;
import com.db.awmd.challenge.service.AccountsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.concurrent.Callable;

@RestController
@RequestMapping("/v1/accounts")
@Slf4j
public class AccountsController {

    private final AccountsService accountsService;

    @Autowired
    public AccountsController(AccountsService accountsService) {

        this.accountsService = accountsService;
    }


    @GetMapping(path = "/{accountId}")
    public Account getAccount(@PathVariable String accountId) {
        log.info("Retrieving account for id {}", accountId);
        return this.accountsService.getAccount(accountId);
    }


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createAccount(@RequestBody @Valid Account account) {
        log.info("Creating account {}", account);

        try {
            this.accountsService.createAccount(account);
        } catch (DuplicateAccountIdException daie) {
            return new ResponseEntity<>(daie.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }



    @PostMapping(path = "/transfer/money", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Callable<String> runRepairOrderIncrementalLoad(@RequestBody @Valid TransferDetails transferDetails)
            throws InvalidAccountDetailsException {
        return new Callable<String>() {
            public String call() throws Exception {
                accountsService.transferMoney(transferDetails);
                System.out.println("sender" +transferDetails.getAccountToId());
                System.out.println(" revicer"+transferDetails.getAccountFromId());
                System.out.println("amount "+transferDetails.getTransferAmount());
                return "{ \"response\": \"Success\"}";

            }
        };
   }
}
