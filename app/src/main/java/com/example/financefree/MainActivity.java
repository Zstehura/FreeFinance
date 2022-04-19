package com.example.financefree;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.example.financefree.database.DatabaseManager;

/**
 *  TODO:   Implement ads
 *          Data validation needed for dialogs
 *          Add warning message about security
 *          Add memory cleanup functions
 *          Add Tax estimation machine
 *          Add what if section?
 *          Add loan calculator
 *          *Set up Settings
 *          *Add tools menu
*/


@SuppressWarnings("SpellCheckingInspection")
public class MainActivity extends AppCompatActivity {
    private static final String CALC_HISTORY_KEY = "calc_hist";

    private enum Operation {
        NONE,ADD,SUBTRACT,EXPONENT,MULTIPLY,DIVIDE,INVERT,SQUARE_ROOT,ERROR
    }

    public static DatabaseManager dm;

    private double num1, num2, prevResult;
    private Operation op;
    private StringBuilder calcEq, sNum;
    private int dispLen;
    private TextView tvCalcDisp, tvCalcHist1, tvCalcHist2, tvCalcHist3, tvCalcHist4, tvCalcHist5;
    private Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0, btnDec, btnPlus,
                    btnMinus, btnMult, btnDivide, btnInv, btnExp, btnSqrt, btnEq, btnCE;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get database
        dm = new DatabaseManager(this.getApplication());

        // Clear out old data
        //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        //int days = preferences.getInt("clear_data_older_than", 365);
        //if(days > 0){
            //DatabaseManager.cleanUpDatabase(days);
        //}

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
        setBtnClickers();
        resetCalcVars();
        dispLen = 25; //tvCalcDisp.length();
        //Log.d("MainActivity>>>>", "Calculator display length: " + dispLen);


        SharedPreferences pref = getSharedPreferences(CALC_HISTORY_KEY, Context.MODE_PRIVATE);
        tvCalcHist1.setText(pref.getString(CALC_HISTORY_KEY + "1", ""));
        tvCalcHist2.setText(pref.getString(CALC_HISTORY_KEY + "2", ""));
        tvCalcHist3.setText(pref.getString(CALC_HISTORY_KEY + "3", ""));
        tvCalcHist4.setText(pref.getString(CALC_HISTORY_KEY + "4", ""));
        tvCalcHist5.setText(pref.getString(CALC_HISTORY_KEY + "5", ""));
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

    private void setOp(Operation op){
        if(this.op != Operation.NONE) {
            this.op = Operation.ERROR;
            executeCalc();
        }
        else {
            if (sNum.toString().equals("")) {
                sNum.append(prevResult);
                calcEq.append(prevResult);
            }

            try {
                num1 = Double.parseDouble(sNum.toString());
                this.op = op;
                sNum = new StringBuilder();

                if (op == Operation.ADD) {
                    addCalcChar('+');
                } else if (op == Operation.MULTIPLY) {
                    addCalcChar('*');
                } else if (op == Operation.DIVIDE) {
                    addCalcChar('/');
                } else if (op == Operation.EXPONENT) {
                    addCalcChar('^');
                } else if (op == Operation.INVERT) {
                    calcEq.insert(0, "Inv(");
                    addCalcChar(')');
                    executeCalc();
                } else if (op == Operation.SQUARE_ROOT) {
                    calcEq.insert(0, "Sqrt(");
                    addCalcChar(')');
                    executeCalc();
                } else if (op == Operation.SUBTRACT) {
                    if (sNum.toString().equals("0")) {
                        calcEq = new StringBuilder();
                        sNum.append("-");
                        addCalcChar('-');
                        this.op = Operation.NONE;
                    } else {
                        addCalcChar('-');
                    }
                }
            } catch (Exception e) {
                Log.e("MainActivity", "SetOp error: " + e.getMessage());
                this.op = Operation.ERROR;
                executeCalc();
            }
        }
    }

    private void setBtnClickers(){
        btn0.setOnClickListener(view -> addCalcNum('0'));
        btn1.setOnClickListener(view -> addCalcNum('1'));
        btn2.setOnClickListener(view -> addCalcNum('2'));
        btn3.setOnClickListener(view -> addCalcNum('3'));
        btn4.setOnClickListener(view -> addCalcNum('4'));
        btn5.setOnClickListener(view -> addCalcNum('5'));
        btn6.setOnClickListener(view -> addCalcNum('6'));
        btn7.setOnClickListener(view -> addCalcNum('7'));
        btn8.setOnClickListener(view -> addCalcNum('8'));
        btn9.setOnClickListener(view -> addCalcNum('9'));
        btnDec.setOnClickListener(view -> addCalcNum('.'));
        btnPlus.setOnClickListener(view -> setOp(Operation.ADD));
        btnMinus.setOnClickListener(view -> setOp(Operation.SUBTRACT));
        btnMult.setOnClickListener(view -> setOp(Operation.MULTIPLY));
        btnDivide.setOnClickListener(view -> setOp(Operation.DIVIDE));
        btnInv.setOnClickListener(view -> setOp(Operation.INVERT));
        btnExp.setOnClickListener(view -> setOp(Operation.EXPONENT));
        btnSqrt.setOnClickListener(view -> setOp(Operation.SQUARE_ROOT));
        btnEq.setOnClickListener(view -> {
            try {
                num2 = Double.parseDouble(sNum.toString());
            }
            catch (Exception e) {
                op = Operation.NONE;
            }
            executeCalc();
        });
        btnCE.setOnClickListener(view -> resetCalcVars());
    }

    private void setViews() {
        tvCalcDisp = findViewById(R.id.txtCalcDisplay);
        tvCalcHist1 = findViewById(R.id.txtCalcHistory1);
        tvCalcHist2 = findViewById(R.id.txtCalcHistory2);
        tvCalcHist3 = findViewById(R.id.txtCalcHistory3);
        tvCalcHist4 = findViewById(R.id.txtCalcHistory4);
        tvCalcHist5 = findViewById(R.id.txtCalcHistory5);

        // btns
        btn0 = findViewById(R.id.btnCalc0);
        btn1 = findViewById(R.id.btnCalc1);
        btn2 = findViewById(R.id.btnCalc2);
        btn3 = findViewById(R.id.btnCalc3);
        btn4 = findViewById(R.id.btnCalc4);
        btn5 = findViewById(R.id.btnCalc5);
        btn6 = findViewById(R.id.btnCalc6);
        btn7 = findViewById(R.id.btnCalc7);
        btn8 = findViewById(R.id.btnCalc8);
        btn9 = findViewById(R.id.btnCalc9);
        btnDec = findViewById(R.id.btnCalcDec);
        btnPlus = findViewById(R.id.btnCalcPlus);
        btnMinus = findViewById(R.id.btnCalcMinus);
        btnMult = findViewById(R.id.btnCalcMult);
        btnDivide = findViewById(R.id.btnCalcDiv);
        btnInv = findViewById(R.id.btnCalcInv);
        btnExp = findViewById(R.id.btnCalcExp);
        btnSqrt = findViewById(R.id.btnCalcSqrt);
        btnEq = findViewById(R.id.btnCalcEq);
        btnCE = findViewById(R.id.btnCalcCE);
    }

    private void resetCalcVars(){
        tvCalcDisp.setText("");
        sNum = new StringBuilder();
        calcEq = new StringBuilder();
        op = Operation.NONE;
        num1 = 0;
        num2 = 0;
    }

    private void executeCalc() {
        String result = "ERR";

        if(op == Operation.ERROR){
            Log.e("MainActivity", "Calculator Error: Unrecognized Operation");
        }
        else if(op == Operation.NONE) {
            prevResult = num2;
            result = String.valueOf(num2);
        }
        else if(op == Operation.ADD){
            prevResult = num1 + num2;
            result = String.valueOf(prevResult);
        }
        else if(op == Operation.SUBTRACT){
            prevResult = num1 - num2;
            result = String.valueOf(prevResult);
        }
        else if(op == Operation.MULTIPLY){
            prevResult = num1 * num2;
            result = String.valueOf(prevResult);
        }
        else if(op == Operation.DIVIDE){
            prevResult = num1 / num2;
            result = String.valueOf(prevResult);
        }
        else if(op == Operation.EXPONENT){
            prevResult = Math.pow(num1,num2);
            result = String.valueOf(prevResult);
        }
        else if(op == Operation.INVERT){
            prevResult = 1 / num1;
            result = String.valueOf(prevResult);
        }
        else if(op == Operation.SQUARE_ROOT){
            prevResult = Math.sqrt(num1);
            result = String.valueOf(prevResult);
        }

        tvCalcHist1.setText(tvCalcHist3.getText().toString());
        tvCalcHist2.setText(tvCalcHist4.getText().toString());
        tvCalcHist3.setText(tvCalcHist5.getText().toString());
        tvCalcHist4.setText(calcEq.toString());
        tvCalcHist5.setText(result);
        resetCalcVars();
    }

    private void addCalcNum(char c) {
        sNum.append(c);
        addCalcChar(c);
    }

    private void addCalcChar(char c) {
        calcEq.append(c);
        if(calcEq.length() > dispLen) {
            tvCalcDisp.setText(calcEq.substring(calcEq.length() - dispLen));
        }
        else {
            tvCalcDisp.setText(calcEq.toString());
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        NavController navController = Navigation.findNavController(this,R.id.nav_host_fragment);
        return NavigationUI.onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item);
    }
}
