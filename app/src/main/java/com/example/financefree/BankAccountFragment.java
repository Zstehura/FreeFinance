package com.example.financefree;

import android.app.AlertDialog;
import android.os.Bundle;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.financefree.database.DatabaseManager;
import com.example.financefree.database.entities.BankAccount;
import com.example.financefree.dialogs.DetailsDialogBA;
import com.example.financefree.dialogs.BankAccountDialog;
import com.example.financefree.recyclers.BankAccountRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class BankAccountFragment extends Fragment implements BankAccountDialog.BankAccountDialogListener {

    RecyclerView rv;
    BankAccountRecyclerViewAdapter barva;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BankAccountFragment() {}

    public static BankAccountFragment newInstance() {return new BankAccountFragment();}

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

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
        barva = new BankAccountRecyclerViewAdapter(list, new BankAccountRecyclerViewAdapter.ViewHolder.BARClickListener() {
            @Override
            public void OnBARowClick(int position) {
                long id = barva.getItemId(position);
                final BankAccount[] ba = new BankAccount[1];
                Thread t = new Thread(() -> ba[0] = DatabaseManager.getBankAccountDao().getBankAccount(barva.getItemId(position)));
                t.start();
                try {t.join();}
                catch (InterruptedException e) {e.printStackTrace();}
                DetailsDialogBA detailsDialogBA = DetailsDialogBA.newInstance(ba[0]);
                detailsDialogBA.show(getChildFragmentManager(), "BankAccountDetails");
            }

            @Override
            public void OnBADeleteClick(int position) {
                AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                        .setTitle("Are you sure you'd like to delete " + barva.getItemName(position) + "?")
                        .setPositiveButton("Delete", (dialogInterface, i) -> {
                            Thread t = new Thread(() -> DatabaseManager.getBankAccountDao().deleteById(barva.getItemId(position)));
                            t.start();
                            try {t.join();}
                            catch (InterruptedException e) {e.printStackTrace();}
                            barva.remove(position);
                        })
                        .setNegativeButton("Cancel", (dialogInterface, i) -> {})
                        .create();
                alertDialog.show();
            }

            @Override
            public void OnBAEditClick(int position) {
                long id = barva.getItemId(position);
                final BankAccount[] ba = new BankAccount[1];
                Thread t = new Thread(() -> ba[0] = DatabaseManager.getBankAccountDao().getBankAccount(id));
                t.start();
                try {t.join();}
                catch (InterruptedException e) {e.printStackTrace();}
                BankAccountDialog bankAccountDialog = BankAccountDialog.newInstance(ba[0],id, position);
                bankAccountDialog.setListener(BankAccountFragment.this);
                bankAccountDialog.show(getParentFragmentManager(),"BankAccountEdit");
            }
        });
        rv.setAdapter(barva);

        view.findViewById(R.id.btnAddBank).setOnClickListener(btn -> {
            BankAccountDialog f = BankAccountDialog.newInstance(null, 0, barva.getItemCount());
            Log.d("BAFrag", "Dialog created");
            f.setListener(this);
            f.show(getParentFragmentManager(), "add_bank");
        });

        return view;
    }

    
    @Override
    public void onBADialogPositiveClick(BankAccountDialog dialog) {
        Log.d("BAFrag", "Dialog Positive Click");
        if(dialog != null) {
            barva.setItem(dialog);
        }
    }

    @Override
    public void onBADialogNegativeClick(BankAccountDialog dialog) {}
}