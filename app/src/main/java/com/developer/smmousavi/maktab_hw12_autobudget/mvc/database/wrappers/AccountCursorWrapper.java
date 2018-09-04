package com.developer.smmousavi.maktab_hw12_autobudget.mvc.database.wrappers;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.developer.smmousavi.maktab_hw12_autobudget.mvc.database.DBSchema.AccountTable;
import com.developer.smmousavi.maktab_hw12_autobudget.mvc.model.Account;

import java.util.UUID;

public class AccountCursorWrapper extends CursorWrapper {
  public AccountCursorWrapper(Cursor cursor) {
    super(cursor);
  }

  public Account getAccount() {

    String uuid = getString(getColumnIndex(AccountTable.Cols.UUID));
    String name = getString(getColumnIndex(AccountTable.Cols.NAME));
    String accountNumber = getString(getColumnIndex(AccountTable.Cols.ACCOUNT_NUMBER));
    String smsNumber = getString(getColumnIndex(AccountTable.Cols.SMS_NUMBER));

    Account account = new Account(UUID.fromString(uuid));

    account.setName(name);
    account.setSmsNumber(smsNumber);
    account.setAccountNumber(accountNumber);

    return account;

  }
}
