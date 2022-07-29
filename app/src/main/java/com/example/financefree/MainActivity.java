package com.example.financefree;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.example.financefree.database.DatabaseManager;

import java.util.HashMap;
import java.util.Map;

/**
 *  TODO:   Implement ads / ad-free payment
*/


@SuppressWarnings("SpellCheckingInspection")
public class MainActivity extends AppCompatActivity {
    private String WARNING_MESSAGE_KEY;
    private static final String CALC_HISTORY_KEY = "calc_hist";
    private final MainCalculator calc = new MainCalculator();

    public static DatabaseManager dm;

    private int dispLen;
    private TextView tvCalcDisp, tvCalcHist1, tvCalcHist2, tvCalcHist3, tvCalcHist4, tvCalcHist5;
    private final Map<Integer, Integer> mapCalcBtns = new HashMap<>();

    @SuppressWarnings("ConstantConditions")
    View.OnClickListener numBtnListener = view -> {
        boolean calcDone = calc.send(mapCalcBtns.get(view.getId()));

        if(calcDone) {
            tvCalcDisp.setText("");
            tvCalcHist1.setText(tvCalcHist3.getText());
            tvCalcHist2.setText(tvCalcHist4.getText());
            tvCalcHist3.setText(tvCalcHist5.getText());
            tvCalcHist4.setText(calc.getPrevEq());
            tvCalcHist5.setText(calc.getAnswer());
        }
        else {
            tvCalcDisp.setText(calc.getCurrentStatement());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyResources.setContext(this);
        final String memLengthKey = this.getString(R.string.mem_length_key);
        WARNING_MESSAGE_KEY = this.getString(R.string.show_sec_msg_key);

        // Get database
        dm = new DatabaseManager(this.getApplication());
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Clear out old data
        int days = Integer.parseInt(preferences.getString(memLengthKey, "365"));
        if(days > 0){
            Thread t = new Thread(() -> DatabaseManager.cleanUpDatabase(days));
            t.start();
        }

        // show the security message
        if(preferences.getBoolean(WARNING_MESSAGE_KEY, true)){
            AlertDialog d = new AlertDialog.Builder(this)
                    .setTitle(R.string.sec_msg_title)
                    .setMessage(R.string.security_message)
                    .setPositiveButton(R.string.thx, (dialogInterface, i) -> {/* Do nothing */})
                    .setNegativeButton(R.string.dont_show_again, ((dialogInterface, i) -> {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean(WARNING_MESSAGE_KEY, false);
                        editor.apply();
                    }))
                    .create();
            d.show();
        }

        // Set navigation
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;

        // Set Calculator up
        findViewById(R.id.btnCalcExpand).setOnClickListener(view -> {
            View v = findViewById(R.id.calculator);
            if(v.getVisibility() == View.VISIBLE){
                v.setVisibility(View.GONE);
                ((ImageButton) view).setImageResource(R.drawable.ic_down);
            }
            else {
                v.setVisibility(View.VISIBLE);
                ((ImageButton) view).setImageResource(R.drawable.ic_up);
            }
        });

        setViews();
        for(int i: mapCalcBtns.keySet()) {
            findViewById(i).setOnClickListener(numBtnListener);
        }
        dispLen = 25; //tvCalcDisp.length();
        //Log.d("MainActivity>>>>", "Calculator display length: " + dispLen);


        SharedPreferences pref = getSharedPreferences(CALC_HISTORY_KEY, Context.MODE_PRIVATE);
        tvCalcHist1.setText(pref.getString(CALC_HISTORY_KEY + "1", ""));
        tvCalcHist2.setText(pref.getString(CALC_HISTORY_KEY + "2", ""));
        tvCalcHist3.setText(pref.getString(CALC_HISTORY_KEY + "3", ""));
        tvCalcHist4.setText(pref.getString(CALC_HISTORY_KEY + "4", ""));
        tvCalcHist5.setText(pref.getString(CALC_HISTORY_KEY + "5", ""));
        tvCalcDisp.setText("0");
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences pref = getSharedPreferences(CALC_HISTORY_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(CALC_HISTORY_KEY + "1", tvCalcHist1.getText().toString());
        editor.putString(CALC_HISTORY_KEY + "2", tvCalcHist2.getText().toString());
        editor.putString(CALC_HISTORY_KEY + "3", tvCalcHist3.getText().toString());
        editor.putString(CALC_HISTORY_KEY + "4", tvCalcHist4.getText().toString());
        editor.putString(CALC_HISTORY_KEY + "5", tvCalcHist5.getText().toString());
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences pref = getSharedPreferences(CALC_HISTORY_KEY, Context.MODE_PRIVATE);
        tvCalcHist1.setText(pref.getString(CALC_HISTORY_KEY + "1", ""));
        tvCalcHist2.setText(pref.getString(CALC_HISTORY_KEY + "2", ""));
        tvCalcHist3.setText(pref.getString(CALC_HISTORY_KEY + "3", ""));
        tvCalcHist4.setText(pref.getString(CALC_HISTORY_KEY + "4", ""));
        tvCalcHist5.setText(pref.getString(CALC_HISTORY_KEY + "5", ""));
    }

    private void setViews() {
        tvCalcDisp = findViewById(R.id.txtCalcDisplay);
        tvCalcHist1 = findViewById(R.id.txtCalcHistory1);
        tvCalcHist2 = findViewById(R.id.txtCalcHistory2);
        tvCalcHist3 = findViewById(R.id.txtCalcHistory3);
        tvCalcHist4 = findViewById(R.id.txtCalcHistory4);
        tvCalcHist5 = findViewById(R.id.txtCalcHistory5);

        // btns
        mapCalcBtns.put(R.id.btnCalc0, 0);
        mapCalcBtns.put(R.id.btnCalc1, 1);
        mapCalcBtns.put(R.id.btnCalc2, 2);
        mapCalcBtns.put(R.id.btnCalc3, 3);
        mapCalcBtns.put(R.id.btnCalc4, 4);
        mapCalcBtns.put(R.id.btnCalc5, 5);
        mapCalcBtns.put(R.id.btnCalc6, 6);
        mapCalcBtns.put(R.id.btnCalc7, 7);
        mapCalcBtns.put(R.id.btnCalc8, 8);
        mapCalcBtns.put(R.id.btnCalc9, 9);
        mapCalcBtns.put(R.id.btnCalcDec, -1);

        mapCalcBtns.put(R.id.btnCalcPlus, MainCalculator.ADD);
        mapCalcBtns.put(R.id.btnCalcMinus, MainCalculator.SUBTRACT);
        mapCalcBtns.put(R.id.btnCalcMult, MainCalculator.MULTIPLY);
        mapCalcBtns.put(R.id.btnCalcDiv, MainCalculator.DIVIDE);
        mapCalcBtns.put(R.id.btnCalcInv, MainCalculator.INVERT);
        mapCalcBtns.put(R.id.btnCalcExp, MainCalculator.EXPONENT);
        mapCalcBtns.put(R.id.btnCalcSqrt, MainCalculator.SQUARE_ROOT);
        mapCalcBtns.put(R.id.btnCalcEq, MainCalculator.EQUALS);
        mapCalcBtns.put(R.id.btnCalcCE, MainCalculator.CLEAR);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        NavController navController = Navigation.findNavController(this,R.id.nav_host_fragment);
        return NavigationUI.onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item);
    }
}
