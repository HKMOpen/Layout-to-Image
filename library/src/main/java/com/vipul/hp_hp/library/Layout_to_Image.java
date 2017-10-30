package com.vipul.hp_hp.library;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewTreeObserver;


/**
 * Created by pang on 10/16/17.
 */
public class Layout_to_Image {

    private View raw_android_view_target;
    private Context _context;
    private taskDone mtasklistener;
    public static String FORMAT_FILE_IMAGE = "yyyyMMddhhmm'_tck.jpg'";
    public static String folder_name = "folder_images";
    private Bitmap output_bitmap = null;

    public interface taskDone {
        void complete(Bitmap bitmap, String path);

        void pre();

        void fail(String message);
    }

    public static Layout_to_Image capture(Context context, View target, taskDone task) {
        Layout_to_Image ly = new Layout_to_Image(context, target);
        ly.setListener(task);
        ly.scan();
        return ly;
    }

    public Layout_to_Image(Context context, View view) {
        this._context = context;
        this.raw_android_view_target = view;

        ViewTreeObserver vto = raw_android_view_target.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(obs);

    }

    public void setListener(taskDone tt) {
        mtasklistener = tt;
    }

    private int h, w;
    private String path_uri;

    private void convert_layout() {
        if (raw_android_view_target == null) {
            mtasklistener.fail("the main view is not found");
            return;
        }
        //https://stackoverflow.com/questions/3591784/getwidth-and-getheight-of-view-returns-0

        //        raw_android_view_target.requestLayout();
        // raw_android_view_target.invalidateOutline();
/*
        int widthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        int heightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);*/

        // raw_android_view_target.measure(widthSpec, heightSpec);
        //   raw_android_view_target.layout(raw_android_view_target.getMeasuredWidth(), raw_android_view_target.getMeasuredHeight());
        if (_context == null) return;
        if (h <= 0 || w <= 0) {
            //   raw_android_view_target.invalidate();
            mtasklistener.fail("view not ready");
        } else {
            output_bitmap = loadBitmapFromView(raw_android_view_target, h, w);
            path_uri = CapturePhotoUtils.insertImage(_context, output_bitmap, "ticketing", new CapturePhotoUtils.Callback() {
                @Override
                public void complete() {
                    raw_android_view_target.requestLayout();
                    mtasklistener.complete(output_bitmap, path_uri);
                }
            });
        }
    }

    public void destroy() {
        try {
            raw_android_view_target.getViewTreeObserver().removeOnGlobalLayoutListener(obs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ViewTreeObserver.OnGlobalLayoutListener obs = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            if (raw_android_view_target == null) return;
            //fully drawn, no need of listener anymore
            h = raw_android_view_target.getHeight();
            w = raw_android_view_target.getWidth();
            if (h <= 0 || w <= 0) {
                //  raw_android_view_target.invalidate();
            }else{
                raw_android_view_target.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        }

    };

    private static Bitmap loadBitmapFromView(View v, int h, int w) {
        Bitmap b = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);

        Paint paint2 = new Paint();
        paint2.setColor(Color.WHITE);
        paint2.setStyle(Paint.Style.FILL);
        c.drawPaint(paint2);

        v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height);
        v.draw(c);
        return b;
    }


    public void scan() {
        mtasklistener.pre();
        convert_layout();
    }


}
