package com.shashankesh.ms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.shashankesh.ms.Data.ItemContract;
import com.shashankesh.ms.Data.ItemDbHelper;
import com.shashankesh.ms.Data.TaskContentProvider;

public class VendorActivity extends AppCompatActivity implements VendorAdapter.VendorAdapterOnClickHandler {
    private VendorAdapter mAdapter;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor);

        toolbar = (Toolbar) findViewById(R.id.toolbar_vendor_activity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Vendors Details");
        RecyclerView ItemlistRecyclerView;

        // Set local attributes to corresponding views
        ItemlistRecyclerView = (RecyclerView) this.findViewById(R.id.recycler_view_vendor_activity);

        // Set layout for the RecyclerView, because it's a list we are using the linear layout
        ItemlistRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemlistRecyclerView.setHasFixedSize(true);

        // Create a DB helper (this will create the DB if run for the first time)
        ItemDbHelper dbHelper = new ItemDbHelper(this);
        // Keep a reference to the mDb until paused or killed. Get a writable database
        // because you will be adding restaurant customers
        //mDb = dbHelper.getWritableDatabase();
        //dbHelper.onUpgrade(mDb,2,3);

        //Fill the database with fake data
        //TestUtil.insertFakeData(mDb);

        // Get all guest info from the database and save in a cursor
        Cursor cursor = getAllVendors();

        //  Pass the entire cursor to the adapter rather than just the count
        // Create an adapter for that cursor to display the data
        mAdapter = new VendorAdapter(cursor, this, this);

        // Link the adapter to the RecyclerView
        ItemlistRecyclerView.setAdapter(mAdapter);
    }

    private Cursor getAllVendors() {
        Uri uri = ItemContract.ItemEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(TaskContentProvider.TYPES_DISTINCT_VAUE).build();
        return getContentResolver().query(
                uri,
                new String[]{ItemContract.ItemEntry.COLUMN_ITEM_VENDOR},
                null,
                null,
                ItemContract.ItemEntry.COLUMN_TIMESTAMP);
    }


    @Override
    public void onClick(long position) {
        Uri number = Uri.parse("tel:919" + position);
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
        startActivity(callIntent);
    }
}
