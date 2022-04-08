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
import com.example.financefree.database.entities.BankAccount;

public class DetailsDialogBA extends DialogFragment {
    private static final String NAME_KEY = "name";
    private static final String NOTES_KEY = "notes";

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
        View dialog = inflater.inflate(R.layout.dialog_bank_account_details, null);

        assert getArguments() != null;
        TextView tvName = dialog.findViewById(R.id.txtNameBADet);
        TextView tvNotes = dialog.findViewById(R.id.txtNotesBADet);
        tvName.setText(getArguments().getString(NAME_KEY));
        tvNotes.setText(getArguments().getString(NOTES_KEY));

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.bank_account)
                .setPositiveButton("Ok", (dialogInterface, i) -> {})
                .setView(dialog)
                .create();
    }

    @NonNull
    public static DetailsDialogBA newInstance(@NonNull BankAccount ba){
        DetailsDialogBA f = new DetailsDialogBA();
        Bundle args = new Bundle();
        args.putString(NAME_KEY, ba.name);
        args.putString(NOTES_KEY, ba.notes);
        f.setArguments(args);
        return f;
    }
}
