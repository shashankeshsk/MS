package com.shashankesh.ms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.shashankesh.ms.Data.ItemContract;
import com.shashankesh.ms.Data.ItemDbHelper;

public class NewItemAddActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private TextInputLayout mItemName;
    private TextInputLayout mItemCount;
    private TextInputLayout mItemVendr;
    private Button mCreateBtn;
    private SQLiteDatabase mDb;
    private String mThreshold;

    private final static String LOG_TAG = NewItemAddActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item_add);
        mItemCount = (TextInputLayout) findViewById(R.id.new_item_count);
        mItemName = (TextInputLayout) findViewById(R.id.new_item_name);
        mItemVendr = (TextInputLayout) findViewById(R.id.new_item_vendr);
        mCreateBtn = (Button) findViewById(R.id.new_itm_btn);
        //setting up the share threshold value
        setupSharedPreferences();

        // Create a DB helper (this will create the DB if run for the first time)
        ItemDbHelper dbHelper = new ItemDbHelper(this);
        // Keep a reference to the mDb until paused or killed. Get a writable database
        // because you will be adding restaurant customers
        mDb = dbHelper.getWritableDatabase();

        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemName = mItemName.getEditText().getText().toString().trim();
                itemName = itemName.substring(0,1).toUpperCase()+ itemName.substring(1).toLowerCase();
                String itemVendr = mItemVendr.getEditText().getText().toString().trim();
                itemVendr = itemVendr.toLowerCase();
                String itemCount = "";
                try {
                    itemCount = mItemCount.getEditText().getText().toString().trim();
                } catch (NumberFormatException ex) {
                    // Make sure you surround the Integer.parseInt with a try catch and log any exception
                    Log.e(LOG_TAG, "Failed to parse party size text to number: " + ex.getMessage());
                }
                if (itemName.equals("") || itemCount.equals("") || itemVendr.equals("")) {
                    Toast.makeText(NewItemAddActivity.this, "All fields are required", Toast.LENGTH_LONG).show();
                    return;
                }
                if (itemCount.equals("0")) {
                    Toast.makeText(NewItemAddActivity.this, "Item count cannot be 0", Toast.LENGTH_LONG).show();
                    return;
                }

                Uri uri = addItems(itemName, itemCount, itemVendr);
                if (uri != null)
                    finish();
            }
        });

    }


    private Uri addItems(String itemName, String itemCount, String itemVendr) {
        int thDiv = Integer.valueOf(mThreshold);
        if (checkForDuplicateItem(itemName, itemVendr)) {
            Toast.makeText(NewItemAddActivity.this, "Same Item there with Same Vendor is present," +
                    " Please rather Update this Item", Toast.LENGTH_LONG).show();
            return null;
        }
        ContentValues cv = new ContentValues();
        cv.put(ItemContract.ItemEntry.COLUMN_ITEM_NAME, itemName);
        cv.put(ItemContract.ItemEntry.COLUMN_ITEM_COUNT, itemCount);
        cv.put(ItemContract.ItemEntry.COLUMN_ITEM_VENDOR, itemVendr);
        cv.put(ItemContract.ItemEntry.COLUMN_ITEM_TH, String.valueOf(Integer.valueOf(itemCount) / thDiv));
        return getContentResolver().insert(
                ItemContract.ItemEntry.CONTENT_URI,
                cv);
    }

    private boolean checkForDuplicateItem(String itemName, String itemVendr) {
        Cursor cursor = getContentResolver().query(
                ItemContract.ItemEntry.CONTENT_URI,
                null,
                ItemContract.ItemEntry.COLUMN_ITEM_NAME + "=? AND " + ItemContract.ItemEntry.COLUMN_ITEM_VENDOR + "=?",
                new String[]{itemName, itemVendr},
                null
        );
        if (cursor.getCount() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.name))) {
        } else if (key.equals(getString(R.string.th))) {
            mThreshold = sharedPreferences.getString(key, "3");
        }
    }

    private void setupSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mThreshold = sharedPreferences.getString(getResources().getString(R.string.th), "3");
    }
}
