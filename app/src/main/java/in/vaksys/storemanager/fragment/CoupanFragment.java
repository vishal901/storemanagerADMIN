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
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.vaksys.storemanager.R;
import in.vaksys.storemanager.adapter.Get_Coupan_List_Adapter;
import in.vaksys.storemanager.extra.ApiClient;
import in.vaksys.storemanager.extra.ApiInterface;
import in.vaksys.storemanager.extra.AppConfig;
import in.vaksys.storemanager.extra.DividerItemDecoration;
import in.vaksys.storemanager.extra.PreferenceHelper;
import in.vaksys.storemanager.model.coupan;
import in.vaksys.storemanager.response.AddCuopan;
import in.vaksys.storemanager.response.GetCoupanList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lenovoi3 on 7/27/2016.
 */
public class CoupanFragment extends Fragment implements View.OnClickListener {

    @Bind(R.id.rec_get_coupan)
    RecyclerView recGetCoupan;
    @Bind(R.id.pb_get_coupan)
    ProgressBar pbGetCoupan;
    private Dialog dialog;
    @Bind(R.id.fab_coupon)
    FloatingActionButton fabCoupon;
    private String scoupanname, scoupanprice, branch_id, apikey;
    private EditText edtcoupanname, edtcoupanprice;
    private Button btnsavecoupan;
    PreferenceHelper preferenceHelper;
    ProgressBar progressBar;
    private ArrayList<coupan> addcoupanlist;
    private Get_Coupan_List_Adapter get_coupan_adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_coupons_main, container, false);

        ButterKnife.bind(this, rootView);

        preferenceHelper = new PreferenceHelper(getActivity(), "type");
        branch_id = preferenceHelper.LoadStringPref(AppConfig.PREF_BRANCH_ID, "");
        apikey = preferenceHelper.LoadStringPref(AppConfig.PREF_USER_KEY, "");

        Get_Coupan_List(apikey);
        fabCoupon.setOnClickListener(this);


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
        dialog.setContentView(R.layout.dailog_add_coupons);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        edtcoupanname = (EditText) dialog.findViewById(R.id.edt_coupan_name);
        edtcoupanprice = (EditText) dialog.findViewById(R.id.edt_coupan_price);
        btnsavecoupan = (Button) dialog.findViewById(R.id.btn_save_coupan);
        progressBar = (ProgressBar) dialog.findViewById(R.id.pb_create_cuopan);
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

                Add_Coupan_Network_Call(scoupanname, scoupanprice, branch_id, apikey);
            }
        });


    }

    private void Add_Coupan_Network_Call(String sproductname, String sproductprice, String branch_id, final String apikey) {

        progressBar.setVisibility(View.VISIBLE);
        btnsavecoupan.setEnabled(true);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<AddCuopan> addProductCall = apiInterface.ADD_COUPAN_RESPONSE_CALL(sproductname, sproductprice, branch_id, apikey);

        addProductCall.enqueue(new Callback<AddCuopan>() {
            @Override
            public void onResponse(Call<AddCuopan> call, Response<AddCuopan> response) {

                ApiClient.showLog("code", "" + response.code());
                if (response.code() == 200) {

                    progressBar.setVisibility(View.GONE);
                    btnsavecoupan.setEnabled(false);

                    if (!response.body().isError()) {

                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.hide();
                        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                        Get_Coupan_List(apikey);
                    } else {
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    progressBar.setVisibility(View.GONE);
                    btnsavecoupan.setEnabled(false);
                    Toast.makeText(getActivity(), "Something Worng Data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddCuopan> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                btnsavecoupan.setEnabled(false);
                Toast.makeText(getActivity(), "No Internet Accesss", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Get_Coupan_List(String apikey) {

        pbGetCoupan.setVisibility(View.VISIBLE);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<GetCoupanList> getBranchAdminCall = apiInterface.GET_ALL_COUPAN(apikey);

        getBranchAdminCall.enqueue(new Callback<GetCoupanList>() {
            @Override
            public void onResponse(Call<GetCoupanList> call, Response<GetCoupanList> response) {

                ApiClient.showLog("code", "" + response.code());
                if (response.code() == 200) {
                    pbGetCoupan.setVisibility(View.GONE);

                    List<GetCoupanList.DataBean> as = response.body().getData();
                    if (!response.body().isError()) {

                        addcoupanlist = new ArrayList<>();

                        for (GetCoupanList.DataBean a : as) {

                            coupan data = new coupan();
                            data.setCoupan_id((a.getId()));
                            data.setCoupan_name(a.getCouponName());
                            data.setCoupan_price(a.getPrice());
                            addcoupanlist.add(data);

                        }
                        recGetCoupan.addItemDecoration(new DividerItemDecoration(getActivity()));

                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        recGetCoupan.setLayoutManager(layoutManager);
                        get_coupan_adapter = new Get_Coupan_List_Adapter(getActivity(), addcoupanlist);
                        recGetCoupan.setAdapter(get_coupan_adapter);
                        get_coupan_adapter.notifyDataSetChanged();

                        //    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }

                } else {
                    pbGetCoupan.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Somthing worng", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<GetCoupanList> call, Throwable t) {
                pbGetCoupan.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "No Internet Accesss", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean validateCoupanName() {
        if (edtcoupanname.getText().toString().trim().isEmpty()) {
            edtcoupanname.setError(getString(R.string.err_msg_coupan_name));
            requestFocus(edtcoupanname);
            return false;
        } else {
            return true;
        }
    }

    private boolean validateCoupanPrice() {
        if (edtcoupanprice.getText().toString().trim().isEmpty()) {
            edtcoupanprice.setError(getString(R.string.err_msg_coupan_price));
            requestFocus(edtcoupanprice);
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
