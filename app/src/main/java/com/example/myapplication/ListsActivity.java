package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListsActivity extends AppCompatActivity {

    private ArrayList<ListItem> mListItems;
    private ListView mListView;
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
                Intent intent = new Intent(ListsActivity.this, OrderHistoryActivity.class);
                startActivity(intent);
                return true;
            case R.id.login:
                // Handle "Order History" menu item click
                intent = new Intent(ListsActivity.this, LoginActivity.class);
                startActivity(intent);
                return true;
            case R.id.new_user:
                // Handle "Order History" menu item click
                intent = new Intent(ListsActivity.this, NewUserActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);

        mListItems = new ArrayList<>();
        mListView = findViewById(R.id.list_view);

        // Make a request to the PHP script using Volley
        String url = "https://kentzysk.com/androidinv/get_lists.php?userID=" + getUserId();
        JsonArrayRequest request = new JsonArrayRequest(url, response -> {

            // Loop through the lists and create a ListItem object for each one
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject list = response.getJSONObject(i);
                    int listId = list.getInt("listID");
                    String listName = list.getString("listName");

                    ListItem listItem = new ListItem(listId, listName);
                    mListItems.add(listItem);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            // Create an array adapter and set it to the list view
            ArrayAdapter<ListItem> adapter = new ArrayAdapter<>(ListsActivity.this,
                    android.R.layout.simple_list_item_1, mListItems);
            mListView.setAdapter(adapter);

            // Set an onItemClickListener for the list view
            mListView.setOnItemClickListener((parent, view, position, id) -> {
                ListItem listItem = (ListItem) parent.getItemAtPosition(position);
                int listId = listItem.getListId();

                // Launch the InventoryActivity with the corresponding listID
                Intent intent = new Intent(ListsActivity.this, InventoryActivity.class);
                intent.putExtra("listID", listId);
                startActivity(intent);
            });
        }, error -> Toast.makeText(ListsActivity.this, "Error loading lists", Toast.LENGTH_SHORT).show());

        // Add the request to the Volley request queue
        Volley.newRequestQueue(this).add(request);
    }

    // Get the userID from the SharedPreferences
    private String getUserId() {
        return getSharedPreferences("MyPrefs", MODE_PRIVATE)
                .getString("userID", "");
    }

    // Inner class for the list item
    private static class ListItem {
        private int listId;
        private String listName;

        public ListItem(int listId, String listName) {
            this.listId = listId;
            this.listName = listName;
        }

        public int getListId() {
            return listId;
        }

        @Override
        public String toString() {
            return listName;
        }
    }
}