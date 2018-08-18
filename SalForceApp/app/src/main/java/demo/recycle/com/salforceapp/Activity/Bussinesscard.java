package demo.recycle.com.salforceapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.widget.TextView;
import com.googlecode.tesseract.android.TessBaseAPI;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import demo.recycle.com.salforceapp.R;

public class Bussinesscard extends AppCompatActivity {

    private static final int CAMERA = 1;
    TextView BusinessCard;
    private static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/TesseractSample/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bussinesscard);
        BusinessCard=(TextView)findViewById(R.id.textView);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA);




    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CAMERA) {


            Bitmap bitmap = (Bitmap) data.getExtras().get("data");


            Uri tempUri = getImageUri(getApplicationContext(),bitmap);

            // CALL THIS METHOD TO GET THE ACTUAL PATH
            Bitmap bitmap1 = null;
            try {

                bitmap1 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), tempUri);
            } catch (IOException e) {
                e.printStackTrace();
            }


        /*    try {
                BusinessCard.setText(extractText(bitmap));
            } catch (Exception e) {
                e.printStackTrace();
            }

            */


            TextRecognizer text = new TextRecognizer.Builder(getApplicationContext()).build();
            if (!text.isOperational()) {

            } else {
                Frame frame = new Frame.Builder().setBitmap(bitmap1).build();
                SparseArray<TextBlock> items = text.detect(frame);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < items.size(); i++) {
                    TextBlock myitem = items.valueAt(i);
                    sb.append(myitem.getValue());
                    sb.append("\n");
                    BusinessCard.setText(sb);

                }


            }

        }


    }


    private String extractText(Bitmap bitmap) throws Exception
    {
        TessBaseAPI tessBaseApi = new TessBaseAPI();
        tessBaseApi.init(DATA_PATH, "eng");
        tessBaseApi.setImage(bitmap);
        String extractedText = tessBaseApi.getUTF8Text();
        tessBaseApi.end();
        return extractedText;
    }



    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }








}
