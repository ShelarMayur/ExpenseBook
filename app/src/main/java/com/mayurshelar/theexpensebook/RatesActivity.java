package com.mayurshelar.theexpensebook;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RatesActivity extends AppCompatActivity {

    private EditText editTextAmount;
    private Spinner spinnerFromCurrency;
    private Spinner spinnerToCurrency;
    private Button buttonConvert;
    private TextView textViewResult;

    private static final String API_KEY = "YOUR_OPEN_EXCHANGE_RATES_API_KEY";
    private static final String BASE_URL = "https://open.er-api.com/v6/latest/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rates);

        editTextAmount = findViewById(R.id.editTextAmount);
        spinnerFromCurrency = findViewById(R.id.spinnerFromCurrency);
        spinnerToCurrency = findViewById(R.id.spinnerToCurrency);
        buttonConvert = findViewById(R.id.buttonConvert);
        textViewResult = findViewById(R.id.textViewResult);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.currencies, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerFromCurrency.setAdapter(adapter);
        spinnerToCurrency.setAdapter(adapter);

        buttonConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                convertCurrency();
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

            }
        });
    }

    private void convertCurrency() {
        String fromCurrency = spinnerFromCurrency.getSelectedItem().toString();
        String toCurrency = spinnerToCurrency.getSelectedItem().toString();
        String amount = editTextAmount.getText().toString();

        if (amount.isEmpty()) {
            textViewResult.setText("Please enter an amount.");
            return;
        }

        String url = BASE_URL + fromCurrency;

        new FetchExchangeRateTask().execute(url, toCurrency, amount);
    }

    private class FetchExchangeRateTask extends AsyncTask<String, Void, Double> {

        @Override
        protected Double doInBackground(String... params) {
            String urlString = params[0];
            String toCurrency = params[1];
            String amount = params[2];

            try {
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                try {
                    InputStream in = urlConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    StringBuilder stringBuilder = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }

                    JSONObject jsonObject = new JSONObject(stringBuilder.toString());
                    JSONObject rates = jsonObject.getJSONObject("rates");
                    return rates.getDouble(toCurrency) * Double.parseDouble(amount);

                } finally {
                    urlConnection.disconnect();
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return null;
            }
        }



        @Override
        protected void onPostExecute(Double result) {
            if (result != null) {
                textViewResult.setText(String.format("%s %s = %.2f %s",
                        editTextAmount.getText().toString(),
                        spinnerFromCurrency.getSelectedItem().toString(),
                        result,
                        spinnerToCurrency.getSelectedItem().toString()));
            } else {
                textViewResult.setText("Error fetching exchange rate.");
            }
        }
    }
}
