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

public class DetailsDialogRP extends DialogFragment {
    private static final String NAME_KEY = "name";
    private static final String NOTES_KEY = "notes";
    private static final String BANK_KEY = "bank";
    private static final String AMOUNT_KEY = "amount";
    private static final String START_DATE_KEY = "start";
    private static final String END_DATE_KEY = "end";
    private static final String FREQUENCY_KEY = "freq";

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
        View dialog = inflater.inflate(R.layout.dialog_recurring_payment_details, null);

        assert getArguments() != null;
        TextView tvName = dialog.findViewById(R.id.txtNameRPDet);
        TextView tvNotes = dialog.findViewById(R.id.txtNotesRPDet);
        TextView tvBank = dialog.findViewById(R.id.txtBankAccRPDet);
        TextView tvAmount = dialog.findViewById(R.id.txtAmountRPDet);
        TextView tvStart = dialog.findViewById(R.id.txtStartRPDet);
        TextView tvEnd = dialog.findViewById(R.id.txtEndRPDet);
        TextView tvFreq = dialog.findViewById(R.id.txtFreqRPDet);

        tvName.setText(getArguments().getString(NAME_KEY));
        tvNotes.setText(getArguments().getString(NOTES_KEY));
        tvBank.setText(getArguments().getString(BANK_KEY));
        tvAmount.setText(getArguments().getString(AMOUNT_KEY));
        tvStart.setText(getArguments().getString(START_DATE_KEY));
        tvEnd.setText(getArguments().getString(END_DATE_KEY));
        tvFreq.setText(getArguments().getString(FREQUENCY_KEY));

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.bank_account)
                .setPositiveButton(R.string.ok, (dialogInterface, i) -> {})
                .setView(dialog)
                .create();
    }

    @NonNull
    public static DetailsDialogRP newInstance(@NonNull RecurringPayment rp){
        DetailsDialogRP f = new DetailsDialogRP();
        Bundle args = new Bundle();
        String[] baName = new String[1];
        Thread t = new Thread(() -> baName[0] = DatabaseManager.getBankAccountDao().getBankAccount(rp.bank_id).name);
        t.start();

        args.putString(NAME_KEY, rp.name);
        args.putString(NOTES_KEY, rp.notes);
        args.putString(AMOUNT_KEY, String.valueOf(rp.amount));
        args.putString(START_DATE_KEY, DateParser.getString(rp.start_date));
        args.putString(END_DATE_KEY, DateParser.getString(rp.end_date));
        args.putString(FREQUENCY_KEY, (new Frequency(rp).toString()));

        try{ t.join();}
        catch (InterruptedException e){e.printStackTrace();}

        args.putString(BANK_KEY, baName[0]);
        f.setArguments(args);
        return f;
    }
}
