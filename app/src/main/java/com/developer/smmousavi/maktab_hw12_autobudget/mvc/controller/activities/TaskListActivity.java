package com.developer.smmousavi.maktab_hw12_autobudget.mvc.controller.activities;

import com.developer.smmousavi.maktab_hw12_autobudget.mvc.controller.fragments.TasksListFragment;

import androidx.fragment.app.Fragment;

public class TaskListActivity extends _SingleFragmentActivity {

  public static final String TAG = "com.developer.smmousavi.maktab_hw12_autobudget.mvc.controller.activities.TAG";

  @Override
  public Fragment createFragment() {
    return TasksListFragment.newInstance();
  }

  @Override
  public String getTag() {
    return TAG;
  }

}
