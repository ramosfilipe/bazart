package com.boleiros.bazart.profile;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.boleiros.bazart.R;

/**
 * Created by diego on 8/30/14.
 */
public class DialogPhoneOptionsProfile extends DialogFragment {
    public DialogPhoneOptionsProfile() {
        // Empty constructor required for DialogFragment
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_dialog_phone_options_profile, container);
        getDialog().setTitle("Opções");
        Bundle bundle = getArguments();
        final String phone = bundle.getString("phone");
        final RelativeLayout ligar = (RelativeLayout) view.findViewById(R.id.relativeLayoutLigarProfile);
        final RelativeLayout sms = (RelativeLayout) view.findViewById(R.id.relativeLayoutSmsProfile);

        ligar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phone));
                startActivity(intent);
            }
        });

        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("sms:" + phone));
                startActivity(sendIntent);

            }
        });

        return view;
    }

}
