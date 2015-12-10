package com.jixstreet.temanusahapartner.util;

import android.content.Context;

import com.jixstreet.temanusahapartner.R;
import com.jixstreet.temanusahapartner.entities.CreditCeiling;
import com.jixstreet.temanusahapartner.entities.CreditPurpose;
import com.jixstreet.temanusahapartner.entities.Partner;
import com.jixstreet.temanusahapartner.entities.TimeRange;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by satryaway on 10/5/2015.
 * a class to get data
 */
public class Seeder {

    public static List<CreditPurpose> getCreditPurpose(Context context) {
        List<CreditPurpose> creditPurposeList = new ArrayList<>();
        CreditPurpose creditPurpose;

        creditPurpose = new CreditPurpose(1, context.getString(R.string.kredit_modal_usaha));
        creditPurposeList.add(creditPurpose);

        creditPurpose = new CreditPurpose(2, context.getString(R.string.kredit_investasi));
        creditPurposeList.add(creditPurpose);

        creditPurpose = new CreditPurpose(3, context.getString(R.string.kredit_konsumtif));
        creditPurposeList.add(creditPurpose);

        creditPurpose = new CreditPurpose(4, context.getString(R.string.kredit_mobil));
        creditPurposeList.add(creditPurpose);

        creditPurpose = new CreditPurpose(5, context.getString(R.string.kredit_sepeda_motor));
        creditPurposeList.add(creditPurpose);

        creditPurpose = new CreditPurpose(7, context.getString(R.string.kredit_pegawai));
        creditPurposeList.add(creditPurpose);

        creditPurpose = new CreditPurpose(8, context.getString(R.string.kpr));
        creditPurposeList.add(creditPurpose);

        return creditPurposeList;
    }

    public static List<CreditCeiling> getCreditCeiling(Context context) {
        List<CreditCeiling> creditCeilingList = new ArrayList<>();
        CreditCeiling creditCeiling;

        creditCeiling = new CreditCeiling(1, context.getString(R.string.ceiling_1));
        creditCeilingList.add(creditCeiling);

        creditCeiling = new CreditCeiling(2, context.getString(R.string.ceiling_2));
        creditCeilingList.add(creditCeiling);

        creditCeiling = new CreditCeiling(3, context.getString(R.string.ceiling_3));
        creditCeilingList.add(creditCeiling);

        creditCeiling = new CreditCeiling(4, context.getString(R.string.ceiling_4));
        creditCeilingList.add(creditCeiling);

        creditCeiling = new CreditCeiling(5, context.getString(R.string.ceiling_5));
        creditCeilingList.add(creditCeiling);

        creditCeiling = new CreditCeiling(6, context.getString(R.string.ceiling_6));
        creditCeilingList.add(creditCeiling);

        creditCeiling = new CreditCeiling(7, context.getString(R.string.ceiling_7));
        creditCeilingList.add(creditCeiling);

        creditCeiling = new CreditCeiling(8, context.getString(R.string.ceiling_8));
        creditCeilingList.add(creditCeiling);

        creditCeiling = new CreditCeiling(9, context.getString(R.string.ceiling_9));
        creditCeilingList.add(creditCeiling);

        creditCeiling = new CreditCeiling(10, context.getString(R.string.ceiling_10));
        creditCeilingList.add(creditCeiling);

        return creditCeilingList;
    }

    public static List<TimeRange> getTimeRange(Context context) {
        List<TimeRange> timeRangeList = new ArrayList<>();
        TimeRange timeRange;

        timeRange = new TimeRange(1, context.getString(R.string.time_range_1));
        timeRangeList.add(timeRange);

        timeRange = new TimeRange(2, context.getString(R.string.time_range_2));
        timeRangeList.add(timeRange);

        timeRange = new TimeRange(3, context.getString(R.string.time_range_3));
        timeRangeList.add(timeRange);

        timeRange = new TimeRange(4, context.getString(R.string.time_range_4));
        timeRangeList.add(timeRange);

        timeRange = new TimeRange(5, context.getString(R.string.time_range_5));
        timeRangeList.add(timeRange);

        timeRange = new TimeRange(6, context.getString(R.string.time_range_6));
        timeRangeList.add(timeRange);

        return timeRangeList;
    }

    public static List<String> getCreditPurposeList(Context context){
        List<String> stringList = new ArrayList<>();
        stringList.add(context.getString(R.string.kredit_modal_usaha));
        stringList.add(context.getString(R.string.kredit_investasi));
        stringList.add(context.getString(R.string.kredit_konsumtif));
        stringList.add(context.getString(R.string.kredit_mobil));
        stringList.add(context.getString(R.string.kredit_sepeda_motor));
        stringList.add(context.getString(R.string.kredit_pegawai));
        stringList.add(context.getString(R.string.kpr));

        return stringList;
    }

    public static List<String> getTimeRangeList(Context context){
        List<String> stringList = new ArrayList<>();
        stringList.add(context.getString(R.string.time_range_1));
        stringList.add(context.getString(R.string.time_range_2));
        stringList.add(context.getString(R.string.time_range_3));
        stringList.add(context.getString(R.string.time_range_4));
        stringList.add(context.getString(R.string.time_range_5));
        stringList.add(context.getString(R.string.time_range_6));

        return stringList;
    }

    public static List<String> getCreditCeilingList(Context context){
        List<String> stringList = new ArrayList<>();
        stringList.add(context.getString(R.string.ceiling_1));
        stringList.add(context.getString(R.string.ceiling_2));
        stringList.add(context.getString(R.string.ceiling_3));
        stringList.add(context.getString(R.string.ceiling_4));
        stringList.add(context.getString(R.string.ceiling_5));
        stringList.add(context.getString(R.string.ceiling_6));
        stringList.add(context.getString(R.string.ceiling_7));
        stringList.add(context.getString(R.string.ceiling_8));
        stringList.add(context.getString(R.string.ceiling_9));
        stringList.add(context.getString(R.string.ceiling_10));

        return stringList;
    }

    public static List<Partner> getPartners() {
        List<Partner> partnerList = new ArrayList<>();

        partnerList.add(new Partner(1, "Ariel Tatum", "PT. Bank Rakyat Indonesia", -6.896923, 107.614888, 1));
        partnerList.add(new Partner(2, "Channing Tatum", "PT. Bank Negeri", -6.917464, 107.619123, 1));
        partnerList.add(new Partner(3, "Jacob Blazcskowsky", "PT. Mandiri", -6.759638, 107.609781, 2));
        partnerList.add(new Partner(4, "Didier Armando", "PT. Mitra Kukar", -6.943438, 107.646983, 3));

        return partnerList;
    }

    public static List<Partner> getPartnersWithResult() {
        List<Partner> partnerList = new ArrayList<>();

        partnerList.add(new Partner(5, "Channing Totem", "PT. Bank Negara Indonesia", -6.896923, 107.614898, 4));
        partnerList.add(new Partner(6, "Hazard", "PT. Mandiri", -6.917564, 107.613123, 4));
        partnerList.add(new Partner(7, "Shelly Elle Wood", "PT. Mega", -6.917454, 107.619113, 5));

        return partnerList;
    }

}
