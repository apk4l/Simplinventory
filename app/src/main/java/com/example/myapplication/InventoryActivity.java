package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class InventoryActivity extends AppCompatActivity {

    private LinearLayout mContainer;
    private ArrayList<InventoryItem> mInventoryItems;
    private TextView mOrderTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        mContainer = findViewById(R.id.container);
        mOrderTextView = findViewById(R.id.order_text_view);
        // Get a reference to the calculate order button
        Button calculateOrderButton = findViewById(R.id.calculate_button);

        // Set the click listener for the button
        calculateOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateOrder(v);
            }
        });

        // Get the listID parameter from the intent
        // String listID = getIntent().getStringExtra("listID");

        // Make a request to the PHP script using Volley
        String url = "https://kentzysk.com/androidinv/run_inventory.php?listID=2";
        JsonArrayRequest request = new JsonArrayRequest(url,
                response -> {
                    // Loop through the items and create a text field and an edit text field for each one
                    mInventoryItems = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject item = response.getJSONObject(i);
                            String itemName = item.getString("itemName");
                            int isCase = item.getInt("isCase");
                            int reqStock = item.getInt("reqStock");
                            int perCase = item.getInt("perCase");
                            String caseName = item.getString("caseName");

// Create a new linear layout to hold the text and edit text fields
                            LinearLayout itemLayout = new LinearLayout(InventoryActivity.this);
                            itemLayout.setOrientation(LinearLayout.HORIZONTAL);

// Create a new text field and add it to the linear layout
                            TextView textField = new TextView(InventoryActivity.this);
                            textField.setText(itemName);
                            itemLayout.addView(textField);

// Create a new edit text field and add it to the linear layout
                            EditText editText = new EditText(InventoryActivity.this);
                            editText.setTag(itemName);
                            itemLayout.addView(editText);

                            // Create a new inventory item and add it to the list
                            InventoryItem inventoryItem = new InventoryItem(itemName, isCase, reqStock, perCase, caseName);
                            mInventoryItems.add(inventoryItem);

                            // Add the linear layout to the container
                            mContainer.addView(itemLayout);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                error -> Toast.makeText(InventoryActivity.this, "Error loading items", Toast.LENGTH_SHORT).show());

        // Add the request to the Volley request queue
        Volley.newRequestQueue(this).add(request);
    }

    public void calculateOrder(View view) {
        // Loop through the inventory items and calculate the order amounts
        ArrayList<String> orderList = new ArrayList<>();
        for (InventoryItem item : mInventoryItems) {
            // Get the edit text field for the item
            EditText editText = mContainer.findViewWithTag(item.getItemName());

            // Check if the EditText view is null
            if (editText == null) {
                continue;
            }

            // Get the user input and calculate the order amount
            String inputText = editText.getText().toString();
            if (TextUtils.isEmpty(inputText)) {
                continue;
            }

            int currentStock = Integer.parseInt(inputText);
            if (currentStock <= 0) {
                continue;
            }

            int orderAmount = item.getReqStock() - currentStock;

            // Add the order amount to the list
            if (orderAmount > 0) {
                String orderItem = "Order amount " + item.getCaseName() + ": " + orderAmount;
                orderList.add(orderItem);
            }
        }

        // Display the order list in the order TextView
        if (orderList.size() > 0) {
            String message = TextUtils.join("\n", orderList);
            mOrderTextView.setText(message);  // Update the TextView with the order list
        } else {
            Toast.makeText(this, "No items to order", Toast.LENGTH_SHORT).show();
        }
    }

}