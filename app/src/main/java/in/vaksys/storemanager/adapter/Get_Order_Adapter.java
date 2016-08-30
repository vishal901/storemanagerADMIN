package in.vaksys.storemanager.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import in.vaksys.storemanager.R;
import in.vaksys.storemanager.activity.OrderList_Detail_Activity;
import in.vaksys.storemanager.model.getorederlist;


/**
 * Created by vishal on 7/18/2016.
 */
public class Get_Order_Adapter extends RecyclerView.Adapter<Get_Order_Adapter.ViewHolder> {
    private List<getorederlist> countries;
    private Context context;


    public Get_Order_Adapter(Context context, List<getorederlist> countries) {
        this.countries = countries;
        this.context = context;


    }

    @Override
    public Get_Order_Adapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.set_get_order_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Get_Order_Adapter.ViewHolder viewHolder, int i) {

        final getorederlist data = countries.get(i);

        viewHolder.u_id.setText("#"+data.getUid());
        viewHolder.customer_name.setText(data.getCustomername());
        viewHolder.product_count.setText(data.getProcut_count());
        viewHolder.coupan_count.setText(data.getCoupan_count());
        viewHolder.total.setText(data.getTotal());
        viewHolder.create_date.setText(data.getDate());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               String id =  data.getUid();
                Intent intent = new Intent(context,OrderList_Detail_Activity.class);
                intent.putExtra("id",id);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView u_id, customer_name, product_count,coupan_count,total,create_date;

        public ViewHolder(View view) {
            super(view);

            u_id = (TextView) view.findViewById(R.id.txt_orderid_getorder);
            customer_name = (TextView) view.findViewById(R.id.txt_name_getorder);
            product_count = (TextView) view.findViewById(R.id.txt_product_count_getorder);
            coupan_count = (TextView) view.findViewById(R.id.txt_coupan_count_getorder);
            total = (TextView) view.findViewById(R.id.txt_total_getorder);
            create_date = (TextView) view.findViewById(R.id.txt_date_getorder);
        }
    }


}
