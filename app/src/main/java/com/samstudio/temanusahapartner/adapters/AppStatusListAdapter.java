package com.samstudio.temanusahapartner.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.samstudio.temanusahapartner.R;
import com.samstudio.temanusahapartner.entities.Application;
import com.samstudio.temanusahapartner.util.CommonConstants;
import com.samstudio.temanusahapartner.util.TextConverter;
import com.samstudio.temanusahapartner.util.UniversalImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by satryaway on 10/17/2015.
 * adapter for application status
 */
public class AppStatusListAdapter extends BaseAdapter {
    private List<Application> applicationList = new ArrayList<>();
    private Context context;
    private UniversalImageLoader imageLoader;

    public AppStatusListAdapter (Context context, List<Application> applicationList) {
        this.context = context;
        this.applicationList = applicationList;
        imageLoader = new UniversalImageLoader(context);
        imageLoader.initImageLoader();
    }

    public void update(List<Application> applicationList) {
        this.applicationList = applicationList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return applicationList.size();
    }

    @Override
    public Object getItem(int position) {
        return applicationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder holder;

        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.partner_status_lv_item_layout, parent, false);
            holder.partnerPicIV = (ImageView) convertView.findViewById(R.id.partner_pic_iv);
            holder.partnerNameTV = (TextView) convertView.findViewById(R.id.partner_name_tv);
            holder.ptNameTV = (TextView) convertView.findViewById(R.id.pt_name_tv);
            holder.statusTV = (TextView) convertView.findViewById(R.id.app_status_tv);
            holder.dateTV = (TextView) convertView.findViewById(R.id.date_tv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        imageLoader.display(holder.partnerPicIV, CommonConstants.SERVICE_PROFILE_PIC + applicationList.get(position).getCustomer().getProfilePicture());
        holder.partnerNameTV.setText(applicationList.get(position).getCustomer().getFirstName() + " " + applicationList.get(position).getCustomer().getLastName());
        holder.ptNameTV.setText(applicationList.get(position).getCustomer().getCompanyName());
        holder.statusTV.setText(TextConverter.convertStatsCodeToString(applicationList.get(position).getStatus()));
        holder.dateTV.setText(applicationList.get(position).getDatetime());

        return convertView;
    }

    class ViewHolder {
        ImageView partnerPicIV;
        TextView partnerNameTV;
        TextView ptNameTV;
        TextView statusTV;
        TextView dateTV;
    }
}
