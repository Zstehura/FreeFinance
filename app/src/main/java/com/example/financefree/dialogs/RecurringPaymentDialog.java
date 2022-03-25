package com.example.financefree.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import com.example.financefree.R;
import com.example.financefree.fileClasses.DataManager;
import com.example.financefree.fileClasses.RecurringPayment;
import com.example.financefree.structures.parseDate;

import java.util.HashMap;
import java.util.Map;


//
//  TODO:   TEST THIS DIALOG
//

public class RecurringPaymentDialog extends DialogFragment {

    public static final String NAME_KEY = "name";
    public static final String FREQUENCY_TYPE_KEY = "frequency_type";
    public static final String FREQUENCY_NUM_KEY = "frequency";
    public static final String AMOUNT_KEY = "amount";
    public static final String START_DATE_KEY = "start_date";
    public static final String END_DATE_KEY = "end_date";
    public static final String NOTES_KEY = "notes";
    public static final String BANK_ID_KEY = "bank_id";

    public interface RecurringPaymentDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    public EditText txtName, txtFrequencyNum, txtAmount, txtStart,
            txtEnd, txtNotes;
    public Spinner spnBank, spnFreqType;
    public long bankId;
    private RecurringPaymentDialogListener listener;
    private Map<Long,String> mBanks;
    private Map<Integer,String> mFreqs;

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

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize dialog settings
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialog = inflater.inflate(R.layout.dialog_recurring_payment,null);

        // populate bank & Frequency choices
        mBanks = DataManager.getBankMap();
        //mFreqs = RecurringPayment.getFrequencyTypes();
        spnBank = dialog.findViewById(R.id.spnBankIdRecur);
        spnFreqType = dialog.findViewById(R.id.spnFreqTypeRecur);
        ArrayAdapter<String> bankAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_dropdown_item_1line,
                (String[]) mBanks.values().toArray());
        ArrayAdapter<String> freqAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_dropdown_item_1line,
                (String[]) mFreqs.values().toArray());
        spnBank.setAdapter(bankAdapter);
        spnFreqType.setAdapter(freqAdapter);
        spnBank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                int i=0;
                for(long l: mBanks.keySet()) {
                    if(i == position) {
                        bankId = l;
                    }
                    i++;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {bankId = 0;}
        });
        spnFreqType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {bankId = 0;}
        });

        // Set all values
        txtName = dialog.findViewById(R.id.txtNameRecur);
        txtAmount = dialog.findViewById(R.id.txtAmountRecur);
        //txtFrequencyNum = dialog.findViewById(R.id.txtFreqRecur);
        txtEnd = dialog.findViewById(R.id.txtEndDateRecur);
        txtStart = dialog.findViewById(R.id.txtStartDateRecur);
        txtNotes = dialog.findViewById(R.id.txtNotesRecur);
        assert getArguments() != null;
        txtName.setText(getArguments().getString(NAME_KEY));
        txtAmount.setText(String.valueOf(getArguments().getDouble(AMOUNT_KEY)));
        txtFrequencyNum.setText(String.valueOf(getArguments().getInt(FREQUENCY_NUM_KEY)));
        txtEnd.setText(parseDate.getString(getArguments().getLong(END_DATE_KEY)));
        txtStart.setText(parseDate.getString(getArguments().getLong(START_DATE_KEY)));
        txtNotes.setText(getArguments().getString(NOTES_KEY));

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.bank_account)
                .setView(dialog)
                .setPositiveButton(R.string.set, (dialogInterface, i) -> listener.onDialogPositiveClick(RecurringPaymentDialog.this))
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> listener.onDialogNegativeClick(RecurringPaymentDialog.this))
                .create();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    public static RecurringPaymentDialog newInstance(RecurringPayment recurringPayment){
        RecurringPaymentDialog f = new RecurringPaymentDialog();

        Bundle args = new Bundle();

        if (recurringPayment == null) {
            args.putString(NAME_KEY, "");
            //args.putInt(FREQUENCY_TYPE_KEY, RecurringPayment.FREQ_ON_DATE_MONTHLY);
            args.putInt(FREQUENCY_NUM_KEY, 1);
            args.putDouble(AMOUNT_KEY, 0);
            args.putLong(START_DATE_KEY, parseDate.getLong(1,1,2020));
            args.putLong(END_DATE_KEY, parseDate.getLong(1,1,2099));
            args.putString(NOTES_KEY, "");
            args.putLong(BANK_ID_KEY, 0);
        }
        else {
            args.putString(NAME_KEY, recurringPayment.name);
            //args.putInt(FREQUENCY_TYPE_KEY, recurringPayment.frequencyType);
            //args.putInt(FREQUENCY_NUM_KEY, recurringPayment.frequencyNum);
            args.putDouble(AMOUNT_KEY, recurringPayment.amount);
            //args.putLong(START_DATE_KEY, recurringPayment.startDate);
            //args.putLong(END_DATE_KEY, recurringPayment.endDate);
            args.putString(NOTES_KEY, recurringPayment.notes);
            args.putLong(BANK_ID_KEY, recurringPayment.bankId);
        }

        f.setArguments(args);
        return f;
    }
}
