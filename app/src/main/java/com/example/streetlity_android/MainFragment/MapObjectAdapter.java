package com.example.streetlity_android.MainFragment;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.streetlity_android.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MapObjectAdapter extends ArrayAdapter implements Filterable {

    Context context;
    private ArrayList<MapObject> mDisplayedValues;
    private ArrayList<MapObject> mOriginalValues;

    public MapObjectAdapter(@NonNull Context context, int resource, @NonNull ArrayList<MapObject> objects) {
        super(context, resource, objects);
        this.context = context;
        this.mDisplayedValues = objects;
        this.mOriginalValues = objects;
    }

    @Override
    public int getCount() {
        return (mDisplayedValues == null) ? 0 :mDisplayedValues.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView tvName, tvRating, tvAdrress, tvDistance;
        RatingBar rb;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder holder = null;
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        if (convertView == null) {

            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.lv_item_map_object, null);

            holder.tvName = convertView.findViewById(R.id.tv_name);
            holder.tvRating = convertView.findViewById(R.id.tv_rating);
            holder.tvAdrress = convertView.findViewById(R.id.tv_address);
            holder.tvDistance = convertView.findViewById(R.id.tv_distance);
            holder.rb = convertView.findViewById(R.id.ratingbar_map_object);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvName.setText(this.mDisplayedValues.get(position).getName());

        DecimalFormat df = new DecimalFormat("#.#");
        holder.tvRating.setText("("+ df.format(this.mDisplayedValues.get(position).getRating()) +")");

        holder.tvAdrress.setText(this.mDisplayedValues.get(position).getAddress());

        float distance = this.mDisplayedValues.get(position).getDistance();
        String dis = "m";
        if(distance > 1000){
            dis = "km";
            distance = distance / 1000;
        }
        holder.tvDistance.setText("~" + df.format(distance) + dis);

        holder.rb.setRating(this.mDisplayedValues.get(position).getRating());

        return convertView;
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        if(position < mDisplayedValues.size()) {

        }else{
            position = position - mDisplayedValues.size();
        }
        return this.mDisplayedValues.get(position);
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                mDisplayedValues = (ArrayList<MapObject>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<MapObject> FilteredArrList = new ArrayList<MapObject>();

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<MapObject>(mDisplayedValues); // saves the original data in mOriginalValues
                }

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0 || Integer.parseInt(constraint.toString()) == 0) {

                    // set the Original result to return
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                } else {
                    int filterNumber = Integer.parseInt(constraint.toString());
                    for (int i = 0; i < mOriginalValues.size(); i++) {
                        if (mOriginalValues.get(i).getBankId()== filterNumber) {
                            FilteredArrList.add(mOriginalValues.get(i));
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }
}