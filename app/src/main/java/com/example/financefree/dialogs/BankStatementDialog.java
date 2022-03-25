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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.financefree.R;
import com.example.financefree.structures.parseDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BankStatementDialog extends DialogFragment {

    // TODO: Change const
    public static final String NAME_KEY = "name";
    public static final String AMOUNT_KEY = "amount";
    public static final String DATE_KEY = "date";

    public interface BankStatementDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    private BankStatementDialogListener listener;

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
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialog = inflater.inflate(R.layout.dialog_bank_statement,null);

        TextView txtName = dialog.findViewById(R.id.txtBankNameStat);
        EditText txtDate = dialog.findViewById(R.id.txtDateStat);
        EditText txtAmount = dialog.findViewById(R.id.txtAmountStat);
        assert getArguments() != null;
        txtName.setText(getArguments().getString(NAME_KEY));
        txtDate.setText(getArguments().getString(DATE_KEY));
        txtAmount.setText(String.valueOf(getArguments().getDouble(AMOUNT_KEY)));

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.bank_account)
                .setView(dialog)
                .setPositiveButton(R.string.set, (dialogInterface, i) -> listener.onDialogPositiveClick(BankStatementDialog.this))
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> listener.onDialogNegativeClick(BankStatementDialog.this))
                .create();
    }


    @NonNull
    public static BankStatementDialog newInstance(String name, double amount, long date) {
        BankStatementDialog f = new BankStatementDialog();
        Bundle args = new Bundle();

        args.putDouble(AMOUNT_KEY, amount);
        args.putString(DATE_KEY, parseDate.getString(date));
        args.putString(NAME_KEY, name);
        f.setArguments(args);

        return f;
    }
}
