package com.shashankesh.ms.Data;

import android.net.Uri;
import android.provider.BaseColumns;

/*
 *File ItemContract
 *Created by Shashankesh Upadhyay
 *on Monday, November, 2019
 */public class ItemContract {
    // The authority, which is how your code knows which Content Provider to access
    public static final String AUTHORITY = "com.shashankesh.ms";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    // This is the path for the "tasks" directory
    public static final String PATH_TASKS = "items";

    public static final class ItemEntry implements BaseColumns {
        // TaskEntry content URI = base content URI + path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASKS).build();

        public static final String TABLE_NAME = "items";
        public static final String COLUMN_ITEM_NAME = "itemName";
        public static final String COLUMN_ITEM_COUNT = "item_count";
        public static final String COLUMN_ITEM_VENDOR= "item_vendor";
        public static final String COLUMN_ITEM_TH= "item_th";
        public static final String COLUMN_ITEM_ORDER= "item_to_order";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}
