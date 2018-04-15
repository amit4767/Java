package com.db.awmd.challenge.dao;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.repository.AccountsRepositoryInMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public class TransferMoneyImpl implements TransferMoney {

    @Autowired
    AccountsRepositoryInMemory accountsRepositoryInMemory;

    @Override
    public  synchronized void transferMoney(Account accountfrom, Account accountto, BigDecimal transferAmount){
        accountfrom.setBalance(accountfrom.getBalance().subtract(transferAmount));
        accountfrom.setBalance(accountfrom.getBalance().add(transferAmount));
        accountsRepositoryInMemory.updateAccount(accountfrom);
        accountsRepositoryInMemory.updateAccount(accountto);

    }
}
