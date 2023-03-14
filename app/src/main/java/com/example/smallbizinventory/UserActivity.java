package com.example.smallbizinventory;

import android.content.SharedPreferences;
import android.text.Html;
import android.text.Spanned;
import android.view.MenuItem;
import android.widget.*;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import android.util.Log;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.HashMap;

import android.content.Intent;
import org.json.JSONException;
import org.json.JSONObject;


public class UserActivity extends AppCompatActivity {
    private Button mChangePassButton;
    private TextView mChangePassText;

    private TextView mDisplayUserName;

    private TextView mDisplayEmail;

    private ListsActivity lists;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            // This will handle click events on the back button in the title bar
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        setTitle("User Profile");
        // Enable the back arrow in the title bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mChangePassButton = findViewById(R.id.change_pass_button);
        mDisplayUserName = findViewById(R.id.display_username);
        mDisplayEmail = findViewById(R.id.display_email);

        // Make a request to the PHP script using Volley
        String url = "https://kentzysk.com/androidinv/get_user.php?userID=" + getUserId();
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject user = new JSONObject(response);
                String username = user.getString("username");
                String email = user.getString("email");

                // Update the TextViews
                mDisplayUserName.setText(username);
                mDisplayEmail.setText(email);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Toast.makeText(UserActivity.this, "Error retrieving user data", Toast.LENGTH_SHORT).show();
            error.printStackTrace();
        });

        // Add the request to the request queue
        Volley.newRequestQueue(this).add(request);

        mChangePassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the change pass activity
                Intent intent = new Intent(UserActivity.this, ChangePasswordActivity.class);
                intent.putExtra("userID", getUserId());
                startActivity(intent);
            }
        });
    }

    String getUserId() {
        return getSharedPreferences("MyPrefs", MODE_PRIVATE)
                .getString("userID", "");
    }
}