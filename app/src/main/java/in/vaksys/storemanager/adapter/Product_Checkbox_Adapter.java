package in.vaksys.storemanager.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import in.vaksys.storemanager.R;
import in.vaksys.storemanager.extra.ApiClient;
import in.vaksys.storemanager.extra.MyApplication;
import in.vaksys.storemanager.extra.PreferenceHelper;
import in.vaksys.storemanager.model.EventCustomer;
import in.vaksys.storemanager.model.customerlist;
import in.vaksys.storemanager.model.product;


/**
 * Created by vishal on 7/18/2016.
 */
public class Product_Checkbox_Adapter extends RecyclerView.Adapter<Product_Checkbox_Adapter.ViewHolder> {
    private List<product> customerdata;
    private Context context;
    PreferenceHelper preferenceHelper;
    MyApplication myApplication;
    private String user_fname, user_gender, user_adddress, user_phone, user_email, user_ailment_key;
    private ArrayList<product> ailemtArrayList = new ArrayList<>();
    private EventBus eventBus;
    private ArrayList<product> productArrayList= new ArrayList<>();

    public Product_Checkbox_Adapter(Context context, List<product> countries) {
        this.customerdata = countries;
        this.context = context;

        myApplication = MyApplication.getInstance();
        myApplication.createDialog((Activity) context, false);

        eventBus = EventBus.getDefault();

    }

    @Override
    public Product_Checkbox_Adapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row_product, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Product_Checkbox_Adapter.ViewHolder viewHolder, final int position) {

        final product cust_data = customerdata.get(position);

        viewHolder.tv_id.setText(cust_data.getProduct_id());
        viewHolder.CheckBoxAddProduct.setText(cust_data.getProduct_name());


        viewHolder.CheckBoxAddProduct.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){


                    ApiClient.showLog("check box data", customerdata.get(position).getProduct_name());


                    product product = new product();
                    product.setProduct_name(customerdata.get(position).getProduct_name());
                    product.setProduct_id(customerdata.get(position).getProduct_id());
                    product.setProduct_price(customerdata.get(position).getProduct_price());
                    productArrayList.add(product);

                    for (product s : productArrayList){

                        ApiClient.showLog("data",s.getProduct_price());
                    }

                }else {

                    productArrayList.remove(position);
                    for (product s : productArrayList){

                        ApiClient.showLog("data",s.getProduct_price());
                    }

                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return customerdata.size();
    }

    public ArrayList<product> getAllData() {
        return productArrayList;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name, tv_id;
        private CheckBox CheckBoxAddProduct;

        public ViewHolder(View view) {
            super(view);
            CheckBoxAddProduct = (CheckBox) view.findViewById(R.id.cb_select_product);
            tv_id = (TextView) view.findViewById(R.id.txt_product_id);
        }


    }


}
