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
import java.util.*;

import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FoodStockActivity extends AppCompatActivity {
    private TextView mOrderTextView;
    private Button mSubmitButton;

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
                Intent intent = new Intent(FoodStockActivity.this, OrderHistoryActivity.class);
                startActivity(intent);
                return true;
            case R.id.login:
                // Handle "Order History" menu item click
                intent = new Intent(FoodStockActivity.this, LoginActivity.class);
                startActivity(intent);
                return true;
            case R.id.new_user:
                // Handle "Order History" menu item click
                intent = new Intent(FoodStockActivity.this, NewUserActivity.class);
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

        Intent intent = getIntent();
        String listID = intent.getStringExtra("listID");

        mOrderTextView = findViewById(R.id.order_text_view);
        mSubmitButton = findViewById(R.id.submit_button);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
    private void runInventory(String listID) {
        String url = "https://yourdomain.com/run_inventory.php?listID=" + listID;
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            List<String> orderList = new ArrayList<String>();

                            JSONArray jsonArray = new JSONArray(response);

                            // Use a loop to iterate through the results returned by the PHP script
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                // Create a text field for each food item to get the current stock from the user
                                String itemName = jsonObject.getString("itemName");
                                String caseName = jsonObject.getString("caseName");
                                int requiredStock = jsonObject.getInt("reqStock");
                                int stockPerCase = jsonObject.getInt("perCase");
                                int isCase = jsonObject.getInt("isCase");

                                EditText stockEditText = new EditText(getApplicationContext());
                                stockEditText.setHint("Enter current stock for " + itemName);

                                // Add the stockEditText to the appropriate layout

                                // Use a method to calculate the amount to order
                                int currentStock = Integer.parseInt(stockEditText.getText().toString());
                                int casesToOrder = 0;
                                if (isCase == 1) {
                                    casesToOrder = (requiredStock - currentStock) / stockPerCase;
                                    System.out.println("Order " + casesToOrder + " cases of " + itemName); }
                                else {
                                    casesToOrder = (requiredStock - currentStock);
                                    System.out.println(itemName + " " + casesToOrder + " " + caseName);

                                }


                                // Print out the list of items to order for the user
                                if (casesToOrder > 0) {
                                    String orderText = casesToOrder + caseName;
                                    if (casesToOrder > 1) {
                                        orderText += "s";
                                    }
                                    orderText += " of " + itemName;
                                    // Add the orderText to the appropriate layout
                                }
                            }
                            if (orderList.size() > 0) {
                                StringBuilder orderText = new StringBuilder();
                                for (String item : orderList) {
                                    orderText.append(item + "\n");
                                }
                                mOrderTextView.setText("Delco Order: \n" + orderText.toString());
                                // Connect to MySQL and submit order
                                submitOrder(orderText.toString());
                            } else {
                                mOrderTextView.setText("No items to order.");
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
            }
        });
        queue.add(stringRequest);
    }



}
