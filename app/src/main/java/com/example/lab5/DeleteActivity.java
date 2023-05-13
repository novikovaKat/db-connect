package com.example.lab5;

import android.app.Activity;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

public class DeleteActivity extends Activity {
    public EditText editID;
    public Button btnConfirm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_activity);

        editID = findViewById(R.id.editID);
        btnConfirm = findViewById(R.id.btnConfirmUpdate);

        btnConfirm.setOnClickListener(view -> confirmDelete());
    }

    private void confirmDelete() {
        String id_s = editID.getText().toString();

        if(!id_s.isEmpty()){
            DBHelper dbHelper = new DBHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            long newRowId = db.delete(Contract.GuestEntry.TABLE_NAME,"_id = ?",  new String[] {id_s});
            if (newRowId == -1) {
                // Если ID  -1, значит произошла ошибка
                Toast.makeText(this, "Помилка при видаленні акаунту", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Акаунт видалено успішно", Toast.LENGTH_SHORT).show();
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
