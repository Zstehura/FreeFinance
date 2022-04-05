package com.example.financefree;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.financefree.database.DatabaseManager;
import com.example.financefree.database.entities.BankAccount;
import com.example.financefree.dialogs.BankAccountDialog;
import com.example.financefree.recyclers.BankAccountRVContent;
import com.example.financefree.recyclers.BankAccountRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class BankAccountFragment extends Fragment implements BankAccountDialog.BankAccountDialogListener {

    List<BankAccount> rvItemList;
    RecyclerView rv;
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
        final List<BankAccount> list = new ArrayList<>();
        Thread t = new Thread(() -> list.addAll(DatabaseManager.getBankAccountDao().getAll()));
        t.start();
        View view = inflater.inflate(R.layout.fragment_bank_account_list, container, false);

        rv = view.findViewById(R.id.recBankAccts);
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        barva = new BankAccountRecyclerViewAdapter(list);
        rv.setAdapter(barva);

        view.findViewById(R.id.btnAddBank).setOnClickListener(btn -> {
            BankAccountDialog f = BankAccountDialog.newInstance(null, 0, barva.getItemCount());
            Log.d("BAFrag", "Dialog created");
            f.show(this.getChildFragmentManager(), "add_bank");
        });

        return view;
    }

    @Override
    public void onBADialogPositiveClick(BankAccountDialog dialog) {
        Log.d("BAFrag", "Dialog Positive Click");
        if(dialog != null) {
            BankAccount ba = new BankAccount();
            Thread t;
            if(dialog.isNew) t = new Thread(() -> DatabaseManager.getBankAccountDao().insertAll(ba));
            else t = new Thread(() -> DatabaseManager.getBankAccountDao().update(ba));
            ba.notes = ((BankAccountDialog) dialog).bnkNotes.getText().toString();
            ba.name = ((BankAccountDialog) dialog).bnkName.getText().toString();
            ba.bank_id = ((BankAccountDialog) dialog).bankId;
            t.start();

            if(dialog.isNew) barva.notifyItemRangeInserted(dialog.position, 1);
            else barva.notifyItemChanged(dialog.position);
            BankAccountRVContent.addItem(ba);

            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBADialogNegativeClick(BankAccountDialog dialog) {}
}