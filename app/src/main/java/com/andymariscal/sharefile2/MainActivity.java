package com.andymariscal.sharefile2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.documentfile.provider.DocumentFile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(v -> {
            /*
            * Remember:
            * - Add PDF to asset folder
            * - Create an "xml" folder under the "res" folder
            * - - In the xml folder add the "filepaths.xml" file
            * - Add the provider Tag in the manifest
            * */
            try {
                File pdfPath = new File(getCacheDir(), "tmp.pdf");
                InputStream in = getAssets().open("tmp.pdf");
                try (FileOutputStream out = new FileOutputStream(pdfPath, false)) {
                    int read;
                    byte[] bytes = new byte[in.available()];
                    while ((read = in.read(bytes)) != -1) {
                        out.write(bytes, 0, read);
                    }
                }
                Uri uri = FileProvider.getUriForFile(
                        this,
                        getApplicationContext().getPackageName() + ".provider",
                        pdfPath);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(uri, getContentResolver().getType(uri));
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(intent, ""));
            } catch (IOException e) {
                Log.e("Andres", e.getLocalizedMessage(), e);
                e.printStackTrace();
            }
        });

        findViewById(R.id.button2).setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            i.addCategory(Intent.CATEGORY_DEFAULT);
            startActivityForResult(Intent.createChooser(i, "Choose directory"), 9999);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        https://commonsware.com/blog/2019/11/09/scoped-storage-stories-trees.html
//        https://stackoverflow.com/questions/61118918/create-new-file-in-the-directory-returned-by-intent-action-open-document-tree
    }
}