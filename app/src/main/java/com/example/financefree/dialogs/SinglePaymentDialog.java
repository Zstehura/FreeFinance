package com.example.financefree.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
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
import com.example.financefree.structures.CashFlow;
import com.example.financefree.structures.DateParser;
import com.example.financefree.structures.Payment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SinglePaymentDialog extends DialogFragment {

    public static final String NAME_KEY = "name";
    public static final String NOTES_KEY = "notes";
    public static final String AMOUNT_KEY = "amount";
    public static final String DATE_KEY = "date";
    public static final String BANK_ID_KEY = "bank_id";
    public static final String EDIT_ID_KEY = "edit";
    public static final String PAYMENT_ID_KEY = "p_id";
    public static final String IS_NEW_KEY = "new";
    public static final String ID_KEY = "id";
    public static final String POSITION_KEY = "pos";

    public interface SinglePaymentDialogListener {
        void onDialogPositiveClick(SinglePaymentDialog dialog);
        void onDialogNegativeClick(SinglePaymentDialog dialog);
    }

    private SinglePaymentDialogListener listener;

    private final Map<Long,String> mBanks = new HashMap<>();
    public EditText txtName, txtDate, txtNotes;
    private EditText txtAmount;
    public Spinner spnBanks, spnCashFlow;
    public long bankId, pId, editId;
    public boolean isEdit, isNew;
    public CashFlow flow;
    public int position;

    public void setListener(SinglePaymentDialogListener listener){
        this.listener = listener;
    }

    public double getAmount() {
        flow.setFlowMag(Double.parseDouble(txtAmount.getText().toString()));
        return flow.getFlow();
    }

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
    }

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
        View dialog = inflater.inflate(R.layout.dialog_single_payment,null);
        txtName = dialog.findViewById(R.id.txtNameSing);
        txtDate = dialog.findViewById(R.id.txtDateSing);
        txtAmount = dialog.findViewById(R.id.txtAmountSing);
        txtNotes = dialog.findViewById(R.id.txtNotesSing);
        spnBanks = dialog.findViewById(R.id.spnBankIdSing);
        spnCashFlow = dialog.findViewById(R.id.spnBeingPaidSing);

        try{t.join();}
        catch (InterruptedException e) {e.printStackTrace();}
        List<String> lBanks = new ArrayList<>(mBanks.values());
        List<String> lFlow = CashFlow.getChoices();

        if(lBanks.size() < 1) {
            return new AlertDialog.Builder(getContext())
                    .setTitle("A Bank Account is REQUIRED to add payments")
                    .setPositiveButton("Ok", (dialogInterface, i) -> {})
                    .create();
        }

        ArrayAdapter<String> bankAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_dropdown_item_1line,
                lBanks);
        ArrayAdapter<String> flowAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_dropdown_item_1line,
                lFlow);
        spnBanks.setAdapter(bankAdapter);
        spnCashFlow.setAdapter(flowAdapter);
        spnBanks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                int i=0;
                for(long l: mBanks.keySet()) {
                    if(i == position) bankId = l;
                    i++;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {bankId = -1; }
        });
        spnCashFlow.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) { flow.setCashFlow(position); }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        assert getArguments() != null;
        txtName.setText(getArguments().getString(NAME_KEY));
        txtNotes.setText(getArguments().getString(NOTES_KEY));
        txtAmount.setText(String.valueOf(getArguments().getDouble(AMOUNT_KEY)));
        txtDate.setText(getArguments().getString(DATE_KEY));
        bankId = getArguments().getLong(BANK_ID_KEY);
        editId = getArguments().getLong(EDIT_ID_KEY);
        pId = getArguments().getLong(PAYMENT_ID_KEY);
        isNew = getArguments().getBoolean(IS_NEW_KEY);
        position = getArguments().getInt(POSITION_KEY);
        if((isNew && pId != -1 && editId == -1) || (!isNew && pId != -1 && editId != -1)){
            isEdit = true;
        }
        else if((isNew && pId == -1 && editId == -1) || (!isNew && pId != -1)){
            isEdit = false;
        }

        int n = 0;
        for(long id: mBanks.keySet()) {
            if(id == bankId) spnBanks.setSelection(n);
            n++;
        }

        return new AlertDialog.Builder(getActivity())
                .setTitle("Payment")
                .setView(dialog)
                .setPositiveButton("Set", ((dialogInterface, i) -> listener.onDialogPositiveClick(SinglePaymentDialog.this)))
                .setNegativeButton("Cancel", ((dialogInterface, i) -> listener.onDialogNegativeClick(SinglePaymentDialog.this)))
                .create();
    }

    
    public static SinglePaymentDialog newInstance(Payment p,long editId, long pId, boolean isNew, int position){
        SinglePaymentDialog f = new SinglePaymentDialog();
        Bundle args = new Bundle();

        if(p != null) {
            args.putString(NAME_KEY, p.name);
            args.putDouble(AMOUNT_KEY, p.amount);
            args.putString(DATE_KEY, DateParser.getString(p.date));
            args.putLong(ID_KEY, p.id);
            args.putString(NOTES_KEY, p.notes);
            args.putLong(BANK_ID_KEY, p.bankId);
        }
        else {
            args.putString(NAME_KEY, "");
            args.putDouble(AMOUNT_KEY, 0);
            args.putString(DATE_KEY, DateParser.getToday());
            args.putLong(ID_KEY, -1);
            args.putString(NOTES_KEY, "");
            args.putLong(BANK_ID_KEY, -1);
            args.putLong(PAYMENT_ID_KEY, -1);
        }

        args.putInt(POSITION_KEY, position);
        args.putLong(PAYMENT_ID_KEY, pId);
        args.putLong(EDIT_ID_KEY, editId);
        args.putBoolean(IS_NEW_KEY, isNew);
        f.setArguments(args);

        return f;
    }
}
