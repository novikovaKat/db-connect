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
    private String convertedImage;
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
        Bitmap bitmap = null;

        switch(requestCode) {
            case GALLERY_REQUEST:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imageView.setImageBitmap(bitmap);
                    if(bitmap!=null){
                        convertedImage = MainActivity.convertToBase64(bitmap);
                    }
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

        if(checkValues(firstName, lastName, email, address)){
            DBHelper dbHelper = new DBHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            // Створюємо об'єкт ContentValues, де імена стовпців - ключі,
            // а інформация про акаунт - значення
            ContentValues values = new ContentValues();
            values.put(Contract.GuestEntry.COLUMN_FIRST_NAME, firstName);
            values.put(Contract.GuestEntry.COLUMN_LAST_NAME, lastName);
            values.put(Contract.GuestEntry.COLUMN_EMAIL, email);
            values.put(Contract.GuestEntry.COLUMN_ADDRESS, address);
            values.put(Contract.GuestEntry.COLUMN_IMAGE, convertedImage);

            long newRowId = db.insert(Contract.GuestEntry.TABLE_NAME, null, values);
            // Виводимо повідомлення про результат
            if (newRowId == -1) {
                // Якщо ID  -1, значить виникла помилка
                Toast.makeText(this, "Error adding account", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Account added successfully", Toast.LENGTH_SHORT).show();
            }
            db.close();
            finish();
        }
    }
    public boolean checkValues(String first_name, String last_name, String email, String address){
        if (first_name.isBlank()){
            Toast.makeText(this, "Error: empty name value", Toast.LENGTH_SHORT).show();
            return false;
        }
        for (Character c: first_name.toCharArray()) {
            if(Character.isDigit(c)){
                Toast.makeText(this, "Error: name contains digits", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (last_name.isBlank()){
            Toast.makeText(this, "Error: empty last name value", Toast.LENGTH_SHORT).show();
            return false;
        }
        for (Character c: last_name.toCharArray()) {
            if(Character.isDigit(c)){
                Toast.makeText(this, "Error: last name contains digits", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (email.isBlank()){
            Toast.makeText(this, "Error: empty email value", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(address.isBlank()){
            Toast.makeText(this, "Error: empty address value", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
