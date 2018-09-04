package com.developer.smmousavi.maktab_hw12_autobudget.mvc.database.wrappers;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.developer.smmousavi.maktab_hw12_autobudget.mvc.database.DBSchema.DepositTable;
import com.developer.smmousavi.maktab_hw12_autobudget.mvc.model.Deposit;

import java.util.UUID;

import saman.zamani.persiandate.PersianDate;

public class DepositCursorWrapper extends CursorWrapper {

  public DepositCursorWrapper(Cursor cursor) {
    super(cursor);
  }

  public Deposit getDeposit() {
    String uuid = getString(getColumnIndex(DepositTable.Cols.UUID));
    double amount = getDouble(getColumnIndex(DepositTable.Cols.AMOUNT));
    long timeStamp = getLong(getColumnIndex(DepositTable.Cols.DATE));
    String bankName = getString(getColumnIndex(DepositTable.Cols.BANK_NAME));
    String accountNumber = getString(getColumnIndex(DepositTable.Cols.ACCOUNT_NUMBER));
    //int ignoredInt = getInt(getColumnIndex(DepositTable.Cols.IGNORED));

    Deposit deposit = new Deposit(UUID.fromString(uuid));

    deposit.setAmount(amount);
    deposit.setDate(new PersianDate(timeStamp));
    deposit.setBankName(bankName);
    deposit.setAccountNumber(accountNumber);
    //deposit.setIgnored(ignoredInt == 1);

    return deposit;
  }
}
