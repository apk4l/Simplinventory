package com.example.smallbizinventory;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
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

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class NewPasswordActivity extends AppCompatActivity {

    private EditText mResetCodeEditText;
    private EditText mPasswordEditText;
    private EditText mConfirmPasswordEditText;
    private Button mResetPasswordButton;
    private LoginActivity login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_password_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mResetCodeEditText = findViewById(R.id.reset_code_edittext);
        mPasswordEditText = findViewById(R.id.password_edittext);
        mConfirmPasswordEditText = findViewById(R.id.confirm_password_edittext);
        mResetPasswordButton = findViewById(R.id.reset_password_button);
        login = new LoginActivity();
        String email = getSharedPreferences("email", MODE_PRIVATE).toString();
        mResetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String resetCode = mResetCodeEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();
                String confirmPassword = mConfirmPasswordEditText.getText().toString();
                String hashPassword = hashPassword(password);
                if (!password.equals(confirmPassword)) {
                    // Show an error message if the passwords don't match
                    Toast.makeText(NewPasswordActivity.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Send reset code and new password to PHP script
                String url = "https://kentzysk.com/androidinv/reset_pw_with_code.php";
                RequestQueue queue = Volley.newRequestQueue(NewPasswordActivity.this);
                StringRequest request = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject json = new JSONObject(response);
                                    boolean success = json.getBoolean("success");
                                    if (success) {
                                        // Show a success message and launch the login activity
                                        Toast.makeText(NewPasswordActivity.this, "Password reset successful", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(NewPasswordActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        // Show an error message
                                        Toast.makeText(NewPasswordActivity.this, "Invalid reset code or password", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    // Handle JSON parsing error
                                    Toast.makeText(NewPasswordActivity.this, "Error parsing JSON response", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Handle network error
                                Toast.makeText(NewPasswordActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("email", email);
                        params.put("reset_code", resetCode);
                        params.put("new_password", hashPassword);
                        return params;
                    }
                };
                queue.add(request);
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