package in.vaksys.storemanager.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import in.vaksys.storemanager.adapter.Branch_Adapter;
import in.vaksys.storemanager.extra.ApiClient;
import in.vaksys.storemanager.extra.ApiInterface;
import in.vaksys.storemanager.extra.AppConfig;
import in.vaksys.storemanager.extra.PreferenceHelper;
import in.vaksys.storemanager.model.branch;
import in.vaksys.storemanager.response.CreateBranchAdmin;
import in.vaksys.storemanager.response.GetBranchAdmin;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lenovoi3 on 7/27/2016.
 */
public class BranchFragment extends Fragment implements View.OnClickListener {
    @Bind(R.id.linearLayout)
    LinearLayout linearLayout;

    @Bind(R.id.pb_get_branch)
    ProgressBar pbGetBranch;
    @Bind(R.id.rec_get_branch)
    RecyclerView recGetBranch;
    private Dialog dialog;
    @Bind(R.id.fab_branch)
    FloatingActionButton fabBranch;

    private EditText edtBranchName, edtBranchAddress;
    private Button btnCreateBranch, btnFinish;
    private View layouthide;
    private ProgressBar progressBar;
    private String sBranchName, sBranchAddress;
    PreferenceHelper preferenceHelper;
    private String Apikey;
    ApiInterface apiInterface;
    Branch_Adapter adapter;

    private List<branch> addbranch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_branch_main, container, false);

        ButterKnife.bind(this, rootView);
        preferenceHelper = new PreferenceHelper(getActivity(), "type");
        Apikey = preferenceHelper.LoadStringPref(AppConfig.PREF_USER_KEY, "");
        ApiClient.showLog("api key", Apikey);
        GetBranch_Network_Call(Apikey);

        fabBranch.setOnClickListener(this);
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
        dialog.setContentView(R.layout.dailog_add_branch);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        btnCreateBranch = (Button) dialog.findViewById(R.id.btn_create_branch_admin);
        edtBranchName = (EditText) dialog.findViewById(R.id.edt_add_branch_name_admin);
        edtBranchAddress = (EditText) dialog.findViewById(R.id.edt_add_branch_address_admin);
        layouthide = dialog.findViewById(R.id.layout_hide);
        progressBar = (ProgressBar) dialog.findViewById(R.id.pb_create_branch);

        btnCreateBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!validateBranchName()) {
                    return;
                }
                if (!validateBranchAddress()) {
                    return;
                }

                sBranchName = edtBranchName.getText().toString().trim();
                sBranchAddress = edtBranchAddress.getText().toString().trim();

                CreateBranchAdminCall(sBranchName, sBranchAddress, Apikey);

            }
        });

//        btnFinish.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                dialog.hide();
//
//
//            }
//        });

        dialog.show();

    }

    private void GetBranch_Network_Call(String apikey) {

        pbGetBranch.setVisibility(View.VISIBLE);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<GetBranchAdmin> getBranchAdminCall = apiInterface.GET_BRANCH_ADMIN_CALL(apikey);

        getBranchAdminCall.enqueue(new Callback<GetBranchAdmin>() {
            @Override
            public void onResponse(Call<GetBranchAdmin> call, Response<GetBranchAdmin> response) {

                ApiClient.showLog("code", "" + response.code());
                if (response.code() == 200) {
                    pbGetBranch.setVisibility(View.GONE);

                    List<GetBranchAdmin.BranchBean> as = response.body().getBranch();
                    if (!response.body().isError()) {

                        addbranch = new ArrayList<branch>();

                        for (GetBranchAdmin.BranchBean a : as) {

                            branch data = new branch();
                            data.setId_branch((a.getId()));
                            data.setBranchname(a.getName());
                            data.setBranchaddress(a.getAddress());
                            addbranch.add(data);

                        }
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        recGetBranch.setLayoutManager(layoutManager);
                        adapter = new Branch_Adapter(getActivity(), addbranch);
                        recGetBranch.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                        //    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }

                } else {
                    pbGetBranch.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Somthing worng", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<GetBranchAdmin> call, Throwable t) {
                pbGetBranch.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "No Internet Accesss", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void CreateBranchAdminCall(String sBranchName, String sBranchAddress, final String apikey) {

        progressBar.setVisibility(View.VISIBLE);
        layouthide.setVisibility(View.GONE);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<CreateBranchAdmin> createBranchAdminCall = apiInterface.CREATE_BRANCH_RESPONSE_CALL(sBranchName, sBranchAddress, apikey);
        createBranchAdminCall.enqueue(new Callback<CreateBranchAdmin>() {
            @Override
            public void onResponse(Call<CreateBranchAdmin> call, Response<CreateBranchAdmin> response) {

                ApiClient.showLog("code", "" + response.code());

                if (response.code() == 200) {

                    progressBar.setVisibility(View.GONE);
                    layouthide.setVisibility(View.VISIBLE);

                    if (!response.body().isError()) {

                        ApiClient.showLog("callllll", "Callllling");

                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.hide();
                        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        GetBranch_Network_Call(apikey);
                    } else {
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }

                } else {

                    Toast.makeText(getActivity(), "Something Worng", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    layouthide.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onFailure(Call<CreateBranchAdmin> call, Throwable t) {

                progressBar.setVisibility(View.GONE);
                layouthide.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), "No Internet Access", Toast.LENGTH_SHORT).show();

            }
        });
    }


    private boolean validateBranchName() {
        if (edtBranchName.getText().toString().trim().isEmpty()) {
            edtBranchName.setError(getString(R.string.err_msg_branch_name));
            requestFocus(edtBranchName);
            return false;
        } else {
            return true;
        }
    }

    private boolean validateBranchAddress() {
        if (edtBranchAddress.getText().toString().trim().isEmpty()) {
            edtBranchAddress.setError(getString(R.string.err_msg_branch_address));
            requestFocus(edtBranchAddress);
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
