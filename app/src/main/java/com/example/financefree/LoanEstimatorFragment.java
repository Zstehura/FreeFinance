package com.example.financefree;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.financefree.structures.LoanCalculator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
class LoanEstimatorFragment extends Fragment {
    private static final int FIELD_LOAN_TYPE = 0;
    private static final int FIELD_PRINCIPLE = 1;
    private static final int FIELD_APR = 2;
    private static final int FIELD_START = 3;
    private static final int FIELD_END = 4;
    private static final int FIELD_TERM = 5;
    private static final int FIELD_FREQUENCY = 6;
    private static final int FIELD_PAYMENT_AMOUNT = 7;

    private CheckBox chkEdPrinciple, chkEdApr, chkEdStart, chkEdEnd,
            chkEdTerm, chkEdFreq, chkEdPayment, chkAdPrinciple, chkAdApr, chkAdStart,
            chkAdEnd, chkAdTerm, chkAdFreq, chkAdPayment;
    private EditText etPrinciple, etApr, etStart, etEnd, etTermLen, etPayment;
    private Spinner spnLoanType, spnFrequency;

    private LoanCalculator calculator;

    public LoanEstimatorFragment() {}

    public static LoanEstimatorFragment newInstance() {
        LoanEstimatorFragment l = new LoanEstimatorFragment();
        l.calculator = new LoanCalculator();
        return l;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loan_estimator, container, false);

        spnLoanType = view.findViewById(R.id.spnLoanType);
        spnFrequency = view.findViewById(R.id.spnFrequency);
        etPrinciple = view.findViewById(R.id.txtPrinciple);
        etApr = view.findViewById(R.id.txtApr);
        etStart = view.findViewById(R.id.txtStartDate);
        etEnd = view.findViewById(R.id.txtEndDate);
        etTermLen = view.findViewById(R.id.txtTermLength);
        etPayment = view.findViewById(R.id.txtPaymentAmount);

        chkEdApr =          view.findViewById(R.id.chkEditApr);
        chkEdEnd =          view.findViewById(R.id.chkEditEnd);
        chkEdPrinciple =    view.findViewById(R.id.chkEditPrinciple);
        chkEdStart =        view.findViewById(R.id.chkEditStart);
        chkEdTerm =         view.findViewById(R.id.chkEditTermLen);
        chkEdFreq =         view.findViewById(R.id.chkEditFreq);
        chkEdPayment =      view.findViewById(R.id.chkEditPayAmnt);
        chkAdApr =          view.findViewById(R.id.chkAdjustApr);
        chkAdEnd =          view.findViewById(R.id.chkAdjustEnd);
        chkAdPrinciple =    view.findViewById(R.id.chkAdjustPrinciple);
        chkAdStart =        view.findViewById(R.id.chkAdjustStart);
        chkAdTerm =         view.findViewById(R.id.chkAdjustTermLen);
        chkAdFreq =         view.findViewById(R.id.chkAdjustFreq);
        chkAdPayment =      view.findViewById(R.id.chkAdjustPayAmnt);

        List<String> lFreqs = new ArrayList<>(Arrays.asList(
                getResources().getString(R.string.monthly),
                getResources().getString(R.string.bimonthly)));
        ArrayAdapter<String> freqAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_dropdown_item_1line,
                lFreqs);
        spnFrequency.setAdapter(freqAdapter);
        spnFrequency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //calculator.setFrequency(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        setChkListeners();


        return view;
    }

    private void calcPrinciple() {

    }
    private void calcApr() {

    }
    private void calcStart() {

    }
    private void calcEnd() {

    }
    private void calcTerm() {

    }
    private void calcFrequency() {

    }
    private void calcPayment() {

    }

    private void setTxtListeners() {
        etPrinciple.setOnEditorActionListener(((textView, i, keyEvent) -> {

            return false;
        }));
        etApr.setOnEditorActionListener(((textView, i, keyEvent) -> {

            return false;
        }));
        etStart.setOnEditorActionListener(((textView, i, keyEvent) -> {

            return false;
        }));
        etEnd.setOnEditorActionListener(((textView, i, keyEvent) -> {

            return false;
        }));
        etTermLen.setOnEditorActionListener(((textView, i, keyEvent) -> {

            return false;
        }));
        etPayment.setOnEditorActionListener(((textView, i, keyEvent) -> {

            return false;
        }));
    }


    private void setChkListeners() {
        chkEdApr.setOnCheckedChangeListener((compoundButton, b) -> {
            if(compoundButton.isChecked()) {
                setEditChksExcept((CheckBox) compoundButton);

            }
        });
        chkEdEnd.setOnCheckedChangeListener((compoundButton, b) -> {

        });
        chkEdPrinciple.setOnCheckedChangeListener((compoundButton, b) -> {

        });
        chkEdStart.setOnCheckedChangeListener((compoundButton, b) -> {

        });
        chkEdTerm.setOnCheckedChangeListener((compoundButton, b) -> {

        });
        chkEdFreq.setOnCheckedChangeListener((compoundButton, b) -> {

        });
        chkEdPayment.setOnCheckedChangeListener((compoundButton, b) -> {

        });
        chkAdApr.setOnCheckedChangeListener((compoundButton, b) -> {

        });
        chkAdEnd.setOnCheckedChangeListener((compoundButton, b) -> {

        });
        chkAdPrinciple.setOnCheckedChangeListener((compoundButton, b) -> {

        });
        chkAdStart.setOnCheckedChangeListener((compoundButton, b) -> {

        });
        chkAdTerm.setOnCheckedChangeListener((compoundButton, b) -> {

        });
        chkAdFreq.setOnCheckedChangeListener((compoundButton, b) -> {

        });
        chkAdPayment.setOnCheckedChangeListener((compoundButton, b) -> {

        });
    }

    private void setEditChksExcept(CheckBox chk) {
        if(chk.getId() != chkEdPrinciple.getId()) chkEdPrinciple.setChecked(false);
        if(chk.getId() != chkEdApr.getId()) chkEdApr.setChecked(false);
        if(chk.getId() != chkEdStart.getId()) chkEdStart.setChecked(false);
        if(chk.getId() != chkEdEnd.getId()) chkEdEnd.setChecked(false);
        if(chk.getId() != chkEdTerm.getId()) chkEdTerm.setChecked(false);
        if(chk.getId() != chkEdFreq.getId()) chkEdFreq.setChecked(false);
        if(chk.getId() != chkEdPayment.getId()) chkEdPayment.setChecked(false);
    }
    private void setAdjustChksExcept(CheckBox chk) {
        if(chk.getId() != chkAdPrinciple.getId()) chkAdPrinciple.setChecked(false);
        if(chk.getId() != chkAdApr.getId()) chkAdApr.setChecked(false);
        if(chk.getId() != chkAdStart.getId()) chkAdStart.setChecked(false);
        if(chk.getId() != chkAdEnd.getId()) chkAdEnd.setChecked(false);
        if(chk.getId() != chkAdTerm.getId()) chkAdTerm.setChecked(false);
        if(chk.getId() != chkAdFreq.getId()) chkAdFreq.setChecked(false);
        if(chk.getId() != chkAdPayment.getId()) chkAdPayment.setChecked(false);
    }

//    private void enableEdits(boolean principle, boolean type, boolean apr, boolean start,
//                             boolean end, boolean term, boolean freq, boolean payment){
//        chkEdPrinciple.setEnabled(principle);
//        chkEdType.setEnabled(type);
//        chkEdApr.setEnabled(apr);
//        chkEdStart.setEnabled(start);
//        chkEdEnd.setEnabled(end);
//        chkEdTerm.setEnabled(term);
//        chkEdFreq.setEnabled(freq);
//        chkEdPayment.setEnabled(payment);
//    }


    private void enableAdjust(int fieldEnabled){
        boolean principle = false, type = false, apr = false,
                start = false, end = false, term = false, freq = false,
                payment = false;
        if(fieldEnabled == FIELD_LOAN_TYPE) {

        }
        else if(fieldEnabled == FIELD_PRINCIPLE) {

        }
        else if(fieldEnabled == FIELD_APR) {

        }

        chkAdPrinciple.setEnabled(principle);
        chkAdApr.setEnabled(apr);
        chkAdStart.setEnabled(start);
        chkAdEnd.setEnabled(end);
        chkAdTerm.setEnabled(term);
        chkAdFreq.setEnabled(freq);
        chkAdPayment.setEnabled(payment);
    }
}