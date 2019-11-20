package com.shashankesh.ms.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.shashankesh.ms.Data.ItemContract.*;
import androidx.annotation.Nullable;

/*
 *File ItemDbHelper
 *Created by Shashankesh Upadhyay
 *on Monday, November, 2019
 */
public class ItemDbHelper extends SQLiteOpenHelper {

    // The database name
    private static final String DATABASE_NAME = "itemlist.db";

    // If you change the database schema, you must increment the database version
    private static final int DATABASE_VERSION = 1;

    public ItemDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a table to hold waitlist data
        final String SQL_CREATE_WAITLIST_TABLE = "CREATE TABLE " + ItemEntry.TABLE_NAME + " (" +
                ItemEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ItemEntry.COLUMN_ITEM_COUNT + " INTEGER NOT NULL, " +
                ItemEntry.COLUMN_ITEM_NAME + " TEXT NOT NULL, " +
                ItemEntry.COLUMN_ITEM_VENDOR + " TEXT NOT NULL, " +
                ItemEntry.COLUMN_ITEM_TH + " INTEGER NOT NULL, " +
                ItemEntry.COLUMN_ITEM_ORDER + " INTEGER NOT NULL DEFAULT 0, " +
                ItemEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                "); ";

        //  Execute the query by calling execSQL on sqLiteDatabase and pass the string query SQL_CREATE_WAITLIST_TABLE
        db.execSQL(SQL_CREATE_WAITLIST_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // For now simply drop the table and create a new one. This means if you change the
        // DATABASE_VERSION the table will be dropped.
        // In a production app, this method might be modified to ALTER the table
        // instead of dropping it, so that existing data is not deleted.
        // COMPLETED (9) Inside, execute a drop table query, and then call onCreate to re-create it
        db.execSQL("DROP TABLE IF EXISTS " + ItemEntry.TABLE_NAME);
        onCreate(db);
    }
}
