package com.vipul.hp_hp.layout_to_image;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by pang on 10/19/17.
 */

public abstract class TicketPass extends Fragment {


    protected final void error(String error_message) {
        //ErrorMessage.alert("error " + error_code + " " + t.getMessage(), getChildFragmentManager());

      /*  new MaterialDialog.Builder(getContext())
                .title(R.string.payment_selection_label_remarks)
                .content(error_message)
                .positiveText(R.string.okay_now)
                .cancelable(false)
                .show();*/


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), container, false);
        // return super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    protected abstract int getLayout();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Fabric.with(getContext(), new Crashlytics());
        //ButterKnife.bind(this, view);
        //   core_api = Appliance.getGDCore().getTicketingApi();
        //   user_api = Appliance.getGDCore().getUserSession();
        //  coupon_api = Appliance.getGDCore().getCouponService();
    }

}
