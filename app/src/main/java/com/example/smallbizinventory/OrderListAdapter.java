package com.example.smallbizinventory;

import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup;
import java.util.ArrayList;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.TextView;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {

    private ArrayList<Order> mOrderList;

    public OrderListAdapter(ArrayList<Order> orders) {
        mOrderList = orders;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView orderTextView;
        private TextView orderDateView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.orderTextView = itemView.findViewById(R.id.orderTextView);
            this.orderDateView = itemView.findViewById(R.id.orderDateView);
        }

        public void bind(Order order) {
            orderTextView.setText(order.getOrderText());
            orderDateView.setText(order.getOrderDate());
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the layout
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_item, parent, false);

        // Return a new instance of ViewHolder
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Get the item to be displayed
        Order order = mOrderList.get(position);

        // Display item in layout
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return mOrderList.size();
    }
}