package com.developer.smmousavi.maktab_hw12_autobudget.mvc.database.wrappers;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.developer.smmousavi.maktab_hw12_autobudget.mvc.database.DBSchema.AllTaskTable;
import com.developer.smmousavi.maktab_hw12_autobudget.mvc.model.Task;

import java.util.UUID;

public class TaskCursorWrapper extends CursorWrapper {

  public TaskCursorWrapper(Cursor cursor) {
    super(cursor);
  }

  public Task getAllTasks() {
    String uuidString = getString(getColumnIndex(AllTaskTable.Cols.TASK_UUID));
    String title = getString(getColumnIndex(AllTaskTable.Cols.TITLE));
    String initialTitle = getString(getColumnIndex(AllTaskTable.Cols.INITIAL));
    long date = getLong(getColumnIndex(AllTaskTable.Cols.DATE));
    long time = getLong(getColumnIndex(AllTaskTable.Cols.TIME));
    int isImportant = getInt(getColumnIndex(AllTaskTable.Cols.IMPORTANCE_STATUS));
    int isDone = getInt(getColumnIndex(AllTaskTable.Cols.DONE_STATUS));
    int isAlarmRequired = getInt(getColumnIndex(AllTaskTable.Cols.ALARM_REQUIRED_STATUS));

    /* here should use the Task(UUID, UUID) constructor, because we want to set
     *  both userId and taskId */
    Task task = new Task(UUID.fromString(uuidString));
    task.setTitle(title);
    task.setInitial(initialTitle);
    if (date != 0)
      task.setDate(date);

    if (date != 0)
      task.setTime(time);

    task.setImportant(isImportant == 1);
    task.setDone(isDone == 1);
    task.setAlarmRequired(isAlarmRequired == 1);


    /*String dateString = getString(getColumnIndex(AllTaskTable.Cols.DATE));
    Long dateLong = getLong(getColumnIndex(AllTaskTable.Cols.DATE));
    if (!dateString.equals(""))
      task.setDate(new Date(dateLong));

    else
      task.setDate(null);*/

    return task;
  }


}