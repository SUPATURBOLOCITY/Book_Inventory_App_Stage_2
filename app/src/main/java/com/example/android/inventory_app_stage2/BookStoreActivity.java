package com.example.android.inventory_app_stage2;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.inventory_app_stage2.data.BookStoreContract.BookStoreEntry;

class BookStoreActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int BOOK_LOADER = 0;

    BookStoreCursorAdapter CursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookstore);


        FloatingActionButton plus = (FloatingActionButton) findViewById(R.id.plus);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BookStoreActivity.this, PlusActivity.class);
                startActivity(intent);
            }
        });

        ListView bookListView = (ListView) findViewById(R.id.list);


        View emptyView = findViewById(R.id.empty_view);
        bookListView.setEmptyView(emptyView);


        CursorAdapter = new BookStoreCursorAdapter(this, null);
        bookListView.setAdapter(CursorAdapter);

        // Setup Item Click Listener
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                //Create new intent to go to editor activity
                Intent intent = new Intent(BookStoreActivity.this, PlusActivity.class);
                Uri currentBookUri = ContentUris.withAppendedId(BookStoreEntry.CONTENT_URI, id);

                //set the URI on to the data field of the intent
                intent.setData(currentBookUri);
                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(BOOK_LOADER, null, this);
    }
    public void insertBook() {

        /**
         *  Create a Content Values Object
         */
        ContentValues values = new ContentValues();
        values.put(BookStoreEntry.COLUMN_BOOK_NAME, "");
        values.put(BookStoreEntry.COLUMN_BOOK_PRICE,"");
        values.put(BookStoreEntry.COLUMN_BOOK_QUANTITY,"");
        values.put(BookStoreEntry.COLUMN_BOOK_SUPPLIERNAME, "");
        values.put(BookStoreEntry.COLUMN_BOOK_SUPPLIERADDRESS, "");
        values.put(BookStoreEntry.COLUMN_BOOK_SUPPLIERPHONENUMBER, "000-000-0000");

        Uri newUri = getContentResolver().insert(BookStoreEntry.CONTENT_URI, values);
    }

    /**
     *  Helper Method to Delete all Books in the Database
     */
    public void deleteAllBooks() {
        int rowsDeleted = getContentResolver().delete(BookStoreEntry.CONTENT_URI, null, null);
        Toast.makeText(this, R.string.delete_all_books,
                Toast.LENGTH_SHORT).show();
    }


    public void showDeleteConfirmationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_all_dialog);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                deleteAllBooks();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if (dialog == null) {
                    return;
                }
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bookstore, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_insert_dummy_data:
                insertBook();
                return true;

            case R.id.action_delete_all_entries:
                showDeleteConfirmationDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {

        String[] projection = {
                BookStoreEntry._ID,
                BookStoreEntry.COLUMN_BOOK_NAME,
                BookStoreEntry.COLUMN_BOOK_PRICE,
                BookStoreEntry.COLUMN_BOOK_QUANTITY,
                BookStoreEntry.COLUMN_BOOK_SUPPLIERADDRESS};

        return new CursorLoader(this,
                BookStoreEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        CursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        CursorAdapter.swapCursor(null);
    }
}