package com.developer.smmousavi.maktab_hw12_autobudget.mvc.controller.fragments;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import com.developer.smmousavi.maktab_hw12_autobudget.R;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimePickerFragment extends DialogFragment {

  public static final String ARGS_TIME_MILLIES = "arg_time_millies";
  public static final String EXTRA_TIME =
    "com.example.smmousavi.maktab_hw82_remindemelater.mvc.controller.fragments.extra_time";

  private TimePicker mTimePicker;


  public TimePickerFragment() {
    /* Required empty public constructor */

  }// end of TimePickerFragment()


  public static TimePickerFragment newInstance(long timeMillies) {

    Bundle args = new Bundle();
    args.putLong(ARGS_TIME_MILLIES, timeMillies);

    TimePickerFragment fragment = new TimePickerFragment();
    fragment.setArguments(args);
    return fragment;
  }// end of newInstance()


  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    LayoutInflater inflater = LayoutInflater.from(getActivity());
    View view = inflater.inflate(R.layout.fragment_time_picker, null, false);
    mTimePicker = view.findViewById(R.id.time_picker);

    long time = getArguments().getLong(ARGS_TIME_MILLIES);
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(time);

    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    int minute = calendar.get(Calendar.MINUTE);

    mTimePicker.setCurrentHour(hour);
    mTimePicker.setCurrentMinute(minute);

    return new AlertDialog.Builder(getActivity())
      .setView(view)
      .setPositiveButton("ثبت زمان", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
          int hour = mTimePicker.getCurrentHour();
          int minute = mTimePicker.getCurrentMinute();
          Calendar calendar = Calendar.getInstance();
          calendar.set(Calendar.HOUR_OF_DAY, hour);
          calendar.set(Calendar.MINUTE, minute);

          sendData(Activity.RESULT_OK, calendar.getTimeInMillis());
        }
      })
      .setNegativeButton("انصراف", null)
      .create();

  }

  private void sendData(int resultCode, long time) {
    Fragment target = getTargetFragment();
    if (target == null)
      return;

    Intent intent = new Intent();
    intent.putExtra(EXTRA_TIME, time);
    target.onActivityResult(getTargetRequestCode(), resultCode, intent);
  }
}
