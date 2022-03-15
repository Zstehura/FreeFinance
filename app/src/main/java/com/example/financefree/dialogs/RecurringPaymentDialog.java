package com.example.financefree.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.financefree.R;
import com.example.financefree.databaseClasses.RecurringPayment;
import com.example.financefree.structures.BankList;
import com.example.financefree.structures.parseDate;


//
//  TODO:   TEST THIS DIALOG
//

public class RecurringPaymentDialog extends DialogFragment {

    public static final String NAME_KEY = "name";
    public static final String FREQUENCY_TYPE_KEY = "frequency_type";
    public static final String FREQUENCY_KEY = "frequency";
    public static final String AMOUNT_KEY = "amount";
    public static final String START_DATE_KEY = "start_date";
    public static final String END_DATE_KEY = "end_date";
    public static final String NOTES_KEY = "notes";
    public static final String BANK_ID_KEY = "bank_id";
    public static final String ID_KEY = "key";

    public interface RecurringPaymentDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    private RecurringPaymentDialogListener listener;

    BankList banks = new BankList();
    String name, notes;
    int freq_type, freq;
    long dateSt, dateEn, bankId, rpId;
    double amount;

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

        Bundle extras = getArguments();
        if(extras != null){
            name = extras.getString(NAME_KEY);
            freq_type = extras.getInt(FREQUENCY_TYPE_KEY);
            freq = extras.getInt(FREQUENCY_KEY);
            amount = extras.getDouble(AMOUNT_KEY);
            dateSt = extras.getLong(START_DATE_KEY);
            dateEn = extras.getLong(END_DATE_KEY);
            notes = extras.getString(NOTES_KEY);
            bankId = extras.getLong(BANK_ID_KEY);
            rpId = extras.getLong(ID_KEY);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize dialog settings
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialog = inflater.inflate(R.layout.dialog_recurring_payment,null);

        // populate bank choices
        Spinner spnBank = (Spinner) dialog.findViewById(R.id.spnBankIdRecur);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_dropdown_item_1line, banks.names);
        spnBank.setAdapter(adapter);
        spnBank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                bankId = banks.ids.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                bankId = 0;
            }
        });

        // Set all values
        EditText etName, etNotes, etFreq, etStart, etEnd, etAmount;
        Spinner spnFreqType;
        CheckBox chkNoEnd;
        etName = dialog.findViewById(R.id.txtNameRecur);
        etNotes = dialog.findViewById(R.id.txtNotesRecur);
        etFreq = dialog.findViewById(R.id.txtFreqRecur);
        etStart = dialog.findViewById(R.id.txtStartDateRecur);
        etEnd = dialog.findViewById(R.id.txtEndDateRecur);
        etAmount = dialog.findViewById(R.id.txtAmountRecur);
        spnFreqType = dialog.findViewById(R.id.spnFreqTypeRecur);
        chkNoEnd = dialog.findViewById(R.id.chkNoEndRecur);
        etName.setText(name);
        etNotes.setText(notes);
        etFreq.setText(String.valueOf(freq));
        etStart.setText(parseDate.getString(dateSt));
        if(dateEn > parseDate.getLong(12,31,2098)){
            etEnd.setText("");
            etEnd.setEnabled(false);
            chkNoEnd.setChecked(true);
        }
        else {
            etEnd.setText(parseDate.getString(dateEn));
            chkNoEnd.setChecked(false);
        }
        chkNoEnd.setOnClickListener(view ->{
            if(chkNoEnd.isChecked()){
                etEnd.setText("");
                etEnd.setEnabled(false);
            }
            else {
                etEnd.setText(parseDate.getString(dateEn));
                etEnd.setEnabled(true);
            }
        });
        etAmount.setText(String.valueOf(amount));
        if(freq_type == RecurringPayment.ON) {
            spnFreqType.setSelection(0);
        }
        else {
            spnFreqType.setSelection(1);
        }
        spnFreqType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if(position == 0) freq_type = RecurringPayment.ON;
                else freq_type = RecurringPayment.EVERY;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getContext(), "This shouldn't be an option, homie", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setView(dialog)
                .setPositiveButton(R.string.set, (dialogInterface, i) -> listener.onDialogPositiveClick(RecurringPaymentDialog.this))
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> listener.onDialogNegativeClick(RecurringPaymentDialog.this));
        return builder.create();
    }

    @NonNull
    public static RecurringPaymentDialog newInstance(RecurringPayment recurringPayment){
        RecurringPaymentDialog f = new RecurringPaymentDialog();

        Bundle args = new Bundle();
        if (recurringPayment == null) {
            args.putString(NAME_KEY, "");
            args.putInt(FREQUENCY_TYPE_KEY, RecurringPayment.ON);
            args.putInt(FREQUENCY_KEY, 1);
            args.putDouble(AMOUNT_KEY, 0);
            args.putLong(START_DATE_KEY, parseDate.getLong(1,1,2020));
            args.putLong(END_DATE_KEY, parseDate.getLong(1,1,2099));
            args.putString(NOTES_KEY, "");
            args.putLong(BANK_ID_KEY, -1);
            args.putLong(ID_KEY, -1);
        }
        else {
            args.putString(NAME_KEY, recurringPayment.name);
            args.putInt(FREQUENCY_TYPE_KEY, recurringPayment.frequencyType);
            args.putInt(FREQUENCY_KEY, recurringPayment.frequency);
            args.putDouble(AMOUNT_KEY, recurringPayment.amount);
            args.putLong(START_DATE_KEY, recurringPayment.startDate);
            args.putLong(END_DATE_KEY, recurringPayment.endDate);
            args.putString(NOTES_KEY, recurringPayment.notes);
            args.putLong(BANK_ID_KEY, recurringPayment.bankId);
            args.putLong(ID_KEY, recurringPayment.rp_id);
        }
        f.setArguments(args);
        return f;
    }
}
