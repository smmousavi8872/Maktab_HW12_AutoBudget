package com.developer.smmousavi.maktab_hw12_autobudget.mvc.controller.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.smmousavi.maktab_hw12_autobudget.R;
import com.developer.smmousavi.maktab_hw12_autobudget.mvc.database.Repository;
import com.developer.smmousavi.maktab_hw12_autobudget.mvc.model.Task;
import com.developer.smmousavi.maktab_hw12_autobudget.mvc.utilites.FontChange;
import com.google.android.material.snackbar.Snackbar;

import java.util.UUID;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import ir.hamsaa.persiandatepicker.Listener;
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog;
import ir.hamsaa.persiandatepicker.util.PersianCalendar;
import saman.zamani.persiandate.PersianDate;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskDetailFragment extends Fragment {

  public static final String ARGS_TASK_ID = "args_task_id";
  public static final String DIALOG_DATE_TAG = "dialog_date_tag";
  public static final String DIALOG_TIME_TAG = "dialog_time_tag";
  public static final int REQUEST_DATE = 0;
  public static final int REQUEST_TIME = 1;

  private TextView mDecriptionTxt;
  private EditText mDecriptionEdt;
  private TextView mTaskDateTxt;
  private TextView mTaskTimeTxt;
  private TextView mImportatnTxt;
  private TextView mAlarmTxt;
  private TextView mDoneTxt;
  private Button mDateBtn;
  private Button mTimeBtn;
  private CheckBox mSetImportantChk;
  private CheckBox mSetDoneChk;
  private CheckBox mSetAlarmChk;
  private View[] arrViews;
  private Button mEditBtn;
  private Button mDoneBtn;
  private Button mDeleteBtn;
  private UUID taskId;
  private Task mClickedTask;
  private long mTaskDate;
  private long mTaskTime;
  private boolean doneStatusChanged;
  private boolean isNewTask;


  public static TaskDetailFragment newInstance(UUID taskId) {

    Bundle args = new Bundle();
    args.putSerializable(ARGS_TASK_ID, taskId);

    TaskDetailFragment fragment = new TaskDetailFragment();
    fragment.setArguments(args);
    return fragment;
  } // end of newInstance()


  public TaskDetailFragment() {
    /* Required empty public constructor */

  }// end of TaskDetailFragment()


  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode != Activity.RESULT_OK)
      return;
    if (requestCode == REQUEST_TIME) {
      long time = data.getLongExtra(TimePickerFragment.EXTRA_TIME, 0);
      if (time != 0) {
        updateTime(time);
        mTaskTime = time;
      }

    }
  }// end of onActivityResult()


  private void updateDate(long date) {
    PersianCalendar persianCalendar = new PersianCalendar(date);
    mDateBtn.setText(persianCalendar.getPersianLongDate());
  } // end of updateDate()


  private void updateTime(long time) {
    PersianDate pDate = new PersianDate(time);
    mTimeBtn.setText(pDate.getHour() + ":" + pDate.getMinute());
  }// end of updateTime()


  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);


  }// end of onCreate()


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    /* Inflate the layout for this fragment */
    View view = inflater.inflate(R.layout.fragment_task_detail, container, false);
    getViews(view);

    Bundle bundle = getArguments();
    taskId = (UUID) bundle.getSerializable(ARGS_TASK_ID);


    if (taskId == null) {
      mClickedTask = new Task();
      prepareForAdd();
      isNewTask = true;

    } else {
      mClickedTask = Repository.getInstance(getActivity()).getTask(taskId);
      prepareForUpdate();
      isNewTask = false;

    }

    mDateBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        PersianDatePickerDialog picker = new PersianDatePickerDialog(getActivity())
          .setPositiveButtonString("ثبت تاریخ")
          .setNegativeButton("انصراف")
          .setTodayButton("امروز")
          .setTodayButtonVisible(true)
          .setMaxYear(PersianDatePickerDialog.THIS_YEAR)
          .setMinYear(1300)
          .setMaxYear(1500)
          .setTypeFace(new FontChange(getActivity()).getFontDirooz())
          .setActionTextColor(Color.GRAY)
          .setListener(new Listener() {
            @Override
            public void onDateSelected(PersianCalendar persianCalendar) {
              mTaskDate = persianCalendar.getTimeInMillis();
              mDateBtn.setText(persianCalendar.getPersianLongDate());
            }

            @Override
            public void onDismissed() {

            }
          });

        picker.show();

      }
    });
    mTimeBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        mTaskTime = mClickedTask.getTime();
        if (mTaskTime == 0)
          mTaskTime = System.currentTimeMillis();

        FragmentManager fm = getFragmentManager();
        TimePickerFragment dialog = TimePickerFragment.newInstance(mTaskTime);
        dialog.setTargetFragment(TaskDetailFragment.this, REQUEST_TIME);
        dialog.show(fm, DIALOG_TIME_TAG);
      }
    });

    return view;
  }// end of onCreateView()


  public void getViews(View view) {
    FontChange fontChange = new FontChange(getActivity());
    arrViews = new View[]{
      mDecriptionTxt = view.findViewById(R.id.txt_detail_description),
      mDecriptionEdt = view.findViewById(R.id.edt_detail_description),
      mDateBtn = view.findViewById(R.id.btn_detail_date),
      mTimeBtn = view.findViewById(R.id.btn_detail_time),
      mSetImportantChk = view.findViewById(R.id.chk_detail_important),
      mSetDoneChk = view.findViewById(R.id.chk_detail_done),
      mSetAlarmChk = view.findViewById(R.id.chk_detail_set_alarm)
    };
    mTaskDateTxt = view.findViewById(R.id.edt_detail_date);
    mTaskTimeTxt = view.findViewById(R.id.edt_detail_time);
    mImportatnTxt = view.findViewById(R.id.txt_detail_important);
    mAlarmTxt = view.findViewById(R.id.txt_detail_set_alarm);
    mDoneTxt = view.findViewById(R.id.txt_detail_done);
    mEditBtn = view.findViewById(R.id.btn_detail_edit);
    mDoneBtn = view.findViewById(R.id.btn_detail_done);
    mDeleteBtn = view.findViewById(R.id.btn_detail_delete);

    fontChange.setTextViewFont(FontChange.DIROOZ, mDecriptionTxt, mTaskDateTxt, mTaskTimeTxt, mImportatnTxt, mAlarmTxt, mDoneTxt);
    fontChange.setButtonFont(FontChange.PARASTOO, mDateBtn, mTimeBtn, mEditBtn, mDoneBtn, mDeleteBtn);
    fontChange.setEditTextFont(FontChange.DIROOZ, mDecriptionEdt);
  }// end of getViews()


  public void prepareForAdd() {
    mEditBtn.setVisibility(View.GONE);
    mDeleteBtn.setVisibility(View.GONE);
    mDoneBtn.setText(R.string.add_task_button_title);

    mDoneBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String titleFromat = mDecriptionEdt.getText().toString().trim();
        if (!titleFromat.equals("")) {
          registerTaskDetails();

          Repository.getInstance(getActivity()).addTask(mClickedTask);
          /*if (!mClickedTask.isDone())
            Repository.getInstance(getActivity()).addUndoneTask(mClickedTask);

          else
            Repository.getInstance(getActivity()).addDoneTask(mClickedTask);*/

          getActivity().finish();
          makeToastAlert(R.string.new_task_added_alert);
        } else
          makeSnackbarAlert(R.string.title_is_not_set_alert);
      }
    });
  }// end of prepareForAdd()


  public void prepareForUpdate() {
    setEnabledViews(false);

    mDoneBtn.setVisibility(View.GONE);
    mDecriptionEdt.setText(mClickedTask.getTitle());
    long taskDate = mClickedTask.getDate();
    long taskTime = mClickedTask.getTime();
    if (taskDate != 0)
      updateDate(taskDate);

    if (taskTime != 0)
      updateTime(taskTime);

    mSetDoneChk.setChecked(mClickedTask.isDone());
    mSetImportantChk.setChecked(mClickedTask.isImportant());
    mSetAlarmChk.setChecked(mClickedTask.isAlarmRequired());

    mEditBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        setEnabledViews(true);
        hideAnimation(mEditBtn);
        hideAnimation(mDeleteBtn);
        mDoneBtn.setVisibility(View.VISIBLE);
        mDoneBtn.setText(R.string.update_task_button_title);

        mSetDoneChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            if (isChecked)
              makeSnackbarAlert(R.string.set_done_alert);

            else
              makeSnackbarAlert(R.string.set_undone_alert);
          }
        });

        mSetImportantChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            if (isChecked)
              makeSnackbarAlert(R.string.set_important_alert);
          }
        });

        mDoneBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            String titleFromat = mDecriptionEdt.getText().toString().trim();
            if (!titleFromat.equals("")) {
              int doneStatusChangeId = registerTaskDetails();

              if (doneStatusChanged) {
                if (doneStatusChangeId == 1) {
                  doneStatusChanged = false;

                } else if (doneStatusChangeId == 2) {
                  doneStatusChanged = false;
                }
              }
              Repository.getInstance(getActivity()).updateTask(mClickedTask);
              makeToastAlert(R.string.task_update_alert);
              getActivity().finish();

            } else
              makeSnackbarAlert(R.string.title_is_not_set_alert);
          }
        });
      }
    });/* end on mEditBtn.setOnClickListener() */

    mDeleteBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        popAlertDialog();
      }
    });
  }// end of prepareForUpdate()


  private int registerTaskDetails() {
    boolean doneChange = mClickedTask.isDone();
    int doneStatusChangeId = 0;

    mClickedTask.setTitle(mDecriptionEdt.getText().toString());
    mClickedTask.setDate(mTaskDate);
    mClickedTask.setTime(mTaskTime);
    mClickedTask.setImportant(mSetImportantChk.isChecked());
    mClickedTask.setDone(mSetDoneChk.isChecked());
    mClickedTask.setAlarmRequired(mSetAlarmChk.isChecked());

    if (!isNewTask) {
      if (mClickedTask.isDone() && !doneChange) {
        doneStatusChanged = true;
        doneStatusChangeId = 1;

      } else if (!mClickedTask.isDone() && doneChange) {
        doneStatusChanged = true;
        doneStatusChangeId = 2;

      }
    }
    return doneStatusChangeId;
  }// end of onCompletedTaskDetialListener()


  private void hideAnimation(View view) {
    TranslateAnimation animate = new TranslateAnimation(0, 0, 0, view.getWidth());
    animate.setDuration(500);
    animate.setFillAfter(true);
    view.startAnimation(animate);
    view.setVisibility(View.GONE);
  }


  private void popAlertDialog() {
    new AlertDialog.Builder(getContext())
      .setTitle(R.string.delete_task_dialog_title)
      .setNegativeButton("انصراف", null)
      .setPositiveButton("حذف", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
          /*if (mClickedTask.isDone())
            Repository.getInstance(getActivity()).removeDoneTask(mClickedTask);

          else
            Repository.getInstance(getActivity()).removeUndoneTask(mClickedTask);*/

          Repository.getInstance(getActivity()).removeTask(mClickedTask);
          makeToastAlert(R.string.task_delete_alert);
          getActivity().finish();
        }
      }).show();
  }// end of showAlertDialog()


  public void setEnabledViews(boolean isEnabled) {
    for (View view: arrViews) {
      if (isEnabled) {
        view.animate().alpha(1.0f);

      } else {
        view.setAlpha(0.5f);
      }
    }
  }// end of setEnabledViews()


  private void makeToastAlert(int alertText) {
    Toast.makeText(getActivity(), alertText, Toast.LENGTH_SHORT).show();
  }


  private void makeSnackbarAlert(int alertText) {
    Snackbar.make(getView(), alertText, Snackbar.LENGTH_SHORT).show();
  }
}