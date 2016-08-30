package in.vaksys.storemanager.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.pinball83.maskededittext.MaskedEditText;

import java.util.ArrayList;
import java.util.List;

import belka.us.androidtoggleswitch.widgets.BaseToggleSwitch;
import belka.us.androidtoggleswitch.widgets.ToggleSwitch;
import butterknife.Bind;
import butterknife.ButterKnife;
import in.vaksys.storemanager.R;
import in.vaksys.storemanager.activity.AddCustomerActivity;
import in.vaksys.storemanager.adapter.CustomerList_Adapter;
import in.vaksys.storemanager.adapter.GetStroeManager_Adapter;
import in.vaksys.storemanager.adapter.SpinnerFindusAdapter;
import in.vaksys.storemanager.adapter.SpinnerTextAdapterstatic;
import in.vaksys.storemanager.extra.ApiClient;
import in.vaksys.storemanager.extra.ApiInterface;
import in.vaksys.storemanager.extra.AppConfig;
import in.vaksys.storemanager.extra.DividerItemDecoration;
import in.vaksys.storemanager.extra.MyApplication;
import in.vaksys.storemanager.extra.PreferenceHelper;
import in.vaksys.storemanager.model.customerlist;
import in.vaksys.storemanager.model.findus;
import in.vaksys.storemanager.response.FindAs;
import in.vaksys.storemanager.response.GetCustomerList;
import in.vaksys.storemanager.response.RegisterResponse;
import in.vaksys.storemanager.response.getstoremanagerlist;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lenovoi3 on 7/27/2016.
 */
public class CustomerFragment extends Fragment implements View.OnClickListener {

    @Bind(R.id.rec_get_customerlist)
    RecyclerView recGetCustomerlist;
    @Bind(R.id.pb_get_customerlist)
    ProgressBar pbGetCustomerlist;
    private Dialog dialog;
    @Bind(R.id.fab_customer)
    FloatingActionButton fabCustomer;
    ArrayList<customerlist> customerlistArrayList;
    private CustomerList_Adapter customerList_adapter;
    private String apikey,user_type,branchid;
    PreferenceHelper preferenceHelper;
    private EditText edtFirstNameUser, edtLastNameUser, edtAddressUser, edtEmailUser,edtOtherWriteUser;
    private String user_fname, user_gender, user_adddress, user_phone, user_email;
    private ArrayList<String> day, month, year, state;
    Spinner spDay,spMonth,spYear,spsourc;
    private ArrayList<findus> sourc;
    ToggleSwitch toggleMaleFemale;
    private String malefemale = "MALE";
    Button btnEditCustomer;
    private ProgressBar progressBar2;
    private MyApplication myApplication;
    private MaskedEditText maskedEditText_phone,maskedEditText_landline;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_customer_main, container, false);

        ButterKnife.bind(this, rootView);
        myApplication = MyApplication.getInstance();
        myApplication.createDialog(getActivity(), false);

        preferenceHelper = new PreferenceHelper(getActivity(),"type");
        apikey = preferenceHelper.LoadStringPref(AppConfig.PREF_USER_KEY,"");
        user_type = preferenceHelper.LoadStringPref(AppConfig.PREF_USER_TYPE, "");
        branchid = preferenceHelper.LoadStringPref(AppConfig.PREF_BRANCH_ID,"");
        ApiClient.showLog("key",apikey);

        if (user_type.equalsIgnoreCase("0")) {
            //admin login
            fabCustomer.setVisibility(View.GONE);

        }

        fabCustomer.setOnClickListener(this);

        GetCustomerList_Network_Call();

        return rootView;


    }

    private void GetCustomerList_Network_Call() {

        pbGetCustomerlist.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<GetCustomerList> getCustomerListCall = apiInterface.GET_CUSTOME_LIST(apikey);

        getCustomerListCall.enqueue(new Callback<GetCustomerList>() {
            @Override
            public void onResponse(Call<GetCustomerList> call, Response<GetCustomerList> response) {
                ApiClient.showLog("code", "" + response.code());
                if (response.code() == 200) {
                    pbGetCustomerlist.setVisibility(View.GONE);
                  List<GetCustomerList.DataBean> as = response.body().getData();
                    if (!response.body().isError()) {

                        customerlistArrayList = new ArrayList<customerlist>();
                        for (GetCustomerList.DataBean a : as) {

                            ApiClient.showLog("data",a.getFirstName());

                            customerlist data = new customerlist();
                            data.setCustomer_id(a.getId());
                            data.setCustomername((a.getFirstName()));
                            data.setCustomeraddress(a.getAddress());
                            data.setCustomergender(a.getGender());
                            data.setCustomercontactno(a.getMobile());
                            data.setCustomeraliment(a.getEmail());
                            //// TODO: 8/27/2016 add new aliments
                            data.setCustomer_id(a.getAilments());
                            customerlistArrayList.add(data);

                        }
                        recGetCustomerlist.addItemDecoration(new DividerItemDecoration(getActivity()));
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        recGetCustomerlist.setLayoutManager(layoutManager);
                        customerList_adapter = new CustomerList_Adapter(getActivity(), customerlistArrayList);
                        recGetCustomerlist.setAdapter(customerList_adapter);
                        customerList_adapter.notifyDataSetChanged();

                    }else {

                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    pbGetCustomerlist.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Something Worng Data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetCustomerList> call, Throwable t) {
                pbGetCustomerlist.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "No Internet Accesss", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View v) {

        dialog = new Dialog(getActivity());
        dialog.setTitle("Add Customer");
        dialog.setContentView(R.layout.dailog_add_customer);
        dialog.show();
        findas_network_call();


        edtFirstNameUser = (EditText) dialog.findViewById(R.id.edt_fname_user);
        edtAddressUser = (EditText) dialog.findViewById(R.id.edt_address_user);
        maskedEditText_phone = (MaskedEditText) dialog.findViewById(R.id.edt_mobile_user);
        edtEmailUser = (EditText) dialog.findViewById(R.id.edt_email_user);
        toggleMaleFemale  = (ToggleSwitch) dialog.findViewById(R.id.toggle_male_female);
        edtOtherWriteUser = (EditText) dialog.findViewById(R.id.edt_other_write_user);
        btnEditCustomer = (Button) dialog.findViewById(R.id.btn_submit_newmember);

        spDay = (Spinner) dialog.findViewById(R.id.sp_day);
        spMonth = (Spinner) dialog.findViewById(R.id.sp_month);
        spYear = (Spinner) dialog.findViewById(R.id.sp_Year);
        spsourc = (Spinner) dialog.findViewById(R.id.sp_source);
        progressBar2 = (ProgressBar) dialog.findViewById(R.id.pb_add_customer_detail);




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


        SpinnerTextAdapterstatic spinnerTextAdapterday = new SpinnerTextAdapterstatic(getActivity(), day);
        // attaching data adapter to spinner
        spDay.setAdapter(spinnerTextAdapterday);

        SpinnerTextAdapterstatic spinnerTextAdaptermonth = new SpinnerTextAdapterstatic(getActivity(), month);
        // attaching data adapter to spinner
        spMonth.setAdapter(spinnerTextAdaptermonth);

        SpinnerTextAdapterstatic spinnerTextAdapteryr = new SpinnerTextAdapterstatic(getActivity(), year);
        // attaching data adapter to spinner
        spYear.setAdapter(spinnerTextAdapteryr);

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
                user_phone = maskedEditText_phone.getText().toString();
                user_email = edtEmailUser.getText().toString();



                customer_detail_call(branchid,user_fname,user_adddress,user_phone,user_email,malefemale);


            }
        });

//        Intent intent = new Intent(getActivity(), AddCustomerActivity.class);
//        startActivity(intent);

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

                            SpinnerFindusAdapter spinnerTextAdapteryrs = new SpinnerFindusAdapter(getActivity(), sourc);
                            spsourc.setAdapter(spinnerTextAdapteryrs);
                            spsourc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    FindAs.DataBean aa = response.body().getData().get(position);

                                    ApiClient.showLog("titel",aa.getTitle());

                                    if (aa.getTitle().equalsIgnoreCase("Other")) {

                                        edtOtherWriteUser.setVisibility(View.VISIBLE);

                                    }else {

                                        edtOtherWriteUser.setVisibility(View.GONE);
                                    }


                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                        } else {

                            Toast.makeText(getActivity(), "No Response Found", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        myApplication.hideDialog();
                        Toast.makeText(getActivity(), "Something wrong", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<FindAs> call, Throwable t) {
                myApplication.hideDialog();
                Toast.makeText(getActivity(), "No Internet Access", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private boolean validateFirstName() {
        if (edtFirstNameUser.getText().toString().trim().isEmpty()) {
            edtFirstNameUser.setError(getString(R.string.err_msg_first_name));
            requestFocus(edtFirstNameUser);
            return false;
        } else {
            return true;
        }
    }

    private boolean validateAddress() {
        if (edtAddressUser.getText().toString().trim().isEmpty()) {
            edtAddressUser.setError(getString(R.string.err_msg_address));
            requestFocus(edtAddressUser);
            return false;
        } else {
            return true;
        }
    }
    private boolean validateNumber() {
        if (maskedEditText_phone.getText().toString().trim().isEmpty()) {
            maskedEditText_phone.setError(getString(R.string.err_msg_number));
            requestFocus(maskedEditText_phone);
            return false;
        }
        if (maskedEditText_phone.length() != 10) {
            maskedEditText_phone.setError(getString(R.string.err_msg_valid_number));
            requestFocus(maskedEditText_phone);
            return false;
        } else {
            return true;
        }
    }

    private boolean validateEmail() {
        String email = edtEmailUser.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            edtEmailUser.setError(getString(R.string.err_msg_email));
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
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void customer_detail_call(String branchid, String user_fname, String user_adddress, String user_phone, String user_email, String malefemale) {

        progressBar2.setVisibility(View.VISIBLE);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<RegisterResponse> responseCall = apiInterface.REGISTER_RESPONSE_CALL(branchid,user_fname,malefemale,user_adddress,user_phone,user_email);

        responseCall.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                progressBar2.setVisibility(View.GONE);

                if (response.code() == 200) {

                    if (!response.body().isError()) {

                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar2.setVisibility(View.GONE);
                        dialog.hide();

                        GetCustomerList_Network_Call();


                    } else {
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar2.setVisibility(View.GONE);

                    }
                } else {

                    Toast.makeText(getActivity(), "something worng", Toast.LENGTH_SHORT).show();
                    progressBar2.setVisibility(View.GONE);

                }

            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                progressBar2.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "No Internet Access", Toast.LENGTH_SHORT).show();

            }
        });

    }

}
