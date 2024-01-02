package com.mayurshelar.theexpensebook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class InterestCalculatorActivity extends AppCompatActivity {

    private TextInputEditText principalAmount, monthlyDeposit, months, interestRate;

    private double interest;

    TextView principal, interestTotal, maturity;
    private Button calculateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest_calculator);

        calculateBtn = findViewById(R.id.calculate);

        principal = findViewById(R.id.total_amount);
        interestTotal = findViewById(R.id.interest);
        maturity = findViewById(R.id.maturity);

        principalAmount = findViewById(R.id.principal_amount);
        monthlyDeposit = findViewById(R.id.monthly_deposit);
        months = findViewById(R.id.months);
        interestRate = findViewById(R.id.interest_rates);



        calculateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String principalAmt = principalAmount.getText().toString();
                String mnthDeposits = monthlyDeposit.getText().toString();
                String mnths = months.getText().toString();
                String interest = interestRate.getText().toString();
                boolean error = false;

                if(principalAmt.isEmpty()){
                    principalAmount.setError("Required Field");
                    error = true;
                }
                if(mnths.isEmpty()){
                    months.setError("Required Field");
                    error = true;
                }
                if(mnthDeposits.isEmpty()){
                    monthlyDeposit.setError("Required Field");
                    error = true;
                }
                if(interest.isEmpty()){
                    interestRate.setError("Required Field");
                    error = true;
                }

                if(error){
                    return;
                } else {
                    float amt = Float.parseFloat(principalAmt);
                    float mnthsD = Float.parseFloat(mnthDeposits);
                    int noOfMonths = Integer.parseInt(mnths);
                    float interestRate = Float.parseFloat(interest);



                    ArrayList<Float> s = new ArrayList<>();
                    calculateMaturityDetails(amt, mnthsD, noOfMonths, interestRate,s);

                    principal.setText("Pricipal: "+s.get(0).toString());
                    interestTotal.setText("Interest: "+s.get(1).toString());
                    maturity.setText("Maturity Balance: "+s.get(2).toString());

                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });

    }

    public static void calculateMaturityDetails(float principalAmount, float monthlyDeposit, int numberOfMonths, float annualInterestRate, ArrayList<Float> s) {
        float monthlyInterestRate = (float) annualInterestRate / 12 / 100;
        float totalPrincipal = 0.0f;
        float totalInterest = 0.0f;
        float maturityBalance = principalAmount;

        for (int j = 0; j < numberOfMonths; j++) {
            float interestForMonth = maturityBalance * monthlyInterestRate; // Interest for the month
            totalInterest += interestForMonth; // Accumulate interest
            maturityBalance = (maturityBalance + monthlyDeposit) * (1 + monthlyInterestRate); // Update the balance for the next month
        }

        totalPrincipal = principalAmount + (numberOfMonths * monthlyDeposit);

        s.add(totalPrincipal);
        s.add(totalInterest);
        s.add(maturityBalance);
    }
}