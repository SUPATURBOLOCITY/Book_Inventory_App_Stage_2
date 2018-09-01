package com.example.android.inventory_app_stage2.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class BookStoreContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.inventory_app_stage_2";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_BOOKS = "Book";

    public BookStoreContract() {
    }

    public static abstract class BookStoreEntry implements BaseColumns {
        private BookStoreEntry() {

        }

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BOOKS);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;

        public static final String TABLE_NAME = "Book";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_BOOK_NAME = "Book_Name";
        public static final String COLUMN_BOOK_PRICE = "Price";
        public static final String COLUMN_BOOK_QUANTITY = "Quantity";
        public static final String COLUMN_BOOK_SUPPLIERNAME = "SupplierName";
        public static final String COLUMN_BOOK_SUPPLIERADDRESS = "SupplierAddress";
        public static final String COLUMN_BOOK_SUPPLIERPHONENUMBER = "SupplierPhoneNumber";
    }
}