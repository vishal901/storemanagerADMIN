package in.vaksys.storemanager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import in.vaksys.storemanager.R;
import in.vaksys.storemanager.model.product;


/**
 * Created by vishal on 7/18/2016.
 */
public class Set_Product_Pay_Adapter extends RecyclerView.Adapter<Set_Product_Pay_Adapter.ViewHolder> {
    private ArrayList<product> productArrayList;
    private Context context;
    private product data;


    public Set_Product_Pay_Adapter(Context context, ArrayList<product> list) {
        this.productArrayList = list;
        this.context = context;


    }

    @Override
    public Set_Product_Pay_Adapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.set_product_pay_data, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final Set_Product_Pay_Adapter.ViewHolder viewHolder, int i) {

        data = productArrayList.get(i);

        viewHolder.p_name.setText(data.getProduct_name());
        viewHolder.p_price.setText(data.getProduct_price());

    }

    public ArrayList<product> getAllData() {
        return productArrayList;
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView p_price, p_name;

        public ViewHolder(View view) {
            super(view);

            p_name = (TextView) view.findViewById(R.id.txt_p_name);
            p_price = (TextView) view.findViewById(R.id.txt_p_price);
        }
    }


}
