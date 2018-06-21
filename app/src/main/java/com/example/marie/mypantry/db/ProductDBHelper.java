package com.example.marie.mypantry.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ProductDBHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_PRODUCT_TABLE =
            "CREATE TABLE " + ProductDBContract.ProductEntry.TABLE_NAME + " (" +
                    ProductDBContract.ProductEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    ProductDBContract.ProductEntry.COLUMN_NAME_PRODUCT + " TEXT NOT NULL," +
                    ProductDBContract.ProductEntry.COLUMN_NAME_AMOUNT + " REAL NOT NULL, " +
                    ProductDBContract.ProductEntry.COLUMN_NAME_AMT_UNIT + " TEXT NOT NULL," +
                    ProductDBContract.ProductEntry.COLUMN_NAME_LOCATION + " TEXT NOT NULL," +
                    ProductDBContract.ProductEntry.COLUMN_NAME_SECONDARY_LOCATION + " TEXT)";

    private static final String SQL_DELETE_PRODUCT_TABLE =
            "DROP TABLE IF EXISTS " + ProductDBContract.ProductEntry.TABLE_NAME;

    public ProductDBHelper(Context context) {
        super(context, ProductDBContract.DATABASE_NAME, null, ProductDBContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("Creating DB Query: " + SQL_CREATE_PRODUCT_TABLE);
        db.execSQL(SQL_CREATE_PRODUCT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //if just cache for online data?
        Log.w(ProductDBContract.TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL(SQL_DELETE_PRODUCT_TABLE);
        onCreate(db);
    }
}
