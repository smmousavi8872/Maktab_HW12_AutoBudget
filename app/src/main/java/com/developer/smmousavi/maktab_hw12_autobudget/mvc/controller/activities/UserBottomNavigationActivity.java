package com.developer.smmousavi.maktab_hw12_autobudget.mvc.controller.activities;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.developer.smmousavi.maktab_hw12_autobudget.R;
import com.developer.smmousavi.maktab_hw12_autobudget.mvc.controller.fragments.ExpenseListFragment;
import com.developer.smmousavi.maktab_hw12_autobudget.mvc.controller.fragments.ExpenseReportFragment;
import com.developer.smmousavi.maktab_hw12_autobudget.mvc.controller.fragments.TasksListFragment;
import com.developer.smmousavi.maktab_hw12_autobudget.mvc.database.Repository;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import me.anwarshahriar.calligrapher.Calligrapher;

public class UserBottomNavigationActivity extends AppCompatActivity {

  public static final String SAVE_INSTANCE_CREATED = "save_instance_created";
  public static final int PERMISSION_READ_SMS_CODE = 0;
  public static final int PERMISSION_RECEIVE_SMS_CODE = 1;

  private FloatingActionButton fab;
  private BottomNavigationView navView;
  private boolean created;
  private AppCompatActivity activity;


  @Override
  public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
    outState.putBoolean(SAVE_INSTANCE_CREATED, created);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user_bottom_navigation);
    activity = this;

    Calligrapher calligrapher = new Calligrapher(this);
    calligrapher.setFont(this, "fonts/Parastoo-FD.ttf", true);

    if (savedInstanceState != null)
      created = true;

    final Handler handler = new Handler();
    final Runnable runnable = new Runnable() {
      @Override
      public void run() {
        addFragment(ExpenseReportFragment.newInstance(), ExpenseReportFragment.TAG);
      }
    };

    if (!created) {
      //getSupportActionBar().setTitle(Html.fromHtml("<font color=\"black\">" + getString(R.string.report_text) + "</font>"));
      //getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.simple_background_white));
      handler.postDelayed(runnable, 500);
    }


    fab = findViewById(R.id.new_expense_fab);
    navView = findViewById(R.id.bottom_navigation_view);

    navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
          case R.id.expense_report_item:
            //getSupportActionBar().setTitle(Html.fromHtml("<font color=\"black\">" + getString(R.string.report_text) + "</font>"));
            //getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.simple_background_white));
            addFragment(ExpenseReportFragment.newInstance(), ExpenseReportFragment.TAG);
            return true;
          case R.id.expense_list_item:
            buildNotificaion(activity, "");
            //getSupportActionBar().setTitle(Html.fromHtml("<font color=\"black\">" + getString(R.string.charge_list_text) + "</font>"));
            //getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.simple_background_white));
            addFragment(ExpenseListFragment.newInstance(), ExpenseListFragment.TAG);
            return true;
          case R.id.deadline_list_item:
            //getSupportActionBar().setTitle(Html.fromHtml("<font color=\"black\">" + getString(R.string.deadline_list_text) + "</font>"));
            //getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.simple_background_white));
            addFragment(TasksListFragment.newInstance(), ExpenseListFragment.TAG);
            return true;
          default:
            return false;
        }
      }
    });

    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = TaskDetailActivity.newIntent(activity);
        startActivity(intent);
      }
    });

    if (ContextCompat.checkSelfPermission(this,
      Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this,
        new String[]{Manifest.permission.READ_SMS}, PERMISSION_READ_SMS_CODE);

    }

    if (ContextCompat.checkSelfPermission(this,
      Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this,
        new String[]{Manifest.permission.RECEIVE_SMS}, PERMISSION_RECEIVE_SMS_CODE);
    }


  }


  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    if (requestCode == PERMISSION_READ_SMS_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED)
      Repository.getInstance(this).addAllTransactions("+9820004000");
  }

  private void addFragment(Fragment fragment, String tag) {
    FragmentManager fm = getSupportFragmentManager();
    Fragment foundFragment = fm.findFragmentById(R.id.user_fragments_container);
    if (foundFragment == null) {
      fm.beginTransaction()
        .add(R.id.user_fragments_container, fragment, tag)
        .commit();

    } else {
      fm.beginTransaction()
        .remove(foundFragment)
        .add(R.id.user_fragments_container, fragment, tag)
        .commit();
    }
  }


  public void buildNotificaion(Context ctx, String notificationText) {
    Log.i("TAG11", "notifcation is called");
    NotificationManager mNotificationManager;

    NotificationCompat.Builder mBuilder =
      new NotificationCompat.Builder(ctx.getApplicationContext(), "notify_001");
    Intent ii = new Intent(ctx.getApplicationContext(), TaskDetailActivity.class);
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

}
