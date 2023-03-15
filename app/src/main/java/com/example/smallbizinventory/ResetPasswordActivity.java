package com.example.smallbizinventory;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ResetPasswordActivity extends AppCompatActivity {
    private EditText mEmailEditText;
    private EditText mResetCodeEditText;
    private Button mSubmitButton;

    private Button mResetPasswordButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Reset Password");
        mEmailEditText = findViewById(R.id.email_edittext);
        mSubmitButton = findViewById(R.id.submit_button);
        mResetPasswordButton = findViewById(R.id.enter_code_button);

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmailEditText.getText().toString();
                String url = "https://kentzysk.com/androidinv/resetcode.php";
                RequestQueue queue = Volley.newRequestQueue(ResetPasswordActivity.this);
                StringRequest request = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject json = new JSONObject(response);
                                    boolean success = json.getBoolean("success");
                                    if (success) {
                                        // Launch the reset password form activity
                                        Intent intent = new Intent(ResetPasswordActivity.this, NewPasswordActivity.class);
                                        intent.putExtra("email", email);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        // Show an error message
                                        Toast.makeText(ResetPasswordActivity.this, "Error sending reset code", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    // Handle JSON parsing error
                                    Toast.makeText(ResetPasswordActivity.this, "Error parsing JSON response", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Handle network error
                                Toast.makeText(ResetPasswordActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("email", email);
                        return params;
                    }
                };
                queue.add(request);
            }
        });
        mResetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResetPasswordActivity.this, NewPasswordActivity.class);
                startActivity(intent);
            }
        });
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