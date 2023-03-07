package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
        getList(listID);

        mOrderTextView = findViewById(R.id.order_text_view);
        mSubmitButton = findViewById(R.id.submit_button);
        // Get the container LinearLayout that holds the EditText fields
        LinearLayout container = findViewById(R.id.edittext_container);

        // Create a StringBuilder object to store the order text
        StringBuilder orderText = new StringBuilder();
        // Loop through all the EditText fields and add their text to the order text
        for (int i = 0; i < container.getChildCount(); i++) {
            View child = container.getChildAt(i);
            if (child instanceof EditText) {
                EditText editText = (EditText) child;
                String stockText = editText.getText().toString();
                String itemName = editText.getHint().toString().replace("Enter current stock for ", "");
                orderText.append(itemName + ": " + stockText + "\n");
            }
        }
        mSubmitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                // Display the order text in the order text view
                mOrderTextView.setText("Delco Order: \n" + orderText.toString());

                // Connect to MySQL and submit order
                submitOrder(orderText.toString());
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
        String url = "https://kentzysk.com/androidinv/run_inventory.php?listID=" + listID;
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            LinearLayout container = findViewById(R.id.edittext_container);
                            createEditTextFields(container, jsonArray);

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


    private void getList(String listID) {
        listID = "2";
        String url = "https://kentzysk.com/androidinv/run_inventory.php?listID=" + listID;
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            // Use a loop to iterate through the results returned by the PHP script
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                // Create a text field for each food item to get the current stock from the user
                                String itemName = jsonObject.getString("itemName");
                                EditText stockEditText = new EditText(getApplicationContext());
                                stockEditText.setHint("Enter current stock for " + itemName);
                                stockEditText.setText("0");

                                // Add the EditText field to the container LinearLayout
                                LinearLayout container = findViewById(R.id.edittext_container);
                                container.addView(stockEditText);
                            }

                            // Call createEditTextFields method to create and add EditText fields to the layout

                            // Move the code that submits the order to the onClick method of the submit button
                            // if (orderList.size() > 0) {
                            //     StringBuilder orderText = new StringBuilder();
                            //     for (String item : orderList) {
                            //         orderText.append(item + "\n");
                            //     }
                            //     mOrderTextView.setText("Delco Order: \n" + orderText.toString());
                            //     // Connect to MySQL and submit order
                            //     submitOrder(orderText.toString());
                            // } else {
                            //     mOrderTextView.setText("No items to order.");
                            // }
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

    private void createEditTextFields(LinearLayout container, JSONArray jsonArray) throws JSONException {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String itemName = jsonObject.getString("itemName");
            EditText editText = new EditText(this);
            editText.setHint("Enter current stock for " + itemName);
            editText.setText("0");
            container.addView(editText);
        }
    }
}


