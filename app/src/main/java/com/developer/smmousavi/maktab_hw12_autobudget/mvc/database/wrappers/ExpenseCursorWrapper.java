package com.developer.smmousavi.maktab_hw12_autobudget.mvc.database.wrappers;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.developer.smmousavi.maktab_hw12_autobudget.mvc.database.DBSchema.ExpenseTable;
import com.developer.smmousavi.maktab_hw12_autobudget.mvc.model.Expense;

import java.util.UUID;

import saman.zamani.persiandate.PersianDate;

public class ExpenseCursorWrapper extends CursorWrapper {
  public ExpenseCursorWrapper(Cursor cursor) {
    super(cursor);
  }

  public Expense getExpense() {

    String uuid = getString(getColumnIndex(ExpenseTable.Cols.UUID));
    String title = getString(getColumnIndex(ExpenseTable.Cols.TITLE));
    String categoryName = getString(getColumnIndex(ExpenseTable.Cols.CATEGORY_NAME));
    int categoryItem = getInt(getColumnIndex(ExpenseTable.Cols.CATEGORY_ITEM));
    long timeStamp = getLong(getColumnIndex(ExpenseTable.Cols.DATE));
    double amount = getDouble(getColumnIndex(ExpenseTable.Cols.AMOUNT));
    int intWithdraw = getInt(getColumnIndex(ExpenseTable.Cols.WITHDRAW));

    Expense expense = new Expense(UUID.fromString(uuid));

    expense.setTitle(title);
    expense.setCategoryName(categoryName);
    expense.setCategoryItem(categoryItem);
    expense.setDate(new PersianDate(timeStamp));
    expense.setAmount(amount);
    expense.setWithdarw(intWithdraw == 1);

    return expense;

  }
}
