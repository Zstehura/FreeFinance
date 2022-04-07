package com.example.financefree.recyclers;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.financefree.database.DatabaseManager;
import com.example.financefree.database.entities.BankAccount;
import com.example.financefree.dialogs.BankAccountDialog;
import com.example.financefree.recyclers.BankAccountRVContent.BankAccountRVItem;
import com.example.financefree.databinding.FragmentBankAccountBinding;

import java.lang.ref.WeakReference;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;

/**
 * {@link RecyclerView.Adapter} that can display a {@link BankAccountRVItem}.
 */
public class BankAccountRecyclerViewAdapter extends RecyclerView.Adapter<BankAccountRecyclerViewAdapter.ViewHolder> {
    public List<BankAccountRVItem> mValues;
    private final ViewHolder.BARClickListener listener;

    public BankAccountRecyclerViewAdapter(List<BankAccount> list, ViewHolder.BARClickListener listener) {
        Log.d("BADialog", "Initializing");
        this.listener = listener;
        mValues = BankAccountRVContent.getItems(list);
        Log.d("BADialog", "List Initialized");
    }

    @androidx.annotation.NonNull
    @Override
    public ViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentBankAccountBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), listener);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mName.setText(String.valueOf(mValues.get(position).name));
        holder.mDetails.setText(mValues.get(position).details);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setItem(BankAccountDialog dialog) {
        if(dialog.isNew) {
            long[] id = new long[1];
            BankAccount ba = new BankAccount();
            ba.name = dialog.bnkName.getText().toString();
            ba.notes = dialog.bnkNotes.getText().toString();
            Thread t = new Thread(() -> {
                DatabaseManager.getBankAccountDao().insert(ba);
                id[0] = DatabaseManager.getBankAccountDao().getLast().bank_id;
            });
            t.start();
            try {t.join();}
            catch (InterruptedException e) {e.printStackTrace();}
            ba.bank_id = id[0];
            BankAccountRVContent.addItem(ba);
            mValues = BankAccountRVContent.getItems();
            this.notifyItemInserted(dialog.position);
        }
        else {
            BankAccount ba = new BankAccount();
            ba.name = dialog.bnkName.getText().toString();
            ba.notes = dialog.bnkNotes.getText().toString();
            ba.bank_id = dialog.bankId;
            Thread t = new Thread(() -> DatabaseManager.getBankAccountDao().update(ba));
            t.start();
            BankAccountRVContent.updateItem(ba);
            this.notifyItemChanged(dialog.position);
            try {t.join();}
            catch (InterruptedException e) {e.printStackTrace();}
        }
    }
    @Override
    public long getItemId(int position) {
        return mValues.get(position).itemId;
    }

    public void remove(int position) {
        mValues.remove(position);
        this.notifyItemRemoved(position);
    }

    public String getItemName(int position) {
        return mValues.get(position).name;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final WeakReference<BARClickListener> listenerRef;
        public final TextView mName, mDetails;
        public final ImageButton btnDel, btnEdit;
        public BankAccountRVItem mItem;

        public ViewHolder(FragmentBankAccountBinding binding, BARClickListener listener) {
            super(binding.getRoot());
            mName = binding.lblName;
            mDetails = binding.lblDesc;
            btnDel = binding.btnDelBank;
            btnEdit = binding.btnEditBank;
            listenerRef = new WeakReference<>(listener);

            btnEdit.setOnClickListener(this);
            btnDel.setOnClickListener(this);
            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view.getId() == btnDel.getId()) {
                listenerRef.get().OnBADeleteClick(getBindingAdapterPosition());
            }
            else if(view.getId() == btnEdit.getId()) {
                listenerRef.get().OnBAEditClick(getBindingAdapterPosition());
            }
            else {
                listenerRef.get().OnBARowClick(getBindingAdapterPosition());
            }
        }

        public interface BARClickListener {
            void OnBARowClick(int position);
            void OnBADeleteClick(int position);
            void OnBAEditClick(int position);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mDetails.getText() + "'";
        }
    }
}