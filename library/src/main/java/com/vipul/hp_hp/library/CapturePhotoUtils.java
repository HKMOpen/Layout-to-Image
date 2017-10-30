package com.vipul.hp_hp.library;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore.Images;

/**
 * HKM Invention 2015
 * Created by hesk on 8/17/2015.
 */
public class CapturePhotoUtils {

    public interface Callback {
        void complete();
    }

    /**
     * A copy of the Android internals  insertImage method, this method populates the
     * meta data with DATE_ADDED and DATE_TAKEN. This fixes a common problem where media
     * that is inserted manually gets saved at the end of the gallery (because date is not populated).
     *
     * @see Images.Media#insertImage(ContentResolver, Bitmap, String, String)
     */

    /**
     * The file will be named as default date and time
     *
     * @param contex      the context in the activity
     * @param source      the bitmap source file
     * @param description the description in string
     * @param cb          optional additional call back
     * @return the string in return
     */
    public static final String insertImage(Context contex, Bitmap source, String description, Callback cb) {
        final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy:ss:mm:hh");
        final String filename_title = "img" + df.format(new Date());
        return insertImage(contex.getContentResolver(), source, filename_title, description, cb);
    }

    /**
     * the simple date format for the context
     *
     * @param contex      the context in the activity
     * @param source      the bitmap source file
     * @param title       the title in string
     * @param description the description in string
     * @param cb          optional additional call back
     * @return the string in return
     */
    public static final String insertImage(Context contex,
                                           Bitmap source,
                                           String title,
                                           String description,
                                           Callback cb) {
        final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy ssmm");
        final String time = df.format(new Date());
        return insertImage(contex.getContentResolver(), source, title, description, cb);
    }

    /**
     * @param cr          The content resolver
     * @param source      the bitmap source file
     * @param title       the title in string
     * @param description the description in string
     * @param cb          optional additional call back
     * @return the string in return
     */
    public static final String insertImage(ContentResolver cr,
                                           Bitmap source,
                                           String title,
                                           String description,
                                           Callback cb) {

        ContentValues values = new ContentValues();
        values.put(Images.Media.TITLE, title);
        values.put(Images.Media.DISPLAY_NAME, title);
        values.put(Images.Media.DESCRIPTION, description);
        values.put(Images.Media.MIME_TYPE, "image/jpeg");
        // Add the date meta data to ensure the image is added at the front of the gallery
        values.put(Images.Media.DATE_ADDED, System.currentTimeMillis());
        values.put(Images.Media.DATE_TAKEN, System.currentTimeMillis());

        Uri url = null;
        String stringUrl = null;    /* value to be returned */

        try {
            url = cr.insert(Images.Media.EXTERNAL_CONTENT_URI, values);

            if (source != null) {
                OutputStream imageOut = cr.openOutputStream(url);
                try {
                    source.compress(Bitmap.CompressFormat.JPEG, 50, imageOut);
                } finally {
                    imageOut.close();
                }

                long id = ContentUris.parseId(url);
                // Wait until MINI_KIND thumbnail is generated.
                Bitmap miniThumb = Images.Thumbnails.getThumbnail(cr, id, Images.Thumbnails.MINI_KIND, null);
                // This is for backward compatibility.
                storeThumbnail(cr, miniThumb, id, 50F, 50F, Images.Thumbnails.MICRO_KIND);
            } else {
                cr.delete(url, null, null);
                url = null;
            }
        } catch (Exception e) {
            if (url != null) {
                cr.delete(url, null, null);
                url = null;
            }
        }

        if (url != null) {
            stringUrl = url.toString();
        }
        if (cb != null) {
            cb.complete();
        }
        return stringUrl;
    }

    /**
     * A copy of the Android internals StoreThumbnail method, it used with the insertImage to
     * populate the android.provider.MediaStore.Images.Media#insertImage with all the correct
     * meta data. The StoreThumbnail method is private so it must be duplicated here.
     *
     * @param cr     content resolver
     * @param source sour
     * @param id     the ID or the UUID for the saved file
     * @param width  expected saved image in width
     * @param height expected saved image in height
     * @param kind   the file format in saved file
     * @return the bitmap data
     * @see Images.Media (StoreThumbnail private method)
     */
    private static final Bitmap storeThumbnail(
            ContentResolver cr,
            Bitmap source,
            long id,
            float width,
            float height,
            int kind) {

        // create the matrix to scale it
        Matrix matrix = new Matrix();

        float scaleX = width / source.getWidth();
        float scaleY = height / source.getHeight();

        matrix.setScale(scaleX, scaleY);

        Bitmap thumb = Bitmap.createBitmap(source, 0, 0,
                source.getWidth(),
                source.getHeight(), matrix,
                true
        );

        ContentValues values = new ContentValues(4);
        values.put(Images.Thumbnails.KIND, kind);
        values.put(Images.Thumbnails.IMAGE_ID, (int) id);
        values.put(Images.Thumbnails.HEIGHT, thumb.getHeight());
        values.put(Images.Thumbnails.WIDTH, thumb.getWidth());

        Uri url = cr.insert(Images.Thumbnails.EXTERNAL_CONTENT_URI, values);

        try {
            OutputStream thumbOut = cr.openOutputStream(url);
            thumb.compress(Bitmap.CompressFormat.JPEG, 100, thumbOut);
            thumbOut.close();
            return thumb;
        } catch (FileNotFoundException ex) {
            return null;
        } catch (IOException ex) {
            return null;
        }
    }
}
