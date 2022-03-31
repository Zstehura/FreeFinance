package com.example.financefree;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.financefree.database.entities.BankAccount;
import com.example.financefree.recyclers.BankAccountRVContent;
import com.example.financefree.recyclers.BankAccountRVContent.BankAccountRVItem;
import com.example.financefree.databinding.FragmentBankAccountBinding;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;

/**
 * {@link RecyclerView.Adapter} that can display a {@link BankAccountRVItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class BankAccountRecyclerViewAdapter extends RecyclerView.Adapter<BankAccountRecyclerViewAdapter.ViewHolder> {

    private final List<BankAccountRVItem> mValues;

    public BankAccountRecyclerViewAdapter(@NonNull List<BankAccount> list) {
        mValues = BankAccountRVContent.getItems(list);
    }

    @androidx.annotation.NonNull
    @Override
    public ViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentBankAccountBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(String.valueOf(mValues.get(position).id));
        holder.mContentView.setText(mValues.get(position).details);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public BankAccountRVItem mItem;

        public ViewHolder(FragmentBankAccountBinding binding) {
            super(binding.getRoot());
            mIdView = binding.lblName;
            mContentView = binding.lblDesc;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}