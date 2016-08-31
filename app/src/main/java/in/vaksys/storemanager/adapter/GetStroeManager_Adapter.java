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
import android.util.MalformedJsonException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import in.vaksys.storemanager.model.branch;
import in.vaksys.storemanager.model.user;
import in.vaksys.storemanager.response.DeleteUser;
import in.vaksys.storemanager.response.GetBranchAdmin;
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
    private MyApplication myApplication;
    private ArrayList<branch> getbranch;
    private String branchid;
    private String branchname;
    private ArrayList<user> userArrayList= new ArrayList<>();

    public GetStroeManager_Adapter(Context context, List<user> countries) {
        this.countries = countries;
        this.context = context;

        preferenceHelper = new PreferenceHelper(context, "type");
        apikey = preferenceHelper.LoadStringPref(AppConfig.PREF_USER_KEY, "");
        myApplication = MyApplication.getInstance();
        myApplication.createDialog((Activity) context, false);

        ApiClient.showLog("apikey",apikey);
    }

    @Override
    public GetStroeManager_Adapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.set_user, viewGroup, false);
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
                alertDialogBuilder.setTitle("Sure Want to Delete this User??")
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

                //// TODO: 8/30/2016 update store manager reming
               update_user(data.getId_user(), apikey, mypos, data.getUsername(), data.getUserbranch());
            }
        });
    }

    private void DeleteUser_Network_Call(String id, final int mypos, String apikey) {

        myApplication.DialogMessage("Delete User..");
        myApplication.showDialog();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        //// TODO: 8/29/2016 change delete user class
        Call<DeleteUser> deleteCustomerCall = apiInterface.DELETE_USER_CALL(id, apikey);

        deleteCustomerCall.enqueue(new Callback<DeleteUser>() {
            @Override
            public void onResponse(Call<DeleteUser> call, Response<DeleteUser> response) {

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
            public void onFailure(Call<DeleteUser> call, Throwable t) {
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


    private void update_user(final String user_id, final String apikey, final int mypos, String user_name, String branch) {

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dailog_update_user);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        Get_All_Branch(apikey);


        spBranchUser = (AppCompatSpinner) dialog.findViewById(R.id.sp_branch_user_update);
        btn_user_update = (Button) dialog.findViewById(R.id.btn_create_new_user_admin_update);
        edtUserName = (EditText) dialog.findViewById(R.id.edt_add_user_name_admin_update);
        edtPassword = (EditText) dialog.findViewById(R.id.edt_add_password_admin_update);
        edtConfimPassword = (EditText) dialog.findViewById(R.id.edt_add_confim_password_admin_update);
        progressBar = (ProgressBar) dialog.findViewById(R.id.pb_create_new_user_admin_update);
        linearLayout = (LinearLayout) dialog.findViewById(R.id.layout_create_user_admin_update);


        edtUserName.setText(user_name);

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

                Update_User_Network_Call(sUsername, sPassword, apikey, mypos,branchid,user_id,branchname);


            }
        });
    }

    //// TODO: 8/29/2016 update user
    private void Update_User_Network_Call(final String sUsername, String sPassword, String apikey, final int mypos, final String branchid, final String user_id, final String branchname) {

        progressBar.setVisibility(View.VISIBLE);
       // btnsaveproduct.setEnabled(false);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<UpdateUser> updateProductCall  = apiInterface.UPDATE_USER_RESPONSE_CALL(user_id,sUsername,branchid,apikey);

        updateProductCall.enqueue(new Callback<UpdateUser>() {
            @Override
            public void onResponse(Call<UpdateUser> call, Response<UpdateUser> response) {

                ApiClient.showLog("code",""+response.code());
                if (response.code() == 200){

                    progressBar.setVisibility(View.GONE);
                 //   btnsaveproduct.setEnabled(true);

                    if (!response.body().isError()){

                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        countries.remove(mypos);
                        user userdata = new user();
                        userdata.setUsername(sUsername);
                        userdata.setUserbranch(branchname);
                        userdata.setId_user(user_id);

                        userArrayList.add(userdata);
                        countries.add(mypos,userdata);
                        notifyDataSetChanged();
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

    private void Get_All_Branch(String apikey) {

        myApplication.DialogMessage("Loading Branch...");
        myApplication.showDialog();


       ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<GetBranchAdmin> getBranchAdminCall = apiInterface.GET_BRANCH_ADMIN_CALL(apikey);

        getBranchAdminCall.enqueue(new Callback<GetBranchAdmin>() {
                                       @Override
                                       public void onResponse(Call<GetBranchAdmin> call, final Response<GetBranchAdmin> response) {

                                           ApiClient.showLog("code", "" + response.code());
                                           if (response.code() == 200) {
                                               myApplication.hideDialog();

                                               List<GetBranchAdmin.BranchBean> as = response.body().getBranch();
                                               if (!response.body().isError()) {
                                                   getbranch = new ArrayList<branch>();

                                                   for (GetBranchAdmin.BranchBean a : as) {

                                                       branch getbranchadd = new branch();
                                                       getbranchadd.setId_branch(a.getId());
                                                       getbranchadd.setBranchname(a.getName());
                                                       getbranchadd.setBranchaddress(a.getAddress());
                                                       getbranch.add(getbranchadd);

                                                   }

                                                   SpinnerTextAdapter spinnerTextAdapterday = new SpinnerTextAdapter(context, getbranch);
                                                   spBranchUser.setAdapter(spinnerTextAdapterday);
                                                   spBranchUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                       @Override
                                                       public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                                           GetBranchAdmin.BranchBean branchBean = response.body().getBranch().get(position);
                                                           branchid = branchBean.getId();
                                                           branchname = branchBean.getName();
                                                           System.out.println(branchBean.getId()+ branchBean.getName());
                                                       }

                                                       @Override
                                                       public void onNothingSelected(AdapterView<?> parent) {

                                                       }
                                                   });

                                               } else {
                                                   myApplication.hideDialog();

                                                   Toast.makeText(context, "Somthing worng", Toast.LENGTH_SHORT).show();
                                               }


                                           }
                                       }

                                       @Override
                                       public void onFailure(Call<GetBranchAdmin> call, Throwable t) {
                                           myApplication.hideDialog();

                                           Toast.makeText(context, "No Internet Accesss", Toast.LENGTH_SHORT).show();
                                       }
                                   }

        );

    }

}


