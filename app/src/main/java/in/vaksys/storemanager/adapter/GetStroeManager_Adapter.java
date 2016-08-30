package in.vaksys.storemanager.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import in.vaksys.storemanager.R;
import in.vaksys.storemanager.extra.ApiClient;
import in.vaksys.storemanager.extra.ApiInterface;
import in.vaksys.storemanager.extra.AppConfig;
import in.vaksys.storemanager.extra.PreferenceHelper;
import in.vaksys.storemanager.model.product;
import in.vaksys.storemanager.model.productdata;
import in.vaksys.storemanager.model.user;
import in.vaksys.storemanager.response.DeleteCustomer;
import in.vaksys.storemanager.response.UpdateProduct;
import in.vaksys.storemanager.response.UpdateUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by vishal on 7/18/2016.
 */
public class GetStroeManager_Adapter extends RecyclerView.Adapter<GetStroeManager_Adapter.ViewHolder> {
    private List<user> countries;
    private Context context;
    private int mypos;
    private PreferenceHelper preferenceHelper;
    private String apikey;
    private Dialog dialog;
    private AppCompatSpinner spBranchUser;
    private Button btn_user_update;
    private EditText edtUserName, edtPassword, edtConfimPassword;
    private ProgressBar progressBar;
    private LinearLayout linearLayout;
    private String sUsername, sPassword, sConfingpasswod;
    private String id;


    public GetStroeManager_Adapter(Context context, List<user> countries) {
        this.countries = countries;
        this.context = context;

        preferenceHelper = new PreferenceHelper(context, "type");
        apikey = preferenceHelper.LoadStringPref(AppConfig.PREF_USER_KEY, "");
    }

    @Override
    public GetStroeManager_Adapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.set_product, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GetStroeManager_Adapter.ViewHolder viewHolder, int i) {

        final user data = countries.get(i);

        viewHolder.userno.setText(data.getId_user());
        viewHolder.username.setText(data.getUsername());
        viewHolder.userbranch.setText(data.getUserbranch());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(context,Pain_Activity1.class);
//                context.startActivity(intent);
            }
        });

        viewHolder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                id = data.getId_user();
                mypos = viewHolder.getAdapterPosition();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Sure Want to Delete this Product??")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int ids) {

                                DeleteUser_Network_Call(id, mypos, apikey);
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

                ApiClient.showLog("product id", data.getId_user());
                mypos = viewHolder.getAdapterPosition();

                update_Product(data.getId_user(), apikey, mypos, data.getUsername(), data.getUserbranch());
            }
        });
    }

    private void DeleteUser_Network_Call(String id, final int mypos, String apikey) {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        //// TODO: 8/29/2016 change delete user class
        Call<DeleteCustomer> deleteCustomerCall = apiInterface.DELETE_CUSTOMER_CALL(id, apikey);

        deleteCustomerCall.enqueue(new Callback<DeleteCustomer>() {
            @Override
            public void onResponse(Call<DeleteCustomer> call, Response<DeleteCustomer> response) {

                ApiClient.showLog("code", "" + response.code());

                if (response.code() == 200) {

                    if (!response.body().isError()) {


                        countries.remove(mypos);

                        notifyDataSetChanged();
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();


                    } else {

                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {

                    Toast.makeText(context, "Something Worng Data", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<DeleteCustomer> call, Throwable t) {

                Toast.makeText(context, "No Internet Access", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView userno, username, userbranch;
        private ImageView img_edit, img_delete;

        public ViewHolder(View view) {
            super(view);

            userno = (TextView) view.findViewById(R.id.txt_set_product_no);
            username = (TextView) view.findViewById(R.id.txt_set_product_name);
            userbranch = (TextView) view.findViewById(R.id.txt_set_product_price);
            img_edit = (ImageView) view.findViewById(R.id.img_edit_product);
            img_delete = (ImageView) view.findViewById(R.id.img_delete_product);
        }
    }


    private void update_Product(final String user_id, final String apikey, final int mypos, String user_name, String user_password) {

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dailog_update_user);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();


        spBranchUser = (AppCompatSpinner) dialog.findViewById(R.id.sp_branch_user_update);
        btn_user_update = (Button) dialog.findViewById(R.id.btn_create_new_user_admin_update);
        edtUserName = (EditText) dialog.findViewById(R.id.edt_add_user_name_admin_update);
        edtPassword = (EditText) dialog.findViewById(R.id.edt_add_password_admin_update);
        edtConfimPassword = (EditText) dialog.findViewById(R.id.edt_add_confim_password_admin_update);
        progressBar = (ProgressBar) dialog.findViewById(R.id.pb_create_new_user_admin_update);
        linearLayout = (LinearLayout) dialog.findViewById(R.id.layout_create_user_admin_update);


        btn_user_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validateusername()) {
                    return;
                }
                if (!validatepassword()) {
                    return;
                }

                sUsername = edtUserName.getText().toString().trim();
                sPassword = edtPassword.getText().toString().trim();
                sConfingpasswod = edtConfimPassword.getText().toString().trim();

                Update_User_Network_Call(sUsername, sPassword, apikey, mypos);


            }
        });
    }

    //// TODO: 8/29/2016 update user
    private void Update_User_Network_Call(String sUsername, String sPassword, String apikey, int mypos) {

        progressBar.setVisibility(View.VISIBLE);
       // btnsaveproduct.setEnabled(false);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<UpdateUser> updateProductCall  = apiInterface.UPDATE_USER_RESPONSE_CALL(sUsername,sPassword,"",apikey);

        updateProductCall.enqueue(new Callback<UpdateUser>() {
            @Override
            public void onResponse(Call<UpdateUser> call, Response<UpdateUser> response) {

                if (response.code() == 200){

                    progressBar.setVisibility(View.GONE);
                 //   btnsaveproduct.setEnabled(true);

                    if (!response.body().isError()){

                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
//
//                        productdata.remove(mypos);
//                        product productlist = new product();
//                        productlist.setProduct_id(product_id);
//                        productlist.setProduct_name(sproductname);
//                        productlist.setProduct_price(sproductprice);
//                        productArrayList.add(productlist);
//
//                        productdata.add(mypos,productlist);
//                        notifyDataSetChanged();

                        dialog.dismiss();

                    }else {

                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }

                }else {
                    progressBar.setVisibility(View.GONE);
                 //   btnsaveproduct.setEnabled(true);

                    Toast.makeText(context, "Something Worng Response", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UpdateUser> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
              //  btnsaveproduct.setEnabled(true);

                Toast.makeText(context, "No Internet Access", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private boolean validateusername() {
        if (edtUserName.getText().toString().trim().isEmpty()) {
            edtUserName.setError(context.getString(R.string.err_msg_first_name));
            requestFocus(edtUserName);
            return false;
        } else {
            return true;
        }
    }

    private boolean validatepassword() {
        if (edtPassword.getText().toString().trim().isEmpty()) {
            edtPassword.setError(context.getString(R.string.err_msg_password));
            requestFocus(edtPassword);
            return false;
        } else {
            return true;
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }



}


