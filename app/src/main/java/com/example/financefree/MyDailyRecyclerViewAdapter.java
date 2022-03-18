package com.example.financefree;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.financefree.RecyclerContent.ListItem;
import com.example.financefree.databinding.RecurringPaymentsBinding;
import com.example.financefree.structures.payment;
import com.example.financefree.structures.statement;

import java.util.LinkedList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ListItem}.
 */
public class MyDailyRecyclerViewAdapter extends RecyclerView.Adapter<MyDailyRecyclerViewAdapter.ViewHolder> {
    private final List<ListItem> itemList;

    public MyDailyRecyclerViewAdapter(long date) {
        // Generate list of statements
        itemList = new LinkedList<>();
       // List<statement> statements = DatabaseAccessor.getStatementsOnDate(date);
       // for(statement s: statements){
       //     itemList.add(new ListItem(s.bankName, s.amount, "", 'b', s.bankId));
       // }
       // List<payment> payments = DatabaseAccessor.getPaymentsOnDate(date);
       // for(payment p: payments){
       //     itemList.add(new ListItem(p.name,p.amount,String.valueOf(p.bankId),p.cType,p.id));
       // }
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