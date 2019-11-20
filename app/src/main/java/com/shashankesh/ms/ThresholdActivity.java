package com.shashankesh.ms;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.shashankesh.ms.Data.ItemContract;
import com.shashankesh.ms.Data.ItemDbHelper;

import java.util.HashMap;

public class ThresholdActivity extends AppCompatActivity implements ThresholdAdapter.ThresholdAdapterOnClickHandler, LoaderManager.LoaderCallbacks<Cursor> {
    private ThresholdAdapter mAdapter;
    Toolbar toolbar;
    private final int ADMIN_PASS = 1234;
    private static final int TASK_LOADER_ID = 1;
    private static final String TAG = ThresholdActivity.class.getSimpleName();
    private int mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_threshold);

        toolbar = (Toolbar) findViewById(R.id.toolbar_th_activity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Threshold Values");
        RecyclerView ItemlistRecyclerView;

        // Set local attributes to corresponding views
        ItemlistRecyclerView = (RecyclerView) this.findViewById(R.id.recycler_view_th_activity);

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
        Cursor cursor = getAllItem();

        //  Pass the entire cursor to the adapter rather than just the count
        // Create an adapter for that cursor to display the data
        mAdapter = new ThresholdAdapter(cursor, this, this);

        // Link the adapter to the RecyclerView
        ItemlistRecyclerView.setAdapter(mAdapter);
        /*
         Ensure a loader is initialized and active. If the loader doesn't already exist, one is
         created, otherwise the last created loader is re-used.
         */
        getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);
    }

    private Cursor getAllItem() {
        Uri uri = ItemContract.ItemEntry.CONTENT_URI;
        return getContentResolver().query(
                uri,
                null,
                null,
                null,
                ItemContract.ItemEntry.COLUMN_TIMESTAMP);
    }

    @Override
    public void onClick(int ID, int position) {
        showAddItemDialog(ThresholdActivity.this, ID);
        //Toast.makeText(ThresholdActivity.this,"Update Threshold "+position,Toast.LENGTH_SHORT).show();
        mPosition = position;
    }

    private void showAddItemDialog(ThresholdActivity thresholdActivity, int ID) {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_box, null);
        TextInputEditText taskEditText = (TextInputEditText) view.findViewById(R.id.dialog_edit_text);
        taskEditText.setHint("Pass");
        AlertDialog dialog = new AlertDialog.Builder(thresholdActivity)
                .setTitle("Update Threshold")
                .setMessage("Enter Employee ID")
                .setView(view)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = taskEditText.getText().toString().trim();
                        if (Integer.valueOf(task) == ADMIN_PASS) {
                            showThUpdateDialog(thresholdActivity, ID);
                        } else {
                            Toast.makeText(ThresholdActivity.this, "Not Allowed To Update", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    private void showThUpdateDialog(ThresholdActivity thresholdActivity, int ID) {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_box, null);
        TextInputEditText taskEditText = (TextInputEditText) view.findViewById(R.id.dialog_edit_text);
        taskEditText.setHint("Threshold");
        taskEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        AlertDialog dialog = new AlertDialog.Builder(thresholdActivity)
                .setTitle("Update Threshold")
                .setMessage("Enter New Threshold")
                .setView(view)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = taskEditText.getText().toString().trim();
                        if(task.equals("")||task==null||task.equals("0")) {
                            Toast.makeText(ThresholdActivity.this, "Threshold cannot be empty", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        UpdateThreshold(task, ID);
                        startActivity(new Intent(ThresholdActivity.this,ThresholdActivity.class));
                        Toast.makeText(ThresholdActivity.this, task, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    private void UpdateThreshold(String task, int ID) {
        checForOrderUpdation(task,ID);
        ContentValues cv = new ContentValues();
        cv.put(ItemContract.ItemEntry.COLUMN_ITEM_TH, task);
        getContentResolver().update(
                ItemContract.ItemEntry.CONTENT_URI,
                cv,
                ItemContract.ItemEntry._ID + "=?",
                new String[]{String.valueOf(ID)}
        );
    }

    private void checForOrderUpdation(String task, int id) {
        int itemCount = -1;
        Cursor cursor = getContentResolver().query(
                ItemContract.ItemEntry.CONTENT_URI,
                null,
                ItemContract.ItemEntry._ID+"=?",
                new String[]{String.valueOf(id)},
                null
        );
        if(cursor.moveToFirst()){
             itemCount = cursor.getInt(cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_COUNT));
        }
        if (itemCount!=-1 && Integer.valueOf(task)>=itemCount){
            ContentValues cv  = new ContentValues();
            cv.put(ItemContract.ItemEntry.COLUMN_ITEM_ORDER,"1");
            getContentResolver().update(
                    ItemContract.ItemEntry.CONTENT_URI,
                    cv,
                    ItemContract.ItemEntry._ID+"=?",
                    new String[]{String.valueOf(id)}
            );
        }else if(itemCount!=-1 && Integer.valueOf(task)<itemCount){
            ContentValues cv  = new ContentValues();
            cv.put(ItemContract.ItemEntry.COLUMN_ITEM_ORDER,"0");
            getContentResolver().update(
                    ItemContract.ItemEntry.CONTENT_URI,
                    cv,
                    ItemContract.ItemEntry._ID+"=?",
                    new String[]{String.valueOf(id)}
            );
        }
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
                    if (args == null) {
                        return getContentResolver().query(
                                ItemContract.ItemEntry.CONTENT_URI,
                                null,
                                null,
                                null,
                                ItemContract.ItemEntry.COLUMN_TIMESTAMP
                        );
                    }else {
                        String task = args.getString("TH");
                        int ID = args.getInt("ID");
                        ContentValues cv = new ContentValues();
                        cv.put(ItemContract.ItemEntry.COLUMN_ITEM_TH, task);

                        getContentResolver().update(
                                ItemContract.ItemEntry.CONTENT_URI,
                                cv,
                                ItemContract.ItemEntry._ID + "=?",
                                new String[]{String.valueOf(ID)}
                        );
                        return getContentResolver().query(
                                ItemContract.ItemEntry.CONTENT_URI,
                                null,
                                null,
                                null,
                                ItemContract.ItemEntry.COLUMN_TIMESTAMP
                        );
                    }

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
     * @param data   The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        // Update the data that the adapter uses to create ViewHolders
        mAdapter.swapCursor(data, mPosition);
        //mAdapter.notifyItemChanged(mPosition);
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
        mAdapter.swapCursor(null, mPosition);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //moveTaskToBack(true);
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
}
