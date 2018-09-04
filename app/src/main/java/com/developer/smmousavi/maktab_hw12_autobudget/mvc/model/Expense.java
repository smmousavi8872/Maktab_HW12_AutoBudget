package com.developer.smmousavi.maktab_hw12_autobudget.mvc.model;

import java.util.UUID;

import saman.zamani.persiandate.PersianDate;

public class Expense {
  UUID id;
  String title;
  String categoryName;
  PersianDate date;
  double amount;
  int categoryItem;
  boolean withdarw;


  public Expense() {
    id = UUID.randomUUID();
  }


  public Expense(UUID id) {
    this.id = id;
  }


  public UUID getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getCategoryName() {
    return categoryName;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

  public int getCategoryItem() {
    return categoryItem;
  }

  public void setCategoryItem(int categoryItem) {
    this.categoryItem = categoryItem;
  }

  public PersianDate getDate() {
    return date;
  }

  public void setDate(PersianDate date) {
    this.date = date;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public boolean isWithdarw() {
    return withdarw;
  }

  public void setWithdarw(boolean withdarw) {
    this.withdarw = withdarw;
  }
}
