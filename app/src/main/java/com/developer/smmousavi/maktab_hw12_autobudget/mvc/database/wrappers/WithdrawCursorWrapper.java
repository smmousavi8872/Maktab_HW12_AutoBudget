package com.developer.smmousavi.maktab_hw12_autobudget.mvc.database.wrappers;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.developer.smmousavi.maktab_hw12_autobudget.mvc.database.DBSchema.WithdrawTable;
import com.developer.smmousavi.maktab_hw12_autobudget.mvc.model.Withdraw;

import java.util.UUID;

import saman.zamani.persiandate.PersianDate;

public class WithdrawCursorWrapper extends CursorWrapper {
  public WithdrawCursorWrapper(Cursor cursor) {
    super(cursor);
  }

  public Withdraw getWithdraw() {

    String uuid = getString(getColumnIndex(WithdrawTable.Cols.UUID));
    double amount = getDouble(getColumnIndex(WithdrawTable.Cols.AMOUNT));
    long timeStamp = getLong(getColumnIndex(WithdrawTable.Cols.DATE));
    String bankName = getString(getColumnIndex(WithdrawTable.Cols.BANK_NAME));
    String accountNumber = getString(getColumnIndex(WithdrawTable.Cols.ACCOUNT_NUMBER));
    int ignoredInt = getInt(getColumnIndex(WithdrawTable.Cols.IGNORED));

    Withdraw withdraw = new Withdraw(UUID.fromString(uuid));

    withdraw.setAmount(amount);
    withdraw.setDate(new PersianDate(timeStamp));
    withdraw.setBankName(bankName);
    withdraw.setAccountNumber(accountNumber);
    withdraw.setIgnored(ignoredInt == 1);

    return withdraw;

  }
}
