package com.example.financefree.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BankStatementDialog extends DialogFragment {

    // TODO: Change const
    public static final String NAME_KEY = "name";
    public static final String AMOUNT_KEY = "amount";
    public static final String DATE_KEY = "date";
    public static final String BANK_ID_KEY = "bank_id";
    public static final String ID_KEY = "id";
    public static final String BANK_NAMES_KEY = "bank_names";
    public static final String BANK_IDS_KEY = "bank_ids";

    public interface BankStatementDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    private BankStatementDialogListener listener;

    // TODO: Change vars
    List<String> bankNames;
    List<Long> bankIds;
    String name;
    long date, bankId, spId;
    double amount;

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        try {
            listener = (BankStatementDialogListener) context;
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    // TODO: Change create
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getArguments();
        if(extras != null){
            name = extras.getString(NAME_KEY);
            amount = extras.getDouble(AMOUNT_KEY);
            date = extras.getLong(DATE_KEY);
            bankId = extras.getLong(BANK_ID_KEY);
            spId = extras.getLong(ID_KEY);
            bankNames = extras.getStringArrayList(BANK_NAMES_KEY);
            bankIds = extras.getParcelable(BANK_IDS_KEY);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: Update all of this after layout is done
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View dialog = inflater.inflate(R.layout.dialog_single_payment,null);
        Spinner spnBank = (Spinner) dialog.findViewById(R.id.spnBankIdSing);
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

        builder.setView(dialog)
                .setPositiveButton(R.string.set, (dialogInterface, i) -> listener.onDialogPositiveClick(BankStatementDialog.this))
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> listener.onDialogNegativeClick(BankStatementDialog.this));

        return builder.create();
    }


    // TODO: Change all this around
    @NonNull
    public static BankStatementDialog newInstance() { //@NonNull BankStatement bankStatement, @NonNull Map<Long, String> banks){
        BankStatementDialog f = new BankStatementDialog();

        Bundle args = new Bundle();
       // args.putString(NAME_KEY, DatabaseAccessor.getBankName(bankStatement.bank_id));
       // args.putDouble(AMOUNT_KEY, bankStatement.amount);
       // args.putLong(DATE_KEY, bankStatement.date);
       // args.putLong(ID_KEY, bankStatement.s_id);
        // args.putStringArrayList(BANK_NAMES_KEY, new ArrayList<>(banks.values()));
        // args.putParcelable(BANK_IDS_KEY, (Parcelable) banks.keySet());
        f.setArguments(args);

        return f;
    }
}
