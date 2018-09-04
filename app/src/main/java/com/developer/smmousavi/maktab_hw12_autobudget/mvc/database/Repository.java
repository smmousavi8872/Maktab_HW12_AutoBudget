package com.developer.smmousavi.maktab_hw12_autobudget.mvc.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.util.Log;

import com.developer.smmousavi.maktab_hw12_autobudget.mvc.database.DBSchema.AccountTable;
import com.developer.smmousavi.maktab_hw12_autobudget.mvc.database.DBSchema.AllTaskTable;
import com.developer.smmousavi.maktab_hw12_autobudget.mvc.database.DBSchema.DepositTable;
import com.developer.smmousavi.maktab_hw12_autobudget.mvc.database.DBSchema.ExpenseTable;
import com.developer.smmousavi.maktab_hw12_autobudget.mvc.database.DBSchema.WithdrawTable;
import com.developer.smmousavi.maktab_hw12_autobudget.mvc.database.wrappers.AccountCursorWrapper;
import com.developer.smmousavi.maktab_hw12_autobudget.mvc.database.wrappers.DepositCursorWrapper;
import com.developer.smmousavi.maktab_hw12_autobudget.mvc.database.wrappers.ExpenseCursorWrapper;
import com.developer.smmousavi.maktab_hw12_autobudget.mvc.database.wrappers.TaskCursorWrapper;
import com.developer.smmousavi.maktab_hw12_autobudget.mvc.database.wrappers.WithdrawCursorWrapper;
import com.developer.smmousavi.maktab_hw12_autobudget.mvc.model.Account;
import com.developer.smmousavi.maktab_hw12_autobudget.mvc.model.Deposit;
import com.developer.smmousavi.maktab_hw12_autobudget.mvc.model.Expense;
import com.developer.smmousavi.maktab_hw12_autobudget.mvc.model.Task;
import com.developer.smmousavi.maktab_hw12_autobudget.mvc.model.Withdraw;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import saman.zamani.persiandate.PersianDate;


public class Repository {
  private static Repository instance;
  private SQLiteDatabase database;
  private Context context;


  private Repository(Context context) {
    DatabaseHelper db = new DatabaseHelper(context);
    database = db.getWritableDatabase();
    Calendar c = Calendar.getInstance();
    this.context = context;

  } // end of Repository()


  public static Repository getInstance(Context context) {
    if (instance == null)
      instance = new Repository(context);

    return instance;
  }// end of getInstance()


  public void addDeposit(Deposit deposit) {
    ContentValues values = getDepositContentValues(deposit);
    database.insert(DepositTable.NAME, null, values);
  }// end of addDeposit()


  public void addWithdraw(Withdraw withdraw) {
    ContentValues values = getWithdrawContentValues(withdraw);
    database.insert(WithdrawTable.NAME, null, values);
  }// end of addWithdraw()


  public void addExpense(Expense expense) {
    ContentValues values = getExpenseContentValues(expense);
    database.insert(ExpenseTable.NAME, null, values);
  }// end of addExpense()


  public void addAccount(Account account) {
    ContentValues values = getAccountContentValues(account);
    database.insert(AccountTable.NAME, null, values);
  }// end of addAccount()


  public Deposit getDeposit(UUID dipositId) {
    String whereClause = DepositTable.Cols.UUID + " = ? ";
    String[] whereArgs = {dipositId.toString()};
    try (DepositCursorWrapper cursor = getDepositQuery(DepositTable.NAME, whereClause, whereArgs)) {
      if (cursor.getCount() == 0)
        return null;

      cursor.moveToFirst();
      return cursor.getDeposit();
    }
  }// end of getDeposit()


  public Withdraw getWithdraw(UUID withdrawId) {
    String whereClause = WithdrawTable.Cols.UUID + " = ? ";
    String[] whereArgs = {withdrawId.toString()};
    try (WithdrawCursorWrapper cursor = getWithdrawQuery(WithdrawTable.NAME, whereClause, whereArgs)) {
      if (cursor.getCount() == 0)
        return null;

      cursor.moveToFirst();
      return cursor.getWithdraw();
    }
  }// end of getWithdraw()


  public Expense getExpense(UUID expenseId) {
    String whereClause = ExpenseTable.Cols.UUID + " = ? ";
    String[] whereArgs = {expenseId.toString()};
    try (ExpenseCursorWrapper cursor = getExpenseQuery(WithdrawTable.NAME, whereClause, whereArgs)) {
      if (cursor.getCount() == 0)
        return null;

      cursor.moveToFirst();
      return cursor.getExpense();
    }
  }// end of getExpense()


  public Account getAccount(UUID accountId) {
    String whereClause = AccountTable.Cols.UUID + " = ? ";
    String[] whereArgs = {accountId.toString()};
    try (AccountCursorWrapper cursor = getAccountQuery(AccountTable.NAME, whereClause, whereArgs)) {
      if (cursor.getCount() == 0)
        return null;

      cursor.moveToFirst();
      return cursor.getAccount();
    }
  }// end of getAccount()


  public List<Account> getAccountList() {
    List<Account> accounts = new ArrayList<>();
    AccountCursorWrapper cursor = getAccountQuery(AccountTable.NAME, null, null);

    if (cursor.getCount() > 0) {
      try {
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
          accounts.add(cursor.getAccount());
          cursor.moveToNext();
        }

      } finally {
        cursor.close();
      }
    }
    return accounts;
  }// end of getAccountList()


  public List<Withdraw> getWithdrawListByMonth(int month) {
    List<Withdraw> withdraws = new ArrayList<>();
    String whereClause = WithdrawTable.Cols.MONTH + " = " + month;
    WithdrawCursorWrapper cursor = getWithdrawQuery(WithdrawTable.NAME, whereClause, null);

    if (cursor.getCount() > 0) {
      try {
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
          withdraws.add(cursor.getWithdraw());
          cursor.moveToNext();
        }

      } finally {
        cursor.close();
      }
    }
    return withdraws;
  }// end of getWithdrawListByMonth()


  public List<Deposit> getDepositListByMonth(int month) {
    List<Deposit> deposits = new ArrayList<>();
    String whereClause = DepositTable.Cols.MONTH + " = " + month;
    DepositCursorWrapper cursor = getDepositQuery(DepositTable.NAME, whereClause, null);
    if (cursor.getCount() > 0) {
      try {
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
          deposits.add(cursor.getDeposit());
          cursor.moveToNext();
        }

      } finally {
        cursor.close();
      }
    }
    return deposits;
  }// end of getDepositListByMonth()


  public List<Expense> getExpenseListByMonth(int month) {
    List<Expense> expenses = new ArrayList<>();
    String whereClause = ExpenseTable.Cols.MONTH + " = " + month;
    ExpenseCursorWrapper cursor = getExpenseQuery(ExpenseTable.NAME, whereClause, null);
    if (cursor.getCount() > 0) {
      try {
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
          expenses.add(cursor.getExpense());
          cursor.moveToNext();
        }

      } finally {
        cursor.close();
      }
    }
    return expenses;
  }// end of getExpenseListByMonth()


  public List<Expense> getExpenseListByMonth(int thisMonth, int prevMonth) {
    List<Expense> expenses = new ArrayList<>();
    String whereClause = ExpenseTable.Cols.MONTH + " = " + thisMonth + " or " + ExpenseTable.Cols.MONTH + " = " + prevMonth;
    ExpenseCursorWrapper cursor = getExpenseQuery(ExpenseTable.NAME, whereClause, null);
    if (cursor.getCount() > 0) {
      try {
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
          expenses.add(cursor.getExpense());
          cursor.moveToNext();
        }

      } finally {
        cursor.close();
      }
    }
    return expenses;
  }// end of getExpenseListByMonth()


  public List<Withdraw> getUnregisteredWithdrawList() {
    List<Withdraw> withdraws = new ArrayList<>();
    String whereClause = WithdrawTable.Cols.IGNORED + " = " + 0;
    WithdrawCursorWrapper cursor = getWithdrawQuery(WithdrawTable.NAME, whereClause, null);
    if (cursor.getCount() > 0) {
      try {
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
          withdraws.add(cursor.getWithdraw());
          cursor.moveToNext();
        }

      } finally {
        cursor.close();
      }
    }
    return withdraws;
  }// end of getExpenseListByMonth()


  public void removeAccount(Account account) {
    String whereClause = AccountTable.Cols.UUID + " = ? ";
    String[] whereArgs = {account.getId().toString()};
    database.delete(AccountTable.NAME, whereClause, whereArgs);
  }// end of removeAccount()


  public void removeDeposit(Deposit deposit) {
    String whereClause = DepositTable.Cols.UUID + " = ? ";
    String[] whereArgs = {deposit.getId().toString()};
    database.delete(DepositTable.NAME, whereClause, whereArgs);
  }// end of removeAccount()


  public void removeWithdraw(Withdraw withdraw) {
    String whereClause = WithdrawTable.Cols.UUID + " = ? ";
    String[] whereArgs = {withdraw.getId().toString()};
    database.delete(WithdrawTable.NAME, whereClause, whereArgs);
  }// end of removeAccount()


  public void removeExpenseById(Expense expense) {
    String whereClause = ExpenseTable.Cols.UUID + " = ? ";
    String[] whereArgs = {expense.getId().toString()};
    database.delete(AccountTable.NAME, whereClause, whereArgs);
  }// end of removeExpenseById()


  public void removeExpenseByMonth(int month) {
    String whereClause = ExpenseTable.Cols.MONTH + " = " + month;
    database.delete(AccountTable.NAME, whereClause, null);
  }// end of removeExpenseByMonth()


  public void updateDeposit(Deposit deposit) {
    ContentValues values = getDepositContentValues(deposit);
    String whereClause = DepositTable.Cols.UUID + " = ? ";
    String[] whereArgs = {deposit.getId().toString()};
    database.update(DepositTable.NAME, values, whereClause, whereArgs);
  }// end of updateDeposit()


  public void updateWithdraw(Withdraw withdraw) {
    ContentValues values = getWithdrawContentValues(withdraw);
    String whereClause = WithdrawTable.Cols.UUID + " = ? ";
    String[] whereArgs = {withdraw.getId().toString()};
    database.update(WithdrawTable.NAME, values, whereClause, whereArgs);
  }// end of updateWithdraw()


  public void updateAccount(Account account) {
    ContentValues values = getAccountContentValues(account);
    String whereClause = AccountTable.Cols.UUID + " = ? ";
    String[] whereArgs = {account.getId().toString()};
    database.update(AccountTable.NAME, values, whereClause, whereArgs);
  }// end of updateAccount()


  public void updateExpense(Expense expense) {
    ContentValues values = getExpenseContentValues(expense);
    String whereClause = AccountTable.Cols.UUID + " = ? ";
    String[] whereArgs = {expense.getId().toString()};
    database.update(AccountTable.NAME, values, whereClause, whereArgs);
  }// end of updateAccount()


  public Task getTask(UUID taskId) {
    String whereClause = AllTaskTable.Cols.TASK_UUID + " = ? ";

    String[] whereArgs = {taskId.toString()};
    try (TaskCursorWrapper cursor = queryTasks(AllTaskTable.NAME, whereClause, whereArgs)) {
      if (cursor.getCount() == 0)
        return null;

      cursor.moveToFirst();
      return cursor.getAllTasks();

    }

  }// end of getTasks()


  public void addTask(Task task) {
    ContentValues values = getTaskContentValues(task);
    database.insert(AllTaskTable.NAME, null, values);
  }// end of addTask()


  public void updateTask(Task task) {
    String taskIdString = task.getTaskId().toString();
    ContentValues values = getTaskContentValues(task);
    String whereClause = AllTaskTable.Cols.TASK_UUID + " = ? ";
    String[] whereArgs = {taskIdString};
    database.update(AllTaskTable.NAME, values, whereClause, whereArgs);
  }


  public List<Task> getAllTasks() {
    List<Task> allTasks = new ArrayList<>();
    TaskCursorWrapper cursor = queryTasks(AllTaskTable.NAME, null, null);
    if (cursor.getCount() > 0) {
      try {
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
          allTasks.add(cursor.getAllTasks());
          cursor.moveToNext();

        }
      } finally {
        cursor.close();
      }
    }
    return allTasks;
  }// end of getAllTasks()


  public List<Task> getUndoneTasks() {
    List<Task> allTasks = new ArrayList<>();
    String whereClause = AllTaskTable.Cols.DONE_STATUS + " = 0";
    TaskCursorWrapper cursor = queryTasks(AllTaskTable.NAME, whereClause, null);
    if (cursor.getCount() > 0) {
      try {
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
          allTasks.add(cursor.getAllTasks());
          cursor.moveToNext();

        }
      } finally {
        cursor.close();
      }
    }
    return allTasks;
  }// end of getTasks()


  public void removeTask(Task task) {
    String whereClause = AllTaskTable.Cols.TASK_UUID + " = ? ";
    String[] whereArgs = {task.getTaskId().toString()};
    database.delete(AllTaskTable.NAME, whereClause, whereArgs);
  }// end of removeTasks()


  public ContentValues getDepositContentValues(Deposit deposit) {
    ContentValues values = new ContentValues();
    values.put(DepositTable.Cols.UUID, deposit.getId().toString());
    values.put(DepositTable.Cols.AMOUNT, deposit.getAmount());
    values.put(DepositTable.Cols.DATE, deposit.getPersianDate().getTime());
    values.put(DepositTable.Cols.MONTH, deposit.getPersianDate().getShMonth());
    values.put(DepositTable.Cols.BANK_NAME, deposit.getBankName());
    values.put(DepositTable.Cols.ACCOUNT_NUMBER, deposit.getAccountNumber());
    values.put(DepositTable.Cols.IGNORED, deposit.isIgnored() ? 1 : 0);

    return values;
  }// end of getDepositContentValues()


  public ContentValues getWithdrawContentValues(Withdraw withdraw) {
    ContentValues values = new ContentValues();
    values.put(WithdrawTable.Cols.UUID, withdraw.getId().toString());
    values.put(WithdrawTable.Cols.AMOUNT, withdraw.getAmount());
    values.put(WithdrawTable.Cols.DATE, withdraw.getPersianDate().getTime());
    values.put(WithdrawTable.Cols.MONTH, withdraw.getPersianDate().getShMonth());
    values.put(WithdrawTable.Cols.BANK_NAME, withdraw.getBankName());
    values.put(WithdrawTable.Cols.ACCOUNT_NUMBER, withdraw.getAccountNumber());
    values.put(WithdrawTable.Cols.IGNORED, withdraw.isIgnored() ? 1 : 0);

    return values;
  }// end of getWithdrawContentValues()


  public ContentValues getExpenseContentValues(Expense expense) {
    ContentValues values = new ContentValues();
    values.put(ExpenseTable.Cols.UUID, expense.getId().toString());
    values.put(ExpenseTable.Cols.TITLE, expense.getTitle());
    values.put(ExpenseTable.Cols.CATEGORY_NAME, expense.getCategoryName());
    values.put(ExpenseTable.Cols.CATEGORY_ITEM, expense.getCategoryItem());
    values.put(ExpenseTable.Cols.DATE, expense.getDate().getTime());
    values.put(ExpenseTable.Cols.MONTH, expense.getDate().getShMonth());
    values.put(ExpenseTable.Cols.AMOUNT, expense.getAmount());
    values.put(ExpenseTable.Cols.WITHDRAW, expense.isWithdarw() ? 1 : 0);

    return values;
  }// end of getExpenseContentValues()


  public ContentValues getAccountContentValues(Account account) {
    ContentValues values = new ContentValues();
    values.put(AccountTable.Cols.UUID, account.getId().toString());
    values.put(AccountTable.Cols.NAME, account.getName());
    values.put(AccountTable.Cols.ACCOUNT_NUMBER, account.getAccountNumber());
    values.put(AccountTable.Cols.SMS_NUMBER, account.getSmsNumber());

    return values;
  }// end of getAccountContentValues()


  private static ContentValues getTaskContentValues(Task task) {
    ContentValues values = new ContentValues();
    values.put(AllTaskTable.Cols.TASK_UUID, task.getTaskId().toString());
    values.put(AllTaskTable.Cols.TITLE, task.getTitle());
    values.put(AllTaskTable.Cols.INITIAL, task.getInitial());
    if (task.getDate() != 0)
      values.put(AllTaskTable.Cols.DATE, task.getDate());

    if (task.getTime() != 0)
      values.put(AllTaskTable.Cols.TIME, task.getTime());

    values.put(AllTaskTable.Cols.IMPORTANCE_STATUS, task.isImportant() ? 1 : 0);
    values.put(AllTaskTable.Cols.DONE_STATUS, task.isDone() ? 1 : 0);
    values.put(AllTaskTable.Cols.ALARM_REQUIRED_STATUS, task.isAlarmRequired() ? 1 : 0);

    return values;
  }


  public DepositCursorWrapper getDepositQuery(String tableName, String whereClause, String[] whereArgs) {
    Cursor cursor = database.query(
      tableName,
      null,
      whereClause,
      whereArgs,
      null,
      null,
      null);

    return new DepositCursorWrapper(cursor);
  }// end of getDepositQuery()


  public WithdrawCursorWrapper getWithdrawQuery(String tableName, String whereClause, String[] whereArgs) {
    Cursor cursor = database.query(
      tableName,
      null,
      whereClause,
      whereArgs,
      null,
      null,
      null);

    return new WithdrawCursorWrapper(cursor);
  }// end of getWithdrawQuery()


  public ExpenseCursorWrapper getExpenseQuery(String tableName, String whereClause, String[] whereArgs) {
    Cursor cursor = database.query(
      tableName,
      null,
      whereClause,
      whereArgs,
      null,
      null,
      null);

    return new ExpenseCursorWrapper(cursor);
  }// end of getExpenseQuery()


  public AccountCursorWrapper getAccountQuery(String tableName, String whereClause, String[] whereArgs) {
    Cursor cursor = database.query(
      tableName,
      null,
      whereClause,
      whereArgs,
      null,
      null,
      null);

    return new AccountCursorWrapper(cursor);
  }// end of getAccountQuery()


  private TaskCursorWrapper queryTasks(String tableName, String whereClause, String[] whereArgs) {
    Cursor cursor = database.query(
      tableName,
      null, /* cols == null returns all cols */
      whereClause,
      whereArgs,
      null,
      null,
      null);

    return new TaskCursorWrapper(cursor);
  } // end of queryAllTasks()

  public void addAllTransactions(String sender) {
    final String SMS_URI_INBOX = "content://sms/inbox";
    try {
      Uri uri = Uri.parse(SMS_URI_INBOX);
      String[] projection = new String[]{"_id", "address", "person", "body", "date", "type"};
      Cursor cursor = context.getContentResolver().query(
        uri,
        projection,
        "address='" + sender + "'",
        null,
        "date asc");

      if (cursor.moveToFirst()) {
        int index_Body = cursor.getColumnIndex("body");
        int index_Date = cursor.getColumnIndex("date");
        do {
          String smsBody = cursor.getString(index_Body);
          long smsDate = cursor.getLong(index_Date);

          addMessage(smsBody, new PersianDate(smsDate));
        } while (cursor.moveToNext());

        if (!cursor.isClosed())
          cursor.close();
      }
    } catch (
      SQLiteException ex) {
      Log.d("SQLiteException", ex.getMessage());
    }
  }

  public void addMessage(String message, PersianDate messageDate) {
    List<String> messageSplits = splitMessage(message);
    boolean isWithdraw = messageSplits.get(1).contains("-") || messageSplits.get(1).contains("برداشت");

    if (isWithdraw) {
      Withdraw withdraw = new Withdraw();
      withdraw.setBankName(messageSplits.get(0));
      withdraw.setDate(messageDate);
      withdraw.setAmount(getDoubleAmount(messageSplits.get(1)));
      withdraw.setAccountNumber(messageSplits.get(2).replace("حساب:", ""));
      addWithdraw(withdraw);

    } else {
      Deposit deposit = new Deposit();
      deposit.setBankName(messageSplits.get(0));
      deposit.setDate(messageDate);
      deposit.setAmount(getDoubleAmount(messageSplits.get(1)));
      deposit.setAccountNumber(messageSplits.get(2).replace("حساب:", ""));
      addDeposit(deposit);
    }

  }


  private double getDoubleAmount(String amount) {
    int indexCounter = 0;
    amount = amount.replace(",", "");
    amount = amount.replace("+", "");
    amount = amount.replace("-", "");

    while (amount.charAt(indexCounter) != ':')
      indexCounter++;

    amount = amount.substring(indexCounter + 1);

    return Double.parseDouble(amount) / 10;
  }


  public List<String> splitMessage(String message) {
    String[] roughSplits = message.split("\\r?\\n");
    List<String> messageSplits = new ArrayList<>();
    for (String split: roughSplits) {
      if (!split.trim().equals(""))
        messageSplits.add(split);
    }
    return messageSplits;
  }
}
