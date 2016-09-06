package in.vaksys.storemanager.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.TextView;
import android.widget.Toast;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import belka.us.androidtoggleswitch.widgets.BaseToggleSwitch;
import belka.us.androidtoggleswitch.widgets.ToggleSwitch;
import butterknife.Bind;
import butterknife.ButterKnife;
import in.vaksys.storemanager.R;
import in.vaksys.storemanager.adapter.CustomerList_Adapter;
import in.vaksys.storemanager.adapter.SpinnerFindusAdapter;
import in.vaksys.storemanager.adapter.SpinnerTextAdapterstatic;
import in.vaksys.storemanager.extra.ApiClient;
import in.vaksys.storemanager.extra.ApiInterface;
import in.vaksys.storemanager.extra.AppConfig;
import in.vaksys.storemanager.extra.DividerItemDecoration;
import in.vaksys.storemanager.extra.MyApplication;
import in.vaksys.storemanager.extra.PhoneNumberTextWatcher;
import in.vaksys.storemanager.extra.PreferenceHelper;
import in.vaksys.storemanager.model.customerlist;
import in.vaksys.storemanager.model.findus;
import in.vaksys.storemanager.response.FindAs;
import in.vaksys.storemanager.response.GetCustomerList;
import in.vaksys.storemanager.response.RegisterResponse;
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
    @Bind(R.id.btn_export_customer)
    Button btnExportCustomer;
    private Dialog dialog;
    @Bind(R.id.fab_customer)
    FloatingActionButton fabCustomer;
    ArrayList<customerlist> customerlistArrayList;
    private CustomerList_Adapter customerList_adapter;
    private String apikey, user_type, branchid;
    PreferenceHelper preferenceHelper;
    private EditText edtFirstNameUser, edtPhone, edtLastNameUser, edtAddressUser, edtEmailUser, edtOtherWriteUser, edtZipcode, edtLandlineUser, edtAddress1User;
    Spinner spDay, spMonth, spYear, spsourc;
    private ArrayList<findus> sourc;
    ToggleSwitch toggleMaleFemale;
    private String malefemale = "MALE";
    Button btnEditCustomer;
    Spinner spState, spCity;
    private ProgressBar progressBar2;
    private MyApplication myApplication;
    private ArrayList<String> day, month, year, state, city;
    private String st_day, st_month, st_yr, st_city, st_state, user_dob, user_city, user_state, user_fname, user_gender, user_adddress, user_phone, user_email, st_dob;
    private String st_findus_word = "";
    private String st_address2 = "";
    private String st_lastname = "";
    private String st_findustitle = "";
    private String st_zip = "";
    private String st_landline = "";

    private static final String IMAGE_DIRECTORY_NAME = "StoreManager";
    public static String timeStamp;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private File mediaFile;
    private List<String[]> data = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_customer_main, container, false);

        ButterKnife.bind(this, rootView);
        myApplication = MyApplication.getInstance();
        myApplication.createDialog(getActivity(), false);

        preferenceHelper = new PreferenceHelper(getActivity(), "type");
        apikey = preferenceHelper.LoadStringPref(AppConfig.PREF_USER_KEY, "");
        user_type = preferenceHelper.LoadStringPref(AppConfig.PREF_USER_TYPE, "");
        branchid = preferenceHelper.LoadStringPref(AppConfig.PREF_BRANCH_ID, "");
        ApiClient.showLog("key", apikey);

        if (user_type.equalsIgnoreCase("0")) {
            //admin login
            fabCustomer.setVisibility(View.GONE);

        }

        fabCustomer.setOnClickListener(this);
        btnExportCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                exportData();


            }
        });

        GetCustomerList_Network_Call();

        return rootView;


    }

    private void exportData() {




        if (customerlistArrayList.size() > 0) {


            try {

                myApplication.DialogMessage("Loading...");
                myApplication.showDialog();

                File mediaStorageDir = new File(
                        Environment
                                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                        IMAGE_DIRECTORY_NAME);

                // Create the storage directory if it does not exist
                if (!mediaStorageDir.exists()) {
                    if (!mediaStorageDir.mkdirs()) {
                        // MyApplication.getInstance().showLog("TAG", "Oops! Failed create " + IMAGE_DIRECTORY_NAME + " directory");

                    }
                }

                timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());


                mediaFile = new File(mediaStorageDir.getPath() + File.separator
                        + "file_" + timeStamp + ".csv");
                CSVWriter writer = new CSVWriter(new FileWriter(mediaFile));


                for (customerlist s : customerlistArrayList) {

                    data.add(new String[]{s.getCustomer_id(), s.getCustomername(), s.getCustomercontactno(),s.getCustomeraddress(), s.getCity()});
//                   data.add(new String[]{ s.getCity()});
//                   data.add(new String[]{s.getContactId()});

                }
                writer.writeAll(data);
                writer.close();


                Toast.makeText(getActivity(), "Export Successfully Show File "+mediaFile, Toast.LENGTH_SHORT).show();
                System.out.println("*** Also wrote this information to file: " + mediaFile);

                myApplication.hideDialog();

            } catch (Exception e) {

                e.printStackTrace();
                myApplication.hideDialog();
            }


        } else {
            myApplication.hideDialog();
            Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_SHORT).show();
        }
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

                            ApiClient.showLog("data", a.getFirstName());

                            customerlist data = new customerlist();

                            System.out.println(a.getId());

                            data.setCustomer_id(a.getId());
                            data.setCustomername((a.getFirstName()));
                            data.setCustomeraddress(a.getAddress());
                            data.setCustomergender(a.getGender());
                            data.setCustomercontactno(a.getMobile());
                            data.setCustomeremail(a.getEmail());
                            data.setDob(a.getDateOfBirth());
                            data.setFindus(a.getHowDidYouFindUs());
                            //// TODO: 8/27/2016 add new aliments

                            data.setCustomeraliment(a.getAilments());

                            data.setLastname(a.getLastName());
                            data.setAddress2(a.getAddress1());
                            data.setZipcode(a.getZip());
                            data.setCity(a.getCity());
                            data.setState(a.getState());
                            data.setLandline(a.getLandline());
                            data.setFindusreason(a.getFindUsReason());

                            customerlistArrayList.add(data);

                        }
                        recGetCustomerlist.addItemDecoration(new DividerItemDecoration(getActivity()));
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        recGetCustomerlist.setLayoutManager(layoutManager);
                        customerList_adapter = new CustomerList_Adapter(getActivity(), customerlistArrayList);
                        recGetCustomerlist.setAdapter(customerList_adapter);
                        customerList_adapter.notifyDataSetChanged();

                    } else {

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
        dialog.setContentView(R.layout.dailog_add_customer_new);
        dialog.show();
        findas_network_call();


        edtFirstNameUser = (EditText) dialog.findViewById(R.id.edt_fname_user);
        edtAddressUser = (EditText) dialog.findViewById(R.id.edt_address_user);
        edtPhone = (EditText) dialog.findViewById(R.id.edt_mobile_user);
        edtEmailUser = (EditText) dialog.findViewById(R.id.edt_email_user);
        toggleMaleFemale = (ToggleSwitch) dialog.findViewById(R.id.toggle_male_female);
        edtOtherWriteUser = (EditText) dialog.findViewById(R.id.edt_other_write_user);
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
//        st_zip = edtZipcode.getText().toString();
//        st_landline = edtLandlineUser.getText().toString();
//        st_findus_word = edtOtherWriteUser.getText().toString();
//        st_address2 = edtAddress1User.getText().toString();
//        st_lastname = edtLastNameUser.getText().toString();
//

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

        SpinnerTextAdapterstatic spinnerTextAdapterday = new SpinnerTextAdapterstatic(getActivity(), day);
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

        SpinnerTextAdapterstatic spinnerTextAdaptermonth = new SpinnerTextAdapterstatic(getActivity(), month);
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

        SpinnerTextAdapterstatic spinnerTextAdapteryr = new SpinnerTextAdapterstatic(getActivity(), year);
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

        SpinnerTextAdapterstatic spinnerTextAdaptercity = new SpinnerTextAdapterstatic(getActivity(), city);
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

        SpinnerTextAdapterstatic spinnerTextAdapterstate = new SpinnerTextAdapterstatic(getActivity(), state);
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

                    user_dob = "";
                } else {

                    user_dob = st_dob;
                }
//----------------------------------------------------------------------

                if (st_city.equalsIgnoreCase("Select City")) {

                    user_city = "";

                } else {
                    user_city = st_city;
                }

//------------------------------------------------------------------------
                if (st_state.equalsIgnoreCase("Select State")) {

                    user_state = "";

                } else {
                    user_state = st_state;
                }

//-------------------------------------------------------------------------

                st_zip = edtZipcode.getText().toString();
                st_landline = edtLandlineUser.getText().toString();
                st_findus_word = edtOtherWriteUser.getText().toString();
                st_address2 = edtAddress1User.getText().toString();
                st_lastname = edtLastNameUser.getText().toString();

                user_fname = edtFirstNameUser.getText().toString();
                user_adddress = edtAddressUser.getText().toString();
                user_phone = edtPhone.getText().toString();
                user_email = edtEmailUser.getText().toString();


                customer_detail_call(branchid, user_fname, user_adddress, user_phone, user_email, malefemale);


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

                                    st_findustitle = aa.getTitle();

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
        if (edtPhone.getText().toString().trim().isEmpty()) {
            edtPhone.setError(getString(R.string.err_msg_number));
            requestFocus(edtPhone);
            return false;
        }
        if (edtPhone.length() != 12) {
            edtPhone.setError(getString(R.string.err_msg_valid_number));
            requestFocus(edtPhone);
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
        Call<RegisterResponse> responseCall = apiInterface.REGISTER_RESPONSE_CALL(branchid,
                user_fname, malefemale, user_adddress, user_phone, user_email, "",
                st_lastname, user_dob, st_address2, user_city, user_state, st_zip, st_findustitle, "", st_findus_word, st_landline);

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
