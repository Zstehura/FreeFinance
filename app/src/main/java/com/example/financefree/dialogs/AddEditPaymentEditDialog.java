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
import com.example.financefree.databaseClasses.PaymentEdit;
import com.example.financefree.databaseClasses.SinglePayment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddEditPaymentEditDialog extends DialogFragment {
    public static final String MOVE_TO_KEY = "move_to_date";
    public static final String ACTION_KEY = "action";
    public static final String AMOUNT_KEY = "amount";
    public static final String DATE_KEY = "date";
    public static final String ID_KEY = "id";
    public static final String NEW_AMOUNT = "new_amount";

    public interface PaymentEditDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    private PaymentEditDialogListener listener;

    List<String> bankNames;
    List<Long> bankIds;
    String name;
    long date, bankId, spId;
    double amount;

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        try {
            listener = (PaymentEditDialogListener) context;
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getArguments();
        if(extras != null){
            // TODO: Replace these
            amount = extras.getDouble(AMOUNT_KEY);
            date = extras.getLong(DATE_KEY);
            spId = extras.getLong(ID_KEY);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: Update all of this after layout is done
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View dialog = inflater.inflate(R.layout.add_edit_singlepayment_dialog,null);
        Spinner spnBank = (Spinner) dialog.findViewById(R.id.spnBankIdSing);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_dropdown_item_1line, bankNames);
        spnBank.setAdapter(adapter);
        spnBank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                bankId = bankIds.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                bankId = 0;
            }
        });

        builder.setView(dialog)
                .setPositiveButton(R.string.set, (dialogInterface, i) -> listener.onDialogPositiveClick(AddEditPaymentEditDialog.this))
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> listener.onDialogNegativeClick(AddEditPaymentEditDialog.this));

        return builder.create();
    }

    @NonNull
    public static AddEditPaymentEditDialog newInstance(@NonNull PaymentEdit paymentEdit, @NonNull Map<Long, String> banks){
        AddEditPaymentEditDialog f = new AddEditPaymentEditDialog();

        Bundle args = new Bundle();
        // TODO: Replace these
        // args.putString(NAME_KEY, paymentEdit.name);
        // args.putDouble(AMOUNT_KEY, paymentEdit.amount);
        // args.putLong(DATE_KEY, paymentEdit.date);
        // args.putLong(ID_KEY, paymentEdit.sp_id);
        // args.putStringArrayList(BANK_NAMES_KEY, new ArrayList<>(banks.values()));
        // args.putParcelable(BANK_IDS_KEY, (Parcelable) banks.keySet());
        f.setArguments(args);

        return f;
    }
}
