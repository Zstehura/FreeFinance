package com.example.financefree;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.financefree.databaseClasses.DatabaseAccessor;
import com.example.financefree.structures.parseDate;
import com.example.financefree.structures.payment;
import com.example.financefree.structures.statement;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment {
    public long currentDate;

    //TODO: switch this all around

    public CalendarFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */

    public static CalendarFragment newInstance(long date) {
        CalendarFragment calendarFragment = new CalendarFragment();
        calendarFragment.currentDate = date;
        return calendarFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalendarView cv = getView().findViewById(R.id.calendarView);
        TextView tv = getView().findViewById(R.id.txtDay);
        RecyclerView rv = getView().findViewById(R.id.viewList);

        cv.setOnDateChangeListener((calendarView, iYear, iMonth, iDay) -> {
            currentDate = parseDate.getLong(iMonth, iDay, iYear);
            tv.setText(parseDate.getString(currentDate));
            List<statement> statements = DatabaseAccessor.getStatementsOnDate(currentDate);
            List<payment> payments = DatabaseAccessor.getPaymentsOnDate(currentDate);
            


        });

        rv.setOnClickListener(view -> {

        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.calendar_fragment, container, false);
    }
}