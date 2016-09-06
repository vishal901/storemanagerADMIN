package in.vaksys.storemanager.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import in.vaksys.storemanager.R;
import in.vaksys.storemanager.extra.MyApplication;
import in.vaksys.storemanager.extra.PreferenceHelper;
import in.vaksys.storemanager.model.EventCustomer;
import in.vaksys.storemanager.model.customerlist;


/**
 * Created by vishal on 7/18/2016.
 */
public class Ailments_Adapter extends RecyclerView.Adapter<Ailments_Adapter.ViewHolder> {
    private List<customerlist> customerdata;
    private Context context;
    PreferenceHelper preferenceHelper;
    MyApplication myApplication;
    private String user_fname, user_gender, user_adddress, user_phone, user_email, user_ailment_key;
    private ArrayList<customerlist> ailemtArrayList = new ArrayList<>();
    private EventBus eventBus;

    public Ailments_Adapter(Context context, List<customerlist> countries) {
        this.customerdata = countries;
        this.context = context;

        myApplication = MyApplication.getInstance();
        myApplication.createDialog((Activity) context, false);

        eventBus = EventBus.getDefault();





    }

    @Override
    public Ailments_Adapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Ailments_Adapter.ViewHolder viewHolder, final int position) {

        final customerlist cust_data = customerdata.get(position);

        viewHolder.tv_name.setText(cust_data.getCustomername());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                eventBus.post(new EventCustomer(cust_data.getCustomer_id(),cust_data.getCustomername()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return customerdata.size();
    }
    public ArrayList<customerlist> getAllData(){
        return ailemtArrayList;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name;

        public ViewHolder(View view) {
            super(view);

            tv_name = (TextView) view.findViewById(R.id.countryName);
        }


    }



}
