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
import com.example.financefree.database.entities.RecurringPayment;
import com.example.financefree.structures.DateParser;
import com.example.financefree.structures.Frequency;
import com.example.financefree.structures.Statement;

import java.text.NumberFormat;
import java.util.concurrent.atomic.AtomicReference;

public class DetailsDialogSt extends DialogFragment {
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
        View dialog = inflater.inflate(R.layout.dialog_bank_statement_details, null);

        assert getArguments() != null;
        TextView tvName = dialog.findViewById(R.id.txtNameBSDet);
        TextView tvNotes = dialog.findViewById(R.id.txtNotesBSDet);
        TextView tvAmount = dialog.findViewById(R.id.txtAmountBSDet);
        TextView tvDate = dialog.findViewById(R.id.txtDateBSDet);

        tvName.setText(getArguments().getString(BANK_NAME_KEY));
        tvNotes.setText(getArguments().getString(NOTES_KEY));
        tvAmount.setText(getArguments().getString(AMOUNT_KEY));
        tvDate.setText(getArguments().getString(DATE_KEY));

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.statement)
                .setPositiveButton("Ok", (dialogInterface, i) -> {})
                .setView(dialog)
                .create();
    }

    @NonNull
    public static DetailsDialogSt newInstance(@NonNull Statement st){
        DetailsDialogSt f = new DetailsDialogSt();
        Bundle args = new Bundle();
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        AtomicReference<String> baNotes = new AtomicReference<>();
        Thread t = new Thread(() -> baNotes.set(DatabaseManager.getBankAccountDao().getBankAccount(st.bankId).notes));
        t.start();

        args.putString(BANK_NAME_KEY, st.bankName);
        args.putString(DATE_KEY, DateParser.getString(st.date));
        args.putString(AMOUNT_KEY, nf.format(st.amount));

        try{ t.join();}
        catch (InterruptedException e){e.printStackTrace();}

        args.putString(NOTES_KEY, baNotes.get());
        f.setArguments(args);
        return f;
    }
}
