package com.presentation;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.media.VolumeShaper;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.BroadcastReceiver;
import android.support.v4.content.LocalBroadcastManager;
import android.content.IntentFilter;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class PDFViewActivity extends AppCompatActivity implements IShowPage, View.OnClickListener {

    private PDFViewPager pdfviewpager;
    private PDFAdapter adapter;
    private static final String STATE_CURRENT_PAGE_INDEX = "current_page_index";
    private ParcelFileDescriptor mFileDescriptor;
    private PdfRenderer mPdfRenderer;
    private PdfRenderer.Page mCurrentPage;
    private int mPageIndex;
    private PDFConfig config;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }

        config = intent.getParcelableExtra(PDFConfig.EXTRA_CONFIG);

        setContentView(R.layout.activity_pdf);
        // button key control
        /*
        findViewById(R.id.button).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);*/
        pdfviewpager = findViewById(R.id.pdfviewfpager);
        pdfviewpager.setSwipeOrientation(config.getSwipeorientation());


        //////////////////////////////
        // Handle message
        IntentFilter messageFilter = new IntentFilter(Intent.ACTION_SEND);
        Receiver messageReceiver = new Receiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, messageFilter);
        //////////////////////////////

        mPageIndex = 0;
        // If there is a savedInstanceState (screen orientations, etc.), we restore the page index.
        if (null != savedInstanceState) {
            mPageIndex = savedInstanceState.getInt(STATE_CURRENT_PAGE_INDEX, 0);
        }


        //render the pdf view
        try {
            openRenderer(this);
            setUpViewPager();
        } catch (FileNotFoundException e) {
            Toast.makeText(this, "Error! " + e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error! " + e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private void setUpViewPager() {
        adapter = new PDFAdapter(PDFViewActivity.this, this, mPdfRenderer.getPageCount());
        pdfviewpager.setAdapter(adapter);
        pdfviewpager.setCurrentItem(mPageIndex);
    }

    @Override
    protected void onDestroy() {
        try {
            closeRenderer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (null != mCurrentPage) {
            outState.putInt(STATE_CURRENT_PAGE_INDEX, mCurrentPage.getIndex());
        }
    }


    private void openRenderer(Context context) throws IOException {
        File file = new File(config.getFilepath());
        mFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
        if (mFileDescriptor != null) {
            mPdfRenderer = new PdfRenderer(mFileDescriptor);
        }

    }


    private void closeRenderer() throws IOException {
        if (null != mCurrentPage)
            mCurrentPage.close();

        if (null != mPdfRenderer)
            mPdfRenderer.close();

        if (null != mFileDescriptor)
            mFileDescriptor.close();
    }

    public Bitmap showPage(int index) {
        if (mPdfRenderer.getPageCount() <= index) {
            return null;
        }
        // Make sure to close the current page before opening another one.
        if (null != mCurrentPage) {
            mCurrentPage.close();
        }
        // Use `openPage` to open a specific page in PDF.
        mCurrentPage = mPdfRenderer.openPage(index);
        // Important: the destination bitmap must be ARGB (not RGB).
        Bitmap bitmap = Bitmap.createBitmap(mCurrentPage.getWidth(), mCurrentPage.getHeight(),
                Bitmap.Config.ARGB_8888);
        mCurrentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
        return bitmap;
    }

    public void prevPage(){
        int x = pdfviewpager.getCurrentItem();
        int y = x-1;
        swapPage(y);
    }

    public void nextPage(){
        int x = pdfviewpager.getCurrentItem();
        int y = x+1;
        swapPage(y);
    }

    public void setLandscape()  {
        int i = getResources().getConfiguration().orientation;
        if (i== Configuration.ORIENTATION_PORTRAIT){
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);}
        else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }



    public void onClick(View view) {
        int i = view.getId();
        System.out.println("Button clicked:" +i);
        int x = pdfviewpager.getCurrentItem();
        System.out.println("Current page index:"+x);
        //int x = mCurrentPage.getIndex()-1;

        if (i == R.id.button) {
            int y = x+1;
            System.out.println(y);
            swapPage(y);
        }
        if (i == R.id.button2){
            int y = x-1;
            System.out.println(y);
            swapPage(y);
        }
    }

    public void swapPage(int x) {
        pdfviewpager.setCurrentItem(x);
        adapter.notifyDataSetChanged();
    }

    public class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = "I just received a message from the wearable";
            System.out.println("BREAK1");
            //Bundle bd = intent.getExtras();
            String gg = intent.getStringExtra("YOLO");
            if (gg.equals("prev")){
                prevPage();
            } else if (gg.equals("next")){
                nextPage();
            } else if (gg.equals("oren")){
                System.out.println("OOOOORRRRAA");
                setLandscape();
            }
        }
    }


}
