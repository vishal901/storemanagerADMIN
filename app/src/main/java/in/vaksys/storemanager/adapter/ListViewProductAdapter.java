package in.vaksys.storemanager.adapter;

/**
 * Created by dell980 on 5/10/2016.
 */

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import in.vaksys.storemanager.R;
import in.vaksys.storemanager.extra.AdapterCallback;
import in.vaksys.storemanager.extra.AdapterProductCallback;
import in.vaksys.storemanager.extra.ApiClient;
import in.vaksys.storemanager.extra.AppConfig;
import in.vaksys.storemanager.extra.MyApplication;
import in.vaksys.storemanager.extra.PreferenceHelper;
import in.vaksys.storemanager.model.customerlist;
import in.vaksys.storemanager.model.product;


public class ListViewProductAdapter extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;

    private List<product> worldpopulationlist = null;
    ArrayList<product> productArrayList;

    private ArrayList<product> arraylist;
    private AdapterProductCallback mAdapterCallback;
    PreferenceHelper preferenceHelper;

    public ListViewProductAdapter(Fragment fragment,
                                  List<product> worldpopulationlist) {
        mContext = MyApplication.getInstance();
        this.worldpopulationlist = worldpopulationlist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<product>();
        this.arraylist.addAll(worldpopulationlist);
        try {
            this.mAdapterCallback = ((AdapterProductCallback) fragment);
        } catch (ClassCastException e) {
            throw new ClassCastException("Fragment must implement AdapterCallback.");
        }
    }



    @Override
    public int getCount() {
        return worldpopulationlist.size();
    }

    @Override
    public product getItem(int position) {
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
            view = inflater.inflate(R.layout.list_row_product, null);
            // Locate the TextViews in listview_item.xml
            holder.code = (TextView) view.findViewById(R.id.txt_product_id);
//            holder.countryName = (TextView) view.findViewById(R.id.txt_product_name);
            holder.CheckBoxAddProduct = (CheckBox) view.findViewById(R.id.cb_select_product);
            view.setTag(holder);
            productArrayList = new ArrayList<product>();

            holder.CheckBoxAddProduct.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                   if (isChecked){


                   ApiClient.showLog("check box data", worldpopulationlist.get(position).getProduct_name());


                       product product = new product();
                       product.setProduct_name(worldpopulationlist.get(position).getProduct_name());
                       product.setProduct_id(worldpopulationlist.get(position).getProduct_id());
                       product.setProduct_price(worldpopulationlist.get(position).getProduct_price());
                       productArrayList.add(product);

                      for (product s : productArrayList){

                          ApiClient.showLog("data",s.getProduct_name());
                      }

                   }else {

                       productArrayList.remove(position);
                       for (product s : productArrayList){

                           ApiClient.showLog("data",s.getProduct_name());
                       }

                   }
                }
            });
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.code.setText(worldpopulationlist.get(position).getProduct_id());
//        holder.countryName.;
        holder.CheckBoxAddProduct.setText(worldpopulationlist.get(position).getProduct_name());
        // Listen for ListView Item Click
        view.setOnClickListener(
                new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                //Toast.makeText(mContext, worldpopulationlist.get(position).getCode(), Toast.LENGTH_SHORT).show();

                preferenceHelper = new PreferenceHelper(mContext,"data");
                preferenceHelper.initPref();
                preferenceHelper.SaveStringPref(AppConfig.PREF_PRODUCT_ID,worldpopulationlist.get(position).getProduct_id());
                preferenceHelper.SaveStringPref(AppConfig.PREF_PRODUCT_NAME,worldpopulationlist.get(position).getProduct_name());
                preferenceHelper.ApplyPref();


                mAdapterCallback.onMethodPoductCallback();

            }
        });

        return view;
    }

    public ArrayList<product> getAllData(){
        return productArrayList;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        worldpopulationlist.clear();
        if (charText.length() == 0) {
            worldpopulationlist.addAll(arraylist);
        } else {
            for (product wp : arraylist) {
                if (wp.getProduct_name().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    worldpopulationlist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class ViewHolder {
        TextView code;
//        TextView countryName;
        CheckBox CheckBoxAddProduct;
    }

}