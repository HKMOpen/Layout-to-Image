package com.vipul.hp_hp.layout_to_image;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vipul.hp_hp.library.Layout_to_Image;

import java.util.Random;

/**
 * Created by pang on 10/19/17.
 */

public class TicScan extends TicketPass {
    private Layout_to_Image layout_to_image;
    private LinearLayout linear_layout_load;
    private Bitmap bitmap;
    private ImageView imageView;
    private Button save_img_label;
    private TextView test_num;

    @Override
    protected int getLayout() {
        return R.layout.pass;
    }

    public static int genRandom() {
        Random r = new Random();
        int myValue = r.nextInt(30000) + 10000;
        return myValue;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        linear_layout_load = (LinearLayout) view.findViewById(R.id.ticketsdeteailtrans);
        save_img_label = (Button) view.findViewById(R.id.othershow);
        test_num = (TextView) view.findViewById(R.id.qr_code_pass_secret);


        layout_to_image = new Layout_to_Image(getActivity(), linear_layout_load);
        layout_to_image.setListener(new Layout_to_Image.taskDone() {
            @Override
            public void complete(Bitmap bitmap, String path) {
                Toast.makeText(getActivity(), path + " successfully display.", Toast.LENGTH_LONG);
            }

            @Override
            public void pre() {

            }

            @Override
            public void fail(String message) {

            }
        });
        save_img_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_to_image.scan();
                test_num.setText(String.valueOf(genRandom()));
            }
        });
    }
}
