package com.example.financefree.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.financefree.R;
import com.example.financefree.database.entities.BankAccount;

public class BankAccountDialog extends DialogFragment {
    private static final String NAME_KEY = "name";
    private static final String ID_KEY = "id";
    private static final String NOTES_KEY = "notes";
    private static final String NEW_KEY = "isnew";
    private static final String POSITION_KEY = "pos";

    public interface BankAccountDialogListener {
        void onBADialogPositiveClick(BankAccountDialog dialog);
        void onBADialogNegativeClick(BankAccountDialog dialog);
    }

    private BankAccountDialogListener listener;

    public boolean isNew;
    public int position;
    public long bankId;
    public EditText bnkName;
    public EditText bnkNotes;

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        try {
            listener = (BankAccountDialogListener) context;
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setListener(BankAccountDialogListener listener) {
        this.listener = listener;
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
        View dialog = inflater.inflate(R.layout.dialog_bank_account, null);

        bnkName = dialog.findViewById(R.id.txtNameBankAcc);
        bnkNotes = dialog.findViewById(R.id.txtNotesBankAcc);
        assert getArguments() != null;
        position = getArguments().getInt(POSITION_KEY);
        isNew = getArguments().getBoolean(NEW_KEY);
        bankId = getArguments().getLong(ID_KEY);
        bnkName.setText(getArguments().getString(NAME_KEY, ""));
        bnkNotes.setText(getArguments().getString(NOTES_KEY, ""));

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.bank_account)
                .setView(dialog)
                .setPositiveButton(R.string.set, (dialogInterface, i) -> listener.onBADialogPositiveClick(BankAccountDialog.this))
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> listener.onBADialogNegativeClick(BankAccountDialog.this))
                .create();
    }

    @NonNull
    public static BankAccountDialog newInstance(@Nullable BankAccount ba, long id, int position){
        BankAccountDialog f = new BankAccountDialog();
        Bundle args = new Bundle();

        if(ba != null) {
            args.putString(NAME_KEY, ba.name);
            args.putString(NOTES_KEY, ba.notes);
            args.putBoolean(NEW_KEY, false);
        }
        else {
            args.putString(NAME_KEY, "");
            args.putString(NOTES_KEY, "");
            args.putBoolean(NEW_KEY, true);
        }

        args.putInt(POSITION_KEY, position);
        args.putLong(ID_KEY, id);
        f.setArguments(args);
        return f;
    }
}
