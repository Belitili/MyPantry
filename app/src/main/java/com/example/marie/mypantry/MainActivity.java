package com.example.marie.mypantry;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marie.mypantry.db.ProductDBContract;
import com.example.marie.mypantry.db.ProductDBHelper;

import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainScreen";

    private ProductDBHelper dbHelper;

    private ListView productListView;

    private ProductAdaptor adapter;
    //private ArrayAdapter<String> adapter;


    // + button to add new product, search button to search, x to go back to all products
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_product:
                this.addProductDialog();
                return true;
            case R.id.action_search_products:
                this.searchProductsDialog();
                return true;
            case R.id.action_show_all:
                this.printAllList();
                return true;
            case R.id.action_filter_location:
                this.locationFilterDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addProductDialog() {
        //alertdialog to add product + amount
        LinearLayout layout = new LinearLayout(this);
        final EditText productNameBox = new EditText(this);
        productNameBox.setHint(this.getString(R.string.product));
        layout.addView(productNameBox);

        final EditText productAmountBox = new EditText(this);
        productAmountBox.setHint(this.getString(R.string.amount));
        productAmountBox.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        layout.addView(productAmountBox);

        final Spinner unitSpinner = new Spinner(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.units_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(adapter);
        layout.addView(unitSpinner);

        final Spinner locationSpinner = new Spinner(this);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.locations_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(adapter2);
        layout.addView(locationSpinner);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(this.getString(R.string.add_product))
                .setView(layout)
                .setPositiveButton(this.getString(R.string.add), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (productNameBox.getText().toString().trim().equals("")) {
                            Toast.makeText(getApplicationContext(), "Product field was empty", Toast.LENGTH_LONG).show();
                        } else {
                            String product = String.valueOf(productNameBox.getText());
                            String amount = String.valueOf(productAmountBox.getText());
                            String amount_unit = unitSpinner.getSelectedItem().toString();
                            String location = locationSpinner.getSelectedItem().toString();
                            if (amount.toString().trim().equals("")) {
                                amount = "1";
                            }
                            Log.d(TAG, "Add: "+ amount + " " + product + ", location: " + location);
                            //add data to local db
                            SQLiteDatabase db = dbHelper.getWritableDatabase();
                            ContentValues values = new ContentValues();
                            values.put(ProductDBContract.ProductEntry.COLUMN_NAME_PRODUCT, product);
                            values.put(ProductDBContract.ProductEntry.COLUMN_NAME_AMOUNT, amount);
                            values.put(ProductDBContract.ProductEntry.COLUMN_NAME_AMT_UNIT, amount_unit);
                            values.put(ProductDBContract.ProductEntry.COLUMN_NAME_LOCATION, location);
                            db.insertWithOnConflict(ProductDBContract.ProductEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                            db.close();
                            printAllList();
                        }
                    }
                })
                .setNegativeButton(this.getString(R.string.cancel), null)
                .create();
        dialog.show();
        return;
    }

    private void searchProductsDialog() {
        final EditText searchProductEditText = new EditText(this);
        AlertDialog dialogSearch = new AlertDialog.Builder(this)
                .setTitle(this.getString(R.string.search_products))
                .setView(searchProductEditText)
                .setPositiveButton(this.getString(R.string.search), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogSearch, int which) {
                        String searchString = String.valueOf(searchProductEditText.getText());
                        Log.d(TAG, "Search for: " + searchString);
                        //search in local db and print list
                        printSearchList(searchString);
                    }
                })
                .setNegativeButton(this.getString(R.string.cancel), null)
                .create();
        dialogSearch.show();
        return;
    }

    private void locationFilterDialog() {
        final Spinner locationSpinner = new Spinner(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.locations_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(adapter);

        AlertDialog dialogSearch = new AlertDialog.Builder(this)
                .setTitle(this.getString(R.string.filter_location))
                .setView(locationSpinner)
                .setPositiveButton(this.getString(R.string.search), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogSearch, int which) {
                        String locationString = locationSpinner.getSelectedItem().toString();
                        Log.d(TAG, "Filter by location: " + locationString);
                        //search in local db and print list
                        printLocationFilteredList(locationString);
                    }
                })
                .setNegativeButton(this.getString(R.string.cancel), null)
                .create();
        dialogSearch.show();
        return;
    }

    //print list of all products and show menu
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new ProductDBHelper(this);
        productListView = (ListView) findViewById(R.id.list_products);
        this.printAllList();
    }

    //print list based on searchstring
    private void printSearchList(String searchString) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String whereClause = ProductDBContract.ProductEntry.COLUMN_NAME_PRODUCT +  " LIKE ?";
        searchString = "%"+searchString+"%"; //case insensitive, can be in middle of string
        String[] whereArgs = new String[]{searchString};
        Cursor cursor = db.query(ProductDBContract.ProductEntry.TABLE_NAME,
                new String[]{ProductDBContract.ProductEntry._ID,
                        ProductDBContract.ProductEntry.COLUMN_NAME_PRODUCT,
                        ProductDBContract.ProductEntry.COLUMN_NAME_AMOUNT,
                        ProductDBContract.ProductEntry.COLUMN_NAME_AMT_UNIT,
                        ProductDBContract.ProductEntry.COLUMN_NAME_LOCATION},
                whereClause, whereArgs, null, null, null);
        Log.d(TAG, "cursor: " + cursor);

        this.printFromQueryList(cursor);

        cursor.close();
        Log.d(TAG, "ProductList updated");
        db.close();
        return;
    }

    //print list based on location filter
    private void printLocationFilteredList(String locationString) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String whereClause = ProductDBContract.ProductEntry.COLUMN_NAME_LOCATION +  " = ?";
        String[] whereArgs = new String[]{locationString};
        Cursor cursor = db.query(ProductDBContract.ProductEntry.TABLE_NAME,
                new String[]{ProductDBContract.ProductEntry._ID,
                        ProductDBContract.ProductEntry.COLUMN_NAME_PRODUCT,
                        ProductDBContract.ProductEntry.COLUMN_NAME_AMOUNT,
                        ProductDBContract.ProductEntry.COLUMN_NAME_AMT_UNIT,
                        ProductDBContract.ProductEntry.COLUMN_NAME_LOCATION},
                whereClause, whereArgs, null, null, null);
        Log.d(TAG, "cursor: " + cursor);

        this.printFromQueryList(cursor);

        cursor.close();
        Log.d(TAG, "ProductList updated");
        db.close();
        return;
    }

    //print all products
    private void printAllList() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(ProductDBContract.ProductEntry.TABLE_NAME,
                new String[]{ProductDBContract.ProductEntry._ID,
                        ProductDBContract.ProductEntry.COLUMN_NAME_PRODUCT,
                        ProductDBContract.ProductEntry.COLUMN_NAME_AMOUNT,
                        ProductDBContract.ProductEntry.COLUMN_NAME_AMT_UNIT,
                        ProductDBContract.ProductEntry.COLUMN_NAME_LOCATION},
                null, null, null, null, null);
        this.printFromQueryList(cursor);

        cursor.close();
        Log.d(TAG, "ProductList updated");
        db.close();
        return;
    }

    //print from query
    private void printFromQueryList(Cursor cursor) {
        Vector<Product> products = new Vector<Product>();
        //ArrayList<String> products = new ArrayList<String>();
        while(cursor.moveToNext()) {
            int i_prod = cursor.getColumnIndex(ProductDBContract.ProductEntry.COLUMN_NAME_PRODUCT);
            int i_amt = cursor.getColumnIndex(ProductDBContract.ProductEntry.COLUMN_NAME_AMOUNT);
            int i_amt_unt = cursor.getColumnIndex(ProductDBContract.ProductEntry.COLUMN_NAME_AMT_UNIT);
            int i_loc = cursor.getColumnIndex(ProductDBContract.ProductEntry.COLUMN_NAME_LOCATION);
            products.add(new Product(cursor.getString(i_prod), Double.parseDouble(cursor.getString(i_amt)), cursor.getString(i_amt_unt), cursor.getString(i_loc)));
            //products.add(cursor.getString(i_prod));
        }

        if (adapter == null) {
            //adapter = new ArrayAdapter<>(this, R.layout.item_product, R.id.product_name, products);
            //change: custom product adaptor
            adapter = new ProductAdaptor(this, R.layout.item_product, products);
            productListView.setAdapter(adapter);
        } else {
            adapter.clear();
            adapter.addAll(products);
            adapter.notifyDataSetChanged();
        }
        return;
    }

    //Render submenu for extra functions
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //sourcetype of r.menu is menu
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //when you press x next to product
    public void deleteProduct(View view) {
        View parent = (View) view.getParent();
        TextView productTextView = (TextView) parent.findViewById(R.id.product_name);
        //dahm string is name + amount => fix custom productadaptor
        String product = String.valueOf(productTextView.getText());
        Log.d(TAG, "Remove: "+ product);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(ProductDBContract.ProductEntry.TABLE_NAME,
                ProductDBContract.ProductEntry.COLUMN_NAME_PRODUCT + "= ?",
                new String[]{product});
        db.close();
        printAllList();
        return;
    }
}
