package com.developer.smmousavi.maktab_hw12_autobudget.mvc.controller.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.developer.smmousavi.maktab_hw12_autobudget.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExpenseReportFragment extends Fragment {

  public static final String TAG = "ExpenseReportFragment";

  private TabLayout tabLayout;
  private ViewPager viewPager;
  private FragmentStatePagerAdapter adapter;
  private List<Fragment> expenseReportFragments;
  private List<String> expenseReportTitles;


  public static ExpenseReportFragment newInstance() {

    Bundle args = new Bundle();

    ExpenseReportFragment fragment = new ExpenseReportFragment();
    fragment.setArguments(args);
    return fragment;
  }


  public ExpenseReportFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_expense_report, container, false);

    expenseReportFragments = new ArrayList<>();
    expenseReportTitles = new ArrayList<>();
    viewPager = view.findViewById(R.id.view_pager);
    tabLayout = view.findViewById(R.id.tab_layout);

    addFragment(PrevMonthReportFragment.newInstance(), getResources().getString(R.string.last_month_text));
    addFragment(ThisMonthReportFragment.newInstance(), getResources().getString(R.string.this_month_text));

    adapter = new FragmentStatePagerAdapter(getFragmentManager()) {
      @Override
      public Fragment getItem(int position) {
        return expenseReportFragments.get(position);
      }

      @Override
      public int getCount() {
        return expenseReportFragments.size();
      }

      @Nullable
      @Override
      public CharSequence getPageTitle(int position) {
        return expenseReportTitles.get(position);
      }
    };

    viewPager.setAdapter(adapter);
    viewPager.setCurrentItem(1);

    tabLayout.setupWithViewPager(viewPager);

    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      @Override
      public void onPageSelected(int position) {
        //this method might be useful
      }

      @Override
      public void onPageScrollStateChanged(int state) {

      }
    });

    return view;
  }


  private void addFragment(Fragment musicFragmetn, String title) {
    expenseReportFragments.add(musicFragmetn);
    expenseReportTitles.add(title);

  }


}
