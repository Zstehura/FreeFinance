package com.example.financefree.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.financefree.R;
import com.example.financefree.database.DatabaseManager;
import com.example.financefree.database.entities.BankAccount;
import com.example.financefree.database.entities.BankStatement;
import com.example.financefree.structures.DateParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BankStatementDialog extends DialogFragment {

    public static final String BANK_ID_KEY = "bank_id";
    public static final String STATEMENT_ID_KEY = "s_id";
    public static final String AMOUNT_KEY = "amount";
    public static final String DATE_KEY = "date";
    public static final String IS_NEW_KEY = "new";
    public static final String POSITION_KEY = "pos";

    public interface BankStatementDialogListener {
        void onDialogPositiveClick(BankStatementDialog dialog);
        void onDialogNegativeClick(BankStatementDialog dialog);
    }

    public EditText etAmount, etDate;
    public Spinner spnBank;
    public long bankId, sId;
    public int position;
    public boolean isNew;

    private final Map<Long, String> mBanks = new HashMap<>();
    private BankStatementDialogListener listener;

    public void setListener(BankStatementDialogListener listener) {this.listener = listener;}

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
    }

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

        Thread t = new Thread(() -> {
            List<BankAccount> l = DatabaseManager.getBankAccountDao().getAll();
            for(BankAccount ba: l){
                mBanks.put(ba.bank_id, ba.name);
            }
        });
        t.start();

        spnBank = dialog.findViewById(R.id.spnBankNameStat);
        etDate = dialog.findViewById(R.id.txtDateStat);
        etAmount = dialog.findViewById(R.id.txtAmountStat);

        assert getArguments() != null;
        isNew = getArguments().getBoolean(IS_NEW_KEY);
        bankId = getArguments().getLong(BANK_ID_KEY);
        position = getArguments().getInt(POSITION_KEY);
        sId = getArguments().getLong(STATEMENT_ID_KEY);
        etDate.setText(getArguments().getString(DATE_KEY));
        etAmount.setText(String.valueOf(getArguments().getDouble(AMOUNT_KEY)));

        try{t.join();}
        catch (InterruptedException e){ Log.e("BankStatementDialog", e.getMessage());}

        List<String> lBanks = new ArrayList<>(mBanks.values());
        ArrayAdapter<String> bankAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_dropdown_item_1line,
                lBanks);
        spnBank.setAdapter(bankAdapter);
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

        int n = 0;
        for(long id: mBanks.keySet()) {
            if(id == bankId) spnBank.setSelection(n);
            n++;
        }

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.bank_account)
                .setView(dialog)
                .setPositiveButton(R.string.set, (dialogInterface, i) -> listener.onDialogPositiveClick(BankStatementDialog.this))
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> listener.onDialogNegativeClick(BankStatementDialog.this))
                .create();
    }


    
    @NonNull
    public static BankStatementDialog newInstance(BankStatement bs, int position, boolean isNew) {
        BankStatementDialog f = new BankStatementDialog();
        Bundle args = new Bundle();

        if(bs == null){
            args.putLong(BANK_ID_KEY, 0);
            args.putDouble(AMOUNT_KEY, 0);
            args.putString(DATE_KEY, DateParser.getToday());
            args.putLong(STATEMENT_ID_KEY, 0);
        }
        else {
            args.putLong(BANK_ID_KEY, bs.bank_id);
            args.putDouble(AMOUNT_KEY, bs.amount);
            args.putString(DATE_KEY, DateParser.getString(bs.date));
            args.putLong(STATEMENT_ID_KEY, bs.statement_id);
        }
        args.putBoolean(IS_NEW_KEY, isNew);
        args.putInt(POSITION_KEY, position);

        f.setArguments(args);

        return f;
    }
}
