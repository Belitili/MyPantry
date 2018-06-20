package com.example.marie.mypantry.db;

import android.provider.BaseColumns;

public final class ProductDBContract {

    public static final String TAG = "ProductDatabase";

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ProductTables.db";

    private ProductDBContract() {}

    public static class ProductEntry implements BaseColumns {
        public static final String TABLE_NAME = "products";

        public static final String COLUMN_NAME_PRODUCT = "product";
        public static final String COLUMN_NAME_AMOUNT = "amount";
    }
}
