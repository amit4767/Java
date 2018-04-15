package com.db.awmd.challenge.service;

import com.db.awmd.challenge.dao.TransferMoneyImpl;
import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.TransferDetails;
import com.db.awmd.challenge.exception.InvalidAccountDetailsException;
import com.db.awmd.challenge.repository.AccountsRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountsService {

  @Getter
  private final AccountsRepository accountsRepository;

  @Autowired
  private EmailNotificationService emailNotificationService;

  @Autowired
  private TransferMoneyImpl transferMoneyImpl;

  @Autowired
  public AccountsService(AccountsRepository accountsRepository) {
    this.accountsRepository = accountsRepository;
  }

  public void createAccount(Account account) {
    this.accountsRepository.createAccount(account);
  }

  public Account getAccount(String accountId) {
    return this.accountsRepository.getAccount(accountId);
  }

  public  synchronized void transferMoney(TransferDetails transferDetails){
      Account accountfrom = this.accountsRepository.getAccount(transferDetails.getAccountFromId());
      Account accountto = this.accountsRepository.getAccount(transferDetails.getAccountToId());
      BigDecimal transferAmount = transferDetails.getTransferAmount();
      verifyTransferDetails(accountfrom,accountto,transferAmount);
      transferMoneyImpl.transferMoney(accountfrom,accountto,transferAmount);
      emailNotificationService.notifyAboutTransfer(accountfrom ,"");
      emailNotificationService.notifyAboutTransfer(accountto ,"");

  }

  private void verifyTransferDetails(Account accountfrom ,Account accountto ,BigDecimal transferAmount) throws  InvalidAccountDetailsException{

     if(null != accountfrom && null != accountto){
        checkValidAmount(accountfrom.getBalance(),transferAmount);
     }else {
       throw  new InvalidAccountDetailsException("Account id not present");
     }


  }

  private void checkValidAmount( BigDecimal accountFromAmount , BigDecimal transferAmount) throws InvalidAccountDetailsException{
       if( transferAmount.compareTo(accountFromAmount) == -1 ) throw  new InvalidAccountDetailsException("Balance cant go negative");

  }

}
