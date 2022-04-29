package com.example.financefree;

import android.graphics.Typeface;
import android.os.Bundle;


import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.financefree.database.DatabaseManager;
import com.example.financefree.structures.DateParser;
import com.example.financefree.structures.Payment;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AnnualViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnnualViewFragment extends Fragment {
    private static final String YEAR_ARG_KEY = "year";
    private static final String MONTH_ARG_KEY = "month";
    private ImageButton btnYearDec, btnYearInc, btnMonthDec, btnMonthInc;
    private GridLayout grdYear, grdMonth;
    private TextView tvYear, tvMonth, lblYrPmnt, lblYrAmnt, lblMnthPmnt, lblMnthAmnt;

    private int nYear, nMonth;

    public AnnualViewFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param nYear Year to begin at.
     * @param nMonth month to begin at (of the given year)
     * @return A new instance of fragment AnnualViewFragment.
     */
    public static AnnualViewFragment newInstance(int nYear, int nMonth) {
        AnnualViewFragment fragment = new AnnualViewFragment();
        Bundle args = new Bundle();
        args.putInt(YEAR_ARG_KEY, nYear);
        args.putInt(MONTH_ARG_KEY, nMonth);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            nYear = getArguments().getInt(YEAR_ARG_KEY);
            nMonth = getArguments().getInt(MONTH_ARG_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_annual_view, container, false);

        // set controls
        tvYear = view.findViewById(R.id.txtYear);
        grdYear = view.findViewById(R.id.gridYearly);
        btnYearDec = view.findViewById(R.id.btnYearLeft);
        btnYearInc = view.findViewById(R.id.btnYearRight);
        lblYrAmnt = view.findViewById(R.id.lblYearAmount);
        lblYrPmnt = view.findViewById(R.id.lblYearPayment);
        tvMonth = view.findViewById(R.id.txtMonth);
        grdMonth = view.findViewById(R.id.gridMonthly);
        btnMonthDec = view.findViewById(R.id.btnMonthLeft);
        btnMonthInc = view.findViewById(R.id.btnMonthRight);
        lblMnthPmnt = view.findViewById(R.id.lblMonthPayment);
        lblMnthAmnt = view.findViewById(R.id.lblMonthAmount);

        btnYearInc.setOnClickListener((view1 -> {
            int y = Integer.parseInt(tvYear.getText().toString());
            nYear = y + 1;
            setYear();
            setMonth();
        }));
        btnYearDec.setOnClickListener(view1 -> {
            int y = Integer.parseInt(tvYear.getText().toString());
            nYear = y - 1;
            setYear();
            setMonth();
        });
        btnMonthInc.setOnClickListener(view1 -> {
            if(DateParser.monthToInt(tvMonth.getText().toString()) == Calendar.DECEMBER) {
                nMonth = Calendar.JANUARY;
                nYear++;
                setYear();
            }
            else {
                nMonth++;
            }
            setMonth();
        });
        btnMonthDec.setOnClickListener(view1 -> {
            if(DateParser.monthToInt(tvMonth.getText().toString()) == Calendar.JANUARY) {
                nMonth = Calendar.DECEMBER;
                nYear--;
                setYear();
            }
            else {
                nMonth--;
            }
            setMonth();
        });

        GregorianCalendar gc = new GregorianCalendar();
        nYear = gc.get(Calendar.YEAR);
        nMonth = gc.get(Calendar.MONTH);
        setYear();
        setMonth();

        return view;
    }

    private void clearAnnualView() {
        for(int i = grdYear.getChildCount() - 1; i >= 0; i--){
            View v = grdYear.getChildAt(i);
            if(v.getId() != lblYrAmnt.getId() && v.getId() != lblYrPmnt.getId()) {
                grdYear.removeView(v);
            }
        }
    }
    private void clearMonthView(){
        for(int i = grdMonth.getChildCount() - 1; i >= 0; i--){
            View v = grdMonth.getChildAt(i);
            if(v.getId() != lblMnthPmnt.getId() && v.getId() != lblMnthAmnt.getId()) {
                grdMonth.removeView(v);
            }
        }
    }

    private void addYearly(String name, double amount) {
        NumberFormat f = NumberFormat.getCurrencyInstance();
        String amnt = f.format(amount);
        GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
        lp.setGravity(Gravity.END);
        TextView tvName = new TextView(getContext());
        TextView tvAmount = new TextView(getContext());

        tvName.setText(name);
        tvAmount.setText(amnt);
        tvAmount.setLayoutParams(lp);

        if(amount > 0){
            tvAmount.setTextColor(getResources().getColor(R.color.dark_green,null));
            tvName.setTextColor(getResources().getColor(R.color.dark_green,null));
        }
        else {
            tvAmount.setTextColor(getResources().getColor(R.color.dark_red,null));
            tvName.setTextColor(getResources().getColor(R.color.dark_red,null));
        }

        if(name.equals(getString(R.string.total)) || name.equals(getString(R.string.overall))){
            tvAmount.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            tvName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        }

        grdYear.addView(tvName);
        grdYear.addView(tvAmount);
    }
    private void addMonthly(String name, double amount) {
        NumberFormat f = NumberFormat.getCurrencyInstance();
        String amnt = f.format(amount);
        GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
        lp.setGravity(Gravity.END);
        TextView tvName = new TextView(getContext());
        TextView tvAmount = new TextView(getContext());

        tvName.setText(name);
        tvAmount.setText(amnt);
        tvAmount.setLayoutParams(lp);

        if(amount > 0){
            tvAmount.setTextColor(getResources().getColor(R.color.dark_green, null));
            tvName.setTextColor(getResources().getColor(R.color.dark_green,null));
        }
        else {
            tvAmount.setTextColor(getResources().getColor(R.color.dark_red,null));
            tvName.setTextColor(getResources().getColor(R.color.dark_red,null));
        }

        if(name.equals(getString(R.string.total)) || name.equals(getString(R.string.overall))){
            tvAmount.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            tvName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        }

        grdMonth.addView(tvName);
        grdMonth.addView(tvAmount);
    }

    @SuppressWarnings("ConstantConditions")
    private void setYear() {
        AtomicReference<List<Payment>> list = new AtomicReference<>();
        Thread t = new Thread(() -> list.set(DatabaseManager.getAnnualPayments(nYear)));
        t.start();

        clearAnnualView();
        double ttlIn = 0;
        double ttlOut = 0;

        try{t.join();}
        catch (InterruptedException e) { e.printStackTrace();}

        tvYear.setText(String.valueOf(nYear));
        Map<String, Double> mapOut = new HashMap<>();
        Map<String, Double> mapIn = new HashMap<>();

        for(Payment p: list.get()){
            if(p.amount > 0) {
                // money in
                if (mapIn.containsKey(p.name)) {
                    double d = mapIn.get(p.name);
                    d += p.amount;
                    mapIn.replace(p.name, d);
                } else {
                    mapIn.put(p.name, p.amount);
                }
            }
            else {
                // money out
                if (mapOut.containsKey(p.name)) {
                    double d = mapOut.get(p.name);
                    d += p.amount;
                    mapOut.replace(p.name, d);
                } else {
                    mapOut.put(p.name, p.amount);
                }
            }
        }

        for(String sName: mapIn.keySet()){
            String s = "  " + sName;
            addYearly(s, mapIn.get(sName));
            ttlIn += mapIn.get(sName);
        }
        addYearly(getString(R.string.total), ttlIn);

        for(String sName: mapOut.keySet()){
            String s = "  " + sName;
            addYearly(s, mapOut.get(sName));
            ttlOut += mapOut.get(sName);
        }
        addYearly(getString(R.string.total), ttlOut);

        addYearly(getString(R.string.overall), ttlIn + ttlOut);
    }

    @SuppressWarnings("ConstantConditions")
    private void setMonth() {
        AtomicReference<List<Payment>> list = new AtomicReference<>();
        Thread t = new Thread(() -> list.set(DatabaseManager.getMonthlyPayments(nMonth, nYear)));
        t.start();

        clearMonthView();
        double ttlIn = 0;
        double ttlOut = 0;

        try{t.join();}
        catch (InterruptedException e) { e.printStackTrace();}

        tvMonth.setText(DateParser.monthToString(nMonth));
        Map<String, Double> mapOut = new HashMap<>();
        Map<String, Double> mapIn = new HashMap<>();

        for(Payment p: list.get()){
            if(p.amount > 0) {
                // money in
                if (mapIn.containsKey(p.name)) {
                    double d = mapIn.get(p.name);
                    d += p.amount;
                    mapIn.replace(p.name, d);
                } else {
                    mapIn.put(p.name, p.amount);
                }
            }
            else {
                // money out
                if (mapOut.containsKey(p.name)) {
                    double d = mapOut.get(p.name);
                    d += p.amount;
                    mapOut.replace(p.name, d);
                } else {
                    mapOut.put(p.name, p.amount);
                }
            }
        }

        for(String sName: mapIn.keySet()){
            String s = "  " + sName;
            addMonthly(s, mapIn.get(sName));
            ttlIn += mapIn.get(sName);
        }
        addMonthly(getString(R.string.total), ttlIn);

        for(String sName: mapOut.keySet()){
            String s = "  " + sName;
            addMonthly(s, mapOut.get(sName));
            ttlOut += mapOut.get(sName);
        }
        addMonthly(getString(R.string.total), ttlOut);

        addMonthly(getString(R.string.overall), ttlIn + ttlOut);
    }
}