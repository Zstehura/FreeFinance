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
import com.example.financefree.database.entities.RecurringPayment;
import com.example.financefree.dialogs.BankAccountDialog;
import com.example.financefree.dialogs.DetailsDialogRP;
import com.example.financefree.dialogs.RecurringPaymentDialog;
import com.example.financefree.recyclers.RecurringPaymentRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class RecurringPaymentFragment extends Fragment implements RecurringPaymentDialog.RecurringPaymentDialogListener {

    RecyclerView rv;
    RecurringPaymentRecyclerViewAdapter rprva;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecurringPaymentFragment() {}

    @SuppressWarnings("unused")
    public static RecurringPaymentFragment newInstance() {return new RecurringPaymentFragment();}

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final List<RecurringPayment> list = new ArrayList<>();
        Thread t = new Thread(() -> list.addAll(DatabaseManager.getRecurringPaymentDao().getAll()));
        t.start();
        View view = inflater.inflate(R.layout.fragment_recurring_payment_list, container, false);

        rv = view.findViewById(R.id.recRecurPaymnts);
        try {t.join();}
        catch (InterruptedException e) {e.printStackTrace();}
        rprva = new RecurringPaymentRecyclerViewAdapter(list, new RecurringPaymentRecyclerViewAdapter.ViewHolder.RPRVClickListener() {
            @Override
            public void OnDeleteClicked(int position) {
                AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                        .setTitle("Are you sure you'd like to delete " + rprva.getItemName(position) + "?")
                        .setPositiveButton("Delete", (dialogInterface, i) -> {
                            Thread t = new Thread(() -> DatabaseManager.getRecurringPaymentDao().deleteById(rprva.getItemId(position)));
                            t.start();
                            try {t.join();}
                            catch (InterruptedException e) {e.printStackTrace();}
                            rprva.remove(position);
                        })
                        .setNegativeButton("Cancel", (dialogInterface, i) -> {})
                        .create();
                alertDialog.show();
            }

            
            @Override
            public void OnEditClicked(int position) {
                long id = rprva.getItemId(position);
                final RecurringPayment[] rp = new RecurringPayment[1];
                Thread t = new Thread(() -> rp[0] = DatabaseManager.getRecurringPaymentDao().getRecurringPayment(id));
                t.start();
                try {t.join();}
                catch (InterruptedException e) {e.printStackTrace();}
                RecurringPaymentDialog recurringPaymentDialog = RecurringPaymentDialog.newInstance(rp[0], position);
                recurringPaymentDialog.setListener(RecurringPaymentFragment.this);
                recurringPaymentDialog.show(getParentFragmentManager(),"RecurringPaymentEdit");
            }

            @Override
            public void OnItemClicked(int position) {
                final RecurringPayment[] rp = new RecurringPayment[1];
                Thread t = new Thread(() -> rp[0] = DatabaseManager.getRecurringPaymentDao().getRecurringPayment(rprva.getItemId(position)));
                t.start();
                try {t.join();}
                catch (InterruptedException e) {e.printStackTrace();}
                DetailsDialogRP detailsDialogRP = DetailsDialogRP.newInstance(rp[0]);
                detailsDialogRP.show(getChildFragmentManager(), "RecurPaymentDetails");
            }
        });
        rv.setAdapter(rprva);

        view.findViewById(R.id.btnAddRecur).setOnClickListener(btn -> {
            RecurringPaymentDialog f = RecurringPaymentDialog.newInstance(null, rprva.getItemCount());
            Log.d("BAFrag", "Dialog created");
            f.setListener(this);
            f.show(getParentFragmentManager(), "addRecurringPayment");
        });

        return view;
    }

    
    @Override
    public void onDialogPositiveClick(RecurringPaymentDialog dialog) {
        Log.d("RPFrag", "Dialog Positive Click");
        if(dialog != null) {
            rprva.setItem(dialog);
        }
    }

    @Override
    public void onDialogNegativeClick(RecurringPaymentDialog dialog) {    }
}