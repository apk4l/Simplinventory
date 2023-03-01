package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreateListActivity extends AppCompatActivity {

    private EditText mListNameEditText;
    private Button mCreateListButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_list_activity);

        mListNameEditText = findViewById(R.id.list_name_edittext);
        mCreateListButton = findViewById(R.id.create_list_button);

        mCreateListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String listName = mListNameEditText.getText().toString();

                // Get user ID from SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                String userID = sharedPreferences.getString("userID", "");

                // Send list name and user ID to PHP script
                String url = "https://kentzysk.com/androidinv/create_list.php";
                RequestQueue queue = Volley.newRequestQueue(CreateListActivity.this);
                StringRequest request = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject json = new JSONObject(response);
                                    boolean success = json.getBoolean("success");
                                    if (success) {
                                        // Show success message
                                        Toast.makeText(CreateListActivity.this, "List created successfully", Toast.LENGTH_SHORT).show();

                                        // Finish the activity
                                        finish();
                                    } else {
                                        // Show an error message
                                        String error = json.getString("error");
                                        Toast.makeText(CreateListActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    // Handle JSON parsing error
                                    e.printStackTrace();
                                    Toast.makeText(CreateListActivity.this, "Error parsing JSON response", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Handle network error
                                Toast.makeText(CreateListActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("listName", listName);
                        params.put("userID", userID);
                        return params;
                    }
                };
                queue.add(request);
            }
        });
    }
}