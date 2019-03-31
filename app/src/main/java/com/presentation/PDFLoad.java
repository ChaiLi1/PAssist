package com.presentation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

//import com.github.pdfviewer.PDFView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class PDFLoad extends AppCompatActivity implements View.OnClickListener {

    private static final String FILENAME = "aries.pdf";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.Full);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ppt_main);
        findViewById(R.id.asset).setOnClickListener(this);
        findViewById(R.id.storage).setOnClickListener(this);
        findViewById(R.id.buttonz).setOnClickListener(this);

    }

    private void Pickpdfstorage() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(intent, 10);
    }

    private void OpenfileFromAsset() {
        System.out.println("HAHAHAHA2");

        File file = new File(getCacheDir(), FILENAME);
        if (!file.exists()) {

            try {
                InputStream asset = getAssets().open(FILENAME);
                FileOutputStream output = null;
                output = new FileOutputStream(file);
                final byte[] buffer = new byte[1024];
                int size;
                while ((size = asset.read(buffer)) != -1) {
                    output.write(buffer, 0, size);
                }
                asset.close();
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        setTheme(R.style.Full);
        OpenPdfActivity(file.getAbsolutePath());
    }

    private void OpenPdfActivity(String absolutePath) {

        PDFView.with(PDFLoad.this)
                .fromfilepath(absolutePath)
                .swipeHorizontal(false)
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case 10:
                // Get the Uri of the selected file
                if (resultCode == RESULT_OK) {

                    if (null != data.getData()) {

                        Uri uri = data.getData();
                        File file;

                        if (uri.getScheme().equals("content")) {

                            file = new File(getCacheDir(), data.getData().getLastPathSegment());

                            try {
                                InputStream iStream = getContentResolver().openInputStream(uri);
                                FileOutputStream output = null;
                                output = new FileOutputStream(file);
                                final byte[] buffer = new byte[1024];
                                int size;
                                while ((size = iStream.read(buffer)) != -1) {
                                    output.write(buffer, 0, size);
                                }
                                iStream.close();
                                output.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else
                            file = new File(uri.getPath());


                        OpenPdfActivity(file.getAbsolutePath());
                    }
                }
                break;
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.asset:
                OpenfileFromAsset();
                break;
            case R.id.storage:
                Pickpdfstorage();
                break;
            case R.id.buttonz:
                Intent intent = new Intent(PDFLoad.this, Communicate.class);
                startActivity(intent);
                break;
                /*
                case R.id.download:
                initDownload();
                break;*/
        }
    }

    /*
    private void initDownload() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.antennahouse.com/")
                .build();

        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

        Call<ResponseBody> request = retrofitInterface.downloadFile();
        request.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    ResponseBody body = response.body();

                    InputStream iStream = new BufferedInputStream(body.byteStream(), 1024 * 8);

                    //creating file name dynamically
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.US);
                    Date now = new Date();
                    String fileName = formatter.format(now) + ".pdf";

                    File file = new File(getCacheDir(), fileName);
                    FileOutputStream output = null;
                    output = new FileOutputStream(file);
                    final byte[] buffer = new byte[1024];
                    int size;
                    while ((size = iStream.read(buffer)) != -1) {
                        output.write(buffer, 0, size);
                    }
                    iStream.close();
                    output.close();

                    OpenPdfActivity(file.getAbsolutePath());


                } catch (IOException e) {

                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("testing");

            }
        });

    }
    */
}
