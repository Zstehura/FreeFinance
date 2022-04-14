package com.example.financefree;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.financefree.database.DatabaseManager;
import com.example.financefree.database.entities.BankStatement;
import com.example.financefree.database.entities.PaymentEdit;
import com.example.financefree.database.entities.SinglePayment;
import com.example.financefree.dialogs.BankStatementDialog;
import com.example.financefree.recyclers.DailyRVContent;
import com.example.financefree.recyclers.DailyRecyclerViewAdapter;
import com.example.financefree.structures.Construction;
import com.example.financefree.structures.DateParser;
import com.example.financefree.structures.Payment;
import com.example.financefree.structures.Statement;

import java.text.NumberFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment
        implements DailyRecyclerViewAdapter.ViewHolder.DRClickListener, BankStatementDialog.BankStatementDialogListener {


    public long currentDate;
    public DailyRecyclerViewAdapter drva;
    private RecyclerView rv;
    private CalendarView cv;
    private TextView tv;
    private ImageButton btnAdd;

    //TODO: switch this all around

    public CalendarFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CalendarFragment.
     */

    public static CalendarFragment newInstance(long date) {
        CalendarFragment calendarFragment = new CalendarFragment();
        calendarFragment.currentDate = date;
        return calendarFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        cv = view.findViewById(R.id.calendarView);
        tv = view.findViewById(R.id.txtDay);
        rv = view.findViewById(R.id.viewList);
        btnAdd = view.findViewById(R.id.btnAddNew);

        btnAdd.setOnClickListener(btn -> {
            // clicked add button
        });

        cv.setOnDateChangeListener((calendarView, iYear, iMonth, iDay) -> updateDay(DateParser.getLong(iMonth, iDay, iYear)));
        updateDay(DateParser.getLong(new GregorianCalendar()));
        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateDay(long date) {
        AtomicReference<List<Payment>> paymentList = new AtomicReference<>();
        AtomicReference<List<Statement>> statementList = new AtomicReference<>();
        Thread t = new Thread(() -> {
            paymentList.set(DatabaseManager.getPaymentsForDay(date));
            statementList.set(DatabaseManager.getStatementsForDay(date));
        });
        t.start();

        currentDate = date;
        tv.setText(DateParser.getString(date));

        try{t.join();}
        catch (InterruptedException e){e.printStackTrace();}
        drva = new DailyRecyclerViewAdapter(paymentList.get(), statementList.get(), this);
        rv.setAdapter(drva);
        drva.currentDate = currentDate;
        drva.notifyDataSetChanged();
    }

    @Override
    public void OnDailyRowClick(int position) {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void OnDailyDeleteClick(int position) {
        DailyRVContent.DailyRVItem dvi = drva.mValues.get(position);
        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setTitle("Delete")
                .setPositiveButton("Delete", (dialogInterface, i) -> {
                    Thread t = null;

                    if(dvi.isPayment){
                        if(dvi.isRecurring){
                            // just edit to skip this
                            t = new Thread(() -> {
                                List<PaymentEdit> pel = DatabaseManager.getPaymentEditDao().getEditsForRpOnDate(currentDate, drva.getItemId(position));
                                if(pel.size() == 0){
                                    PaymentEdit pe = Construction.getEdit(DatabaseManager.getRecurringPaymentDao()
                                            .getRecurringPayment(drva.getItemId(position)), currentDate);
                                    pe.skip = true;
                                    DatabaseManager.getPaymentEditDao().insert(pe);
                                }
                                else if(pel.size() == 1) {
                                    PaymentEdit pe = pel.get(0);
                                    pe.skip = true;
                                    DatabaseManager.getPaymentEditDao().update(pe);
                                }
                            });
                        }
                        else {
                            // Not recurring
                            t = new Thread(() -> {
                                SinglePayment sp = DatabaseManager.getSinglePaymentDao().getPayment(drva.getItemId(position));
                                DatabaseManager.getSinglePaymentDao().delete(sp);
                            });
                        }
                    }
                    else {
                        // delete statement
                        t = new Thread(() -> {
                            BankStatement bs = DatabaseManager.getBankStatementDao().getStatement(drva.getItemId(position));
                            if(bs != null) DatabaseManager.getBankStatementDao().delete(bs);
                        });
                    }
                    t.start();

                    drva.remove(position);

                    try {t.join(); }
                    catch (InterruptedException e) {e.printStackTrace();}
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> {/* Do nothing */})
                .create();
        alertDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void OnDailyEditClick(int position) {
        Thread t = null;
        DailyRVContent.DailyRVItem dvi = drva.mValues.get(position);

        if(dvi.isPayment) {
            if(dvi.isRecurring){
                // Recurring Payment
                // Create payment edit

            }
            else {
                // Single Payment

            }
        }
        else{
            // TODO: Statement name keeps coming up Null after update. Works fine for days that aren't today (4/14/22)
            // Bank Statement
            BankStatement bs = Construction.makeStatement(drva.getItemId(position), currentDate, dvi.amount);

            BankStatementDialog bsd = BankStatementDialog.newInstance(bs, position, dvi.isCalculated);
            bsd.setListener(this);
            bsd.show(getParentFragmentManager(), "BankStatementEdit");
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onDialogPositiveClick(BankStatementDialog dialog) {
        if(dialog != null) drva.setItem(dialog);
    }

    @Override
    public void onDialogNegativeClick(BankStatementDialog dialog) { }
}