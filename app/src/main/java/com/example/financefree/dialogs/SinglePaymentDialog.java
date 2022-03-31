package com.example.financefree.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.financefree.R;
import com.example.financefree.structures.PaymentGeneric;

public class SinglePaymentDialog extends DialogFragment {

    public static final String NAME_KEY = "name";
    public static final String AMOUNT_KEY = "amount";
    public static final String DATE_KEY = "date";
    public static final String BANK_ID_KEY = "bank_id";
    public static final String ID_KEY = "id";
    public static final String TYPE_KEY = "type";

    public interface SinglePaymentDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    private SinglePaymentDialogListener listener;

    //BankList banks = new BankList();
    String name;
    long date, bankId, spId;
    double amount;
    char cType;

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        try {
            listener = (SinglePaymentDialogListener) context;
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
            amount = extras.getDouble(AMOUNT_KEY);
            date = extras.getLong(DATE_KEY);
            bankId = extras.getLong(BANK_ID_KEY);
            spId = extras.getLong(ID_KEY);
            cType = extras.getChar(TYPE_KEY);
        }

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize dialog components
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialog = inflater.inflate(R.layout.dialog_single_payment,null);

        // populate BankPicker
        Spinner spnBank = (Spinner) dialog.findViewById(R.id.spnBankIdSing);
        //ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_dropdown_item_1line, banks.names);
        //spnBank.setAdapter(adapter);
        spnBank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
           //     bankId = banks.ids.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                bankId = 0;
            }
        });

        // Lock name if its a recurring payment
        if(cType == 'r'){
            EditText txt = dialog.findViewById(R.id.txtNameSing);
            txt.setEnabled(false);
        }




        builder.setView(dialog)
                .setPositiveButton(R.string.set, (dialogInterface, i) -> listener.onDialogPositiveClick(SinglePaymentDialog.this))
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> listener.onDialogNegativeClick(SinglePaymentDialog.this));
        return builder.create();
    }

    public static SinglePaymentDialog newInstance(PaymentGeneric p){
        SinglePaymentDialog f = new SinglePaymentDialog();
        Bundle args = new Bundle();

        if(p != null) {
            args.putString(NAME_KEY, p.name);
            args.putDouble(AMOUNT_KEY, p.amount);
            args.putLong(DATE_KEY, p.date);
            args.putLong(ID_KEY, p.id);
            args.putChar(TYPE_KEY, p.cType);
        }

        f.setArguments(args);

        return f;
    }
}
