package com.developer.smmousavi.maktab_hw12_autobudget.mvc.controller.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.developer.smmousavi.maktab_hw12_autobudget.mvc.controller.fragments.TaskDetailFragment;

import androidx.fragment.app.Fragment;

public class TaskDetailActivity extends _SingleFragmentActivity {

  public static final String TAG = "com.developer.smmousavi.maktab_hw12_autobudget.mvc.controller.activities.TAG";

  public static Intent newIntent(Context orgin) {
    return new Intent(orgin, TaskDetailActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setTitle("موعد جدید");
  }


  @Override
  public Fragment createFragment() {
    return TaskDetailFragment.newInstance(null);
  }

  @Override
  public String getTag() {
    return TAG;
  }

}
