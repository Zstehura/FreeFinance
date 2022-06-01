package com.example.financefree;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.financefree.structures.LoanCalculator;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.regex.Pattern;

public class LoanEstimatorFragment extends Fragment {

    public LoanEstimatorFragment() { }

    private LoanCalculator lc = new LoanCalculator(10000, 48, 0.06, 250);
    private NumberFormat nfp = NumberFormat.getPercentInstance();
    private NumberFormat nfc = NumberFormat.getCurrencyInstance();

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

        return view;
    }

    private void setListeners() {
        etAmt.setOnEditorActionListener((textView, i, keyEvent) -> {
            String s = textView.getText().toString().replaceAll("[$,]", "");
            double d = Double.parseDouble(s);
            etAmt.setText(nfc.format(d));
            return false;
        });
        etPct.setOnEditorActionListener((textView, i, keyEvent) -> {
            String s = textView.getText().toString().replaceAll("[$,]", "");
            double d = Double.parseDouble(s);
            if(d >= 0.40) {
                d /= 100;
            }
            etAmt.setText(nfp.format(d));
            return false;
        });
        etPmt.setOnEditorActionListener((textView, i, keyEvent) -> {
            String s = textView.getText().toString().replaceAll("[$,]", "");
            double d = Double.parseDouble(s);
            etPmt.setText(nfc.format(d));
            return false;
        });
    }

    private void updateNums(double num) {

    }
}