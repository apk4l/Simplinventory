package com.example.smallbizinventory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.Collections;

public class InventoryItemAdapter extends RecyclerView.Adapter<InventoryItemAdapter.InventoryItemViewHolder> {
    private ArrayList<InventoryItem> mInventoryItems;
    private OnItemMoveListener mListener;
    public InventoryItemAdapter(ArrayList<InventoryItem> inventoryItems) {
        mInventoryItems = inventoryItems;


    }

    @NonNull
    @Override
    public InventoryItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inventory_item, parent, false);
        return new InventoryItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryItemViewHolder holder, int position) {
        // Get the inventory item at the current position
        InventoryItem item = mInventoryItems.get(position);

        // Set the item name
        holder.mItemNameTextView.setText(item.getItemName());

        // Set the isCase text based on the value of isCase
        //   holder.mIsCaseTextView.setText(String.valueOf(item.isCase()));

        // Set the reqStock text
        //      holder.mReqStockTextView.setText(String.valueOf(item.getReqStock()));

        // Set the perCase text
        //    holder.mPerCaseTextView.setText(String.valueOf(item.getPerCase()));

        // Set the caseName text
        //   holder.mCaseNameTextView.setText(item.getCaseName());

        // Add null check for the view holder
        if (holder != null) {
            holder.mIncrementButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int currentPosition = holder.getAdapterPosition();
                    int newPosition = currentPosition - 1;

                    if (newPosition >= 0) {
                        Collections.swap(mInventoryItems, currentPosition, newPosition);
                        notifyItemChanged(currentPosition);
                        notifyItemChanged(newPosition);
                    }
                }
            });

            holder.mDecrementButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int currentPosition = holder.getAdapterPosition();
                    int newPosition = currentPosition + 1;
                    if (newPosition < mInventoryItems.size()) {
                        Collections.swap(mInventoryItems, currentPosition, newPosition);
                        notifyItemChanged(currentPosition);
                        notifyItemChanged(newPosition);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mInventoryItems.size();
    }

    public class InventoryItemViewHolder extends RecyclerView.ViewHolder {
        public TextView mItemNameTextView;
        public TextView mIsCaseTextView;
        public TextView mReqStockTextView;
        public TextView mPerCaseTextView;
        public TextView mCaseNameTextView;

        public Button mIncrementButton;
        public Button mDecrementButton;

        public InventoryItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mItemNameTextView = itemView.findViewById(R.id.item_name_textview);
            //  mIsCaseTextView = itemView.findViewById(R.id.is_case_textview);
            //   mReqStockTextView = itemView.findViewById(R.id.req_stock_textview);
            //   mPerCaseTextView = itemView.findViewById(R.id.per_case_textview);
            //   mCaseNameTextView = itemView.findViewById(R.id.case_name_textview);

            mIncrementButton = itemView.findViewById(R.id.increment_button);
            mDecrementButton = itemView.findViewById(R.id.decrement_button);

            mIncrementButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int currentPosition = getAdapterPosition();
                    int newPosition = currentPosition + 1;
                    if (newPosition < mInventoryItems.size()) {
                        Collections.swap(mInventoryItems, currentPosition, newPosition);
                        notifyItemChanged(currentPosition);
                        notifyItemChanged(newPosition);
                    }
                }
            });

            mDecrementButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int currentPosition = getAdapterPosition();
                    int newPosition = currentPosition - 1;
                    if (newPosition >= 0) {
                        Collections.swap(mInventoryItems, currentPosition, newPosition);
                        notifyItemChanged(currentPosition);
                        notifyItemChanged(newPosition);
                    }
                }
            });
        }
    }

    public interface OnItemMoveListener {
        boolean onItemMove(int fromPosition, int toPosition);

        void onItemDismiss(int position);
    }

    public void setOnItemMoveListener(OnItemMoveListener listener) {
        this.mListener = listener;
    }
    }
