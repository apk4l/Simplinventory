package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;
import android.app.Activity;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.*;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListsActivity extends AppCompatActivity {

    private ArrayList<ListItem> mListItems;
    private ListView mListView;

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                // Show a dialog to get the new list name from the user
                showNewListDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);
        setTitle("Your Lists");
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
                String listName = listItem.getListName();

                // Launch the InventoryActivity with the corresponding listID
                Intent intent = new Intent(ListsActivity.this, InventoryActivity.class);
                intent.putExtra("listID", listId);
                intent.putExtra("listName", listName);
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
        public String getListName() {
            return listName;
        }
        @Override
        public String toString() {
            return listName;
        }
    }
    private void showNewListDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New List");
        final EditText input = new EditText(this);
        builder.setView(input);
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String listName = input.getText().toString();
                if (!listName.isEmpty()) {
                    createNewList(listName);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
    private void createNewList(String listName) {
        String url = "https://kentzysk.com/androidinv/new_list.php";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            boolean success = json.getBoolean("success");
                            if (success) {
                                // Refresh the list view
                                refreshList();
                            } else {
                                Toast.makeText(ListsActivity.this, "Error creating new list", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(ListsActivity.this, "Error parsing JSON response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ListsActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userID", getUserId());
                params.put("listName", listName);
                return params;
            }
        };
        queue.add(request);
    }
    private void refreshList() {
        mListItems.clear();
        String url = "https://kentzysk.com/androidinv/get_lists.php?userID=" + getUserId();
        JsonArrayRequest request = new JsonArrayRequest(url, response -> {
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
            ArrayAdapter<ListItem> adapter = new ArrayAdapter<>(ListsActivity.this,
                    android.R.layout.simple_list_item_1, mListItems);
            mListView.setAdapter(adapter);
        }, error -> Toast.makeText(ListsActivity.this, "Error loading lists", Toast.LENGTH_SHORT).show());
        Volley.newRequestQueue(this).add(request);
    }
}