package com.shashankesh.ms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.shashankesh.ms.Data.ItemContract;
import com.shashankesh.ms.Data.ItemDbHelper;

public class PrintActivity extends AppCompatActivity {
    private PrintAdapter mAdapter;
    Toolbar toolbar;
    //private SQLiteDatabase mDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);

        toolbar = (Toolbar) findViewById(R.id.toolbar_print_activity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Items To order");
        RecyclerView ItemlistRecyclerView;

        // Set local attributes to corresponding views
        ItemlistRecyclerView = (RecyclerView) this.findViewById(R.id.recycler_view_print_activity);

        // Set layout for the RecyclerView, because it's a list we are using the linear layout
        ItemlistRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemlistRecyclerView.setHasFixedSize(true);

        // Create a DB helper (this will create the DB if run for the first time)
        ItemDbHelper dbHelper = new ItemDbHelper(this);
        // Keep a reference to the mDb until paused or killed. Get a writable database
        // because you will be adding restaurant customers


        // Get all guest info from the database and save in a cursor
        Cursor cursor = getAllItemToOrder();

        //  Pass the entire cursor to the adapter rather than just the count
        // Create an adapter for that cursor to display the data
        mAdapter = new PrintAdapter(cursor, this);

        // Link the adapter to the RecyclerView
        ItemlistRecyclerView.setAdapter(mAdapter);
    }

    private Cursor getAllItemToOrder() {
        String selection = ItemContract.ItemEntry.COLUMN_ITEM_ORDER+"=?";
        return getContentResolver().query(
                ItemContract.ItemEntry.CONTENT_URI,
                null,
                selection,
                new String[]{"1"},
                ItemContract.ItemEntry.COLUMN_TIMESTAMP
        );
    }

}
