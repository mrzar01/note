package com.nizardan.note;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Inflater;


public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_STORAGE = 100;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view, int i, long l) {
                Intent intent = new Intent(
                        MainActivity.this,
                        edit.class);
                Map<String, Object> data = (Map<String, Object>)
                        adapterView.getAdapter().getItem(i);
                intent.putExtra("filename", data.get("name")
                        .toString());
                Toast.makeText(MainActivity.this,
                        "You clicked " + data.get("name"),
                        Toast.LENGTH_SHORT).show();

                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView,
                                                   View view, int i, long l) {
                        Map<String, Object> data = (Map<String, Object>)
                                adapterView.getAdapter().getItem(i);
                        tampilkanDialogKonfirmasiHapusCatatan
                                (data.get("name")
                                        .toString());
                        return true;
                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.optionmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemAdd:
                Intent intent =
                        new Intent(this,
                                edit.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    void tampilkanDialogKonfirmasiHapusCatatan(
            final String filename) {
        new AlertDialog.Builder(this)
                .setTitle("Hapus Catatan ini?")
                .setMessage("Apakah Anda yakin ingin menghapus Catatan "
                        +filename+"?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Hapus",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                hapusFile(filename);
                            }
                        })
                .setNegativeButton("Jangan", null)
                .show();
    }

    void hapusFile(String filename) {
        File path = getDir("NOTES",MODE_PRIVATE);
        File file = new File(path.toString(), filename);
        if (file.exists()) {
            file.delete();
        }
        mengambilListFilePadaFolder();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mengambilListFilePadaFolder();
    }

    void mengambilListFilePadaFolder() {
        File path = getDir("NOTES",MODE_PRIVATE);
        File directory = new File(path.toString());

        if (directory.exists()) {
            File[] files = directory.listFiles();
            String[] filenames = new String[files.length];
            String[] dateCreated = new String[files.length];

            SimpleDateFormat simpleDateFormat =
                    new SimpleDateFormat
                            ("dd MMM yyyy HH:mm:ss");
            ArrayList<Map<String, Object>> itemDataList =
                    new ArrayList<Map<String, Object>>();
            for (int i = 0; i < files.length; i++) {
                filenames[i] = files[i].getName();
                Date lastModDate = new Date(files[i].lastModified());
                dateCreated[i] = simpleDateFormat.format(lastModDate);
                Map<String, Object> listItemMap = new HashMap<>();
                listItemMap.put("name", filenames[i]);
                listItemMap.put("date", dateCreated[i]);
                itemDataList.add(listItemMap);
            }
            SimpleAdapter simpleAdapter =
                    new SimpleAdapter(this, itemDataList,
                            android.R.layout.simple_list_item_2,
                            new String[]{"name", "date"},
                            new int[]{android.R.id.text1,
                                    android.R.id.text2});
            listView.setAdapter(simpleAdapter);
            simpleAdapter.notifyDataSetChanged();
        }
    }




}


