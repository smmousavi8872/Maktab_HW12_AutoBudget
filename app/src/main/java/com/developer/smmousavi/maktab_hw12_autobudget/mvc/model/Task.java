package com.developer.smmousavi.maktab_hw12_autobudget.mvc.model;

import java.util.UUID;

public class Task {

  private UUID mTaskId;
  private String mTitle;
  private long mDate;
  private long mTime;
  private boolean mDone;
  private boolean mImportant;
  private boolean mAlarmRequired;
  private String initial;


  public Task() {
    mTaskId = UUID.randomUUID();
  }


  public Task(UUID taskId) {
    mTaskId = taskId;

  }


  public UUID getTaskId() {
    return mTaskId;
  }


  public String getTitle() {
    return mTitle;
  }


  public void setTitle(String mTitle) {
    this.mTitle = mTitle;
    initial = mTitle.substring(0, 1);
  }


  public long getDate() {
    return mDate;
  }


  public void setDate(long mDate) {
    this.mDate = mDate;
  }


  public long getTime() {
    return mTime;
  }


  public void setTime(long mTime) {
    this.mTime = mTime;
  }


  public boolean isDone() {
    return mDone;
  }


  public void setDone(boolean mDone) {
    this.mDone = mDone;
  }


  public boolean isImportant() {
    return mImportant;
  }


  public void setImportant(boolean mImportant) {
    this.mImportant = mImportant;
  }


  public boolean isAlarmRequired() {
    return mAlarmRequired;
  }


  public void setAlarmRequired(boolean mAlarmRequired) {
    this.mAlarmRequired = mAlarmRequired;
  }


  public String getInitial() {
    return initial;
  }


  public void setInitial(String initial) {
    this.initial = initial;
  }

}