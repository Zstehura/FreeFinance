package com.example.financefree;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

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
import java.util.List;
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

    private final TextView.OnEditorActionListener dedListener = (textView, i, keyEvent) -> {
        updateTotalDeductions();
        return false;
    };

    private SwitchCompat swiUseNativeData;
    private Spinner spnTaxYear, spnFilingAs;
    private EditText txtInc, txtStdDed;
    private TextView lblTotalCred, txtTotalDed, txtTotalCred, txtAfterDed, txtTax, txtTaxCollected;
    private TextView lblDed1, lblDed2, lblDed3, lblCred1, lblCred2;
    private Button btnAddDeduction, btnAddCredit;
    private GridLayout grdCredits, grdDeductions;


    public TaxEstimatorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param nYear Tax year to be used.
     * @return A new instance of fragment TaxEstimatorFragment.
     */

    public static TaxEstimatorFragment newInstance(int nYear) {
        TaxEstimatorFragment fragment = new TaxEstimatorFragment();
        Bundle args = new Bundle();
        args.putInt(YEAR_ARG_KEY, nYear);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            int year = getArguments().getInt(YEAR_ARG_KEY);
            try {
                taxYear = new TaxYear(year, getContext());
            } catch (IOException | TaxYear.YearNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tax_estimator, container, false);

        swiUseNativeData = view.findViewById(R.id.switchUseMyData);
        spnTaxYear = view.findViewById(R.id.spnTaxYear);
        spnFilingAs = view.findViewById(R.id.spnFileAs);
        txtInc = view.findViewById(R.id.txtGrossInc);
        txtStdDed = view.findViewById(R.id.txtStdDeduction);
        txtTotalDed = view.findViewById(R.id.txtTotalDeductions);
        txtTotalCred = view.findViewById(R.id.txtTotalCredits);
        txtAfterDed = view.findViewById(R.id.txtAfterDeductions);
        txtTax = view.findViewById(R.id.txtTax);
        txtTaxCollected = view.findViewById(R.id.txtTaxCollected);
        btnAddDeduction = view.findViewById(R.id.btnAddDeduction);
        btnAddCredit = view.findViewById(R.id.btnAddCredit);
        grdCredits = view.findViewById(R.id.grdCredits);
        grdDeductions = view.findViewById(R.id.grdDeductions);
        lblDed1 = view.findViewById(R.id.lblDed1);
        lblDed2 = view.findViewById(R.id.lblDed2);
        lblDed3 = view.findViewById(R.id.lblDed3);
        lblCred1 = view.findViewById(R.id.lblCred1);
        lblCred2 = view.findViewById(R.id.lblCred2);
        lblTotalCred = view.findViewById(R.id.lblTotalCreds);


        List<String> yrs = new ArrayList<>();
        for(int i = TaxYear.FIRST_YEAR_AVAILABLE; i <= TaxYear.LAST_YEAR_AVAILABLE; i++) {
            yrs.add(String.valueOf(i));
        }
        ArrayAdapter<String> yrAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_dropdown_item_1line,
                yrs);
        ArrayAdapter<String> fileAsAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_dropdown_item_1line,
                TaxYear.FILING_STATUS);
        spnTaxYear.setAdapter(yrAdapter);
        spnFilingAs.setAdapter(fileAsAdapter);

        swiUseNativeData.setOnClickListener(view1 -> {
            setUseNativeData(swiUseNativeData.isChecked());
        });
        txtInc.setOnEditorActionListener((textView, i, keyEvent) -> {
            taxYear.setIncome(getNum(txtInc));
            updateAfterDeductions();
            try {updateTaxLbls();}
            catch (TaxYear.FilingNotFoundException e) {e.printStackTrace();}
            return false;
        });
        spnTaxYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                calculateIncome(i + TaxYear.FIRST_YEAR_AVAILABLE);
                taxYear.setYear(i + TaxYear.FIRST_YEAR_AVAILABLE);
                swiUseNativeData.setChecked(true);
                setUseNativeData(true);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.e("TaxYearSelector", "ERROR: nothing clicked");
            }
        });
        spnFilingAs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                taxYear.setFileAs(TaxYear.FILING_STATUS.get(i));
                updateStdDeduction();
                updateTotalDeductions();
                updateAfterDeductions();
                try {updateTaxLbls();}
                catch (TaxYear.FilingNotFoundException e) {e.printStackTrace();}
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.e("FilingAsSelector", "ERROR: nothing clicked");
            }
        });
        txtStdDed.setOnEditorActionListener(dedListener);
        btnAddDeduction.setOnClickListener(view1 -> {
            EditText etName = new EditText(getContext());
            EditText etAmount = new EditText(getContext());
            ImageButton btnDel = new ImageButton(getContext());
            btnDel.setImageResource(R.drawable.ic_delete);
            btnDel.setBackgroundColor(getResources().getColor(R.color.dark_red));
            btnDel.setOnClickListener(view2 -> {
                removeDeduction((ImageButton) view2);
            });

            etAmount.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
            etAmount.setOnEditorActionListener(dedListener);
            etAmount.setLabelFor(btnDel.getId());
            etName.setLabelFor(btnDel.getId());
            for(int i = grdDeductions.getChildCount() - 1;i >= 0; i--){
                if(grdDeductions.getChildAt(i).getId() == view1.getId()){
                    grdDeductions.setC
                }
            }

        });



        return view;
    }

    //
    //  UI update/calculation methods
    //

    private void setUseNativeData(boolean useNativeData) {
        setIncome(calculatedIncome, !useNativeData);
        setCreditsEnabled(!useNativeData);
        setDeductionsEnabled(!useNativeData);
        updateTotalDeductions();
        updateTotalCredits();
        updateAfterDeductions();
        try {updateTaxLbls();}
        catch (TaxYear.FilingNotFoundException e) {e.printStackTrace();}
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void calculateIncome(int year) {
        AtomicReference<Double> n = new AtomicReference<>();
        Thread t = new Thread(() -> {
            List<Payment> lp = DatabaseManager.getAnnualPayments(year);
            double d = 0;
            for(Payment p: lp) {
                if(p.amount > 0) {
                    d += p.amount;
                }
            }
            n.set(d);
        });
        t.start();
        try {
            t.join();
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        calculatedIncome = n.get();
    }

    private void setIncome(double num, boolean enabled){
        taxYear.setIncome(num);
        if(enabled) {
            txtInc.setText(String.valueOf(num));
        }
        else {
            txtInc.setText(nf.format(num));
        }
        txtInc.setEnabled(enabled);
    }

    private void setDeductionsEnabled(boolean enabled){
        for(int i = grdDeductions.getChildCount() - 1; i >= 0; i--){
            if(grdDeductions.getChildAt(i).getId() == btnAddDeduction.getId()) {
                grdDeductions.getChildAt(i).setEnabled(enabled);
                if(enabled) grdDeductions.getChildAt(i).setVisibility(View.VISIBLE);
                else grdDeductions.getChildAt(i).setVisibility(View.GONE);
            }
            else if(grdDeductions.getChildAt(i).getId() == txtStdDed.getId()) {
                grdDeductions.getChildAt(i).setEnabled(enabled);
                if(enabled && grdDeductions.getChildAt(i) instanceof EditText)
                    ((EditText) grdDeductions.getChildAt(i)).setText(String.valueOf(taxYear.getStdDeduction()));
            }
            else if(grdDeductions.getChildAt(i).getId() != lblDed1.getId() &&
                    grdDeductions.getChildAt(i).getId() != lblDed2.getId() &&
                    grdDeductions.getChildAt(i).getId() != lblDed3.getId()) {
                if(!enabled) grdDeductions.removeViewAt(i);
            }
        }
    }

    private void removeDeduction(ImageButton btnDel) {
        for(int i = grdDeductions.getChildCount() - 1; i >= 0; i--){
            if(grdDeductions.getChildAt(i).getLabelFor() == btnDel.getId()){
                grdDeductions.removeViewAt(i);
            }
        }
        grdDeductions.removeView(btnDel);
    }

    private void updateTotalDeductions() {
        List<Double> deds = new ArrayList<>();
        for(int i = 0; i < grdDeductions.getChildCount(); i++){
            if(grdDeductions.getChildAt(i) instanceof EditText && (i % 3) == 2) {
                deds.add(getNum((EditText) grdDeductions.getChildAt(i)));
            }
        }
        taxYear.setDeductions(deds);
        txtTotalDed.setText(nf.format(taxYear.getDeductionSum()));
    }

    private void  updateAfterDeductions(){
        double n = taxYear.getIncome() - taxYear.getDeductionSum();
        if(n < 0) n = 0;
        txtAfterDed.setText(nf.format(n));
    }

    private void updateStdDeduction() {
        txtStdDed.setText(nf.format(taxYear.getStdDeduction()));
    }

    private void updateTaxLbls() throws TaxYear.FilingNotFoundException {
        txtTax.setText(nf.format(taxYear.getTaxNoCreds()));
        txtTaxCollected.setText(nf.format(taxYear.getTax()));
    }

    private void setCreditsEnabled(boolean enabled){
        if(!enabled) {
            for(int i = grdCredits.getChildCount()-1; i >= 0;i--){
                if(grdCredits.getChildAt(i).getId() != lblCred1.getId() &&
                        grdCredits.getChildAt(i).getId() != lblCred2.getId()) {
                    grdCredits.removeViewAt(i);
                }
            }
            grdCredits.setVisibility(View.GONE);
            txtTotalCred.setVisibility(View.GONE);
            lblTotalCred.setVisibility(View.GONE);
        }
        else {
            grdCredits.setVisibility(View.VISIBLE);
            txtTotalCred.setVisibility(View.VISIBLE);
            lblTotalCred.setVisibility(View.VISIBLE);
        }
    }

    private void updateTotalCredits() {
        List<Double> creds = new ArrayList<>();
        for(int i = 0; i < grdCredits.getChildCount(); i++){
            if(grdCredits.getChildAt(i) instanceof EditText && (i % 3) == 2) {
                creds.add(getNum((EditText) grdCredits.getChildAt(i)));
            }
        }
        taxYear.setCredits(creds);
        txtTotalCred.setText(nf.format(taxYear.getCreditSum()));
    }


    // Getter functions for views

    private double getNum(EditText editText) {
        double d = 0;
        try {
            d = Double.parseDouble(editText.getText().toString());
        }
        catch (Exception e){
            Log.e(this.getClass().getName(), e.getMessage());
        }
        return d;
    }
    private double getNum(TextView view) {
        double d = 0;
        try {
            d = Double.parseDouble(view.getText().toString());
        }
        catch (Exception e){
            Log.e(this.getClass().getName(), e.getMessage());
        }
        return d;
    }
}