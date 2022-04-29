package com.example.financefree.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.DialogFragment;

import com.example.financefree.R;
import com.example.financefree.RecurringPaymentFragment;
import com.example.financefree.database.DatabaseManager;
import com.example.financefree.database.entities.BankAccount;
import com.example.financefree.database.entities.RecurringPayment;
import com.example.financefree.recyclers.RecurringPaymentRecyclerViewAdapter;
import com.example.financefree.structures.CashFlow;
import com.example.financefree.structures.DateParser;
import com.example.financefree.structures.Frequency;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecurringPaymentDialog extends DialogFragment {

    public static final String NAME_KEY = "name";
    public static final String RP_ID_KEY = "id";
    public static final String FREQUENCY_TYPE_KEY = "frequency_type";
    public static final String FREQUENCY_NUM_KEY = "frequency";
    public static final String AMOUNT_KEY = "amount";
    public static final String START_DATE_KEY = "start_date";
    public static final String END_DATE_KEY = "end_date";
    public static final String NOTES_KEY = "notes";
    public static final String BANK_ID_KEY = "bank_id";
    public static final String POSITION_KEY = "pos";
    public static final String IS_NEW_KEY = "is_new";

    public void setListener(RecurringPaymentDialogListener listener) {
        this.listener = listener;
    }

    public interface RecurringPaymentDialogListener {
        void onDialogPositiveClick(RecurringPaymentDialog dialog);
        void onDialogNegativeClick(RecurringPaymentDialog dialog);
    }

    public TextView lblFreq, lblFreq2, lblEndDate;
    public CheckBox chkNoEnd;
    public EditText txtName, txtFrequencyNum, txtStart,
            txtEnd, txtNotes;
    private EditText txtAmount;
    public Spinner spnBank, spnFreqType, spnDow, spnCashFlow;
    public long bankId, rpId;
    public boolean isNew;
    public int freqNum, freqType, position;
    private RecurringPaymentDialogListener listener;
    public CashFlow flow;
    private final Map<Long,String> mBanks = new HashMap<>();

    public double getAmount() {
        flow.setFlowMag(Double.parseDouble(txtAmount.getText().toString()));
        return flow.getFlow();
    }

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        try {
            listener = (RecurringPaymentDialogListener) context;
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    
    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        flow = new CashFlow();
        Thread t = new Thread(() -> {
            List<BankAccount> l = DatabaseManager.getBankAccountDao().getAll();
            for(BankAccount ba: l){
                mBanks.put(ba.bank_id, ba.name);
            }
        });
        t.start();

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialog = inflater.inflate(R.layout.dialog_recurring_payment,null);
        Map<Integer, String> mFreqs = Frequency.typeOptions();

        // Set all values
        txtName = dialog.findViewById(R.id.txtNameRecur);
        txtAmount = dialog.findViewById(R.id.txtAmountRecur);
        txtFrequencyNum = dialog.findViewById(R.id.txtFreqNumRecur);
        txtEnd = dialog.findViewById(R.id.txtEndDateRecur);
        txtStart = dialog.findViewById(R.id.txtStartDateRecur);
        txtNotes = dialog.findViewById(R.id.txtNotesRecur);
        lblFreq = dialog.findViewById(R.id.lblFreqNumRecur);
        lblFreq2 = dialog.findViewById(R.id.lblFreqNumRecur2);
        lblEndDate = dialog.findViewById(R.id.lblEndDateRecur);
        chkNoEnd = dialog.findViewById(R.id.chkNoEnd);
        spnBank = dialog.findViewById(R.id.spnBankIdRecur);
        spnFreqType = dialog.findViewById(R.id.spnFreqTypeRecur);
        spnDow = dialog.findViewById(R.id.spnDowRecur);
        spnCashFlow = dialog.findViewById(R.id.spnBeingPaidRecur);


        try {t.join();}
        catch (InterruptedException e) {e.printStackTrace();}
        List<String> lBanks, lFreqs;
        lBanks = new ArrayList<>(mBanks.values());
        lFreqs = new ArrayList<>(mFreqs.values());

        if(lBanks.size() < 1) {
            return new AlertDialog.Builder(getContext())
                    .setTitle("A Bank Account is REQUIRED to add payments")
                    .setPositiveButton("Ok", (dialogInterface, i) -> {})
                    .create();
        }

        ArrayAdapter<String> bankAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_dropdown_item_1line,
                lBanks);
        ArrayAdapter<String> freqAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_dropdown_item_1line,
                lFreqs);
        ArrayAdapter<String> dowAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_dropdown_item_1line,
                Arrays.asList("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"));
        ArrayAdapter<String> flowAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_dropdown_item_1line,
                CashFlow.getChoices());
        spnBank.setAdapter(bankAdapter);
        spnFreqType.setAdapter(freqAdapter);
        spnDow.setAdapter(dowAdapter);
        spnCashFlow.setAdapter(flowAdapter);
        spnBank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                int i=0;
                for(long l: mBanks.keySet()) {
                    if(i == position) bankId = l;
                    i++;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {bankId = -1;}
        });
        spnFreqType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                freqType = position;
                if(position == 0) {
                    // Specific date every month
                    setETVis();
                    lblFreq.setText(R.string.date);
                    txtFrequencyNum.setText("");
                }
                else if(position == 1){
                    // Every X days
                    setETVis();
                    lblFreq.setText(R.string.every);
                    txtFrequencyNum.setText("");
                }
                else if(position == 2){
                    // Every X weeks
                    setETVis();
                    lblFreq.setText(R.string.every);
                    txtFrequencyNum.setText("");
                }
                else if(position == 3){
                    // Every X months
                    setETVis();
                    lblFreq.setText(R.string.every);
                    txtFrequencyNum.setText("");
                }
                else if(position == 4){
                    // Every month, 1st & 3rd
                    setSpnVis();
                }
                else if(position == 5){
                    // Every month, 2nd & 4th
                    setSpnVis();
                }
                else if(position == 6){
                    // last day of the month
                    setGone();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {setGone();}
            private void setGone(){
                txtFrequencyNum.setVisibility(View.GONE);
                lblFreq.setVisibility(View.GONE);
                spnDow.setVisibility(View.GONE);
                lblFreq2.setVisibility(View.GONE);
            }
            private void setETVis() {
                txtFrequencyNum.setVisibility(View.VISIBLE);
                lblFreq.setVisibility(View.VISIBLE);
                spnDow.setVisibility(View.GONE);
                lblFreq2.setVisibility(View.GONE);
            }
            private void setSpnVis(){
                txtFrequencyNum.setVisibility(View.GONE);
                lblFreq.setVisibility(View.GONE);
                spnDow.setVisibility(View.VISIBLE);
                lblFreq2.setVisibility(View.VISIBLE);
            }
        });
        spnDow.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                freqNum = (i+1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                freqNum = -1;
            }
        });
        spnCashFlow.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) { flow.setCashFlow(position); }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        chkNoEnd.setOnClickListener(view -> {
            if(((CheckBox) view).isChecked()){
                txtEnd.setText("12/31/2099");
                txtEnd.setVisibility(View.GONE);
                lblEndDate.setVisibility(View.GONE);
            }
            else {
                txtEnd.setText(DateParser.getToday());
                txtEnd.setVisibility(View.VISIBLE);
                lblEndDate.setVisibility(View.VISIBLE);
            }
        });

        assert getArguments() != null;
        freqType = getArguments().getInt(FREQUENCY_TYPE_KEY);
        freqNum = getArguments().getInt(FREQUENCY_NUM_KEY);
        if(freqType >= 0 && freqType <= 3) txtFrequencyNum.setText(String.valueOf(freqNum));
        else if(freqType <= 5) spnDow.setSelection((freqNum-1));
        spnFreqType.setSelection(freqType);
        txtName.setText(getArguments().getString(NAME_KEY));
        txtAmount.setText(String.valueOf(getArguments().getDouble(AMOUNT_KEY)));

        long d = getArguments().getLong(END_DATE_KEY);
        if(DateParser.getLong(1,1,2099) < d){
            chkNoEnd.setChecked(true);
            txtEnd.setVisibility(View.GONE);
            lblEndDate.setVisibility(View.GONE);
        }
        else{
            chkNoEnd.setChecked(false);
            txtEnd.setVisibility(View.VISIBLE);
            lblEndDate.setVisibility(View.VISIBLE);
        }
        txtEnd.setText(DateParser.getString(d));
        txtStart.setText(DateParser.getString(getArguments().getLong(START_DATE_KEY)));
        txtNotes.setText(getArguments().getString(NOTES_KEY));
        position = getArguments().getInt(POSITION_KEY);
        rpId = getArguments().getLong(RP_ID_KEY);
        isNew = getArguments().getBoolean(IS_NEW_KEY);
        bankId = getArguments().getLong(BANK_ID_KEY);

        int n = 0;
        for(long id: mBanks.keySet()) {
            if(id == bankId) spnBank.setSelection(n);
            n++;
        }
        txtFrequencyNum.setText(String.valueOf(freqNum));

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.bank_account)
                .setView(dialog)
                .setPositiveButton(R.string.set, (dialogInterface, i) -> listener.onDialogPositiveClick(RecurringPaymentDialog.this))
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> listener.onDialogNegativeClick(RecurringPaymentDialog.this))
                .create();
    }

    
    @NonNull
    public static RecurringPaymentDialog newInstance(RecurringPayment rp, int position){
        RecurringPaymentDialog f = new RecurringPaymentDialog();

        Bundle args = new Bundle();

        if (rp == null) {
            args.putString(NAME_KEY, "");
            args.putInt(FREQUENCY_TYPE_KEY, 6);
            args.putInt(FREQUENCY_NUM_KEY, 1);
            args.putDouble(AMOUNT_KEY, 0);
            args.putLong(START_DATE_KEY, DateParser.getLong(0,1,2020));
            args.putLong(END_DATE_KEY, DateParser.getLong(11,31,2099));
            args.putString(NOTES_KEY, "");
            args.putLong(BANK_ID_KEY, 0);
            args.putLong(RP_ID_KEY, -1);
            args.putBoolean(IS_NEW_KEY, true);
        }
        else {
            args.putString(NAME_KEY, rp.name);
            args.putInt(FREQUENCY_TYPE_KEY, rp.type_option);
            args.putInt(FREQUENCY_NUM_KEY, rp.frequency_number);
            args.putDouble(AMOUNT_KEY, rp.amount);
            args.putLong(START_DATE_KEY, rp.start_date);
            args.putLong(END_DATE_KEY, rp.end_date);
            args.putString(NOTES_KEY, rp.notes);
            args.putLong(BANK_ID_KEY, rp.bank_id);
            args.putLong(RP_ID_KEY, rp.rp_id);
            args.putBoolean(IS_NEW_KEY, false);
        }
        args.putInt(POSITION_KEY, position);
        f.setArguments(args);
        return f;
    }
}
