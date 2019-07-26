package com.nizardan.note;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class edit extends AppCompatActivity {

    EditText editFileName;
    EditText editContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        editFileName=findViewById(R.id.editFileName);
        editContent=findViewById(R.id.editContent);

        //mengubah title toolbar
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            String fileName = extras.getString("filename");
            editFileName.setText(fileName);
            getSupportActionBar().setTitle("Ubah Catatan");
        } else {
            getSupportActionBar().setTitle("Tambah Catatan");
        }
        bacaFile();


    }


    void bacaFile() {
        File path=getDir("NOTES",MODE_PRIVATE);
        File file = new File(path, editFileName
                .getText().toString());
        if (file.exists()) {
            StringBuilder text = new StringBuilder();
            try {
                BufferedReader br =
                        new BufferedReader(
                                new FileReader(file));
                String line = br.readLine();
                while (line != null) {
                    text.append(line);
                    line = br.readLine();
                }
                br.close();
            } catch (IOException e) {
                System.out.println("Error " + e.getMessage());
            }
            editContent.setText(text.toString());
        }
    }

    public void saveData(View view) {
        buatDanUbah();
    }
    void buatDanUbah(){
        File path=getDir("NOTES",MODE_PRIVATE);
        File file = new File(path.toString(),
                editFileName.getText().toString());
        FileOutputStream outputStream = null;
        try {
            file.createNewFile();
            outputStream = new FileOutputStream(file,
                    false);
            outputStream.write(editContent.getText()
                    .toString().getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        onBackPressed();
    }




}
