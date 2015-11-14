package com.samstudio.temanusahapartner.util;

import com.samstudio.temanusahapartner.entities.Application;
import com.samstudio.temanusahapartner.entities.Customer;
import com.samstudio.temanusahapartner.entities.Partner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by satryaway on 9/21/2015.
 */
public class Utility {
    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static List<Partner> parsePartners(JSONObject response) {
        List<Partner> partnerList = new ArrayList<>();
        try {
            JSONArray jsonArray = response.getJSONArray(CommonConstants.RETURN_DATA);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                Partner partner = new Partner();
                partner.setId(Integer.valueOf(object.getString(CommonConstants.ID)));
                partner.setFirstName(object.getString(CommonConstants.FIRST_NAME));
                partner.setLastName(object.getString(CommonConstants.LAST_NAME));
                partner.setCompany(object.getString(CommonConstants.COMPANY));
                partner.setLat(Double.valueOf(object.getString(CommonConstants.LATITUDE)));
                partner.setLng(Double.valueOf(object.getString(CommonConstants.LONGITUDE)));
                partnerList.add(partner);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return partnerList;
    }

    public static Partner parsePartner(JSONObject response) {
        Partner partner = new Partner();
        try {
            JSONObject object = response.getJSONObject(CommonConstants.RETURN_DATA);
            partner.setId(Integer.valueOf(object.getString(CommonConstants.ID)));
            partner.setFirstName(object.getString(CommonConstants.FIRST_NAME));
            partner.setLastName(object.getString(CommonConstants.LAST_NAME));
            partner.setPhoneNumber(object.getString(CommonConstants.PHONE));
            partner.setGender(object.getString(CommonConstants.GENDER));
            partner.setPlaceOfBirth(object.getString(CommonConstants.PLACE_OF_BIRTH));
            partner.setDateOfBirth(object.getString(CommonConstants.DATE_OF_BIRTH));
            partner.setCompany(object.getString(CommonConstants.COMPANY));
            partner.setBranch(object.getString(CommonConstants.BRANCH));
            partner.setDescription(object.getString(CommonConstants.DESCRIPTION));
            partner.setEmail(object.getString(CommonConstants.EMAIL));
            partner.setProfilePicture(object.getString(CommonConstants.PROFILE_PICTURE));
            partner.setLoanType(object.getString(CommonConstants.LOAN_TYPE));
            partner.setLoanSegment(object.getString(CommonConstants.LOAN_SEGMENT));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return partner;
    }

    public static Application parseApplications(JSONObject jsonObject) {
        Application application = new Application();
        try {
            Customer customer = new Customer();
            application.setId(jsonObject.getString(CommonConstants.APP_ID));
            application.setStatus(jsonObject.getString(CommonConstants.STATUS));
            application.setDatetime(jsonObject.getString(CommonConstants.DATETIME));
            application.setProcessDatetime(jsonObject.getString(CommonConstants.PROCESS_DATETIME));
            application.setMeetupDatetime(jsonObject.getString(CommonConstants.MEETUP_DATETIME));
            application.setMeetupVenue(jsonObject.getString(CommonConstants.MEETUP_VENUE));

            customer.setFirstName(jsonObject.getString(CommonConstants.FIRST_NAME));
            customer.setLastName(jsonObject.getString(CommonConstants.LAST_NAME));
            customer.setCompanyName(jsonObject.getString(CommonConstants.COMPANY_NAME));
            customer.setProfilePicture(jsonObject.getString(CommonConstants.PROFILE_PICTURE));
            customer.setPhone(jsonObject.getString(CommonConstants.PHONE));

            application.setCustomer(customer);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return application;
    }

    public static String convertLoanType(String loanType) {
        return null;
    }

    public static Application parseApplicants(JSONObject response) {
        Application application = new Application();
        try {
            application.setId(response.getString(CommonConstants.APP_ID));
            application.setLoanType(response.getString(CommonConstants.LOAN_TYPE));
            application.setLoanSegment(response.getString(CommonConstants.LOAN_SEGMENT));
            application.setTimeRange(response.getString(CommonConstants.LOAN_PERIOD));

            Customer customer = new Customer();
            customer.setId(response.getString(CommonConstants.ID));
            customer.setFirstName(response.getString(CommonConstants.FIRST_NAME));
            customer.setLastName(response.getString(CommonConstants.LAST_NAME));
            customer.setDateOfBirth(response.getString(CommonConstants.DATE_OF_BIRTH));
            customer.setProfilePicture(response.getString(CommonConstants.PROFILE_PICTURE));
            customer.setMaritalStatus(response.getString(CommonConstants.MARITAL_STATUS));
            customer.setJob(response.getString(CommonConstants.JOB));
            customer.setCompanyName(response.getString(CommonConstants.COMPANY_NAME));
            customer.setLatitude(Double.valueOf(response.getString(CommonConstants.LATITUDE)));
            customer.setLongitude(Double.valueOf(response.getString(CommonConstants.LONGITUDE)));

            application.setCustomer(customer);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return application;
    }
}
