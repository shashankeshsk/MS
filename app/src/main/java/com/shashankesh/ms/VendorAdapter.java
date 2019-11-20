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
 *File VendorAdapter
 *Created by Shashankesh Upadhyay
 *on Monday, November, 2019
 */public class VendorAdapter extends RecyclerView.Adapter<VendorAdapter.VendorAdapterViewHolder> {

    // Holds on to the cursor to display the itemlist
    private Cursor mCursor;
    private Context mContext;
    private final VendorAdapter.VendorAdapterOnClickHandler mClickHandler;
    private long mPhoneNumber [] = new long[50];

    public VendorAdapter(Cursor mCursor, Context mContext, VendorAdapterOnClickHandler mClickHandler) {
        this.mCursor = mCursor;
        this.mContext = mContext;
        this.mClickHandler = mClickHandler;
    }

    public interface VendorAdapterOnClickHandler {
        void onClick(long position);
    }

    @NonNull
    @Override
    public VendorAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Get the RecyclerView item layout
        int layoutIdForListItem = R.layout.vendor_list;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new VendorAdapterViewHolder(view);
    }

    public void swapCursor(Cursor allItem) {
        mCursor = allItem;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull VendorAdapterViewHolder holder, int position) {
        //  Move the cursor to the passed in position, return if moveToPosition returns false
        // Move the mCursor to the position of the item to be displayed
        if (!mCursor.moveToPosition(position))
            return; // bail if returned null
        //  Call getString on the cursor to get the guest's name
        String name = mCursor.getString(mCursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_VENDOR));
        long phoneNumber = Math.round(Math.random()*1000000000);
        mPhoneNumber[position] = phoneNumber;
        // Display the guest name
        holder.vendorName.setText(name);
        //  Set the holder's thTextView text to the party size
        // Display the party count
        holder.vendorPh.setText(String.format("Vendors Contact : +91-9" + phoneNumber));
    }

    @Override
    public int getItemCount() {
        //  Update the getItemCount to return the getCount of the cursor
        return mCursor.getCount();
    }

    public class VendorAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView vendorName;
        TextView vendorPh;
        public VendorAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            vendorName = (TextView) itemView.findViewById(R.id.vendor_name);
            vendorPh = (TextView) itemView.findViewById(R.id.vendor_phone);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            //if(mCursor.moveToPosition(adapterPosition))
            mClickHandler.onClick(mPhoneNumber[adapterPosition]);
            //else Log.e(ItemActivity.class.getSimpleName(),"Wrong Cursor click");
        }
    }
}