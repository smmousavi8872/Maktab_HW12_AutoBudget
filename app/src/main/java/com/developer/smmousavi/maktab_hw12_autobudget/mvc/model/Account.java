package com.developer.smmousavi.maktab_hw12_autobudget.mvc.model;

import java.util.UUID;

public class Account {

  private UUID id;
  private String name;
  private String accountNumber;
  private String smsNumber;


  public Account() {
    id = UUID.randomUUID();
  }


  public Account(UUID id) {
    this.id = id;
  }


  public UUID getId() {
    return id;
  }


  public String getName() {
    return name;
  }


  public void setName(String name) {
    this.name = name;
  }


  public String getAccountNumber() {
    return accountNumber;
  }


  public void setAccountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
  }


  public String getSmsNumber() {
    return smsNumber;
  }


  public void setSmsNumber(String smsNumber) {
    this.smsNumber = smsNumber;
  }
}
