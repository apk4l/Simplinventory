package com.example.smallbizinventory;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ReOrderItemsActivity extends AppCompatActivity {

    private ArrayList<InventoryItem> mInventoryItems;
    private RecyclerView mRecyclerView;
    private InventoryItemAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reorder_items_activity);
        // Enable the back arrow in the title bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Get the listID from the intent
        int listID = getIntent().getIntExtra("listID", -1);
        String listName = getIntent().getStringExtra("listName");
        setTitle("Reorder Items: " + listName);

        // Check if the listID is valid
        if (listID == -1) {
            Toast.makeText(this, "Invalid listID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Make a request to the PHP script using Volley to get the items
        String url = "https://kentzysk.com/androidinv/run_inventory.php?listID=" + listID;
        JsonArrayRequest request = new JsonArrayRequest(url, response -> {
            // Loop through the items and create an inventory item for each one
            mInventoryItems = new ArrayList<>();
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject item = response.getJSONObject(i);
                    int itemID = item.getInt("itemID");
                    String itemName = item.getString("itemName");
                    int itemOrder = item.getInt("itemOrder");
                    int isCase = item.getInt("isCase");
                    int reqStock = item.getInt("reqStock");
                    int perCase = item.getInt("perCase");
                    String caseName = item.getString("caseName");
                    InventoryItem inventoryItem = new InventoryItem(itemName, isCase, reqStock, perCase, caseName, itemID);
                    mInventoryItems.add(inventoryItem);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            // Set up the recycler view with the adapter
            mRecyclerView = findViewById(R.id.recycler_view);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mAdapter = new InventoryItemAdapter(mInventoryItems);
            mRecyclerView.setAdapter(mAdapter);

        }, error -> Toast.makeText(ReOrderItemsActivity.this, "Error loading items", Toast.LENGTH_SHORT).show());

        // Add the request to the Volley request queue
        Volley.newRequestQueue(this).add(request);

        // Set up the save button
        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(view -> {

            // Update the item order for each inventory item based on its position in the list view
            for (int i = 0; i < mInventoryItems.size(); i++) {
                InventoryItem item = mInventoryItems.get(i);
                item.setItemOrder(i + 1);
            }

            // Make a request to the PHP script using Volley to update the item orders in the database
            String updateUrl = "https://kentzysk.com/androidinv/update_item_orders.php";
            RequestQueue queue = Volley.newRequestQueue(ReOrderItemsActivity.this);
            StringRequest updateRequest = new StringRequest(Request.Method.POST, updateUrl,
                    response -> {
                        // Show a success message and finish the activity
                        Toast.makeText(ReOrderItemsActivity.this, "Items reordered successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    },
                    error -> {
                        // Show an error message
                        Toast.makeText(ReOrderItemsActivity.this, "Error updating item orders", Toast.LENGTH_SHORT).show();
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    // Create a map of inventory item IDs to their new item orders
                    Map<String, String> params = new HashMap<>();
                    for (InventoryItem item : mInventoryItems) {
                        params.put(String.valueOf(item.getItemID()), String.valueOf(item.getItemOrder()));
                    }
                    // Add the itemOrders parameter to the map
                    params.put("itemOrders", new JSONObject(params).toString());
                    Log.d("ReOrderItemsActivity", "JSON being sent: " + new JSONObject(params).toString());
                    return params;
                }
            };
              queue.add(updateRequest);
        });

// Set up the touch helper for drag and drop functionality
        ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {

            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }

            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();

                // Swap the items in your ArrayList
                Collections.swap(mInventoryItems, fromPosition, toPosition);

                // Notify the adapter of the item move
                mAdapter.notifyItemMoved(fromPosition, toPosition);

                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // Not implemented
            }
        };

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Handle the back arrow click
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}