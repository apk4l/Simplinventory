package com.example.smallbizinventory;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;

public class OrdersByListActivity extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    private OrderListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Order> mOrderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        String listName = getIntent().getStringExtra("listName");
        String listID = getIntent().getStringExtra("listID");
        setTitle("History: " + listName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRecyclerView = findViewById(R.id.order_list_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mOrderList = new ArrayList<>();
        mAdapter = new OrderListAdapter(mOrderList);
        mRecyclerView.setAdapter(mAdapter);

        getOrderHistory(listID);

    }

    // Call this method to inflate the menu



    private void getOrderHistory(String listID) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://kentzysk.com/androidinv/orders_by_list.php?listID=" + listID;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {   JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String orderList = jsonArray.getJSONObject(i).getString("orderList");
                                String orderDate = jsonArray.getJSONObject(i).getString("orderDate");
                                mOrderList.add(new Order(orderList, orderDate));
                            }
                            mAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // handle error
            }
        });
        queue.add(stringRequest);
    }
    @Override
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



