package com.developer.smmousavi.maktab_hw12_autobudget.mvc.controller.receivers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.developer.smmousavi.maktab_hw12_autobudget.R;
import com.developer.smmousavi.maktab_hw12_autobudget.mvc.controller.activities.UserBottomNavigationActivity;
import com.developer.smmousavi.maktab_hw12_autobudget.mvc.database.Repository;
import com.developer.smmousavi.maktab_hw12_autobudget.mvc.model.Account;
import com.developer.smmousavi.maktab_hw12_autobudget.mvc.model.Deposit;
import com.developer.smmousavi.maktab_hw12_autobudget.mvc.model.Withdraw;

import java.util.ArrayList;
import java.util.List;

import androidx.core.app.NotificationCompat;
import saman.zamani.persiandate.PersianDate;

public class SMSReceiver extends BroadcastReceiver {

  private static final int YOUR_PI_REQ_CODE = 0;
  private static final int NOTIF_ID = 1;
  private Context context;
  private Repository repository;
  private List<Account> accountList;

  public void onReceive(Context context, Intent intent) {
    repository = Repository.getInstance(context);
    Account myAccount = new Account();
    myAccount.setName("بانک ملی");
    myAccount.setAccountNumber("71008");
    myAccount.setSmsNumber("+989127638779");
    repository.addAccount(myAccount);

    final Bundle bundle = intent.getExtras();
    try {
      String message = "";
      String sender = "";
      if (bundle != null) {
        final Object[] pdusObj = (Object[]) bundle.get("pdus");
        for (int i = 0; i < pdusObj.length; i++) {
          SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
          sender = sms.getDisplayOriginatingAddress();
          message = sms.getDisplayMessageBody();
          String formattedText = String.format("Message from %s is %s", sender, message);

          Log.i("TAG7", formattedText);
        }
      }
      accountList = repository.getAccountList();

      if (accountList.size() > 0) {
        for (Account account: accountList) {
          if (account.getSmsNumber().equals(sender) && !message.equals("")) {
            addMessage(message);
            return;
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

  }


  public void addMessage(String message) {
    List<String> messageSplits = splitMessage(message);
    boolean isWithdraw = messageSplits.get(1).contains("-") || messageSplits.get(1).contains("برداشت");
    PersianDate persianDate = PersianDate.today();

    if (isWithdraw) {
      Withdraw withdraw = new Withdraw();
      String bankName = messageSplits.get(0);
      String amount = messageSplits.get(1);
      String accountNumber = messageSplits.get(2).replace("حساب:", "");
      withdraw.setBankName(bankName);
      withdraw.setDate(persianDate);
      withdraw.setAmount(getDoubleAmount(amount));
      withdraw.setAccountNumber(accountNumber);
      repository.addWithdraw(withdraw);
      String notificationText = String.format("مبلغ %s ریال از حساب %s در %s برداشت شد.", amount, accountNumber, bankName);
      buildNotificaion(context, notificationText);

    } else {
      Deposit deposit = new Deposit();
      String bankName = messageSplits.get(0);
      String amount = messageSplits.get(1);
      String accountNumber = messageSplits.get(2).replace("حساب:", "");
      deposit.setBankName(bankName);
      deposit.setDate(persianDate);
      deposit.setAmount(getDoubleAmount(amount));
      deposit.setAccountNumber(accountNumber);
      repository.addDeposit(deposit);
      String notificationText = String.format("مبلغ %s ریال به حساب %s در %s واریز شد.", amount, accountNumber, bankName);
      buildNotificaion(context, notificationText);
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

    return (Double.parseDouble(amount)) / 10;
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


  public void buildNotificaion(Context ctx, String notificationText) {
    Log.i("TAG11", "notifcation is called");
    NotificationManager mNotificationManager;

    NotificationCompat.Builder mBuilder =
      new NotificationCompat.Builder(ctx.getApplicationContext(), "notify_001");
    Intent ii = new Intent(ctx.getApplicationContext(), UserBottomNavigationActivity.class);
    PendingIntent pendingIntent = PendingIntent.getActivity(ctx, 0, ii, 0);

    NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
    bigText.setBigContentTitle("Today's Bible Verse");
    bigText.setSummaryText("Text in detail");

    mBuilder.setContentIntent(pendingIntent);
    mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
    mBuilder.setContentTitle("Your Title");
    mBuilder.addAction(R.mipmap.ic_launcher_round, "Your Title", pendingIntent);
    mBuilder.setContentText("Your text");
    mBuilder.setPriority(Notification.PRIORITY_MAX);
    mBuilder.setStyle(bigText);

    mNotificationManager =
      (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);


    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      NotificationChannel channel = new NotificationChannel("notify_001",
        "Channel human readable title",
        NotificationManager.IMPORTANCE_DEFAULT);
      mNotificationManager.createNotificationChannel(channel);
    }

    mNotificationManager.notify(0, mBuilder.build());
  }
  // .setSmallIcon(android.R.drawable.ic_popup_reminder)

}

