package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.ClipboardManager;
import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FoodStockActivityOLD extends AppCompatActivity {
    private EditText mDoughEditText;
    private EditText mCheeseEditText;
    private EditText mSauceEditText;
    private EditText mChickenEditText;
    private TextView mOrderTextView;
    private Button mSubmitButton;

    // Required stock
    private static final int REQUIRED_DOUGH = 90;
    private static final int DOUGH_PER_CASE = 30;
    private static final int REQUIRED_CHEESE = 12;
    private static final int CHEESE_PER_CASE = 4;
    private static final int REQUIRED_SAUCE = 4;
    private static final int SAUCE_PER_CASE = 6;
    private static final int REQUIRED_CHICKEN = 4;
    private static final int CHICKEN_PER_CASE = 6;


    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.navigation.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_stock:
                // Handle "Food Stock" menu item click
                return true;
            case R.id.menu_history:
                // Handle "Order History" menu item click
                Intent intent = new Intent(FoodStockActivityOLD.this, OrderHistoryActivity.class);
                startActivity(intent);
                return true;
            case R.id.login:
                // Handle "Order History" menu item click
                intent = new Intent(FoodStockActivityOLD.this, LoginActivity.class);
                startActivity(intent);
                return true;
            case R.id.new_user:
                // Handle "Order History" menu item click
                intent = new Intent(FoodStockActivityOLD.this, NewUserActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_stock);
        mOrderTextView = findViewById(R.id.order_text_view);
        mSubmitButton = findViewById(R.id.submit_button);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get current stock
                int currentDough = Integer.parseInt(mDoughEditText.getText().toString());
                int currentCheese = Integer.parseInt(mCheeseEditText.getText().toString());
                int currentSauce = Integer.parseInt(mSauceEditText.getText().toString());
                int currentChicken = Integer.parseInt(mChickenEditText.getText().toString());


                // Compare current stock to required stock
                StringBuilder orders = new StringBuilder();
                if (currentDough < REQUIRED_DOUGH) {
                    int cases = (REQUIRED_DOUGH - currentDough) / DOUGH_PER_CASE;
                    if ((REQUIRED_DOUGH - currentDough) % DOUGH_PER_CASE != 0) {
                        cases++;
                    }
                    orders.append(cases + " case dough \n");
                }
                if (currentCheese < REQUIRED_CHEESE) {
                    int cases = (REQUIRED_CHEESE - currentCheese) / CHEESE_PER_CASE;
                    if ((REQUIRED_CHEESE - currentCheese) % CHEESE_PER_CASE != 0) {
                        cases++;
                    }
                    orders.append(cases + " case cheese \n");
                }
                if (currentSauce < REQUIRED_SAUCE) {
                    int cases = (REQUIRED_SAUCE - currentSauce) / SAUCE_PER_CASE;
                    if ((REQUIRED_SAUCE - currentSauce) % SAUCE_PER_CASE != 0) {
                        cases++;
                    }
                    orders.append(cases + " case sauce \n");
                }
                if (currentChicken < REQUIRED_CHICKEN) {
                    int cases = (REQUIRED_CHICKEN - currentChicken) / CHICKEN_PER_CASE;
                    if ((REQUIRED_CHICKEN - currentChicken) % CHICKEN_PER_CASE != 0) {
                        cases++;
                    }
                    orders.append(cases + " case chicken \n");
                }


                Button copyButton = findViewById(R.id.copy_button);
                EditText editText = findViewById(R.id.order_text_view);
                copyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // code to copy text from EditText goes here
                        String text = editText.getText().toString();
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        clipboard.setText(text);

                    }
                });


                // Display list of items to order
                if (orders.length() > 0) {
                    orders.setLength(orders.length() - 2);
                    mOrderTextView.setText("Delco Order: \n" + orders.toString());
                    // Connect to MySQL and submit order
                    submitOrder(orders.toString());
                } else {
                    mOrderTextView.setText("No items to order.");
                }


            }


        });
    }


    private void submitOrder(final String orders) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://inv.thealleypub.com/androidinv.php";
        // Get the current date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        final String currentDate = sdf.format(new Date());

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the response
                        mOrderTextView.setText("Order submitted: \n" + orders);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
                mOrderTextView.setText("Error submitting order: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("orderlist", orders.toString());
                params.put("date", currentDate);
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
