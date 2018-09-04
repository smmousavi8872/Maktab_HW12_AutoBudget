package com.developer.smmousavi.maktab_hw12_autobudget.mvc.controller.fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.smmousavi.maktab_hw12_autobudget.R;
import com.developer.smmousavi.maktab_hw12_autobudget.mvc.controller.activities.TaskDetailPagerActivity;
import com.developer.smmousavi.maktab_hw12_autobudget.mvc.database.Repository;
import com.developer.smmousavi.maktab_hw12_autobudget.mvc.model.Task;
import com.developer.smmousavi.maktab_hw12_autobudget.mvc.utilites.FontChange;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ir.hamsaa.persiandatepicker.util.PersianCalendar;
import saman.zamani.persiandate.PersianDate;


/**
 * A simple {@link Fragment} subclass.
 */
public class TasksListFragment extends Fragment {

  private RecyclerView mRecyclerView;
  private List<Task> mTasksList;
  private TaskAdapter mTaskAdapter;
  private ImageView mEmptyListImg;
  private Repository repository;
  private Typeface fntDirooz;
  private Typeface fntPrastoo;

  public TasksListFragment() {
    /* Required empty public constructor */
  }


  public static TasksListFragment newInstance() {
    Bundle args = new Bundle();

    TasksListFragment fragment = new TasksListFragment();
    fragment.setArguments(args);
    return fragment;

  }// end of newInstance()


  @Override
  public void onResume() {
    super.onResume();
    updateListUI();
  }

  @RequiresApi(api = Build.VERSION_CODES.O)
  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
    repository = Repository.getInstance(getActivity());

  }// end of onCreate()


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    /* Inflate the layout for this fragment */
    View view = inflater.inflate(R.layout.fragment_tasks_list, container, false);
    getViews(view);

    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    updateListUI();

    return view;
  }// end of onCreateView


  public void getViews(View view) {
    mEmptyListImg = view.findViewById(R.id.empty_list);
    mRecyclerView = view.findViewById(R.id.recycler_view);
  }// end of getViews()


  public void updateListUI() {
    mTasksList = Repository.getInstance(getActivity()).getAllTasks();

    if (mTasksList.size() > 0) {
      mEmptyListImg.setVisibility(View.GONE);

    } else {
      mEmptyListImg.setVisibility(View.VISIBLE);

    }
    mTaskAdapter = new TaskAdapter(mTasksList);
    mRecyclerView.setAdapter(mTaskAdapter);
  }// end of updateListUI()


  private class TaskViewHolder extends RecyclerView.ViewHolder {
    private TextView mTaskTitleTxt;
    private TextView mTaskDateAndTimeTxt;
    private Button mTaskIsDoneBtn;
    private Task mClikedTask;

    public TaskViewHolder(final View itemView) {
      super(itemView);
      getViews(itemView);

      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Intent intent = TaskDetailPagerActivity.newIntent(getActivity(), mClikedTask.getTaskId());
          startActivity(intent);
        }
      });

      mTaskIsDoneBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          mClikedTask.setDone(true);
          Repository.getInstance(getActivity()).updateTask(mClikedTask);
          makeSnackbarAlert(String.format("\'%s\' انجام شد ", mClikedTask.getTitle()));
          hideView(view);
        }
      });
    }/* end of TaskViewHolder() */


    private void getViews(View itemView) {
      mTaskTitleTxt = itemView.findViewById(R.id.txt_list_item_title);
      mTaskDateAndTimeTxt = itemView.findViewById(R.id.txt_list_item_subtitle);
      mTaskIsDoneBtn = itemView.findViewById(R.id.btn_list_item_done);
      FontChange fc = new FontChange(getActivity());
      fc.setTextViewFont(FontChange.DIROOZ, mTaskTitleTxt, mTaskDateAndTimeTxt);
      fc.setButtonFont(FontChange.PARASTOO, mTaskIsDoneBtn);
    }/* end of getViews() */


    private void hideView(View view) {
      TranslateAnimation animate = new TranslateAnimation(0, -view.getWidth(), 0, 0);
      animate.setDuration(500);
      animate.setFillAfter(true);
      view.startAnimation(animate);
      view.setVisibility(View.GONE);
      final Handler handler = new Handler();
      handler.postDelayed(new Runnable() {
        @Override
        public void run() {
          updateListUI();
        }
      }, 500);
    }// endOfHideView()


    public void bindeTaskViewHolder(Task task) {
      mClikedTask = task;
      mTaskTitleTxt.setText(mClikedTask.getTitle());

      long taskDate = task.getDate();
      long taskTime = task.getTime();

      PersianCalendar date = new PersianCalendar(taskDate);
      PersianDate time = new PersianDate(taskTime);

      if (taskDate != 0 & taskTime != 0) {
        String dateFormat = date.getPersianShortDate() + ", " + time.getHour() + ":" + time.getMinute();
        mTaskDateAndTimeTxt.setText(dateFormat);

      } else if (taskDate != 0 && taskTime == 0) {
        String dateFormat = date.getPersianShortDate();
        mTaskDateAndTimeTxt.setText(dateFormat);

      } else if (taskDate == 0 && taskTime != 0) {
        String dateFormat = time.getHour() + ":" + time.getMinute();
        mTaskDateAndTimeTxt.setText(dateFormat);

      } else
        mTaskDateAndTimeTxt.setText(R.string.task_item_subtitle_date_time_not_set);

      if (task.isDone())
        mTaskIsDoneBtn.setVisibility(View.INVISIBLE);

    }/* end of bindeTaskViewHolder() */
  }// end of TaskViewHolder{}


  private class TaskAdapter extends RecyclerView.Adapter<TaskViewHolder> {

    private List<Task> mTasksList;

    public TaskAdapter(List<Task> tasksList) {
      mTasksList = tasksList;

    } /* end of TaskAdapter{} */

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      LayoutInflater inflater = LayoutInflater.from(getActivity());
      View view = inflater.inflate(R.layout.task_item_view, parent, false);
      ImageView bellImg = view.findViewById(R.id.list_item_bell);
      ImageView starImg = view.findViewById(R.id.list_item_star);

      if (viewType == 0) {
        bellImg.setVisibility(View.GONE);

      } else if (viewType == 1) {
        starImg.setVisibility(View.GONE);

      } else if (viewType == 2) {
        bellImg.setVisibility(View.GONE);
        starImg.setVisibility(View.GONE);

      }
      return new TaskViewHolder(view);
    } /* end of onCreateViewHolder() */


    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
      holder.bindeTaskViewHolder(mTasksList.get(position));

    } /* end of onBindViewHolder() */

    @Override
    public int getItemCount() {
      return mTasksList.size();
    } /* end of getItemCount() */


    @Override
    public int getItemViewType(int position) {
      if (mTasksList.get(position).isImportant() && !mTasksList.get(position).isAlarmRequired())
        return 0;

      else if (!mTasksList.get(position).isImportant() && mTasksList.get(position).isAlarmRequired())
        return 1;

      else if (!mTasksList.get(position).isAlarmRequired() && !mTasksList.get(position).isImportant())
        return 2;

      else
        return 3;

    } /* end of getItemViewType */

  }// end of TaskAdapter{}


  public void showAlertDialog(int titleResId, DialogInterface.OnClickListener okAction,
                              DialogInterface.OnClickListener cancelAction) {
    new AlertDialog.Builder(getActivity())
      .setTitle(titleResId)
      .setPositiveButton(android.R.string.ok, okAction)
      .setNegativeButton(android.R.string.cancel, cancelAction)
      .show();
  } // end of showAlertDialog()


  private void makeToastAlert(String alertText) {
    Toast.makeText(getActivity(), alertText, Toast.LENGTH_SHORT).show();
  }// end of makeToaskAlert()


  private void makeSnackbarAlert(String alertText) {
    Snackbar.make(getView(), alertText, Snackbar.LENGTH_SHORT).show();
  }// end of makeSnackBarAlert


}