package com.example.lab5;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
    final String LOG_TAG = "myLogs";
    DBHelper dbHelper;

    public Button btnAdd;
    public Button btnRead;
    public Button btnClear;
    public Button btnUpdate;
    public Button btnDelete;
    public EditText editID;
    public EditText editFirstName;
    public EditText editLastName;
    public EditText editEmail;
    public EditText editAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        btnRead = findViewById(R.id.btnRead);
        btnClear = findViewById(R.id.btnClear);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        editID = findViewById(R.id.editID);
        editFirstName = findViewById(R.id.editFirstName);
        editLastName = findViewById(R.id.editLastName);
        editEmail = findViewById(R.id.editEmail);
        editAddress = findViewById(R.id.editAddress);


        btnAdd.setOnClickListener(view -> insertAccount());
        btnRead.setOnClickListener(view -> displayInfo());
        btnClear.setOnClickListener(view -> clearInfo());
        btnUpdate.setOnClickListener(view -> updateAccount());
        btnDelete.setOnClickListener(view -> deleteAccount());

        dbHelper = new DBHelper(this);
    }

    private void deleteAccount() {
        String id_s = editID.getText().toString();

        if(!id_s.isEmpty()){
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete(Contract.GuestEntry.TABLE_NAME,"_id = ?",  new String[] {id_s});
            db.close();
        }
        else{
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Error: ID is empty");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
    }

    private void updateAccount() {
        String id_s = editID.getText().toString();

        if(!id_s.isEmpty()){
            String firstName = String.valueOf(editFirstName.getText());
            String lastName = String.valueOf(editLastName.getText());
            String email = String.valueOf(editEmail.getText());
            String address = String.valueOf(editAddress.getText());

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            // Створюємо об'єкт ContentValues, де імена стовпців - ключі,
            // а інформация про акаунт - значення
            ContentValues values = new ContentValues();
            if (!firstName.isEmpty()){
                values.put(Contract.GuestEntry.COLUMN_FIRST_NAME, firstName);
            }
            if(!lastName.isEmpty()){
                values.put(Contract.GuestEntry.COLUMN_LAST_NAME, lastName);
            }
            if(!email.isEmpty()){
                values.put(Contract.GuestEntry.COLUMN_EMAIL, email);
            }
            if(!address.isEmpty()){
                values.put(Contract.GuestEntry.COLUMN_ADDRESS, address);
            }

            long newRowId = db.update(Contract.GuestEntry.TABLE_NAME, values, "_id = ?",  new String[] {id_s});
            db.close();
        }
        else{
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Error: ID is empty");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
    }

    private void clearInfo() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(Contract.GuestEntry.TABLE_NAME, null, null);
        db.close();
    }

    private void insertAccount() {
        String firstName = String.valueOf(editFirstName.getText());
        String lastName = String.valueOf(editLastName.getText());
        String email = String.valueOf(editEmail.getText());
        String address = String.valueOf(editAddress.getText());

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // Створюємо об'єкт ContentValues, де імена стовпців - ключі,
        // а інформация про акаунт - значення
        ContentValues values = new ContentValues();
        values.put(Contract.GuestEntry.COLUMN_FIRST_NAME, firstName);
        values.put(Contract.GuestEntry.COLUMN_LAST_NAME, lastName);
        values.put(Contract.GuestEntry.COLUMN_EMAIL, email);
        values.put(Contract.GuestEntry.COLUMN_ADDRESS, address);

        long newRowId = db.insert(Contract.GuestEntry.TABLE_NAME, null, values);
        db.close();
    }

    public void displayInfo(){
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


        EditText displayTextView = (EditText) findViewById(R.id.dbInfo);

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
            db.close();
        }
    }

}