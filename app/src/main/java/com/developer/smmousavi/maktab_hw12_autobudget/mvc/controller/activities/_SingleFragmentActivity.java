package com.developer.smmousavi.maktab_hw12_autobudget.mvc.controller.activities;

import android.os.Bundle;

import com.developer.smmousavi.maktab_hw12_autobudget.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


public abstract class _SingleFragmentActivity extends AppCompatActivity {

  public abstract Fragment createFragment();

  public abstract String getTag();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_single_fragment);

    FragmentManager fm = getSupportFragmentManager();
    Fragment foundFragment = fm.findFragmentById(R.id.fragment_container);

    if (foundFragment == null) {
      foundFragment = createFragment();
      String tag = getTag();
      fm.beginTransaction()
        .add(R.id.fragment_container, foundFragment, tag)
        .commit();
    }
  }
}
