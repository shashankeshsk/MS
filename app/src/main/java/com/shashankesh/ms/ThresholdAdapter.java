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
 *File ThresholdAdapter
 *Created by Shashankesh Upadhyay
 *on Tuesday, November, 2019
 */public class ThresholdAdapter extends RecyclerView.Adapter<ThresholdAdapter.ThresholdAdapterViewHolder> {

    // Holds on to the cursor to display the itemlist
    private Cursor mCursor;
    private Context mContext;
    private final ThresholdAdapter.ThresholdAdapterOnClickHandler mClickHandler;


    public ThresholdAdapter(Cursor mCursor, Context mContext, ThresholdAdapterOnClickHandler mClickHandler) {
        this.mContext = mContext;
        this.mCursor = mCursor;
        this.mClickHandler = mClickHandler;
    }

    public interface ThresholdAdapterOnClickHandler {
        void onClick(int anInt, int position);
    }

    @NonNull
    @Override
    public ThresholdAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Get the RecyclerView item layout
        int layoutIdForListItem = R.layout.threshold_list;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new ThresholdAdapter.ThresholdAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThresholdAdapterViewHolder holder, int position) {
        //  Move the cursor to the passed in position, return if moveToPosition returns false
        // Move the mCursor to the position of the item to be displayed
        if (!mCursor.moveToPosition(position))
            return; // bail if returned null
        //  Call getString on the cursor to get the guest's name
        String name = mCursor.getString(mCursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_NAME));
        int itemsCount  = mCursor.getInt(mCursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_COUNT));
        //  Call getInt on the cursor to get the party size
        int itemTh = mCursor.getInt(mCursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_TH));
        //  Set the holder's nameTextView text to the guest's name
        // Display the guest name
        holder.nameTextView.setText(name);
        holder.countTextView.setText(itemsCount+" (item left)");
        //  Set the holder's thTextView text to the party size
        // Display the party count
        holder.thTextView.setText(String.format("Estimated Threshold : " + itemTh));

        //check id item is less than the threshold value
        if(itemsCount<=itemTh){
            holder.countTextView.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimaryLight));
        }else holder.countTextView.setBackgroundColor(mContext.getResources().getColor(R.color.colorAccentLight));
    }

    @Override
    public int getItemCount() {
        //  Update the getItemCount to return the getCount of the cursor
        return mCursor.getCount();
    }

    public void swapCursor(Cursor allItem, int mPosition) {
        if(allItem==null)return;
        mCursor = allItem;
        //notifyDataSetChanged();
        notifyItemChanged(mPosition);
    }

    public class ThresholdAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView nameTextView;
        TextView thTextView;
        TextView countTextView;
        public ThresholdAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.th_item_name);
            thTextView = (TextView) itemView.findViewById(R.id.th_value);
            countTextView = (TextView)itemView.findViewById(R.id.th_item_count);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View item) {
            int adapterPosition = getAdapterPosition();
            if(mCursor.moveToPosition(adapterPosition))
                mClickHandler.onClick(mCursor.getInt(mCursor.getColumnIndex(ItemContract.ItemEntry._ID)),adapterPosition);
            //else Log.e(ItemActivity.class.getSimpleName(),"Wrong Cursor click");
        }
    }
}
