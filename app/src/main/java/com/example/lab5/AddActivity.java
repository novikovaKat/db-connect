package com.example.lab5;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class AddActivity extends Activity {
    public EditText editFirstName;
    public EditText editLastName;
    public EditText editEmail;
    public EditText editAddress;
    public Button btnConfirm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity);

        editFirstName = findViewById(R.id.editFirstName);
        editLastName = findViewById(R.id.editLastName);
        editEmail = findViewById(R.id.editEmail);
        editAddress = findViewById(R.id.editAddress);
        btnConfirm = findViewById(R.id.btnConfirmAdd);

        btnConfirm.setOnClickListener(view -> confirmAdd());
    }

    private void confirmAdd() {
        String firstName = String.valueOf(editFirstName.getText());
        String lastName = String.valueOf(editLastName.getText());
        String email = String.valueOf(editEmail.getText());
        String address = String.valueOf(editAddress.getText());

        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // Створюємо об'єкт ContentValues, де імена стовпців - ключі,
        // а інформация про акаунт - значення
        ContentValues values = new ContentValues();
        values.put(Contract.GuestEntry.COLUMN_FIRST_NAME, firstName);
        values.put(Contract.GuestEntry.COLUMN_LAST_NAME, lastName);
        values.put(Contract.GuestEntry.COLUMN_EMAIL, email);
        values.put(Contract.GuestEntry.COLUMN_ADDRESS, address);

        long newRowId = db.insert(Contract.GuestEntry.TABLE_NAME, null, values);
        // Виводимо повідомлення про результат
        if (newRowId == -1) {
            // Якщо ID  -1, значить виникла помилка
            Toast.makeText(this, "Помилка при додаванні акаунту", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Акаунт додано успішно", Toast.LENGTH_SHORT).show();
        }
        db.close();
        finish();
    }
}
