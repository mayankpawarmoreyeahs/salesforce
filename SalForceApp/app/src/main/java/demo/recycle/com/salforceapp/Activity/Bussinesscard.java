package demo.recycle.com.salforceapp.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.widget.TextView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import demo.recycle.com.salforceapp.R;

public class Bussinesscard extends AppCompatActivity {

    private static final int CAMERA = 1;
    TextView BusinessCard;

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


            TextRecognizer text = new TextRecognizer.Builder(getApplicationContext()).build();
            if (!text.isOperational()) {

            } else {
                Frame frame = new Frame.Builder().setBitmap(bitmap).build();
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


















}
