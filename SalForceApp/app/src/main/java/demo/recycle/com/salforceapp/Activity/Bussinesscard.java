package demo.recycle.com.salforceapp.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.googlecode.tesseract.android.TessBaseAPI;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import demo.recycle.com.salforceapp.R;

public class Bussinesscard extends AppCompatActivity {

    private static final int CAMERA = 1;
    TextView businessCardtext;
    private static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/TesseractSample/";
    SurfaceView  cameraView;
    CameraSource mCameraSource;
    Button scan,back;

    Set<String> hash_SetEMAIL = new HashSet<String>();
    Set<String> hash_SetPhone=new HashSet<String>();
    Set<String> hash_SetAdd=new HashSet<String>();

    Set  addressnumber=new HashSet();
    Set  phonenumber=new HashSet();
    Set  emailnumber=new HashSet();

    String[]  addressnumberintostring=new String[100];
    String[]  phonenumberintostring=new String[100];
    String[]  emailnumbertostyring=new String[1000];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bussinesscard);
        businessCardtext=(TextView)findViewById(R.id.textView);
        cameraView = (SurfaceView) findViewById(R.id.surface_view);
        scan=(Button)findViewById(R.id.scan);
        back=(Button)findViewById(R.id.back);

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Log.d("TAG", "onClick: "+hash_SetEMAIL.toString());
                Log.d("TAG", "onClick: "+hash_SetPhone.toString());

              HashSet<String> hashSet= new HashSet<String>();

              for(int i=0;i<hash_SetAdd.toString().split(" ").length;i++) {
                  hashSet.add(hash_SetAdd.toString().split(" ")[i]);

              }
                Log.d("TAG", "onClick1: "+hash_SetAdd.toString());

/*
                Log.d("mayank", "onClick:1"+phonenumberintostring.toString());
                Log.d("mayank", "onClick:2"+addressnumberintostring.toString());
                Log.d("mayank", "onClick:3"+emailnumbertostyring.toString());
           String[] array = new HashSet<String>(Arrays.asList(emailnumbertostyring)).toArray(new String[0]);




                for(int i=0;i<array.length;i++)
           {
               Log.d("mayank", "on"+array[i]);



           }



                String duplicatePattern = "\\b([\\w\\s']+) \\1\\b";
                Pattern p = Pattern.compile(duplicatePattern);
                for (String phrase : array) {
                    Matcher m = p.matcher(phrase);
                    if (m.matches()) {
                        System.out.println(m.group(1));
                        Log.d("TAG", "onClick: "+m.group(1));
                    } else {
                        System.out.println(phrase);
                    }
                }




                Log.d("mayank", "onClick:1"+addressnumber);
                Log.d("mayank", "onClick:2"+phonenumber);
                Log.d("mayank", "onClick:3"+emailnumber);

                StringBuilder stringBuilder=new StringBuilder();




                for (   Object s : emailnumber) {

                    stringBuilder.append(" "+s.toString());
                }


            Log.d("mayank",deDup(stringBuilder.toString()));


           /*     for(int i=0;i<emailnumber.size();i++)
                {

                    emailnumber.

                }

              */




            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

               startCameraSource();




    }

    public String deDup(String s) {
        return new LinkedHashSet<String>(Arrays.asList(s.split("-"))).toString().replaceAll("(^\\[|\\]$)", "").replace(", ", "-");
    }

    private void startCameraSource() {

        //Create the TextRecognizer
        final TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();

        if (!textRecognizer.isOperational()) {
            Log.w("TAG", "Detector dependencies not loaded yet");
        } else {

            //Initialize camerasource to use high resolution and set Autofocus on.
            mCameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setAutoFocusEnabled(true)
                    .setRequestedFps(2.0f)
                    .build();

            /**
             * Add call back to SurfaceView and check if camera permission is granted.
             * If permission is granted we can start our cameraSource and pass it to surfaceView
             */
            cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    try {

                        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {


                            return;
                        }
                        mCameraSource.start(cameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                }

                /**
                 * Release resources for cameraSource
                 */
                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    mCameraSource.stop();
                }
            });

            //Set the TextRecognizer's Processor.
            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {

                String Email;
                String Phone;

                @Override
                public void release() {
                }

                /**
                 * Detect all the text from camera using TextBlock and the values into a stringBuilder
                 * which will then be set to the textView.
                 * */
                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {



                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                    if (items.size() != 0 ){



                        businessCardtext.post(new Runnable() {

                            @Override
                            public void run() {
                                StringBuilder namestring = new StringBuilder();
                                StringBuilder addressstring=new StringBuilder();
                                StringBuilder phonenum=new StringBuilder();
                                StringBuilder emailstring=new StringBuilder();
                                StringBuilder stringBuilder=new StringBuilder();



                                for(int i=0;i<items.size();i++){

                                    TextBlock item = items.valueAt(i);
                                    stringBuilder.append(item.getValue());





                         /*           Pattern pattern = Pattern.compile("\\d{3}-\\d{3}-\\d{4}");
                                    Matcher matcher = pattern.matcher(item.getValue());
                                    if (matcher.find()) {

                                        phonenum.append(matcher.group(0));
                                    }


                                    Pattern pattern1 = Pattern.compile("^\\([4-6]{1}[0-9]{2}\\)\\s?[0-9]{3}-[0-9]{4}$");
                                    Matcher matcherP = pattern1.matcher(item.getValue());
                                    if (matcherP.find()) {

                                        phonenum.append(matcherP.group(0));
                                    }

                                    Pattern pattern3 = Pattern.compile("^[0-9]\\d{2,4}-\\d{6,8}$");
                                    Matcher matcher3 = pattern3.matcher(item.getValue());
                                    if (matcher3.find()) {

                                        phonenum.append(matcher3.group(0));
                                    }



                                    Pattern pattern4 = Pattern.compile("(?:\\d+\\s*)+");
                                    Matcher matcher4 = pattern4.matcher(item.getValue());
                                    if (matcher4.find()) {

                                        phonenum.append(matcher4.group(0));
                                    }

*/

                                    Pattern pattern = Pattern.compile("\\d{10}");
                                    Matcher matcher = pattern.matcher(item.getValue());
                                    if (matcher.find()) {
                                        hash_SetPhone.add(matcher.group(0));
                                        phonenum.append(matcher.group(0));
                                        phonenum.trimToSize();
                                    }


                                    Pattern pattern1 = Pattern.compile("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}");
                                    Matcher matcherP = pattern1.matcher(item.getValue());
                                    if (matcherP.find()) {

                                        hash_SetPhone.add(matcherP.group(0));

                                        phonenum.append(matcherP.group(0));
                                        phonenum.trimToSize();
                                    }

                                    Pattern pattern3 = Pattern.compile("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}");
                                    Matcher matcher3 = pattern3.matcher(item.getValue());
                                    if (matcher3.find()) {

                                        hash_SetPhone.add(matcher3.group(0));

                                        phonenum.append(matcher3.group(0));
                                        phonenum.trimToSize();
                                    }



                                    Pattern pattern4 = Pattern.compile("\\(\\d{3}\\)-\\d{3}-\\d{4}");
                                    Matcher matcher4 = pattern4.matcher(item.getValue());
                                    if (matcher4.find()) {

                                        hash_SetPhone.add(matcher4.group(0));

                                        phonenum.append(matcher4.group(0));
                                        phonenum.trimToSize();
                                    }



                                    String address = "(\\d*)\\s+((?:[\\w+\\s*-])+)[\\,]\\s+([a-zA-Z]+)\\s+([0-9a-zA-Z]+)";

                                    Pattern add = Pattern.compile(address);

                                    Matcher matcher1 = add.matcher(item.getValue());
                                    if (matcher1.find()) {

                                        hash_SetAdd.add(matcher1.group(0));

                                        addressstring.append(matcher1.group(0));
                                    }



                                    String regex = "^[a-zA-Z][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";

                                    Pattern email = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+");

                                    Matcher m = email.matcher(item.getValue());
                                    if (m.find()) {

                                        hash_SetEMAIL.add(m.group(0));

                                    }







                                    phonenumber.add(phonenum);
                                    phonenumberintostring[i]=phonenum.toString();
                                    emailnumbertostyring[i]=emailnumber.toString();
                                    addressnumberintostring[i]=addressnumber.toString();
                                    emailnumber.add(emailstring);
                                    addressnumber.add(addressstring);
                                    businessCardtext.setText(stringBuilder.toString());

                                }


                            }
                        });
                    }
                }
            });
        }
    }

    public static HashSet<StringBuilder> dups(StringBuilder[] str) {
        HashSet<StringBuilder> hs = new HashSet<StringBuilder>();
        for (int i = 0; i < str.length; i++) {

               hs.add(str[i]);

        }


        return  hs;
    }



}
