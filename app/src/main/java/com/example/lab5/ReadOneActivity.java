package com.example.lab5;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class ReadOneActivity extends Activity {
    private EditText editId;
    private TextView firstNameView;
    private TextView lastNameView;
    private TextView emailView;
    private TextView addressView;
    private ImageView imageView;
    private Button btnRead;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_one_activity);
        editId = findViewById(R.id.editID);
        firstNameView = findViewById(R.id.editFirstName);
        lastNameView = findViewById(R.id.editLastName);
        emailView = findViewById(R.id.editEmail);
        addressView = findViewById(R.id.editAddress);
        imageView = findViewById(R.id.imageView);
        btnRead = findViewById(R.id.btnRead);

        btnRead.setOnClickListener(view -> {
            String id = (editId.getText()).toString();
            onClickRead(id);
        });
    }

    private void onClickRead(String id) {
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                Contract.GuestEntry._ID,
                Contract.GuestEntry.COLUMN_FIRST_NAME,
                Contract.GuestEntry.COLUMN_LAST_NAME,
                Contract.GuestEntry.COLUMN_EMAIL,
                Contract.GuestEntry.COLUMN_ADDRESS};

        String selection = Contract.GuestEntry._ID + "=?";
        String[] selectionArgs = {id};
        Cursor cursor = db.query(
                Contract.GuestEntry.TABLE_NAME,   // таблиця
                projection,            // стовпці
                selection,                  // стовпці для умови WHERE
                selectionArgs,                  // значення для умови WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // порядок сортування

        int firstNameColumnIndex = cursor.getColumnIndex(Contract.GuestEntry.COLUMN_FIRST_NAME);
        int lastNameColumnIndex = cursor.getColumnIndex(Contract.GuestEntry.COLUMN_LAST_NAME);
        int emailColumnIndex = cursor.getColumnIndex(Contract.GuestEntry.COLUMN_EMAIL);
        int addressColumnIndex = cursor.getColumnIndex(Contract.GuestEntry.COLUMN_ADDRESS);

        if (cursor.moveToNext()) {
            firstNameView.setText(cursor.getString(firstNameColumnIndex));
            lastNameView.setText(cursor.getString(lastNameColumnIndex));
            emailView.setText(cursor.getString(emailColumnIndex));
            addressView.setText(cursor.getString(addressColumnIndex));
        }
        cursor.close();
        db.close();
    }
}
