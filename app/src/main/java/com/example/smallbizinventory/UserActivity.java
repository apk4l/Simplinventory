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


public class UserActivity extends AppCompatActivity {
    private Button mChangePassButton;
    private TextView mChangePassText;

    private ListsActivity lists;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        setTitle("User Profile");
        mChangePassButton = findViewById(R.id.change_pass_button);

        mChangePassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Launch the chane pass activity
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
