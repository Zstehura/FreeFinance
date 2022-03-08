package com.example.financefree.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.financefree.R;
import com.example.financefree.databaseClasses.AppDatabase;
import com.example.financefree.databaseClasses.RecurringPayment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AddEditRecurringPaymentDialog extends DialogFragment {

    public static final String NAME_KEY = "name";
    public static final String FREQUENCY_TYPE_KEY = "frequency_type";
    public static final String FREQUENCY_KEY = "frequency";
    public static final String AMOUNT_KEY = "amount";
    public static final String START_DATE_KEY = "start_date";
    public static final String END_DATE_KEY = "end_date";
    public static final String NOTES_KEY = "notes";
    public static final String BANK_ID_KEY = "bank_id";
    public static final String ID_KEY = "key";
    public static final String BANK_NAMES_KEY = "bank_names";
    public static final String BANK_IDS_KEY = "bank_ids";

    public interface RecurringPaymentDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    private RecurringPaymentDialogListener listener;

    List<String> bankNames;
    List<Long> bankIds;
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
            bankNames = extras.getStringArrayList(BANK_NAMES_KEY);
            bankIds = extras.getParcelable(BANK_IDS_KEY);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View dialog = inflater.inflate(R.layout.add_edit_recurringpayment_dialog,null);
        Spinner spnBank = (Spinner) dialog.findViewById(R.id.spnBankIdRecur);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_dropdown_item_1line, bankNames);
        spnBank.setAdapter(adapter);
        spnBank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                bankId = bankIds.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                bankId = 0;
            }
        });

        ;builder.setView(dialog)
                .setPositiveButton(R.string.set, (dialogInterface, i) -> listener.onDialogPositiveClick(AddEditRecurringPaymentDialog.this))
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> listener.onDialogNegativeClick(AddEditRecurringPaymentDialog.this));

        return builder.create();
    }

    @NonNull
    public static AddEditRecurringPaymentDialog newInstance(@NonNull RecurringPayment recurringPayment, @NonNull Map<Long, String> banks){
        AddEditRecurringPaymentDialog f = new AddEditRecurringPaymentDialog();

        Bundle args = new Bundle();
        args.putString(NAME_KEY, recurringPayment.name);
        args.putInt(FREQUENCY_TYPE_KEY, recurringPayment.frequencyType);
        args.putInt(FREQUENCY_KEY, recurringPayment.frequency);
        args.putDouble(AMOUNT_KEY, recurringPayment.amount);
        args.putLong(START_DATE_KEY, recurringPayment.startDate);
        args.putLong(END_DATE_KEY, recurringPayment.endDate);
        args.putString(NOTES_KEY, recurringPayment.notes);
        args.putLong(BANK_ID_KEY, recurringPayment.bankId);
        args.putLong(ID_KEY, recurringPayment.rp_id);
        args.putStringArrayList(BANK_NAMES_KEY, new ArrayList<>(banks.values()));
        args.putParcelable(BANK_IDS_KEY, (Parcelable) banks.keySet());
        f.setArguments(args);

        return f;
    }
}
