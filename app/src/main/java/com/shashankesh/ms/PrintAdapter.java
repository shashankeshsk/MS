package com.shashankesh.ms;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shashankesh.ms.Data.ItemContract;

/*
 *File PrintAdapter
 *Created by Shashankesh Upadhyay
 *on Sunday, November, 2019
 */public class PrintAdapter extends RecyclerView.Adapter<PrintAdapter.PrintAdapterViewHolder> {

    // Holds on to the cursor to display the itemlist
    private Cursor mCursor;
    private Context mContext;

    public PrintAdapter(Cursor mCursor, Context mContext) {
        this.mCursor = mCursor;
        this.mContext = mContext;
    }



    @NonNull
    @Override
    public PrintAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Get the RecyclerView item layout
        int layoutIdForListItem = R.layout.print_item_list;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new PrintAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrintAdapterViewHolder holder, int position) {
        //  Move the cursor to the passed in position, return if moveToPosition returns false
        // Move the mCursor to the position of the item to be displayed
        if (!mCursor.moveToPosition(position))
            return; // bail if returned null
        //  Call getString on the cursor to get the guest's name
        String name = mCursor.getString(mCursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_NAME));
        //  Call getInt on the cursor to get the party size
        int itemCount = mCursor.getInt(mCursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_COUNT));
        int th = mCursor.getInt(mCursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_TH));
        String itemVndr = mCursor.getString(mCursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_VENDOR));
        //  Set the holder's nameTextView text to the guest's name
        // Display the guest name
        holder.nameTextView.setText(name);
        //  Set the holder's thTextView text to the party size
        // Display the party count
        holder.itemSize.setText(String.format("Item to Order : " + (th-itemCount+1)));
        holder.itemVndr.setText(String.format("Vendor : "+itemVndr));
    }

    @Override
    public int getItemCount() {
        //  Update the getItemCount to return the getCount of the cursor
        return mCursor.getCount();
    }

    public void swapCursor(Cursor allItem) {
        mCursor = allItem;
        notifyDataSetChanged();
    }

    public class PrintAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView itemVndr;
        TextView itemSize;
        public PrintAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.print_item_name);
            itemSize = (TextView) itemView.findViewById(R.id.print_item_to_order);
            itemVndr = (TextView) itemView.findViewById(R.id.print_item_vndr);
        }
    }
}