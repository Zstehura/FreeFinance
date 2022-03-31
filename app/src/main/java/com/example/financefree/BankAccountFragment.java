package com.example.financefree;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.financefree.database.entities.BankAccount;
import com.example.financefree.dialogs.BankAccountDialog;

import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class BankAccountFragment extends Fragment implements BankAccountDialog.BankAccountDialogListener {

    List<BankAccount> rvItemList;
    BankAccountRecyclerViewAdapter barva;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BankAccountFragment() {
    }

    @SuppressWarnings("unused")
    public static BankAccountFragment newInstance(int columnCount) {
        return new BankAccountFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bank_account_list, container, false);

        RecyclerView rv = view.findViewById(R.id.recBankAccts);
        barva = new BankAccountRecyclerViewAdapter(rvItemList);
        rv.setAdapter(barva);

       // Disposable d = MainActivity.dm.daoBankAccount.getAll().subscribeOn(Schedulers.io())
       //         .observeOn(AndroidSchedulers.mainThread())
       //         .subscribe(list -> rvItemList = list,
       //                 throwable -> Log.e("BankAccountFragment", "ERROR: " + throwable.getMessage()));
       // d.dispose();

        view.findViewById(R.id.btnAddBank).setOnClickListener(btn -> {
            BankAccountDialog f = new BankAccountDialog();
            f.show(this.getChildFragmentManager(), "add_bank");
        });

        return view;
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        if(dialog instanceof BankAccountDialog) {
            BankAccount eba = new BankAccount();
            eba.notes = ((BankAccountDialog) dialog).bnkNotes.getText().toString();
            eba.name = ((BankAccountDialog) dialog).bnkName.getText().toString();
            eba.bank_id = ((BankAccountDialog) dialog).bankId;
            // Disposable d = MainActivity.dm.daoBankAccount.insertAll(eba)
            //         .subscribeOn(Schedulers.io())
            //         .observeOn(AndroidSchedulers.mainThread())
            //         .subscribe(() -> Log.d("MainActivity", "Complete: Added " + eba.bankName),
            //                 throwable -> Log.e("MainAcivity", "Error: " + throwable.getMessage()));
            // d.dispose();

        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }
}