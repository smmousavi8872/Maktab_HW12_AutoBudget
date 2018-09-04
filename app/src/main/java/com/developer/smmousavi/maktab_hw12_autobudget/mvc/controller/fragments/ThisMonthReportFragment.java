package com.developer.smmousavi.maktab_hw12_autobudget.mvc.controller.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.developer.smmousavi.maktab_hw12_autobudget.R;
import com.developer.smmousavi.maktab_hw12_autobudget.mvc.database.Repository;
import com.developer.smmousavi.maktab_hw12_autobudget.mvc.model.Deposit;
import com.developer.smmousavi.maktab_hw12_autobudget.mvc.model.Withdraw;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import saman.zamani.persiandate.PersianDate;


/**
 * A simple {@link Fragment} subclass.
 */
public class ThisMonthReportFragment extends Fragment {

  public static final String TAG = "ThisMonthReportFragment";

  private GraphView graph;

  public static ThisMonthReportFragment newInstance() {

    Bundle args = new Bundle();

    ThisMonthReportFragment fragment = new ThisMonthReportFragment();
    fragment.setArguments(args);
    return fragment;
  }


  public ThisMonthReportFragment() {
    // Required empty public constructor
  }


  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_this_month_report, container, false);
    graph = view.findViewById(R.id.graph);

    updateChart();

    return view;
  }


  private void renderChart(BarGraphSeries<DataPoint> depositSerie, BarGraphSeries<DataPoint> withdrawSerie) {
    graph.addSeries(withdrawSerie);
    graph.addSeries(depositSerie);

    graph.getGridLabelRenderer().setVerticalLabelsVisible(false);
    graph.getLegendRenderer().setVisible(true);

    depositSerie.setSpacing(0);
    withdrawSerie.setSpacing(0);
    depositSerie.setAnimated(true);
    withdrawSerie.setAnimated(true);

    // set manual X bounds
    graph.getViewport().setYAxisBoundsManual(true);
    graph.getViewport().setXAxisBoundsManual(true);
    graph.getViewport().setMaxX(31);

    // enable scaling and scrolling
    graph.getViewport().setScrollable(true); // enables horizontal scrolling
    graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling

    graph.addSeries(withdrawSerie);
    graph.addSeries(depositSerie);

    // draw values on top
    depositSerie.setDrawValuesOnTop(true);
    withdrawSerie.setDrawValuesOnTop(true);
    withdrawSerie.setColor(Color.RED);
    depositSerie.setValuesOnTopColor(Color.BLACK);
    withdrawSerie.setValuesOnTopColor(Color.BLACK);
  }


  private void updateChart() {
    BarGraphSeries<DataPoint> depositSerie = new BarGraphSeries<>(getDepositDatePoints());
    BarGraphSeries<DataPoint> withdrawSerie = new BarGraphSeries<>(getWithdrawDatePoints());

    renderChart(depositSerie, withdrawSerie);
  }


  public DataPoint[] getWithdrawDatePoints() {
    int currentMonth = PersianDate.today().getShMonth();
    List<Withdraw> withdraws = Repository.getInstance(getActivity()).getWithdrawListByMonth(currentMonth);
    DataPoint[] dataPoints = new DataPoint[withdraws.size()];
    for (int i = 0; i < withdraws.size(); i++) {
      dataPoints[i] = new DataPoint(withdraws.get(i).getPersianDate().getShDay(), withdraws.get(i).getAmount());
    }
    return dataPoints;
  }


  public DataPoint[] getDepositDatePoints() {
    int currentMonth = PersianDate.today().getShMonth();
    List<Deposit> deposits = Repository.getInstance(getActivity()).getDepositListByMonth(currentMonth);
    DataPoint[] dataPoints = new DataPoint[deposits.size()];
    for (int i = 0; i < deposits.size(); i++) {
      dataPoints[i] = new DataPoint(deposits.get(i).getPersianDate().getShDay(), deposits.get(i).getAmount());
    }
    return dataPoints;
  }

}