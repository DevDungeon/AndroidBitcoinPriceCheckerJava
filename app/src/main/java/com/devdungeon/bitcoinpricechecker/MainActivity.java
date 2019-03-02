package com.devdungeon.bitcoinpricechecker;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private TextView mainText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        this.mainText = findViewById(R.id.mainText);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.checkButton);






        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getBitcoinPrice();

                Snackbar.make(view, "Updating Bitcoin price now...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void getBitcoinPrice() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://api.coindesk.com/v1/bpi/currentprice/BTC.json";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String price = "N/A";
                        try {
                            JSONObject json = new JSONObject(response);
                            // ['bpi']['USD']['rate']
                            price = json.getJSONObject("bpi").getJSONObject("USD").getString("rate");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mainText.setText(getString(R.string.bitcoin_price, price));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mainText.setText(getString(R.string.http_error_message));
            }
        });
        queue.add(stringRequest);
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
}
