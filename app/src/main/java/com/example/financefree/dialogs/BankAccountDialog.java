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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.financefree.R;
import com.example.financefree.fileClasses.BankAccount;
import com.example.financefree.fileClasses.DataManager;
import com.example.financefree.fileClasses.SinglePayment;
import com.example.financefree.structures.IdMgr;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BankAccountDialog extends DialogFragment {
    private static final String NAME_KEY = "name";
    private static final String ID_KEY = "id";
    private static final String NOTES_KEY = "notes";

    public interface BankAccountDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    private BankAccountDialogListener listener;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialog = inflater.inflate(R.layout.dialog_bank_account, null);
        builder.setView(dialog)
                .setPositiveButton(R.string.set, (dialogInterface, i) -> listener.onDialogPositiveClick(BankAccountDialog.this))
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> listener.onDialogNegativeClick(BankAccountDialog.this));
        //bnkName = dialog.findViewById(R.id.txtNameBankAcc);
        //bnkNotes = dialog.findViewById(R.id.txtNotesBankAcc);
        //assert savedInstanceState != null;
        //bnkName.setText(savedInstanceState.getString(NAME_KEY, ""));
        //bnkNotes.setText(savedInstanceState.getString(NOTES_KEY, ""));

        return builder.create();
    }

    @NonNull
    public static BankAccountDialog newInstance(@Nullable BankAccount ba, long id){
        BankAccountDialog f = new BankAccountDialog();
        Bundle args = new Bundle();
        if(ba != null) {
            args.putString(NAME_KEY, ba.name);
            args.putString(NOTES_KEY, ba.notes);
        }
        else {
            args.putString(NAME_KEY, "");
            args.putString(NOTES_KEY, "");
        }

        args.putLong(ID_KEY, id);
        f.setArguments(args);
        return f;
    }
}
