package com.example.financefree;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.financefree.RecyclerContent.ListItem;
import com.example.financefree.databinding.RecurringPaymentsBinding;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ListItem}.
 */
public class MyRecurringPaymentsRecyclerViewAdapter extends RecyclerView.Adapter<MyRecurringPaymentsRecyclerViewAdapter.ViewHolder> {
    private final List<ListItem> itemList;

    public MyRecurringPaymentsRecyclerViewAdapter(List<ListItem> items) {
        itemList = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

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
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public ListItem mItem;

        public ViewHolder(RecurringPaymentsBinding binding) {
            super(binding.getRoot());
            mIdView = binding.itemNumber;
            mContentView = binding.content;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}