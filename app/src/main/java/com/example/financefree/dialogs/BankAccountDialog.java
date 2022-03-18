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
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.financefree.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class BankAccountDialog extends DialogFragment {

    // public static AppDatabase db;


    public interface BankAccountDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    private BankAccountDialogListener listener;
    public long bankId;
    // public BankAccount ba;

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        try {
            listener = (BankAccountDialogListener) context;
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

       // db = AppDatabase.getAppDatabase(this.getContext());

        // TODO: Update all of this after layout is done
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View dialog = inflater.inflate(R.layout.dialog_single_payment,null);
        /*
        if(bankId >= 0) {
            // means there is a legitimate bank account to find, populate with
            db.bankAccountDao().getById(bankId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe( bankAccount -> {
                        ba = bankAccount;
                    },
                    throwable -> {
                        System.out.println("BankAccountDialog: " + throwable.getMessage());
                    });
        }
        else {
            // means this is a create new situation
            ba = new BankAccount();
        }

         */

        builder.setView(dialog)
                .setPositiveButton(R.string.set, (dialogInterface, i) -> listener.onDialogPositiveClick(BankAccountDialog.this))
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> listener.onDialogNegativeClick(BankAccountDialog.this));

        return builder.create();
    }


    // TODO: Change all this around
    @NonNull
    public static BankAccountDialog newInstance(long bankId){
        BankAccountDialog f = new BankAccountDialog();
        f.bankId = bankId;
        return f;
    }
}
