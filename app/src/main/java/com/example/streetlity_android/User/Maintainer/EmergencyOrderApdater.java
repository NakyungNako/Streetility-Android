package com.example.streetlity_android.User.Maintainer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.streetlity_android.R;

import java.util.ArrayList;

public class EmergencyOrderApdater extends ArrayAdapter {

    Context context;
    private ArrayList<EmergencyOrderObject> mOriginalValues;

    public EmergencyOrderApdater(@NonNull Context context, int resource, @NonNull ArrayList<EmergencyOrderObject> objects) {
        super(context, resource, objects);
        this.context = context;
        this.mOriginalValues = objects;
    }

    @Override
    public int getCount() {
        return (mOriginalValues == null) ? 0 :mOriginalValues.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView id,name,phone,reason;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        EmergencyOrderApdater.ViewHolder holder = null;
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        if (convertView == null) {

            holder = new EmergencyOrderApdater.ViewHolder();
            convertView = inflater.inflate(R.layout.lv_item_order, null);
            holder.id = (TextView) convertView.findViewById(R.id.id_order);
            holder.name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.phone = (TextView) convertView.findViewById(R.id.tv_phone);
            holder.reason = (TextView) convertView.findViewById(R.id.tv_address);
            convertView.setTag(holder);
        } else {
            holder = (EmergencyOrderApdater.ViewHolder) convertView.getTag();
        }


        holder.id.setText(Integer.toString(this.mOriginalValues.get(position).getId()));
        holder.name.setText(this.mOriginalValues.get(position).getName());
        holder.phone.setText(this.mOriginalValues.get(position).getPhone());
        holder.reason.setText(this.mOriginalValues.get(position).getReason());

        return convertView;
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        if(position < mOriginalValues.size()) {

        }else{
            position = position - mOriginalValues.size();
        }
        return this.mOriginalValues.get(position);
    }
}
