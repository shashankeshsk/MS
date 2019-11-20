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
 *File ItemAdapter
 *Created by Shashankesh Upadhyay
 *on Sunday, November, 2019
 */public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemAdapterViewHolder> {

    // Holds on to the cursor to display the itemlist
    private Cursor mCursor;
    private Context mContext;
    private final ItemAdapterOnClickHandler mClickHandler;

    public ItemAdapter(Context context, Cursor cursor, ItemAdapterOnClickHandler mClickHandler) {
        this.mContext = context;
        this.mCursor = cursor;
        this.mClickHandler = mClickHandler;
    }

    public interface ItemAdapterOnClickHandler {
        void onClick(int position);
    }

    @NonNull
    @Override
    public ItemAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Get the RecyclerView item layout
        int layoutIdForListItem = R.layout.item_list;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new ItemAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapterViewHolder holder, int position) {
        //  Move the cursor to the passed in position, return if moveToPosition returns false
        // Move the mCursor to the position of the item to be displayed
        if (!mCursor.moveToPosition(position))
            return; // bail if returned null
        //  Call getString on the cursor to get the guest's name
        String name = mCursor.getString(mCursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_NAME));
        //  Call getInt on the cursor to get the party size
        int itemCount = mCursor.getInt(mCursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_COUNT));
        String itemVndr = mCursor.getString(mCursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_VENDOR));
        //  Set the holder's nameTextView text to the guest's name
        // Display the guest name
        holder.nameTextView.setText(name);
        //  Set the holder's thTextView text to the party size
        // Display the party count
        holder.itemSize.setText(String.format("Item Count : " + itemCount));
        holder.itemVndr.setText(String.format("Vendor : "+itemVndr));

        //setting the itemView tag with the item id
        holder.itemView.setTag(mCursor.getInt(mCursor.getColumnIndex(ItemContract.ItemEntry._ID)));
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

    public class ItemAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameTextView;
        TextView itemVndr;
        TextView itemSize;

        //TextView countTextView;

        /**
         * Constructor for our ViewHolder. Within this constructor, we get a reference to our
         * TextViews
         *
         * @param itemView The View that you inflated in
         *                 {@link ItemAdapter#onCreateViewHolder(ViewGroup, int)}
         */
        public ItemAdapterViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.item_name);
            itemSize = (TextView) itemView.findViewById(R.id.item_count);
            itemVndr = (TextView) itemView.findViewById(R.id.item_vndr);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View item) {
            int adapterPosition = getAdapterPosition();
            if(mCursor.moveToPosition(adapterPosition)) {
                mClickHandler.onClick(mCursor.getInt(mCursor.getColumnIndex(ItemContract.ItemEntry._ID)));
            }
            //else Log.e(ItemActivity.class.getSimpleName(),"Wrong Cursor click");
        }
    }
}
