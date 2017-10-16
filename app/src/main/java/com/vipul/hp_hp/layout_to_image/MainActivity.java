package com.vipul.hp_hp.layout_to_image;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.vipul.hp_hp.library.Layout_to_Image;


public class MainActivity extends AppCompatActivity {
    public static final int PERMISSIONS_REQUEST_READ_WRITE = 302;
    private Layout_to_Image layout_to_image;
    private LinearLayout relativeLayout;
    private Bitmap bitmap;
    private ImageView imageView;
    private Button save_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        relativeLayout = (LinearLayout) findViewById(R.id.container);
        imageView = (ImageView) findViewById(R.id.imageView2);
        save_img = (Button) findViewById(R.id.save_img);


        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSIONS_REQUEST_READ_WRITE);

                // PERMISSIONS_REQUEST_READ_WRITE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        save_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_to_image = Layout_to_Image.capture(MainActivity.this, relativeLayout, new Layout_to_Image.taskDone() {
                    @Override
                    public void complete() {
                        bitmap = layout_to_image.convert_layout();
                        imageView.setImageBitmap(bitmap);
                    }

                    @Override
                    public void fail(String message) {

                    }
                });
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}