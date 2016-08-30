package in.vaksys.storemanager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import in.vaksys.storemanager.R;
import in.vaksys.storemanager.model.EventData;
import in.vaksys.storemanager.model.branch;
import in.vaksys.storemanager.model.product;
import in.vaksys.storemanager.model.productdata;


/**
 * Created by vishal on 7/18/2016.
 */
public class Add_Product_Adapter extends RecyclerView.Adapter<Add_Product_Adapter.ViewHolder> {
    private ArrayList<productdata> countries;
    private Context context;
    private int mypos;
    private productdata data;
    private int single_price;
    private EventBus eventBus = EventBus.getDefault();


    public Add_Product_Adapter(Context context, ArrayList<productdata> countries) {
        this.countries = countries;
        this.context = context;


    }

    @Override
    public Add_Product_Adapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.set_product_data, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final Add_Product_Adapter.ViewHolder viewHolder, int i) {

        data = countries.get(i);

        // viewHolder.branchid.setText(data.getProduct_id());
        viewHolder.branchname.setText(data.getProduct_name());
        // viewHolder.branchaddress.setText(data.getProduct_price());

        viewHolder.imgdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mypos = viewHolder.getAdapterPosition();
                data = countries.get(mypos);
                countries.remove(mypos);
                single_price = Integer.parseInt(data.getProduct_price());

                eventBus.post(new EventData(single_price));
             //   EventBus.getDefault().post(new EventData(single_price));

                notifyDataSetChanged();
            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(context,Pain_Activity1.class);
//                context.startActivity(intent);
            }
        });
    }

    public ArrayList<productdata> getAllData() {
        return countries;
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView branchid, branchname, branchaddress;
        private ImageView imgdelete;

        public ViewHolder(View view) {
            super(view);

            //branchid = (TextView) view.findViewById(R.id.txt_branch_id);
            branchname = (TextView) view.findViewById(R.id.spin_text_cupan);
            imgdelete = (ImageView) view.findViewById(R.id.img_delete_product);
            //  branchaddress = (TextView) view.findViewById(R.id.txt_branch_addresss);
        }
    }


}
