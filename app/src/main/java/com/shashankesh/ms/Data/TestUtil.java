package com.shashankesh.ms.Data;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.shashankesh.ms.Data.ItemContract.*;
import java.util.ArrayList;
import java.util.List;

/*
 *File TestUtil
 *Created by Shashankesh Upadhyay
 *on Monday, November, 2019
 */public class TestUtil {
    
    public static void insertFakeData(){
//        if(db == null){
//            return;
//        }
        //create a list of fake guests
        List<ContentValues> list = new ArrayList<ContentValues>();

        TaskContentProvider cp = new TaskContentProvider();

        ContentValues cv = new ContentValues();
        cv.put(ItemEntry.COLUMN_ITEM_NAME, "TestData1");
        cv.put(ItemEntry.COLUMN_ITEM_COUNT, 12);
        cv.put(ItemEntry.COLUMN_ITEM_VENDOR, "skm");
        cv.put(ItemEntry.COLUMN_ITEM_TH, 6);
        cv.put(ItemEntry.COLUMN_ITEM_ORDER, 0);
        list.add(cv);

        cv = new ContentValues();
        cv.put(ItemEntry.COLUMN_ITEM_NAME, "TestData2");
        cv.put(ItemEntry.COLUMN_ITEM_COUNT, 2);
        cv.put(ItemEntry.COLUMN_ITEM_TH, 6);
        cv.put(ItemEntry.COLUMN_ITEM_VENDOR, "skm");
        cv.put(ItemEntry.COLUMN_ITEM_ORDER, 1);
        list.add(cv);

        cv = new ContentValues();
        cv.put(ItemEntry.COLUMN_ITEM_NAME, "TestData2");
        cv.put(ItemEntry.COLUMN_ITEM_COUNT, 99);
        cv.put(ItemEntry.COLUMN_ITEM_TH, 6);
        cv.put(ItemEntry.COLUMN_ITEM_VENDOR, "smi");
        cv.put(ItemEntry.COLUMN_ITEM_ORDER, 0);

        list.add(cv);

        cv = new ContentValues();
        cv.put(ItemEntry.COLUMN_ITEM_NAME, "TestData3");
        cv.put(ItemEntry.COLUMN_ITEM_COUNT, 1);
        cv.put(ItemEntry.COLUMN_ITEM_TH, 6);
        cv.put(ItemEntry.COLUMN_ITEM_VENDOR, "pro");
        cv.put(ItemEntry.COLUMN_ITEM_ORDER, 1);

        list.add(cv);

        cv = new ContentValues();
        cv.put(ItemEntry.COLUMN_ITEM_NAME, "TestData4");
        cv.put(ItemEntry.COLUMN_ITEM_COUNT, 45);
        cv.put(ItemEntry.COLUMN_ITEM_TH, 6);
        cv.put(ItemEntry.COLUMN_ITEM_ORDER, 0);
        cv.put(ItemEntry.COLUMN_ITEM_VENDOR, "bajaj");
        list.add(cv);

        //insert all guests in one transaction
        try
        {
            //db.beginTransaction();
            //clear the table first
            //db.delete (ItemEntry.TABLE_NAME,null,null);
            //go through the list and add one by one
            for(ContentValues c:list){
                cp.insert(
                        ItemEntry.CONTENT_URI,
                        c
                );
            }
            //db.setTransactionSuccessful();
        }
        catch (SQLException e) {
            //too bad :(
        }
        finally
        {
            //db.endTransaction();
        }

    }
}
