package com.example.financefree.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.financefree.R;
import com.example.financefree.database.DatabaseManager;
import com.example.financefree.structures.DateParser;
import com.example.financefree.structures.Payment;
import com.example.financefree.structures.Statement;

import java.text.NumberFormat;
import java.util.concurrent.atomic.AtomicReference;

public class DetailsDialogPa extends DialogFragment {
    private static final String PAYMENT_NAME_KEY = "name";
    private static final String NOTES_KEY = "notes";
    private static final String BANK_NAME_KEY = "bank_name";
    private static final String AMOUNT_KEY = "amount";
    private static final String DATE_KEY = "date";

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
        View dialog = inflater.inflate(R.layout.dialog_payment_details, null);

        assert getArguments() != null;
        TextView tvName = dialog.findViewById(R.id.txtNamePaDet);
        TextView tvBankName = dialog.findViewById(R.id.txtBankNamePaDet);
        TextView tvNotes = dialog.findViewById(R.id.txtNotesPaDet);
        TextView tvAmount = dialog.findViewById(R.id.txtAmountPaDet);
        TextView tvDate = dialog.findViewById(R.id.txtDatePaDet);

        tvName.setText(getArguments().getString(PAYMENT_NAME_KEY));
        tvBankName.setText(getArguments().getString(BANK_NAME_KEY));
        tvNotes.setText(getArguments().getString(NOTES_KEY));
        tvAmount.setText(getArguments().getString(AMOUNT_KEY));
        tvDate.setText(getArguments().getString(DATE_KEY));

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.payment)
                .setPositiveButton("Ok", (dialogInterface, i) -> {})
                .setView(dialog)
                .create();
    }

    @NonNull
    public static DetailsDialogPa newInstance(@NonNull Payment pa){
        DetailsDialogPa f = new DetailsDialogPa();
        Bundle args = new Bundle();
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        AtomicReference<String> paNotes = new AtomicReference<>();
        AtomicReference<String> paBankName = new AtomicReference<>();
        Thread t;

        if(pa.cType == 'r') {
            t = new Thread(() -> {
                paBankName.set(DatabaseManager.getBankAccountDao().getBankAccount(pa.bankId).name);
                paNotes.set(DatabaseManager.getRecurringPaymentDao().getRecurringPayment(pa.id).notes);
            });
        }
        else {
            t = new Thread(() -> {
                paBankName.set(DatabaseManager.getBankAccountDao().getBankAccount(pa.bankId).name);
                paNotes.set(DatabaseManager.getSinglePaymentDao().getPayment(pa.id).notes);
            });
        }
        t.start();

        args.putString(PAYMENT_NAME_KEY, pa.name);
        args.putString(DATE_KEY, DateParser.getString(pa.date));
        args.putString(AMOUNT_KEY, nf.format(pa.amount));

        try{ t.join();}
        catch (InterruptedException e){e.printStackTrace();}

        args.putString(NOTES_KEY, paNotes.get());
        args.putString(BANK_NAME_KEY, paBankName.get());
        f.setArguments(args);
        return f;
    }
}
