package com.example.financefree;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

/**
 * A fragment representing a list of Items.
 */
public class ToolFragment extends Fragment {

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ToolFragment() {}

    @SuppressWarnings("unused")
    public static ToolFragment newInstance(int columnCount) {
        return new ToolFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tool_item_list, container, false);

        CardView cvAnnual, cvTax, cvLoan;

        cvAnnual = view.findViewById(R.id.itemAnnualView);
        cvTax = view.findViewById(R.id.itemTaxEstimator);
        cvLoan = view.findViewById(R.id.itemLoanCalc);

        cvAnnual.setOnClickListener(view1 -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.annualViewFragment);
        });
        cvTax.setOnClickListener(view1 -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.taxEstimatorFragment);
        });
        cvLoan.setOnClickListener(view1 -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.loanEstimatorFragment);
        });

        return view;
    }
}