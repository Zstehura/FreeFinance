package com.example.financefree.recyclers;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.financefree.database.entities.RecurringPayment;
import com.example.financefree.dialogs.RecurringPaymentDialog;
import com.example.financefree.recyclers.RecurringPaymentRVContent.RecurringPaymentRVItem;
import com.example.financefree.databinding.FragmentRecurringPaymentBinding;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link RecurringPaymentRVItem}.
 * TODO: Replace the implementation with code for your data type.
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

    public void setItem(RecurringPaymentDialog dialog) {

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