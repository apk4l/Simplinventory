package com.example.smallbizinventory;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.*;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NewUserActivity extends AppCompatActivity {

    private EditText mUserIDEditText;
    private EditText mPasswordEditText;
    private EditText mEmailEditText;
    private EditText mResetQuestionEditText;
    private EditText mResetAnswerEditText;
    private Button mCreateAccountButton;

    private ImageView mVerificationImageView;
    private EditText mVerificationEditText;
    private Bitmap mVerificationBitmap;
    private String mVerificationCode;


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
        mVerificationImageView = findViewById(R.id.verification_imageview);
        mVerificationEditText = findViewById(R.id.verification_edittext);

// Generate random verification code on app side
        String appVerificationCode = UUID.randomUUID().toString().substring(0, 6);

// Send verification code to server to generate verification image
        String imageUrl = "https://kentzysk.com/androidinv/generate_verification_image.php?verification_code=" + appVerificationCode;
        RequestQueue queue = Volley.newRequestQueue(NewUserActivity.this);
        ImageRequest imageRequest = new ImageRequest(imageUrl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        // Set verification image
                        mVerificationBitmap = bitmap;
                        mVerificationImageView.setImageBitmap(mVerificationBitmap);

                        // Debug code to log Bitmap dimensions
                        Log.d("Verification", "Bitmap dimensions: " + mVerificationBitmap.getWidth() + "x" + mVerificationBitmap.getHeight());
                    }
                }, 0, 0, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle network error
                Toast.makeText(NewUserActivity.this, "Error retrieving verification image", Toast.LENGTH_SHORT).show();
            }

        });
        queue.add(imageRequest);
        mCreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = mUserIDEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();
                String email = mEmailEditText.getText().toString();
                String resetQuestion = mResetQuestionEditText.getText().toString();
                String resetAnswer = mResetAnswerEditText.getText().toString();
                String verificationCode = mVerificationEditText.getText().toString();

                // Hash password using SHA-256
                String hashedPassword = hashPassword(password);

                // Check verification code
                if (!verificationCode.equals(mVerificationCode)) {
                    Toast.makeText(NewUserActivity.this, "Verification code incorrect", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Send new user details to PHP script
                String url = "https://kentzysk.com/androidinv/new_user.php";
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
                        params.put("username", username);
                        params.put("password", hashedPassword);
                        params.put("email", email);
                        params.put("resetQuestion", resetQuestion);
                        params.put("resetAnswer", resetAnswer);
                        return params;
                    }
                };
                queue.add(request);
            }
        });

// Set verification code and add listener to verification EditText
        mVerificationCode = appVerificationCode;
        mVerificationEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    String verificationCode = mVerificationEditText.getText().toString();
                    if (!verificationCode.equals(mVerificationCode)) {
                        Toast.makeText(NewUserActivity.this, "Verification code incorrect", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                return false;
            }
        });
    }
    private String hashPassword(String password) {
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