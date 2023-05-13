package com.example.lab5;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

public class UpdateActivity extends Activity {
    public EditText editID;
    public EditText editFirstName;
    public EditText editLastName;
    public EditText editEmail;
    public EditText editAddress;
    public Button btnConfirm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_activity);
        
        editID = findViewById(R.id.editID);
        editFirstName = findViewById(R.id.editFirstName);
        editLastName = findViewById(R.id.editLastName);
        editEmail = findViewById(R.id.editEmail);
        editAddress = findViewById(R.id.editAddress);
        btnConfirm = findViewById(R.id.btnConfirmUpdate);

        btnConfirm.setOnClickListener(view -> confirmUpdate());
    }

    private void confirmUpdate() {
        String id_s = editID.getText().toString();

        if(!id_s.isEmpty()){
            String firstName = String.valueOf(editFirstName.getText());
            String lastName = String.valueOf(editLastName.getText());
            String email = String.valueOf(editEmail.getText());
            String address = String.valueOf(editAddress.getText());

            DBHelper dbHelper = new DBHelper(this);
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
            if (newRowId == -1) {
                // Если ID  -1, значит произошла ошибка
                Toast.makeText(this, "Помилка при оновленні акаунту", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Акаунт оновлено успішно", Toast.LENGTH_SHORT).show();
            }
            db.close();
            finish();
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
}
