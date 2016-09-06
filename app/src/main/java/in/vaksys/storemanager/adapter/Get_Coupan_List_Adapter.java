package in.vaksys.storemanager.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import in.vaksys.storemanager.R;
import in.vaksys.storemanager.extra.ApiClient;
import in.vaksys.storemanager.extra.ApiInterface;
import in.vaksys.storemanager.extra.AppConfig;
import in.vaksys.storemanager.extra.MyApplication;
import in.vaksys.storemanager.extra.PreferenceHelper;
import in.vaksys.storemanager.model.coupan;
import in.vaksys.storemanager.model.product;
import in.vaksys.storemanager.model.productdata;
import in.vaksys.storemanager.response.DeleteCustomer;
import in.vaksys.storemanager.response.UpdateCoupan;
import in.vaksys.storemanager.response.UpdateProduct;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by vishal on 7/18/2016.
 */
public class Get_Coupan_List_Adapter extends RecyclerView.Adapter<Get_Coupan_List_Adapter.ViewHolder> {
    private List<coupan> countries;
    private Context context;
    private int mypos;
    private ArrayList<coupan> productArrayList = new ArrayList<>();
    private String id,apikey;
    private PreferenceHelper preferenceHelper;
    private Dialog dialog;
    private EditText edtcoupanname, edtcoupanprice;
    private Button btnsavecoupan;
    ProgressBar progressBar;
    private String scoupanname, scoupanprice, branch_id,type;
    private MyApplication myApplication;

    public Get_Coupan_List_Adapter(Context context, List<coupan> countries) {
        this.countries = countries;
        this.context = context;

        preferenceHelper = new PreferenceHelper(context, "type");
        apikey = preferenceHelper.LoadStringPref(AppConfig.PREF_USER_KEY, "");
        branch_id = preferenceHelper.LoadStringPref(AppConfig.PREF_BRANCH_ID, "");
        type  =preferenceHelper.LoadStringPref(AppConfig.PREF_USER_TYPE,"");
        myApplication = MyApplication.getInstance();
        myApplication.createDialog((Activity) context, false);
    }

    @Override
    public Get_Coupan_List_Adapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.set_product, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final Get_Coupan_List_Adapter.ViewHolder viewHolder, int i) {

        final coupan data = countries.get(i);

        viewHolder.coupan_no.setText(data.getCoupan_id());
        viewHolder.coupanname.setText(data.getCoupan_name());
        viewHolder.coupanprice.setText("\u0024"+data.getCoupan_price());

        if (type.equalsIgnoreCase("1")) {
            //admin login
            viewHolder.img_delete.setEnabled(false);
            viewHolder.img_edit.setEnabled(false);

        }

        viewHolder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                id = data.getCoupan_id();
                mypos = viewHolder.getAdapterPosition();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Sure Want to Delete this Coupan??")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int ids) {

                                DeleteCoupan_Network_Call(id, mypos, apikey);
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

        viewHolder.img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ApiClient.showLog("product id", data.getCoupan_id());
                mypos = viewHolder.getAdapterPosition();

                update_Product(data.getCoupan_id(),apikey,mypos,data.getCoupan_name(),data.getCoupan_price());
            }
        });
    }

    private void update_Product(final String coupan_id, final String apikey, int mypos, final String coupan_name, final String coupan_price) {


        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dailog_update_coupons);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        edtcoupanname = (EditText) dialog.findViewById(R.id.edt_coupan_name);
        edtcoupanprice = (EditText) dialog.findViewById(R.id.edt_coupan_price);
        btnsavecoupan = (Button) dialog.findViewById(R.id.btn_save_coupan);
        progressBar = (ProgressBar) dialog.findViewById(R.id.pb_create_cuopan);

        edtcoupanname.setText(coupan_name);
        edtcoupanprice.setText(coupan_price);

        btnsavecoupan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validateCoupanName()) {
                    return;
                }
                if (!validateCoupanPrice()) {
                    return;
                }

                scoupanname = edtcoupanname.getText().toString().trim();
                scoupanprice = edtcoupanprice.getText().toString().trim();

                Update_Coupan_Network_Call(scoupanname, scoupanprice, coupan_id, apikey,branch_id);
            }
        });

    }

    private void Update_Coupan_Network_Call(final String scoupanname, final String scoupanprice, final String coupan_id, String apikey, String branch_id) {

        progressBar.setVisibility(View.VISIBLE);
        btnsavecoupan.setEnabled(false);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<UpdateCoupan> updateProductCall  = apiInterface.URL_UPDATE_COUPAN(scoupanname,scoupanprice,coupan_id, branch_id,apikey);

        updateProductCall.enqueue(new Callback<UpdateCoupan>() {
            @Override
            public void onResponse(Call<UpdateCoupan> call, Response<UpdateCoupan> response) {

                if (response.code() == 200){

                    progressBar.setVisibility(View.GONE);
                    btnsavecoupan.setEnabled(true);

                    if (!response.body().isError()){

                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        countries.remove(mypos);
                        coupan coupanlist = new coupan();
                        coupanlist.setCoupan_id(coupan_id);
                        coupanlist.setCoupan_name(scoupanname);
                        coupanlist.setCoupan_price(scoupanprice);
                        productArrayList.add(coupanlist);

                        countries.add(mypos,coupanlist);
                        notifyDataSetChanged();

                        dialog.dismiss();

                    }else {

                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }

                }else {
                    progressBar.setVisibility(View.GONE);
                    btnsavecoupan.setEnabled(true);

                    Toast.makeText(context, "Something Worng Response", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UpdateCoupan> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                btnsavecoupan.setEnabled(true);

                Toast.makeText(context, "No Internet Access", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView coupan_no, coupanname, coupanprice;
        private ImageView img_edit, img_delete;

        public ViewHolder(View view) {
            super(view);

            coupan_no = (TextView) view.findViewById(R.id.txt_set_product_no);
            coupanname = (TextView) view.findViewById(R.id.txt_set_product_name);
            coupanprice = (TextView) view.findViewById(R.id.txt_set_product_price);
            img_edit = (ImageView) view.findViewById(R.id.img_edit_product);
            img_delete = (ImageView) view.findViewById(R.id.img_delete_product);
        }
    }

    private boolean validateCoupanName() {
        if (edtcoupanname.getText().toString().trim().isEmpty()) {
            edtcoupanname.setError(context.getString(R.string.err_msg_coupan_name));
            requestFocus(edtcoupanname);
            return false;
        } else {
            return true;
        }
    }

    private boolean validateCoupanPrice() {
        if (edtcoupanprice.getText().toString().trim().isEmpty()) {
            edtcoupanprice.setError(context.getString(R.string.err_msg_coupan_price));
            requestFocus(edtcoupanprice);
            return false;
        } else {
            return true;
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            ( (Activity)context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void DeleteCoupan_Network_Call(final String id, final int mypos, String apikey) {

        myApplication.DialogMessage("Delete Coupan...");
        myApplication.showDialog();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        //// TODO: 8/29/2016 change delete class
        Call<DeleteCustomer> deleteCustomerCall = apiInterface.DELETE_COUPAN_CALL(id, apikey);

        deleteCustomerCall.enqueue(new Callback<DeleteCustomer>() {
            @Override
            public void onResponse(Call<DeleteCustomer> call, Response<DeleteCustomer> response) {

                ApiClient.showLog("code", "" + response.code());

                if (response.code() == 200) {

                    if (!response.body().isError()) {

                        myApplication.hideDialog();
                        countries.remove(mypos);

                        notifyDataSetChanged();
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();


                    } else {
                        myApplication.hideDialog();
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    myApplication.hideDialog();
                    Toast.makeText(context, "Something Worng Data", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<DeleteCustomer> call, Throwable t) {
                myApplication.hideDialog();
                Toast.makeText(context, "No Internet Access", Toast.LENGTH_SHORT).show();

            }
        });

    }

}
