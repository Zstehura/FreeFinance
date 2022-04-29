package com.example.financefree;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.financefree.database.DatabaseManager;
import com.example.financefree.structures.Payment;
import com.example.financefree.structures.TaxYear;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaxEstimatorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaxEstimatorFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String YEAR_ARG_KEY = "year";
    private static final NumberFormat nf = NumberFormat.getCurrencyInstance();

    private TaxYear taxYear;
    private double calculatedIncome;
    private String prefFilingAs;

    private SwitchCompat swiUseNativeData;
    private Spinner spnTaxYear, spnFilingAs;
    private EditText txtInc, txtStdDed;
    private TextView txtAfterDed, txtTaxCollected;


    public TaxEstimatorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TaxEstimatorFragment.
     */

    public static TaxEstimatorFragment newInstance() {
        return new TaxEstimatorFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tax_estimator, container, false);
        try {
            taxYear = new TaxYear(TaxYear.LAST_YEAR_AVAILABLE, getContext());
        } catch (IOException | TaxYear.YearNotFoundException e) {
            e.printStackTrace();
        }

        swiUseNativeData = view.findViewById(R.id.switchUseMyData);
        spnTaxYear = view.findViewById(R.id.spnTaxYear);
        spnFilingAs = view.findViewById(R.id.spnFileAs);
        txtInc = view.findViewById(R.id.txtGrossInc);
        txtStdDed = view.findViewById(R.id.txtStdDeduction);
        txtAfterDed = view.findViewById(R.id.txtAfterDeductions);
        txtTaxCollected = view.findViewById(R.id.txtTaxCollected);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(requireContext());
        prefFilingAs = pref.getString(getString(R.string.file_as_key), TaxYear.FILING_STATUS.get(0));

        List<String> yrs = new ArrayList<>();
        for(int i = TaxYear.FIRST_YEAR_AVAILABLE; i <= TaxYear.LAST_YEAR_AVAILABLE; i++) {
            yrs.add(String.valueOf(i));
        }
        ArrayAdapter<String> yrAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_dropdown_item_1line,
                yrs);
        ArrayAdapter<String> fileAsAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_dropdown_item_1line,
                TaxYear.FILING_STATUS_H);
        spnTaxYear.setAdapter(yrAdapter);
        spnFilingAs.setAdapter(fileAsAdapter);

        swiUseNativeData.setOnClickListener(view1 -> setUseData(swiUseNativeData.isChecked()));
        spnTaxYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setYear(i + TaxYear.FIRST_YEAR_AVAILABLE);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { Log.e("spnTaxYear", "ERROR: nothing selected"); }
        });
        spnFilingAs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                taxYear.setFileAs(TaxYear.FILING_STATUS.get(i));
                if(swiUseNativeData.isChecked()){
                    setStdDed(taxYear.getStdDeduction(), false);
                }
                updateTaxCalculations();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.e("spnFileAs", "ERROR: Nothing selected");
            }
        });
        txtStdDed.setOnEditorActionListener((textView, i, keyEvent) -> {
            taxYear.setUsingStdDeduction(false);
            taxYear.setDeductions(new ArrayList<>(Collections.singletonList(getNum(txtStdDed))));
            updateTaxCalculations();
            return false;
        });
        txtInc.setOnEditorActionListener(((textView, i, keyEvent) -> {
            taxYear.setIncome(getNum(txtInc));
            updateTaxCalculations();
            return false;
        }));

        // Initialize Data
        spnFilingAs.setSelection(TaxYear.getFilingStatus(prefFilingAs));
        spnTaxYear.setSelection(yrAdapter.getCount()-1);
        swiUseNativeData.setChecked(true);

        return view;
    }

    //
    //  UI update/calculation methods
    //

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setYear(int year) {
        AtomicReference<Double> calcInc = new AtomicReference<>();
        Thread t = new Thread(() -> {
            List<Payment> lp = DatabaseManager.getAnnualPayments(year);
            double d = 0;
            for(Payment p: lp){
                if(p.amount > 0) d += p.amount;
            }
            calcInc.set(d);
        });
        t.start();

        try {
            taxYear.setYear(year);
        } catch (TaxYear.YearNotFoundException e) {
            e.printStackTrace();
        }
        taxYear.setFileAs(TaxYear.FILING_STATUS.get(spnFilingAs.getSelectedItemPosition()));

        try { t.join(); }
        catch (InterruptedException e) { e.printStackTrace();}
        calculatedIncome = calcInc.get();

        if(swiUseNativeData.isChecked()){
            setUseData(true);
        }
    }

    private void setUseData(boolean useData) {
        if(useData){
            taxYear.setFileAs(TaxYear.getFilingKey(prefFilingAs));
            taxYear.setUsingStdDeduction(true);
            taxYear.setIncome(calculatedIncome);
            spnFilingAs.setSelection(TaxYear.getFilingStatus(prefFilingAs));
            setIncome(calculatedIncome, false);
            setStdDed(taxYear.getStdDeduction(), false);
        }
        else {
            setIncome(calculatedIncome, true);
            setStdDed(taxYear.getStdDeduction(), true);
        }
        updateTaxCalculations();
    }

    private void setIncome(double amount, boolean enabled){
        String s = nf.format(amount);
        if(enabled) s = String.valueOf(amount);
        txtInc.setText(s);
        txtInc.setEnabled(enabled);
    }

    private void setStdDed(double amount, boolean enabled) {
        String s = nf.format(amount);
        if(enabled) s = String.valueOf(amount);
        txtStdDed.setText(s);
        txtStdDed.setEnabled(enabled);
    }

    private void updateTaxCalculations(){
        double d = getNum(txtInc) - getNum(txtStdDed);
        if(d < 0) d = 0;
        txtAfterDed.setText(nf.format(d));
        try {
            txtTaxCollected.setText(nf.format(taxYear.getTax()));
        } catch (TaxYear.FilingNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Getter functions for views

    private double getNum(EditText editText) {
        double d = 0;
        String num = editText.getText().toString().replace(",", "");
        num = num.replace("$", "");

        try {
            d = Double.parseDouble(num);
        }
        catch (Exception e){
            Log.e(this.getClass().getName(), e.getMessage());
        }
        return d;
    }
}