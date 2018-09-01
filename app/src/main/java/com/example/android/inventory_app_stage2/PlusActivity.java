package com.example.android.inventory_app_stage2;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventory_app_stage2.data.BookStoreDbHelper;
import com.example.android.inventory_app_stage2.data.BookStoreContract.BookStoreEntry;

public class PlusActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_BOOK_LOADER = 0;
    private String supplierPhoneNumber;
    private EditText NameEditText;
    private EditText PriceEditText;
    private TextView QuantityTextView;
    private Button IncreaseQuantity;
    private Button DecreaseQuantity;
    private EditText SupplierNameEditText;
    private EditText SupplierPhoneNumberEditText;
    private EditText SupplierAddressEditText;
    private Uri CurrentBookUri;
    public BookStoreDbHelper DbHelper;

    private boolean BookhasChanged = false;

    private View.OnTouchListener TouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            BookhasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plus);

        Intent intent = getIntent();
        CurrentBookUri = intent.getData();


        if (CurrentBookUri != null) {
            setTitle(getString(R.string.editor_activity_title_edit_book));

            // Initialize Loader
            getLoaderManager().initLoader(EXISTING_BOOK_LOADER, null, this);
        } else {
            setTitle(getString( R.string.editor_activity_title_new_book));
            invalidateOptionsMenu();
        }

        // Find all Views we need to read user Input
        NameEditText = (EditText) findViewById(R.id.edit_book_name);
        PriceEditText = (EditText) findViewById(R.id.edit_book_price);
        QuantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        SupplierNameEditText = (EditText) findViewById(R.id.edit_supplier_name);
        SupplierAddressEditText = (EditText) findViewById(R.id.edit_supplier_address);
        SupplierPhoneNumberEditText = (EditText) findViewById(R.id.edit_supplier_phonenumber);
        IncreaseQuantity = findViewById(R.id.increment_button);
        DecreaseQuantity = findViewById(R.id.decrement_button);

        IncreaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentQuantityString = QuantityTextView.getText().toString();
                int currentQuantityValue;
                if (currentQuantityString.length() != 0) {
                    currentQuantityValue = Integer.parseInt(currentQuantityString) + 1;
                    if (currentQuantityValue < 0) {
                        return;
                    }
                    QuantityTextView.setText(String.valueOf(currentQuantityValue));
                } else {
                    currentQuantityValue = 0;
                    QuantityTextView.setText(String.valueOf(currentQuantityValue));
                }
            }

        });

        DecreaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentQuantityString = QuantityTextView.getText().toString();
                int currentQuantityValue;
                if (currentQuantityString.length() == 0) {
                    currentQuantityValue = 0;
                    QuantityTextView.setText(String.valueOf(currentQuantityValue));
                } else {
                    currentQuantityValue = Integer.parseInt(currentQuantityString) - 1;
                    if (currentQuantityValue >= 0) {
                        QuantityTextView.setText(String.valueOf(currentQuantityValue));
                    }
                }
            }

        });

        DbHelper = new BookStoreDbHelper(this);

        NameEditText.setOnTouchListener(TouchListener);
        PriceEditText.setOnTouchListener(TouchListener);
        QuantityTextView.setOnTouchListener(TouchListener);
        IncreaseQuantity.setOnTouchListener(TouchListener);
        DecreaseQuantity.setOnTouchListener(TouchListener);
        SupplierNameEditText.setOnTouchListener(TouchListener);
        SupplierAddressEditText.setOnTouchListener(TouchListener);
        SupplierPhoneNumberEditText.setOnTouchListener(TouchListener);
    }

    private void saveBook() {
        String nameString = NameEditText.getText().toString().trim();
        String priceString = PriceEditText.getText().toString().trim();
        String quantityString = QuantityTextView.getText().toString().trim();
        String supplierNameString = SupplierNameEditText.getText().toString().trim();
        String supplierAddressString = SupplierAddressEditText.getText().toString().trim();
        String supplierPhoneNumberString = SupplierPhoneNumberEditText.getText().toString().trim();

        if (TextUtils.isEmpty(nameString) || TextUtils.isEmpty(priceString) ||
                TextUtils.isEmpty(quantityString) ||
                TextUtils.isEmpty(supplierNameString) || TextUtils.isEmpty(supplierPhoneNumberString)) {
            Toast.makeText(this, getString(R.string.required), Toast.LENGTH_SHORT).show();
            return;
        }

        int price = Integer.parseInt(priceString);
        int quantity = Integer.parseInt(quantityString);

        ContentValues values = new ContentValues();
        values.put(BookStoreEntry.COLUMN_BOOK_NAME, nameString);
        values.put(BookStoreEntry.COLUMN_BOOK_PRICE, price);
        values.put(BookStoreEntry.COLUMN_BOOK_QUANTITY, quantity);
        values.put(BookStoreEntry.COLUMN_BOOK_SUPPLIERNAME, supplierNameString);
        values.put(BookStoreEntry.COLUMN_BOOK_SUPPLIERADDRESS, supplierAddressString);
        values.put(BookStoreEntry.COLUMN_BOOK_SUPPLIERPHONENUMBER, supplierPhoneNumberString);

        if (CurrentBookUri == null) {
            Uri newUri = getContentResolver().insert(BookStoreEntry.CONTENT_URI, values);
            if (newUri == null) {
                Toast.makeText(this, getString(R.string.editor_insert_book_failed),
                        Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(this, getString(R.string.editor_insert_book_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            int rowsAffected = getContentResolver().update(CurrentBookUri, values, null, null);

            if (rowsAffected == 0) {

                Toast.makeText(this, getString(R.string.editor_update_book_failed),
                        Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(this, getString(R.string.editor_update_book_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

    /**
     *  Perform the Deletion of the book in the Database
     */
    private void deleteBook() {
        // Only perform the delete if this is an existing book.
        if (CurrentBookUri != null) {

            int rowsDeleted = getContentResolver().delete(CurrentBookUri, null, null);
            // Show a toast message if the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted there was an error with the delete
                Toast.makeText(this, getString(R.string.editor_delete_book_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Or the delete was successful play Toast
                Toast.makeText(this, getString(R.string.delete_all_books),
                        Toast.LENGTH_SHORT).show();
            }
        }
        // Close Activity
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_plus, menu);
        return true;
    }

    /**
     *  Method Updating (Menu items can be hidden or made visible)
     */

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (CurrentBookUri == null) {
            MenuItem menuItem;
            menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
            menuItem = menu.findItem(R.id.action_contact_supplier);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //  User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                saveBook();
                finish();
                return true;
            // Respond to a Click / Menu Option
            case R.id.action_contact_supplier:
                // Contact Supplier via intent
                supplierContact();
                return true;
            // Respond to a click on the Delete Menu Option
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                if (!BookhasChanged) {
                    NavUtils.navigateUpFromSameTask( PlusActivity.this );
                    return true;
                } else {
                    DialogInterface.OnClickListener discardButtonClickListener =
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // User clicked "Discard" button, navigate to parent activity.
                                    NavUtils.navigateUpFromSameTask( PlusActivity.this );
                                }
                            };
                    showUnsavedChangesDialog( discardButtonClickListener );
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Contact to supplier method
     */
    private void supplierContact() {
        Intent supplierContactIntent = new Intent(Intent.ACTION_DIAL);
        supplierContactIntent.setData(Uri.parse("tel:" + supplierPhoneNumber));
        startActivity(supplierContactIntent);
    }

    /**
     * This method is called when the back button is pressed
     */
    @Override
    public void onBackPressed() {
        if (BookhasChanged) {
            // Otherwise if there are unsaved changes, setup a dialog to warn the user.
            // Create a click listener to handle the user confirming that changes should be discarded.
            DialogInterface.OnClickListener discardButtonClickListener =
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // User clicked "Discard" button, close the current activity.
                            finish();
                        }
                    };
            // Show dialog that there are unsaved changes
            showUnsavedChangesDialog( discardButtonClickListener );
        } else {
            super.onBackPressed();
            return;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String[] projection = {
                BookStoreEntry._ID,
                BookStoreEntry.COLUMN_BOOK_NAME,
                BookStoreEntry.COLUMN_BOOK_PRICE,
                BookStoreEntry.COLUMN_BOOK_QUANTITY,
                BookStoreEntry.COLUMN_BOOK_SUPPLIERNAME,
                BookStoreEntry.COLUMN_BOOK_SUPPLIERADDRESS,
                BookStoreEntry.COLUMN_BOOK_SUPPLIERPHONENUMBER
        };
        return new CursorLoader(this, CurrentBookUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null && cursor.getCount() >= 1) {

            if (cursor.moveToFirst()) {
                int nameColumnIndex = cursor.getColumnIndex( BookStoreEntry.COLUMN_BOOK_NAME );
                int priceColumnIndex = cursor.getColumnIndex( BookStoreEntry.COLUMN_BOOK_PRICE );
                int quantityColumnIndex = cursor.getColumnIndex( BookStoreEntry.COLUMN_BOOK_QUANTITY );
                int supplierNameColumnIndex = cursor.getColumnIndex( BookStoreEntry.COLUMN_BOOK_SUPPLIERNAME );
                int supplierAddressColumnIndex = cursor.getColumnIndex( BookStoreEntry.COLUMN_BOOK_SUPPLIERADDRESS );
                int supplierPhoneNumberColumnIndex = cursor.getColumnIndex( BookStoreEntry.COLUMN_BOOK_SUPPLIERPHONENUMBER );

                // Read the book attributes from the Cursor for the current book
                String bookName = cursor.getString( nameColumnIndex );

                int price = cursor.getInt( priceColumnIndex );
                String bookPrice = String.valueOf( price );

                final int quantity = cursor.getInt( quantityColumnIndex );
                String bookQuantity = String.valueOf( quantity );

                String supplierName = cursor.getString( supplierNameColumnIndex );

                String supplierAddress = cursor.getString( supplierAddressColumnIndex );

                supplierPhoneNumber = cursor.getString( supplierPhoneNumberColumnIndex );


                NameEditText.setText( bookName );
                PriceEditText.setText( bookPrice );
                QuantityTextView.setText( bookQuantity );
                SupplierNameEditText.setText( supplierName );
                SupplierAddressEditText.setText( supplierAddress );
                SupplierPhoneNumberEditText.setText( String.valueOf( supplierPhoneNumber ) );
            }
        } else {
            return;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        NameEditText.setText("");
        PriceEditText.setText("");
        QuantityTextView.setText("");
        SupplierNameEditText.setText("");
        SupplierAddressEditText.setText("");
        SupplierPhoneNumberEditText.setText("");
    }

    /**
     * Shows a dialog
     * @param discardButtonClickListener is the click listener
     *
     */
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.continue_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteBook();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}