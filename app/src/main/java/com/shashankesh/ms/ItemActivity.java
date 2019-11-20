package com.shashankesh.ms;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.shashankesh.ms.Data.ItemContract;
import com.shashankesh.ms.Data.ItemDbHelper;
import com.shashankesh.ms.Data.TestUtil;

public class ItemActivity extends AppCompatActivity implements ItemAdapter.ItemAdapterOnClickHandler, LoaderManager.LoaderCallbacks<Cursor> {
    private ItemAdapter mAdapter;
    Toolbar toolbar;
    private SQLiteDatabase mDb;
    // Constants for logging and referring to a unique loader
    private static final String TAG = ItemActivity.class.getSimpleName();
    private static final int TASK_LOADER_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        toolbar = (Toolbar) findViewById(R.id.toolbar_item_activity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Items");
        RecyclerView ItemlistRecyclerView;

        // Set local attributes to corresponding views
        ItemlistRecyclerView = (RecyclerView) this.findViewById(R.id.recycler_view_item_activity);

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
        //TestUtil.insertFakeData();

        // Get all guest info from the database and save in a cursor
        Cursor cursor = getAllItem();

        // Pass the entire cursor to the adapter rather than just the count
        // Create an adapter for that cursor to display the data
        mAdapter = new ItemAdapter(this, cursor, this);

        // Link the adapter to the RecyclerView
        ItemlistRecyclerView.setAdapter(mAdapter);

        /*
         Add a touch helper to the RecyclerView to recognize when a user swipes to delete an item.
         An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
         and uses callbacks to signal when a user is performing these actions.
         */
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Here is where you'll implement swipe to delete

                // COMPLETED (1) Construct the URI for the item to delete
                //[Hint] Use getTag (from the adapter code) to get the id of the swiped item
                // Retrieve the id of the task to delete
                int id = (int) viewHolder.itemView.getTag();

                // Build appropriate uri with String row id appended
                String stringId = Integer.toString(id);
                Uri uri = ItemContract.ItemEntry.CONTENT_URI;
                uri = uri.buildUpon().appendPath(stringId).build();

                // COMPLETED (2) Delete a single row of data using a ContentResolver
                getContentResolver().delete(uri, null, null);

                // COMPLETED (3) Restart the loader to re-query for all tasks after a deletion
                getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, ItemActivity.this);
            }
        }).attachToRecyclerView(ItemlistRecyclerView);

        /*
         Ensure a loader is initialized and active. If the loader doesn't already exist, one is
         created, otherwise the last created loader is re-used.
         */
        getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);
    }

    /**
     * Query the mDb and get all guests from the itemlist table
     *
     * @return Cursor containing the list of guests
     */
    private Cursor getAllItem() {
        return getContentResolver().query(
                ItemContract.ItemEntry.CONTENT_URI,
                null,
                null,
                null,
                ItemContract.ItemEntry.COLUMN_TIMESTAMP
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.add_item) {
            Toast.makeText(this, "new item", Toast.LENGTH_SHORT).show();
            Intent startInt = new Intent(ItemActivity.this, NewItemAddActivity.class);
            startActivity(startInt);
            mAdapter.swapCursor(getAllItem());
            mAdapter.notifyDataSetChanged();
        }
        return true;
    }

    @Override
    public void onClick(int Id) {
        Toast.makeText(this, String.format("ID: " + Id), Toast.LENGTH_SHORT).show();
        Intent startInt = new Intent(ItemActivity.this, ItemIncrsDecrs.class);
        startInt.putExtra("Pos", Id);
        startActivity(startInt);
        mAdapter.swapCursor(getAllItem());
        mAdapter.notifyDataSetChanged();
    }

    /**
     * This method is called after this activity has been paused or restarted.
     * Often, this is after new data has been inserted through an AddTaskActivity,
     * so this restarts the loader to re-query the underlying data for any changes.
     */
    @Override
    protected void onResume() {
        super.onResume();

        // re-queries for all tasks
        getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {
            // Initialize a Cursor, this will hold all the task data
            Cursor mTaskData = null;

            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                if (mTaskData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mTaskData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            // loadInBackground() performs asynchronous loading of data
            @Nullable
            @Override
            public Cursor loadInBackground() {
                // Will implement to load data

                // Query and load all task data in the background; sort by priority
                // [Hint] use a try/catch block to catch any errors in loading data
                try {
                    return getContentResolver().query(
                            ItemContract.ItemEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            ItemContract.ItemEntry.COLUMN_TIMESTAMP
                    );

                } catch (Exception e) {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            // deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Cursor data) {
                mTaskData = data;
                super.deliverResult(data);
            }
        };
    }


    /**
     * Called when a previously created loader has finished its load.
     *
     * @param loader The Loader that has finished.
     * @param data The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        // Update the data that the adapter uses to create ViewHolders
        mAdapter.swapCursor(data);
    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.
     * onLoaderReset removes any references this activity had to the loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
