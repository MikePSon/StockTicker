package com.example.mikepatterson.stockticker;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.*;
import android.view.inputmethod.*;
import android.widget.*;
import android.widget.TextView.*;
import android.view.inputmethod.InputMethodManager;
import android.util.Log;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends ActionBarActivity {
    private EditText enteredVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        String shownName;
        String shownPrice;
        String shownLastTime;
        String shownRange;
        String shownChange;
    }

    public boolean isValid(String symbol) {
        String special = "!@#$%^&*()_./";
        boolean found = false;
        for (int i=0; i<special.length(); i++) {
            if (symbol.indexOf(special.charAt(i)) > 0) {
                found = true;
                break;
            }
        }
        return found;
    }
    public boolean hasSpaceFunc(String symbol) {
        Integer hasSpace = symbol.indexOf(' ');
        if(hasSpace >= 1){
            return true;
        }else {
            return false;
        }
    }
    public void init() {
        EditText enteredSymbol = (EditText) findViewById(R.id.tickerEntry);
        enteredSymbol.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    enteredVal = (EditText) findViewById(R.id.tickerEntry);
                    String thisSymbol = enteredVal.getText().toString();
                    Boolean hasSpace = hasSpaceFunc(thisSymbol);
                    Boolean hasSpecial = isValid(thisSymbol);
                    if(!hasSpace && !hasSpecial) {

//                    String thisSymbol = "GOOG";
                        myStockInfo newLookup = new myStockInfo();

                        newLookup.execute(thisSymbol);
                    }else{
                        Context context = getApplicationContext();
                        CharSequence text = "Invalid Ticker";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                    return false;
                }
                return false;
            }
        });
    }

    private class myStockInfo extends AsyncTask<String, Void, Stock> {
        TextView lastPrice = (TextView)findViewById(R.id.lasttrade);
        TextView lastTime = (TextView)findViewById(R.id.lasttime);
        TextView stockName = (TextView)findViewById(R.id.yourcompany);
        TextView change = (TextView)findViewById(R.id.change);
        TextView range = (TextView)findViewById(R.id.range);


        protected void onPreExecute(){
            setProgress(0);
        }
        @Override
        protected Stock doInBackground(String... symbols) {
            String symbol = symbols[0];
            Stock thisStock = new Stock(symbol);
//            thisStock.load();
            //Unhandled Exceptions, MalformedURL, IOException
            //Tried wrapping in try/catch stmt (below), app shows no errors but code does not run
            try{
                thisStock.load();
            }
            catch (MalformedURLException e1) {e1.printStackTrace();}
            catch (IOException e2) {e2.printStackTrace();}

            return thisStock;
        }

        protected void onProgressUpdate(Integer... progress) {
            setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(Stock result) {
            String lastPriceData = result.getLastTradePrice().toString();
            String companyName = result.getName().toString();
            String tradingRange = result.getRange().toString();
            String tradingChange = result.getChange().toString();
            String lastTradeTime = result.getLastTradeTime().toString();
            lastPrice.setText(lastPriceData);
            stockName.setText(companyName);
            range.setText(tradingRange);
            change.setText(tradingChange);
            lastTime.setText(lastTradeTime);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    static final String tickerText = "Ticker";
    static final String nameText = "Name";
    static final String priceText = "Price";
    static final String lastTimeText = "Last Trade Time";
    static final String changeText = "Change";
    static final String rangeText = "Range";

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        TextView rangeTextField = (TextView)findViewById(R.id.range);
        String range = rangeTextField.getText().toString();
        savedInstanceState.putString(rangeText, range);

        TextView lastTimeTextField = (TextView)findViewById(R.id.lasttime);
        String lastTrade = lastTimeTextField.getText().toString();
        savedInstanceState.putString(lastTimeText, lastTrade);

        TextView nameTextField = (TextView)findViewById(R.id.yourcompany);
        String name = nameTextField.getText().toString();
        savedInstanceState.putString(nameText, name);

        TextView priceTextField = (TextView)findViewById(R.id.lasttrade);
        String price = priceTextField.getText().toString();
        savedInstanceState.putString(priceText, price);

        TextView changeTextField = (TextView)findViewById(R.id.change);
        String change = changeTextField.getText().toString();
        savedInstanceState.putString(changeText, change);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState); // Always call the superclass first

        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore value of members from saved state

            TextView nameTextField = (TextView)findViewById(R.id.yourcompany);
            String compName = savedInstanceState.getString(nameText);
            nameTextField.setText(compName);

            TextView priceTextField = (TextView)findViewById(R.id.lasttrade);
            String yourPrice = savedInstanceState.getString(priceText);
            priceTextField.setText(yourPrice);

            TextView lastTimeTextField = (TextView)findViewById(R.id.lasttime);
            String lastTime = savedInstanceState.getString(lastTimeText);
            lastTimeTextField.setText(lastTime);

            TextView changeTextField = (TextView)findViewById(R.id.change);
            String change = savedInstanceState.getString(changeText);
            changeTextField.setText(change);

            TextView rangeTextField = (TextView)findViewById(R.id.range);
            String range = savedInstanceState.getString(rangeText);
            rangeTextField.setText(range);
        }
    }




}
