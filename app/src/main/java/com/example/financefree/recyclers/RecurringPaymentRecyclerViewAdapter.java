package com.example.financefree.recyclers;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.financefree.database.DatabaseManager;
import com.example.financefree.database.entities.RecurringPayment;
import com.example.financefree.dialogs.RecurringPaymentDialog;
import com.example.financefree.recyclers.RecurringPaymentRVContent.RecurringPaymentRVItem;
import com.example.financefree.databinding.FragmentRecurringPaymentBinding;
import com.example.financefree.structures.DateParser;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * {@link RecyclerView.Adapter} that can display a {@link RecurringPaymentRVItem}.
 */
public class RecurringPaymentRecyclerViewAdapter extends RecyclerView.Adapter<RecurringPaymentRecyclerViewAdapter.ViewHolder> {
    public List<RecurringPaymentRVItem> mValues;
    private final ViewHolder.RPRVClickListener listener;

    public RecurringPaymentRecyclerViewAdapter(List<RecurringPayment> items, ViewHolder.RPRVClickListener listener) {
        this.listener = listener;
        mValues = RecurringPaymentRVContent.getItems(items);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentRecurringPaymentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), listener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mDesc.setText(mValues.get(position).details);
        holder.mName.setText(mValues.get(position).name);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public long getItemId(int position) {return mValues.get(position).id;}

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setItem(RecurringPaymentDialog dialog) {
        RecurringPayment rp = new RecurringPayment();
        rp.name = dialog.txtName.getText().toString();
        rp.notes = dialog.txtNotes.getText().toString();
        rp.amount = Double.parseDouble(dialog.txtAmount.getText().toString());
        rp.end_date = DateParser.getLong(dialog.txtEnd.getText().toString());
        rp.start_date = DateParser.getLong(dialog.txtStart.getText().toString());
        rp.bank_id = dialog.bankId;
        rp.type_option = dialog.freqType;
        rp.frequency_number = dialog.freqNum;

        if(dialog.isNew) {
            AtomicReference<Long> id = new AtomicReference<>();

            Thread t = new Thread(() -> {
                id.set(DatabaseManager.getRecurringPaymentDao().insert(rp));
            });
            t.start();
            try {t.join();}
            catch (InterruptedException e) {e.printStackTrace();}
            rp.rp_id = id.get();
            RecurringPaymentRVContent.addItem(rp);
            mValues = RecurringPaymentRVContent.getItems();
            this.notifyItemInserted(dialog.position);
        }
        else {
            rp.rp_id = dialog.rpId;
            Thread t = new Thread(() -> DatabaseManager.getRecurringPaymentDao().update(rp));
            t.start();
            RecurringPaymentRVContent.updateItem(rp);
            this.notifyItemChanged(dialog.position);
            try {t.join();}
            catch (InterruptedException e) {e.printStackTrace();}
        }
    }

    public String getItemName(int position) {
        return mValues.get(position).name;
    }

    public void remove(int position) {
        mValues.remove(position);
        this.notifyItemRemoved(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final WeakReference<RPRVClickListener> listenerRef;
        public TextView mName, mDesc;
        public ImageButton btnEdit, btnDel;
        public RecurringPaymentRVItem mItem;

        public ViewHolder(FragmentRecurringPaymentBinding binding, RPRVClickListener listener) {
            super(binding.getRoot());
            mName = binding.lblName;
            mDesc = binding.lblDesc;
            btnEdit = binding.btnEditRP;
            btnDel = binding.btnDelRP;
            listenerRef = new WeakReference<>(listener);

            btnEdit.setOnClickListener(this);
            btnDel.setOnClickListener(this);
            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view.getId() == btnDel.getId()){
                listenerRef.get().OnDeleteClicked(getBindingAdapterPosition());
            }
            else if(view.getId() == btnEdit.getId()) {
                listenerRef.get().OnEditClicked(getBindingAdapterPosition());
            }
            else {
                listenerRef.get().OnItemClicked(getBindingAdapterPosition());
            }
        }

        public interface RPRVClickListener {
            void OnDeleteClicked(int position);
            void OnEditClicked(int position);
            void OnItemClicked(int position);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mName.getText() + "'";
        }
    }
}