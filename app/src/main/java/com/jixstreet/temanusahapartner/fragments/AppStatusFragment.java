package com.jixstreet.temanusahapartner.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.jixstreet.temanusahapartner.SetUpMeetingActivity;
import com.jixstreet.temanusahapartner.WaitingForProcessActivity;
import com.jixstreet.temanusahapartner.AppConfirmedActivity;
import com.jixstreet.temanusahapartner.AppRejectedActivity;
import com.jixstreet.temanusahapartner.MeetUpProcessActivity;
import com.jixstreet.temanusahapartner.R;
import com.jixstreet.temanusahapartner.TemanUsahaApplication;
import com.jixstreet.temanusahapartner.WaitingForConfirmationActivity;
import com.jixstreet.temanusahapartner.adapters.AppStatusListAdapter;
import com.jixstreet.temanusahapartner.entities.Application;
import com.jixstreet.temanusahapartner.util.APIAgent;
import com.jixstreet.temanusahapartner.util.CommonConstants;
import com.jixstreet.temanusahapartner.util.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by satryaway on 10/17/2015.
 * Fragment for application status display
 */
public class AppStatusFragment extends Fragment {
    private View view;
    private static String POSITION_ARG = "POSITION";
    private ListView listView;
    private AppStatusListAdapter listAdapter;
    private int position;
    private List<Application> applicationList = new ArrayList<>();

    public static AppStatusFragment newInstance(int position) {
        AppStatusFragment appStatusFragment = new AppStatusFragment();
        Bundle b = new Bundle();
        b.putInt(POSITION_ARG, position);
        appStatusFragment.setArguments(b);

        return appStatusFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        position = getArguments().getInt(POSITION_ARG);
        view = inflater.inflate(R.layout.app_status_fragment_layout, null);
        initUI();
        setCallBack();

        getData();
        return view;
    }

    private void initUI() {
        listView = (ListView) view.findViewById(R.id.app_status_lv);
        listAdapter = new AppStatusListAdapter(getActivity(), new ArrayList<Application>());
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;

                switch (applicationList.get(position).getStatus()) {
                    case "administration process":
                        intent = new Intent(getActivity(), WaitingForProcessActivity.class);
                        intent.putExtra(CommonConstants.DATE, applicationList.get(position).getDatetime());
                        intent.putExtra(CommonConstants.APP_ID, applicationList.get(position).getId());
                        intent.putExtra(CommonConstants.FIRST_NAME, applicationList.get(position).getCustomer().getFirstName() + "  " + applicationList.get(position).getCustomer().getLastName());
                        intent.putExtra(CommonConstants.LOAN_TYPE, applicationList.get(position).getLoanType());
                        intent.putExtra(CommonConstants.LOAN_SEGMENT, applicationList.get(position).getLoanSegment());
                        intent.putExtra(CommonConstants.LOAN_PERIOD, applicationList.get(position).getTimeRange());
                        intent.putExtra(CommonConstants.JOB, applicationList.get(position).getCustomer().getJob());
                        intent.putExtra(CommonConstants.COMPANY_NAME, applicationList.get(position).getCustomer().getCompanyName());
                        intent.putExtra(CommonConstants.DATE_OF_BIRTH, applicationList.get(position).getCustomer().getDateOfBirth());
                        intent.putExtra(CommonConstants.MARITAL_STATUS, applicationList.get(position).getCustomer().getMaritalStatus());
                        break;

                    case "meet up":
                        intent = new Intent(getActivity(), MeetUpProcessActivity.class);
                        intent.putExtra(CommonConstants.DATE, applicationList.get(position).getProcessDatetime());
                        intent.putExtra(CommonConstants.APP_ID, applicationList.get(position).getId());
                        intent.putExtra(CommonConstants.PHONE_NUMBER, applicationList.get(position).getCustomer().getPhone());
                        intent.putExtra(CommonConstants.MEETUP_DATETIME, applicationList.get(position).getMeetupDatetime());
                        intent.putExtra(CommonConstants.MEETUP_VENUE, applicationList.get(position).getMeetupVenue());
                        intent.putExtra(CommonConstants.NOTES, applicationList.get(position).getNotes());
                        break;

                    case "process":
                        intent = new Intent(getActivity(), WaitingForConfirmationActivity.class);
                        intent.putExtra(CommonConstants.DATE, applicationList.get(position).getDatetime());
                        intent.putExtra(CommonConstants.ID, applicationList.get(position).getId());
                        break;

                    case "rejected":
                        intent = new Intent(getActivity(), AppRejectedActivity.class);
                        break;

                    case "cancelled":
                        intent = new Intent(getActivity(), AppRejectedActivity.class);
                        intent.putExtra(CommonConstants.IS_CANCELLED, true);
                        break;

                    case "confirmed":
                        intent = new Intent(getActivity(), SetUpMeetingActivity.class);
                        intent.putExtra(CommonConstants.APP_ID, applicationList.get(position).getId());
                        break;

                    default:
                        intent = new Intent(getActivity(), AppConfirmedActivity.class);
                        intent.putExtra(CommonConstants.DATE, applicationList.get(position).getProcessDatetime());
                        break;
                }

                startActivity(intent);
            }
        });
    }

    private void setCallBack() {

    }

    private void getData() {
        String url = CommonConstants.SERVICE_GET_USERS_LIST + TemanUsahaApplication.getInstance().getSharedPreferences().getInt(CommonConstants.ID, 1);
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.please_wait));

        APIAgent.get(url, null, new JsonHttpResponseHandler(){

            @Override
            public void onStart() {
                progressDialog.show();
            }

            @Override
            public void onFinish() {
                progressDialog.hide();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    int status = response.getInt(CommonConstants.STATUS);
                    if (status == CommonConstants.STATUS_OK) {
                        JSONArray jsonArray = response.getJSONArray(CommonConstants.RETURN_DATA);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Application application = Utility.parseApplications(jsonArray.getJSONObject(i));
                            if (position == 0 && (application.getStatus().equals(CommonConstants.ADMINISTRATION_PROCESS) || application.getStatus().equals(CommonConstants.MEET_UP) || application.getStatus().equals(CommonConstants.PROCESS))) {
                                applicationList.add(application);
                            }

                            if (position == 1 && (application.getStatus().equals(CommonConstants.CONFIRMED) || application.getStatus().equals(CommonConstants.REJECTED) || application.getStatus().equals(CommonConstants.CANCELLED) || application.getStatus().equals(CommonConstants.APPROVED) )) {
                                applicationList.add(application);
                            }
                        }

                        listAdapter.update(applicationList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getActivity(), R.string.RTO, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getActivity(), R.string.SERVER_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }
}