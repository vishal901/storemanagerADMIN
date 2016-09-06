package in.vaksys.storemanager.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.vaksys.storemanager.R;
import in.vaksys.storemanager.adapter.GetStroeManager_Adapter;
import in.vaksys.storemanager.adapter.SpinnerTextAdapter;
import in.vaksys.storemanager.extra.ApiClient;
import in.vaksys.storemanager.extra.ApiInterface;
import in.vaksys.storemanager.extra.AppConfig;
import in.vaksys.storemanager.extra.DividerItemDecoration;
import in.vaksys.storemanager.extra.MyApplication;
import in.vaksys.storemanager.extra.PreferenceHelper;
import in.vaksys.storemanager.model.branch;
import in.vaksys.storemanager.model.user;
import in.vaksys.storemanager.response.GetBranchAdmin;
import in.vaksys.storemanager.response.createuser;
import in.vaksys.storemanager.response.getstoremanagerlist;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lenovoi3 on 7/27/2016.
 */
public class UserFragment extends Fragment implements View.OnClickListener {

    @Bind(R.id.rec_get_user)
    RecyclerView recGetUser;
    @Bind(R.id.pb_get_user)
    ProgressBar pbGetUser;
    private Dialog dialog;
    @Bind(R.id.fab_user)
    FloatingActionButton fabUser;
    ApiInterface apiInterface;

    private ArrayList<branch> getbranch;
    private AppCompatSpinner spBranchUser;
    private ProgressBar progressBar;
    private LinearLayout linearLayout;
    private Button btncrateuser;
    private EditText edtUserName, edtPassword, edtConfimPassword;
    private String sUsername, sPassword, sConfingpasswod;
    private String apikey;
    PreferenceHelper preferenceHelper;
    private String branchid;
    private List<user> addUserList;
    private GetStroeManager_Adapter adapter;
    private MyApplication myApplication;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_userlist_main, container, false);

        ButterKnife.bind(this, rootView);

        myApplication = MyApplication.getInstance();
        myApplication.createDialog(getActivity(), false);

        preferenceHelper = new PreferenceHelper(getActivity(), "type");
        apikey = preferenceHelper.LoadStringPref(AppConfig.PREF_USER_KEY, "");
        fabUser.setOnClickListener(this);

        getalluserlist(apikey);
        return rootView;


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View v) {

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dailog_add_user);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        Get_All_Branch(apikey);

        spBranchUser = (AppCompatSpinner) dialog.findViewById(R.id.sp_branch_user);
        btncrateuser = (Button) dialog.findViewById(R.id.btn_create_new_user_admin);
        edtUserName = (EditText) dialog.findViewById(R.id.edt_add_user_name_admin);
        edtPassword = (EditText) dialog.findViewById(R.id.edt_add_password_admin);
        edtConfimPassword = (EditText) dialog.findViewById(R.id.edt_add_confim_password_admin);
        progressBar = (ProgressBar) dialog.findViewById(R.id.pb_create_new_user_admin);
        linearLayout = (LinearLayout) dialog.findViewById(R.id.layout_create_user_admin);


        btncrateuser.setOnClickListener(new View.OnClickListener() {
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

                Add_User_Network_Call(sUsername, sPassword, apikey, branchid);


            }
        });

//        branch = new ArrayList<>();
//        branch.add("CG Road");
//
//
//        SpinnerTextAdapter spinnerTextAdapterday = new SpinnerTextAdapter(getActivity(), branch);
//        // attaching data adapter to spinner
//        spBranchUser.setAdapter(spinnerTextAdapterday);


    }

    private void Add_User_Network_Call(String username, String sPassword, String key, String branch) {

        progressBar.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.GONE);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<createuser> createuserCall = apiInterface.ADD_USER_RESPONSE_CALL(username, sPassword, branch, key);

        createuserCall.enqueue(new Callback<createuser>() {
            @Override
            public void onResponse(Call<createuser> call, Response<createuser> response) {

                ApiClient.showLog("code", "" + response.code());

                if (response.code() == 200) {
                    progressBar.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);

                    if (!response.body().isError()) {

                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.hide();
                        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                        getalluserlist(apikey);

                    } else {
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    progressBar.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "Something Worng Data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<createuser> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), "No Internet Access", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void getalluserlist(String apikey) {

        pbGetUser.setVisibility(View.VISIBLE);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<getstoremanagerlist> createuserCall = apiInterface.GET_USER_ADMIN_CALL(apikey);

        createuserCall.enqueue(new Callback<getstoremanagerlist>() {
            @Override
            public void onResponse(Call<getstoremanagerlist> call, Response<getstoremanagerlist> response) {
                ApiClient.showLog("code", "" + response.code());

                if (response.code() == 200) {
                    pbGetUser.setVisibility(View.GONE);
                    List<getstoremanagerlist.DataBean> as = response.body().getData();


                    if (!response.body().isError()) {

                        addUserList = new ArrayList<user>();

                        for (getstoremanagerlist.DataBean a : as) {

                            user data = new user();
                            data.setId_user((a.getId()));
                            data.setUsername(a.getUsername());
                            data.setUserbranch(a.getBranchName());
                            addUserList.add(data);

                        }
                        recGetUser.addItemDecoration(new DividerItemDecoration(getActivity()));
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        recGetUser.setLayoutManager(layoutManager);
                        adapter = new GetStroeManager_Adapter(getActivity(), addUserList);
                        recGetUser.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                    } else {

                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    pbGetUser.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Something Wrong Data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<getstoremanagerlist> call, Throwable t) {
                pbGetUser.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "No Internet Access", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void Get_All_Branch(String apikey) {

        myApplication.DialogMessage("Loading Branch...");
        myApplication.showDialog();


        apiInterface = ApiClient.getClient().create(ApiInterface.class);
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

                                                   SpinnerTextAdapter spinnerTextAdapterday = new SpinnerTextAdapter(getActivity(), getbranch);
                                                   spBranchUser.setAdapter(spinnerTextAdapterday);
                                                   spBranchUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                       @Override
                                                       public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                                           GetBranchAdmin.BranchBean branchBean = response.body().getBranch().get(position);
                                                           branchid = branchBean.getId();
                                                       }

                                                       @Override
                                                       public void onNothingSelected(AdapterView<?> parent) {

                                                       }
                                                   });

                                               } else {
                                                   myApplication.hideDialog();

                                                   Toast.makeText(getActivity(), "Somthing worng", Toast.LENGTH_SHORT).show();
                                               }


                                           }
                                       }

                                       @Override
                                       public void onFailure(Call<GetBranchAdmin> call, Throwable t) {
                                           myApplication.hideDialog();

                                           Toast.makeText(getActivity(), "No Internet Accesss", Toast.LENGTH_SHORT).show();
                                       }
                                   }

        );

    }


    private boolean validateusername() {
        if (edtUserName.getText().toString().trim().isEmpty()) {
            edtUserName.setError(getString(R.string.err_msg_first_name));
            requestFocus(edtUserName);
            return false;
        } else {
            return true;
        }
    }

    private boolean validatepassword() {
        if (edtPassword.getText().toString().trim().isEmpty()) {
            edtPassword.setError(getString(R.string.err_msg_password));
            requestFocus(edtPassword);
            return false;
        } else {
            return true;
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

}
