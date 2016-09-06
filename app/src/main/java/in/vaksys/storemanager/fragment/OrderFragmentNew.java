package in.vaksys.storemanager.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.vaksys.storemanager.R;
import in.vaksys.storemanager.activity.Create_Order_Activity;
import in.vaksys.storemanager.adapter.Get_Order_Adapter;
import in.vaksys.storemanager.extra.ApiClient;
import in.vaksys.storemanager.extra.ApiInterface;
import in.vaksys.storemanager.extra.AppConfig;
import in.vaksys.storemanager.extra.PreferenceHelper;
import in.vaksys.storemanager.model.getorederlist;
import in.vaksys.storemanager.response.GetOrederList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lenovoi3 on 9/1/2016.
 */
public class OrderFragmentNew extends Fragment implements View.OnClickListener {

    @Bind(R.id.rec_get_orderlist_new)
    RecyclerView recGetOrderlistNew;
    @Bind(R.id.pb_get_product_all_new)
    ProgressBar pbGetProductAllNew;
    @Bind(R.id.reletivie_hide)
    RelativeLayout reletivieHide;
    @Bind(R.id.fab_add_order)
    FloatingActionButton fabAddOrder;
    private PreferenceHelper preferenceHelper;
    private String apikey, customer_id, branchid, type;
    ArrayList<getorederlist> getorederlists = new ArrayList<>();
    private getorederlist getorederlistdata;
    private Get_Order_Adapter get_order_adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_order1, container, false);
        ButterKnife.bind(this, rootView);

        preferenceHelper = new PreferenceHelper(getActivity(), "type");
        apikey = preferenceHelper.LoadStringPref(AppConfig.PREF_USER_KEY, "");
        branchid = preferenceHelper.LoadStringPref(AppConfig.PREF_BRANCH_ID, "");
        type = preferenceHelper.LoadStringPref(AppConfig.PREF_USER_TYPE, "");

        fabAddOrder.setOnClickListener(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recGetOrderlistNew.setLayoutManager(layoutManager);

        Get_OrderList_Network_Call(apikey);


        return rootView;
    }

    private void Get_OrderList_Network_Call(String apikey) {
        pbGetProductAllNew.setVisibility(View.VISIBLE);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<GetOrederList> getOrederListCall = apiInterface.GET_ORDER_LIST(apikey);

        getOrederListCall.enqueue(new Callback<GetOrederList>() {
            @Override
            public void onResponse(Call<GetOrederList> call, Response<GetOrederList> response) {

                ApiClient.showLog("code", "" + response.code());
                if (response.code() == 200) {

                    //// TODO: 8/16/2016 get all order list api
                    List<GetOrederList.DataBean> as = response.body().getData();
                    if (!response.body().isError()) {
                        pbGetProductAllNew.setVisibility(View.GONE);
                        for (GetOrederList.DataBean a : as) {

                            getorederlistdata = new getorederlist();
                            getorederlistdata.setUid(a.getOrderId());
                            getorederlistdata.setCustomername(a.getCustomerName());
                            getorederlistdata.setProcut_count(a.getProductCount());
                            getorederlistdata.setCoupan_count(a.getCouponCount());
                            getorederlistdata.setTotal(a.getOrderTotal());
                            getorederlistdata.setDate(a.getOrderDate());

                            getorederlists.add(getorederlistdata);

                        }

                        get_order_adapter = new Get_Order_Adapter(getActivity(), getorederlists);
                        recGetOrderlistNew.setAdapter(get_order_adapter);

                    } else {
                        pbGetProductAllNew.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    pbGetProductAllNew.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Something Worng Data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetOrederList> call, Throwable t) {
                pbGetProductAllNew.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "No InterNet Access", Toast.LENGTH_SHORT).show();

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

        Intent intent = new Intent(getActivity(), Create_Order_Activity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
