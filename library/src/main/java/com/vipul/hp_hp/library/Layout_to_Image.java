package com.vipul.hp_hp.library;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by HP-HP on 3/3/2015.
 */
public class Layout_to_Image {

    private View _view;
    private Context _context;
    private Bitmap bMap;
    public static String FORMAT_FILE_IMAGE = "yyyyMMddhhmm_tck.jpg";
    public static String folder_name = "folder_images";

    public static Layout_to_Image capture(Context context, View target) {
        return new Layout_to_Image(context, target);
    }

    private Layout_to_Image(Context context, View view) {
        this._context = context;
        this._view = view;
    }

    public void save_external_image_file(@Nullable String file_date_format) {
        if (bMap == null) return;
        if (file_date_format == null) file_date_format = FORMAT_FILE_IMAGE;
        //Save bitmap
        String extr = Environment.getExternalStorageDirectory().toString() + File.separator + folder_name;

        String fileName = new SimpleDateFormat(file_date_format).format(new Date());
        File myPath = new File(extr, fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            bMap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            MediaStore.Images.Media.insertImage(_context.getContentResolver(), bMap, "Screen", "screen");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }


    public Bitmap convert_layout() {
        _view.setDrawingCacheEnabled(true);
        _view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        _view.layout(0, 0, _view.getMeasuredWidth(), _view.getMeasuredHeight());
        _view.buildDrawingCache(true);
        bMap = Bitmap.createBitmap(_view.getDrawingCache());
        return bMap;
    }
}
