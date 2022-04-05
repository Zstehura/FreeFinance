package com.example.financefree.recyclers;

import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.financefree.BankAccountFragment;
import com.example.financefree.database.DatabaseManager;
import com.example.financefree.database.entities.BankAccount;
import com.example.financefree.recyclers.BankAccountRVContent.BankAccountRVItem;
import com.example.financefree.databinding.FragmentBankAccountBinding;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;

/**
 * {@link RecyclerView.Adapter} that can display a {@link BankAccountRVItem}.
 */
public class BankAccountRecyclerViewAdapter extends RecyclerView.Adapter<BankAccountRecyclerViewAdapter.ViewHolder> {
    private List<BankAccountRVItem> mValues;

    public BankAccountRecyclerViewAdapter(List<BankAccount> list) {
        Log.d("BADialog", "Initializing");
        mValues = BankAccountRVContent.getItems(list);
        Log.d("BADialog", "List Initialized");
    }

    @androidx.annotation.NonNull
    @Override
    public ViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentBankAccountBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mName;
        public final TextView mDetails;
        public BankAccountRVItem mItem;

        public ViewHolder(FragmentBankAccountBinding binding) {
            super(binding.getRoot());
            mName = binding.lblName;
            mDetails = binding.lblDesc;
            // TODO: Finish this binding
            binding.btnDelBank.setOnClickListener(view -> {
                AlertDialog alertDialog = new AlertDialog.Builder(view.getContext())
                        .setTitle("Are you sure you'd like to delete " + mName.getText() + "?")
                        .setPositiveButton("Delete", (dialogInterface, i) -> {
                            DatabaseManager.getBankAccountDao().deleteById(mItem.id);
                        })
                        .create();
            });
        }

        // TODO: Implement interface
        public interface BankAccountRecyclerListener {
            void OnBADeleteClick(int id, int position);
            void OnBAEditClick(int id, int position);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mDetails.getText() + "'";
        }
    }
}