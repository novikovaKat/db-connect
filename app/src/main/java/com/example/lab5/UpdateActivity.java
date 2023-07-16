package com.example.lab5;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.io.IOException;

public class UpdateActivity extends Activity {
    private EditText editID;
    private EditText editFirstName;
    private EditText editLastName;
    private EditText editEmail;
    private EditText editAddress;
    private Button btnConfirm;
    private Button btnLoad;
    private ImageView imageView;
    private String convertedImage;
    static final int GALLERY_REQUEST = 1;

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
        btnLoad = findViewById(R.id.btnLoad);
        imageView = findViewById(R.id.imageView);

        btnConfirm.setOnClickListener(view -> confirmUpdate());
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

    private void confirmUpdate() {
        String id_s = String.valueOf(editID.getText());

        if(!id_s.isEmpty() && TextUtils.isDigitsOnly(id_s)){
            String firstName = String.valueOf(editFirstName.getText());
            String lastName = String.valueOf(editLastName.getText());
            String email = String.valueOf(editEmail.getText());
            String address = String.valueOf(editAddress.getText());

            if(checkName(firstName, "first name") && checkName(lastName, "last name")){
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
                if(convertedImage!=null){
                    values.put(Contract.GuestEntry.COLUMN_IMAGE, convertedImage);
                }

                if(values.size() != 0){
                    long newRowId = db.update(Contract.GuestEntry.TABLE_NAME, values, "_id = ?",  new String[] {id_s});
                    if (newRowId == -1) {
                        // Якщо ID  -1, це помилка
                        Toast.makeText(this, "Error updating account", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Account updated successfully", Toast.LENGTH_SHORT).show();
                    }
                    convertedImage = null;
                    db.close();
                    finish();
                }
                else {
                    Toast.makeText(this, "Error: empty data for updating", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else{
            Toast.makeText(this, "Error: ID is empty", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkName(String name, String field){
        for (Character c: name.toCharArray()) {
            if(Character.isDigit(c)){
                Toast.makeText(this, "Error: " + field + " contains digits", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }
}
