package com.example.lab5;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import java.io.ByteArrayOutputStream;

public class MainActivity extends Activity {
    final String LOG_TAG = "myLogs";
    DBHelper dbHelper;

    private Button btnAdd;
    private Button btnRead;
    private Button btnClear;
    private Button btnUpdate;
    private Button btnDelete;
    private Button btnSort;
    private EditText displayTextView;
    private RadioGroup radioGroup;
    private Button btnReadOne;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        btnRead = findViewById(R.id.btnRead);
        btnClear = findViewById(R.id.btnClear);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        btnSort = findViewById(R.id.btnSort);
        radioGroup = findViewById(R.id.radioGroup);
        btnReadOne = findViewById(R.id.btnReadOne);

        displayTextView = findViewById(R.id.dbInfo);
        displayTextView.setText("");

        btnAdd.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddActivity.class);
            startActivity(intent);
            displayTextView.setText("");
        });
        btnUpdate.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
            startActivity(intent);
            displayTextView.setText("");
        });
        btnDelete.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, DeleteActivity.class);
            startActivity(intent);
            displayTextView.setText("");
        });
        btnReadOne.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ReadOneActivity.class);
            startActivity(intent);
            displayTextView.setText("");
        });

        btnRead.setOnClickListener(view -> readInfo());
        btnClear.setOnClickListener(view -> {
            clearInfo();
            displayTextView.setText(R.string.onClear);
        });
        btnSort.setOnClickListener(view -> sortInfo());

        dbHelper = new DBHelper(this);
    }

    private void sortInfo() {
        int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        String[] projection = {
                Contract.GuestEntry._ID,
                Contract.GuestEntry.COLUMN_FIRST_NAME,
                Contract.GuestEntry.COLUMN_LAST_NAME,
                Contract.GuestEntry.COLUMN_EMAIL,
                Contract.GuestEntry.COLUMN_ADDRESS};

        switch (checkedRadioButtonId){
            case R.id.radioFirstName:
                cursor = db.query(
                        Contract.GuestEntry.TABLE_NAME,   // таблиця
                        projection,            // стовпці
                        null,                  // стовпці для умови WHERE
                        null,                  // значення для умови WHERE
                        null,                  // Don't group the rows
                        null,                  // Don't filter by row groups
                        Contract.GuestEntry.COLUMN_FIRST_NAME + " ASC");                   // порядок сортування
                break;
            case R.id.radioLastName:
                cursor = db.query(
                        Contract.GuestEntry.TABLE_NAME,   // таблиця
                        projection,            // стовпці
                        null,                  // стовпці для умови WHERE
                        null,                  // значення для умови WHERE
                        null,                  // Don't group the rows
                        null,                  // Don't filter by row groups
                        Contract.GuestEntry.COLUMN_LAST_NAME + " ASC");                   // порядок сортування
                break;
            case R.id.radioEmail:
                cursor = db.query(
                        Contract.GuestEntry.TABLE_NAME,   // таблиця
                        projection,            // стовпці
                        null,                  // стовпці для умови WHERE
                        null,                  // значення для умови WHERE
                        null,                  // Don't group the rows
                        null,                  // Don't filter by row groups
                        "SUBSTR(" + Contract.GuestEntry.COLUMN_EMAIL + ", 1, 1) ASC");                   // порядок сортування
                break;
        }
        displayInfo(cursor);
        db.close();
    }

    private void clearInfo() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(Contract.GuestEntry.TABLE_NAME, null, null);
        db.close();
    }
    public void readInfo(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                Contract.GuestEntry._ID,
                Contract.GuestEntry.COLUMN_FIRST_NAME,
                Contract.GuestEntry.COLUMN_LAST_NAME,
                Contract.GuestEntry.COLUMN_EMAIL,
                Contract.GuestEntry.COLUMN_ADDRESS};

        // Робимо запит
        Cursor cursor = db.query(
                Contract.GuestEntry.TABLE_NAME,   // таблиця
                projection,            // стовпці
                null,                  // стовпці для умови WHERE
                null,                  // значення для умови WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // порядок сортування

        displayInfo(cursor);
        db.close();
    }

    public void displayInfo(Cursor cursor){
        try {
            String label = "Таблиця містить " + cursor.getCount() + " акаунтів.\n\n";
            displayTextView.setText(label);
            displayTextView.append(Contract.GuestEntry._ID + " - " +
                    Contract.GuestEntry.COLUMN_FIRST_NAME + " - " +
                    Contract.GuestEntry.COLUMN_LAST_NAME + " - " +
                    Contract.GuestEntry.COLUMN_EMAIL + " - " +
                    Contract.GuestEntry.COLUMN_ADDRESS + "\n");

            // Індекс кожної колонки
            int idColumnIndex = cursor.getColumnIndex(Contract.GuestEntry._ID);
            int firstNameColumnIndex = cursor.getColumnIndex(Contract.GuestEntry.COLUMN_FIRST_NAME);
            int lastNameColumnIndex = cursor.getColumnIndex(Contract.GuestEntry.COLUMN_LAST_NAME);
            int emailColumnIndex = cursor.getColumnIndex(Contract.GuestEntry.COLUMN_EMAIL);
            int addressColumnIndex = cursor.getColumnIndex(Contract.GuestEntry.COLUMN_ADDRESS);

            // Проходимо через всі рядки
            while (cursor.moveToNext()) {
                // Використовуєм индекс для отримання значення
                int currentID = cursor.getInt(idColumnIndex);
                String currentFirstName = cursor.getString(firstNameColumnIndex);
                String currentLastName = cursor.getString(lastNameColumnIndex);
                String currentEmail = cursor.getString(emailColumnIndex);
                String currentAddress = cursor.getString(addressColumnIndex);

                // Виводимо значення кожного стовпця
                displayTextView.append(("\n" + currentID + " - " +
                        currentFirstName + " - " +
                        currentLastName + " - " +
                        currentEmail + " - " +
                        currentAddress));
            }
        } finally {
            // завжди закриваємо курсор
            cursor.close();
        }
    }
    public static String convertToBase64(Bitmap bitmap) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,os);
        byte[] byteArray = os.toByteArray();
        return Base64.encodeToString(byteArray, 0);
    }
    public static Bitmap convertToBitmap(String base64String) {
        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
}