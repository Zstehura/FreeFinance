package com.example.financefree;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.financefree.datahandlers.CustomDate;
import com.example.financefree.datahandlers.DataManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment {

    private CustomDate currentDate;
    private DataManager dataManager;

    public CalendarFragment(CustomDate cd, DataManager dm) {
        // Required empty public constructor
        currentDate = new CustomDate(cd);
        dataManager = dm;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    public static CalendarFragment newInstance(CustomDate cd, DataManager dm) {
        CalendarFragment calendarFragment = new CalendarFragment(cd, dm);
        return calendarFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalendarView cv = getView().findViewById(R.id.calendarView);
        TextView tv = getView().findViewById(R.id.txtDay);
        RecyclerView rv = getView().findViewById(R.id.viewList);

        cv.setOnDateChangeListener((calendarView, iYear, iMonth, iDay) -> {
            try {
                currentDate = new CustomDate(iMonth+1, iDay,iYear);
                tv.setText(currentDate.toString());
                // TODO: Set recycler
            } catch (CustomDate.DateErrorException e) {
                e.printStackTrace();
            }
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