package com.example.myapplication;

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

public class NewUserActivity extends AppCompatActivity {

    private EditText mUserIDEditText;
    private EditText mPasswordEditText;
    private EditText mEmailEditText;
    private EditText mResetQuestionEditText;
    private EditText mResetAnswerEditText;
    private Button mCreateAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        mUserIDEditText = findViewById(R.id.user_id_edittext);
        mPasswordEditText = findViewById(R.id.password_edittext);
        mEmailEditText = findViewById(R.id.email_edittext);
        mResetQuestionEditText = findViewById(R.id.reset_question_edittext);
        mResetAnswerEditText = findViewById(R.id.reset_answer_edittext);
        mCreateAccountButton = findViewById(R.id.create_user_button);

        mCreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userID = mUserIDEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();
                String email = mEmailEditText.getText().toString();
                String resetQuestion = mResetQuestionEditText.getText().toString();
                String resetAnswer = mResetAnswerEditText.getText().toString();

                // Send new user details to PHP script
                String url = "http://kentzysk.com/androidinv/new_user.php";
                RequestQueue queue = Volley.newRequestQueue(NewUserActivity.this);
                StringRequest request = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject json = new JSONObject(response);
                                    boolean success = json.getBoolean("success");
                                    if (success) {
                                        // Show success message
                                        Toast.makeText(NewUserActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        // Show error message
                                        String error = json.getString("error");
                                        Toast.makeText(NewUserActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    // Handle JSON parsing error
                                    Toast.makeText(NewUserActivity.this, "Error parsing JSON response", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle network error
                        Toast.makeText(NewUserActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("userID", userID);
                        params.put("password", password);
                        params.put("email", email);
                        params.put("resetQuestion", resetQuestion);
                        params.put("resetAnswer", resetAnswer);
                        return params;
                    }
                };
                queue.add(request);
            }
        });
    }
}