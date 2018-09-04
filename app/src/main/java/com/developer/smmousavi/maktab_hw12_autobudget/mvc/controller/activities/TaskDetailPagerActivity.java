package com.developer.smmousavi.maktab_hw12_autobudget.mvc.controller.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.developer.smmousavi.maktab_hw12_autobudget.R;
import com.developer.smmousavi.maktab_hw12_autobudget.mvc.controller.fragments.TaskDetailFragment;
import com.developer.smmousavi.maktab_hw12_autobudget.mvc.database.Repository;
import com.developer.smmousavi.maktab_hw12_autobudget.mvc.model.Task;

import java.util.List;
import java.util.UUID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;


public class TaskDetailPagerActivity extends AppCompatActivity {

  public static final String EXTRA_TASK_ID =
    "com.example.smmousavi.maktab_hw82_remindemelater.mvc.controller.activities.extra_task_id";

  private ViewPager viewPager;
  private List<Task> tasksList;
  private UUID taskId;


  public static Intent newIntent(Context orgin, UUID taskId) {
    Intent intent = new Intent(orgin, TaskDetailPagerActivity.class);
    intent.putExtra(EXTRA_TASK_ID, taskId);

    return intent;
  }// end of newIntent()


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_task_detail_pager);


    viewPager = findViewById(R.id.task_detail_view_pager);

    Bundle extras = getIntent().getExtras();
    taskId = (UUID) extras.getSerializable(EXTRA_TASK_ID);

    tasksList = Repository.getInstance(this).getAllTasks();

    setTitle("بروز رسانی موعد");

    viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
      @Override
      public Fragment getItem(int position) {
        UUID id = tasksList.get(position).getTaskId();
        return TaskDetailFragment.newInstance(id);
      }

      @Override
      public int getCount() {
        return tasksList.size();
      }

    }); /* end of setAdapter({...}) */
    viewPager.setCurrentItem(findCurrentItem());

  }// end of onCreate()


  private int findCurrentItem() {
    for (int i = 0; i < tasksList.size(); i++) {
      if (tasksList.get(i).getTaskId().equals(taskId))
        return i;

    }
    return -1;
  }// end of findCurrentItem()


}
