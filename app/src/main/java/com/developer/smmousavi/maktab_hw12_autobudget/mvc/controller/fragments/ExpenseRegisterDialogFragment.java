package com.developer.smmousavi.maktab_hw12_autobudget.mvc.controller.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.developer.smmousavi.maktab_hw12_autobudget.R;
import com.developer.smmousavi.maktab_hw12_autobudget.mvc.database.Repository;
import com.developer.smmousavi.maktab_hw12_autobudget.mvc.model.Expense;
import com.developer.smmousavi.maktab_hw12_autobudget.mvc.model.Withdraw;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ExpenseRegisterDialogFragment extends DialogFragment {

  public static final String ARGS_WITHDRAW_ID = "args_withdraw_id";

  private Typeface fntDirooz;
  private Typeface fntPrastoo;
  private Button navUpBtn;
  private TextView registerNewExpenseTitle;
  private EditText expenseTitleEdt;
  private TextView categorySelectionTitle;
  private Spinner expenseCategorySpn;
  private EditText expenseAmountEdt;
  private TextView dateTitleTxt;
  private TextView dateTxt;
  private Button registerBtn;
  private Repository repository = Repository.getInstance(getActivity());

  private List<String> expenseCategories = new ArrayList<>();
  private Withdraw withdraw;
  private int categoryItem;


  public static ExpenseRegisterDialogFragment newInstance(UUID withdrawId) {
    Bundle args = new Bundle();
    args.putSerializable(ARGS_WITHDRAW_ID, withdrawId);

    ExpenseRegisterDialogFragment fragment = new ExpenseRegisterDialogFragment();
    fragment.setArguments(args);
    return fragment;
  }// end of newInstance()


  public ExpenseRegisterDialogFragment() {
    // Required empty public constructor
  }


  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    LayoutInflater inflater = LayoutInflater.from(getActivity());
    final View view = inflater.inflate(R.layout.expense_register_dialog_fragment, null, false);
    fntDirooz = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Dirooz-FD-WOL.ttf");
    fntPrastoo = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Parastoo-FD.ttf");

    AlertDialog dialog = new AlertDialog.Builder(getActivity())
      .setView(view)
      .create();

    getViews(view);

    setupExpenseCategory();

    UUID withdrawId = (UUID) getArguments().getSerializable(ARGS_WITHDRAW_ID);
    withdraw = Repository.getInstance(getActivity()).getWithdraw(withdrawId);

    expenseAmountEdt.setText(numberFormatter(withdraw.getAmount()));
    dateTxt.setText(withdraw.getPersianDate().toString());

    expenseAmountEdt.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        try {
          expenseAmountEdt.removeTextChangedListener(this);
          String value = expenseAmountEdt.getText().toString();
          if (value != null && !value.equals("")) {
            if (value.startsWith("."))
              expenseAmountEdt.setText("0.");

            if (value.startsWith("0") && !value.startsWith("0."))
              expenseAmountEdt.setText("");

            String str = expenseAmountEdt.getText().toString().replaceAll(",", "");
            if (!value.equals(""))
              expenseAmountEdt.setText(getDecimalFormattedString(str));
            expenseAmountEdt.setSelection(expenseAmountEdt.getText().toString().length());
          }
          expenseAmountEdt.addTextChangedListener(this);

          return;
        } catch (Exception ex) {
          ex.printStackTrace();
          expenseAmountEdt.addTextChangedListener(this);
        }
      }

      @Override
      public void afterTextChanged(Editable editable) {
      }
    });


    registerBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Expense newExpense = new Expense();
        String title = expenseTitleEdt.getText().toString();
        String categoryName = expenseCategories.get(categoryItem);
        if (!title.equals("")) {
          newExpense.setTitle(title);
          newExpense.setCategoryName(categoryName);
          newExpense.setCategoryItem(categoryItem);
          newExpense.setDate(withdraw.getPersianDate());
          String amountStr = expenseAmountEdt.getText().toString().replace(",", "");
          double amount = Double.parseDouble(amountStr);
          newExpense.setAmount(amount);
          repository.addExpense(newExpense);
          withdraw.setIgnored(true);
          Repository.getInstance(getActivity()).updateWithdraw(withdraw);
          dismiss();

        } else {
          expenseTitleEdt.setHintTextColor(Color.RED);

        }
      }
    });

    navUpBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        dismiss();
      }
    });

    return dialog;
  }

  private String numberFormatter(double number) {
    DecimalFormat formatter = new DecimalFormat("#,###,###");
    return formatter.format(number);
  }


  private void getViews(View view) {
    navUpBtn = view.findViewById(R.id.btn_expense_register_nav_up);
    registerNewExpenseTitle = view.findViewById(R.id.txt_expense_register_title);
    expenseTitleEdt = view.findViewById(R.id.edt_expense_title);
    categorySelectionTitle = view.findViewById(R.id.txt_category_selection_title);
    expenseCategorySpn = view.findViewById(R.id.spn_category_selection);
    expenseAmountEdt = view.findViewById(R.id.edt_expense_amount);
    dateTxt = view.findViewById(R.id.btn_date);
    dateTitleTxt = view.findViewById(R.id.txt_date_title);
    registerBtn = view.findViewById(R.id.btn_expense_register);
    registerNewExpenseTitle.setTypeface(fntPrastoo);
    navUpBtn.setTypeface(fntDirooz);
    expenseTitleEdt.setTypeface(fntDirooz);
    categorySelectionTitle.setTypeface(fntDirooz);
    categorySelectionTitle.setTypeface(fntPrastoo);
    expenseAmountEdt.setTypeface(fntDirooz);
    dateTxt.setTypeface(fntDirooz);
    dateTitleTxt.setTypeface(fntPrastoo);
    registerBtn.setTypeface(fntPrastoo);
  } // end of getViews()

  public void setupExpenseCategory() {
    expenseCategories.add("انتخاب کنید");
    expenseCategories.add("شارژ ساختمان");
    expenseCategories.add("برج ماهیانه");
    expenseCategories.add("خرید شخصی");
    expenseCategories.add("خرید همسر");
    expenseCategories.add("خرید فرزندان");
    expenseCategories.add("خرید منزل");
    expenseCategories.add("هزینه ماشین");
    expenseCategories.add("هزینه موبایل");
    expenseCategories.add("قبوض ماهیانه");
    expenseCategories.add("اقساط بانک");
    expenseCategories.add("مسافرت");
    expenseCategories.add("ههمانی");
    expenseCategories.add("غیره...");

    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, expenseCategories);
    expenseCategorySpn.setAdapter(adapter);
    expenseCategorySpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
        categoryItem = position;
      }

      @Override
      public void onNothingSelected(AdapterView<?> parentView) {
      }

    });
  }

  public static String getDecimalFormattedString(String value) {
    StringTokenizer lst = new StringTokenizer(value, ".");
    String str1 = value;
    String str2 = "";
    if (lst.countTokens() > 1) {
      str1 = lst.nextToken();
      str2 = lst.nextToken();
    }
    String str3 = "";
    int i = 0;
    int j = -1 + str1.length();
    if (str1.charAt(-1 + str1.length()) == '.') {
      j--;
      str3 = ".";
    }
    for (int k = j; ; k--) {
      if (k < 0) {
        if (str2.length() > 0)
          str3 = str3 + "." + str2;
        return str3;
      }
      if (i == 3) {
        str3 = "," + str3;
        i = 0;
      }
      str3 = str1.charAt(k) + str3;
      i++;
    }

  }

  public static String trimCommaOfString(String string) {
//        String returnString;
    if (string.contains(",")) {
      return string.replace(",", "");
    } else {
      return string;
    }

  }

}