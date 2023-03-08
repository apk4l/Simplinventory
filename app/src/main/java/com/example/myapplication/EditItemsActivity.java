package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.*;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.InventoryItem;
import com.example.myapplication.R;
import org.json.JSONException;
import org.json.JSONObject;
import android.text.InputType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditItemsActivity extends AppCompatActivity {

    private ArrayList<InventoryItem> mInventoryItems;
    private LinearLayout mContainer;

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
                showNewItemDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String listName = getIntent().getStringExtra("listName");
        setTitle("Editing " + listName);
        mContainer = new LinearLayout(this);
        setContentView(mContainer);
        mContainer.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mContainer.setOrientation(LinearLayout.VERTICAL);

        TableLayout tableLayout = new TableLayout(this);
        tableLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tableLayout.setStretchAllColumns(true);

        // Set the spacing between cells
        int spacing = 5; // in pixels
        tableLayout.setPadding(spacing, spacing, spacing, spacing);

        // Add table headers
        TableRow headerRow = new TableRow(this);
        TextView itemNameHeader = new TextView(this);
        TextView isCaseHeader = new TextView(this);
        TextView reqStockHeader = new TextView(this);
        TextView perCaseHeader = new TextView(this);
        TextView caseNameHeader = new TextView(this);
        itemNameHeader.setText("Item Name");
        isCaseHeader.setText("Case?");
        reqStockHeader.setText("Req. Stock");
        perCaseHeader.setText("Per Case");
        caseNameHeader.setText("Unit Name");
        headerRow.addView(itemNameHeader);
        headerRow.addView(isCaseHeader);
        headerRow.addView(reqStockHeader);
        headerRow.addView(perCaseHeader);
        headerRow.addView(caseNameHeader);
        tableLayout.addView(headerRow);
        // Get the listID from the intent
        int listID = getIntent().getIntExtra("listID", -1);

        // Check if the listID is valid
        if (listID == -1) {
            Toast.makeText(this, "Invalid listID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Make a request to the PHP script using Volley
        String url = "https://kentzysk.com/androidinv/run_inventory.php?listID=" + listID;
        JsonArrayRequest request = new JsonArrayRequest(url, response -> {

            // Loop through the items and create a row for each one
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

                    // Create a new row for the item
                    TableRow row = new TableRow(EditItemsActivity.this);
                    row.setTag(itemID);
                    TextView itemNameTextView = new TextView(EditItemsActivity.this);
                    TextView isCaseTextView = new TextView(EditItemsActivity.this);
                    TextView reqStockTextView = new TextView(EditItemsActivity.this);
                    TextView perCaseTextView = new TextView(EditItemsActivity.this);
                    TextView caseNameTextView = new TextView(EditItemsActivity.this);
                    itemNameTextView.setTextSize(19);
                    // Set the text for each cell in the row
                    itemNameTextView.setText(itemName);
                    isCaseTextView.setText(isCase == 1 ? "Y" : "N");
                    reqStockTextView.setText(String.valueOf(reqStock));
                    perCaseTextView.setText(String.valueOf(perCase));
                    caseNameTextView.setText(caseName);

                    // Set the layout weight for each column
                    TableRow.LayoutParams itemNameLayoutParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2f);
                    TableRow.LayoutParams isCaseLayoutParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
                    TableRow.LayoutParams reqStockLayoutParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
                    TableRow.LayoutParams perCaseLayoutParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
                    TableRow.LayoutParams caseNameLayoutParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);

                    // Set the layout parameters for each cell
                    itemNameTextView.setPadding(0, 10, 0, 50);
                    itemNameTextView.setLayoutParams(itemNameLayoutParams);
                    isCaseTextView.setLayoutParams(isCaseLayoutParams);
                    reqStockTextView.setLayoutParams(reqStockLayoutParams);
                    perCaseTextView.setLayoutParams(perCaseLayoutParams);
                    caseNameTextView.setLayoutParams(caseNameLayoutParams);


                    // Add the cells to the row
                    row.addView(itemNameTextView);
                    row.addView(isCaseTextView);
                    row.addView(reqStockTextView);
                    row.addView(perCaseTextView);
                    row.addView(caseNameTextView);

                    // Create a new inventory item and add it to the list
                    InventoryItem inventoryItem = new InventoryItem(itemName, isCase, reqStock, perCase, caseName, itemID);
                    mInventoryItems.add(inventoryItem);

                    // Add the row to the table layout
                    tableLayout.addView(row);
                    row.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            // Show the context menu
                            registerForContextMenu(v);
                            openContextMenu(v);
                            unregisterForContextMenu(v);
                            return true;
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, error -> Toast.makeText(EditItemsActivity.this, "Error loading items", Toast.LENGTH_SHORT).show());

// Add the request to the Volley request queue
        Volley.newRequestQueue(this).add(request);

// Add the main layout to the container
        mContainer.addView(tableLayout);
    }

    private TableRow createTableRow(String itemName, int isCase, int reqStock, int perCase, String caseName, int itemID) {
        TableRow row = new TableRow(EditItemsActivity.this);
        row.setTag(itemID);
        TextView itemNameTextView = new TextView(EditItemsActivity.this);
        itemNameTextView.setText(itemName);
        itemNameTextView.setPadding(0, 10, 0, 50);
        row.addView(itemNameTextView);

        TextView isCaseTextView = new TextView(EditItemsActivity.this);
        isCaseTextView.setText(String.valueOf(isCase));
        isCaseTextView.setPadding(0, 10, 0, 10);
        row.addView(isCaseTextView);

        TextView reqStockTextView = new TextView(EditItemsActivity.this);
        reqStockTextView.setText(String.valueOf(reqStock));
        reqStockTextView.setPadding(0, 10, 0, 10);
        row.addView(reqStockTextView);

        TextView perCaseTextView = new TextView(EditItemsActivity.this);
        perCaseTextView.setText(String.valueOf(perCase));
        perCaseTextView.setPadding(0, 10, 0, 10);
        row.addView(perCaseTextView);

        TextView caseNameTextView = new TextView(EditItemsActivity.this);
        caseNameTextView.setText(caseName);
        caseNameTextView.setPadding(0, 10, 0, 10);
        row.addView(caseNameTextView);

        row.setGravity(Gravity.CENTER);

        return row;
    }
    private String getUserId() {
        return getSharedPreferences("MyPrefs", MODE_PRIVATE)
                .getString("userID", "");
    }
    @SuppressLint("ResourceType")
    private void showNewItemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Item");

        // Set up the layout for the dialog
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        // Add an EditText for the item name
        final EditText nameInput = new EditText(this);
        nameInput.setHint("Item Name");
        layout.addView(nameInput);

        // Add a radio group for isCase
        final RadioGroup isCaseGroup = new RadioGroup(this);
        isCaseGroup.setOrientation(RadioGroup.HORIZONTAL);

        final RadioButton isCaseYes = new RadioButton(this);
        isCaseYes.setText("Yes");
        isCaseYes.setId(1);
        isCaseGroup.addView(isCaseYes);

        final RadioButton isCaseNo = new RadioButton(this);
        isCaseNo.setText("No");
        isCaseNo.setId(0);
        isCaseGroup.addView(isCaseNo);

        layout.addView(isCaseGroup);

        // Add an EditText for the required stock
        final EditText reqStockInput = new EditText(this);
        reqStockInput.setHint("Required Stock");
        reqStockInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(reqStockInput);

        // Add an EditText for the case amount
        final EditText caseAmountInput = new EditText(this);
        caseAmountInput.setHint("Per Case");
        caseAmountInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(caseAmountInput);

        // Add an EditText for the case name
        final EditText caseNameInput = new EditText(this);
        caseNameInput.setHint("Bulk Name");
        layout.addView(caseNameInput);

        builder.setView(layout);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String itemName = nameInput.getText().toString();
                String isCase = String.valueOf(isCaseGroup.getCheckedRadioButtonId());
                String reqStockStr = reqStockInput.getText().toString();
                String caseAmountStr = caseAmountInput.getText().toString();
                String caseName = caseNameInput.getText().toString();

                if (!itemName.isEmpty() && !isCase.isEmpty() && !reqStockStr.isEmpty() && !caseAmountStr.isEmpty() && !caseName.isEmpty()) {
                    int reqStock = Integer.parseInt(reqStockStr);
                    int caseAmount = Integer.parseInt(caseAmountStr);
                    int isCaseInt = Integer.parseInt(isCase);
                    createNewItem(itemName, reqStock, isCaseInt, caseAmount, caseName);
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
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        // Get the itemID from the tag of the selected row
        TableRow selectedRow = (TableRow) v;
        Log.d("EditItemsActivity", "selectedRow: " + selectedRow);
        Log.d("EditItemsActivity", "selectedRow tag: " + selectedRow.getTag());
        int itemID = (int) selectedRow.getTag();

        menu.setHeaderTitle("Item Options");
        menu.add(Menu.NONE, 0, 0, "Edit");
        menu.add(Menu.NONE, 1, 1, "Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Get the itemID from the tag of the selected row
                TableRow selectedRow = (TableRow) v;
                int itemID = (int) selectedRow.getTag();

                // Show a confirmation dialog
                new AlertDialog.Builder(EditItemsActivity.this)
                        .setTitle("Delete Item")
                        .setMessage("Are you sure you want to delete this item?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Call deleteItem() with the selected itemID
                                deleteItem(itemID);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();

                return true;
            }
        });
    }
    private void createNewItem(String itemName, int reqStock, int isCase, int perCase, String caseName) {
        String url = "https://kentzysk.com/androidinv/new_item.php";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            boolean success = json.getBoolean("success");
                            if (success) {
                                int listID = getIntent().getIntExtra("listID", -1);
                                Intent intent = new Intent(EditItemsActivity.this, EditItemsActivity.class);
                                intent.putExtra("listID", listID);
                                String listName = getIntent().getStringExtra("listName");
                                intent.putExtra("listName", listName);
                                startActivity(intent);
                                finish(); // finish the current activity to prevent stacking of activities
                            } else {
                                Toast.makeText(EditItemsActivity.this, "Error adding item", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(EditItemsActivity.this, "Error parsing JSON response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditItemsActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String listID = String.valueOf(getIntent().getIntExtra("listID", -1));
                Map<String, String> params = new HashMap<>();
                params.put("userID", getUserId());
                params.put("listID", listID);
                params.put("itemName", itemName);
                params.put("reqStock", String.valueOf(reqStock));
                params.put("isCase", String.valueOf(isCase));
                params.put("perCase", String.valueOf(perCase));
                params.put("caseName", caseName);
                return params;
            }
        };
        queue.add(request);
    }
    private void deleteItem(int listID) {
        String url = "https://kentzysk.com/androidinv/delete_list.php?listID=" + listID;
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            boolean success = json.getBoolean("success");
                            if (success) {
                               Intent intent = new Intent(EditItemsActivity.this, EditItemsActivity.class);
                                startActivity(intent);
                                finish(); // finish the current activity to prevent stacking of activities
                            } else {
                                System.out.println("List ID" + listID);
                                Toast.makeText(EditItemsActivity.this, "Error deleting item", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(EditItemsActivity.this, "Error parsing JSON response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditItemsActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("listID", String.valueOf(listID));;
                return params;
            }
        };
        queue.add(request);
    }
}