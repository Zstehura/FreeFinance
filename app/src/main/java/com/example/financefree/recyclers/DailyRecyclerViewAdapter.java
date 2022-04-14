package com.example.financefree.recyclers;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.financefree.database.DatabaseManager;
import com.example.financefree.database.entities.BankAccount;
import com.example.financefree.database.entities.BankStatement;
import com.example.financefree.databinding.FragmentCalendarItemBinding;
import com.example.financefree.dialogs.BankAccountDialog;
import com.example.financefree.dialogs.BankStatementDialog;
import com.example.financefree.structures.Construction;
import com.example.financefree.structures.DateParser;
import com.example.financefree.structures.Payment;
import com.example.financefree.structures.Statement;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.rxjava3.annotations.NonNull;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DailyRVContent}.
 */
public class DailyRecyclerViewAdapter extends RecyclerView.Adapter<DailyRecyclerViewAdapter.ViewHolder> {
    public List<DailyRVContent.DailyRVItem> mValues;
    private final ViewHolder.DRClickListener listener;
    public long currentDate;

    public DailyRecyclerViewAdapter(List<Payment> paymentList, List<Statement> statementList, ViewHolder.DRClickListener listener) {
        this.listener = listener;
        mValues = DailyRVContent.getItems(statementList, paymentList);
    }

    @androidx.annotation.NonNull
    @Override
    public ViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentCalendarItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), listener);
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setItem(BankStatementDialog dialog) {
        if(dialog.isNew) {
            BankStatement bs = new BankStatement();
            AtomicReference<Long> sId = new AtomicReference<>();
            AtomicReference<String> name = new AtomicReference<>();
            bs.bank_id = dialog.bankId;
            bs.amount = Double.parseDouble(dialog.etAmount.getText().toString());
            bs.date = DateParser.getLong(dialog.etDate.getText().toString());
            Thread t = new Thread(() -> {
                sId.set(DatabaseManager.getBankStatementDao().insert(bs));
                name.set(DatabaseManager.getBankAccountDao().getBankAccount(bs.bank_id).name);
            });
            t.start();
            try {t.join();}
            catch (InterruptedException e) {e.printStackTrace();}
            bs.statement_id = sId.get();
            DailyRVContent.updateItem(new Statement(bs, name.get()));
            this.notifyItemChanged(dialog.position);
        }
        else {
            BankStatement bs = new BankStatement();
            AtomicReference<String> name = new AtomicReference<>();
            bs.statement_id = dialog.sId;
            bs.date = DateParser.getLong(dialog.etDate.getText().toString());
            bs.amount = Double.parseDouble(dialog.etAmount.getText().toString());
            bs.bank_id = dialog.bankId;
            Thread t = new Thread(() -> {
                DatabaseManager.getBankStatementDao().update(bs);
                name.set(DatabaseManager.getBankAccountDao().getBankAccount(dialog.bankId).name);
            });
            t.start();
            DailyRVContent.updateItem(new Statement(bs, name.get()));
            this.notifyItemChanged(dialog.position);
            try {t.join();}
            catch (InterruptedException e) {e.printStackTrace();}
        }
    }
    @Override
    public long getItemId(int position) {
        return mValues.get(position).getItemId();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void remove(int position) {
        if (mValues.get(position).isPayment) {
            mValues.remove(position);
            this.notifyItemRemoved(position);
        }
        else {
            AtomicReference<List<Statement>> l = new AtomicReference<>();
            Thread t = new Thread(() -> l.set(DatabaseManager.getStatementsForDay(currentDate)));
            t.start();
            try{t.join();}
            catch (InterruptedException e){e.printStackTrace();}
            Statement s = null;
            for(Statement temp: l.get()){
                if(temp.bankId == getItemId(position)) s = temp;
            }

            DailyRVContent.updateItem(s);
        }

    }

    public String getItemName(int position) {
        return mValues.get(position).name;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final WeakReference<DRClickListener> listenerRef;
        public final TextView mName, mDetails;
        public final ImageButton btnDel, btnEdit;
        public DailyRVContent.DailyRVItem mItem;

        public ViewHolder(FragmentCalendarItemBinding binding, DRClickListener listener) {
            super(binding.getRoot());
            mName = binding.lblName;
            mDetails = binding.lblDesc;
            btnDel = binding.btnDelBank;
            //if(!mItem.isPayment && mItem.isCalculated) btnDel.setVisibility(View.GONE);
            btnEdit = binding.btnEditBank;

            listenerRef = new WeakReference<>(listener);

            btnEdit.setOnClickListener(this);
            btnDel.setOnClickListener(this);
            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view.getId() == btnDel.getId()) {
                listenerRef.get().OnDailyDeleteClick(getBindingAdapterPosition());
            }
            else if(view.getId() == btnEdit.getId()) {
                listenerRef.get().OnDailyEditClick(getBindingAdapterPosition());
            }
            else {
                listenerRef.get().OnDailyRowClick(getBindingAdapterPosition());
            }
        }

        public interface DRClickListener {
            void OnDailyRowClick(int position);
            void OnDailyDeleteClick(int position);
            void OnDailyEditClick(int position);
        }

        @androidx.annotation.NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + mDetails.getText() + "'";
        }
    }
}