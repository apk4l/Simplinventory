package com.example.myapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class InventoryActivity extends AppCompatActivity {

    private LinearLayout mContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        mContainer = findViewById(R.id.container);

        // Get the listID parameter from the intent
       // String listID = getIntent().getStringExtra("listID");

        // Make a request to the PHP script using Volley
        String url = "https://kentzysk.com/androidinv/run_inventory.php?listID=2";
        JsonArrayRequest request = new JsonArrayRequest(url,
                response -> {
                    // Loop through the items and create a text field for each one
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject item = response.getJSONObject(i);
                            String itemName = item.getString("itemName");

                            // Create a new text field and add it to the container
                            final Context context = this;
                            TextView textField = new TextView(context);
                           textField.setText(itemName);
                            mContainer.addView(textField);

                            // Create a new EditText field and add it to the container
                            EditText editText = new EditText(context);
                            editText.setId(View.generateViewId());
                            editText.setHint("Enter " + itemName + " Stock");
                            mContainer.addView(editText);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                error -> Toast.makeText(InventoryActivity.this, "Error loading items", Toast.LENGTH_SHORT).show());

        // Add the request to the Volley request queue
        Volley.newRequestQueue(this).add(request);
    }
}