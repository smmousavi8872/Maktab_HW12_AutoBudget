package com.developer.smmousavi.maktab_hw12_autobudget.mvc.model;

import java.util.UUID;

import saman.zamani.persiandate.PersianDate;

public class Transaction {
  private UUID id;
  private double amount;
  private PersianDate date;
  private String bankName;
  private String accountNumber;
  private boolean ignored;

  public Transaction() {
    id = UUID.randomUUID();
  }

  public Transaction(UUID id) {
    this.id = id;
  }

  public UUID getId() {
    return id;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public PersianDate getPersianDate() {
    return date;
  }

  public void setDate(PersianDate date) {
    this.date = date;
  }

  public String getBankName() {
    return bankName;
  }

  public void setBankName(String bankName) {
    this.bankName = bankName;
  }

  public String getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
  }

  public boolean isIgnored() {
    return ignored;
  }

  public void setIgnored(boolean ignored) {
    this.ignored = ignored;
  }
}
