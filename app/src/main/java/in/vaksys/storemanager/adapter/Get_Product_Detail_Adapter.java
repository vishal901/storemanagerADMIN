package in.vaksys.storemanager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import in.vaksys.storemanager.R;
import in.vaksys.storemanager.model.product;


/**
 * Created by vishal on 7/18/2016.
 */
public class Get_Product_Detail_Adapter extends RecyclerView.Adapter<Get_Product_Detail_Adapter.ViewHolder> {
    private List<product> countries;
    private Context context;


    public Get_Product_Detail_Adapter(Context context, List<product> countries) {
        this.countries = countries;
        this.context = context;


    }

    @Override
    public Get_Product_Detail_Adapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.set_product_detail, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Get_Product_Detail_Adapter.ViewHolder viewHolder, int i) {

        final product data = countries.get(i);

      //  viewHolder.product_no.setText(data.getProduct_id());
        viewHolder.productname.setText(data.getProduct_name());
        viewHolder.productprice.setText(data.getProduct_price());


    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView product_no, productname, productprice;

        public ViewHolder(View view) {
            super(view);

            productname = (TextView) view.findViewById(R.id.txt_set_product_name_detail);
            productprice = (TextView) view.findViewById(R.id.txt_set_product_price_detail);
        }
    }


}
