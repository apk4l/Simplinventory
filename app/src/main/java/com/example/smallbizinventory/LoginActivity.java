package com.example.smallbizinventory;

import android.content.SharedPreferences;
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

public class LoginActivity extends AppCompatActivity {

    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private Button mLoginButton;

    private Button mCreateButton;

    private Button mResetPassButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayShowTitleEnabled(false); // Hide the title text
        getSupportActionBar().setDisplayUseLogoEnabled(true); // Show the logo
        getSupportActionBar().setLogo(R.drawable.logo); // Set the logo image
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        setTitle("Small Biz Inventory");
        mUsernameEditText = findViewById(R.id.username_edittext);
        mPasswordEditText = findViewById(R.id.password_edittext);
        mLoginButton = findViewById(R.id.login_button);
        mCreateButton = findViewById(R.id.create_button);
        mResetPassButton = findViewById(R.id.reset_pw_button);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = mUsernameEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();

                // Send login credentials to PHP script
                String url = "https://kentzysk.com/androidinv/login.php";
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                StringRequest request = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject json = new JSONObject(response);
                                    boolean success = json.getBoolean("success");
                                    if (success) {
                                        String userID = json.getString("userID");
                                        String usernameResponse = json.getString("username");
                                        // Save the login state and userID in SharedPreferences
                                        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putBoolean("isLoggedIn", true);
                                        editor.putString("userID", userID);
                                        editor.apply();

                                        // Launch the main activity
                                        Intent intent = new Intent(LoginActivity.this, ListsActivity.class);
                                        intent.putExtra("userID", userID);
                                        intent.putExtra("username", usernameResponse);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        // Show an error message
                                        Toast.makeText(LoginActivity.this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    // Handle JSON parsing error
                                    Toast.makeText(LoginActivity.this, "Error parsing JSON response", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle network error
                        Toast.makeText(LoginActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("username", username);
                        params.put("password", hashPassword(password));
                        Log.d("LoginActivity", "Hashed password: " + hashPassword(password)); // add this line
                        return params;
                    }
                };
                queue.add(request);
            }
        });
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, NewUserActivity.class);
                startActivity(intent);
            }
        });
        mResetPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
            }
        });
    }
    String hashPassword(String password) {
        try {
            // Use SHA-256 hashing algorithm
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            // Convert byte array to hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            // Handle hashing algorithm not found error
            e.printStackTrace();
            return null;
        }
    }
}