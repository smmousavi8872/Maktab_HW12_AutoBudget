package com.developer.smmousavi.maktab_hw12_autobudget.mvc.database;

public class DBSchema {

  public static final int DATABASE_VERSION = 1;
  public static final String DATABASE_NAME = "database";

  public static final class DepositTable {
    public static final String NAME = "deposit_table";

    public static final class Cols {
      public static final String COLUMN_ID = "id";
      public static final String UUID = "uuid";
      public static final String AMOUNT = "amount";
      public static final String DATE = "date";
      public static final String MONTH = "month";
      public static final String BANK_NAME = "bank_name";
      public static final String ACCOUNT_NUMBER = "account_number";
      public static final String IGNORED = "ignored";

    }
  }

  public static final class WithdrawTable {
    public static final String NAME = "withdraw_table";

    public static final class Cols {
      public static final String COLUMN_ID = "id";
      public static final String UUID = "uuid";
      public static final String AMOUNT = "amount";
      public static final String DATE = "date";
      public static final String MONTH = "month";
      public static final String BANK_NAME = "bank_name";
      public static final String ACCOUNT_NUMBER = "account_number";
      public static final String IGNORED = "ignored";
    }
  }


  public static final class ExpenseTable {
    public static final String NAME = "expense_table";

    public static final class Cols {
      public static final String COLUMN_ID = "id";
      public static final String UUID = "uuid";
      public static final String TITLE = "title";
      public static final String AMOUNT = "amount";
      public static final String CATEGORY_NAME = "category_name";
      public static final String CATEGORY_ITEM = "category_item";
      public static final String DATE = "time_stamp";
      public static final String MONTH = "month";
      public static final String WITHDRAW = "withdraw";

    }
  }


  public static final class AccountTable {
    public static final String NAME = "account_table";

    public static final class Cols {
      public static final String COLUMN_ID = "id";
      public static final String UUID = "uuid";
      public static final String NAME = "name";
      public static final String ACCOUNT_NUMBER = "account_number";
      public static final String SMS_NUMBER = "sms_number";
    }
  }


  public static final class AllTaskTable {
    public static final String NAME = "all_tasks";

    public static final class Cols {
      public static final String TASK_UUID = "task_uuid";
      public static final String TITLE = "title";
      public static final String INITIAL = "initial";
      public static final String DATE = "date";
      public static final String TIME = "time";
      public static final String DONE_STATUS = "done_status";
      public static final String IMPORTANCE_STATUS = "importance_status";
      public static final String ALARM_REQUIRED_STATUS = "alarm_required_status";
    }

  }// end of AllTaskTable{}
}
