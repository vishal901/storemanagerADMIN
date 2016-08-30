package in.vaksys.storemanager.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import belka.us.androidtoggleswitch.widgets.BaseToggleSwitch;
import belka.us.androidtoggleswitch.widgets.ToggleSwitch;
import in.vaksys.storemanager.R;
import in.vaksys.storemanager.extra.ApiClient;
import in.vaksys.storemanager.extra.ApiInterface;
import in.vaksys.storemanager.extra.AppConfig;
import in.vaksys.storemanager.extra.MyApplication;
import in.vaksys.storemanager.extra.PreferenceHelper;
import in.vaksys.storemanager.model.branch;
import in.vaksys.storemanager.model.customerlist;
import in.vaksys.storemanager.model.findus;
import in.vaksys.storemanager.response.DeleteCustomer;
import in.vaksys.storemanager.response.FindAs;
import in.vaksys.storemanager.response.GetBranchAdmin;
import in.vaksys.storemanager.response.UpdateCustomer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by vishal on 7/18/2016.
 */
public class CustomerList_Adapter extends RecyclerView.Adapter<CustomerList_Adapter.ViewHolder> implements View.OnClickListener {
    private List<customerlist> countries;
    private Context context;
    private String id;
    private int mypos;
    private ViewHolder copy_holder;
    private static final String TAG = "CustomerList_Adapter";
    Dialog dialog;
    private EditText edtFirstNameUser, edtLastNameUser, edtAddressUser, edtMobileUser, edtEmailUser, edtOtherWriteUser;
    private String user_fname, user_gender, user_adddress, user_phone, user_email;
    Button btnEditCustomer;
    private String malefemale = "MALE";
    ToggleSwitch toggleMaleFemale;
    private ArrayList<String> day, month, year, state;
    Spinner spDay, spMonth, spYear, spsourc;
    private ArrayList<findus> sourc;
    PreferenceHelper preferenceHelper;
    private String apikey, branchid,type;
    private ProgressBar progressBar;
    private customerlist data;
    private MyApplication myApplication;
    private String aliments;

    public CustomerList_Adapter(Context context, List<customerlist> countries) {
        this.countries = countries;
        this.context = context;
        preferenceHelper = new PreferenceHelper(context, "type");
        apikey = preferenceHelper.LoadStringPref(AppConfig.PREF_USER_KEY, "");
        branchid = preferenceHelper.LoadStringPref(AppConfig.PREF_BRANCH_ID, "");
        type  =preferenceHelper.LoadStringPref(AppConfig.PREF_USER_TYPE,"");
        ApiClient.showLog("apikey", apikey);
        myApplication = MyApplication.getInstance();
        myApplication.createDialog((Activity) context, false);


    }

    @Override
    public CustomerList_Adapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.set_customer_list_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomerList_Adapter.ViewHolder viewHolder, int i) {

        data = countries.get(i);

        viewHolder.customername.setText(data.getCustomername());
        viewHolder.customeraddress.setText(data.getCustomeraddress());
        viewHolder.customergender.setText(data.getCustomergender());
        viewHolder.customercontactno.setText(data.getCustomercontactno());


        if (type.equalsIgnoreCase("0")) {
            //admin login
            viewHolder.imguserdelete.setEnabled(false);
            viewHolder.imguseredit.setEnabled(false);

        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(context,Pain_Activity1.class);
//                context.startActivity(intent);
            }
        });


        viewHolder.imguserdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                id = data.getCustomer_id();
                mypos = viewHolder.getAdapterPosition();
                Log.e(TAG, "onClick: " + mypos);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Sure Want to Delete this Customer??")
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
        viewHolder.imguseredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                customerlist data = countries.get(mypos);
                id = data.getCustomer_id();
                aliments = data.getCustomeraliment();
                mypos = viewHolder.getAdapterPosition();


                ApiClient.showLog("idposition", id + mypos);


                dialog = new Dialog(context);
                dialog.setTitle("Edit Customer Details");
                dialog.setContentView(R.layout.dailog_edit_user);
                dialog.show();

                findas_network_call();

                btnEditCustomer = (Button) dialog.findViewById(R.id.btn_submit_newmember);

                edtFirstNameUser = (EditText) dialog.findViewById(R.id.edt_fname_user);
                edtAddressUser = (EditText) dialog.findViewById(R.id.edt_address_user);
                edtMobileUser = (EditText) dialog.findViewById(R.id.edt_mobile_user);
                edtEmailUser = (EditText) dialog.findViewById(R.id.edt_email_user);
                toggleMaleFemale = (ToggleSwitch) dialog.findViewById(R.id.toggle_male_female);
                edtOtherWriteUser = (EditText) dialog.findViewById(R.id.edt_other_write_user);

                spDay = (Spinner) dialog.findViewById(R.id.sp_day);
                spMonth = (Spinner) dialog.findViewById(R.id.sp_month);
                spYear = (Spinner) dialog.findViewById(R.id.sp_Year);
                spsourc = (Spinner) dialog.findViewById(R.id.sp_source);
                progressBar = (ProgressBar) dialog.findViewById(R.id.pb_add_customer_detail);




                toggleMaleFemale.setCheckedTogglePosition(0);
                toggleMaleFemale.setOnToggleSwitchChangeListener(new BaseToggleSwitch.OnToggleSwitchChangeListener() {
                    @Override
                    public void onToggleSwitchChangeListener(int position, boolean isChecked) {

                        Log.e("poooo", "" + position);
                        if (position == 0) {

                            malefemale = "MALE";
                        } else {

                            malefemale = "FEMALE";
                        }
                    }
                });

                day = new ArrayList<>();
                for (int i = 1; i <= 31; i++) {
                    day.add(Integer.toString(i));
                }


                month = new ArrayList<>();
                for (int i = 1; i <= 12; i++) {
                    month.add(Integer.toString(i));
                }

                year = new ArrayList<>();
                for (int i = 1910; i <= 2008; i++) {
                    year.add(Integer.toString(i));
                }


                SpinnerTextAdapterstatic spinnerTextAdapterday = new SpinnerTextAdapterstatic(context, day);
                // attaching data adapter to spinner
                spDay.setAdapter(spinnerTextAdapterday);

                SpinnerTextAdapterstatic spinnerTextAdaptermonth = new SpinnerTextAdapterstatic(context, month);
                // attaching data adapter to spinner
                spMonth.setAdapter(spinnerTextAdaptermonth);

                SpinnerTextAdapterstatic spinnerTextAdapteryr = new SpinnerTextAdapterstatic(context, year);
                // attaching data adapter to spinner
                spYear.setAdapter(spinnerTextAdapteryr);


                edtFirstNameUser.setText(data.getCustomername());
                edtAddressUser.setText(data.getCustomeraddress());
                edtMobileUser.setText(data.getCustomercontactno());
                edtEmailUser.setText(data.getCustomeremail());


                btnEditCustomer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!validateFirstName()) {
                            return;
                        }
                        if (!validateAddress()) {
                            return;
                        }

                        if (!validateNumber()) {
                            return;
                        }
                        if (!validateEmail()) {
                            return;
                        }

                        user_fname = edtFirstNameUser.getText().toString();
                        user_adddress = edtAddressUser.getText().toString();
                        user_phone = edtMobileUser.getText().toString();
                        user_email = edtEmailUser.getText().toString();


                        EditUser_Network_Call(id, user_fname, user_phone, malefemale, user_email, user_adddress, branchid, apikey, mypos,aliments);

                    }
                });


            }
        });

        copy_holder = viewHolder;


    }

    private void findas_network_call() {

        myApplication.DialogMessage("Loading...");
        myApplication.showDialog();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<FindAs> call = apiInterface.findus();

        call.enqueue(new Callback<FindAs>() {
            @Override
            public void onResponse(Call<FindAs> call, final Response<FindAs> response) {
                if (response.isSuccessful()) {

                    response.code();
                    Log.e("code", "" + response.code());

                    if (response.code() == 200) {
                        myApplication.hideDialog();
                        List<FindAs.DataBean> as = response.body().getData();

                        if (!response.body().isError()) {

                            sourc = new ArrayList<>();

                            for (FindAs.DataBean a : as) {

                                findus findus = new findus();
                                findus.setId(a.getId());
                                findus.setCode(a.getCode());
                                findus.setTitle(a.getTitle());
                                sourc.add(findus);

                                System.out.println(a.getTitle());
                            }

                            SpinnerFindusAdapter spinnerTextAdapteryrs = new SpinnerFindusAdapter(context, sourc);
                            spsourc.setAdapter(spinnerTextAdapteryrs);
                            spsourc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    FindAs.DataBean aa = response.body().getData().get(position);

                                    ApiClient.showLog("titel", aa.getTitle());

                                    if (aa.getTitle().equalsIgnoreCase("Other")) {

                                        edtOtherWriteUser.setVisibility(View.VISIBLE);

                                    } else {

                                        edtOtherWriteUser.setVisibility(View.GONE);
                                    }


                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                        } else {

                            Toast.makeText(context, "No Response Found", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        myApplication.hideDialog();
                        Toast.makeText(context, "Something wrong", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<FindAs> call, Throwable t) {
                myApplication.hideDialog();
                Toast.makeText(context, "No Internet Access", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void EditUser_Network_Call(final String id, final String user_fname, final String user_phone, final String user_gender, final String user_email, final String user_adddress, String branchid, String apikey, final int mypos, String aliments) {

        progressBar.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<UpdateCustomer> deleteCustomerCall = apiInterface.UPDATE_CUSTOMER_CALL(id, user_fname, user_gender, user_adddress, user_phone, user_email, branchid, apikey, "", "", "", "", aliments, "");

        deleteCustomerCall.enqueue(new Callback<UpdateCustomer>() {
            @Override
            public void onResponse(Call<UpdateCustomer> call, Response<UpdateCustomer> response) {

                ApiClient.showLog("code", "" + response.code());

                if (response.code() == 200) {


                    List<customerlist> customerlists = new ArrayList<customerlist>();

                    if (!response.body().isError()) {

                        progressBar.setVisibility(View.GONE);
//
//                        countries.clear();
//
//                        countries.addAll(countries);

                        countries.remove(mypos);
                        customerlist customerlist = new customerlist();
                        customerlist.setCustomer_id(id);
                        customerlist.setCustomername(user_fname);
                        customerlist.setCustomeremail(user_email);
                        customerlist.setCustomercontactno(user_phone);
                        customerlist.setCustomergender(user_gender);
                        customerlist.setCustomeraddress(user_adddress);
                        customerlists.add(customerlist);

                        countries.add(mypos,customerlist);
                        notifyDataSetChanged();
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        dialog.hide();

                    } else {

                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    progressBar.setVisibility(View.GONE);

                    Toast.makeText(context, "Something Worng Data", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<UpdateCustomer> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context, "No Internet Access", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.img_delete_user:

//                id = data.getCustomer_id();
//                mypos = copy_holder.getAdapterPosition();
//                Log.e(TAG, "onClick: " + mypos);
//                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
//                alertDialogBuilder.setTitle("Sure Want to Delete this Customer??")
//                        .setCancelable(false)
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int ids) {
//
//                                DeleteUser_Network_Call(id, mypos);
//                                dialog.cancel();
//                            }
//                        })
//                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                            }
//                        });
//
//                AlertDialog alertDialog = alertDialogBuilder.create();
//                alertDialog.show();


                break;
            case R.id.img_edit_user:
                Toast.makeText(context, "edit", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    private void DeleteUser_Network_Call(final String id, final int mypos, String apikey) {


        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

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

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView customername, customeraddress, customergender, customercontactno;
        private ImageView imguseredit, imguserdelete;

        public ViewHolder(View view) {
            super(view);

            customername = (TextView) view.findViewById(R.id.txt_customr_name);
            customeraddress = (TextView) view.findViewById(R.id.txt_customr_address);
            customergender = (TextView) view.findViewById(R.id.txt_customr_gender);
            customercontactno = (TextView) view.findViewById(R.id.txt_customr_contactno);
            imguserdelete = (ImageView) view.findViewById(R.id.img_delete_user);
            imguseredit = (ImageView) view.findViewById(R.id.img_edit_user);
        }
    }

    private boolean validateFirstName() {
        if (edtFirstNameUser.getText().toString().trim().isEmpty()) {
            edtFirstNameUser.setError(context.getString(R.string.err_msg_first_name));
            requestFocus(edtFirstNameUser);
            return false;
        } else {
            return true;
        }
    }

    private boolean validateAddress() {
        if (edtAddressUser.getText().toString().trim().isEmpty()) {
            edtAddressUser.setError(context.getString(R.string.err_msg_address));
            requestFocus(edtAddressUser);
            return false;
        } else {
            return true;
        }
    }

    private boolean validateNumber() {
        if (edtMobileUser.getText().toString().trim().isEmpty()) {
            edtMobileUser.setError(context.getString(R.string.err_msg_number));
            requestFocus(edtMobileUser);
            return false;
        }
        if (edtMobileUser.length() != 10) {
            edtMobileUser.setError(context.getString(R.string.err_msg_valid_number));
            requestFocus(edtMobileUser);
            return false;
        } else {
            return true;
        }
    }

    private boolean validateEmail() {
        String email = edtEmailUser.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            edtEmailUser.setError(context.getString(R.string.err_msg_email));
            requestFocus(edtEmailUser);
            return false;
        } else {
            return true;
        }
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
