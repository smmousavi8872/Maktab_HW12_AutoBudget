package com.developer.smmousavi.maktab_hw12_autobudget.mvc.utilites;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class FontChange {
  public static final String PARASTOO = "parastoo";
  public static final String DIROOZ = "dirooz";

  private Typeface fntDirooz;
  private Typeface fntPrastoo;


  public FontChange(Context context) {
    fntDirooz = Typeface.createFromAsset(context.getAssets(), "fonts/Dirooz-FD-WOL.ttf");
    fntPrastoo = Typeface.createFromAsset(context.getAssets(), "fonts/Parastoo-FD.ttf");
  }


  public Typeface getFontDirooz() {
    return fntDirooz;
  }


  public Typeface getFontParastoo() {
    return fntPrastoo;
  }


  public void setButtonFont(String fontName, Button... buttons) {
    Typeface font = null;
    switch (fontName) {
      case PARASTOO:
        font = fntPrastoo;
        break;
      case DIROOZ:
        font = fntDirooz;
    }

    for (Button button: buttons) {
      button.setTypeface(font);
    }
  }


  public void setTextViewFont(String fontName, TextView... textViews) {
    Typeface font = null;
    switch (fontName) {
      case PARASTOO:
        font = fntPrastoo;
        break;
      case DIROOZ:
        font = fntDirooz;
    }

    for (TextView textView: textViews) {
      textView.setTypeface(font);
    }
  }


  public void setEditTextFont(String fontName, EditText... editTexts) {
    Typeface font = null;
    switch (fontName) {
      case PARASTOO:
        font = fntPrastoo;
        break;
      case DIROOZ:
        font = fntDirooz;
    }

    for (EditText editText: editTexts) {
      editText.setTypeface(font);
    }
  }


  public void setLayoutFont(ViewGroup group, String fontName) {
    Typeface font;

    switch (fontName) {
      case PARASTOO:
        font = fntPrastoo;
        break;
      case DIROOZ:
        font = fntDirooz;
        break;
      default:
        font = null;
    }

    int count = group.getChildCount();
    View v;
    for (int i = 0; i < count; i++) {
      v = group.getChildAt(i);
      if (v instanceof TextView || v instanceof EditText || v instanceof Button) {
        ((TextView) v).setTypeface(font);
      } else if (v instanceof ViewGroup)
        setLayoutFont((ViewGroup) v, fontName);
    }
  }


}
