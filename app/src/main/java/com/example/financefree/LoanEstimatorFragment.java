package com.example.financefree;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.financefree.structures.LoanCalculator;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
public class LoanEstimatorFragment extends Fragment {

    public LoanEstimatorFragment() { }

    private final LoanCalculator lc = new LoanCalculator(10000, 48, 0.06, 250);
    private final NumberFormat nfp = NumberFormat.getPercentInstance();
    private final NumberFormat nfc = NumberFormat.getCurrencyInstance();
    //private Cartesian cartesian;

    private final TextView.OnEditorActionListener nfcListener = (textView, i, keyEvent) -> {
        textView.setText(nfc.format(getNum((EditText) textView)));
        updateNums(getNum((EditText) textView));
        return false;
    };
    private final TextView.OnEditorActionListener nfpListener = (textView, i, keyEvent) -> {
        double d = getNum((EditText) textView);
        if(d >= 0.40) {
            d /= 100;
        }
        textView.setText(nfp.format(d));
        updateNums(d);
        return false;
    };
    private final CompoundButton.OnCheckedChangeListener chkAdListener = (compoundButton, b) -> {
        if(compoundButton.isChecked()) {
            uncheckAdChksExc(compoundButton.getId());
        }
        disableTxtsExc();
    };
    private final CompoundButton.OnCheckedChangeListener chkEdListener = (compoundButton, b) -> {
        if(compoundButton.isChecked()) {
            uncheckEdChksExc(compoundButton.getId());
        }
        disableTxtsExc();
    };
    private final View.OnFocusChangeListener etFocusListener = (view, b) -> {
        if(b && view instanceof EditText) {
            ((EditText) view).setText(String.valueOf(getNum((EditText) view)));
        }
    };

    public static LoanEstimatorFragment newInstance() {
        return new LoanEstimatorFragment();
    }

    //private AnyChartView chrtView;
    private BarChart barChart;
    private EditText etAmt, etPct, etLen, etPmt;
    private CheckBox chkEdAmt, chkEdPct, chkEdLen, chkEdPmt,
                    chkAdAmt, chkAdLen, chkAdPmt;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loan_estimator, container, false);

        etAmt = view.findViewById(R.id.txtAmountLoan);
        etPct = view.findViewById(R.id.txtAprLoan);
        etLen = view.findViewById(R.id.txtLoanLen);
        etPmt = view.findViewById(R.id.txtPaymentLoan);
        chkAdAmt = view.findViewById(R.id.chkAdAmount);
        chkAdLen = view.findViewById(R.id.chkAdLen);
        chkAdPmt = view.findViewById(R.id.chkAdPayment);
        chkEdAmt = view.findViewById(R.id.chkEdAmount);
        chkEdPct = view.findViewById(R.id.chkEdApr);
        chkEdLen = view.findViewById(R.id.chkEdLen);
        chkEdPmt = view.findViewById(R.id.chkEdPayment);
        barChart = view.findViewById(R.id.bar_chart);
        //chrtView = view.findViewById(R.id.chrtBarGraph);

        etAmt.setText(nfc.format(10000));
        etPct.setText(nfp.format(.06));
        etLen.setText(String.valueOf(48));
        etPmt.setText(nfc.format(lc.calcPayment()));


        Description d = new Description();
        d.setEnabled(false);
        barChart.setDescription(d);

        XAxis xAxis = barChart.getXAxis();
        //change the position of x-axis to the bottom
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //set the horizontal distance of the grid line
        xAxis.setGranularity(1f);
        //hiding the x-axis line, default true if not set
        xAxis.setDrawAxisLine(false);
        //hiding the vertical grid lines, default true if not set
        xAxis.setDrawGridLines(false);

        YAxis leftAxis = barChart.getAxisLeft();
        //hiding the left y-axis line, default true if not set
        leftAxis.setDrawAxisLine(false);

        YAxis rightAxis = barChart.getAxisRight();
        //hiding the right y-axis line, default true if not set
        rightAxis.setDrawAxisLine(false);

        Legend legend = barChart.getLegend();
        legend.setEnabled(false);
        //setting the shape of the legend form to line, default square shape
        // legend.setForm(Legend.LegendForm.LINE);
        // //setting the text size of the legend
        // legend.setTextSize(11f);
        // //setting the alignment of legend toward the chart
        // legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        // legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        // //setting the stacking direction of legend
        // legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        // //setting the location of legend outside the chart, default false if not set
        // legend.setDrawInside(false);

        /*
        cartesian = AnyChart.column();
        List<DataEntry> data = lc.getBalances();
        Column column = cartesian.column(data);
        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("${%Value}{groupsSeparator: }");
        cartesian.animation(true);
        cartesian.title(getString(R.string.monthly_loan_balance));
        cartesian.yScale().minimum(0d);
        cartesian.yAxis(0).labels().format("${%Value}{groupsSeparator: }");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(0).title("Month");
        cartesian.yAxis(0).title("Balance");

        chrtView.setChart(cartesian);
         */

        setListeners();
        disableTxtsExc();
        updateChart();

        return view;
    }

    private void setListeners() {
        etAmt.setOnEditorActionListener(nfcListener);
        etPct.setOnEditorActionListener(nfpListener);
        etPmt.setOnEditorActionListener(nfcListener);
        etLen.setOnEditorActionListener(((textView, i, keyEvent) -> {
            updateNums(getNum((EditText) textView));
            return false;
        }));
        etAmt.setOnFocusChangeListener(etFocusListener);
        etPmt.setOnFocusChangeListener(etFocusListener);
        etPct.setOnFocusChangeListener(etFocusListener);
        etLen.setOnFocusChangeListener(etFocusListener);
        chkAdAmt.setOnCheckedChangeListener(chkAdListener);
        chkAdLen.setOnCheckedChangeListener(chkAdListener);
        chkAdPmt.setOnCheckedChangeListener(chkAdListener);
        chkEdAmt.setOnCheckedChangeListener(chkEdListener);
        chkEdPmt.setOnCheckedChangeListener(chkEdListener);
        chkEdPct.setOnCheckedChangeListener(chkEdListener);
        chkEdLen.setOnCheckedChangeListener(chkEdListener);
    }

    private double getNum(EditText et) {
        String s = et.getText().toString().replaceAll("[a-zA-Z%$,]", "");
        if(s.equals("")) return 0;
        return Double.parseDouble(s);
    }

    private void disableTxtsExc() {
        etAmt.setEnabled(adChkSelected() && chkEdAmt.isChecked());
        etLen.setEnabled(adChkSelected() && chkEdLen.isChecked());
        etPct.setEnabled(adChkSelected() && chkEdPct.isChecked());
        etPmt.setEnabled(adChkSelected() && chkEdPmt.isChecked());
    }
    private boolean adChkSelected() {
        return chkAdLen.isChecked() || chkAdAmt.isChecked() || chkAdPmt.isChecked();
    }
    private boolean edChkSelected() {
        return chkEdLen.isChecked() || chkEdAmt.isChecked() ||
                chkEdPct.isChecked() || chkEdPmt.isChecked();
    }

    private void uncheckEdChksExc(int chkId) {
        if(chkId != chkEdAmt.getId()) {
            chkEdAmt.setChecked(false);
            chkAdAmt.setEnabled(true);
        }
        else {
            chkAdAmt.setChecked(false);
            chkAdAmt.setEnabled(false);
        }

        if(chkId != chkEdLen.getId()) {
            chkEdLen.setChecked(false);
            chkAdLen.setEnabled(true);
        }
        else {
            chkAdLen.setChecked(false);
            chkAdLen.setEnabled(false);
        }

        if(chkId != chkEdPct.getId()) chkEdPct.setChecked(false);

        if(chkId != chkEdPmt.getId()) {
            chkEdPmt.setChecked(false);
            chkAdPmt.setEnabled(true);
        }
        else {
            chkAdPmt.setChecked(false);
            chkAdPmt.setEnabled(false);
        }
    }

    private void uncheckAdChksExc(int chkId) {
        if(chkId != chkAdAmt.getId()) chkAdAmt.setChecked(false);
        if(chkId != chkAdLen.getId()) chkAdLen.setChecked(false);
        if(chkId != chkAdPmt.getId()) chkAdPmt.setChecked(false);
    }

    private void updateNums(double num) {
        if(chkEdAmt.isChecked()) {
            lc.setPrinciple(num);
        }
        else if(chkEdPct.isChecked()) {
            lc.setApr(num);
        }
        else if(chkEdPmt.isChecked()) {
            lc.setPayment(num);
        }
        else if(chkEdLen.isChecked()) {
            lc.setNumMonths((int) num);
        }
        else {
            Log.e("LoanEstimator", "No edit checked");
        }

        if(chkAdAmt.isChecked()) {
            etAmt.setText(nfc.format(lc.calcPayment()));
        }
        else if(chkAdPmt.isChecked()) {
            etPmt.setText(nfc.format(lc.calcPayment()));
        }
        else if(chkAdLen.isChecked()) {
            int i = lc.calcTermLen();
            if (i < 1) {
                etLen.setText(R.string.never);
            } else {
                etLen.setText(String.valueOf(lc.calcTermLen()));
            }
        }
        else {
            Log.e("LoanEstimator", "No adjust checked");
        }
        if(etLen.getText().toString().equals(getString(R.string.never))){
            //chrtView.setVisibility(View.GONE);
        }
        else {
            //chrtView.setVisibility(View.VISIBLE);
            updateChart();
        }
    }

    private void updateChart() {
        //List<DataEntry> data = lc.getBalances();
        //cartesian.data(data);

        List<Double> values = new ArrayList<>(lc.getBalances());
        List<BarEntry> entries = new ArrayList<>();

        for(int i = 0; i < values.size(); i++) {
            BarEntry temp = new BarEntry(i, values.get(i).floatValue());
            entries.add(temp);
        }

        BarDataSet barDataSet = new BarDataSet(entries, ""); // MyResources.getRes().getString(R.string.monthly_loan_balance));
        barDataSet.setColor(MyResources.getRes().getColor(R.color.dark_green, null));
        barDataSet.setDrawValues(false);

        BarData data = new BarData(barDataSet);
        barChart.setData(data);
        barChart.invalidate();
    }

}