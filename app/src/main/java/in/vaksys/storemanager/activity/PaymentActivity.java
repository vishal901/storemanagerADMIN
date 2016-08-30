package in.vaksys.storemanager.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.vaksys.storemanager.R;
import in.vaksys.storemanager.adapter.Set_Product_Pay_Adapter;
import in.vaksys.storemanager.extra.ApiClient;
import in.vaksys.storemanager.extra.ApiInterface;
import in.vaksys.storemanager.extra.AppConfig;
import in.vaksys.storemanager.extra.PreferenceHelper;
import in.vaksys.storemanager.model.product;
import in.vaksys.storemanager.model.set_product_data;
import in.vaksys.storemanager.response.Create_Order;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lenovoi3 on 8/17/2016.
 */
public class PaymentActivity extends AppCompatActivity {

    ArrayList<set_product_data> classObject;
    ArrayList<product> productArrayList = new ArrayList<>();
    product pro_data;
    @Bind(R.id.rec_set_product)
    RecyclerView recSetProduct;
    @Bind(R.id.txt_coupan_name_payment)
    TextView txtCoupanNamePayment;
    @Bind(R.id.txt_coupan_price_payment)
    TextView txtCoupanPricePayment;
    @Bind(R.id.txt_total_payment)
    TextView txtTotalPayment;
    @Bind(R.id.radio_cash)
    RadioButton radioCash;
    @Bind(R.id.radio_credit)
    RadioButton radioCredit;
    @Bind(R.id.radio_paypal)
    RadioButton radioPaypal;
    @Bind(R.id.btn_pay)
    Button btnPay;
    @Bind(R.id.pb_pay_payment)
    ProgressBar pbPayPayment;
    private Set_Product_Pay_Adapter adapter;
    PreferenceHelper preferenceHelper;

    private String branchid, customer_id, coupanid, payid, productid, cname, cprice, total, paymentname;
    private String apikey;
    private String branch_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remind_me);
        ButterKnife.bind(this);

        preferenceHelper = new PreferenceHelper(PaymentActivity.this, "type");
        apikey = preferenceHelper.LoadStringPref(AppConfig.PREF_USER_KEY, "");
        branch_id = preferenceHelper.LoadStringPref(AppConfig.PREF_BRANCH_ID, "");

        try {
            // Get the Bundle Object
            Bundle bundleObject = getIntent().getExtras();
            classObject = (ArrayList<set_product_data>) bundleObject.getSerializable("key");

            branchid = bundleObject.getString("branchid");
            customer_id = bundleObject.getString("customer_id");
            coupanid = bundleObject.getString("coupanid");
            payid = bundleObject.getString("payid");
            productid = bundleObject.getString("productid");
            cname = bundleObject.getString("cname");

            cprice = bundleObject.getString("cprice");
            total = bundleObject.getString("total");
            paymentname = bundleObject.getString("paymentname");


            ApiClient.showLog("All Data", branchid + "\n" +
                    customer_id + "\n" +
                    coupanid + "\n" +
                    payid + "\n" +
                    productid + "\n" +
                    cname + "\n" +
                    cprice + "\n" +
                    total + "\n" +
                    paymentname + "\n");

            for (set_product_data data : classObject) {

                pro_data = new product();
                pro_data.setProduct_id(data.getProduct_id());
                pro_data.setProduct_name(data.getProduct_name());
                pro_data.setProduct_price(data.getProduct_price());
                productArrayList.add(pro_data);

            }


            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(PaymentActivity.this);
            recSetProduct.setLayoutManager(layoutManager);
            adapter = new Set_Product_Pay_Adapter(PaymentActivity.this, productArrayList);
            recSetProduct.setAdapter(adapter);

            if (paymentname.equalsIgnoreCase("PAYPAL")) {

                radioPaypal.setEnabled(true);
            }
            if (paymentname.equalsIgnoreCase("CASH")) {

                radioCash.setEnabled(true);
            }
            if (paymentname.equalsIgnoreCase("CREDIT")) {

                radioCredit.setEnabled(true);
            }

            txtCoupanNamePayment.setText(cname);
            txtCoupanPricePayment.setText(cprice);
            txtTotalPayment.setText(total);

            btnPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    PlaceOrder_Network_Call(apikey, branch_id, customer_id, productid, coupanid, total, paymentname);
                }
            });


//            for (int index = 0; index < classObject.size(); index++) {
//
//                set_product_data Object = classObject.get(index);
//                Toast.makeText(getApplicationContext(), "Id is :" + Object.getProduct_name(), Toast.LENGTH_SHORT).show();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void PlaceOrder_Network_Call(String apikey, String branchid, String customer_id, String productid, String coupanid, String total, String paymentname) {

        //// TODO: 8/16/2016 errror 500 server errror php

        pbPayPayment.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Create_Order> create_orderCall = apiInterface.ADD_ORDER_RESPONSE_CALL(branchid, customer_id, productid, coupanid, total, paymentname, apikey);

        create_orderCall.enqueue(new Callback<Create_Order>() {
            @Override
            public void onResponse(Call<Create_Order> call, Response<Create_Order> response) {

                ApiClient.showLog("code", "" + response.code());
                if (response.code() == 200) {

                    if (!response.body().isError()) {
                        pbPayPayment.setVisibility(View.GONE);
                        Toast.makeText(PaymentActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        pbPayPayment.setVisibility(View.GONE);
                        Toast.makeText(PaymentActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    pbPayPayment.setVisibility(View.GONE);
                    Toast.makeText(PaymentActivity.this, "Something Worng Data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Create_Order> call, Throwable t) {
                pbPayPayment.setVisibility(View.GONE);

                Toast.makeText(PaymentActivity.this, "No Internet Access", Toast.LENGTH_SHORT).show();
            }
        });


    }

}
