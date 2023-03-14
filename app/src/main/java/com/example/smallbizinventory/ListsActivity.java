package com.example.smallbizinventory;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.*;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListsActivity extends AppCompatActivity {

    private ArrayList<ListItem> mListItems;
    private ListView mListView;
    private int mLongPressedListId;

    private String mLongPressedListName;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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

            case R.id.menu_user:
                // Launch the profile activity
                Intent intent = new Intent(ListsActivity.this, UserActivity.class);
                intent.putExtra("userID", getUserId());
                startActivity(intent);
                return true;
            case R.id.menu_help:
                // Launch the profile activity
                Intent intents = new Intent(ListsActivity.this, HelpActivity.class);
                startActivity(intents);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);
        getSupportActionBar().setDisplayShowTitleEnabled(false); // Hide the title text
        getSupportActionBar().setDisplayUseLogoEnabled(true); // Show the logo
        getSupportActionBar().setLogo(R.drawable.logo); // Set the logo image
        setTitle("Small Biz Inventory");
        mListItems = new ArrayList<>();
        mListView = findViewById(R.id.list_view);

        // Register the ListView for a context menu
        registerForContextMenu(mListView);

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
                    R.layout.list_item, mListItems);
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
    String getUserId() {
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

    // Inflate the context menu when the user long presses on a list item

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.list_view) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.list_item_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        ListItem listItem = (ListItem) mListView.getItemAtPosition(info.position);
        mLongPressedListId = listItem.getListId();
        mLongPressedListName = listItem.getListName();


        switch (item.getItemId()) {
            case R.id.menu_edit:
                Intent intent = new Intent(ListsActivity.this, EditItemsActivity.class);
                intent.putExtra("listID", mLongPressedListId);
                intent.putExtra("listName", mLongPressedListName);
                startActivity(intent);
                return true;
            case R.id.menu_delete:
                new AlertDialog.Builder(ListsActivity.this)
                        .setTitle("Delete Item")
                        .setMessage("Are you sure you want to delete this item?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Call deleteItem() with the selected itemID
                                deleteList(mLongPressedListId);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
                return true;
            default:
                return super.onContextItemSelected(item);
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
    private void deleteList(int listID) {
        String url = "https://kentzysk.com/androidinv/delete_list.php?listID=" + listID;
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            boolean success = json.getBoolean("success");
                            if (success) {
                                Intent intent = new Intent(ListsActivity.this, ListsActivity.class);
                                startActivity(intent);
                                finish(); // finish the current activity to prevent stacking of activities
                            } else {
                                Toast.makeText(ListsActivity.this, "Error deleting list", Toast.LENGTH_SHORT).show();
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
                });
        queue.add(request);
    }
}
