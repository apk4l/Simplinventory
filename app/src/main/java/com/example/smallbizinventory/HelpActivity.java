package com.example.smallbizinventory;

import android.os.Bundle;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.smallbizinventory.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HelpActivity extends AppCompatActivity  {
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
        setContentView(R.layout.activity_help);
        // Enable the back arrow in the title bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Help");
    }
}
