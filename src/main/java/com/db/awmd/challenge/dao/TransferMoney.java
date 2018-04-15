package com.db.awmd.challenge.dao;

import com.db.awmd.challenge.domain.Account;

import java.math.BigDecimal;

public interface TransferMoney {

     void transferMoney(Account accountfrom, Account accountto, BigDecimal transferAmount);
}
