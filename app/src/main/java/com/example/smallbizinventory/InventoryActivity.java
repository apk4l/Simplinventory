package com.example.smallbizinventory;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smallbizinventory.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InventoryActivity extends AppCompatActivity {

    private LinearLayout mContainer;
    private ArrayList<InventoryItem> mInventoryItems;
    private TextView mOrderTextView;
    private int listID;

    private int userID;
    private String listName;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.inventory_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        // Enable the back arrow in the title bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listName = getIntent().getStringExtra("listName");
        setTitle("Inventory: " + listName);
        mContainer = findViewById(R.id.container);
        mOrderTextView = findViewById(R.id.order_text_view);


        // Get the listID from the intent
        listID = getIntent().getIntExtra("listID", -1);
        userID = getIntent().getIntExtra("userID", -1);
        // Check if the listID is valid
        if (listID == -1) {
            Toast.makeText(this, "Invalid listID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        Button copyButton = findViewById(R.id.copy_button);
        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // code to copy text from TextView goes here
                String text = mOrderTextView.getText().toString();
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setText(text);
                Toast.makeText(InventoryActivity.this, "Text copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });
        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the text from the TextView
                String orderList = mOrderTextView.getText().toString();

// Get the user ID and list ID from wherever you have stored them
                String userID = getIntent().getStringExtra("userID");
                int listID = getIntent().getIntExtra("listID", 0);

// Create a request
                String url = "https://kentzysk.com/androidinv/save_order.php";
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                StringRequest request = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Handle the response from the server
                                Log.d("Volley", "Response: " + response);
                                Toast.makeText(InventoryActivity.this, "Order saved", Toast.LENGTH_SHORT).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Handle the error
                                Log.e("Volley", "Error: " + error.getMessage());
                                Toast.makeText(InventoryActivity.this, "Error saving order", Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("userID", userID);
                        params.put("listID", String.valueOf(listID));
                        params.put("orderList", orderList);
                        return params;
                    }
                };
                queue.add(request);
            }
        });
        // Make a request to the PHP script using Volley
        LinearLayout mainLayout = new LinearLayout(InventoryActivity.this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);

        String url = "https://kentzysk.com/androidinv/run_inventory.php?listID=" + listID;
        JsonArrayRequest request = new JsonArrayRequest(url, response -> {

            // Loop through the items and create a text field and an edit text field for each one
            mInventoryItems = new ArrayList<>();
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject item = response.getJSONObject(i);
                    int itemID = item.getInt("itemID");
                    String itemName = item.getString("itemName");
                    int isCase = item.getInt("isCase");
                    int reqStock = item.getInt("reqStock");
                    int perCase = item.getInt("perCase");
                    String caseName = item.getString("caseName");
                    String note = item.getString("note");

                    // Create a new linear layout to hold the text and edit text fields
                    LinearLayout itemLayout = new LinearLayout(InventoryActivity.this);
                    itemLayout.setOrientation(LinearLayout.VERTICAL);
                    itemLayout.setGravity(Gravity.CENTER_HORIZONTAL);

                    // Create a new text field and add it to the linear layout
                    TextView textField = new TextView(InventoryActivity.this);
                    textField.setText(itemName);
                    textField.setGravity(Gravity.CENTER_HORIZONTAL);
                    itemLayout.addView(textField);

                    // Create a new text field for the note
                    if (note.length() > 1) {
                        TextView noteField = new TextView(InventoryActivity.this);
                        noteField.setText("(" + note + ")");
                        noteField.setTextSize(11);
                        noteField.setGravity(Gravity.CENTER_HORIZONTAL);
                        itemLayout.addView(noteField);
                    }

                    // Create a new edit text field and add it to the linear layout
                    EditText editText = new EditText(InventoryActivity.this);
                    editText.setTag(itemName);
                    editText.setMaxLines(1);
                    editText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    editText.setGravity(Gravity.CENTER); // Set gravity to center
                    editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                                // Move focus to the next EditText field
                                View nextEditText = v.focusSearch(View.FOCUS_DOWN);
                                if (nextEditText != null) {
                                    nextEditText.requestFocus();
                                }
                                return true;
                            }
                            return false;
                        }
                    });
                    itemLayout.addView(editText);

                    // Set the layout parameters for the text view and edit text
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
                    textField.setLayoutParams(layoutParams);
                    editText.setLayoutParams(layoutParams);

                    // Create a new inventory item and add it to the list
                    InventoryItem inventoryItem = new InventoryItem(itemName, isCase, reqStock, perCase, caseName, itemID);
                    mInventoryItems.add(inventoryItem);

                    // Add the linear layout to the main layout
                    mainLayout.addView(itemLayout);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            // Create the "Calculate Order" button and add it to the main layout
            Button calculateOrderButton = new Button(InventoryActivity.this);
            calculateOrderButton.setText("Calculate Order");
            calculateOrderButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    calculateOrder(v);
                }
            });
            mainLayout.addView(calculateOrderButton);
        }, error -> Toast.makeText(InventoryActivity.this, "Error loading items", Toast.LENGTH_SHORT).show());

        // Add the request to the Volley request queue
        Volley.newRequestQueue(this).add(request);

        // Add the main layout to the container
        mContainer.addView(mainLayout);
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
            //    if (currentStock <= 0) {
            //      continue;
            // }

            int orderAmount = 0;
            double caseOrder = 0;
            if (item.isCase() == 1) {
                if (currentStock < item.getReqStock()) {
                    caseOrder = (item.getReqStock() - currentStock) / item.getPerCase();
                    if ((item.getReqStock() - currentStock) % item.getPerCase() != 0) {
                        caseOrder++;
                    }
                    orderAmount = (int) caseOrder;
                }
            } else {

                if (currentStock < item.getReqStock()) {
                    caseOrder = (item.getReqStock() - currentStock) / 1;
                    if ((item.getReqStock() - currentStock) % item.getPerCase() != 0) {
                        caseOrder++;
                    }
                    orderAmount = (int) caseOrder;
                }
            }
            // Add the order amount to the list
            if (orderAmount > 0) {
                String orderItem = item.getItemName() + ": " + orderAmount + " " + item.getCaseName();
                orderList.add(orderItem);
            }
            orderAmount = 0;
        }
        ScrollView scrollView = findViewById(R.id.scroll_view);
        scrollView.smoothScrollTo(0, 0);
        // Display the order list in the order TextView
        if (orderList.size() > 0) {
            String message = TextUtils.join("\n", orderList);
            mOrderTextView.setText(message);  // Update the TextView with the order list
            findViewById(R.id.copy_button).setVisibility(View.VISIBLE); // Show the copy button
            findViewById(R.id.save_button).setVisibility(View.VISIBLE); // Show the copy button
            mContainer.scrollTo(0, 0); // Scroll to the top of the container
        } else {
            Toast.makeText(this, "No items to order", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_history:
                // Launch the profile activity
                Intent intent = new Intent(InventoryActivity.this, OrdersByListActivity.class);
                intent.putExtra("listID", String.valueOf(listID));
                intent.putExtra("listName",  String.valueOf(listName));
                startActivity(intent);
                return true;
            case R.id.menu_edit:
                // Launch the profile activity
                Intent intent2 = new Intent(InventoryActivity.this, EditItemsActivity.class);
                intent2.putExtra("listID", listID);
                intent2.putExtra("listName",  String.valueOf(listName));
                startActivity(intent2);
                return true;
            case R.id.menu_help:
                // Launch the profile activity
                Intent intents = new Intent(InventoryActivity.this, HelpActivity.class);
                startActivity(intents);
                return true;
            case android.R.id.home:
                // Handle the back arrow click
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

