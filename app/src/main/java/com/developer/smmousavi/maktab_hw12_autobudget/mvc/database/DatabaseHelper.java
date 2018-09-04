package com.developer.smmousavi.maktab_hw12_autobudget.mvc.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.developer.smmousavi.maktab_hw12_autobudget.mvc.database.DBSchema.AccountTable;
import com.developer.smmousavi.maktab_hw12_autobudget.mvc.database.DBSchema.AllTaskTable;
import com.developer.smmousavi.maktab_hw12_autobudget.mvc.database.DBSchema.DepositTable;
import com.developer.smmousavi.maktab_hw12_autobudget.mvc.database.DBSchema.ExpenseTable;
import com.developer.smmousavi.maktab_hw12_autobudget.mvc.database.DBSchema.WithdrawTable;

import static com.developer.smmousavi.maktab_hw12_autobudget.mvc.database.DBSchema.DATABASE_VERSION;

public class DatabaseHelper extends SQLiteOpenHelper {

  SQLiteDatabase db;

  public DatabaseHelper(Context context) {
    super(context, DBSchema.DATABASE_NAME, null, DATABASE_VERSION);
  }

  public static final String CREATE_DEPOSIT_TABLE = "CREATE TABLE " + DepositTable.NAME + "("
    + DepositTable.Cols.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
    + DepositTable.Cols.UUID + ", "
    + DepositTable.Cols.AMOUNT + ", "
    + DepositTable.Cols.DATE + ", "
    + DepositTable.Cols.MONTH + ", "
    + DepositTable.Cols.BANK_NAME + ", "
    + DepositTable.Cols.ACCOUNT_NUMBER + ", "
    + DepositTable.Cols.IGNORED
    + ")";


  public static final String CREATE_WITHDRAW_TABLE = "CREATE TABLE " + WithdrawTable.NAME + "("
    + WithdrawTable.Cols.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
    + WithdrawTable.Cols.UUID + ", "
    + WithdrawTable.Cols.AMOUNT + ", "
    + WithdrawTable.Cols.DATE + ", "
    + WithdrawTable.Cols.MONTH + ", "
    + WithdrawTable.Cols.BANK_NAME + ", "
    + WithdrawTable.Cols.ACCOUNT_NUMBER + ", "
    + WithdrawTable.Cols.IGNORED
    + ")";


  public static final String CREATE_ACCOUNT_TABLE = "CREATE TABLE " + AccountTable.NAME + "("
    + AccountTable.Cols.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
    + AccountTable.Cols.UUID + ", "
    + AccountTable.Cols.NAME + ", "
    + AccountTable.Cols.ACCOUNT_NUMBER + ", "
    + AccountTable.Cols.SMS_NUMBER
    + ")";


  public static final String CREATE_EXPENSE_TABEL = "CREATE TABLE " + ExpenseTable.NAME + "("
    + ExpenseTable.Cols.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
    + ExpenseTable.Cols.UUID + ", "
    + ExpenseTable.Cols.TITLE + ", "
    + ExpenseTable.Cols.CATEGORY_NAME + ", "
    + ExpenseTable.Cols.CATEGORY_ITEM + ", "
    + ExpenseTable.Cols.DATE + ", "
    + ExpenseTable.Cols.MONTH + ", "
    + ExpenseTable.Cols.AMOUNT + ", "
    + ExpenseTable.Cols.WITHDRAW
    + ")";

  public static final String CREATE_ALL_TASK_TABLE =
    "create table if not exists " + AllTaskTable.NAME + "(" +
      "_id integer primary key autoincrement, " +
      AllTaskTable.Cols.TASK_UUID + "," +
      AllTaskTable.Cols.TITLE + "," +
      AllTaskTable.Cols.INITIAL + "," +
      AllTaskTable.Cols.DATE + "," +
      AllTaskTable.Cols.TIME + "," +
      AllTaskTable.Cols.IMPORTANCE_STATUS + "," +
      AllTaskTable.Cols.DONE_STATUS + "," +
      AllTaskTable.Cols.ALARM_REQUIRED_STATUS +
      ")";// end of CREATE_ALL_TASK_TABLE


  @Override
  public void onCreate(SQLiteDatabase sqLiteDatabase) {
    sqLiteDatabase.execSQL(CREATE_DEPOSIT_TABLE);
    sqLiteDatabase.execSQL(CREATE_WITHDRAW_TABLE);
    sqLiteDatabase.execSQL(CREATE_ACCOUNT_TABLE);
    sqLiteDatabase.execSQL(CREATE_EXPENSE_TABEL);
    sqLiteDatabase.execSQL(CREATE_ALL_TASK_TABLE);
    db = sqLiteDatabase;
  }// end of onCreate()


  @Override
  public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DepositTable.NAME);
    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WithdrawTable.NAME);
    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + AccountTable.NAME);
    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + AllTaskTable.NAME);
    onCreate(sqLiteDatabase);
  }// end of onUpgrade()


}