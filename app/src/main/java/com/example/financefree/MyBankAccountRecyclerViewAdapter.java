package com.example.financefree;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.financefree.RecyclerContent.ListItem;
import com.example.financefree.databaseClasses.BankAccount;
import com.example.financefree.databaseClasses.DatabaseAccessor;
import com.example.financefree.databaseClasses.RecurringPayment;
import com.example.financefree.databinding.RecurringPaymentsBinding;

import java.util.LinkedList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ListItem}.
 */
public class MyBankAccountRecyclerViewAdapter extends RecyclerView.Adapter<MyBankAccountRecyclerViewAdapter.ViewHolder> {
    private final List<ListItem> itemList;

    public MyBankAccountRecyclerViewAdapter() {
        itemList = new LinkedList<>();
        for(BankAccount ba: DatabaseAccessor.db.bankAccountDao().getAll()){
            itemList.add(new ListItem(ba.accountName, 0, ba.notes, 'b', ba.bank_id));
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(RecurringPaymentsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    // TODO: Format text of the things
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = itemList.get(position);
        holder.mIdView.setText(itemList.get(position).name);
        holder.mContentView.setText(itemList.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    // TODO: Figure out what this does
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public ListItem mItem;

        public ViewHolder(RecurringPaymentsBinding binding) {
            super(binding.getRoot());
            mIdView = binding.itemNumber;
            mContentView = binding.content;
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}