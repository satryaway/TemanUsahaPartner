package com.samstudio.temanusahapartner.util;

import android.content.Context;

import com.samstudio.temanusahapartner.R;
import com.samstudio.temanusahapartner.TemanUsahaApplication;

/**
 * Created by satryaway on 10/17/2015.
 * text converter
 */
public class TextConverter {

    public static String convertStatsCodeToString(String code) {
        Context context = TemanUsahaApplication.getInstance();
        String convertedCode = "";
        switch (code) {
            case "administration process" : convertedCode = context.getString(R.string.waiting_for_process); break;
            case "meet up" : convertedCode = context.getString(R.string.meet_up_process); break;
            case "approved" : convertedCode = context.getString(R.string.waiting_for_confirmation); break;
            case "confirmed" : convertedCode = context.getString(R.string.confirmed); break;
            case "cancelled" : convertedCode = context.getString(R.string.cancelled); break;
            default: convertedCode = context.getString(R.string.rejected); break;
        }

        return convertedCode;
    }
}
