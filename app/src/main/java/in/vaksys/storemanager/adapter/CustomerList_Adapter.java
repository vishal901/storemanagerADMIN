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
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.stetho.Stetho;

import java.util.ArrayList;
import java.util.List;

import belka.us.androidtoggleswitch.widgets.BaseToggleSwitch;
import belka.us.androidtoggleswitch.widgets.ToggleSwitch;
import in.vaksys.storemanager.R;
import in.vaksys.storemanager.extra.ApiClient;
import in.vaksys.storemanager.extra.ApiInterface;
import in.vaksys.storemanager.extra.AppConfig;
import in.vaksys.storemanager.extra.MyApplication;
import in.vaksys.storemanager.extra.PhoneNumberTextWatcher;
import in.vaksys.storemanager.extra.PreferenceHelper;
import in.vaksys.storemanager.model.customerlist;
import in.vaksys.storemanager.model.findus;
import in.vaksys.storemanager.response.DeleteCustomer;
import in.vaksys.storemanager.response.FindAs;
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
    private EditText edtFirstNameUser, edtPhone, edtLastNameUser, edtAddressUser, edtEmailUser, edtfindusreason, edtZipcode, edtLandlineUser, edtAddress1User;

    Button btnEditCustomer;
    private String malefemale = "MALE";
    ToggleSwitch toggleMaleFemale;
    private ArrayList<String> day, month, year, state, city;
    private String st_day, st_month, find_us_title,st_yr, st_city, st_state, user_dob, user_city, user_state, user_fname, user_gender, user_adddress, user_phone, user_email, st_dob;
    private String st_findus_reason = "";
    private String st_address2 = "";
    private String st_lastname = "";
    private String st_findustitle = "";
    private String st_zip = "";
    private String st_landline = "";
    Spinner spDay, spMonth, spYear, spsourc;
    private ArrayList<findus> sourc;
    PreferenceHelper preferenceHelper;
    private String apikey, branchid, type;
    private ProgressBar progressBar;
    private MyApplication myApplication;
    private String aliments;
    Spinner spState, spCity;
    private ProgressBar progressBar2;
    private String dateofbrith="";

    public CustomerList_Adapter(Context context, List<customerlist> countries) {
        this.countries = countries;
        this.context = context;
        preferenceHelper = new PreferenceHelper(context, "type");
        apikey = preferenceHelper.LoadStringPref(AppConfig.PREF_USER_KEY, "");
        branchid = preferenceHelper.LoadStringPref(AppConfig.PREF_BRANCH_ID, "");
        type = preferenceHelper.LoadStringPref(AppConfig.PREF_USER_TYPE, "");
        ApiClient.showLog("apikey", apikey);
        myApplication = MyApplication.getInstance();
        myApplication.createDialog((Activity) context, false);

        Stetho.initializeWithDefaults(context);


    }

    @Override
    public CustomerList_Adapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.set_customer_list_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomerList_Adapter.ViewHolder viewHolder, int i) {

        final customerlist data = countries.get(i);


        viewHolder.customername.setText(data.getCustomername());
        viewHolder.customeraddress.setText(data.getCustomeraddress());
        viewHolder.customergender.setText(data.getCustomergender());
        viewHolder.customercontactno.setText(data.getCustomercontactno());


        if (type.equalsIgnoreCase("0")) {
            //admin login
            viewHolder.imguserdelete.setEnabled(false);
            viewHolder.imguseredit.setEnabled(false);

        }


//        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//
//                ApiClient.showLog("cutomer id",id);
////                Intent intent = new Intent(context,Pain_Activity1.class);
////                context.startActivity(intent);
//            }
//        });


        viewHolder.imguserdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                id = data.getCustomer_id();

                mypos = viewHolder.getAdapterPosition();
                Log.e(TAG, "onClick: " + mypos + id);
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


                mypos = viewHolder.getAdapterPosition();
                final customerlist data = countries.get(mypos);
                id = data.getCustomer_id();
                aliments = data.getCustomeraliment();
                dateofbrith = data.getDob();


                ApiClient.showLog("idposition", id + mypos);

                System.out.println("dateofbrith"+dateofbrith);


                dialog = new Dialog(context);
                dialog.setTitle("Edit Customer Details");
                dialog.setContentView(R.layout.dailog_update_customer_new);
                dialog.show();

                findas_network_call();

                edtFirstNameUser = (EditText) dialog.findViewById(R.id.edt_fname_user);
                edtAddressUser = (EditText) dialog.findViewById(R.id.edt_address_user);
                edtPhone = (EditText) dialog.findViewById(R.id.edt_mobile_user);
                edtEmailUser = (EditText) dialog.findViewById(R.id.edt_email_user);
                toggleMaleFemale = (ToggleSwitch) dialog.findViewById(R.id.toggle_male_female);
                edtfindusreason = (EditText) dialog.findViewById(R.id.edt_other_write_user);
                btnEditCustomer = (Button) dialog.findViewById(R.id.btn_submit_newmember);
                edtZipcode = (EditText) dialog.findViewById(R.id.edt_zipcode);
                edtLandlineUser = (EditText) dialog.findViewById(R.id.edt_landline_user);
                edtAddress1User = (EditText) dialog.findViewById(R.id.edt_address1_user);
                edtLastNameUser = (EditText) dialog.findViewById(R.id.edt_lname_user);
                spDay = (Spinner) dialog.findViewById(R.id.sp_day);
                spMonth = (Spinner) dialog.findViewById(R.id.sp_month);
                spYear = (Spinner) dialog.findViewById(R.id.sp_Year);
                spsourc = (Spinner) dialog.findViewById(R.id.sp_source);
                spCity = (Spinner) dialog.findViewById(R.id.sp_city);
                spState = (Spinner) dialog.findViewById(R.id.sp_state);
                progressBar2 = (ProgressBar) dialog.findViewById(R.id.pb_add_customer_detail);

                edtPhone.addTextChangedListener(new PhoneNumberTextWatcher(edtPhone));
                edtLandlineUser.addTextChangedListener(new PhoneNumberTextWatcher(edtLandlineUser));


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

                city = new ArrayList<>();

                city.add("Select City");
                city.add("Mehsana");
                city.add("Gandhinagar");
                city.add("Ahmedabad");

                state = new ArrayList<>();
                state.add("Select State");
                state.add("Gujarat");
                state.add("Panjab");


                SpinnerTextAdapterstatic spinnerTextAdapterday = new SpinnerTextAdapterstatic(context, day);
                // attaching data adapter to spinner
                spDay.setAdapter(spinnerTextAdapterday);


                spDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        st_day = ((TextView) view.findViewById(R.id.spin_text)).getText().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                SpinnerTextAdapterstatic spinnerTextAdaptermonth = new SpinnerTextAdapterstatic(context, month);
                // attaching data adapter to spinner
                spMonth.setAdapter(spinnerTextAdaptermonth);

                spMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        st_month = ((TextView) view.findViewById(R.id.spin_text)).getText().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                SpinnerTextAdapterstatic spinnerTextAdapteryr = new SpinnerTextAdapterstatic(context, year);
                // attaching data adapter to spinner
                spYear.setAdapter(spinnerTextAdapteryr);

                spYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        st_yr = ((TextView) view.findViewById(R.id.spin_text)).getText().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                SpinnerTextAdapterstatic spinnerTextAdaptercity = new SpinnerTextAdapterstatic(context, city);
                // attaching data adapter to spinner
                spCity.setAdapter(spinnerTextAdaptercity);

                spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        st_city = ((TextView) view.findViewById(R.id.spin_text)).getText().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                SpinnerTextAdapterstatic spinnerTextAdapterstate = new SpinnerTextAdapterstatic(context, state);
                // attaching data adapter to spinner
                spState.setAdapter(spinnerTextAdapterstate);

                spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        st_state = ((TextView) view.findViewById(R.id.spin_text)).getText().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


                edtFirstNameUser.setText(data.getCustomername());
                edtAddressUser.setText(data.getCustomeraddress());
                edtPhone.setText(data.getCustomercontactno());
                edtEmailUser.setText(data.getCustomeremail());
                edtLastNameUser.setText(data.getLastname());
                edtAddress1User.setText(data.getAddress2());
                edtZipcode.setText(data.getZipcode());
                edtLandlineUser.setText(data.getLandline());
                edtfindusreason.setText(data.getFindusreason());


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


                        st_dob = st_yr + "-" + st_day + "-" + st_month;
                        if (st_dob.equalsIgnoreCase("1910-1-1")) {

                            user_dob = dateofbrith;
                        } else {

                            user_dob = st_dob;
                        }
//----------------------------------------------------------------------

                        if (st_city.equalsIgnoreCase("Select City")) {

                            user_city = data.getCity();

                        } else {
                            user_city = st_city;
                        }

//------------------------------------------------------------------------
                        if (st_state.equalsIgnoreCase("Select State")) {

                            user_state = data.getState();

                        } else {
                            user_state = st_state;
                        }

//-------------------------------------------------------------------------

                        if (st_findustitle.equalsIgnoreCase(data.getFindus())){


                            find_us_title = data.getFindus();

                        }else {

                            find_us_title = st_findustitle;
                        }

                        st_zip = edtZipcode.getText().toString();
                        st_landline = edtLandlineUser.getText().toString();
                        st_findus_reason = edtfindusreason.getText().toString();
                        st_address2 = edtAddress1User.getText().toString();
                        st_lastname = edtLastNameUser.getText().toString();

                        user_fname = edtFirstNameUser.getText().toString();
                        user_adddress = edtAddressUser.getText().toString();
                        user_phone = edtPhone.getText().toString();
                        user_email = edtEmailUser.getText().toString();






                        EditUser_Network_Call(id, user_fname, user_phone, malefemale, user_email, user_adddress,
                                branchid, apikey, mypos, aliments,st_zip,st_landline,st_findus_reason,st_address2,st_lastname,user_dob, find_us_title,user_city,user_state);

                    }
                });


            }
        });



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
                                    st_findustitle = aa.getTitle();

                                    if (aa.getTitle().equalsIgnoreCase("Other")) {

                                        edtfindusreason.setVisibility(View.VISIBLE);

                                    } else {

                                        edtfindusreason.setVisibility(View.GONE);
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

    private void EditUser_Network_Call(final String id, final String user_fname, final String user_phone, final String user_gender, final String user_email, final String user_adddress, String branchid, String apikey, final int mypos, String aliments,
                                       String st_zip, String st_landline, String st_findus_reason, String st_address2, String st_lastname, String user_dob, String find_us, String user_city, String user_state) {

        progressBar2.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<UpdateCustomer> deleteCustomerCall = apiInterface.UPDATE_CUSTOMER_CALL(id, user_fname, user_gender, user_adddress, user_phone,
                user_email, branchid, apikey, st_lastname, user_dob, st_landline, find_us, aliments, "",st_address2,user_city,user_state,st_zip,st_findus_reason);

        deleteCustomerCall.enqueue(new Callback<UpdateCustomer>() {
            @Override
            public void onResponse(Call<UpdateCustomer> call, Response<UpdateCustomer> response) {

                ApiClient.showLog("code", "" + response.code());

                if (response.code() == 200) {


                    List<customerlist> customerlists = new ArrayList<customerlist>();

                    if (!response.body().isError()) {

                        progressBar2.setVisibility(View.GONE);
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

                        countries.add(mypos, customerlist);
                        notifyDataSetChanged();
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        dialog.hide();

                    } else {

                        progressBar2.setVisibility(View.GONE);
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    progressBar2.setVisibility(View.GONE);

                    Toast.makeText(context, "Something Worng Data", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<UpdateCustomer> call, Throwable t) {
                progressBar2.setVisibility(View.GONE);
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

        myApplication.DialogMessage("Delete Customer...");
        myApplication.showDialog();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<DeleteCustomer> deleteCustomerCall = apiInterface.DELETE_CUSTOMER_CALL(id, apikey);

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
        if (edtPhone.getText().toString().trim().isEmpty()) {
            edtPhone.setError(context.getString(R.string.err_msg_number));
            requestFocus(edtPhone);
            return false;
        }
        if (edtPhone.length() != 12) {
            edtPhone.setError(context.getString(R.string.err_msg_valid_number));
            requestFocus(edtPhone);
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
