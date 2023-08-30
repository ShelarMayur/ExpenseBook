package com.mayurshelar.theexpensebook;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CalculatorActivity extends AppCompatActivity {

    private Button modulo_btn;
    private Button divide_btn;
    private Button plus_btn;
    private Button multiply_btn;
    private Button minus_btn;

    private TextView display;
    private boolean plus=false,minus=false,multiply=false,divide=false,modulo=false;

    private double value1,valueInfinite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);


        Button clear_btn = findViewById(R.id.C);
        modulo_btn = findViewById(R.id.modulo);
        divide_btn = findViewById(R.id.divide);
        Button clearOne_btn = findViewById(R.id.X);
        plus_btn = findViewById(R.id.plus);
        Button seven_btn = findViewById(R.id.seven);
        Button eight_btn = findViewById(R.id.eight);
        Button nine_btn = findViewById(R.id.nine);
        multiply_btn = findViewById(R.id.multiply);
        Button four_btn = findViewById(R.id.four);
        Button five_btn = findViewById(R.id.five);
        Button six_btn = findViewById(R.id.six);
        minus_btn = findViewById(R.id.minus);
        Button one_btn = findViewById(R.id.one);
        Button two_btn = findViewById(R.id.two);
        Button three_btn = findViewById(R.id.three);
        Button doubleZero_btn = findViewById(R.id.double_zero);
        Button zero_btn = findViewById(R.id.zero);
        Button point_btn = findViewById(R.id.point);
        Button equals_btn = findViewById(R.id.equals);

        display = findViewById(R.id.display);
        display.setSelected(true);
        display.setMovementMethod(new ScrollingMovementMethod());
    }

    public void btnClearClicked(View view) {
        display.setText("");
        value1=0;
        valueInfinite=0;
        plus_btn.setBackgroundColor(Color.BLACK);
        multiply_btn.setBackgroundColor(Color.BLACK);
        divide_btn.setBackgroundColor(Color.BLACK);
        minus_btn.setBackgroundColor(Color.BLACK);
        modulo_btn.setBackgroundColor(Color.BLACK);
        plus=minus=multiply=divide=modulo=false;
    }
    public void btnPlusClicked(View view) {
        if(minus==true || plus == true || modulo==true || multiply==true|| divide==true || display.getText().toString()==""){
            Toast.makeText(this, "Not Allowed", Toast.LENGTH_SHORT).show();
            return;
        }
        plus_btn.setBackgroundColor(Color.WHITE);
        plus = true;
        value1 = Double.parseDouble(display.getText().toString());
        display.setText("");
    }
    public void btnModuloClicked(View view) {
        if(minus==true || plus == true || modulo==true || multiply==true|| divide==true || display.getText().toString()==""){
            Toast.makeText(this, "Not Allowed", Toast.LENGTH_SHORT).show();
        }
        modulo_btn.setBackgroundColor(Color.WHITE);
        modulo = true;
        value1 = Double.parseDouble(display.getText().toString());
        display.setText("");
    }
    public void btnDivideClicked(View view) {
        if(minus==true || plus == true || modulo==true || multiply==true|| divide==true || display.getText().toString()==""){
            Toast.makeText(this, "Not Allowed", Toast.LENGTH_SHORT).show();
            return;
        }
        divide_btn.setBackgroundColor(Color.WHITE);
        divide = true;
        value1 = Double.parseDouble(display.getText().toString());
        display.setText("");
    }
    public void btnMultiplyClicked(View view) {
        if(minus==true || plus == true || modulo==true || multiply==true|| divide==true || display.getText().toString()==""){
            Toast.makeText(this, "Not Allowed", Toast.LENGTH_SHORT).show();
            return;
        }
        multiply_btn.setBackgroundColor(Color.WHITE);
        multiply = true;
        value1 = Double.parseDouble(display.getText().toString());
        display.setText("");
    }
    public void btnMinusClicked(View view) {
        if(minus==true || plus == true || modulo==true || multiply==true|| divide==true || display.getText().toString()==""){
            Toast.makeText(this, "Not Allowed", Toast.LENGTH_SHORT).show();
            return;
        }
        minus_btn.setBackgroundColor(Color.WHITE);
        minus = true;
        value1 = Double.parseDouble(display.getText().toString());
        display.setText("");
    }
    public void btnEqualsClicked(View view){
        if(display.getText().toString()==""){
            Toast.makeText(this, "Enter some value", Toast.LENGTH_SHORT).show();
            return;
        }
        if(plus==true){
            valueInfinite = Double.parseDouble(display.getText().toString());
            display.setText(String.valueOf(value1+valueInfinite));
            plus_btn.setBackgroundColor(Color.BLACK);
            plus=false;
        }
        else if(minus==true){
            valueInfinite = Double.parseDouble(display.getText().toString());
            display.setText(String.valueOf(value1-valueInfinite));
            minus_btn.setBackgroundColor(Color.BLACK);
            minus=false;
        }
        else if(multiply==true){
            valueInfinite = Double.parseDouble(display.getText().toString());
            display.setText(String.valueOf(value1*valueInfinite));
            multiply_btn.setBackgroundColor(Color.BLACK);
            multiply=false;
        }
        else if(modulo==true){
            valueInfinite = Double.parseDouble(display.getText().toString());
            display.setText(String.valueOf(value1%valueInfinite));
            modulo_btn.setBackgroundColor(Color.BLACK);
            modulo=false;
        }else if(divide==true){
            valueInfinite = Double.parseDouble(display.getText().toString());
            try{
                display.setText(String.valueOf(value1/valueInfinite));}
            catch (ArithmeticException e){
                Toast.makeText(this, "Divide by zero not possible", Toast.LENGTH_SHORT).show();
                btnClearClicked(view);
            }
            divide_btn.setBackgroundColor(Color.BLACK);
            divide=false;
        }
    }
    public void btnNineClicked(View view) {
        String value = display.getText().toString();
        display.setText(value+"9");
    }
    public void btnEightClicked(View view) {
        String value = display.getText().toString();
        display.setText(value+"8");
    }
    public void btnSevenClicked(View view) {
        String value = display.getText().toString();
        display.setText(value+"7");
    }
    public void btnSixClicked(View view) {
        String value = display.getText().toString();
        display.setText(value+"6");
    }
    public void btnFiveClicked(View view) {
        String value = display.getText().toString();
        display.setText(value+"5");
    }
    public void btnFourClicked(View view) {
        String value = display.getText().toString();
        display.setText(value+"4");
    }

    public void btnThreeClicked(View view) {
        String value = display.getText().toString();
        display.setText(value+"3");
    }
    public void btnTwoClicked(View view) {
        String value = display.getText().toString();
        display.setText(value+"2");
    }
    public void btnOneClicked(View view) {
        String value = display.getText().toString();
        display.setText(value+"1");
    }
    public void btnDoubleZeroClicked(View view) {
        String value = display.getText().toString();
        display.setText(value+"00");
    }
    public void btnZeroClicked(View view) {
        String value = display.getText().toString();
        display.setText(value+"0");
    }
    public void btnPointClicked(View view) {
        String value = display.getText().toString();
        display.setText(value+".");
    }
    public void btnClearOneClicked(View view) {
        if(!display.getText().toString().equals("")) {
            StringBuilder str = new StringBuilder(display.getText().toString());
            display.setText(str.deleteCharAt(str.length() - 1));
        } else{
            Toast.makeText(getApplicationContext(),"Nothing to clear", Toast.LENGTH_SHORT).show();
        }
    }
}