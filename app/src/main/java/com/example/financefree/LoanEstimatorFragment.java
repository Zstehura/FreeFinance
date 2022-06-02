package com.example.financefree;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
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

import java.text.NumberFormat;

// TODO: Add in chart below numbers, etc.

public class LoanEstimatorFragment extends Fragment {

    public LoanEstimatorFragment() { }

    private final LoanCalculator lc = new LoanCalculator(10000, 48, 0.06, 250);
    private final NumberFormat nfp = NumberFormat.getPercentInstance();
    private final NumberFormat nfc = NumberFormat.getCurrencyInstance();

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
        if(compoundButton.isChecked()) uncheckAdChksExc(compoundButton.getId());
    };
    private final CompoundButton.OnCheckedChangeListener chkEdListener = (compoundButton, b) -> {
        if(compoundButton.isChecked()) {
            uncheckEdChksExc(compoundButton.getId());
            disableTxtsExc(compoundButton.getId());
        }
        else {
            disableTxtsExc(0);
        }
    };
    private final View.OnFocusChangeListener etFocusListener = (view, b) -> {
        if(b && view instanceof EditText) {
            ((EditText) view).setText(String.valueOf(getNum((EditText) view)));
        }
    };

    public static LoanEstimatorFragment newInstance() {
        return new LoanEstimatorFragment();
    }

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

        etAmt.setText(nfc.format(10000));
        etPct.setText(nfp.format(.06));
        etLen.setText(String.valueOf(48));
        etPmt.setText(nfc.format(lc.calcPayment()));

        setListeners();
        disableTxtsExc(0);
        return view;
    }

    private void setListeners() {
        etAmt.setOnEditorActionListener(nfcListener);
        etPct.setOnEditorActionListener(nfpListener);
        etPmt.setOnEditorActionListener(nfcListener);
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

    private void disableTxtsExc(int chkId) {
        etAmt.setEnabled(chkId == chkEdAmt.getId());
        etLen.setEnabled(chkId == chkEdLen.getId());
        etPct.setEnabled(chkId == chkEdPct.getId());
        etPmt.setEnabled(chkId == chkEdPmt.getId());
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
            chkAdAmt.setEnabled(false);
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
    }
}