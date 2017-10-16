package com.vipul.hp_hp.library;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.View;

/**
 * Created by HP-HP on 3/3/2015.
 */
public class Layout_to_Image {

    private View raw_android_view_target;
    private Context _context;
    private taskDone mtasklistener;
    public static String FORMAT_FILE_IMAGE = "yyyyMMddhhmm'_tck.jpg'";
    public static String folder_name = "folder_images";

    public interface taskDone {
        void complete();

        void fail(String message);
    }

    public static Layout_to_Image capture(Context context, View target, taskDone task) {
        Layout_to_Image ly = new Layout_to_Image(context, target);
        ly.setListener(task);
        ly.save_images();
        return ly;
    }

    private Layout_to_Image(Context context, View view) {
        this._context = context;
        this.raw_android_view_target = view;
    }

    private void setListener(taskDone tt) {
        mtasklistener = tt;
    }

    private Handler nh = new Handler();
    public Bitmap convert_layout() {
        raw_android_view_target.setDrawingCacheEnabled(true);
        raw_android_view_target.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        raw_android_view_target.layout(0, 0, raw_android_view_target.getMeasuredWidth(), raw_android_view_target.getMeasuredHeight());
        raw_android_view_target.buildDrawingCache(true);
        output_bitmap = Bitmap.createBitmap(raw_android_view_target.getDrawingCache());
        return output_bitmap;
    }

    private void save_images() {
        getImageTask vb = new getImageTask();
        vb.execute();
    }


    private Bitmap output_bitmap = null;

    private class getImageTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            int tried = 0;
            while (tried < 5) {
                try {
                    convert_layout();
                    CapturePhotoUtils.insertImage(_context, output_bitmap, "tons", null);
                    return 1;
                } catch (Exception e) {
                    tried++;
                }
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if (result == 1) {
                nh.post(new Runnable() {
                    @Override
                    public void run() {
                        mtasklistener.complete();
                    }
                });
            }
        }
    }


}
