package com.shashankesh.ms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.shashankesh.ms.Data.ItemContract;
import com.shashankesh.ms.Data.ItemDbHelper;

public class ItemIncrsDecrs extends AppCompatActivity {
    private Toolbar toolbar;
    private SQLiteDatabase mDb;
    private int mId;
    private String mItemName;
    private Cursor mCursor;
    private final String TAG = ItemIncrsDecrs.class.getSimpleName();
    private TextInputLayout mUpdateItem;
    private Button mAddbtn;
    private Button mRemovebtn;
    private String mItemCurrentCount;
    private int mTh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_incrs_decrs);
        toolbar = (Toolbar) findViewById(R.id.incrs_decrs_toolbar);
        setSupportActionBar(toolbar);

        mUpdateItem = (TextInputLayout) findViewById(R.id.update_item_text);
        mAddbtn = (Button) findViewById(R.id.add_itm_btn);
        mAddbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });
        mRemovebtn = (Button) findViewById(R.id.remove_itm_btn);
        mRemovebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem();
            }
        });

        // Create a DB helper (this will create the DB if run for the first time)
        ItemDbHelper dbHelper = new ItemDbHelper(this);
        // Keep a reference to the mDb until paused or killed. Get a writable database
        // because you will be adding restaurant customers
        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("Pos")) {
                mId = intentThatStartedThisActivity.getIntExtra("Pos", -1);
            }
        }
        mDb = dbHelper.getWritableDatabase();
        mCursor = queryItemName();
        if (mCursor.moveToFirst()) {
            mItemName = mCursor.getString(mCursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_NAME));
            mItemCurrentCount = mCursor.getString(mCursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_COUNT));
            mTh = mCursor.getInt(mCursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_TH));
            Log.d(TAG, "itemName: " + mItemName);
        }
        getSupportActionBar().setTitle("Update Item: " + mItemName);

        String count = mUpdateItem.getEditText().getText().toString().trim();

        //updateItem(count, false);
    }

    private void removeItem() {
        String item = mUpdateItem.getEditText().getText().toString().trim();
        updateItem(item, false);
    }

    private void addItem() {
        String item = mUpdateItem.getEditText().getText().toString().trim();
        updateItem(item, true);
    }

    private Cursor queryItemName() {
        String[] columns = {ItemContract.ItemEntry.COLUMN_ITEM_NAME};
        String selection = ItemContract.ItemEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(mId)};
        String groupBy = null;
        String having = null;
        String orderBy = ItemContract.ItemEntry.COLUMN_TIMESTAMP;
        return getContentResolver().query(
                ItemContract.ItemEntry.CONTENT_URI,
                null,
                selection,
                new String[]{String.valueOf(mId)},
                ItemContract.ItemEntry.COLUMN_TIMESTAMP
        );
    }


    private void updateItem(String count, boolean add) {
        boolean needToUpdateTh = false;
        ContentValues cv = new ContentValues();
        String itemUpdated;
        if (add)
            itemUpdated = String.valueOf(Integer.valueOf(count) + Integer.valueOf(mItemCurrentCount));
        else
            itemUpdated = String.valueOf(Integer.valueOf(mItemCurrentCount) - Integer.valueOf(count));
        if (Integer.valueOf(itemUpdated) < 0) itemUpdated = "0";
        if (Integer.valueOf(itemUpdated) < mTh) {
            needToUpdateTh = true;
            Toast.makeText(ItemIncrsDecrs.this, String.format("Please Order " + mItemName + " atleast : " + (mTh - Integer.valueOf(itemUpdated))), Toast.LENGTH_LONG).show();
        }
        String selection = ItemContract.ItemEntry.COLUMN_ITEM_NAME + " = ?";
        if (!needToUpdateTh) {
            cv.put(ItemContract.ItemEntry.COLUMN_ITEM_COUNT, itemUpdated);
            cv.put(ItemContract.ItemEntry.COLUMN_ITEM_ORDER, "0");
        } else {
            cv.put(ItemContract.ItemEntry.COLUMN_ITEM_COUNT, itemUpdated);
            cv.put(ItemContract.ItemEntry.COLUMN_ITEM_ORDER, "1");
        }
        getContentResolver().update(
                ItemContract.ItemEntry.CONTENT_URI,
                cv,
                selection,
                new String[]{mItemName}
        );
        //Toast.makeText(this, "Updated", Toast.LENGTH_LONG).show();
        finish();
    }
}
