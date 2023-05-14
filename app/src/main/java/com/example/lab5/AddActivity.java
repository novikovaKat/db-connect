package com.example.lab5;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.IOException;

public class AddActivity extends Activity {
    public EditText editFirstName;
    public EditText editLastName;
    public EditText editEmail;
    public EditText editAddress;
    public Button btnConfirm;
    public Button btnLoad;
    public ImageView imageView;
    static final int GALLERY_REQUEST = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity);

        editFirstName = findViewById(R.id.editFirstName);
        editLastName = findViewById(R.id.editLastName);
        editEmail = findViewById(R.id.editEmail);
        editAddress = findViewById(R.id.editAddress);
        btnConfirm = findViewById(R.id.btnConfirmAdd);
        btnLoad = findViewById(R.id.btnLoad);
        imageView = findViewById(R.id.imageView);

        btnConfirm.setOnClickListener(view -> confirmAdd());
        btnLoad.setOnClickListener(view -> loadPhoto());
    }

    private void loadPhoto() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case GALLERY_REQUEST:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    // remove previous image uri cache
                    imageView.setImageURI(null);
                    // set image view image from uri
                    imageView.setImageURI(selectedImage);
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + requestCode);
        }
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
