package com.developer.smmousavi.maktab_hw12_autobudget.mvc.controller.fragments;


import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.developer.smmousavi.maktab_hw12_autobudget.R;
import com.developer.smmousavi.maktab_hw12_autobudget.mvc.database.Repository;
import com.developer.smmousavi.maktab_hw12_autobudget.mvc.model.Expense;
import com.developer.smmousavi.maktab_hw12_autobudget.mvc.model.Withdraw;

import java.text.DecimalFormat;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import saman.zamani.persiandate.PersianDate;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExpenseListFragment extends Fragment {

  public static final String TAG = "ExpenseListFragment";
  public static final String EXPENSE_REGISTER_DIALOG_TAG = "expense_register_diaolog_tag";

  private Typeface fntDirooz;
  private Typeface fntPrastoo;
  private RecyclerView recyclerView;
  private ExpenseAdapter adapter;
  private TextView txtTransactionNotifier;
  private Button btnRegisterTransaction;
  private Button btnIgnoreTransaction;
  private TextView txtThisMonthTotalExpense;
  private TextView txtPrevMonthTotalExpense;
  private TextView txtThisMonthTitle;
  private TextView txtPrevMonthTitle;
  private List<Withdraw> unregisteredWithdraws;
  private Withdraw currentWithdraw;

  private TextView txtExpenseTitle;
  private TextView txtExpenseAmount;
  private TextView txtExpenseCategroy;
  private TextView txtExpenseDate;


  public static ExpenseListFragment newInstance() {

    Bundle args = new Bundle();

    ExpenseListFragment fragment = new ExpenseListFragment();
    fragment.setArguments(args);
    return fragment;
  }

  public ExpenseListFragment() {
    // Required empty public constructor
  }


  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    fntDirooz = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Dirooz-FD-WOL.ttf");
    fntPrastoo = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Parastoo-FD.ttf");
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_expense_list, container, false);
    getViews(view);
    recyclerView = view.findViewById(R.id.expenses_recycler_view);
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    updateInformations();

    btnRegisterTransaction.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        ExpenseRegisterDialogFragment dialong = ExpenseRegisterDialogFragment.newInstance(currentWithdraw.getId());
        dialong.show(getFragmentManager(), EXPENSE_REGISTER_DIALOG_TAG);
      }
    });

    btnIgnoreTransaction.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        currentWithdraw.setIgnored(true);
        Repository.getInstance(getActivity()).updateWithdraw(currentWithdraw);
        updateExpenseRegisterNotification();
      }
    });
    setupAdapter();

    return view;
  }


  private void getViews(View view) {
    txtTransactionNotifier = view.findViewById(R.id.txt_transaction_notifier);
    btnRegisterTransaction = view.findViewById(R.id.btn_register_transaction);
    btnIgnoreTransaction = view.findViewById(R.id.btn_ignore_transaction);
    txtThisMonthTotalExpense = view.findViewById(R.id.txt_this_month_total_expense);
    txtPrevMonthTotalExpense = view.findViewById(R.id.txt_last_month_total_expense);
    txtThisMonthTitle = view.findViewById(R.id.this_month_title);
    txtPrevMonthTitle = view.findViewById(R.id.prev_month_title);
    txtTransactionNotifier.setTypeface(fntDirooz);
    btnRegisterTransaction.setTypeface(fntDirooz);
    btnIgnoreTransaction.setTypeface(fntDirooz);
    txtThisMonthTotalExpense.setTypeface(fntDirooz);
    txtPrevMonthTotalExpense.setTypeface(fntDirooz);
    txtThisMonthTitle.setTypeface(fntDirooz);
    txtPrevMonthTitle.setTypeface(fntDirooz);
  }


  private void setupAdapter() {
    int currentMonth = PersianDate.today().getShMonth();
    List<Expense> expenseList = Repository.getInstance(getActivity()).getExpenseListByMonth(currentMonth, currentMonth - 1);
    Log.i("TAG19", expenseList.size() + "");
    adapter = new ExpenseAdapter(expenseList);
    recyclerView.setAdapter(adapter);
  }// end of setupAdapter()


  // this method should be called when a new message has recieved
  public void updateInformations() {
    updateExpenseRegisterNotification();

    updateTotalExpenses();
  }

  private void updateExpenseRegisterNotification() {
    unregisteredWithdraws = Repository.getInstance(getActivity()).getUnregisteredWithdrawList();
    if (unregisteredWithdraws.size() > 0) {
      currentWithdraw = unregisteredWithdraws.get(unregisteredWithdraws.size() - 1);
      String amountStr = numberFormatter(currentWithdraw.getAmount());
      int day = currentWithdraw.getPersianDate().getShDay();
      int month = currentWithdraw.getPersianDate().getShMonth();
      int year = currentWithdraw.getPersianDate().getShYear();
      String dateStr = dateFormatter(year, month, day);
      String accountNumberStr = currentWithdraw.getAccountNumber();
      String bankNameStr = currentWithdraw.getBankName();
      String withdrawNotification = String.format("مبلغ %s تومان در تاریخ %s از حساب %s در %s برداشت شد.", amountStr, dateStr, accountNumberStr, bankNameStr);
      txtTransactionNotifier.setText(withdrawNotification);
    } else {
      String withdrawNotification = "هیچ هزینه ثبت نشده ای موجود نیست.";
      txtTransactionNotifier.setText(withdrawNotification);
      btnIgnoreTransaction.setVisibility(View.GONE);
      btnRegisterTransaction.setVisibility(View.GONE);
    }
  }

  private void updateTotalExpenses() {
    double thisMonthTotalExpense = calcTotalExpenseByMonth(PersianDate.today().getShMonth());
    double prevMonthTotalExpense = calcTotalExpenseByMonth(PersianDate.today().getShMonth() - 1);
    String thisMonthTotalStr = numberFormatter(thisMonthTotalExpense);
    String prevMonthTotalStr = numberFormatter(prevMonthTotalExpense);
    txtThisMonthTotalExpense.setText(thisMonthTotalStr + " تومان");
    txtPrevMonthTotalExpense.setText(prevMonthTotalStr + " تومان");
  }

  public class ExpenseViewHolder extends RecyclerView.ViewHolder {
    private Expense currentExpense;

    public ExpenseViewHolder(@NonNull final View itemView) {
      super(itemView);
      getHolderViews(itemView);

    }// end of ExpenseViewHolder()


    private void getHolderViews(@NonNull View itemView) {
      txtExpenseTitle = itemView.findViewById(R.id.txt_expense_title_item);
      txtExpenseAmount = itemView.findViewById(R.id.txt_expense_amount_item);
      txtExpenseCategroy = itemView.findViewById(R.id.txt_expense_category_item);
      txtExpenseDate = itemView.findViewById(R.id.txt_expense_date_item);
      txtExpenseTitle.setTypeface(fntDirooz);
      txtExpenseAmount.setTypeface(fntDirooz);
      txtExpenseCategroy.setTypeface(fntDirooz);
      txtExpenseDate.setTypeface(fntDirooz);
    }// end of getHolderViews()


    public void onBinde(Expense expense) {
      currentExpense = expense;
      txtExpenseTitle.setText(expense.getTitle());
      txtExpenseAmount.setText(numberFormatter(expense.getAmount()) + " تومان");
      txtExpenseCategroy.setText(expense.getCategoryName());
      int year = expense.getDate().getShYear();
      int month = expense.getDate().getShMonth();
      int day = expense.getDate().getShDay();
      txtExpenseDate.setText(dateFormatter(year, month, day));

    }// end of onBinde()
  }// end of ExpenseViewHolder{}


  public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseViewHolder> {
    private List<Expense> expenses;

    public ExpenseAdapter(List<Expense> expenses) {
      this.expenses = expenses;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      LayoutInflater inflater = LayoutInflater.from(getActivity());
      View view = inflater.inflate(R.layout.expense_item_view, parent, false);
      return new ExpenseViewHolder(view);
    }// end of onCreateViewHolder()

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
      holder.onBinde(expenses.get(position));
    }// end of onBindeViewHolder()

    @Override
    public int getItemCount() {
      return expenses.size();
    }// end of getItemCount()

  }// end of ExpenseAdapter{}


  private String numberFormatter(double number) {
    DecimalFormat formatter = new DecimalFormat("#,###,###");
    return formatter.format(number);
  }


  @SuppressLint("DefaultLocale")
  private String dateFormatter(int y, int m, int d) {
    return String.format("%02d/%02d/%02d", y, m, d);
  }


  public double calcTotalExpenseByMonth(int month) {
    List<Withdraw> withdraws = Repository.getInstance(getActivity()).getWithdrawListByMonth(month);
    double totalExpense = 0;
    for (Withdraw withdraw: withdraws)
      totalExpense += withdraw.getAmount();

    return totalExpense;
  }

}
