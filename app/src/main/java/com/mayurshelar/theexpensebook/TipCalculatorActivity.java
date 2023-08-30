package com.mayurshelar.theexpensebook;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mayurshelar.theexpensebook.Model.Data;

import java.text.DateFormat;
import java.util.Date;
import java.util.Objects;

public class TipCalculatorActivity extends AppCompatActivity {

    private TextInputEditText billEdt, tipEdt, splitEdt, taxEdt;

    private TextView tipAmountTxt, totalAmountTxt, eachTipTxt, eachPayTxt;

    private  InputMethodManager inputMethodManager;

    private FirebaseAuth firebaseAuth;

    private DatabaseReference expenseDatabase;

    private double eachWillPay;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_calculator);


        billEdt = findViewById(R.id.bill);
        tipEdt = findViewById(R.id.tip);
        splitEdt = findViewById(R.id.split);
        taxEdt = findViewById(R.id.tax);

        Button splitPlusBtn = findViewById(R.id.split_plus);
        Button splitMinusBtn = findViewById(R.id.split_minus);

        Button tipPlusBtn = findViewById(R.id.tip_plus);
        Button tipMinusBtn = findViewById(R.id.tip_minus);

        Button okBtn = findViewById(R.id.submit_tip);
        Button saveToExpenseBtn = findViewById(R.id.save_to_expense_btn);
        Button resetBtn = findViewById(R.id.reset_tip);

        tipAmountTxt = findViewById(R.id.tip_amount);
        totalAmountTxt = findViewById(R.id.total_amount);
        eachTipTxt = findViewById(R.id.each_tip_amount);
        eachPayTxt = findViewById(R.id.each_will_pay);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        String uid = user.getUid();
        expenseDatabase = FirebaseDatabase.getInstance().getReference().child("ExpenseDatabase").child(uid);

        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);


        okBtn.setOnClickListener(v -> {
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
            calculateTip();
        });

        resetBtn.setOnClickListener(v -> {
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
            resetCalc();
        });

        splitPlusBtn.setOnClickListener(v -> {
            String split = Objects.requireNonNull(splitEdt.getText()).toString();
            if(split.equals("") || split.equals("99")){
                splitEdt.setText("1");
            } else{
                int splitNum = Integer.parseInt(split);
                splitEdt.setText(String.valueOf(++splitNum));
            }
        });

        splitMinusBtn.setOnClickListener(v -> {
            String split = Objects.requireNonNull(splitEdt.getText()).toString();
            if(split.equals("")|| split.equals("1")){
                splitEdt.setText("1");
            } else{
                int splitNum = Integer.parseInt(split);
                splitEdt.setText(String.valueOf(--splitNum));
            }
        });

        tipPlusBtn.setOnClickListener(v -> {
            String tip = Objects.requireNonNull(tipEdt.getText()).toString();
            if(tip.equals("")|| tip.equals("0")){
                tipEdt.setText("1");
            } else{
                int tipNum = Integer.parseInt(tip);
                tipEdt.setText(String.valueOf(++tipNum));
            }
        });

        tipMinusBtn.setOnClickListener(v -> {
            String tip = Objects.requireNonNull(tipEdt.getText()).toString();
            if(tip.equals("") || tip.equals("0") || tip.equals("99")){
                tipEdt.setText("0");
            }
            else{
                int tipNum = Integer.parseInt(tip);
                tipEdt.setText(String.valueOf(--tipNum));
            }
        });

        saveToExpenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expenseDataInsert();
            }
        });
    }

    private void calculateTip(){
        String billStr = Objects.requireNonNull(billEdt.getText()).toString().trim();
        String tipStr = Objects.requireNonNull(tipEdt.getText()).toString().trim();
        String splitStr = Objects.requireNonNull(splitEdt.getText()).toString().trim();
        String taxStr = Objects.requireNonNull(taxEdt.getText()).toString().trim();

        if(billStr.equals("") || tipStr.equals("") || splitStr.equals("") || taxStr.equals("")){
            Toast.makeText(getApplicationContext(), "Enter required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double bill = Double.parseDouble(billStr);
        double tip = Double.parseDouble(tipStr);
        double split = Double.parseDouble(splitStr);
        double  tax = Double.parseDouble(taxStr);

        if(split == 0){
            split=1;
        }

        double taxAmount = bill*(tax/100);
        double tipAmount = bill*(tip/100);
        double totalAmount = bill + taxAmount + tipAmount;
        eachWillPay = totalAmount/split;
        double eachTip = tipAmount/split;

        tipAmountTxt.setText(String.valueOf(tipAmount));
        totalAmountTxt.setText(String.valueOf(totalAmount));
        eachTipTxt.setText(String.valueOf(eachTip));
        eachPayTxt.setText(String.valueOf(eachWillPay));
    }

    private void resetCalc(){
        tipAmountTxt.setText("0");
        totalAmountTxt.setText("0");
        eachTipTxt.setText("0");
        eachPayTxt.setText("0");

        billEdt.setText("");
        tipEdt.setText("");
        splitEdt.setText("");
        taxEdt.setText("");
    }

    public void expenseDataInsert(){

        if(eachPayTxt.getText().toString().equals("0")){
            Toast.makeText(getApplicationContext(), "No data to insert!",Toast.LENGTH_SHORT).show();
            return;
        }


        AlertDialog.Builder myDialog = new AlertDialog.Builder(TipCalculatorActivity.this);

        LayoutInflater inflater = LayoutInflater.from(TipCalculatorActivity.this);

        View myView = inflater.inflate(R.layout.content_layout_for_data_insertion, null);

        myDialog.setView(myView);

        final AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);

        EditText edtAmount = myView.findViewById(R.id.amount);
        EditText edtType = myView.findViewById(R.id.type);
        EditText edtNote = myView.findViewById(R.id.note);

        Button btnSave = myView.findViewById(R.id.save_btn);
        Button btnCancel = myView.findViewById(R.id.cancel_btn);

        edtAmount.setText(String.valueOf(eachWillPay));
        edtType.setText("Tip");

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = edtAmount.getText().toString().trim();
                String type = edtType.getText().toString().trim();
                String note = edtNote.getText().toString().trim();

                if (amount.isEmpty()) {
                    edtAmount.setError("Enter Amount.");
                    return;
                }
                if (type.isEmpty()) {
                    edtNote.setError("Enter Type.");
                    return;
                }
                if (note.isEmpty()) {
                    edtNote.setError("Enter Note.");
                    return;
                }

                double curAmount = Double.parseDouble(amount);

                String id = expenseDatabase.push().getKey();

                String date = DateFormat.getDateInstance().format(new Date());

                Data data = new Data((int)curAmount, type, note, date, id);

                expenseDatabase.child(id).setValue(data);

                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}