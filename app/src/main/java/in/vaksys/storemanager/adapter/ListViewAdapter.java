package in.vaksys.storemanager.adapter;

/**
 * Created by dell980 on 5/10/2016.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import in.vaksys.storemanager.R;
import in.vaksys.storemanager.extra.AdapterCallback;
import in.vaksys.storemanager.extra.AppConfig;
import in.vaksys.storemanager.extra.MyApplication;
import in.vaksys.storemanager.extra.PreferenceHelper;
import in.vaksys.storemanager.model.customerlist;



public class ListViewAdapter extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;

    private List<customerlist> worldpopulationlist = null;

    private ArrayList<customerlist> arraylist;
    private AdapterCallback mAdapterCallback;
    PreferenceHelper preferenceHelper;

    public ListViewAdapter(Fragment fragment,
                           List<customerlist> worldpopulationlist) {
        mContext = MyApplication.getInstance();
        this.worldpopulationlist = worldpopulationlist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<customerlist>();
        this.arraylist.addAll(worldpopulationlist);
        try {
            this.mAdapterCallback = ((AdapterCallback) fragment);
        } catch (ClassCastException e) {
            throw new ClassCastException("Fragment must implement AdapterCallback.");
        }
    }



    @Override
    public int getCount() {
        return worldpopulationlist.size();
    }

    @Override
    public customerlist getItem(int position) {
        return worldpopulationlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.list_row, null);
            // Locate the TextViews in listview_item.xml
            holder.code = (TextView) view.findViewById(R.id.code);
            holder.countryName = (TextView) view.findViewById(R.id.countryName);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.code.setText(worldpopulationlist.get(position).getCustomer_id());
        holder.countryName.setText(worldpopulationlist.get(position).getCustomername());
        // Listen for ListView Item Click
        view.setOnClickListener(
                new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                //Toast.makeText(mContext, worldpopulationlist.get(position).getCode(), Toast.LENGTH_SHORT).show();

                preferenceHelper = new PreferenceHelper(mContext,"data");
                preferenceHelper.initPref();
                preferenceHelper.SaveStringPref(AppConfig.PREF_USER_ID,worldpopulationlist.get(position).getCustomer_id());
                preferenceHelper.SaveStringPref(AppConfig.PREF_USER_NAME,worldpopulationlist.get(position).getCustomername());
                preferenceHelper.ApplyPref();

//                SharedPreferences sharedPreferences = MyApplication.getInstance().getSharedPreferences("harsh", Context.MODE_PRIVATE);
//
//                SharedPreferences.Editor edit = sharedPreferences.edit();
//
//                edit.putString("value", worldpopulationlist.get(position).getCode);
//                edit.apply();

                mAdapterCallback.onMethodCallback();

            }
        });

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        worldpopulationlist.clear();
        if (charText.length() == 0) {
            worldpopulationlist.addAll(arraylist);
        } else {
            for (customerlist wp : arraylist) {
                if (wp.getCustomername().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    worldpopulationlist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class ViewHolder {
        TextView code;
        TextView countryName;
    }

}