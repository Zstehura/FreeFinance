package com.example.financefree;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.financefree.structures.parseDate;


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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        CalendarView cv = view.findViewById(R.id.calendarView);
        TextView tv = view.findViewById(R.id.txtDay);
        RecyclerView rv = view.findViewById(R.id.viewList);

        // TODO: Make this actually work
        cv.setOnDateChangeListener((calendarView, iYear, iMonth, iDay) -> {
            currentDate = parseDate.getLong(iMonth, iDay, iYear);
            tv.setText(parseDate.getString(currentDate));
            rv.setAdapter(new MyDailyRecyclerViewAdapter(currentDate));
        });

        rv.setOnClickListener(v -> {

        });
        return view;
    }
}