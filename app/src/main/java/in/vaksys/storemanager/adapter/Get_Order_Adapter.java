package in.vaksys.storemanager.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import in.vaksys.storemanager.R;
import in.vaksys.storemanager.activity.OrderList_Detail_Activity;
import in.vaksys.storemanager.extra.ApiClient;
import in.vaksys.storemanager.extra.ApiInterface;
import in.vaksys.storemanager.extra.AppConfig;
import in.vaksys.storemanager.extra.MyApplication;
import in.vaksys.storemanager.extra.PreferenceHelper;
import in.vaksys.storemanager.model.getorederlist;
import in.vaksys.storemanager.response.DeleteOrder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by vishal on 7/18/2016.
 */
public class Get_Order_Adapter extends RecyclerView.Adapter<Get_Order_Adapter.ViewHolder> {
    private final PreferenceHelper preferenceHelper;
    private List<getorederlist> countries;
    private Context context;
    private String id,apikey;
    private int mypos;
    private MyApplication myApplication;

    public Get_Order_Adapter(Context context, List<getorederlist> countries) {
        this.countries = countries;
        this.context = context;

        preferenceHelper = new PreferenceHelper(context, "type");
        apikey = preferenceHelper.LoadStringPref(AppConfig.PREF_USER_KEY, "");
        myApplication = MyApplication.getInstance();
        myApplication.createDialog((Activity) context, false);


    }

    @Override
    public Get_Order_Adapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.set_get_order_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final Get_Order_Adapter.ViewHolder viewHolder, int i) {

        final getorederlist data = countries.get(i);

        viewHolder.u_id.setText("#" + data.getUid());
        viewHolder.customer_name.setText(data.getCustomername());
        viewHolder.product_count.setText(data.getProcut_count());
        viewHolder.coupan_count.setText(data.getCoupan_count());
        viewHolder.total.setText(data.getTotal());
        viewHolder.create_date.setText(data.getDate());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = data.getUid();
                Intent intent = new Intent(context, OrderList_Detail_Activity.class);
                intent.putExtra("id", id);
                context.startActivity(intent);
            }
        });

        viewHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = data.getUid();
                mypos = viewHolder.getAdapterPosition();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Sure Want to Delete this Order??")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int ids) {

                                DeleteOrder_Network_Call(id, mypos, apikey);
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    private void DeleteOrder_Network_Call(String id, final int mypos, String apikey) {
        myApplication.DialogMessage("Delete Order...");
        myApplication.showDialog();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<DeleteOrder> deleteOrderCall = apiInterface.DELETE_ORDER_CALL(id,apikey);
        deleteOrderCall.enqueue(new Callback<DeleteOrder>() {
            @Override
            public void onResponse(Call<DeleteOrder> call, Response<DeleteOrder> response) {

                ApiClient.showLog("code", "" + response.code());
                if (response.code() ==200){

                    if (!response.body().isError()){
                        myApplication.hideDialog();
                        countries.remove(mypos);

                        notifyDataSetChanged();
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }else {
                        myApplication.hideDialog();
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }else {
                    myApplication.hideDialog();
                    Toast.makeText(context, "Something Worng Response", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DeleteOrder> call, Throwable t) {
                myApplication.hideDialog();
                Toast.makeText(context, "No Internet Access", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView u_id, customer_name, product_count, coupan_count, total, create_date;
        private Button btn_edit,btn_delete;

        public ViewHolder(View view) {
            super(view);

            u_id = (TextView) view.findViewById(R.id.txt_orderid_getorder);
            customer_name = (TextView) view.findViewById(R.id.txt_name_getorder);
            product_count = (TextView) view.findViewById(R.id.txt_product_count_getorder);
            coupan_count = (TextView) view.findViewById(R.id.txt_coupan_count_getorder);
            total = (TextView) view.findViewById(R.id.txt_total_getorder);
            create_date = (TextView) view.findViewById(R.id.txt_date_getorder);

            btn_edit = (Button) view.findViewById(R.id.btn_update_order);
            btn_delete = (Button) view.findViewById(R.id.btn_delete_order);
        }
    }


}
