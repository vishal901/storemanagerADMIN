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
import in.vaksys.storemanager.adapter.Get_Product_Adapter;
import in.vaksys.storemanager.extra.ApiClient;
import in.vaksys.storemanager.extra.ApiInterface;
import in.vaksys.storemanager.extra.AppConfig;
import in.vaksys.storemanager.extra.DividerItemDecoration;
import in.vaksys.storemanager.extra.PreferenceHelper;
import in.vaksys.storemanager.model.product;
import in.vaksys.storemanager.response.AddProduct;
import in.vaksys.storemanager.response.GetAllProduct;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lenovoi3 on 7/27/2016.
 */
public class ProductsFragment extends Fragment implements View.OnClickListener {


    @Bind(R.id.fab_product)
    FloatingActionButton fabProduct;
    @Bind(R.id.rec_get_product_list)
    RecyclerView recGetProductList;
    @Bind(R.id.pb_get_product_list)
    ProgressBar pbGetProductList;

    private Dialog dialog;
    PreferenceHelper preferenceHelper;
    private String user_type;
    private EditText edtproductname, edtproductprice;
    private Button btnsaveproduct;
    private String sproductname, sproductprice, branch_id, apikey;
    private ArrayList<product> addproductlist;
    private Get_Product_Adapter get_product_adapter;
    private ProgressBar progressproduct;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_product_main, container, false);

        ButterKnife.bind(this, rootView);
        preferenceHelper = new PreferenceHelper(getActivity(), "type");
        user_type = preferenceHelper.LoadStringPref(AppConfig.PREF_USER_TYPE, "");
        branch_id = preferenceHelper.LoadStringPref(AppConfig.PREF_BRANCH_ID, "");
        apikey = preferenceHelper.LoadStringPref(AppConfig.PREF_USER_KEY, "");
        ApiClient.showLog("type", user_type);

        Get_Product_List(apikey);

        if (user_type.equalsIgnoreCase("0")) {
            //admin login
            fabProduct.setVisibility(View.GONE);

        }
        //stroemanager login
        fabProduct.setOnClickListener(this);


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
        dialog.setContentView(R.layout.dailog_add_product);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();


        edtproductname = (EditText) dialog.findViewById(R.id.edt_add_product_name);
        edtproductprice = (EditText) dialog.findViewById(R.id.edt_add_product_price);
        btnsaveproduct = (Button) dialog.findViewById(R.id.btn_save_product);

        progressproduct = (ProgressBar) dialog.findViewById(R.id.pb_create_product);

        btnsaveproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validateProductName()) {
                    return;
                }
                if (!validateProductPrice()) {
                    return;
                }

                sproductname = edtproductname.getText().toString().trim();
                sproductprice = edtproductprice.getText().toString().trim();

                AddProduct_Network_Call(sproductname, sproductprice, branch_id, apikey);
            }
        });

    }

    private void AddProduct_Network_Call(String sproductname, String sproductprice, String branch_id, final String apikey) {

        progressproduct.setVisibility(View.VISIBLE);
        btnsaveproduct.setEnabled(true);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<AddProduct> addProductCall = apiInterface.ADD_PRODUCT_RESPONSE_CALL(sproductname, sproductprice, branch_id, apikey);

        addProductCall.enqueue(new Callback<AddProduct>() {
            @Override
            public void onResponse(Call<AddProduct> call, Response<AddProduct> response) {

                ApiClient.showLog("code", "" + response.code());
                if (response.code() == 200) {

                    if (!response.body().isError()) {

                        progressproduct.setVisibility(View.GONE);
                        btnsaveproduct.setEnabled(false);

                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.hide();
                        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                        Get_Product_List(apikey);

                    } else {
                        progressproduct.setVisibility(View.GONE);
                        btnsaveproduct.setEnabled(false);

                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    progressproduct.setVisibility(View.GONE);
                    btnsaveproduct.setEnabled(false);

                    Toast.makeText(getActivity(), "Something Worng Data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddProduct> call, Throwable t) {
                progressproduct.setVisibility(View.GONE);
                btnsaveproduct.setEnabled(false);
                Toast.makeText(getActivity(), "No Internet Access", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Get_Product_List(String apikey) {

        pbGetProductList.setVisibility(View.VISIBLE);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<GetAllProduct> getBranchAdminCall = apiInterface.GET_ALL_PRODUCT(apikey);

        getBranchAdminCall.enqueue(new Callback<GetAllProduct>() {
            @Override
            public void onResponse(Call<GetAllProduct> call, Response<GetAllProduct> response) {

                ApiClient.showLog("code", "" + response.code());
                if (response.code() == 200) {
                    pbGetProductList.setVisibility(View.GONE);

                    List<GetAllProduct.DataBean> as = response.body().getData();
                    if (!response.body().isError()) {

                        addproductlist = new ArrayList<product>();

                        for (GetAllProduct.DataBean a : as) {

                            product data = new product();
                            data.setProduct_id((a.getId()));
                            data.setProduct_name(a.getProductName());
                            data.setProduct_price(a.getPrice());
                            addproductlist.add(data);

                        }

                        recGetProductList.addItemDecoration(new DividerItemDecoration(getActivity()));
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        recGetProductList.setLayoutManager(layoutManager);
                        get_product_adapter = new Get_Product_Adapter(getActivity(), addproductlist);
                        recGetProductList.setAdapter(get_product_adapter);
                        get_product_adapter.notifyDataSetChanged();

                        //    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }

                } else {
                    pbGetProductList.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Somthing Worng Response", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<GetAllProduct> call, Throwable t) {
                pbGetProductList.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "No Internet Accesss", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean validateProductName() {
        if (edtproductname.getText().toString().trim().isEmpty()) {
            edtproductname.setError(getString(R.string.err_msg_product_name));
            requestFocus(edtproductname);
            return false;
        } else {
            return true;
        }
    }

    private boolean validateProductPrice() {
        if (edtproductprice.getText().toString().trim().isEmpty()) {
            edtproductprice.setError(getString(R.string.err_msg_product_price));
            requestFocus(edtproductprice);
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
