package com.example.android.inventory_app_stage2;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.inventory_app_stage2.data.BookStoreContract.BookStoreEntry;

public class BookStoreCursorAdapter extends CursorAdapter {

    BookStoreCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list_item
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        TextView nameTextView = (TextView) view.findViewById(R.id.book_name);
        TextView priceTextView = (TextView) view.findViewById(R.id.price);
        TextView quantityTextView = (TextView) view.findViewById(R.id.quantity);

        // Find Columns
        int nameColumnIndex = cursor.getColumnIndex(BookStoreEntry.COLUMN_BOOK_NAME);
        int priceColumnIndex = cursor.getColumnIndex(BookStoreEntry.COLUMN_BOOK_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(BookStoreEntry.COLUMN_BOOK_QUANTITY);

        // Read Attributes
        String book_name = cursor.getString(nameColumnIndex);
        int price = cursor.getInt(priceColumnIndex);
        String bookPrice = "Price : " + String.valueOf(price) + " Euro ";
        int quantity = cursor.getInt(quantityColumnIndex);
        String quantityBook = "In Stock : " + String.valueOf(quantity);

        // Update TextViews
        nameTextView.setText(book_name);
        priceTextView.setText(bookPrice);
        quantityTextView.setText(quantityBook);

        int bookIdColumnIndex = cursor.getColumnIndex(BookStoreEntry._ID);
        final int id = Integer.parseInt(cursor.getString(bookIdColumnIndex));
        final int quantityValue = cursor.getInt(quantityColumnIndex);

        Button soldButton = view.findViewById(R.id.soldButton);
        soldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri currentBookUri = ContentUris.withAppendedId(BookStoreEntry.CONTENT_URI, id);
                String updatedQuantity = String.valueOf(quantityValue - 1);
                if (Integer.parseInt(updatedQuantity) >= 0) {
                    ContentValues values = new ContentValues();
                    values.put(BookStoreEntry.COLUMN_BOOK_QUANTITY, updatedQuantity);
                    context.getContentResolver().update(currentBookUri, values, null, null);
                }
            }
        });
    }
}